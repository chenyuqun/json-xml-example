package com.example.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * JSONFiled 指定不序列化字段
 */

@Data
@AllArgsConstructor
public class IgnoreField1 {
    private Long id;
    @JSONField(serialize = false,deserialize = false)
    private String name;
    private Integer age;
}
