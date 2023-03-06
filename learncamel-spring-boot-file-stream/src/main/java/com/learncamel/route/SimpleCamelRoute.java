package com.learncamel.route;

import org.apache.camel.Exchange;
import org.apache.camel.Header;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;


@Component
public class SimpleCamelRoute  extends RouteBuilder{
    @Override
    public void configure() throws Exception {
//s.txt  -> s.success.txt  data/output
    	//s.txt. -> s.error.txt data/error
        from("timer:hello?period=10s")
                .log("Timer Invoked and the body ${body}")
                .pollEnrich("file:data/input?delete=true&readLock=none")
                .log("${body}")
                .process(new Processor() {
					
					@Override
					public void process(Exchange exchange) throws Exception {
						// TODO Auto-generated method stub
						System.out.println("exchange: " + exchange.getIn().getBody() + exchange.getIn().getHeader("CamelFileName"));
						System.out.println("fileName: " + exchange.getIn().getHeader(Exchange.FILE_NAME));
					}
				})
                .to("log:foo11221").end().to("stream:out")
                .to("file:data/output");


    }
}
