package com.wf.data.common.constants;

/**
 * @author zk
 */
public interface DataConstants {

    /**
     * 是
     */
    int YES = 1;

    /**
     * 否
     */
    int NO = 0;


    String GAME_BETTING_DATA_RECEIVER = "game_betting_data_receiver";
    String CLUB_BETTING_DATA_RECEIVER = "club_betting_data_receiver";

    //平台数据报表
    String PLATFORM_DATA_RECEIVER="platform_data_receiver";
    //游戏数据报表
    String GAME_DATA_RECEIVER="game_data_receiver";
    //用户信息列表
    String USERINFO_DATA_RECEIVER="userinfo_data_receiver";
    //套圈数据报表
    String QUOITS_DATA_RECEIVER="quoits_data_receiver";
    //数据日检
    String DAILY_DATA_RECEIVER="daily_data_receiver";
    //每日关键数据
    String DAILY_KEY_DATA="daily_key_data";
    //每日各渠道关键数据
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

    String MONITOR_GRAY_USER_RECHARGE = "monitor_gray_user_recharge";


    String DATA_FISH_BEGIN_DAY = "data_fish_begin_day";





}
