package com.wf.data.controller.admin.userManual;

import com.wf.core.persistence.Page;
import com.wf.core.utils.excel.ExportExcel;
import com.wf.core.utils.type.DateUtils;
import com.wf.core.utils.type.StringUtils;
import com.wf.core.web.base.ExtJsController;
import com.wf.data.common.constants.DataConstants;
import com.wf.data.dao.data.entity.TargetDefine;
import com.wf.data.service.DataDictService;
import com.wf.data.service.userManual.TargetDefineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/data/usermanual/targetdefine")
public class TargetDefineController extends ExtJsController {
    @Autowired
    private TargetDefineService targetDefineService;
    @Autowired
    private DataDictService dataDictService;
    /**
     * 返水列表
     *
     * @return
     */
    @RequestMapping("/list")
    public Object list() {
        Page<TargetDefine> page = getPage(TargetDefine.class);
        targetDefineService.findPage(page);
        return dataGrid(page);
    }


    /**
     * 导出
     * @param targetType
     * @param response
     */
    @RequestMapping("/export")
    public void export(@RequestParam String targetType, HttpServletResponse response) {
        try {
            TargetDefine targetDefine = new TargetDefine();
            if (!targetType.equals("undefined") && !targetType.equals("null") && StringUtils.isNotBlank(targetType)) {
                targetDefine.setTargetType(Integer.parseInt(targetType));
            }
            List<TargetDefine> targetDefines = targetDefineService.findList(targetDefine, 99999999);

            if (targetDefines != null) {
                for (TargetDefine tDefine : targetDefines) {
                    //事记类型名称
                    if (tDefine.getTargetType() != null) {
                        String targetTypeName = dataDictService.getDictByValue(DataConstants.TARGET_TYPE, tDefine.getTargetType()).getLabel();
                        tDefine.setTargetTypeName(targetTypeName);
                    }
                }
            }

            String fileName = "用户手册指标定义" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
            new ExportExcel("用户手册指标定义", TargetDefine.class).setDataList(targetDefines).write(response, fileName).dispose();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
