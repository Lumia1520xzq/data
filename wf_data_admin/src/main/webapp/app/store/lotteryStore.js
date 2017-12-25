Ext.define("JDD.store.lotteryStore", {
    extend: "DCIS.Store",
    url: '/basedata/private/basedataLotteryController/listBasedataLotteryByLotteryName.do',
    fields: [{name: 'lotteryId', type: 'string', display: '彩种id', show: true},
        {name: 'lotteryName', type: 'string', display: '彩种名称'}, {name: 'shortName', type: 'string', display: '简称'}, {name: 'lotteryCode', type: 'string', display: 'code'}],
    autoLoad: true
});