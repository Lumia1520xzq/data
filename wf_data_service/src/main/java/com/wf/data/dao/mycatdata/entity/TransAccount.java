package com.wf.data.dao.mycatdata.entity;

import com.wf.core.persistence.DataEntity;

/**
 * @author shihui
 * @date 2018/3/6
 */
public class TransAccount extends DataEntity {
    private static final long serialVersionUID = -1;

    private Long userId;
    private Double grandAmount;
    private Double totalAmount;
    private Double useAmount;
    private Double noUseAmount;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Double getGrandAmount() {
        return grandAmount;
    }

    public void setGrandAmount(Double grandAmount) {
        this.grandAmount = grandAmount;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Double getUseAmount() {
        return useAmount;
    }

    public void setUseAmount(Double useAmount) {
        this.useAmount = useAmount;
    }

    public Double getNoUseAmount() {
        return noUseAmount;
    }

    public void setNoUseAmount(Double noUseAmount) {
        this.noUseAmount = noUseAmount;
    }
}
