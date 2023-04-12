package com.learncamel.route;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.beanio.BeanIODataFormat;
import org.apache.camel.spi.DataFormat;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class SimpleCamelRoute extends RouteBuilder {


	@SuppressWarnings("unchecked")
	@Override
    public void configure() throws Exception {

    	
    	 // setup beanio data format using the mapping file, loaded from the classpath
    	 DataFormat format = new BeanIODataFormat(
                 "mappings.xml",
                 "contacts");

    	 
    	 // a route which uses the bean io data format to format a CSV data
         // to java objects
    	from("file:data/input?noop=true")
    	.unmarshal(format)  // da.pta to object
    	.log("body - ${body}")
    	
    	.split(body())
        .log("Received body ")
    	.marshal(format) //object ot data
    	 .log("Received marshal body- ${body} ");
       
    	
    	
    	 // convert list of java objects back to flat format
		/*
		 * from("direct:marshal") .marshal(format) .to("mock:beanio-marshal");
		 */
        

    }
}
