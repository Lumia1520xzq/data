package com.wf.data.controller.admin.board;

import com.wf.core.persistence.Page;
import com.wf.core.utils.excel.ExportExcel;
import com.wf.core.utils.type.DateUtils;
import com.wf.core.utils.type.StringUtils;
import com.wf.core.web.base.ExtJsController;
import com.wf.data.common.constants.DataConstants;
import com.wf.data.dao.data.entity.GrowTest;
import com.wf.data.service.DataDictService;
import com.wf.data.service.data.GrowTestService;
import org.jasig.cas.client.util.AssertionHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 增长测试管理
 *
 * @author onlymark
 */
@RestController
@RequestMapping("/data/growtest/view")
public class GrowTestController extends ExtJsController {
    @Autowired
    private GrowTestService growTestService;
    @Autowired
    private DataDictService dataDictService;

    /**
     * 列表
     *
     * @return
     */
    @RequestMapping("/list")
    public Object list() {
        Page<GrowTest> page = getPage(GrowTest.class);
        growTestService.findPage(page);
        List<GrowTest> growTests = page.getData();
        for (GrowTest growTest: growTests) {
            growTest.setShowUpdateTime(DateUtils.formatDate(growTest.getUpdateTime(),DateUtils.DATE_PATTERN));
        }
        return dataGrid(page);
    }

    /**
     * 保存
     * @return
     */
    @RequestMapping("/save")
    public Object save() {
        GrowTest form = getForm(GrowTest.class);
        form.setOperationUsername(AssertionHolder.getAssertion().getPrincipal().getName());
        if (form == null) {
            return error("请求参数错误");
        }
        growTestService.save(form);
        return success();
    }

    /**
     * 删除
     * @return
     */
    @RequestMapping("/delete")
    public Object delete() {
        GrowTest entity = getForm(GrowTest.class);
        growTestService.delete(entity);
        return success();
    }

    
    @RequestMapping("/export")
    public void export(@RequestParam String point, @RequestParam String testChannel, @RequestParam String beginDate, @RequestParam String operationUsername, HttpServletResponse response) {
        try {
            GrowTest growTest = new GrowTest();
            if (!point.equals("undefined") && !point.equals("null") && StringUtils.isNotBlank(point)) {
                growTest.setPoint(point);
            }
            if (!testChannel.equals("undefined") && !testChannel.equals("null") && StringUtils.isNotBlank(testChannel)) {
                growTest.setTestChannel(testChannel);
            }
            if (!beginDate.equals("undefined") && !beginDate.equals("null") && StringUtils.isNotBlank(beginDate)) {
                growTest.setBeginDate(com.wf.data.common.utils.DateUtils.formatGTMDate(beginDate, "yyyy-MM-dd HH:mm:ss"));
            }
            if (!operationUsername.equals("undefined") && !operationUsername.equals("null") && StringUtils.isNotBlank(operationUsername)) {
                growTest.setOperationUsername(operationUsername);
            }
            List<GrowTest> growTests = growTestService.findList(growTest, 99999999);

            if (growTests != null) {
                for (GrowTest grow : growTests) {
                    //事记类型名称
                    if (grow.getTestTypeId() != null) {
                        String testTypeName = dataDictService.getDictByValue(DataConstants.TEST_TYPE, grow.getTestTypeId().intValue()).getLabel();
                        grow.setTestTypeName(testTypeName);
                    }
                    if(grow.getUpdateTime() != null){
                        grow.setShowUpdateTime(DateUtils.formatDate(grow.getUpdateTime(),DateUtils.DATE_PATTERN));
                    }
                }
            }

            String fileName = "增长测试管理数据" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
            new ExportExcel("增长测试管理数据", GrowTest.class).setDataList(growTests).write(response, fileName).dispose();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
