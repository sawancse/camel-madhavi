package com.learncamel.route;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;


@Component
public class SimpleCamelRoute extends RouteBuilder {
	@Override
	public void configure() throws Exception {

		from("file:data/input")
		.process(new Processor() {
			
			@Override
			public void process(Exchange exchange) throws Exception {
				// TODO Auto-generated method stub
				//String fileName = (String) exchange.getIn().getHeader("camelFileName");
				String fileName = (String) exchange.getIn().getHeader(Exchange.FILE_NAME);
				System.out.println("fileName: " + fileName);
				System.out.println("content type: " +  exchange.getIn().getHeader(Exchange.FILE_CONTENT_TYPE ));
				
				exchange.getIn().setHeader("file:name.ext","csv");
				
			}
		})
		.log("extenstion: ${file:name.ext}")
	    // the exchange pattern is InOnly initially when using a file route
	    .recipientList().constant("direct:${header.file:name.ext}")
	    .end();
		
		
		from("direct:${header.file:name.ext}").log("body: ${body}");

	}
}
