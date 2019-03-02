package com.yunsheng.filestore.entity;

import java.util.List;

public class Series {

    public String name;
    public String type;
    public List<Integer> data;
    public static String TYPE_LINE = "line";
    public static String TYPE_BAR = "bar";

    public Series(String name, String type, List<Integer> data) {

        this.name = name;
        this.type = type;
        this.data = data;
    }

    public String toName() {
        return name;
    }

}
