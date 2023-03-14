package com.learncamel.route;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Header;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import com.learncamel.aggragation.OrderItemStrategy;
import com.learncamel.bean.Item;
import com.learncamel.bean.MyBean;
import com.learncamel.bean.Order;


@Component
public class SimpleCamelRoute  extends RouteBuilder{
    @Override
    public void configure() throws Exception {
    	
    	from("timer:hello?period=50s&repeatCount=1").process(new Processor() {

			@Override
			public void process(Exchange exchange) throws Exception {

				List items = new ArrayList<>();
				items.add(new Item("1", "Camel in Action book", "Book",100.00));
				items.add(new Item("2", "Apple IPhone8", "Phone",200.00));

				Order myOrder = new Order();
				myOrder.setItems(items);

				exchange.getIn().setBody(myOrder);
			}

		}) // .bean(OrderItemStrategy.class);
				.to("direct:processOrder")
				.log("After process ${body}");
         
    	from("direct:processOrder").multicast().to("direct:x", "direct:y", "direct:z");
    	
    	from("direct:x").log("body x ${body}");
    	
    	from("direct:y").log("body y ${body}");
    	
    	from("direct:z").log("body z ${body}");

    }
}
