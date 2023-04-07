package com.learncamel.processor;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import com.learncamel.exception.CamelCustomException;

public class MyProcessor implements Processor {

    public void process(Exchange exchange) throws Exception {
        throw new CamelCustomException("Exception thrown");
    }

}