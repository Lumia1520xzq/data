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
	 * 乱斗三国投注
	 */
	int BUSINESS_TYPE_BETTING_ARROWS = 2007;

	
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
	 * 乱斗三国赢钱
	 */
	int BUSINESS_TYPE_WIN_ARROWS = 4007;
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
	 * 足球返奖
	 */
	int BUSINESS_TYPE_FOOTBALL_RETURN_AWARD = 7002;
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
     * 打鱼
     */
    int BUSINESS_TYPE_FISH_FIRE_FISH = 10002;
    /**
     * 命中鱼
     */
    int BUSINESS_TYPE_FISH_HIT_FISH = 10003;
	/**
	 * 捕鱼预扣除
	 */
	int BUSINESS_TYPE_FISH_PRE_DEDUCT = 10005;
	/**
	 * 乐赢三张 服务费
	 */
	int BUSSINESS_TYPE_TCARD_SERVICE_MONEY = 11001;
	/**
	 * 乐赢三张 底注
	 */
	int BUSSINESS_TYPE_TCARD_BASE_RATE = 11002;
	/**
	 * 乐赢三张 跟牌
	 */
	int BUSSINESS_TYPE_TCARD_CALL = 11003;
	/**
	 * 乐赢三张 比牌
	 */
	int BUSSINESS_TYPE_TCARD_FIGHT = 11004;
	/**
	 * 乐赢三张 赢牌
	 */
	int BUSSINESS_TYPE_TCARD_WIN = 11005;
	/**
	 * 消消乐投注
	 */
	int BUSINESS_TYPE_CANDY_BETTING = 12001;
	/**
	 * 消消乐返奖
	 */
	int BUSINESS_TYPE_CANDY_WIN = 12002;
}