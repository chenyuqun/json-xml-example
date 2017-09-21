package com.example.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * JSONFiled 指定不序列化字段
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IgnoreField1 {

    private Long id;
    @JSONField(serialize = false)
    private String name;
    @JSONField(deserialize = false)
    private Integer age;

}
