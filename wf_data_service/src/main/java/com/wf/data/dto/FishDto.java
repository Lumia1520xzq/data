/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wf.data.dto;

import com.wf.core.utils.excel.ExcelField;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author Administrator
 */
public class FishDto implements Serializable {

    private static final long serialVersionUID = -1L;
    //日期
    private String dates;
    //渠道
    private Long channel;
    //激活人数
    private Long activenum;
    //注册人数
    private Long regmemnum;
    //登陆人数
    private Long lognum;
    //投注人数
    private Long betmem;
    //投注次数
    private Long betcnt;
    //投注金额
    private Long betamt;
    //返奖金额
    private Long resultamt;
    //提交支付
    private Long subpaymem;
    //充值人数
    private Long paymemnum;
    //充值金额（含限时兑换）
    private BigDecimal payamount;
    //出口人数
    private Long exchangemem;
    //成本
    private BigDecimal exchangeamount;
    //新增登陆
    private Long newlogmem;
    //新增首日
    private Long firstdaypaymem;
    //新增七日内付费
    private Long first7daypaymem;
    //新增首日投注
    private Long firstdaybetmem;

    //新增次日留存人数
    private Long newsecondretent;

    private Double newsecondretentRate;
    //新增7日留存人数
    private Long newsevenretent;
    //新增7日内留存人数
    private Long newwithinseven;
    //全量次日留存人数
    private Long allsecondretent;
    //全量次日留存率
    private Double allsecondretentRate;

    //全量7日留存人数
    private Long allsevenretent;
    //全量7日内留存人数
    private Long allwithinseven;

    private String channelName;
    //投注转化率
    private Double bettingRate;
    //ARPU
    private Double bettingArpu;
    private Double resultRate;
    //流水差
    private Long bettingDiff;

    @ExcelField(title = "新用户次留(%)", type = 1, align = 2, sort = 15)
    public Double getNewsecondretentRate() {
        return newsecondretentRate;
    }

    public void setNewsecondretentRate(Double newsecondretentRate) {
        this.newsecondretentRate = newsecondretentRate;
    }

    @ExcelField(title = "全量次留(%)", type = 1, align = 2, sort = 16)
    public Double getAllsecondretentRate() {
        return allsecondretentRate;
    }

    public void setAllsecondretentRate(Double allsecondretentRate) {
        this.allsecondretentRate = allsecondretentRate;
    }

    @ExcelField(title = "渠道", type = 1, align = 2, sort = 2)
    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    @ExcelField(title = "投注转化率(%)", type = 1, align = 2, sort = 11)
    public Double getBettingRate() {
        return bettingRate;
    }

    public void setBettingRate(Double bettingRate) {
        this.bettingRate = bettingRate;
    }

    @ExcelField(title = "ARPU", type = 1, align = 2, sort = 12)
    public Double getBettingArpu() {
        return bettingArpu;
    }

    public void setBettingArpu(Double bettingArpu) {
        this.bettingArpu = bettingArpu;
    }

    @ExcelField(title = "返奖率(%)", type = 1, align = 2, sort = 13)
    public Double getResultRate() {
        return resultRate;
    }

    public void setResultRate(Double resultRate) {
        this.resultRate = resultRate;
    }

    @ExcelField(title = "流水差", type = 1, align = 2, sort = 14)
    public Long getBettingDiff() {
        return bettingDiff;
    }

    public void setBettingDiff(Long bettingDiff) {
        this.bettingDiff = bettingDiff;
    }

    @ExcelField(title = "日期", type = 1, align = 2, sort = 1)
    public String getDates() {
        return dates;
    }

    public void setDates(String dates) {
        this.dates = dates;
    }

    public Long getChannel() {
        return channel;
    }

    public void setChannel(Long channel) {
        this.channel = channel;
    }

    @ExcelField(title = "激活", type = 1, align = 2, sort = 3)
    public Long getActivenum() {
        return activenum;
    }

    public void setActivenum(Long activenum) {
        this.activenum = activenum;
    }

    @ExcelField(title = "注册", type = 1, align = 2, sort = 4)
    public Long getRegmemnum() {
        return regmemnum;
    }

    public void setRegmemnum(Long regmemnum) {
        this.regmemnum = regmemnum;
    }

    @ExcelField(title = "登陆", type = 1, align = 2, sort = 5)
    public Long getLognum() {
        return lognum;
    }

    public void setLognum(Long lognum) {
        this.lognum = lognum;
    }

    public Long getBetmem() {
        return betmem;
    }

    public void setBetmem(Long betmem) {
        this.betmem = betmem;
    }

    public Long getBetcnt() {
        return betcnt;
    }

    public void setBetcnt(Long betcnt) {
        this.betcnt = betcnt;
    }

