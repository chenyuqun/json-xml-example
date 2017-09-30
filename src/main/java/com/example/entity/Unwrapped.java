package com.example.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.Map;

/**
 * 测试包装类
 */
@Data
public class Unwrapped {
    private Integer id;
    private String name;
    private Integer age;
    @JSONField(unwrapped = true)
    private Location location;
    @JSONField(unwrapped = true)
    private Map<String,Object> properties;
}
