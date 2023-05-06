package com.learncamel.route;

import java.security.KeyStore.Entry.Attribute;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.naming.NamingEnumeration;
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

		
		/*
		 * onException(Exception.class).process(new Processor() {
		 * 
		 * public void process(Exchange exchange) throws Exception {
		 * log.info("handling ex 1"); }
		 * }).log("Exception body ${body} ").handled(true).to("file:data/error");
		 */
		
		from("timer:hello?period=30s").process(new Processor() {

			@Override
			public void process(Exchange exchange) throws Exception {

				exchange.getIn().setBody("(ou=scientists)"); // working

			}
		}).to("ldap:ldapserver?base=dc=example,dc=com&scope=subtree").process(new Processor() {

			@Override
			public void process(Exchange exchange) throws Exception {
				// TODO Auto-generated method stub
				Collection<SearchResult> data = exchange.getIn().getBody(Collection.class);
				System.out.println("data: " + data);

				Iterator resultIter = data.iterator();
				SearchResult searchResult = (SearchResult) resultIter.next();
				Attributes attributes = searchResult.getAttributes();
				System.out.println("uniqueMember + " + attributes.get("uniqueMember"));
				List l = new ArrayList();
				for (int i = 0; i < attributes.get("uniqueMember").size(); i++) {
					String uniqueMember = (String) attributes.get("uniqueMember").get(i);
					l.add(uniqueMember);
				}
				exchange.getIn().setBody(l);

				//
			}
		}).split(body()).to("direct:fetchUidfromLdap");

		from("direct:fetchUidfromLdap").process(new Processor() {

			@Override
			public void process(Exchange exchange) throws Exception {
				// TODO Auto-generated method stub
				String uid = exchange.getIn().getBody(String.class);
				String s[] = uid.split(",");
				String string1[] = s[0].split("=");
				// System.out.println("string1 " + string1[1]);
				exchange.getIn().setBody("uid=" + string1[1]);

			}
		}).to("ldap:ldapserver?base=dc=example,dc=com&scope=subtree").log("body ${body}").to("direct:ldapresponse");

		from("direct:ldapresponse").process(new Processor() {

			@Override
			public void process(Exchange exchange) throws Exception {
				// TODO Auto-generated method stub
				StringBuilder sbUidDetails = new StringBuilder();
				Collection<SearchResult> data = exchange.getIn().getBody(Collection.class);
				Iterator resultIter = data.iterator();
				SearchResult searchResult = (SearchResult) resultIter.next();
				Attributes attributes = searchResult.getAttributes();
				javax.naming.directory.Attribute uidAttr = attributes.get("uid");
				String uid = (String) uidAttr.get();
				javax.naming.directory.Attribute mailAttr = attributes.get("mail");
				String mail = (String) mailAttr.get();

				javax.naming.directory.Attribute telephoneNumberAttr = attributes.get("cn");
				if(telephoneNumberAttr ==null)
					return;
				String telephoneNumber = (String) telephoneNumberAttr.get();
				if(telephoneNumber == null)
					telephoneNumber = "NA";
				
				sbUidDetails.append(uid);
				sbUidDetails.append(",");
				sbUidDetails.append(mail);
			    sbUidDetails.append(",");
				sbUidDetails.append(telephoneNumber);
				System.out.println("sbUidDetails + " + sbUidDetails);
				/* NamingEnumeration<?> attributesEnum = null;
				 
				javax.naming.directory.Attribute attribute = attributes.get("telephoneNumber");
				    if (attribute == null) return;
				    attributesEnum = attribute.getAll();
				    Collections.list(attributesEnum).stream().map(Object::toString);*/
				    exchange.getIn().setBody(sbUidDetails+"\n");
				    
			}
		})
		.log("body ${body}")
		 .to("file:data/output/?fileName=ldap.csv&fileExist=Append&charset=utf-8&Noop=false");
	}
}
