package com.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by alexchen on 2017/9/20.
 * 部分不序列化
 */
@Data
@AllArgsConstructor
public class User1 {
    private Long id;
    private transient String name;
    private Integer age;
}
