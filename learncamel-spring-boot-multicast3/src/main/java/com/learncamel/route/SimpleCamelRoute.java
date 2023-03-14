package com.learncamel.route;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Header;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import com.learncamel.aggragation.OrderItemStrategy;
import com.learncamel.bean.Animal;
import com.learncamel.bean.Item;
import com.learncamel.bean.MyBean;
import com.learncamel.bean.Order;
import com.learncamel.processor.AnimalDeepClonePrepare;

@Component
public class SimpleCamelRoute extends RouteBuilder {
	@Override
	public void configure() throws Exception {

		from("timer:hello?period=50s&repeatCount=1").process(new Processor() {

			@Override
			public void process(Exchange exchange) throws Exception {
				// TODO Auto-generated method stub
				Animal animal =new Animal(1, "Dog");
				exchange.getIn().setBody(animal);
			}
		}).to("direct:start").log("After process ${body}");

		from("direct:start").multicast().onPrepare(new AnimalDeepClonePrepare()).to("direct:a").to("direct:b");

		from("direct:a").log("body a ${body}");

		from("direct:b").log("body b ${body}");

	}
}
