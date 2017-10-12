package com.example.entity;

import com.alibaba.fastjson.annotation.JSONPOJOBuilder;

/**
 * Created by alexchen on 2017/10/12.
 */
@JSONPOJOBuilder(buildMethod="createBean",withPrefix = "construct")
public class User10Builder {
    private Integer idValue;
    private String nameValue;
    public User10Builder constructId(Integer id) {
        idValue = id;
        return this;
    }

    public User10Builder constructName(String name) {
        nameValue = name;
        return this;
    }

    public User10 createBean() {
        return new User10(idValue, nameValue);
    }

}
