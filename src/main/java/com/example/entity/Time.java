package com.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 时间类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Time {
    private Date date1;
    private String date2;
    private Long date3;
}
