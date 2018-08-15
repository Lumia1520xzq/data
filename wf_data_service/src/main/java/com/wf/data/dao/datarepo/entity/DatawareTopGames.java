package com.wf.data.dao.datarepo.entity;


import com.wf.core.persistence.DataEntity;

public class DatawareTopGames extends DataEntity {
    private static final long serialVersionUID = -1;
    private Long idN;
    private Integer gameTypeN;
    private Long numberOfBetsN;
    private Long numberOfActiveN;
    private Double bettingHeatN;
    private Double avgSecondaryLeftN;
    private Double flowRatioN;
    private Double heatValueN;
    private String statisticalDateS;

    public Long getIdN() {
        return idN;
    }

    public void setIdN(Long idN) {
        this.idN = idN;
    }

    public Integer getGameTypeN() {
        return gameTypeN;
    }

    public void setGameTypeN(Integer gameTypeN) {
        this.gameTypeN = gameTypeN;
    }

    public Long getNumberOfBetsN() {
        return numberOfBetsN;
    }

    public void setNumberOfBetsN(Long numberOfBetsN) {
        this.numberOfBetsN = numberOfBetsN;
    }

    public Long getNumberOfActiveN() {
        return numberOfActiveN;
    }

    public void setNumberOfActiveN(Long numberOfActiveN) {
        this.numberOfActiveN = numberOfActiveN;
    }

    public Double getBettingHeatN() {
        return bettingHeatN;
    }

    public void setBettingHeatN(Double bettingHeatN) {
        this.bettingHeatN = bettingHeatN;
    }

    public Double getAvgSecondaryLeftN() {
        return avgSecondaryLeftN;
    }

    public void setAvgSecondaryLeftN(Double avgSecondaryLeftN) {
        this.avgSecondaryLeftN = avgSecondaryLeftN;
    }

    public Double getFlowRatioN() {
        return flowRatioN;
    }

    public void setFlowRatioN(Double flowRatioN) {
        this.flowRatioN = flowRatioN;
    }

    public Double getHeatValueN() {
        return heatValueN;
    }

    public void setHeatValueN(Double heatValueN) {
        this.heatValueN = heatValueN;
    }

    public String getStatisticalDateS() {
        return statisticalDateS;
    }

    public void setStatisticalDateS(String statisticalDateS) {
        this.statisticalDateS = statisticalDateS;
    }
}