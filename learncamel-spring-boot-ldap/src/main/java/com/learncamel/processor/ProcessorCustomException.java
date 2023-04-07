package com.learncamel.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import com.learncamel.exception.GenericCamelCustomException;

public class ProcessorCustomException implements Processor{

    public void process(Exchange exchange) throws Exception {
        throw new GenericCamelCustomException("Custom Exception from ProcessorCustomException.");
    }
}