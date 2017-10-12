package com.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by alexchen on 2017/10/2.
 */
@Data
@AllArgsConstructor
public class User8 {
    private Integer id;
    private String name;
    private Object value;
    public User8() {}
    public User8(Integer id, Object value) { this.id = id; this.value = value; }
    public User8(Integer id, String name) { this.id = id; this.name = name; }
    public User8(String name) { this.name = name; }
}