    @ExcelField(title = "投注金额", type = 1, align = 2, sort = 7)
    public Long getBetamt() {
        return betamt;
    }

    public void setBetamt(Long betamt) {
        this.betamt = betamt;
    }

    public Long getResultamt() {
        return resultamt;
    }

    public void setResultamt(Long resultamt) {
        this.resultamt = resultamt;
    }

    public Long getSubpaymem() {
        return subpaymem;
    }

    public void setSubpaymem(Long subpaymem) {
        this.subpaymem = subpaymem;
    }

    @ExcelField(title = "充值人数", type = 1, align = 2, sort = 8)
    public Long getPaymemnum() {
        return paymemnum;
    }

    public void setPaymemnum(Long paymemnum) {
        this.paymemnum = paymemnum;
    }

    @ExcelField(title = "充值金额", type = 1, align = 2, sort = 9)
    public BigDecimal getPayamount() {
        return payamount;
    }

    public void setPayamount(BigDecimal payamount) {
        this.payamount = payamount;
    }

    public Long getExchangemem() {
        return exchangemem;
    }

    public void setExchangemem(Long exchangemem) {
        this.exchangemem = exchangemem;
    }

    @ExcelField(title = "成本", type = 1, align = 2, sort = 10)
    public BigDecimal getExchangeamount() {
        return exchangeamount;
    }

    public void setExchangeamount(BigDecimal exchangeamount) {
        this.exchangeamount = exchangeamount;
    }

    @ExcelField(title = "新增登陆", type = 1, align = 2, sort = 6)
    public Long getNewlogmem() {
        return newlogmem;
    }

    public void setNewlogmem(Long newlogmem) {
        this.newlogmem = newlogmem;
    }

    public Long getFirstdaypaymem() {
        return firstdaypaymem;
    }

    public void setFirstdaypaymem(Long firstdaypaymem) {
        this.firstdaypaymem = firstdaypaymem;
    }

    public Long getFirst7daypaymem() {
        return first7daypaymem;
    }

    public void setFirst7daypaymem(Long first7daypaymem) {
        this.first7daypaymem = first7daypaymem;
    }

    public Long getFirstdaybetmem() {
        return firstdaybetmem;
    }

    public void setFirstdaybetmem(Long firstdaybetmem) {
        this.firstdaybetmem = firstdaybetmem;
    }


    public Long getNewsecondretent() {
        return newsecondretent;
    }

    public void setNewsecondretent(Long newsecondretent) {
        this.newsecondretent = newsecondretent;
    }

    public Long getNewsevenretent() {
        return newsevenretent;
    }

    public void setNewsevenretent(Long newsevenretent) {
        this.newsevenretent = newsevenretent;
    }

    public Long getNewwithinseven() {
        return newwithinseven;
    }

    public void setNewwithinseven(Long newwithinseven) {
        this.newwithinseven = newwithinseven;
    }

    public Long getAllsecondretent() {
        return allsecondretent;
    }

    public void setAllsecondretent(Long allsecondretent) {
        this.allsecondretent = allsecondretent;
    }

    public Long getAllsevenretent() {
        return allsevenretent;
    }

    public void setAllsevenretent(Long allsevenretent) {
        this.allsevenretent = allsevenretent;
    }

    public Long getAllwithinseven() {
        return allwithinseven;
    }

    public void setAllwithinseven(Long allwithinseven) {
        this.allwithinseven = allwithinseven;
    }

    @Override
    public String toString() {
        return "FishDto{" +
                "dates='" + dates + '\'' +
                ", channel=" + channel +
                ", activenum=" + activenum +
                ", regmemnum=" + regmemnum +
                ", lognum=" + lognum +
                ", betmem=" + betmem +
                ", betcnt=" + betcnt +
                ", betamt=" + betamt +
                ", resultamt=" + resultamt +
                ", subpaymem=" + subpaymem +
                ", paymemnum=" + paymemnum +
                ", payamount=" + payamount +
                ", exchangemem=" + exchangemem +
                ", exchangeamount=" + exchangeamount +
                ", newlogmem=" + newlogmem +
                ", firstdaypaymem=" + firstdaypaymem +
                ", first7daypaymem=" + first7daypaymem +
                ", firstdaybetmem=" + firstdaybetmem +
                ", newsecondretent=" + newsecondretent +
                ", newsevenretent=" + newsevenretent +
                ", newwithinseven=" + newwithinseven +
                ", allsecondretent=" + allsecondretent +
                ", allsevenretent=" + allsevenretent +
                ", allwithinseven=" + allwithinseven +
                ", channelName='" + channelName + '\'' +
                ", bettingRate=" + bettingRate +
                ", bettingArpu=" + bettingArpu +
                ", resultRate=" + resultRate +
                ", bettingDiff=" + bettingDiff +
                '}';
    }
}
