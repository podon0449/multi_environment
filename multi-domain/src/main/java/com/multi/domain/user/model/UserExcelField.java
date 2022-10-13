package com.multi.domain.user.model;

import com.podong.annotation.PivotEnumModel;

/**
 * 해당 필드와 데이터를 매칭시키는 enum
 * active 를 활성화 시킨경우 pivotFieldOrientation 을 지정할 수 있음.
 *
 *    1 : PivotFieldOrientation.PageField;
 *    2 : PivotFieldOrientation.RowField;
 *    3 : PivotFieldOrientation.DataField;
 *    4 : PivotFieldOrientation.Hidden;
 *    5 : PivotFieldOrientation.ColumnField;
 *
 *    type 은 넘어온 list 인덱스 순서 //
 * */
public enum UserExcelField implements PivotEnumModel {
    RANK(0,"순위", false, 0),
    USER_IDX(1,"유저정보", true, 2),
    COUNTRY(2,"국가", false, 0),
    DEVICE(3,"기기", false, 0),
    NICKNAME(4,"닉네임" , true, 1),
    EMAIL(5,"이메일" , false, 0),
    AMOUNT(6,"유저 보유금액" , true, 3);

    private int type;
    private String name;
    private boolean active;
    private int pivotFieldOrientation;

    UserExcelField(int type, String name, boolean active, int pivotFieldOrientation) {
        this.type = type;
        this.name = name;
        this.active = active;
        this.pivotFieldOrientation = pivotFieldOrientation;
    }
    public String getKey() {
        return name();
    }

    public String getValue() {
        return String.valueOf(this.type);
    }

    public int getType() {
        return this.type;
    }

    public String getName() {
        return this.name;
    }

    public boolean isActive() {
        return active;
    }

    public int getCode() { return this.type;}

    public int getPivotFieldOrientation() {
        return pivotFieldOrientation;
    }


}
