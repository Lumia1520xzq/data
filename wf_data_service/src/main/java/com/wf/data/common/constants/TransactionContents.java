package com.wf.data.common.constants;

/**
 * 
 * @author Fe 2016年11月28日
 */
public interface TransactionContents {
	
	/**
	 * 投注取消
	 */
	int BUSINESS_TYPE_BETTING_CENCEL = 3;

	/**
	 * 飞镖投注
	 */
	int BUSINESS_TYPE_BETTING_DART = 2001;
	
	/**
	 * 桌球投注
	 */
	int BUSINESS_TYPE_BETTING_BILLIARD = 2002;
	
	/**
	 * 套圈投注
	 */
	int BUSINESS_TYPE_BETTING_QUOITS = 2003;
	/**
	 * 热血军团投注
	 */
	int BUSINESS_TYPE_BETTING_WARS = 2004;

	/**
	 * 热血军团玩家交易-运输物资
	 */
	int BUSINESS_TYPE_WARS_WZZD_BETTING = 2005;
	/**
	 * 幸运转盘投注
	 */
	int BUSINESS_TYPE_WHEEL_BETTING = 2006;
	/**
	 * 乱斗三国投注
	 */
	int BUSINESS_TYPE_BETTING_ARROWS = 2007;
	/**
	 * 乱斗三国 冻结敌人
	 */
	int BUSINESS_TYPE_FORZEN_ARROWS = 2008;
	/**
	 * 桌球追号 返奖
	 */
	int BUSINESS_TYPE_CHASE_NUMBER_BILLIARD = 2009;
	
	/**
	 * 飞镖赢钱
	 */
	int BUSINESS_TYPE_WIN_DART = 4001;
	
	/**
	 * 桌球赢钱
	 */
	int BUSINESS_TYPE_WIN_BILLIARD = 4002;
	/**
	 * 套圈赢钱
	 */
	int BUSINESS_TYPE_WIN_QUOITS = 4003;
	/**
	 * 热血军团赢钱
	 */
	int BUSINESS_TYPE_WIN_WARS = 4004;
	/**
	 * 热血军团玩家交易-运输物资盈利
	 */
	int BUSINESS_TYPE_WARS_WZZD_WIN = 4005;
	/**
	 * 幸运转盘赢钱
	 */
	int BUSINESS_TYPE_WIN_WHEEL = 4006;
	/**
	 * 乱斗三国赢钱
	 */
	int BUSINESS_TYPE_WIN_ARROWS = 4007;
	/**
	 * 任务奖励
	 */
	int BUSINESS_TYPE_TASK_SEND = 4008;
	/**
	 * 飞镖加奖
	 */
	int BUSINESS_TYPE_DART_ADD_REWARD = 4009;
	
	/**
	 * 台球副玩法投注消耗
	 */
	int BUSINESS_TYPE_BILLIARDS_ASSISTANT_BETTING=4010;
	
	/**
	 * 台球副玩法取消投注
	 */
	int BUSINESS_TYPE_BILLIARDS_ASSISTANT_CANCEL_BETTING=4011;
	
	/**
	 * 台球副玩法返奖
	 */
	int BUSINESS_TYPE_BILLIARDS_ASSISTANT_AWARD=4012;
	/**
	 * 热血军团连输返奖
	 */
	int BUSINESS_TYPE_WARS_LOSE_ADD_AWARD=4013;
	/**
	 * 乱斗三国 爆击
	 */
	int BUSINESS_TYPE_ARROWS_CRIT_AWARD = 4014;
	/**
	 * 排行榜奖励发放金叶子
	 */
	int BUSINESS_TYPE_PROFIT_WIN = 4018;
	/*
	 * 飞镖 支持榜 返奖
	 */
	int BUSINESS_TYPE_DART_BETTING_RANK = 4015;

	/**
	 * 摩托投注
	 */
	int BUSINESS_TYPE_BIKE_BETTING = 5001;
	/**
	 * 摩托赢钱
	 */
	int BUSINESS_TYPE_BIKE_WIN = 5002;
	/**
	 * 摩托撤销投注
	 */
	int BUSINESS_TYPE_BIKE_CANCEL = 5003;

	/**
	 * 多多三国投注
	 */
	int BUSINESS_TYPE_KINGDOM_BETTING = 6001;
	/**
	 * 多多三国赢钱
	 */
	int BUSINESS_TYPE_KINGDOM_WIN = 6002;
	/**
	 * 多多三国撤销投注
	 */
	int BUSINESS_TYPE_KINGDOM_CANCEL = 6003;

	/**
	 * 金山兑换
	 */
	int BUSINESS_TYPE_JS_CONVERT = 101;

	/**
	 * 后台充值
	 */
	int BUSINESS_TYPE_RECHARGE = 102;

	/**
	 * 橡皮擦消耗
	 */
	int BUSINESS_TYPE_ERASER = 103;

	/**
	 * 礼包获取
	 */
	int BUSINESS_TYPE_GAME_GIFT = 104;


	/**
	 * 福利购买
	 */
	int BUSINESS_TYPE_WELFARE_BUY = 105;

	/**
	 * 周卡购买
	 */
	int BUSINESS_TYPE_WEEKCARD_BUY = 106;


	/**
	 * 周卡日返
	 */
	int BUSINESS_TYPE_WEEKCARD_RETURN = 107;

	/**
	 * 首充特惠活动
	 */
	int BUSINESS_TYPE_FIRST_CHARGE_PRIVILEGE = 108;
	
	/**
	 * 用户召回，发放金叶子
	 */
	int BUSINESS_TYPE_USER_ADD_LEAFS = 109;

	/**
	 * 分享圈
	 */
	int COTERIE_PRAISE_AWARDS_LEAFS = 110;

	/**
	 * 投注红包
	 */
	int BUSINESS_TYPE_BETTING_RED_PACKET = 111;
	

	/**
	 * 分享圈奖励
	 */
	int BUSINESS_TYPE_COTERIE_AWARDS = 112;
	
	/**
	 * 足球返奖
	 */
	int BUSINESS_TYPE_FOOTBALL_RETURN_AWARD = 7002;
	/**
	 * 足球返回投注金叶子
	 */
	int BUSINESS_TYPE_FOOTBALL_EXCEPTION_RETURN_AWARD = 7003;
	/**
	 * 足球投注
	 */
	int BUSINESS_TYPE_FOOTBALL_BETTING = 7001;
	/**
	 * 摩托返奖
	 */
	int BUSINESS_TYPE_MOTOR_RETURN_AWARD = 5002;
	/**
	 * 摩托撤销
	 */
	int BUSINESS_TYPE_MOTOR_BRTTING_CANCEL = 5003;
	/**
	 * 摩托投注
	 */
	int BUSINESS_TYPE_MOTOR_BETTING = 5001;
	/**
     * 运营活动-看视频返叶子
     */
    int BUSINESS_TYPE_WATCH_VIDEO = 201;
    
    /**
     * 捕鱼炮台升级
     */
    int BUSINESS_TYPE_FISH_BATTERY_UPGRADE=10001;
    /**
     * 打鱼
     */
    int BUSINESS_TYPE_FISH_FIRE_FISH=10002;
    /**
     * 命中鱼
     */
    int BUSINESS_TYPE_FISH_HIT_FISH=10003;
	
}