package com.multi.spring.controller;

import com.multi.domain.user.model.UserExcelDetailCol;
import com.multi.spring.service.ExcelUtilBO;
import com.multi.spring.service.user.UserBO;
import com.multi.util.excel.ExcelFile;
import com.multi.util.excel.poi.PoiSheetExcelFile;
import com.multi.util.excel.poi.resource.ExcelCustomHeader;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

import static com.multi.util.excel.poi.resource.ExcelCustomHeader.USER_COLUMN;


@Api(tags = "POI 엑셀-API")
@RestController
@RequestMapping("/v1/api/poi")
@Slf4j
public class PoiExcelController {
    @Autowired
    UserBO userBO;
    @Autowired ExcelUtilBO excelUtilBO;
    @ApiOperation(value = "poi 엑셀 다운로드")
    @RequestMapping(value = "/excel", method = RequestMethod.GET, produces="text/plain;charset=UTF-8")
    public void getExcelDownload(HttpServletResponse response) throws IOException {
        List<UserExcelDetailCol> userExcelDetailColList = userBO.getUserExcelDetailColList();

        ExcelFile excelFile = new PoiSheetExcelFile(userExcelDetailColList, UserExcelDetailCol.class);
        excelFile.setResponse(response);
        excelFile.write(response.getOutputStream());
    }
    @ApiOperation(value = "poi 엑셀 유저 커스텀 다운로드")
    @RequestMapping(value = "/excel/custom", method = RequestMethod.GET, produces="text/plain;charset=UTF-8")
    public void getExcelUserCustomDownload(HttpServletResponse response) throws IOException {
        // @ExcelColumn 을 사용하는게 아닌 직접 유저가 헤더키와 필드키를 넣고 생성하는 구조
        List<Map<String, Object>> excelMetaList = new ArrayList<>();
        for (int i=1; i < 1000; i++) {
            Map<String, Object> metaMap = new HashMap<>();
            metaMap.put("rank", i);
            metaMap.put("userIdx", "1000000" + i);
            metaMap.put("countryName", "한국");
            metaMap.put("device", "Aos");
            metaMap.put("nickname", "나는엑셀테스트");
            metaMap.put("email", "test"+i+"@naver.com");
            metaMap.put("amount", i + 1000);
            excelMetaList.add(metaMap);
        }
        String[] keys = {"순위", "유저정보", "국가", "기기", "닉네임", "이메일", "유저 보유 금액"};
        List<String> headerKeys = Arrays.asList(keys);
        List<String> fieldKeys = excelUtilBO.getExcelFieldList(excelMetaList.get(0).keySet());
        ExcelFile excelFile = new PoiSheetExcelFile(excelMetaList, headerKeys, fieldKeys, USER_COLUMN);

        excelFile.setResponse(response);
        excelFile.write(response.getOutputStream());
    }
}
