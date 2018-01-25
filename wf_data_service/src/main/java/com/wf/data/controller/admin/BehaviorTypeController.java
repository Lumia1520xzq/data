package com.wf.data.controller.admin;

import com.alibaba.fastjson.JSONObject;
import com.wf.core.persistence.Page;
import com.wf.core.utils.type.NumberUtils;
import com.wf.core.utils.type.StringUtils;
import com.wf.core.web.base.ExtJsController;
import com.wf.data.dao.data.entity.BehaviorType;
import com.wf.data.service.BehaviorTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/data/admin/behaviorType")
public class BehaviorTypeController extends ExtJsController {

    @Autowired
    private BehaviorTypeService behaviorTypeService;

    /**
     * 列表
     *
     * @return
     */
    @RequestMapping("/list")
    public Object list() {
        Page<BehaviorType> page = getPage(BehaviorType.class);
        return dataGrid(behaviorTypeService.findPage(page));
    }

    /**
     * 保存
     *
     * @return
     */
    @RequestMapping("/save")
    public Object save() {
        BehaviorType form = getForm(BehaviorType.class);
        if (form == null) {
            return error("请求参数错误");
        }

        BehaviorType behaviorType = behaviorTypeService.get(form.getId());
        if (behaviorType != null) {
            //判断是否有子节点
            if (form.getParentEventId() == Long.parseLong(form.getSubEventId())) {
                return "父节点不能选择自己";
            }

            //判断该节点下是否有子节点
            List<BehaviorType> subUicBehaviorTypeList = behaviorTypeService.findByParentEventId(behaviorType.getEventId());
            if (subUicBehaviorTypeList != null && subUicBehaviorTypeList.size() > 0) {
                if (behaviorType.getEventId().longValue() != form.getEventId().longValue() || behaviorType.getParentEventId().longValue() != form.getParentEventId().longValue()) {
                    return "该节点下还有子节点，不能修改";
                }
            }
            form.setCreateTime(behaviorType.getCreateTime());
        } else {
            form.setSubEventId(form.getEventIdStr());
            form.setCreateTime(new Date());
        }
        //拼接fullname和eventId
        String eventIdStr = "";
        if (form.getParentEventId() == null || form.getParentEventId() == 0L) {
            form.setParentEventId(0L);
            eventIdStr = form.getSubEventId() + "";
        } else {
            eventIdStr = form.getParentEventId() + "" + form.getSubEventId();
        }
        int length = eventIdStr.length();
        long eventId = Long.parseLong(eventIdStr);
        List<String> list = new ArrayList<String>();
        list.add(form.getName());
        BehaviorType tmp = form;
        while (true) {
            tmp = behaviorTypeService.getByEventId(tmp.getParentEventId());
            if (tmp == null) {
                break;
            }
            list.add(tmp.getName());
        }
        StringBuffer fullName = new StringBuffer();
        for (int i = list.size() - 1; i >= 0; i--) {
            if (i == 0) {
                fullName.append(list.get(i));
            } else {
                fullName.append(list.get(i) + "-");
            }
        }
        form.setEventId(eventId);
        form.setBehaviorLevel(length / 2);
        form.setFullName(fullName.toString());
        form.setUpdateTime(new Date());
        behaviorTypeService.save(form);
        return success();
    }

    /**
     * 删除
     *
     * @return
     */
    @RequestMapping("/delete")
    public Object delete() {
        BehaviorType entity = getForm(BehaviorType.class);
        behaviorTypeService.delete(entity);
        return success();
    }

    /**
     * 获取父事件ID
     *
     * @return
     */
    @RequestMapping("/findAll")
    public Object findAll() {
        BehaviorType behaviorType = new BehaviorType();
        JSONObject json = getRequestJson();

        String keyword = null;
        JSONObject data = json.getJSONObject("data");
        if (data != null && data.size() > 0) {
            keyword = data.getString("data");
        }

        if (StringUtils.isNotBlank(keyword)) {
            keyword = keyword.trim();
            if (NumberUtils.isDigits(keyword)) {
                behaviorType.setEventId(Long.parseLong(keyword));
            } else {
                behaviorType.setName(keyword);
            }
        }
        return behaviorTypeService.findPage(behaviorType);
    }

    @RequestMapping("/getParentEvents")
    public Object getParentChannels() {
        JSONObject json = getRequestJson();

        String keyword = null;
        JSONObject data = json.getJSONObject("data");
        if (data != null) {
            keyword = data.getString("data");
        }
        BehaviorType dto = new BehaviorType();
        if (StringUtils.isNotBlank(keyword)) {
            keyword = keyword.trim();
            if (NumberUtils.isDigits(keyword)) {
                dto.setId(NumberUtils.toLong(keyword));
            } else {
                dto.setName(keyword);
            }
        }
        List<BehaviorType> list = behaviorTypeService.findList(dto, 1000);

        for (BehaviorType cInfo : list) {
            cInfo.setName(cInfo.getName() + "(" + cInfo.getEventId() + ")");
        }
        return list;
    }

    @RequestMapping("/getChildEvents")
    public Object getChildEvents() {
        JSONObject json = getRequestJson();

        String keyword = null;
        JSONObject data = json.getJSONObject("data");
        if (data != null) {
            keyword = data.getString("parentEventId");
        }
        BehaviorType dto = new BehaviorType();
        if (StringUtils.isNotBlank(keyword)) {
            keyword = keyword.trim();
            if (NumberUtils.isDigits(keyword)) {
                dto.setParentEventId(NumberUtils.toLong(keyword));
            } else {
                dto.setName(keyword);
            }
        }
        List<BehaviorType> list = behaviorTypeService.findList(dto, 1000);

        for (BehaviorType cInfo : list) {
            cInfo.setName(cInfo.getName() + "(" + cInfo.getEventId() + ")");
        }
        return list;
    }
}
