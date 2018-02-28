package com.wf.data.controller.admin.thirdChannel;

import com.alibaba.fastjson.JSONObject;
import com.wf.core.persistence.Page;
import com.wf.core.web.base.ExtJsController;
import com.wf.data.dao.mall.entity.MallBizLog;
import com.wf.data.dao.mycatuic.entity.UicUser;
import com.wf.data.service.UicUserService;
import com.wf.data.service.mall.MallBizLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author shihui
 * @date 2018/1/25
 */

@RestController
@RequestMapping("/data/admin/channel/regChannelRecharge")
public class RegChannelRechargeController extends ExtJsController {

    @Autowired
    private MallBizLogService mallBizLogService;

    @Autowired
    private UicUserService uicUserService;

    /**
     * 注册渠道充值查询列表
     *
     * @return
     */
    @RequestMapping("list")
    public Object listData() {
        JSONObject json = getRequestJson();
        Long userId = null;
        Long parentId = null;
        String beginDate = null;
        String endDate = null;
        Long start = null;
        Long length = null;

        JSONObject data = json.getJSONObject("data");
        if (data != null) {
            userId = data.getLong("userId");
            parentId = data.getLong("parentId");
            beginDate = data.getString("beginDate");
            endDate = data.getString("endDate");
            start = json.getLongValue("start");
            length = json.getLongValue("limit");
        }

        MallBizLog note = new MallBizLog();
        note.setUserId(userId);
        note.setParentId(parentId);
        note.setBeginDate(beginDate);
        note.setEndDate(endDate);
        note.setStatus(8);

        //查询购买记录表
        if (note.getParentId() == null) {
            return new Page<MallBizLog>();
        }
            Page<MallBizLog> pageList = mallBizLogService.findPage(new Page<MallBizLog>(note, start, length));
        for (MallBizLog mallBizLog : pageList.getData()) {
            UicUser user = uicUserService.getByUserId(mallBizLog.getUserId());
            if (user != null) {
                mallBizLog.setUserName(user.getNickname());
            }
        }
        return dataGrid(pageList);
    }

    /**
     * 计算充值总额
     *
     * @param dataParam
     * @return
     */
    @RequestMapping("sumData")
    public Object countData(@RequestBody Map<String, Object> dataParam) {
        dataParam.put("status", 8);
        Double data = mallBizLogService.getSumRecharge(dataParam);
        return data == null ? 0D : data;

    }
}
