Ext.define('WF.view.data.board.EntranceViewMain', {
    extend: 'Ext.panel.Panel',
    title: '数据看板-奖多多各入口用户分析',
    xtype: 'EntranceViewMain',
    closable: true,
    autoScroll:true,
    layout: {
        type: 'vbox',
        align: 'stretch'
    },

    initComponent: function () {
        var me = this;
        me.callParent(arguments);
        var store= Ext.create('DCIS.Store', {
            url:'/data/entrance/analysis/getList.do',
            autoload:false,
            fields:['entranceDauRateList','eventNameList','searchDate','dateList',
                'duoDuoDauList','duoduoDauRate',
                'iconDauList','iconDauRate',
                'bannerDauList','bannerDauRate',
                'advDauList','advDauRate',
                'gameDauList','gameDauRate',
                'rollDauList','rollDauRate',
                'pointDauList','pointDauRate',
                'actCenterDauList','actCenterDauRate',
                'pushDauList','pushDauRate',
                'strongAdvDauList','strongAdvDauRate',
                'entranceSignRateList','entranceBettingRateList','entrancePayRateList','entranceDayRetentionList',
                'duoduoSignRate','iconSignRate','bannerSignRate','advSignRate','gameSignRate',
                'rollSignRate','pointSignRate','actCenterSignRate','pushSignRate','strongAdvSignRate',
                'duoduoBettingRate','iconBettingRate','bannerBettingRate','advBettingRate','gameBettingRate',
                'rollBettingRate','pointBettingRate','actCenterBettingRate','pushBettingRate','strongAdvBettingRate',
                'duoduoPayRate','iconPayRate','bannerPayRate','advPayRate','gamePayRate',
                'rollPayRate','pointPayRate','actCenterPayRate','pushPayRate','strongAdvPayRate',
                'duoduoRetentionRate','iconRetentionRate','bannerRetentionRate','advRetentionRate','gameRetentionRate',
                'rollRetentionRate','pointRetentionRate','actCenterRetentionRate','pushRetentionRate','strongAdvRetentionRate'
            ]
        });

        me.add({
            border: false,
            store: store,
            xtype: 'searchpanel',
            title: '查询',
            collapsible: true,
            collapsed: false,
            columns: 3,
            buildField: "Manual",
            forceFit: true,
            export: function () {
                window.location.href = '/data/entrance/analysis/export.do?businessDate=' + me.down(("[name='searchDate']")).value;
            },
            items: [
                {
                    name: 'searchDate',
                    fieldLabel: '日期',
                    xtype: 'datefield',
                    format: 'Y-m-d'
                    // value:Ext.util.Format.date(Ext.Date.add(new Date(),Ext.Date.DAY,-30),"Y-m-d")
                }
            ]
        });

        me.add({
            xtype:'panel',
            layout:'vbox',
            align : 'stretch',
            bodyStyle:'border-width:0 0 0 0;',
            items: [
                {
                title: '各入口数据',align:'stretch',height:380,width:"100%",xtype:"panel",layout:'vbox',bodyStyle:'border-width:0',forceFit:true,
                items:[
                    {width:"100%",height:380,xtype:"panel",layout:'hbox',forceFit:true,bodyStyle:'border-width:0',items:[
                        {id:'entPie',width:"45%",height:380,xtype:"panel",layout:'vbox',forceFit:true},
                        {id:'entMix',width:"45%",height:380,xtype:"panel",layout:'vbox',forceFit:true},
                        {width:"10%",height:380,xtype:"panel",forceFit:true,bodyStyle:'border-width:0'}
                        ]
                      }
                   ]
                },
                {
                    title:'转化率',align:'stretch',height:400,width:"100%",xtype:"panel",layout:'vbox',bodyStyle:'border-width:0',forceFit:true,
                    items:[
                        {width:"100%",height:400,xtype:"panel",layout:'hbox',forceFit:true,bodyStyle:'border-width:0',items:[
                            {id:'entRate0',width:"33.33%",height:400,xtype:"panel",layout:'vbox',forceFit:true},
                            {id:'entRate1',width:"33.33%",height:400,xtype:"panel",layout:'vbox',forceFit:true},
                            {id:'entRate2',width:"33.33%",height:400,xtype:"panel",forceFit:true,bodyStyle:'border-width:0'}
                        ]
                        }
                    ]
                },
                {
                    title:'',align:'stretch',height:400,width:"100%",xtype:"panel",layout:'vbox',bodyStyle:'border-width:0',forceFit:true,
                    items:[
                        {width:"100%",height:400,xtype:"panel",layout:'hbox',forceFit:true,bodyStyle:'border-width:0',items:[
                            {id:'entPic0',width:"33.33%",height:400,xtype:"panel",layout:'vbox',forceFit:true},
                            {id:'entPic1',width:"33.33%",height:400,xtype:"panel",layout:'vbox',forceFit:true},
                            {id:'entPic2',width:"33.33%",height:400,xtype:"panel",forceFit:true,bodyStyle:'border-width:0'}
                        ]
                        }
                    ]
                },
                {
                    title:'留存率',align:'stretch',height:400,width:"100%",xtype:"panel",layout:'vbox',bodyStyle:'border-width:0',forceFit:true,
                    items:[
                        {width:"100%",height:400,xtype:"panel",layout:'hbox',forceFit:true,bodyStyle:'border-width:0',items:[
                            {id:'entRate3',width:"33.33%",height:400,xtype:"panel",layout:'vbox',forceFit:true},
                            {id:'entPic3',width:"33.33%",height:400,xtype:"panel",layout:'vbox',forceFit:true},
                        ]
                        }
                    ]
                }
            ]
        });

        store.addListener('datachanged',function(){
                fun1();
            }
        );

        store.load ({
            callback:function(){
                fun1();
            }
        });

        function fun1(){
            var entranceDauRateList = store.getAt(0).get("entranceDauRateList");
            var eventNameList = store.getAt(0).get("eventNameList");
            var searchDate = store.getAt(0).get("searchDate");
            var dateList = store.getAt(0).get("dateList");

            var duoDuoDauList = store.getAt(0).get("duoDuoDauList");
            var iconDauList = store.getAt(0).get("iconDauList");
            var bannerDauList = store.getAt(0).get("bannerDauList");
            var advDauList = store.getAt(0).get("advDauList");
            var gameDauList = store.getAt(0).get("gameDauList");
            var rollDauList = store.getAt(0).get("rollDauList");
            var pointDauList = store.getAt(0).get("pointDauList");
            var actCenterDauList = store.getAt(0).get("actCenterDauList");
            var pushDauList = store.getAt(0).get("pushDauList");
            var strongAdvDauList = store.getAt(0).get("strongAdvDauList");

            var duoduoDauRate = store.getAt(0).get("duoduoDauRate");
            var iconDauRate = store.getAt(0).get("iconDauRate");
            var bannerDauRate = store.getAt(0).get("bannerDauRate");
            var advDauRate = store.getAt(0).get("advDauRate");
            var gameDauRate = store.getAt(0).get("gameDauRate");
            var rollDauRate = store.getAt(0).get("rollDauRate");
            var pointDauRate = store.getAt(0).get("pointDauRate");
            var actCenterDauRate = store.getAt(0).get("actCenterDauRate");
            var pushDauRate = store.getAt(0).get("pushDauRate");
            var strongAdvDauRate = store.getAt(0).get("strongAdvDauRate");

            var entranceSignRateList = store.getAt(0).get("entranceSignRateList");
            var entranceBettingRateList = store.getAt(0).get("entranceBettingRateList");
            var entrancePayRateList = store.getAt(0).get("entrancePayRateList");
            var entranceDayRetentionList = store.getAt(0).get("entranceDayRetentionList");

            var duoduoSignRate = store.getAt(0).get("duoduoSignRate");
            var iconSignRate = store.getAt(0).get("iconSignRate");
            var bannerSignRate = store.getAt(0).get("bannerSignRate");
            var advSignRate = store.getAt(0).get("advSignRate");
            var gameSignRate = store.getAt(0).get("gameSignRate");
            var rollSignRate = store.getAt(0).get("rollSignRate");
            var pointSignRate = store.getAt(0).get("pointSignRate");
            var actCenterSignRate = store.getAt(0).get("actCenterSignRate");
            var pushSignRate = store.getAt(0).get("pushSignRate");
            var strongAdvSignRate = store.getAt(0).get("strongAdvSignRate");

            var duoduoBettingRate = store.getAt(0).get("duoduoBettingRate");
            var iconBettingRate = store.getAt(0).get("iconBettingRate");
            var bannerBettingRate = store.getAt(0).get("bannerBettingRate");
            var advBettingRate = store.getAt(0).get("advBettingRate");
            var gameBettingRate = store.getAt(0).get("gameBettingRate");
            var rollBettingRate = store.getAt(0).get("rollBettingRate");
            var pointBettingRate = store.getAt(0).get("pointBettingRate");
            var actCenterBettingRate = store.getAt(0).get("actCenterBettingRate");
            var pushBettingRate = store.getAt(0).get("pushBettingRate");
            var strongAdvBettingRate = store.getAt(0).get("strongAdvBettingRate");

            var duoduoPayRate = store.getAt(0).get("duoduoPayRate");
            var iconPayRate = store.getAt(0).get("iconPayRate");
            var bannerPayRate = store.getAt(0).get("bannerPayRate");
            var advPayRate = store.getAt(0).get("advPayRate");
            var gamePayRate = store.getAt(0).get("gamePayRate");
            var rollPayRate = store.getAt(0).get("rollPayRate");
            var pointPayRate = store.getAt(0).get("pointPayRate");
            var actCenterPayRate = store.getAt(0).get("actCenterPayRate");
            var pushPayRate = store.getAt(0).get("pushPayRate");
            var strongAdvPayRate = store.getAt(0).get("strongAdvPayRate");

            var duoduoRetentionRate = store.getAt(0).get("duoduoRetentionRate");
            var iconRetentionRate = store.getAt(0).get("iconRetentionRate");
            var bannerRetentionRate = store.getAt(0).get("bannerRetentionRate");
            var advRetentionRate = store.getAt(0).get("advRetentionRate");
            var gameRetentionRate = store.getAt(0).get("gameRetentionRate");
            var rollRetentionRate = store.getAt(0).get("rollRetentionRate");
            var pointRetentionRate = store.getAt(0).get("pointRetentionRate");
            var actCenterRetentionRate = store.getAt(0).get("actCenterRetentionRate");
            var pushRetentionRate = store.getAt(0).get("pushRetentionRate");
            var strongAdvRetentionRate = store.getAt(0).get("strongAdvRetentionRate");


            var dauList=[];
            dauList.push(duoDuoDauList);
            dauList.push(iconDauList);
            dauList.push(bannerDauList);
            dauList.push(advDauList);
            dauList.push(gameDauList);
            dauList.push(rollDauList);
            dauList.push(pointDauList);
            dauList.push(actCenterDauList);
            dauList.push(pushDauList);
            dauList.push(strongAdvDauList);

            var dauRate=[];
            dauRate.push(duoduoDauRate);
            dauRate.push(iconDauRate);
            dauRate.push(bannerDauRate);
            dauRate.push(advDauRate);
            dauRate.push(gameDauRate);
            dauRate.push(rollDauRate);
            dauRate.push(pointDauRate);
            dauRate.push(actCenterDauRate);
            dauRate.push(pushDauRate);
            dauRate.push(strongAdvDauRate);

            var signRate=[];
            signRate.push(duoduoSignRate);
            signRate.push(iconSignRate);
            signRate.push(bannerSignRate);
            signRate.push(advSignRate);
            signRate.push(gameSignRate);
            signRate.push(rollSignRate);
            signRate.push(pointSignRate);
            signRate.push(actCenterSignRate);
            signRate.push(pushSignRate);
            signRate.push(strongAdvSignRate);

            var bettingRate=[];
            bettingRate.push(duoduoBettingRate);
            bettingRate.push(iconBettingRate);
            bettingRate.push(bannerBettingRate);
            bettingRate.push(advBettingRate);
            bettingRate.push(gameBettingRate);
            bettingRate.push(rollBettingRate);
            bettingRate.push(pointBettingRate);
            bettingRate.push(actCenterBettingRate);
            bettingRate.push(pushBettingRate);
            bettingRate.push(strongAdvBettingRate);

            var payRate=[];
            payRate.push(duoduoPayRate);
            payRate.push(iconPayRate);
            payRate.push(bannerPayRate);
            payRate.push(advPayRate);
            payRate.push(gamePayRate);
            payRate.push(rollPayRate);
            payRate.push(pointPayRate);
            payRate.push(actCenterPayRate);
            payRate.push(pushPayRate);
            payRate.push(strongAdvPayRate);

            var retentionRate=[];
            retentionRate.push(duoduoRetentionRate);
            retentionRate.push(iconRetentionRate);
            retentionRate.push(bannerRetentionRate);
            retentionRate.push(advRetentionRate);
            retentionRate.push(gameRetentionRate);
            retentionRate.push(rollRetentionRate);
            retentionRate.push(pointRetentionRate);
            retentionRate.push(actCenterRetentionRate);
            retentionRate.push(pushRetentionRate);
            retentionRate.push(strongAdvRetentionRate);

            var titleList = [];
            titleList.push("发现页多多游戏入口--DAU及占比");
            titleList.push("首页顶部banner右侧小图标入口--DAU及占比");
            titleList.push("首页顶部banner入口--DAU及占比");
            titleList.push("新增广告位入口--DAU及占比");
            titleList.push("我的福利任务(玩游戏)入口--DAU及占比");
            titleList.push("幸运大转盘(金叶子跳转)入口--DAU及占比");
            titleList.push("我的积分商城(金叶子跳转)入口--DAU及占比");
            titleList.push("发现页活动中心入口--DAU及占比");
            titleList.push("push入口--DAU及占比");
            titleList.push("强广位入口--DAU及占比");

            var signTitleList = [];
            signTitleList.push("发现页多多游戏入口--签到转化率");
            signTitleList.push("首页顶部banner右侧小图标入口--签到转化率");
            signTitleList.push("首页顶部banner入口--签到转化率");
            signTitleList.push("新增广告位入口--签到转化率");
            signTitleList.push("我的福利任务(玩游戏)入口--签到转化率");
            signTitleList.push("幸运大转盘(金叶子跳转)入口--签到转化率");
            signTitleList.push("我的积分商城(金叶子跳转)入口--签到转化率");
            signTitleList.push("发现页活动中心入口--签到转化率");
            signTitleList.push("push入口--签到转化率");
            signTitleList.push("强广位入口--签到转化率");

            var bettingTitleList = [];
            bettingTitleList.push("发现页多多游戏入口--投注转化率");
            bettingTitleList.push("首页顶部banner右侧小图标入口--投注转化率");
            bettingTitleList.push("首页顶部banner入口--投注转化率");
            bettingTitleList.push("新增广告位入口--投注转化率");
            bettingTitleList.push("我的福利任务(玩游戏)入口--投注转化率");
            bettingTitleList.push("幸运大转盘(金叶子跳转)入口--投注转化率");
            bettingTitleList.push("我的积分商城(金叶子跳转)入口--投注转化率");
            bettingTitleList.push("发现页活动中心入口--投注转化率");
            bettingTitleList.push("push入口--投注转化率");
            bettingTitleList.push("强广位入口--投注转化率");

            var payTitleList = [];
            payTitleList.push("发现页多多游戏入口--付费渗透率");
            payTitleList.push("首页顶部banner右侧小图标入口--付费渗透率");
            payTitleList.push("首页顶部banner入口--付费渗透率");
            payTitleList.push("新增广告位入口--付费渗透率");
            payTitleList.push("我的福利任务(玩游戏)入口--付费渗透率");
            payTitleList.push("幸运大转盘(金叶子跳转)入口--付费渗透率");
            payTitleList.push("我的积分商城(金叶子跳转)入口--付费渗透率");
            payTitleList.push("发现页活动中心入口--付费渗透率");
            payTitleList.push("push入口--付费渗透率");
            payTitleList.push("强广位入口--付费渗透率");

            var retentionTitleList = [];
            retentionTitleList.push("发现页多多游戏入口--次日留存率");
            retentionTitleList.push("首页顶部banner右侧小图标入口--次日留存率");
            retentionTitleList.push("首页顶部banner入口--次日留存率");
            retentionTitleList.push("新增广告位入口--次日留存率");
            retentionTitleList.push("我的福利任务(玩游戏)入口--次日留存率");
            retentionTitleList.push("幸运大转盘(金叶子跳转)入口--次日留存率");
            retentionTitleList.push("我的积分商城(金叶子跳转)入口--次日留存率");
            retentionTitleList.push("发现页活动中心入口--次日留存率");
            retentionTitleList.push("push入口--次日留存率");
            retentionTitleList.push("强广位入口--次日留存率");

            var arrays =[];
            for(var i = 0;i < entranceDauRateList.length;i++){
                arrays[i] = {
                    value:entranceDauRateList[i],
                    name:eventNameList[i]
                }
            }
            var pieOption = {
                title : {
                    text: searchDate +'活跃用户各入口来源比例',
                    x:'center',
                    textStyle:{ //标题内容的样式
                        fontStyle:'normal',//主标题文字字体风格，默认normal，有italic(斜体),oblique(斜体)
                        fontWeight:"bold",//可选normal(正常)，bold(加粗)，bolder(加粗)，lighter(变细)，100|200|300|400|500...
                        fontFamily:"san-serif",//主题文字字体，默认微软雅黑
                        fontSize:22//主题文字字体大小，默认为18px
                    }
                },
                tooltip : {
                    trigger: 'item',
                    formatter: "{a}<br/>{b}:{c}({d}%)"
                },
                legend: {
                    orient:'vertical',
                    right:'1%',
                    top:'15%',
                    data:eventNameList
                },
                calculable : true,
                series : [
                    {
                        name:'入口来源',
                        type:'pie',
                        radius:'55%',
                        center: ['40%','60%'],
                        data:arrays
                    }
                ]
            };
            entranceAndRate(titleList[0],dateList,dauList[0],dauRate[0]);
            me.echarts = echarts.init(Ext.get("entPie").dom);
            me.echarts.setOption(pieOption);
            me.echarts.on("click", eConsole);

            transRate(searchDate+"各入口签到转化率",eventNameList,entranceSignRateList,0);
            me.echarts.on('click', function(param) {
            trendPic(signTitleList[param.dataIndex],dateList,signRate[param.dataIndex],0);
            });
            transRate(searchDate+"各入口投注转化率",eventNameList,entranceBettingRateList,1);
            me.echarts.on('click', function(param) {
            trendPic(bettingTitleList[param.dataIndex],dateList,bettingRate[param.dataIndex],1);
            });
            transRate(searchDate+"各入口付费渗透率",eventNameList,entrancePayRateList,2);
            me.echarts.on('click', function(param) {
                trendPic(payTitleList[param.dataIndex],dateList,payRate[param.dataIndex],2);
            });
            transRate(searchDate+"各入口次日留存率",eventNameList,entranceDayRetentionList,3);
            me.echarts.on('click', function(param) {
                trendPic(retentionTitleList[param.dataIndex],dateList,retentionRate[param.dataIndex],3);
            });
            trendPic(signTitleList[0],dateList,signRate[0],0);
            trendPic(bettingTitleList[0],dateList,bettingRate[0],1);
            trendPic(payTitleList[0],dateList,payRate[0],2);
            trendPic(retentionTitleList[0],dateList,retentionRate[0],3);

            // 增加饼图的监听事件
            function eConsole(param) {
                if (typeof param.seriesIndex != 'undefined') {
                    if (param.type == 'click') {
                        var pieLenght= pieOption.legend.data.length;
                        for(var i=0;i<pieLenght;i++){
                            everyClick(param,i,pieOption.legend.data[i],titleList[i],dateList,dauList[i],dauRate[i]);
                        }
                    }
                }
            }

            //点击饼图每块区域触发的事件
            function everyClick(param,i,txt,title,dateList,dauList,dauRate){
                if(param.seriesIndex==0 && param.dataIndex==i){
                    entranceAndRate(title,dateList,dauList,dauRate);
                }
            }
        }

        function trendPic(title,dateList,transRate,id){
            var rateOption =
                {
                    title: {text:title,left:'center',
                        textStyle:{//标题内容的样式
                            fontStyle:'normal',//主标题文字字体风格，默认normal，有italic(斜体),oblique(斜体)
                            // fontWeight:"bold",
                            fontFamily:"san-serif",//主题文字字体，默认微软雅黑
                            fontSize:18//主题文字字体大小，默认为18px
                        }},
                    tooltip: {trigger: 'axis',
                        formatter: function (params) {
                            var str='';
                            for(var i = 0; i < params.length; i++){
                                str += '日期:'+params[i].name+'<br/>'+ '转化率:' + params[i].value + '%';
                            }
                            return str;
                        }
                    },
                    toolbox: {
                        show : true,
                        feature : {
                            mark : {show: true}
                        }
                    },
                    calculable : true,
                    grid:{
                        left:'7%',
                        top:'20%',
                        right:'6%',
                        bottom:'15%'
                    },
                    xAxis: {
                        type : 'category',
                        data: dateList,
                        "axisLabel":{
                            interval: 0,
                            rotate:50
                        }
                    },
                    yAxis: {type : 'value',
                        axisLabel : {
                            formatter: '{value}%'
                        }
                    },
                    series: [{
                        name: title,
                        type: 'line',
                        smooth:true,
                        data: transRate,
                        itemStyle:{
                            normal:{
                                color:'cornflowerblue'
                            }
                        }
                    }
                    ]
                };
            me.echarts = echarts.init(Ext.get("entPic"+id).dom);
            me.echarts.setOption(rateOption);
        }

        function transRate(title,eventNameList,rateList,id){
            var rateOption =
                {
                    title: {text:title,left:'center',
                        textStyle:{//标题内容的样式
                            fontStyle:'normal',//主标题文字字体风格，默认normal，有italic(斜体),oblique(斜体)
                            // fontWeight:"bold",
                            fontFamily:"san-serif",//主题文字字体，默认微软雅黑
                            fontSize:18//主题文字字体大小，默认为18px
                        }},
                    tooltip: {trigger: 'axis',
                        formatter: function (params) {
                            var str='';
                            for(var i = 0; i < params.length; i++){
                                str += '入口:'+params[i].name+'<br/>'+ '转化率:' + params[i].value + '%';
                            }
                            return str;
                        }
                    },
                    toolbox: {
                        show : true,
                        feature : {
                            mark : {show: true}
                        }
                    },
                    calculable : true,
                    grid:{
                        left:'7%',
                        top:'20%',
                        right:'6%',
                        bottom:'40%'
                    },
                    xAxis: {
                        type : 'category',
                        data: eventNameList,
                        "axisLabel":{
                            interval: 0,
                            rotate:50
                        }
                    },
                    yAxis: {type : 'value',
                            axisLabel : {
                                formatter: '{value}%'
                            }
                            },
                    series: [{
                        name: title,
                        type: 'bar',
                        smooth:true,
                        data: rateList,
                        barWidth: 30,
                        itemStyle:{
                            normal:{
                                color:'cornflowerblue'
                            }
                        }
                       }
                    ]
                };
            me.echarts = echarts.init(Ext.get("entRate"+id).dom);
            me.echarts.setOption(rateOption);
        }


        function entranceAndRate(title,dateList,dauList,dauRateList){
            var mixOption =
                {
                    title: {text:title,left:'center',
                        textStyle:{//标题内容的样式
                            fontStyle:'normal',//主标题文字字体风格，默认normal，有italic(斜体),oblique(斜体)
                            // fontWeight:"bold",
                            fontFamily:"san-serif",//主题文字字体，默认微软雅黑
                            fontSize:18//主题文字字体大小，默认为18px
                        }},
                    tooltip: {trigger: 'axis',
                        formatter: function (params) {
                            var str='';
                            for(var i = 0; i < params.length; i++){
                                if (i == 0){
                                    str += '日期:'+params[i].name+'<br/>';
                                }
                                if(params[i].seriesName=='DAU占比'){
                                    str +=  params[i].seriesName +':'+params[i].value +"%<br/>";
                                }
                                else{
                                    str +=  params[i].seriesName +':'+params[i].value +"<br/>";
                                }
                            }
                            return str;
                        }
                    },
                    toolbox: {
                        show : true,
                        feature : {
                            mark : {show: true}
                        }
                    },
                    calculable : true,
                    grid:{
                        left:'10%',
                        top:'20%',
                        right:'6%',
                        bottom:'13%'
                    },
                    xAxis: {
                        type : 'category',
                        data: dateList,
                        "axisLabel":{
                            interval: 0,
                            rotate:20
                        }
                    },
                    yAxis: [
                        {type : 'value',
                        name:"DAU",
                        axisLabel : {
                            formatter: '{value}'
                        }
                            // splitLine: {          // 控制Y轴的分隔线(辅助线)
                            //     show: false      // 默认显示，属性show控制显示与
                            // }
                        },
                        {type : 'value',
                            name:"DAU占比",
                            axisLabel : {
                                formatter: '{value}%'
                            },
                            splitLine: {          // 控制Y轴的分隔线(辅助线)
                                show: false      // 默认显示，属性show控制显示与
                            }
                        }
                    ],
                    series: [{
                        name: 'DAU',
                        type: 'bar',
                        smooth:true,
                        data: dauList,
                        barWidth: 30,
                        itemStyle:{
                            normal:{
                                color:'cornflowerblue'
                            }
                        }
                    },
                    {
                        name: 'DAU占比',
                        type: 'line',
                        yAxisIndex: 1,
                        smooth:true,
                        data: dauRateList,
                        itemStyle:{
                            normal:{
                                color:'green'
                            }
                        }
                    }
                    ]
                };
              me.echarts = echarts.init(Ext.get("entMix").dom);
              me.echarts.setOption(mixOption);
           }
      }
});



