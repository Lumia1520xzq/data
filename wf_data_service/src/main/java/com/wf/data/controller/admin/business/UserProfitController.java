package com.wf.data.controller.admin.business;

import com.alibaba.fastjson.JSONObject;
import com.wf.core.web.base.ExtJsController;
import com.wf.data.dao.datarepo.entity.DatawareUserInfoExtendStatistics;
import com.wf.data.dao.mycatuic.entity.UicUser;
import com.wf.data.service.UicUserService;
import com.wf.data.service.data.DatawareUserInfoExtendStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: lcs
 * @date: 2018/03/12
 */
@RestController
@RequestMapping("/data/admin/business/profit")
public class UserProfitController extends ExtJsController {

    @Autowired
    private DatawareUserInfoExtendStatisticsService statisticsService;
    @Autowired
    private UicUserService uicUserService;

    @RequestMapping(value = {"list"})
    public Object listData() {
        JSONObject json = getRequestJson();
        JSONObject data = json.getJSONObject("data");

        long start = json.getLongValue("start");
        long length = json.getLongValue("limit");
        if (length == 0L) {
            length = 20L;
        }

        Long userId = null;
        if (data != null) {
            userId = data.getLong("userId");
        }
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("start", start);
        map.put("length", length);

        List<DatawareUserInfoExtendStatistics> list = statisticsService.getProfitByUserId(map);
        for (DatawareUserInfoExtendStatistics dto : list) {
            if (dto.getUserId() != null) {
                UicUser user = uicUserService.get(dto.getUserId());
                if (null != user) {
                    dto.setUserName(user.getNickname());
                }
            }
        }
        return list;
    }


}
