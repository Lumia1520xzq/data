package com.wf.data.common.constants;

public interface BuryingPointContents {


    /**
     * LOADING界面
     */
    int POINT_TYPE_LOADING = 1;
    /**
     * 登录奖励界面--已埋
     */
    int POINT_TYPE_LOGIN_AWARDS = 2;
    /**
     * 昨日排行榜展示界面流失用户数--已埋
     */
    int POINT_TYPE_YESTERDAY_TOP50 = 3;
    /**
     * 充值界面打开--已埋
     */
    int POINT_TYPE_OPEN_RECHARGE_PAGE = 4;
    /**
     * 充值页面兑换按钮点击--已埋
     */
    int POINT_TYPE_RECHARGE_BUTTON = 5;
    /**
     * 充值跳转后实际充值用户数（余额充值/支付宝等充值分别统计）（充值埋点）--已埋
     */
    int POINT_TYPE_RECHARGE_SUCCESS = 6;
    /**
     * 用户投注----已埋（分游戏）
     */
    int POINT_TYPE_USER_BEATING = 7;
    /**
     * 进入游戏主界面--已埋
     */
    int POINT_TYPE_GAME_MAIN_PAGE = 8;
    /**
     * 进入福利界面--已埋
     */
    int POINT_TYPE_WEAL_PAGE = 9;

    /**
     * 用户单次打开投注局数统计和用户单日总投注局数统计
     */
    
    /*int POINT_TYPE_USER_BEATING = 7;*/

    /**
     * 投注额度选择统计（100,1000,10000投注次数）（是否实际投注出去的次数）
     */
    /*int POINT_TYPE_USER_BEATING = 7;*/

    /**
     * 临时大厅埋点
     */
    int POINT_TYPE_TEMP_HALL = 90;
    /**
     * 临时大厅-游戏入口埋点
     */
    int POINT_TYPE_TEMP_HALL_INCOME_GAME = 91;


    /**
     * 弹窗右上角关闭按钮
     */
    int POINT_TYPE_POP_WINDOW_CLOSE = 11;

    /**
     * QQ一键登录按钮
     */
    int POINT_TYPE_QQ_LOGIN_BTN = 12;

    /**
     * 手机号登录按钮
     */
    int POINT_TYPE_TEL_LOGIN_BTN = 13;

    /**
     * 首页页面 任务按钮
     */
    int POINT_TYPE_TASK_BTN = 15;

    /**
     * 首页页面 排行榜按钮
     */
    int POINT_TYPE_RANKING_BTN = 16;

    /**
     * 首页页面 banner
     */
    int POINT_TYPE_BANNER = 17;

    /**
     * 首页页面 梦想桌球
     */
    int POINT_TYPE_BILLIARDS_BTN = 18;

    /**
     * 首页页面 梦想飞镖
     */
    int POINT_TYPE_DART_BTN = 19;

    /**
     * 首页页面 热血军团
     */
    int POINT_TYPE_WARS_BTN = 20;

    /**
     * 首页页面 貂蝉保卫战
     */
    int POINT_TYPE_ARROWS_BTN = 21;

    /**
     * 首页页面 菜单栏抽奖按钮
     */
    int POINT_TYPE_LUCKY_BTN = 22;

    /**
     * 个人登录页面 免费注册按钮
     */
    int POINT_TYPE_REGIS_BTN = 23;

    /**
     * 个人登录页面 左上角返回按钮
     */
    int POINT_TYPE_RETURN_BACK_BTN = 24;

    /**
     * 个人登录页面 页面下面的QQ登录按钮
     */
    int POINT_TYPE_LOGIN_BY_QQ_BTN = 25;

    /**
     * 个人中心页面 切换账号
     */
    int POINT_TYPE_CHANGE_ACCOUNT_BTN = 26;

    /**
     * 摩托进入主界面
     */
    int POINT_TYPE_BIKE_ENTER = 27;

    /**
     * 摩托 打开充值界面
     */
    int POINT_TYPE_BIKE_OPEN_RECHARGE = 28;

    /**
     * 摩托 点击充值界面
     */
    int POINT_TYPE_BIKE_OPEN_RECHARGE_CLICK = 29;


