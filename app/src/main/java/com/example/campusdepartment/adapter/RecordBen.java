package com.example.campusdepartment.adapter;

/**
 * Created by 林嘉煌 on 2021/1/6.
 */

public class RecordBen {
    private String name;

    public RecordBen() {

    }

    public RecordBen(String name) {

        this.name = name;
    }

    @Override
    public String toString() {
        return "RecordBen{" +
                "name='" + name + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
