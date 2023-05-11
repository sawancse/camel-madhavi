/*
 * package com.learncamel.route;
 * 
 * import java.util.ArrayList; import java.util.List;
 * 
 * import org.apache.camel.CamelContext; import org.apache.camel.Exchange;
 * import org.apache.camel.Processor; import
 * org.apache.camel.builder.RouteBuilder; import
 * org.apache.camel.component.jasypt.JasyptPropertiesParser; import
 * org.apache.camel.component.properties.PropertiesComponent; import
 * org.apache.camel.impl.DefaultCamelContext; import
 * org.springframework.beans.factory.annotation.Autowired; import
 * org.springframework.core.env.Environment; import
 * org.springframework.stereotype.Component;
 * 
 * @Component public class SimpleRouteBuilder extends RouteBuilder {
 * 
 * 
 * @Autowired Environment environment;
 * 
 * @Override public void configure() throws Exception {
 * 
 * 
 * 
 * 
 * from("direct:start").transform().
 * simple("Hi ${body} the decrypted password is: ${properties:password}"); } }
 */