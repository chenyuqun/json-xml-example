package com.example.entity;

import com.alibaba.fastjson.parser.deserializer.ExtraProcessable;
import com.alibaba.fastjson.serializer.JSONSerializable;
import com.alibaba.fastjson.serializer.JSONSerializer;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by alexchen on 2017/10/2.
 */
public class Model implements JSONSerializable,ExtraProcessable {
    protected Map<String, Object> attributes = new HashMap<String, Object>();

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public Object getAttribute(String name) {
        return attributes.get(name);
    }

    @Override
    public void write(JSONSerializer serializer,
                      Object fieldName,
                      Type fieldType,
                      int features) throws IOException {
        serializer.write(attributes); // 定制序列化
    }

    @Override
    public void processExtra(String key, Object value) {
        attributes.put(key, value); // 定制反序列化
    }
}
