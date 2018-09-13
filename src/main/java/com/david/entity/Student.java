package com.david.entity;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@EqualsAndHashCode
public class Student {
    private String name;
    private int age;
    private String male;
    private String studentNo;

}
