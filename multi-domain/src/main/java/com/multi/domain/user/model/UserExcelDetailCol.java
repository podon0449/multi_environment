package com.multi.domain.user.model;

import com.podong.annotation.DefaultBodyStyle;
import com.podong.annotation.DefaultHeaderStyle;
import com.podong.annotation.ExcelColumn;
import com.podong.annotation.ExcelColumnStyle;
import com.podong.style.DefaultExcelCellStyle;
import lombok.Getter;
import lombok.Setter;

/**
 *  VOTE / QR 사용중
 * */
@Getter
@Setter
@DefaultHeaderStyle(style = @ExcelColumnStyle(excelCellStyleClass = DefaultExcelCellStyle.class, enumName = "GREY_HEADER"))
@DefaultBodyStyle(style = @ExcelColumnStyle(excelCellStyleClass = DefaultExcelCellStyle.class, enumName = "BODY"))
public class UserExcelDetailCol {
    @ExcelColumn(headerName = "순위")
    private int rank;

    @ExcelColumn(headerName = "유저정보")
    private String userIdx;

    @ExcelColumn(headerName = "국가")
    private String countryName;

    @ExcelColumn(headerName = "기기")
    private String device;

    @ExcelColumn(headerName = "닉네임")
    private String nickname;

    @ExcelColumn(headerName = "이메일")
    private String email;

    @ExcelColumn(headerName = "유저 보유 금액")
    private int amount;


}