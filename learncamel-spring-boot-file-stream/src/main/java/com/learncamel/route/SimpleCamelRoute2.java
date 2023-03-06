/*
 * package com.learncamel.route;
 * 
 * import org.apache.camel.builder.RouteBuilder; import
 * org.springframework.stereotype.Component;
 * 
 * 
 * @Component public class SimpleCamelRoute2 extends RouteBuilder{
 * 
 * @Override public void configure() throws Exception {
 * 
 * from("file:data/input?delete=true&readLock=none") .to("file:data/output");
 * 
 * 
 * } }
 */