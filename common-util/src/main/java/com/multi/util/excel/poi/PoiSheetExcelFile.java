package com.multi.util.excel.poi;

import com.multi.common.core.exception.CommonException;
import com.multi.util.excel.XSSFExcelFile;
import com.multi.util.excel.poi.resource.DataFormatDecider;
import com.multi.util.excel.poi.resource.DefaultDataFormatDecider;
import com.multi.util.excel.poi.resource.ExcelCustomHeader;
import com.multi.util.excel.poi.resource.ExcelRenderPoiResourceFactory;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

import static com.multi.common.core.exception.ErrorType.ERROR;
import static com.multi.common.core.exception.ServiceStatusCode.ERROR_NULL;

/**
 * PoiSheetExcelFile - poi XSSF 엑셀 차트 생성을 위한 ExcelFile class
 */
public final class PoiSheetExcelFile<T> extends XSSFExcelFile<T> {

	private static final int ROW_START_INDEX = 0;
	private static final int COLUMN_START_INDEX = 0;
	private int currentRowIndex = ROW_START_INDEX;

	public PoiSheetExcelFile(Class<T> type) {
		super(type);
	}

	public PoiSheetExcelFile(List<T> data, Class<T> type) {
		super(data, type);
	}

	public PoiSheetExcelFile(List<T> data, List<String> headerKeys, List<String> fieldKeys, ExcelCustomHeader excelCustom) {
		super(data, headerKeys, fieldKeys, excelCustom);
	}


	public PoiSheetExcelFile(List<T> data, Class<T> type, DataFormatDecider dataFormatDecider) {
		super(data, type, dataFormatDecider);
	}

	@Override
	protected void validateData(List<T> data) {
		if (null == data) {
			throw new CommonException(ERROR, ERROR_NULL, "poi excel data is null");
		}
	}

	@Override
	public void renderExcel(List<T> data) {
		// 1. Create sheet and renderHeader
		sheet = wb.createSheet();
		sheet.setDefaultColumnWidth((short) 25);

		renderHeadersWithNewSheet(sheet, currentRowIndex++, COLUMN_START_INDEX);

		if (data.isEmpty()) {
			return;
		}

		// 2. Render Body
		for (Object renderedData : data) {
			renderBody(renderedData, currentRowIndex++, COLUMN_START_INDEX);
		}
	}

	@Override
	public void addRows(List<T> data) {
		renderBody(data, currentRowIndex++, COLUMN_START_INDEX);
	}

	@Override
	public void addSheet(Map<String, Object> dataMap) {
		List<T> data = (List<T>) dataMap.get("excelList");
		super.resource = ExcelRenderPoiResourceFactory.prepareRenderResource((List<String>)dataMap.get("headerKeys"), (List<String>)dataMap.get("fieldKeys"), wb, new DefaultDataFormatDecider() , (ExcelCustomHeader) dataMap.get("ExcelColumn"));
		renderExcel(data);
	}



	@Override
	public void setResponse(HttpServletResponse response) {
		super.setResponse(response);
	}

}
