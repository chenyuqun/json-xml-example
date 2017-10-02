package com.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by alexchen on 2017/9/30.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Location {
    private int longitude;
    private int latitude;
}
