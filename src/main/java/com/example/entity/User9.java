package com.example.entity;

import com.alibaba.fastjson.annotation.JSONCreator;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * JSONCreator
 */
@Getter
@Setter
@ToString
public class User9 {
    private Integer id;
    private String name;
    @JSONCreator
    public User9(@JSONField(name = "id") Integer  id,
                 @JSONField(name = "name") String name){
        this.id = id;
        this.name = name;
    }
}
