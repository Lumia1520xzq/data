package com.wf.data.controller.api;

import com.alibaba.fastjson.JSONArray;
import com.wf.data.common.DataBaseController;
import com.wf.data.dao.datarepo.entity.DatawareUserLabel;
import com.wf.data.service.DatawareUserLabelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author shihui
 * @date 2018/6/13
 */
@CrossOrigin(maxAge = 3600)
@RequestMapping("/data/api/userLabel")
@RestController
public class UserLabelController extends DataBaseController {

    @Autowired
    private DatawareUserLabelService userLabelService;

    @RequestMapping(value = "/getAllLabels", method = RequestMethod.GET)
    public String getAllLabels(@RequestParam String userId) {
        DatawareUserLabel userLabel  =userLabelService.getUserLabelByUserId(Long.parseLong(userId));

        return JSONArray.toJSONString(userLabel);
    }
}
