package com.suramire.myapplication.test;

import java.io.Serializable;

/**
 * Created by Suramire on 2017/6/21.
 */

public class Student implements Serializable {
//    private static final long serialVersionUID = 1L;
    String name;
    int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Student(String name, int age) {
        this.name = name;
        this.age = age;
    }
}
