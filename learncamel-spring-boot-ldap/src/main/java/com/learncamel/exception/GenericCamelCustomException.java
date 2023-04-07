package com.learncamel.exception;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;


public class GenericCamelCustomException extends Exception {

    private static final long serialVersionUID = 7607128449240157466L;
    public GenericCamelCustomException(String message){
        super(message);
    }
}