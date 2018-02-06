package com.wf.data.controller.admin;

import com.alibaba.fastjson.JSONObject;
import com.wf.core.persistence.Page;
import com.wf.core.utils.TraceIdUtils;
import com.wf.core.utils.excel.ExportExcel;
import com.wf.core.utils.excel.ImportExcel;
import com.wf.core.utils.type.DateUtils;
import com.wf.core.utils.type.StringUtils;
import com.wf.core.web.base.ExtJsController;
import com.wf.data.dao.data.entity.DataDict;
import com.wf.data.dao.data.entity.DataEvent;
import com.wf.data.service.DataDictService;
import com.wf.data.service.EventService;
import com.wf.data.service.UicUserService;
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
 * @date 2018/1/29
 */

@RestController
@RequestMapping("data/admin/event")
public class EventController extends ExtJsController {

    @Autowired
    private EventService eventService;

    @Autowired
    private UicUserService uicUserService;

    @Autowired
    private DataDictService dataDictService;

    /**
     * 查询列表
     */
    @RequestMapping("/list")
    public Object list() {
        JSONObject json = getRequestJson();
        Long channelId = null;
        Integer eventType = null;
        String beginDate = null;
        String endDate = null;
        Long start = null;
        Long length = null;

        JSONObject data = json.getJSONObject("data");

        if (data != null) {
            channelId = data.getLong("channelId");
            eventType = data.getInteger("eventType");
            beginDate = data.getString("beginDate");
            endDate = data.getString("endDate");
            start = json.getLongValue("start");
            length = json.getLongValue("limit");
        }

        //设置默认搜索时间为昨天
        if (StringUtils.isBlank(beginDate) || StringUtils.isBlank(endDate)) {
            beginDate = com.wf.data.common.utils.DateUtils.getYesterdayDate();
            endDate = com.wf.data.common.utils.DateUtils.getYesterdayDate();
        }

        DataEvent dataEvent = new DataEvent();
        dataEvent.setChannelId(channelId);
        dataEvent.setEventType(eventType);
        dataEvent.setBeginDate(beginDate);
        dataEvent.setEndDate(endDate);

        Page<DataEvent> dataEventPage = new Page<>(dataEvent, start, length);
        dataEventPage = eventService.findPage(dataEvent);
        return dataGrid(dataEventPage);
    }

    /**
     * 保存
     *
     * @return
     */
    @RequestMapping("/save")
    public Object save() {
        DataEvent form = getForm(DataEvent.class);

        if (form == null) {
            return error("请求参数错误");
        }

        //开始日期默认为昨天
        if (StringUtils.isBlank(form.getBeginDate())){
            form.setBeginDate(DateUtils.getYesterdayDate());
        }

        DataEvent dataEvent = eventService.get(form.getId());
        if (dataEvent == null) {//新增
            form.setCreater(AssertionHolder.getAssertion().getPrincipal().getName());
        } else {//修改
            form.setUpdater(AssertionHolder.getAssertion().getPrincipal().getName());
            form.setUpdateTime(new Date());
            form.setCreater(dataEvent.getCreater());
        }
        eventService.save(form);
        return success();
    }

    /**
     * 删除
     *
     * @return
     */
    @RequestMapping("/delete")
    public Object delete() {
        DataEvent entity = getForm(DataEvent.class);
        eventService.delete(entity);
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
            String fileName = "事记导入模板.xlsx";
            new ExportExcel("事记管理", DataEvent.class, 2).write(response, fileName).dispose();
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
            List<DataEvent> list = ei.getDataList(DataEvent.class);

            DataDict params = new DataDict();
            params.setType("event_type");
            List<DataDict> eventTypeList = dataDictService.findList(params);
            for (DataEvent entity : list) {
                try {
                    entity.setCreater(AssertionHolder.getAssertion().getPrincipal().getName());
                    entity.setCreateTime(new Date());
                    entity.setDeleteFlag(0);
                    if (StringUtils.isNotBlank(entity.getEventTypeName())){
                        for (DataDict dataDict : eventTypeList) {
                            if (entity.getEventTypeName().equals(dataDict.getLabel())){
                                entity.setEventType(dataDict.getValue());
                                break;
                            }else {
                                logger.error("导入模板下载失败！类别填写不正确！");
                            }
                        }
                    }
                    eventService.save(entity);
                    count++;
                } catch (Exception e) {
                    eventService.delete(entity);
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
