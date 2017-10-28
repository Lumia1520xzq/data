package com.wf.uic.common.hander;

import com.wf.core.cache.CacheData;
import com.wf.core.cache.CacheHander;
import com.wf.core.cache.CacheKey;
import com.wf.core.sensitive.SensitiveWordHander;
import com.wf.core.sensitive.WordsTransfer;
import com.wf.uic.common.constants.UicCacheKey;
import com.wf.uic.crudserivce.UicSensitiveCrudService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;

/**
 *
 */
@Component
public class SensitiveHander implements InitializingBean {
    @Autowired
    private SensitiveWordHander sensitiveWordHander;
    @Autowired
    private UicSensitiveCrudService uicSensitiveService;
    @Autowired
    private CacheHander cacheHander;

    @Override
    public void afterPropertiesSet() throws Exception {
        sensitiveWordHander.init(new WordsTransfer() {
            @Override
            public Collection<String> getWords() {
                return uicSensitiveService.findAll();
            }

            @Override
            public Map<String, String> getSensitive() {
                return cacheHander.cache(UicCacheKey.UIC_SENSITIVE_CONTENT.key(),
                        ()-> sensitiveWordHander.build(), CacheKey.DAY_2);
            }
        });
    }
}
