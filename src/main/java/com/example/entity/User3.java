package com.example.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 测试JSONField的不同名称属性
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User3 {
    @JSONField(name="ID")
    private Long id;
    @JSONField(serialzeFeatures ={SerializerFeature.WriteNullStringAsEmpty})
    private String name;
    @JSONField(alternateNames = {"old"},parseFeatures = {Feature.AllowSingleQuotes})
    private Integer age;
    @JSONField(format = "yyyy-MM-dd")
    private Date birthday;
    @JSONField(serializeUsing = MoneySerializer.class)
    private Integer salary;

}
