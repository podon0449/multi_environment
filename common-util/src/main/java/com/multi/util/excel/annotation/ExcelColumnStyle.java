package com.multi.util.excel.annotation;


import com.multi.util.excel.style.ExcelCellStyle;
import com.multi.util.excel.style.CustomExcelCellStyle;
import com.multi.util.excel.style.DefaultExcelCellStyle;

public @interface ExcelColumnStyle {

	/**
	 * Enum implements {@link ExcelCellStyle}
	 * Also, can use just class.
	 * If not use Enum, enumName will be ignored
	 * @see DefaultExcelCellStyle
	 * @see CustomExcelCellStyle
	 */
	Class<? extends ExcelCellStyle> excelCellStyleClass();

	/**
	 * name of Enum implements {@link ExcelCellStyle}
	 * if not use Enum, enumName will be ignored
	 */
	String enumName() default "";

}
