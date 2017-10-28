package com.wf.uic.crudserivce;

import com.wf.core.service.CrudService;
import com.wf.uic.dao.entity.mysql.UicSensitive;
import com.wf.uic.dao.mysql.UicSensitiveDao;
import org.springframework.stereotype.Service;

import java.util.List;
/**
 * 敏感词
 * @author fxy
 * @date 2017/02/15
 */
@Service
public class UicSensitiveCrudService extends CrudService<UicSensitiveDao, UicSensitive> {
    public List<String> findAll() {
        return dao.findAll();
    }
}


















