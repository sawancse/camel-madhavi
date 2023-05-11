package com.learncamel;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jasypt.JasyptPropertiesParser;
import org.apache.camel.component.properties.PropertiesComponent;
import org.apache.camel.impl.DefaultCamelContext;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.learncamel.route.SimpleRouteBuilder;

@SpringBootApplication
public class LearncamelSpringBootApplication {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(LearncamelSpringBootApplication.class, args);
		JasyptPropertiesParser jasypt = new JasyptPropertiesParser();
		// and set the master password
		jasypt.setPassword("secret");

		// create the properties component
		PropertiesComponent pc = new PropertiesComponent();
		pc.setLocation("application.properties");
		// and use the jasypt properties parser so we can decrypt values
		pc.setPropertiesParser(jasypt);
		
		 CamelContext camelContext = new DefaultCamelContext();
		    camelContext.addComponent("properties", pc);
		    camelContext.addRoutes(new RouteBuilder() {
		        @Override
		        public void configure() throws Exception {
		            from("direct:start").transform().simple("Hi ${body} the decrypted password is: ${properties:password}");
		        }
		    });
		    camelContext.start();
		    
		    try {
		        ProducerTemplate producer = camelContext.createProducerTemplate();
		        String result = producer.requestBody("direct:start", "John", String.class);
		        System.out.println("result: " + result);
		        Assert.assertEquals("Hi John the decrypted password is: admin", result.trim());
		    } finally {
		    	//camelContext.close();
		    }
		    
		    
	}
	
}
