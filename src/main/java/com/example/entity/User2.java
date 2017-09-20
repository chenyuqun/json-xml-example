package com.example.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by alexchen on 2017/9/20.
 * 测试顺序
 */
@Data
@AllArgsConstructor
public class User2 {
    @JSONField(ordinal = 1)
    private Long id;
    @JSONField(ordinal = 2)
    private  String name;
    @JSONField(ordinal = 3)
    private Integer age;
}