    /**
     * 每日弹窗 展示埋点
     */
    int POINT_LOGIN_DAYTIPS = 32;

    /**
     * 每日弹窗 QQ登录成功
     */
    int POINT_USER_LOGIN_QQ = 31;

    /**
     * 每日弹窗 手机登录成功
     */
    int POINT_USER_LOGIN_PHONE = 33;

    /**
     * 每日弹窗 试玩按钮点击
     */
    int POINT_DAYTIPS_USER_TRYGAME = 34;

    /**
     * 每日弹窗 进入试玩
     */
    int POINT_USER_TRYGAME_LOADING = 35;
    
    /**
     * 打开视频界面
     */
    int POINT_USER_OPEN_VIDEO = 36;
    
    /**
     * 打开宝箱
     */
    int POINT_USER_OPEN_BOX = 37;
    
    /**
     * 打开联赛界面
     */
    int POINT_LEAGUE_MATCHES_OPEN = 38;
    
    /**
     * 联赛关闭按钮
     */
    int POINT_LEAGUE_MATCHES_CLOSE = 39;
    
    
    
    /**
     * 点击任务按钮
     */
    int POINT_HOME_TASK_BIN = 100;

    /**
     * 充值回馈按钮
     */
    int POINT_HOME_FEEDBACK_BIN = 101;

    /**
     * 排行榜按钮
     */
    int POINT_HOME_RANKING_BIN = 102;

    /**
     * 头像按钮
     */
    int POINT_HOME_HEAD_BIN = 103;

    /**
     * 平台轮播图
     */
    int POINT_HOME_BANNER_BIN = 104;
    /**
     * 跑马灯
     */
    int POINT_HOME_MARQUEE_BIN = 105;
    /**
     * 各游戏ICON
     */
    int POINT_HOME_GAME_ICON_BIN = 106;
    /**
     * 底栏-游戏ICON
     */
    int POINT_HOME_BOTTOM_GAME_BIN = 107;
    /**
     * 底栏-背包ICON
     */
    int POINT_HOME_BOTTOM_BACKPACK_BIN = 108;
    /**
     * 底栏-抽奖
     */
    int POINT_HOME_BOTTOM_DRAW_BIN = 109;
    /**
     * 充值+号
     */
    int POINT_HOME_RECHANGE_BIN = 110;
    /**
     * 任务浮层-成长任务
     */
    int POINT_TASK_GROW_BIN = 111;
    /**
     * 任务浮层-每日任务
     */
    int POINT_TASK_DAY_BIN = 112;
    /**
     * 排行榜浮层-我的盈利ICON
     */
    int POINT_RANKING_MYPROFIT_BIN = 113;
    /**
     * 排行榜浮层-盈利榜ICON
     */
    int POINT_RANKING_PROFITLIST_BIN = 114;
    /**
     * 排行榜浮层-财富榜ICON
     */
    int POINT_RANKING_RICHLIST_BIN = 115;
    /**
     * 排行榜浮层-我的盈利，查看详细规则
     */
    int POINT_RANKING_RULE_BIN = 116;
    /**
     * 排行榜浮层-盈利榜中昨日榜单
     */
    int POINT_RANKING_YESTERDAY_BIN = 117;
    /**
     * 点击欢乐套圈
     */
    int POINT_DRAW_QUOITS_BIN = 118;
    /**
     * 付费转盘-展示转盘
     */
    int POINT_DRAW_PAY_WHEEL_SHOW_BIN = 119;
    /**
     * 付费转盘-点击抽奖
     */
    int POINT_DRAW_PAY_WHEEL_BETTING_BIN = 120;


    /**
     * 游戏历史开奖记录
     */
    int POINT_GAME_HISTORY_LIST = 121;
    /**
     * 游戏更多菜单按钮
     */
    int POINT_GAME_MORE_BTN = 122;
    /**
     * 红包按钮
     */
    int POINT_LUCKEY_PACKET = 123;
    /**
     * 游戏玩法说明
     */
    int POINT_GAME_PLAY_DESC = 124;
    /**
     * 游戏投注记录
     */
    int POINT_GAME_BETTING_RECORD = 125;
    /**
     * 游戏支持排行
     */
    int POINT_GAME_BETTING_RANK = 126;
    /**
     * 游戏客服专区
     */
    int POINT_PLAT_CUSTOM_SERVICE = 127;
    /**
     * 游戏返回大厅
     */
    int POINT_PLAT_BACK_PLATFORM = 128;


