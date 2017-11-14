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


    String DART_SIGNED_DAYS = "dart_signed_days";
    String DART_JS_CONVERT_MONEY = "dart_js_convert_money";
    String DART_REWARD_TO_PLATFORM = "dart_reward_to_platform";
    String DART_BETTING_MONEYS = "dart_betting_moneys";
    String DART_RICH_BETTING_MONEYS = "dart_rich_betting_moneys";
    String DART_ROBOT_IMGS = "dart_robot_imgs";
    String DART_ROBOT_NAMES = "dart_robot_names";
    //dart邮件告警接收人  切换为game_alarm_receiver
    String DART_ALARM_EMAIL_RECEIVER = "dart_alarm_receiver";

    //由DART_ALARM_EMAIL_RECEIVER过度
    String GAME_ALARM_EMAIL_RECEIVER = "game_alarm_receiver";
    String GAME_BETTING_DATA_RECEIVER = "game_betting_data_receiver";
    String GAME_RECHARGE_AUTH_CODE_RECEIVER = "game_recharge_auth_code_receiver";
    String GAME_BETTING_MAX_MONEY = "game_betting_max_money";
    String GAME_RICH_BETTING_MAX_MONEY = "game_rich_betting_max_money";
    String GAME_RECHARGE_MAX_MONEY = "game_recharge_max_money";
    String GAME_ERASER_COST = "game_eraser_cost";
    String GAME_SIGNED_RANDOM = "game_signed_random";
    String GAME_BEFORE_PAYMENT="game_before_payment";
    String CLUB_BETTING_DATA_RECEIVER = "club_betting_data_receiver";

    //平台数据报表
    String PLATFORM_DATA_RECEIVER="platform_data_receiver";
    //游戏数据报表
    String GAME_DATA_RECEIVER="game_data_receiver";
    //用户信息列表
    String USERINFO_DATA_RECEIVER="userinfo_data_receiver";
    //套圈数据报表
    String QUOITS_DATA_RECEIVER="quoits_data_receiver";
    //风控
    String MONITOR_RISK_RECEIVER = "monitor_risk_receiver";
    //风控 每小时新增用户数
    String MONITOR_RISK_NEWUSER_RECEIVER = "monitor_risk_newuser_receiver";
    /**
     * 用户注册前行为分析收件人
     */
    String MONITOR_BEFORE_PAYMENT_RECEIVER = "monitor_before_payment_receiver";
    //注册时间风控
    String MONITOR_RISK_CREATETIME_RECEIVER = "monitor_risk_createtime_receiver";
    //数据日检
    String DAILY_DATA_RECEIVER="daily_data_receiver";
    //每日关键数据
    String DAILY_KEY_DATA="daily_key_data";
    //每日各渠道关键数据
    String CHANNEL_DATA_RECEIVER="channel_data_receiver";


    String GAME_AWARDS_LOW_RECEIVER = "game_awards_low_receiver";
    //首充特惠活动开关
    String JS_COVERT_FIRST_CHARGE_PRIVILEGE = "js_covert_first_charge_privilege";
    String FIRST_CHARGE_ITEMS = "first_charge_items";
    String FIRST_CHARGE_CHANNELS = "first_charge_channels";
    //充值回馈渠道开关
    String CHARGE_CONVERT_RETURN_PROFIT_CHANNELS="charge_convert_return_profit_channels";

    //平台活动换肤时间
    String PLAT_CHANGE_BACKGROUND_TIME = "plat_change_background_time";

    //发放彩金卡数量控制
    String RANKING_PROFIT_HANDSEL_COUNT = "ranking_profit_handsel_count";
    //平台签到
    String PLAT_SIGNED_DAYS = "plat_signed_days";
    //平台签到随机
    String PLAT_SIGNED_RANDOM = "plat_signed_random";
    // 平台连续签到
    String PLAT_SIGNED_BOX = "signed_box";

    //平台签到7天之后赠送金叶子用户金叶子分界点
    String PLAT_SIGNED_USER_POINT = "plat_signed_user_point";
    //平台签到7天之后赠送金叶子
    String PLAT_SIGNED_LEAF = "plat_signed_leaf";

    // 复活基金
    String PLAT_FUHUOJIJIN = "plat_fuhuojijin";

    //系统公告置顶时间：小时
    String SYS_NOTICE_TOPTIME_HOURS = "sys_notice_toptime_hours";
    String SELF_SEND_NOTICE_TOPTIME_HOURS = "self_send_notice_toptime_hours";

    int UIC_WAP_USER_TYPE = 1;
    int UIC_JS_LOTTERY_TYPE = 2;
    int UIC_QQ_USER_TYPE = 3;
    int UIC_WECHAT_USER_TYPE = 4;

    /**
     * 盈利榜
     */
    int AWARDS_SOURCE_TYPE_YJB = 1;
    /**
     * 套圈
     */
    int AWARDS_SOURCE_TYPE_QUOITS = 2;
    /**
     * 活动
     */
    int AWARDS_SOURCE_TYPE_ACTIVITY = 3;
    /**
     * 碎片兑换
     */
    int AWARDS_SOURCE_TYPE_FRAGMENT = 4;

    String GAME_STOP_FOR_UPDATE = "更新维护中,请稍后重试";
    //跑马灯消息发布花费叶子
    int USERSEND_NOTICE_COST = 1000;
    /**
     * 飞镖加奖活动开启标识 true|false
     */
    String ACTIVITY_DART_ADD_REWARDS ="activity_dart_add_rewards";
    /**
     * 飞镖加奖活动标题
     */
    String ACTIVITY_DART_ADD_REWARDS_TITLE ="activity_dart_add_rewards_title";
    /**
     * 飞镖加奖活动宣传图
     */
    String ACTIVITY_DART_ADD_REWARDS_PIC = "activity_activity_dart_add_rewards_pic";

    /**
     * WAP支付方式
     */
    String PAY_TYPE = "pay_type";

    /**
     * 订单来源
     */
    String ORDER_SOURCE = "order_source";
    /**
     * 发送短信
     */
    String WAP_SMS_SEND = "wap_sms_send";

    String DICT_ARROWS_BANNER = "dict_arrows_banner";
    String DICT_DART_BANNER = "dict_dart_banner";
    /**
     * vip等级
     */
    String PLAT_VIP_LEVEL = "plat_vip_level";

    /**
     * APP支付方式
     */
    String APP_PAY_TYPE = "app_pay_type";

    /**
     * 安卓
     */
    int CLIENT_TYPE_ANDROID = 1;

    /**
     * IOS
     */
    int CLIENT_TYPE_IOS = 2;

    /*
     * 三国爆击开启标识
     */
    String ACTIVITY_ARROWS_IS_CRIT = "activity_arrows_is_crit";

    /**
     * 飞镖月支持榜 结算日
     */
    String DART_BETTING_RANK_MONTH_DAY_FLAG = "dart_betting_rank_month_day_flag";

    /**
     * 飞镖 日榜 开关
     */
    String DART_BETTING_RANK_DAY = "dart_betting_rank_day";
    /**
     * 飞镖月榜 开关
     */
    String DART_BETTING_RANK_MONTH = "dart_betting_rank_month";
    /**
     * 飞镖广播下限值
     */
    String DART_BROADCAST_VALUE = "dart_broadcast_value";
    /**
     * 飞镖月榜 开关
     */
    String QUOITS_LEVEL = "quoits_level";

    /**** 摩托 ***/
    String BIKE_BETTING_MONEYS = "bike_betting_moneys";
    /**
     * 摩托投注上线
     */
    String BIKE_BETTING_MONEYS_MAX = "bike_betting_moneys_max";
    /**
     * 摩托基础返奖率
     */
    String BIKE_BASE_RETURN_RATE = "bike_base_return_rate";
    /**
     * 摩托风控基础数值
     */
    String BIKE_MONITOR_BASE_BETTING_AMOUNT = "bike_monitor_base_betting_amount";
    /**
     * 摩托风控返奖率
     */
    String BIKE_MONITOR_BASE_BETTING_AMOUNT_RATE = "bike_monitor_base_betting_amount_rate";

    /****** 三国 ********/
    String KINGDOM_BETTING_MONEYS = "kingdom_betting_moneys";

    String KINGDOM_BETTING_MONEYS_MAX = "kingdom_betting_moneys_max";

    /**
     * 同ip个数设置
     */
    String MONITOR_IP_COUNT = "monitor_ip_count";
    /**
     *同ip下登录用户数
     */
    String MONITOR_RISK_IP_DAY = "monitor_risk_ip_day";

    String MONITOR_GRAY_DAY = "monitor_gray_day";

    /**
     * 用户累计充值金额
     */
    String MONITOR_RISK_USER_RECHARGE = "monitor_risk_user_recharge";

    String MONITOR_GRAY_USER_RECHARGE = "monitor_gray_user_recharge";

    String MONITOR_RISK_WF_IPS = "monitor_risk_wf_ips";
    /**
     * ip邮件接收人
     */
    String MONITOR_IP_RECEIVER = "monitor_ip_receiver";

}
