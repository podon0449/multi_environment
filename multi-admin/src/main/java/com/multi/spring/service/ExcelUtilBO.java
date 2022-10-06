package com.multi.spring.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class ExcelUtilBO {
    /** 해당 excelList 필드를 추출하기 위함 */
    public List<String> getExcelFieldList(Set<String> excelField) {
        List<String> fieldKeys = new ArrayList<>();
        for (String field : excelField) {
            fieldKeys.add(field);
        }
        return fieldKeys;
    }
}
