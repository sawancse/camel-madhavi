package com.learncamel.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import com.learncamel.bean.Animal;

public class AnimalDeepClonePrepare implements Processor {

		@Override
        public void process(Exchange exchange) throws Exception {
            Animal body = exchange.getIn().getBody(Animal.class);

            // do a deep clone of the body which wont affect when doing multicasting
            Animal clone = body.deepClone();
            exchange.getIn().setBody(clone);
        }
    }