package com.wf.data.common.constants;

/**
 * @author shihui
 * @date 2018/4/12
 */
public interface EmailContents {

    /**
     * 队列名
     */
    String EMAIL_RABBITMQ_NAME = "email_send_queue";


    /**
     * 每小时邮件
     */
    String BETTING_INFO_ALIAS = "wf_data_task_platformGameDataPerHour";


    /**
     * 彩色版-全渠道数据统计
     */
    String CHANNEL_DATA_ALIAS = "wf_data_task_dailyColorDataForAllChannel";



    /**
     * 付费项数据分析邮件
     */
    String DAILY_PAYMENT_ANALYSE = "wf_data_task_dailyPaymentAnalyse";

    /**
     * 游戏数据日报表分析汇总
     */
    String GAME_DAILY_DATA_ALIAS = "wf_data_task_dailyReportForAllGameData";


    /**
     * 彩色版-奖多多渠道数据统计
     */
    String JDD_CHANNEL_DATA_ALIAS = "wf_data_task_dailyColorDataForJdd";


    /**
     * 套圈数据统计
     */
    String LOOP_DATA_ALIAS = "wf_data_task_dailyReportForQuoitsData";


    /**
     * 澳客用户名单
     */
    String AK_USER_DATA_ALIAS = "wf_data_task_dailyExportUserListForOkooo";


    /**
     * 金山用户名单
     */
    String JS_USER_DATA_ALIAS = "wf_data_task_dailyExportUserListForJdd";


    /**
     * 每日平台数据报表分析汇总
     */
    String PLATFORM_DAILY_DATA = "wf_data_task_dailyPlatformData";
}
