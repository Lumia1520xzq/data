package com.wf.data.controller.admin;

import com.wf.core.persistence.Page;
import com.wf.core.utils.TraceIdUtils;
import com.wf.core.utils.excel.ExportExcel;
import com.wf.core.utils.excel.ImportExcel;
import com.wf.core.utils.type.DateUtils;
import com.wf.core.web.base.ExtJsController;
import com.wf.data.dao.data.entity.DataDailyRecord;
import com.wf.data.service.DataDailyRecordService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.jasig.cas.client.util.AssertionHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * @author shihui
 * @date 2018/1/31
 */

@RestController
@RequestMapping("/data/admin/dailyRecord")
public class DataDailyRecordController extends ExtJsController {

    @Autowired
    private DataDailyRecordService dataDailyRecordService;


    /**
     * 查询列表
     */
    @RequestMapping("/list")
    public Object list() {
        Page<DataDailyRecord> page = getPage(DataDailyRecord.class);
        return dataGrid(dataDailyRecordService.findPage(page));
    }

    /**
     * 保存
     *
     * @return
     */
    @RequestMapping("/save")
    public Object save() {
        DataDailyRecord form = getForm(DataDailyRecord.class);
        if (form == null) {
            return error("请求参数错误");
        }

        DataDailyRecord dataDailyRecord = dataDailyRecordService.get(form.getId());
        if (dataDailyRecord == null) {//新增
            form.setCreater(AssertionHolder.getAssertion().getPrincipal().getName());
        } else {//修改
            form.setUpdater(AssertionHolder.getAssertion().getPrincipal().getName());
            form.setUpdateTime(new Date());
            form.setCreater(dataDailyRecord.getCreater());
        }
        dataDailyRecordService.save(form);
        return success();
    }

    /**
     * 删除
     *
     * @return
     */
    @RequestMapping("/delete")
    public Object delete() {
        DataDailyRecord entity = getForm(DataDailyRecord.class);
        dataDailyRecordService.delete(entity);
        return success();
    }

    /**
     * 下载导入数据模板
     *
     * @param response
     * @return
     */
    @RequestMapping(value = "/importFile/template")
    public void importFileTemplate(HttpServletResponse response) {
        try {
            String fileName = "日报数据导入模板.xlsx";
            new ExportExcel("日报数据管理", DataDailyRecord.class, 2).write(response, fileName).dispose();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 导入数据
     *
     * @param file
     * @return
     */
    @RequestMapping(value = "/importFile")
    public Object importFile(MultipartFile file) {
        int count = 0;
        try {
            ImportExcel ei = new ImportExcel(file, 1, 0);
            List<DataDailyRecord> list = ei.getDataList(DataDailyRecord.class);
            for (DataDailyRecord entity : list) {
                try {
                    if (StringUtils.isNotBlank(entity.getDataTimeStr())){
                        entity.setDataTime(DateUtils.parseDate(entity.getDataTimeStr()));
                    }
                    entity.setCreater(AssertionHolder.getAssertion().getPrincipal().getName());
                    entity.setCreateTime(new Date());
                    entity.setDeleteFlag(0);
                    dataDailyRecordService.save(entity);
                    count++;
                } catch (Exception e) {
                    dataDailyRecordService.delete(entity);
                    logger.error("导入模板下载失败！失败信息 traceId={} ,ex={} ", TraceIdUtils.getTraceId(), ExceptionUtils.getStackTrace(e));
                    return error("导入失败！导入失败的记录为：\" + entity");
                }
            }
        } catch (Exception e) {
            logger.error("导入模板下载失败！失败信息 traceId={} ,ex={} ", TraceIdUtils.getTraceId(), ExceptionUtils.getStackTrace(e));
            return error("导入模板下载失败！失败信息：" + e.getMessage());

        }
        return success("成功导入" + count + "记录");
    }
}