    /**
     * 奖池点击浮层按钮
     */
    int POINT_AWARD_POOL_LAYER = 129;
    /**
     * 进入套圈按钮
     */
    int POINT_GOTO_QUOITS = 130;
    /**
     * 获奖名单按钮
     */
    int POINT_GET_AWARD_USER_LIST = 131;
    /**
     * 摩托游戏入口按钮
     */
    int POINT_GOTO_MOTO_BTN = 132;
    /**
     * 音量开关
     */
    int POINT_VOLUME_SWITCH_BTN = 133;
    /**
     * 领奖台
     */
    int POINT_FETCH_AWARD_STEP_BTN = 134;
    /**
     * 赛况
     */
    int POINT_MOTO_OUTS_BTN = 135;
    /**
     * 赛况
     */
    int POINT_MOTO_OUTS_TAB_BTN = 136;
    /**
     * 车况
     */
    int POINT_MOTO_CAR_TAB_BTN = 137;
    /**
     * 历史
     */
    int POINT_MOTO_HISTORY_TAB_BTN = 138;

    /**
     * 哈雷
     */
    int POINT_MOTO_HL_INFO_BTN = 139;
    /**
     * 宝马
     */
    int POINT_MOTO_BMW_INFO_BTN = 140;
    /**
     * 杜卡迪
     */
    int POINT_MOTO_DKD_INFO_BTN = 141;
    /**
     * 雅马哈
     */
    int POINT_MOTO_YMH_INFO_BTN = 142;
    /**
     * 本田
     */
    int POINT_MOTO_HONDA_INFO_BTN = 143;

    /**
     * 哈雷详细记录
     */
    int POINT_MOTO_HL_DETAIL_BTN = 144;
    /**
     * 宝马详细记录
     */
    int POINT_MOTO_BMW_DETAIL_BTN = 145;
    /**
     * 杜卡迪详细记录
     */
    int POINT_MOTO_DKD_DETAIL_BTN = 146;
    /**
     * 雅马哈详细记录
     */
    int POINT_MOTO_YMH_DETAIL_BTN = 147;
    /**
     * 本田详细记录
     */
    int POINT_MOTO_HONDA_DETAIL_BTN = 148;
    /**
     * 摩托介绍
     */
    int POINT_MOTO_INTRODUCE_BTN = 149;


    /**
     * 连胜加奖
     */
    int POINT_ARMY_PLUS_AWARDS_BTN = 150;


    /**
     * 继续游戏
     */
    int POINT_SANGUO_CONTINUE_BTN = 151;


    /**
     * 启动用户数（启动app开始计算）
     */
    int POINT_APP_START_BTN = 152;
    
    /**
     * 登录成功计算
     */
    int POINT_APP_DAILYLOGIN_BTN = 153;
    /**
     * 游客登录成功
     */
    int POINT_APP_VISITORNEW_BTN = 154;
    /**
     * 签到:新人签到
     */
    int POINT_APP_NEWSIGN_BTN = 155;
    /**
     * 签到:每日签到
     */
    int POINT_APP_DAYSIGN_BTN = 156;
    /**
     * 微信登录点击
     */
    int POINT_APP_WXLOGIN_BTN = 157;
    /**
     * 手机注册点击
     */
    int POINT_APP_MOBILEREG_BTN = 158;
    /**
     * 我的充值按钮点击
     */
    int POINT_APP_MINE_BTN = 159;
    /**
     * 支付宝支付点击
     */
    int POINT_APP_ALIPAYPAY_BTN = 160;
    /**
     * 微信支付点击
     */
    int POINT_APP_WECHATPAY_BTN = 161;
    /**
     * 支付成功
     */
    int POINT_APP_PAYSUCCESS_BTN = 162;
   
