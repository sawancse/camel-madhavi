package com.learncamel.process;

import com.learncamel.domain.Item;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class BuildSQLProcessor implements org.apache.camel.Processor {
    @Override
    public void process(Exchange exchange) throws Exception {

        Item item = (Item) exchange.getIn().getBody();
        System.out.println(" Item in Processor is : " + item);
      //  Item item1=new Item();
        if(item.getTxType().equals("ADD")){
           
			
			/*
			 * item1.setItemDescription(item.getItemDescription());
			 * item1.setSku(item.getSku()); item1.setPrice(item.getPrice());
			 * item1.setTxType(item.getTxType()); item1.setItemId(item.getItemId());
			 */
			 

        }else if(item.getTxType().equals("UPDATE")){

        }else if(item.getTxType().equals("DELETE")){

        }
        exchange.getIn().setBody(item);

    }
}
