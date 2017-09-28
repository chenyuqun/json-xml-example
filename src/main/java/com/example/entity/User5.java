package com.example.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 测试filter
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User5 {
    @Necessary
    private Integer id;
    @Necessary
    private String name;
    @JSONField(label = "ignore")
    private Integer age;
}
