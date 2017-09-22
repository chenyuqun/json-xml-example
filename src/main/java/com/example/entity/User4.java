package com.example.entity;

import lombok.*;

import java.util.List;

/**
 * Created by alexchen on 2017/9/22.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Builder
public class User4 {
    @NonNull
    private Long id;
    @NonNull
    private String name;
    @NonNull
    private Integer age;
    @NonNull
    private Gender gender;
    private List<User4> friends;
    private String[] labels;

}
