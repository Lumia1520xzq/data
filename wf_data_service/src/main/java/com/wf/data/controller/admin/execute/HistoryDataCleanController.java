package com.wf.data.controller.admin.execute;

import com.wf.core.web.base.ExtJsController;
import com.wf.data.common.utils.DateUtils;
import com.wf.data.service.business.ChannelInfoHourService;
import com.wf.data.service.business.PlatUserSignHourService;
import jodd.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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

        if(DateUtils.parseDateTime(startTime).getTime() > DateUtils.parseDateTime(endTime).getTime()){
            return error("开始时间大于结束时间");
        }

        List<String> datelist = DateUtils.getDateList(startTime, endTime);
        channelInfoHourService.dataClean(datelist);
        return success("清洗开始执行");
    }


    /**
     * 清洗channelInfoHour表
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

        if(DateUtils.parseDateTime(startTime).getTime() > DateUtils.parseDateTime(endTime).getTime()){
            return error("开始时间大于结束时间");
        }

        platUserSignHourService.dataClean(startTime, endTime);
        return success("清洗开始执行");
    }


}
