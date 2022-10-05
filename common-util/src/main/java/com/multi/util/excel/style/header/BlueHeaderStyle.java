package com.multi.util.excel.style.header;

import com.multi.util.excel.style.CustomExcelCellStyle;
import com.multi.util.excel.style.align.DefaultExcelAlign;
import com.multi.util.excel.style.border.DefaultExcelBorders;
import com.multi.util.excel.style.border.ExcelBorderStyle;
import com.multi.util.excel.style.configurer.ExcelCellStyleConfigurer;

public class BlueHeaderStyle extends CustomExcelCellStyle {

    @Override
    public void configure(ExcelCellStyleConfigurer configurer) {
        configurer.foregroundColor(223, 235, 246)
                .excelBorders(DefaultExcelBorders.newInstance(ExcelBorderStyle.THIN))
                .excelAlign(DefaultExcelAlign.CENTER_CENTER);
    }

}