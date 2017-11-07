package com.wf.data.controller.request;

import com.wf.core.persistence.DataEntity;

import javax.validation.constraints.NotNull;

public class BuryingPointRequest extends DataEntity {
    private static final long serialVersionUID = -1;
    @NotNull(message = "埋点类型不能为空")
    private Integer buryingType;
    @NotNull(message = "游戏类型不能为空")
    private Integer gameType;

    public Integer getBuryingType() {
        return buryingType;
    }

    public void setBuryingType(Integer buryingType) {
        this.buryingType = buryingType;
    }

    public Integer getGameType() {
        return gameType;
    }

    public void setGameType(Integer gameType) {
        this.gameType = gameType;
    }

}