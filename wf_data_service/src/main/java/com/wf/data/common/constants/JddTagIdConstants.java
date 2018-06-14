package com.wf.data.common.constants;

/**
 * @author lcs
 * @date 2018/03/06
 */
public interface JddTagIdConstants {

    /**
     * 经过游戏平台的全量用户彩票ID
     */
    String ALL_GAME_USER = "302";

    /**
     * 昨日新增用户彩票ID
     */
    String YESTERDAY_NEW_GAME_USER = "303";

    /**
     * 回流用户彩票ID
     */
    String BACK_FLOW_USER = "370";

    /**
     * 未付费老用户彩票ID
     */
    String UNPAY_OLD_USER = "371";

    /**
     * 付费用户，7日内没有付费用户彩票ID
     */
    String UNPAY_SEVEN_DAY = "385";

    /**
     * 新注册用户7天活跃且未付费
     */
    String FOUR_ONE_SEVEN = "417";

    /**
     * 7天未活跃用户
     */
    String FOUR_ONE_EIGHT = "418";

    /**
     * 10天活跃投注用户
     */
    String FOUR_ONE_NINE = "419";

    /**
     * 老用户未付费预测流失
     */
    String SIX_FOUR_SIX = "646";

    /**
     * 老用户小户预测流失
     */
    String SIX_FOUR_SEVEN = "647";

    /**
     * 老用户中户预测流失
     */
    String SIX_FOUR_EIGHT = "648";

    /**
     * 老用户大户预测流失
     */
    String SIX_FOUR_NINE = "649";

    /**
     * 近14日未活跃用户
     */
    String SIX_FIVE_NINE = "659";

    /**
     * 近30日未活跃用户
     */
    String SIX_SIX_ZERO = "660";
}
