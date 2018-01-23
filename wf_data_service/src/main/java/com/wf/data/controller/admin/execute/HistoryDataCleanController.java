package com.wf.data.controller.admin.execute;

import com.alibaba.fastjson.JSONObject;
import com.wf.core.web.base.ExtJsController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 历史数据补充
 *
 * @author liucs
 */
@RestController
@RequestMapping("/data/admin/dataClean")
public class HistoryDataCleanController extends ExtJsController {


    /**
     * 配置项列表
     *
     * @return
     */
    @RequestMapping("/channelInfoHour")
    public Object channelInfoHour() {
        JSONObject json = getRequestJson();

        return success("清洗开始执行");
    }


}
