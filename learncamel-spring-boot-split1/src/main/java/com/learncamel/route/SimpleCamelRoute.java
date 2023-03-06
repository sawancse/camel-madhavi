package com.learncamel.route;

import org.apache.camel.Exchange;
import org.apache.camel.Header;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import com.learncamel.bean.MyBean;


@Component
public class SimpleCamelRoute  extends RouteBuilder{
    @Override
    public void configure() throws Exception {
    	 from("file:data/input?delete=true&readLock=none").process(new Processor() {
 			
 			@Override
 			public void process(Exchange exchange) throws Exception {
 				// TODO Auto-generated method stub
 				String body = exchange.getIn().getBody().toString();
 				exchange.setProperty("foo", "foo");
 				exchange.getIn().setHeader("level", "gold");
 				log.info("body is : " + body);
 			}
 		})
    	 .to("direct:start");
         
         
         from("direct:start") 
     	.filter().method(MyBean.class, "isGoldCustomerExchange")
     		.log("after filter body is ${body}")
     	.end()
     	.split().tokenize("\n")
     	.log("split body is ${body}")
         .to("direct:x") .to("direct:y") .to("direct:z"); 

         from("direct:x")
         .log("in route x body message ${body}");
         
         from("direct:y")
         .log("in route x body message ${body}");
         
         from("direct:z")
         .log("in route x body message ${body}");

    }
}
