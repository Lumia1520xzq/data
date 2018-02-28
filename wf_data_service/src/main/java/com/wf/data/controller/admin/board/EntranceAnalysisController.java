package com.wf.data.controller.admin.board;

import com.google.common.collect.Lists;
import com.wf.core.utils.excel.ExportExcel;
import com.wf.core.web.base.ExtJsController;
import com.wf.data.common.utils.DateUtils;
import com.wf.data.dao.datarepo.entity.DatawareFinalEntranceAnalysis;
import com.wf.data.service.data.DatawareFinalEntranceAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author lcs
 * 2018/02/02
 */
@RestController
@RequestMapping(value = "/data/entrance/analysis")
public class EntranceAnalysisController extends ExtJsController {


    @Autowired
    private DatawareFinalEntranceAnalysisService datawareFinalEntranceAnalysisService;

    /**
     * 导出用户数据
     *
     * @return
     */
    @RequestMapping(value = "export")
    public void exportFile(@RequestParam String businessDate, HttpServletResponse response) {
        List<DatawareFinalEntranceAnalysis> list = Lists.newArrayList();
        DatawareFinalEntranceAnalysis dto = new DatawareFinalEntranceAnalysis();
        dto.setBusinessDate(businessDate);
        try {
            list = datawareFinalEntranceAnalysisService.findList(dto, 1000);
            String fileName = "奖多多各入口用户分析表" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
            new ExportExcel("奖多多各入口用户分析表", DatawareFinalEntranceAnalysis.class).setDataList(list).write(response, fileName).dispose();
        } catch (Exception e) {
            logger.error("导出失败：" + e.getMessage());
        }
    }
}

