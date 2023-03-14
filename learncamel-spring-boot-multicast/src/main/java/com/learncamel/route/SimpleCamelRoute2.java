package com.learncamel.route;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.learncamel.bean.Item;
import com.learncamel.bean.Order;

@Component
public class SimpleCamelRoute2 extends RouteBuilder {

	@Override
	public void configure() throws Exception {

		from("timer:hello?period=50s&repeatCount=1").process(new Processor() {

			@Override
			public void process(Exchange exchange) throws Exception {

				List items = new ArrayList();
				items.add(new Item("1", "Camel in Action book", "Book",100.00));
				items.add(new Item("2", "Apple IPhone8", "Phone",200.00));

				Order myOrder = new Order();
				myOrder.setItems(items);

				exchange.getIn().setBody(myOrder);
			}

		}) // .bean(OrderItemStrategy.class); .to("direct:processOrder")
		.multicast().parallelProcessing().timeout(300000).to("direct:processOrder","direct:process");
		
		from("direct:process").multicast().to("direct:x", "direct:y", "direct:z");	
	}

}
