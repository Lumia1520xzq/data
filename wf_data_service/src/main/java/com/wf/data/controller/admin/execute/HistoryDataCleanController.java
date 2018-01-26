package com.wf.data.controller.admin.execute;

import com.wf.core.web.base.ExtJsController;
import com.wf.data.common.utils.DateUtils;
import com.wf.data.service.business.*;
import jodd.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 历史数据补充
 *
 * @author liucs
 */
@RestController
@RequestMapping("/data/admin/dataClean")
public class HistoryDataCleanController extends ExtJsController {

    @Autowired
    private ChannelInfoHourService channelInfoHourService;
    @Autowired
    private PlatUserSignHourService platUserSignHourService;
    @Autowired
    private ChannelInfoAllService channelInfoAllService;
    @Autowired
    private BettingLogDayService bettingLogDayService;
    @Autowired
    private BettingLogHourService bettingLogHourService;
    @Autowired
    private BuryingPointDayService buryingPointDayService;
    @Autowired
    private BuryingPointHourService buryingPointHourService;


    /**
     * 清洗channelInfoHour表
     *
     * @return
     */
    @RequestMapping("/channelInfoHour")
    @ResponseBody
    public Object channelInfoHour(HttpServletRequest request) {
        String startTime = request.getParameter("startTime");
        String endTime = request.getParameter("endTime");

        if (StringUtil.isBlank(startTime)) {
            return error("开始时间为空");
        }
        if (StringUtil.isBlank(endTime)) {
            return error("结束时间为空");
        }

        if (DateUtils.parseDateTime(startTime).getTime() > DateUtils.parseDateTime(endTime).getTime()) {
            return error("开始时间大于结束时间");
        }

        channelInfoHourService.dataClean(startTime, endTime);
        return success("清洗开始执行");
    }


    /**
     * 清洗platSignedUser表
     *
     * @return
     */
    @RequestMapping("/platSignedUser")
    @ResponseBody
    public Object platSignedUser(HttpServletRequest request) {
        String startTime = request.getParameter("startTime");
        String endTime = request.getParameter("endTime");

        if (StringUtil.isBlank(startTime)) {
            return error("开始时间为空");
        }
        if (StringUtil.isBlank(endTime)) {
            return error("结束时间为空");
        }

        if (DateUtils.parseDateTime(startTime).getTime() > DateUtils.parseDateTime(endTime).getTime()) {
            return error("开始时间大于结束时间");
        }

        platUserSignHourService.dataClean(startTime, endTime);
        return success("清洗开始执行");
    }

    /**
     * 清洗channelInfoAll表
     *
     * @return
     */
    @RequestMapping("/channelInfoAll")
    @ResponseBody
    public Object channelInfoAll(HttpServletRequest request) {
        String startTime = request.getParameter("startTime");
        String endTime = request.getParameter("endTime");

        if (StringUtil.isBlank(startTime)) {
            return error("开始时间为空");
        }
        if (StringUtil.isBlank(endTime)) {
            return error("结束时间为空");
        }

        if (DateUtils.parseDate(startTime).getTime() > DateUtils.parseDate(endTime).getTime()) {
            return error("开始时间大于结束时间");
        }

        channelInfoAllService.dataClean(startTime, endTime);
        return success("清洗开始执行");
    }


    /**
     * 清洗bettingLogDay表
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("/bettingLogDay")
    public Object bettingLogDay(HttpServletRequest request) {
        String startTime = request.getParameter("startTime");
        String endTime = request.getParameter("endTime");

        if (StringUtil.isBlank(startTime)) {
            return error("开始时间为空");
        }
        if (StringUtil.isBlank(endTime)) {
            return error("结束时间为空");
        }

        if (DateUtils.parseDate(startTime).getTime() > DateUtils.parseDate(endTime).getTime()) {
            return error("开始时间大于结束时间");
        }

        bettingLogDayService.dataClean(startTime, endTime);
        return success("清洗开始执行");
    }

    /**
     * 清洗bettingLogHour表
     *
     * @return
     */
    @RequestMapping("/bettingLogHour")
    @ResponseBody
    public Object bettingLogHour(HttpServletRequest request) {
        String startTime = request.getParameter("startTime");
        String endTime = request.getParameter("endTime");

        if (StringUtil.isBlank(startTime)) {
            return error("开始时间为空");
        }
        if (StringUtil.isBlank(endTime)) {
            return error("结束时间为空");
        }

        if (DateUtils.parseDateTime(startTime).getTime() > DateUtils.parseDateTime(endTime).getTime()) {
            return error("开始时间大于结束时间");
        }

        bettingLogHourService.dataClean(startTime, endTime);
        return success("清洗开始执行");
    }


    /**
     * 清洗buryingPointDay表
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("/buryingPointDay")
    public Object buryingPointDay(HttpServletRequest request) {
        String startTime = request.getParameter("startTime");
        String endTime = request.getParameter("endTime");

        if (StringUtil.isBlank(startTime)) {
            return error("开始时间为空");
        }
        if (StringUtil.isBlank(endTime)) {
            return error("结束时间为空");
        }

        if (DateUtils.parseDate(startTime).getTime() > DateUtils.parseDate(endTime).getTime()) {
            return error("开始时间大于结束时间");
        }

        buryingPointDayService.dataClean(startTime, endTime);
        return success("清洗开始执行");
    }

    /**
     * 清洗buryingPointHour表
     *
     * @return
     */
    @RequestMapping("/buryingPointHour")
    @ResponseBody
    public Object buryingPointHour(HttpServletRequest request) {
        String startTime = request.getParameter("startTime");
        String endTime = request.getParameter("endTime");

        if (StringUtil.isBlank(startTime)) {
            return error("开始时间为空");
        }
        if (StringUtil.isBlank(endTime)) {
            return error("结束时间为空");
        }

        if (DateUtils.parseDateTime(startTime).getTime() > DateUtils.parseDateTime(endTime).getTime()) {
            return error("开始时间大于结束时间");
        }

        buryingPointHourService.dataClean(startTime, endTime);
        return success("清洗开始执行");
    }

}
