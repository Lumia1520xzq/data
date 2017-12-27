package com.wf.data.common.constants;

/**
 * @author zk
 */
public interface DataConstants {

    /**
     * 否
     */
    int NO = 0;
    String GAME_BETTING_DATA_RECEIVER = "game_betting_data_receiver";
    String CLUB_BETTING_DATA_RECEIVER = "club_betting_data_receiver";

    /**
     * 平台数据报表
     */
    String PLATFORM_DATA_RECEIVER="platform_data_receiver";
    /**
     * 游戏数据报表
     */
    String GAME_DATA_RECEIVER="game_data_receiver";
    /**
     * 用户信息列表
     */
    String USERINFO_DATA_RECEIVER="userinfo_data_receiver";
    /**
     * 套圈数据报表
     */
    String QUOITS_DATA_RECEIVER="quoits_data_receiver";
    /**
     * 数据日检
     */
    String DAILY_DATA_RECEIVER="daily_data_receiver";
    /**
     * 每日关键数据
     */
    String DAILY_KEY_DATA="daily_key_data";
    /**
     * 每日各渠道关键数据
     */
    String CHANNEL_DATA_RECEIVER="channel_data_receiver";
    /**
     * 同ip个数设置
     */
    String MONITOR_IP_COUNT = "monitor_ip_count";

    String MONITOR_RISK_WF_IPS = "monitor_risk_wf_ips";
    /**
     * ip邮件接收人
     */
    String MONITOR_IP_RECEIVER = "monitor_ip_receiver";

    /**
     *同ip下登录用户数
     */
    String MONITOR_RISK_IP_DAY = "monitor_risk_ip_day";
    /**
     * 用户累计充值金额
     */
    String MONITOR_RISK_USER_RECHARGE = "monitor_risk_user_recharge";

    /**
     * 同步周期
     * 暂定前1小时
     */
    String DATA_DATAWARE_HOURS = "data_dataware_hours";

    String DATA_DATAWARE_FLAG = "data_dataware_flag";

    String DATA_DATAWARE_DAYS = "data_dataware_days";

    String DATA_DATAWARE_UIC_GROUP = "data_dataware_uic_group";

    String DATA_DATAWARE_BETTING_DAY = "data_dataware_betting_day";
    String DATA_DATAWARE_GAMETYPE = "data_dataware_gametype";
    /**
     * 埋点历史数据日期
     */
    String DATA_DATAWARE_BURYING_HISTORY_HOUR = "data_dataware_burying_history_hour";
    String DATA_DATAWARE_BURYING_HISTORY_DAY = "data_dataware_burying_history_day";

    String DATA_DATAWARE_BURYING_FLAG_HOUR = "data_dataware_burying_flag_hour";
    String DATA_DATAWARE_BURYING_FLAG_DAY = "data_dataware_burying_flag_day";

    String DATA_DATAWARE_SIGN_FLAG_DAY = "data_dataware_sign_flag_day";
    String DATA_DATAWARE_SIGN_HISTORY_DAY = "data_dataware_sign_history_day";

    String DATA_DATAWARE_USERINFO_FLAG_DAY = "data_dataware_userinfo_flag_day";
    String DATA_DATAWARE_USERINFO_HISTORY_DAY = "data_dataware_userinfo_history_day";

    String DATA_DATAWARE_CONVERT_FLAG_DAY = "data_dataware_convert_flag_day";
    String DATA_DATAWARE_CONVERT_HISTORY_DAY = "data_dataware_convert_history_day";

    String DATA_DATAWARE_CONVERT_FLAG_HOUR = "data_dataware_convert_flag_hour";
    String DATA_DATAWARE_CONVERT_HISTORY_HOUR = "data_dataware_convert_history_hour";


    /**
     * 每日推送
     */
    String DAILY_INTRODUCE_RECEIVER = "daily_introduce_receiver";

    String MONITOR_GRAY_USER_RECHARGE = "monitor_gray_user_recharge";

    String DATA_FISH_BEGIN_DAY = "data_fish_begin_day";

}
