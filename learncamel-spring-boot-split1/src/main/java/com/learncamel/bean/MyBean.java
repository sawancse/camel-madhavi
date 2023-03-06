package com.learncamel.bean;

import org.apache.camel.Exchange;
import org.apache.camel.Header;

public class MyBean { 
	public boolean isGoldCustomer(@Header("level") String level)
	 { 
		System.out.println("calling isGoldCustomer m/d");
		return level.equals("gold"); 
	}
	
	 public boolean isGoldCustomerExchange(Exchange exchange) {
		 System.out.println("calling isGoldCustomerExchange m/d");
		 if(exchange.getIn().getHeader("level").equals("gold"))
			 return true;
		return false;
	  }
	 
 } 
