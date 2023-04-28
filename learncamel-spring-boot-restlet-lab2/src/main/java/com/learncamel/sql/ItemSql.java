package com.learncamel.sql;

public class ItemSql {
	public static String findItemBySku() {
		String st=  "jpa://com.learncamel.domain.Item?query=select itm from com.learncamel.domain.Item itm where itm.sku = '${headers.sku}'";
		return st;
	}
	
	public static String findItemBySkuUsingNamedQuery(String sku) {
		String st=  "jpa://com.learncamel.domain.Item?namedQuery=Item.findBySku";
		return st;
	}
}