    /**
     * 支付失败
     */
    int POINT_APP_PAYFAIL_BTN = 163;
    /**
     * 首页-消息中心入口点击
     */
    int POINT_APP_MESSAGE_BTN = 164;
    /**
     * “如何获取”按钮点击
     */
    int POINT_APP_RANKING_BTN = 165;
    /**
     * 橡皮擦按钮点击
     */
    int POINT_APP_ERASER_BTN = 166;
    /**
     * “当前奖励”点击
     */
    int POINT_APP_AWARD_BTN = 167;
    /**
     * 首页分享圈入口点击
     */
    int POINT_APP_SHARE_BTN = 168;
    /**
     * 分享圈-“贴士”点击 
     */
    int POINT_APP_TIPS_BTN = 169;
    /**
     * 分享圈-“攻略”点击
     */
    int POINT_APP_STRATEGY_BTN = 170;
    /**
     * 分享圈-“分享”编辑按钮点击
     */
    int POINT_APP_EDITSHARE_BTN = 171;
    /**
     * 分享圈-分享-确认发布按钮点击
     */
    int POINT_APP_OKSHARE_BTN = 172;
    /**
     * 我的消息按钮点击
     */
    int POINT_APP_NEWS_BTN = 173;
    /**
     * 首页运营位置点击
     */
    int POINT_APP_MANAGER_BTN_1 = 174;
    int POINT_APP_MANAGER_BTN_2 = 175;
    int POINT_APP_MANAGER_BTN_3 = 176;
    
    
    /**
     * 我的昵称修改点击
     */  
    int POINT_APP_NICENAME_BTN = 177;
  
    
    /**
     * 我的资金明细按钮点击
     */  
    int POINT_APP_DETAILS_BTN = 178;
  
    /**
     * 我的收货信息明细按钮点击
     */  
    int POINT_APP_RECEIPTINFO_BTN = 179;
  
    /**
     * 邀请好友入口点击
     */  
    int POINT_APP_INVITE_BTN = 180;
  
    /**
     * 我的设置按钮点击
     */  
    int POINT_APP_SETUP_BTN = 181;
  
    /**
     * 资金明细-充值记录标签页点击
     */  
    int POINT_APP_RECHARGEINFO_BTN = 182;
  
    /**
     * 资金明细-金叶子记录标签页点击
     */  
    int POINT_APP_LEAFINFO_BTN = 183;
  
    /**
     * 设置-手机绑定点击
     */  
    int POINT_APP_BINDING_BTN = 184;
  
    /**
     * 设置-通知中心点击
     */  
    int POINT_APP_NOTICE_BTN = 185;
  
    /**
     * 设置-清除缓存点击
     */  
    int POINT_APP_CACHE_BTN = 186;
  
   
    /**
     * 设置-帮助中心点击
     */  
    int POINT_APP_HELP_BTN = 187;
    /**
     * 设置-关于我们点击
     */  
    int POINT_APP_US_BTN = 188;
    /**
     * 设置-退出登录点击
     */  
    int POINT_APP_OUT_BTN = 189;
    /**
     * 领奖台-排行入口点击
     */  
    int POINT_APP_REWARD_RANK_BTN = 190;
    
    /**
     * 领奖台-金额制合成弹窗
     */  
    int POINT_APP_REWARD_MONEY_BTN = 191;
    /**
     * 领奖台-金额合成奖励浮层
     */  
    int POINT_APP_REWARD_AWARD_BTN = 192;
    /**
     * 领奖台-实物碎片（手机）合成弹窗
     */  
    int POINT_APP_REWARD_PHONE_BTN = 193;
    /**
     * 领奖台-实物碎片（手机）合成奖励弹窗
     */  
    int POINT_APP_PHONE_AWARD_BTN = 194;
    /**
     * 领奖台-碎片记录点击
     */  
    int POINT_APP_REWARD_FRAGMENT_BTN = 195;
    /**
     * 领奖台-实物记录点击
     */  
    int POINT_APP_REWARD_ENTITY_BTN = 196;
    /**
     * 领奖台-实物领取弹窗
     */  
    int POINT_APP_REWARD_GET_BTN = 197;
    /**
     * 领奖台-查询弹窗
     */  
    int POINT_APP_REWARD_QUERY_BTN = 198;
}