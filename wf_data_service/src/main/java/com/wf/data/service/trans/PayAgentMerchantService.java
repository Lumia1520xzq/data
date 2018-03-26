package com.wf.data.service.trans;

import com.wf.core.service.CrudService;
import com.wf.data.dao.trans.PayAgentMerchantDao;
import com.wf.data.dao.trans.entity.PayAgentMerchant;
import org.springframework.stereotype.Service;

/**
 * 支付渠道对应支付方式
 *
 * @author shihui
 * @date 2018/3/28
 */
@Service
public class PayAgentMerchantService extends CrudService<PayAgentMerchantDao, PayAgentMerchant> {
}
