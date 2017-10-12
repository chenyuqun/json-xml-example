package com.example.entity;

import com.alibaba.fastjson.annotation.JSONType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by alexchen on 2017/10/12.
 */
@Getter
@Setter
@AllArgsConstructor
//@JSONType(builder = User10Builder.class)
public class User10 {
    private Integer id;
    private String name;
}
