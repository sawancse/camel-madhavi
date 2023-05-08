package com.learncamel.bean;
import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.springframework.stereotype.Component;


@Component
public class LdapUserStrategy implements AggregationStrategy {
    
    @Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
        StringBuilder sb = new StringBuilder();
    	//String s="Hello"; String t="world"; String new = s+t; //Hello WOrld
           if (oldExchange == null) {
               
               String newItem= newExchange.getIn().getBody(String.class);
               System.out.println("newItem: " + newItem);
               newExchange.getIn().setBody(newItem);
                // the first time we aggregate we only have the new exchange,
                // so we just return it
               newExchange.setProperty(Exchange.AGGREGATION_COMPLETE_ALL_GROUPS, true);
                return newExchange;
            }
           
            String order = oldExchange.getIn().getBody(String.class);
            String order1= newExchange.getIn().getBody(String.class);
     
          //  System.out.println("Aggregate old items: " + order);
           // System.out.println("Aggregate new item: " + order1);
            sb.append(order+"\n");
            sb.append(order1);
            oldExchange.getIn().setBody(sb);
            // return old as this is the one that has all the orders gathered until now
            System.out.println("oldExchange after agg: " +oldExchange.getIn().getBody());
            return oldExchange;
    }


}
