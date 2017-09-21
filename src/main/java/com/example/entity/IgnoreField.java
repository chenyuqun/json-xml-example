package com.example.entity;

import com.alibaba.fastjson.annotation.JSONType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * JSONType选择序列化的字段
 * 也可以使用includes,看文档使用equals() 比 equalsIgnoreCase()性能好点
 */
@JSONType(ignores ={"name"} )
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IgnoreField {
    private Long id;
    private String name;
    private Integer age;
}
