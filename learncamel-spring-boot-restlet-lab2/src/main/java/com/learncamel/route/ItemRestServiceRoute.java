package com.learncamel.route;

import com.learncamel.constant.CommonConstant;
import com.learncamel.domain.Item;
import com.learncamel.domain.Response;
import com.learncamel.exception.ValidationException;
import com.learncamel.process.BuildSQLProcessor;
import com.learncamel.process.SuccessProcessor;
import com.learncamel.sql.ItemSql;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.gson.GsonDataFormat;
import org.apache.camel.dataformat.bindy.csv.BindyCsvDataFormat;
import org.apache.camel.spi.DataFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;

import java.math.BigDecimal;
import java.util.logging.Logger;

@Component
@Slf4j
public class ItemRestServiceRoute  extends RouteBuilder{


    @Autowired
    Environment environment;

    @Qualifier("dataSource")
    @Autowired
    DataSource dataSource;

    @Autowired
    BuildSQLProcessor buildSQLProcessor;
    
    @Autowired
    Response response;
    
    
    @Override
    public void configure() throws Exception {

    	log.info("Starting the Camel Route");
        GsonDataFormat itemFormat = new GsonDataFormat(Item.class);
        GsonDataFormat responseFormat = new GsonDataFormat(Response.class);
        DataFormat bindy = new BindyCsvDataFormat(Item.class);
        
    	onException(ValidationException.class)
    	.process(new Processor() {

			@Override
			public void process(Exchange exchange) throws Exception {
				
				System.out.println(exchange.getIn().getExchange().getException().getMessage());
				
				response.setErrorCode(10001);
				response.setErrorMessage(exchange.getIn().getExchange().getException().getMessage());
				response.setMessage("Failed");
				
				exchange.getIn().setBody(response);

			}
		})
    	.log("response: ${body} !")
    	.marshal(responseFormat).end();
		
    	
        

      //  rest("/{{api.ver}}/item").id("item-route-id").description("PUT URL item Route").consumes("application/json")
	/*	.produces("application/json,application/xml").put("/createitem").outType(String.class).responseMessage()
		.code(HttpStatus.OK.value()).message(HttpStatus.OK.getReasonPhrase()).responseModel(String.class)
		.endResponseMessage().route().routeId("createitem-route-id").convertBodyTo(String.class).unmarshal(itemFormat).to("direct:createItem").end().endRest();
*/
        from("restlet:http://localhost:8083/item?restletMethods=POST").routeId("itemPostRoute")
        .log("Received Body is ${body}")
        .convertBodyTo(String.class)
        .unmarshal(itemFormat)
        .log("Unmarshaled record is ${body}")
      //  .process(buildSQLProcessor)
        .to("jpa://com.learncamel.domain.Item").log("Persisted Item Record to DB: ${body}!")
       // .to("{{selectNode}}")
        .convertBodyTo(String.class)
        .log("Inserted Country is ${body}");
        

        from("restlet:http://localhost:8083/item?restletMethods=GET").routeId("item-get-route-id")
        .log("Received Body is ${body}")
        .convertBodyTo(String.class)
        .to("direct:itemLookUpBySku").marshal(itemFormat).log("Persisted Item Record to DB: ${body}!");
        
		from(CommonConstant.creteItemRoute).process(buildSQLProcessor).log("Persisted Item Record to DB: ${body}!").to("jpa://com.learncamel.domain.Item").end();
		
		
		from("direct:itemLookUpBySku").process(new Processor() {

			@Override
			public void process(Exchange exchange) throws Exception {
				String sku = exchange.getIn().getHeader("sku", String.class);
				if (StringUtils.isEmpty(sku)) {
					throw new ValidationException("Missing param 'sku'!");
				}
				log.info("jpa query : " + ItemSql.findItemBySku());

			}
		}).toD(ItemSql.findItemBySku())
		.log("Found Item Record for an sku no from DB: ${body}!").end();

		

    }
}
