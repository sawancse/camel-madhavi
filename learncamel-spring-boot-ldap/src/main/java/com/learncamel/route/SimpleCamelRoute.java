package com.learncamel.route;

import org.apache.camel.Exchange;
import org.apache.camel.Header;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

/*https://camel.apache.org/components/3.20.x/ldap-component.html*/
@Component
public class SimpleCamelRoute extends RouteBuilder {
	@Override
	public void configure() throws Exception {

		from("timer:hello?period=20s").process(new Processor() {

			@Override
			public void process(Exchange exchange) throws Exception {
				// TODO Auto-generated method stub

			}
		}).to("direct:ldapcall")
/*		.to("ldap:ldapserver?base=ou=mygroup,ou=groups,ou=system,member=uid=huntc,ou=users,ou=system").log("ldap res ${body}");
*/		///.to("ldap:ldapserver?base=ou=mathematicians").log("ldap res ${body}");   //step-1
		.to("ldap:ldapserver?base=uid=boyle,dc=example,dc=com").log("ldap res ${body}");   //step-1
		//writing to csv
		//
		
//"ldap:ldapserver?base=ou=mygroup,ou=groups,ou=system",
	//    "(member=uid=huntc,ou=users,ou=system)"

		from("direct:ldapcall").process(new Processor() {

			@Override
			public void process(Exchange exchange) throws Exception {
				// TODO Auto-generated method stub

			}
		});

	}
}
