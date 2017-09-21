package com.example.entity;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * Created by alexchen on 2017/9/21.
 */
public class MoneySerializer implements ObjectSerializer{

    @Override
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        Integer value=(Integer) object;
        String text=value+"元";
        serializer.write(text);
    }
}
