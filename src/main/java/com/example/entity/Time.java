package com.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Created by alexchen on 2017/9/25.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Time {
    private Date date1;
    private String date2;
    private Long date3;
}
