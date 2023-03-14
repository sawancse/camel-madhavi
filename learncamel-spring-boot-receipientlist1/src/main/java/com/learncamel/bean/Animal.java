package com.learncamel.bean;

import java.io.Serializable;

public class Animal implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String name;
    public Animal() {
    }
    public Animal(int id, String name) {
        this.id = id;
        this.name = name;
    }
    public Animal deepClone() {
        Animal clone = new Animal();
        clone.setId(getId());
        clone.setName(getName());
        return clone;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    @Override
    public String toString() {
        return id + " " + name;
    }
}
