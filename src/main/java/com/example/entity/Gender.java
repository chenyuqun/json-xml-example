package com.example.entity;

import lombok.Getter;

/**
 * 性别枚举类
 */
@Getter
public enum Gender {
    Male("帅哥"),
    Female("美女");
    private String desc;

    private Gender(String desc){
        this.desc=desc;
    }

    public String toString(){
        return desc;
    }

}
