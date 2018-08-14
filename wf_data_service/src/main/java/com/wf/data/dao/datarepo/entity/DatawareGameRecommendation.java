package com.wf.data.dao.datarepo.entity;


import com.wf.core.persistence.DataEntity;

public class DatawareGameRecommendation extends DataEntity {
    private static final long serialVersionUID = -1;
    private Long idN;
    private Long userIdN;
    private Long gameAn;
    private Long gameBn;
    private Double similarityN;
    private String statisticalDateS;

    public Long getIdN() {
        return idN;
    }

    public void setIdN(Long idN) {
        this.idN = idN;
    }

    public Long getUserIdN() {
        return userIdN;
    }

    public void setUserIdN(Long userIdN) {
        this.userIdN = userIdN;
    }

    public Long getGameAn() {
        return gameAn;
    }

    public void setGameAn(Long gameAn) {
        this.gameAn = gameAn;
    }

    public Long getGameBn() {
        return gameBn;
    }

    public void setGameBn(Long gameBn) {
        this.gameBn = gameBn;
    }

    public Double getSimilarityN() {
        return similarityN;
    }

    public void setSimilarityN(Double similarityN) {
        this.similarityN = similarityN;
    }

    public String getStatisticalDateS() {
        return statisticalDateS;
    }

    public void setStatisticalDateS(String statisticalDateS) {
        this.statisticalDateS = statisticalDateS;
    }
}