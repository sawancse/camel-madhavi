package com.learncamel.route;

import org.apache.camel.Exchange;
import org.apache.camel.Header;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import com.learncamel.bean.MyBean;
import com.learncamel.exception.CamelCustomException;
import com.learncamel.exception.GenericCamelCustomException;
import com.learncamel.processor.MyProcessor;
import com.learncamel.processor.ProcessorCustomException;
/*https://camel.apache.org/components/3.20.x/ldap-component.html*/
@Component
public class SimpleCamelRoute extends RouteBuilder {
	@Override
	public void configure() throws Exception {

		from("file:data/input?delete=true&readLock=none").process(new Processor() {

			@Override
			public void process(Exchange exchange) throws Exception {
				// TODO Auto-generated method stub

			}
		}).to("direct:ldapcall")
/*		.to("ldap:ldapserver?base=ou=mygroup,ou=groups,ou=system,member=uid=huntc,ou=users,ou=system").log("ldap res ${body}");
*/		.to("ldap:ldapserver?base=ou=mathematicians").log("ldap res ${body}");


		from("direct:ldapcall").process(new Processor() {

			@Override
			public void process(Exchange exchange) throws Exception {
				// TODO Auto-generated method stub

			}
		});

	}
}
