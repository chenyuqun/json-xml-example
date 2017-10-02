package com.example.entity;

/**
 * Created by alexchen on 2017/10/2.
 */
public class User6 extends Model {

    public String getName(){
        return (String) attributes.get("name");
    }

    public void setName(String name) {
        attributes.put("name", name);
    }
}
