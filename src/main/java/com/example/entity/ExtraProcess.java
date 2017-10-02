package com.example.entity;

import com.alibaba.fastjson.parser.deserializer.ExtraProcessable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by alexchen on 2017/10/2.
 */
@NoArgsConstructor
public class ExtraProcess implements ExtraProcessable{
    @Getter
    @Setter
    private Integer id;
    @Getter
    private String name;

    @Override
    public void processExtra(String key, Object value) {
        System.out.println("extra key:"+key);
    }
}
