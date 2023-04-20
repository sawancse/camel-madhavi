package com.learncamel.route;

import java.security.KeyStore.Entry.Attribute;
import java.util.Collection;
import java.util.Iterator;

import javax.naming.directory.Attributes;
import javax.naming.directory.SearchResult;

import org.apache.camel.Exchange;
import org.apache.camel.Header;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

/*https://camel.apache.org/components/3.20.x/ldap-component.html*/
@Component
public class SimpleCamelRoute extends RouteBuilder {
	@Override
	public void configure() throws Exception {

		from("timer:hello?period=30s").process(new Processor() {

			@Override
			public void process(Exchange exchange) throws Exception {
				// TODO Auto-generated method stub
				//.getIn().setBody("(ou=mathematicians)");
				exchange.getIn().setBody("(objectClass=*)");
				// exchange.getIn().setHeader("objectClass","person");
				// exchange.getIn().setHeader("sn","Euler");
			}
		})
		//.to("direct:ldapcall")
		.to("ldap:ldapserver?base=uid=pasteur,dc=example,dc=com&scope=subtree")
		//.to("ldap:ldapserver?base=dc=example,dc=com&scope=subtree")  
		.log("ldap res ${body}")
		.process(new Processor() {
			
			@Override
			public void process(Exchange exchange) throws Exception {
				// TODO Auto-generated method stub
				 Collection<SearchResult> data =  exchange.getIn().getBody(Collection.class);
				 System.out.println("data: " + data);
				 
				 Iterator resultIter = data.iterator();
				  SearchResult searchResult = (SearchResult) resultIter.next();
				  Attributes attributes = searchResult.getAttributes();
				  javax.naming.directory.Attribute deviceCNAttr = attributes.get("cn");
				  javax.naming.directory.Attribute deviceMailAttr = attributes.get("mail");
				  javax.naming.directory.Attribute deviceTelphoneAttr = attributes.get("telephoneNumber");
				  String cn = (String) deviceCNAttr.get();attributes.get("cn");
				  String mail = (String) deviceMailAttr.get();attributes.get("mail");
				  String telephoneNumber = (String) deviceTelphoneAttr.get();attributes.get("telephoneNumber");
				  String output= cn +"|" + mail+"|"+telephoneNumber;
				  System.out.println(output);
				  exchange.getIn().setBody(output);
				
			}
		})
		  .to("file:data/output?fileName=ldap.csv");


	}
}
