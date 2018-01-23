package com.wf.data.controller.admin.execute;

import com.wf.core.web.base.ExtJsController;
import com.wf.data.common.utils.DateUtils;
import com.wf.data.service.business.ChannelInfoHourService;
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

        if(StringUtil.isBlank(startTime)){
            return error("开始时间为空");
        }
        if(StringUtil.isBlank(endTime)){
            return error("结束时间为空");
        }

        List<String> datelist = DateUtils.getDateList(startTime, endTime);

        for(String searchDate : datelist){
            if (datelist.get(0) == searchDate) {

            } else if (searchDate == datelist.get(datelist.size() - 1)) {
            } else {
            }
        }

        return success("清洗开始执行");
    }


}
