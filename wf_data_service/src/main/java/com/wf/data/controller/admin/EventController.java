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
import com.wf.data.service.ChannelInfoService;
import com.wf.data.service.DataDictService;
import com.wf.data.service.EventService;
import com.wf.data.service.UicUserService;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.jasig.cas.client.util.AssertionHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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

    @Autowired
    private ChannelInfoService channelInfoService;

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
        String content = null;
        Long start = null;
        Long length = null;

        JSONObject data = json.getJSONObject("data");

        if (data != null) {
            channelId = data.getLong("channelId");
            eventType = data.getInteger("eventType");
            beginDate = data.getString("beginDate");
            endDate = data.getString("endDate");
            content = data.getString("content");
            start = json.getLongValue("start");
            length = json.getLongValue("limit");
        }

        //设置默认搜索时间为昨天
        if (StringUtils.isBlank(beginDate)) {
            Calendar c = Calendar.getInstance();
            c.add(Calendar.MONTH, -1);
            beginDate = com.wf.data.common.utils.DateUtils.formatDate(c.getTime(), com.wf.data.common.utils.DateUtils.DATE_PATTERN);
        }

        DataEvent dataEvent = new DataEvent();
        dataEvent.setChannelId(channelId);
        dataEvent.setEventType(eventType);
        dataEvent.setBeginDate(beginDate);
        dataEvent.setEndDate(endDate);
        dataEvent.setContent(content);

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
        if (StringUtils.isBlank(form.getBeginDate())) {
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
                //数据日期为空，不存储
                if (StringUtils.isBlank(entity.getBeginDate())) {
                    continue;
                }
                try {
                    entity.setCreater(AssertionHolder.getAssertion().getPrincipal().getName());
                    entity.setCreateTime(new Date());
                    entity.setDeleteFlag(0);
                    if (StringUtils.isNotBlank(entity.getEventTypeName())) {
                        for (DataDict dataDict : eventTypeList) {
                            if (entity.getEventTypeName().equals(dataDict.getLabel())) {
                                entity.setEventType(dataDict.getValue());
                                break;
                            }
                        }
                        if (entity.getEventType() == null) {
                            return error("类别不存在！");
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

    @RequestMapping("/export")
    public void export(@RequestParam String channelId, @RequestParam String eventType, @RequestParam String beginDate, @RequestParam String endDate, @RequestParam String content, HttpServletResponse response) {
        try {
            DataEvent dataEvent = new DataEvent();
            if (!channelId.equals("undefined") && !channelId.equals("null") && StringUtils.isNotBlank(channelId)) {
                dataEvent.setChannelId(Long.parseLong(channelId));
            }
            if (!eventType.equals("undefined") && !eventType.equals("null") && StringUtils.isNotBlank(eventType)) {
                dataEvent.setEventType(Integer.parseInt(eventType));
            }
            if (!beginDate.equals("undefined") && !beginDate.equals("null") && StringUtils.isNotBlank(beginDate)) {
                dataEvent.setBeginDate(formatGTMDate(beginDate));
            }
            if (!endDate.equals("undefined") && !endDate.equals("null") && StringUtils.isNotBlank(endDate)) {
                dataEvent.setEndDate(formatGTMDate(endDate));
            }
            if (!content.equals("undefined") && !content.equals("null") && StringUtils.isNotBlank(content)) {
                dataEvent.setContent(content);
            }
            List<DataEvent> events = eventService.findList(dataEvent, 99999999);

            if (events != null) {
                for (DataEvent event : events) {
                    //事记类型名称
                    if (event.getEventType() != null) {
                        String eventTypeName = dataDictService.getDictByValue("event_type", event.getEventType()).getLabel();
                        event.setEventTypeName(eventTypeName);
                    }
                    if (event.getChannelId() == null) {
                        event.setChannelId(0L);
                    }
                }
            }

            String fileName = "事记管理数据" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
            new ExportExcel("事记管理数据", DataEvent.class).setDataList(events).write(response, fileName).dispose();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 格式化GMT时间
     *
     * @param date
     * @return
     */
    public String formatGTMDate(String date) {
        DateFormat gmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        date = date.replace("GMT 0800", "GMT +08:00").replace("GMT 0800", "GMT+0800").replaceAll("\\(.*\\)", "");
        SimpleDateFormat defaultFormat = new SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss z", Locale.US);
        Date time = null;
        try {
            time = defaultFormat.parse(date);
            gmt.setTimeZone(TimeZone.getTimeZone("GMT"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return gmt.format(time);
    }
}
