package com.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by alexchen on 2017/9/20.
 * 普通bean
 */
@Data
@AllArgsConstructor
public class User {
    private Long id;
    private  String name;
    private Integer age;
}
