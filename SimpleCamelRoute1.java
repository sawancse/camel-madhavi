package com.learncamel.route;

import java.io.IOException;
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
public class SimpleCamelRoute1 extends RouteBuilder {
	@Override
	public void configure() throws Exception {

		onException(IOException.class).handled(true).log("IOException occurred due: ${exception.message}").transform()
				.simple("Error ${exception.message}").to("mock:error");

		onException(Exception.class).process(new Processor() {

			public void process(Exchange exchange) throws Exception {
				StringBuilder sbUidDetails = new StringBuilder();
				String mail = (String) exchange.getProperty("mail");
				sbUidDetails.append(mail);

				exchange.getIn().setBody(sbUidDetails + "\n");
			}
		}).log("Exception body ${body} ").handled(true).to(
				"file:data/error?fileName=$simple{date:now:yyyyMMdd}/ldaperror.csv&fileExist=Append&charset=utf-8&Noop=false");

		from("timer:hello?period=30s").process(new Processor() {

			@Override
			public void process(Exchange exchange) throws Exception {
				List lifOfGroup = new ArrayList();
				lifOfGroup.add("(ou=scientists)");
				lifOfGroup.add("(ou=mathematicians)");
				exchange.getIn().setBody(lifOfGroup); // working

			}
		}).split(body()).log("processing group ${body}")
				.process(new Processor() {
					
					@Override
					public void process(Exchange exchange) throws Exception {
						// TODO Auto-generated method stub
						String groupBody = exchange.getIn().getBody(String.class);
						
						String groupName = groupBody.split("=")[1];
						groupName = groupName.substring(0,groupName.length()-1);
						System.out.println("groupName: " + groupName);
						exchange.setProperty("groupName", groupName);
					}
				})
				.to("ldap:ldapserver?base=dc=example,dc=com&scope=subtree").log("group output body ${body} ")
				.process(new Processor() {

					@Override
					public void process(Exchange exchange) throws Exception {
						// TODO Auto-generated method stub
						Collection<SearchResult> data = exchange.getIn().getBody(Collection.class);
						System.out.println("data " + data);
						Iterator resultIter = data.iterator();
						SearchResult searchResult = (SearchResult) resultIter.next();
						Attributes attributes = searchResult.getAttributes();
						List listOfuniqueMember = new ArrayList();
						for (int i = 0; i < attributes.get("uniquemember").size(); i++) {
							String uniqueMember = (String) attributes.get("uniquemember").get(i);
							listOfuniqueMember.add(uniqueMember);

						}
						exchange.getIn().setBody(listOfuniqueMember);
						System.out.println("listOfuniqueMember " + listOfuniqueMember);
						//
					}
				}).split(body()).parallelProcessing(true).to("direct:fetchUidfromLdap");

		from("direct:fetchUidfromLdap").process(new Processor() {

			@Override
			public void process(Exchange exchange) throws Exception {
				// TODO Auto-generated method stub
				String uid = exchange.getIn().getBody(String.class);
				String s[] = uid.split(",");
				String string1[] = s[0].split("="); 
				System.out.println("string1 " + string1[1]);
				exchange.getIn().setBody("uid=" + string1[1]);

			}
		}).to("ldap:ldapserver?base=dc=example,dc=com&scope=subtree").to("direct:ldapresponse").to(
				"file:data/output/?fileName=$simple{date:now:yyyyMMdd}/ldap.csv&fileExist=Append&charset=utf-8&Noop=false&move=done&readLockTimeout=1200");

		from("direct:ldapresponse").process(new Processor() {

			@Override
			public void process(Exchange exchange) throws Exception {
				// TODO Auto-generated method stub
				StringBuilder sbUidDetails = new StringBuilder();
				Collection<SearchResult> data = exchange.getIn().getBody(Collection.class);
				Iterator resultIter = data.iterator();
				SearchResult searchResult = (SearchResult) resultIter.next();
				Attributes attributes = searchResult.getAttributes();

				NamingEnumeration<String> ids = attributes.getIDs();
				System.out.println("name is : " + ids.next().toString());
				while (ids.hasMore())
					System.out.println("name is : " + ids.next().toString());

				
				
				String uid = (String) attributes.get("uid").get();
				exchange.setProperty("uid", uid);

				String mail = (String) attributes.get("mail").get();
				exchange.setProperty("mail", mail);

				String cn = (String) attributes.get("cn").get();
				if (cn == null)
					cn = "";
				
				if(cn.contains(",")) {
					
					cn.replaceAll(",", "|");
				}
				//String fullName[] = cn.split(",");
				/*String fName= fullName[0];
				String mName= fullName[1];
				String lName= fullName[2];*/
				exchange.setProperty("cn", cn);
				sbUidDetails.append(exchange.getProperty("groupName"));
				sbUidDetails.append("|");
				sbUidDetails.append(uid);
				sbUidDetails.append("|");
				sbUidDetails.append(mail);
				sbUidDetails.append("|");
				sbUidDetails.append(cn);
				exchange.getIn().setBody(sbUidDetails + "\n");
			}
		}).log("subDetails body ${body}");

	}
}
