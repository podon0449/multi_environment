package com.multi.common.core.exception;

public enum ErrorType {

    ERROR(0, "HT_ER_"),
    ERROR_SYSTEM(1, "HT_SY_"),

    ERROR_SQL(2, "HT_SQ_"),

    ERROR_USER_DATA(3, "HT_US_");


    private int type;
    private String name;

    ErrorType(int type, String name) {
        this.type = type;
        this.name = name;
    }

    public int getType() {
        return this.type;
    }

    public String getName() {
        return this.name;
    }

}
