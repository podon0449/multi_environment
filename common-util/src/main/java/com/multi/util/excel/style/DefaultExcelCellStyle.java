package com.multi.util.excel.style;

import com.multi.util.excel.style.align.DefaultExcelAlign;
import com.multi.util.excel.style.align.ExcelAlign;
import com.multi.util.excel.style.border.DefaultExcelBorders;
import com.multi.util.excel.style.border.ExcelBorderStyle;
import com.multi.util.excel.style.color.DefaultExcelColor;
import com.multi.util.excel.style.color.ExcelColor;
import org.apache.poi.ss.usermodel.CellStyle;

/**
 * Example of using ExcelCellStyle as Enum
 */
public enum DefaultExcelCellStyle implements ExcelCellStyle {

	GREY_HEADER(DefaultExcelColor.rgb(217, 217, 217),
			DefaultExcelBorders.newInstance(ExcelBorderStyle.THIN), DefaultExcelAlign.CENTER_CENTER),
	BLUE_HEADER(DefaultExcelColor.rgb(223, 235, 246),
			DefaultExcelBorders.newInstance(ExcelBorderStyle.THIN), DefaultExcelAlign.CENTER_CENTER),
	BODY(DefaultExcelColor.rgb(255, 255, 255),
			DefaultExcelBorders.newInstance(ExcelBorderStyle.THIN), DefaultExcelAlign.RIGHT_CENTER);

	private final ExcelColor backgroundColor;
	/**
	 * like CSS margin or padding rule,
	 * List<DefaultExcelBorder> represents rgb TOP RIGHT BOTTOM LEFT
	 */
	private final DefaultExcelBorders borders;
	private final ExcelAlign align;

	DefaultExcelCellStyle(ExcelColor backgroundColor, DefaultExcelBorders borders, ExcelAlign align) {
		this.backgroundColor = backgroundColor;
		this.borders = borders;
		this.align = align;
	}

	@Override
	public void apply(CellStyle cellStyle) {
		backgroundColor.applyForeground(cellStyle);
		borders.apply(cellStyle);
		align.apply(cellStyle);
	}

}
