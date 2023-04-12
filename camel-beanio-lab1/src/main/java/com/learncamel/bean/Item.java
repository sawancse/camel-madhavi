package com.learncamel.bean;
public class Item {

    public Item(String id, String name, String type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }

    public Item(String id, String name, String type, double price) {
		super();
		this.id = id;
		this.name = name;
		this.type = type;
		this.price = price;
	}

	private String id;

    private String name;
    
    private String type;

    private double price;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
    
    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Item [id=");
        builder.append(id);
        builder.append(", name=");
        builder.append(name);
        builder.append(", type=");
        builder.append(type);
        builder.append(", price=");
        builder.append(price);
        builder.append("]");
        return builder.toString();
    }
}