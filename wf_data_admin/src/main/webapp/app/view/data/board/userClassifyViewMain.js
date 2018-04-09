Ext.define('WF.view.data.board.userClassifyViewMain', {
    extend: 'Ext.panel.Panel',
    title: '数据概览-用户分层分析',
    xtype: 'userClassifyViewMain',
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
            url:'data/user/classify/getList.do',
            autoload:false,
            fields: ["0","1","2","3","4","5","6",
                "dateList","dayRetentionDateList","weekRetentionDateList","weekLostDateList"]
        });

        var parentChannelStore = Ext.create('DCIS.Store', {
            url: 'data/admin/common/data/getFilterChannels.do',
            autoLoad: true,
            fields: ['id', 'name']
        });

        me.add({
            border: false,
            store: store,
            xtype: 'searchpanel',
            title: '查询',
            collapsible: true,
            collapsed: false,
            columns: 2,
            buildField: "Manual",
            forceFit: true,
            items: [{
                name: 'parentId',
                fieldLabel: '主渠道',
                xtype: 'combo',
                emptyText: "--请选择--",
                displayField: 'name',
                valueField: "id",
                editable: true,
                queryMode: "local",
                width:275,
                store: parentChannelStore
            },
                {
                    name: 'tabId',
                    xtype: 'hiddenfield',
                    value: ''
                },
                {
                    name: 'startTime',
                    fieldLabel: '开始时间',
                    xtype: 'datefield',
                    format: 'Y-m-d',
                    value:Ext.util.Format.date(Ext.Date.add(new Date(),Ext.Date.DAY,-14),"Y-m-d")
                },
                {
                    name: 'endTime',
                    fieldLabel: '结束时间',
                    xtype: 'datefield',
                    format: 'Y-m-d',
                    value:Ext.util.Format.date(Ext.Date.add(new Date(),Ext.Date.DAY,-1),"Y-m-d")
                }]
        });

        me.add({
            name:'myTabs',
            xtype:'tabpanel',
            layout:'column',
            align : 'stretch',
            bodyStyle: 'border-width:0',
            items: [
                {
                    title: '活跃数据',
                    itemId: 'classifyActive',
                    width: "16.5%",
                    height: "100%",
                    xtype: "panel",
                    forceFit: true,
                    bodyStyle: 'border-color:black',
                    layout: 'vbox',
                    items: [
                        {
                            height:400,width:"100%",xtype:"panel",layout:'hbox',forceFit:true,bodyStyle:'border-width:0;',
                            items:[
                                {width:"100%",height:400,xtype:"panel",layout:'hbox',forceFit:true,bodyStyle:'border-width:0',items:[
                                    {id:'userClassifyActive0',width:"33.33%",height:400,xtype:"panel",layout:'vbox',forceFit:true,bodyStyle:'border-width:0;'},
                                    {id:'userClassifyActive1',width:"33.33%",height:400,xtype:"panel",layout:'vbox',forceFit:true,bodyStyle:'border-width:0;'},
                                    {width:"33.33%",height:400,xtype:"panel",forceFit:true,bodyStyle:'border-width:0;'}
                                ]
                                }
                            ]
                        },
                        {
                            height:400,width:"100%",xtype:"panel",layout:'hbox',forceFit:true,bodyStyle:'border-width:0;',
                            items:[
                                {width:"100%",height:400,xtype:"panel",layout:'hbox',forceFit:true,bodyStyle:'border-width:0',items:[
                                    {id:'userClassifyActive2',width:"33.33%",height:400,xtype:"panel",layout:'vbox',forceFit:true,bodyStyle:'border-width:0;'},
                                    {id:'userClassifyActive3',width:"33.33%",height:400,xtype:"panel",layout:'vbox',forceFit:true,bodyStyle:'border-width:0;'},
                                    {id:'userClassifyActive4',width:"33.33%",height:400,xtype:"panel",forceFit:true,bodyStyle:'border-width:0;'}
                                ]
                                }
                            ]
                        }
                    ],
                    listeners: {
                        'activate': function (tab) {
                            me.down(("[name='tabId']")).setValue(tab.itemId);
                            doSearch();
                        }
                    }
                },
                {
                    title: '投注数据',
                    itemId: 'classifyBetting',
                    width: "16.5%",
                    height: "100%",
                    xtype: "panel",
                    forceFit: true,
                    bodyStyle: 'border-color:black',
                    layout: 'vbox',
                    items: [
                        {
                            height:400,width:"100%",xtype:"panel",layout:'hbox',forceFit:true,bodyStyle:'border-width:0;',
                            items:[
                                {width:"100%",height:400,xtype:"panel",layout:'hbox',forceFit:true,bodyStyle:'border-width:0',items:[
                                    {id:'userClassifyBetting0',width:"33.33%",height:400,xtype:"panel",layout:'vbox',forceFit:true,bodyStyle:'border-width:0;'},
                                    {id:'userClassifyBetting1',width:"33.33%",height:400,xtype:"panel",layout:'vbox',forceFit:true,bodyStyle:'border-width:0;'},
                                    {id:'userClassifyBetting2',width:"33.33%",height:400,xtype:"panel",layout:'vbox',forceFit:true,bodyStyle:'border-width:0;'}
                                ]
                                }
                            ]
                        },
                        {
                            height:400,width:"100%",xtype:"panel",layout:'hbox',forceFit:true,bodyStyle:'border-width:0;',
                            items:[
                                {width:"100%",height:400,xtype:"panel",layout:'hbox',forceFit:true,bodyStyle:'border-width:0',items:[
                                    {id:'userClassifyBetting3',width:"33.33%",height:400,xtype:"panel",layout:'vbox',forceFit:true,bodyStyle:'border-width:0;'},
                                    {id:'userClassifyBetting4',width:"33.33%",height:400,xtype:"panel",layout:'vbox',forceFit:true,bodyStyle:'border-width:0;'},
                                    {id:'userClassifyBetting5',width:"33.33%",height:400,xtype:"panel",layout:'vbox',forceFit:true,bodyStyle:'border-width:0;'}
                                ]
                                }
                            ]
                        },
                        {
                            height:400,width:"100%",xtype:"panel",layout:'hbox',forceFit:true,bodyStyle:'border-width:0;',
                            items:[
                                {width:"100%",height:400,xtype:"panel",layout:'hbox',forceFit:true,bodyStyle:'border-width:0',items:[
                                    {id:'userClassifyBetting6',width:"33.33%",height:400,xtype:"panel",layout:'vbox',forceFit:true,bodyStyle:'border-width:0;'},
                                    {id:'userClassifyBetting7',width:"33.33%",height:400,xtype:"panel",layout:'vbox',forceFit:true,bodyStyle:'border-width:0;'},
                                    {id:'userClassifyBetting8',width:"33.33%",height:400,xtype:"panel",layout:'vbox',forceFit:true,bodyStyle:'border-width:0;'}
                                ]
                                }
                            ]
                        }
                    ],
                    listeners: {
                        'activate': function (tab) {
                            me.down(("[name='tabId']")).setValue(tab.itemId);
                            doSearch();
                        }
                    }
                },
                {
                    title:'付费数据',
                    itemId:'classifyPay',
                    width:"16.5%",
                    height:"100%",
                    xtype:"panel",
                    forceFit: true,
                    bodyStyle: 'border-color:black',
                    layout: 'vbox',
                    items: [
                        {
                            height:400,width:"100%",xtype:"panel",layout:'hbox',forceFit:true,bodyStyle:'border-width:0;',
                            items:[
                                {width:"100%",height:400,xtype:"panel",layout:'hbox',forceFit:true,bodyStyle:'border-width:0',items:[
                                    {id:'userClassifyPay0',width:"33.33%",height:400,xtype:"panel",layout:'vbox',forceFit:true,bodyStyle:'border-width:0;'},
                                    {id:'userClassifyPay1',width:"33.33%",height:400,xtype:"panel",layout:'vbox',forceFit:true,bodyStyle:'border-width:0;'},
                                    {id:'userClassifyPay2',width:"33.33%",height:400,xtype:"panel",layout:'vbox',forceFit:true,bodyStyle:'border-width:0;'}
                                ]
                                }
                            ]
                        },
                        {
                            height:400,width:"100%",xtype:"panel",layout:'hbox',forceFit:true,bodyStyle:'border-width:0;',
                            items:[
                                {width:"100%",height:400,xtype:"panel",layout:'hbox',forceFit:true,bodyStyle:'border-width:0',items:[
                                    {id:'userClassifyPay3',width:"33.33%",height:400,xtype:"panel",layout:'vbox',forceFit:true,bodyStyle:'border-width:0;'},
                                    {id:'userClassifyPay4',width:"33.33%",height:400,xtype:"panel",layout:'vbox',forceFit:true,bodyStyle:'border-width:0;'},
                                    {id:'userClassifyPay5',width:"33.33%",height:400,xtype:"panel",layout:'vbox',forceFit:true,bodyStyle:'border-width:0;'}
                                ]
                                }
                            ]
                        },
                        {
                            height:400,width:"100%",xtype:"panel",layout:'hbox',forceFit:true,bodyStyle:'border-width:0;',
                            items:[
                                {width:"100%",height:400,xtype:"panel",layout:'hbox',forceFit:true,bodyStyle:'border-width:0',items:[
                                    {id:'userClassifyPay6',width:"33.33%",height:400,xtype:"panel",layout:'vbox',forceFit:true,bodyStyle:'border-width:0;'},
                                    {id:'userClassifyPay7',width:"33.33%",height:400,xtype:"panel",layout:'vbox',forceFit:true,bodyStyle:'border-width:0;'},
                                    {width:"33.33%",height:400,xtype:"panel",layout:'vbox',forceFit:true,bodyStyle:'border-width:0;'}
                                ]
                                }
                            ]
                        },
                        {
                            height:400,width:"100%",xtype:"panel",layout:'hbox',forceFit:true,bodyStyle:'border-width:0;',
                            items:[
                                {width:"100%",height:400,xtype:"panel",layout:'hbox',forceFit:true,bodyStyle:'border-width:0',items:[
                                    {id:'userClassifyPay8',width:"33.33%",height:400,xtype:"panel",layout:'vbox',forceFit:true,bodyStyle:'border-width:0;'},
                                    {id:'userClassifyPay9',width:"33.33%",height:400,xtype:"panel",layout:'vbox',forceFit:true,bodyStyle:'border-width:0;'},
                                    {width:"33.33%",height:400,xtype:"panel",layout:'vbox',forceFit:true,bodyStyle:'border-width:0;'}
                                ]
                                }
                            ]
                        }
                    ],
                    listeners: {
                        'activate': function (tab) {
                            me.down(("[name='tabId']")).setValue(tab.itemId);
                            doSearch();
                        }
                    }
                },
                {
                    title:'留存数据',
                    itemId:'classifyRemain',
                    width:"16.5%",
                    height:"100%",
                    xtype:"panel",
                    forceFit: true,
                    bodyStyle: 'border-color:black',
                    layout: 'vbox',
                    items: [
                        {
                            height:400,width:"100%",xtype:"panel",layout:'hbox',forceFit:true,bodyStyle:'border-width:0;',
                            items:[
                                {width:"100%",height:400,xtype:"panel",layout:'hbox',forceFit:true,bodyStyle:'border-width:0',items:[
                                    {id:'userClassifyRemain0',width:"33.33%",height:400,xtype:"panel",layout:'vbox',forceFit:true,bodyStyle:'border-width:0;'},
                                    {id:'userClassifyRemain1',width:"33.33%",height:400,xtype:"panel",layout:'vbox',forceFit:true,bodyStyle:'border-width:0;'},
                                    {width:"33.33%",height:400,xtype:"panel",layout:'vbox',forceFit:true,bodyStyle:'border-width:0;'}
                                ]
                                }
                            ]
                        }
                    ],
                    listeners: {
                        'activate': function (tab) {
                            me.down(("[name='tabId']")).setValue(tab.itemId);
                            doSearch();
                        }
                    }
                },
                {
                    title:'流失数据',
                    itemId:'classifyLost',
                    width:"16.5%",
                    height:"100%",
                    xtype:"panel",
                    forceFit:true,
                    bodyStyle:'border-color:black',
                    layout: 'vbox',
                    items: [
                        {
                            height:400,width:"100%",xtype:"panel",layout:'hbox',forceFit:true,bodyStyle:'border-width:0;',
                            items:[
                                {width:"100%",height:400,xtype:"panel",layout:'hbox',forceFit:true,bodyStyle:'border-width:0',items:[
                                    {id:'userClassifyLost0',width:"33.33%",height:400,xtype:"panel",layout:'vbox',forceFit:true,bodyStyle:'border-width:0;'},
                                    {width:"33.33%",height:400,xtype:"panel",layout:'vbox',forceFit:true,bodyStyle:'border-width:0;'},
                                    {width:"33.33%",height:400,xtype:"panel",layout:'vbox',forceFit:true,bodyStyle:'border-width:0;'}
                                ]
                                }
                            ]
                        }
                    ],
                    listeners: {
                        'activate': function (tab) {
                            me.down(("[name='tabId']")).setValue(tab.itemId);
                            doSearch();
                        }
                    }
                }
            ]
        });

        store.addListener('datachanged',function(){
                var value = {
                    tabId:me.down("[name='tabId']").value
                };
                fun1(value.tabId);
            }
        );

        store.load ({
            callback:function(){
                fun1("classifyActive");
            }
        });

        function doSearch() {
            var value = {
                parentId:me.down("[name='parentId']").value,
                tabId:me.down("[name='tabId']").value,
                startTime:Ext.util.Format.date(me.down("[name='startTime']").value, "Y-m-d"),
                endTime:Ext.util.Format.date(me.down("[name='endTime']").value, "Y-m-d")
            };
            store.load({
                params: value,
                callback:function(){
                    fun1(value.tabId);
                }
            });
        }

        function fun1(tabId){
            var legArr =  ['新注册','累充0','累充1-100','累充100-1000','累充1000-10000','累充10000-100000','累充10万以上'];
            var dateList = store.getAt(0).get("dateList");
            var dayRetentionDateList = store.getAt(0).get("dayRetentionDateList");
            var weekRetentionDateList = store.getAt(0).get("weekRetentionDateList");
            var weekLostDateList = store.getAt(0).get("weekLostDateList");
            console.log(dayRetentionDateList);
            console.log(weekRetentionDateList);
            console.log(weekLostDateList);
            var newUserList = store.getAt(0).get("0");
            var rechargeOneList = store.getAt(0).get("1");
            var rechargeTwoList = store.getAt(0).get("2");
            var rechargeThreeList = store.getAt(0).get("3");
            var rechargeFourList = store.getAt(0).get("4");
            var rechargeFiveList = store.getAt(0).get("5");
            var rechargeSixList = store.getAt(0).get("6");
            var size = newUserList.length;

            if (tabId == 'classifyActive'){
                //1、DAU
                var newUserDauList = [];
                var rechargeOneDauList = [];
                var rechargeTwoDauList = [];
                var rechargeThreeDauList = [];
                var rechargeFourDauList = [];
                var rechargeFiveDauList = [];
                var rechargeSixDauList = [];
                //2、日活占比
                var newUserDauRateList = [];
                var rechargeOneDauRateList = [];
                var rechargeTwoDauRateList = [];
                var rechargeThreeDauRateList = [];
                var rechargeFourDauRateList = [];
                var rechargeFiveDauRateList = [];
                var rechargeSixDauRateList = [];
                //3、累计用户数
                var newUserTotalCountList = [];
                var rechargeOneTotalCountList = [];
                var rechargeTwoTotalCountList = [];
                var rechargeThreeTotalCountList = [];
                var rechargeFourTotalCountList = [];
                var rechargeFiveTotalCountList = [];
                var rechargeSixTotalCountList = [];
                //4、活跃率
                var newUserTotalRateList = [];
                var rechargeOneTotalRateList = [];
                var rechargeTwoTotalRateList = [];
                var rechargeThreeTotalRateList = [];
                var rechargeFourTotalRateList = [];
                var rechargeFiveTotalRateList = [];
                var rechargeSixTotalRateList = [];
                //5、人均登陆次数
                var newUserLoginCountList = [];
                var rechargeOneLoginCountList = [];
                var rechargeTwoLoginCountList = [];
                var rechargeThreeLoginCountList = [];
                var rechargeFourLoginCountList = [];
                var rechargeFiveLoginCountList = [];
                var rechargeSixLoginCountList = [];


                for (var i=0;i<size;i++) {
                    newUserDauList[i] = newUserList[i].dau;
                    rechargeOneDauList[i] = rechargeOneList[i].dau;
                    rechargeTwoDauList[i] = rechargeTwoList[i].dau;
                    rechargeThreeDauList[i] = rechargeThreeList[i].dau;
                    rechargeFourDauList[i] = rechargeFourList[i].dau;
                    rechargeFiveDauList[i] = rechargeFiveList[i].dau;
                    rechargeSixDauList[i] = rechargeSixList[i].dau;

                    newUserDauRateList[i] = newUserList[i].dauRate;
                    rechargeOneDauRateList[i] = rechargeOneList[i].dauRate;
                    rechargeTwoDauRateList[i] = rechargeTwoList[i].dauRate;
                    rechargeThreeDauRateList[i] = rechargeThreeList[i].dauRate;
                    rechargeFourDauRateList[i] = rechargeFourList[i].dauRate;
                    rechargeFiveDauRateList[i] = rechargeFiveList[i].dauRate;
                    rechargeSixDauRateList[i] = rechargeSixList[i].dauRate;

                    newUserTotalCountList[i] = newUserList[i].totalUserCount;
                    rechargeOneTotalCountList[i] = rechargeOneList[i].totalUserCount;
                    rechargeTwoTotalCountList[i] = rechargeTwoList[i].totalUserCount;
                    rechargeThreeTotalCountList[i] = rechargeThreeList[i].totalUserCount;
                    rechargeFourTotalCountList[i] = rechargeFourList[i].totalUserCount;
                    rechargeFiveTotalCountList[i] = rechargeFiveList[i].totalUserCount;
                    rechargeSixTotalCountList[i] = rechargeSixList[i].totalUserCount;

                    newUserTotalRateList[i] = newUserList[i].totalUserRate;
                    rechargeOneTotalRateList[i] = rechargeOneList[i].totalUserRate;
                    rechargeTwoTotalRateList[i] = rechargeTwoList[i].totalUserRate;
                    rechargeThreeTotalRateList[i] = rechargeThreeList[i].totalUserRate;
                    rechargeFourTotalRateList[i] = rechargeFourList[i].totalUserRate;
                    rechargeFiveTotalRateList[i] = rechargeFiveList[i].totalUserRate;
                    rechargeSixTotalRateList[i] = rechargeSixList[i].totalUserRate;

                    newUserLoginCountList[i] = newUserList[i].loginCount;
                    rechargeOneLoginCountList[i] = rechargeOneList[i].loginCount;
                    rechargeTwoLoginCountList[i] = rechargeTwoList[i].loginCount;
                    rechargeThreeLoginCountList[i] = rechargeThreeList[i].loginCount;
                    rechargeFourLoginCountList[i] = rechargeFourList[i].loginCount;
                    rechargeFiveLoginCountList[i] = rechargeFiveList[i].loginCount;
                    rechargeSixLoginCountList[i] = rechargeSixList[i].loginCount;
                }

                var dauOption = template("DAU",dateList,legArr);
                dauOption.series.push(areaStyleSeries(legArr[0],newUserDauList));
                dauOption.series.push(areaStyleSeries(legArr[1],rechargeOneDauList));
                dauOption.series.push(areaStyleSeries(legArr[2],rechargeTwoDauList));
                dauOption.series.push(areaStyleSeries(legArr[3],rechargeThreeDauList));
                dauOption.series.push(areaStyleSeries(legArr[4],rechargeFourDauList));
                dauOption.series.push(areaStyleSeries(legArr[5],rechargeFiveDauList));
                dauOption.series.push(areaStyleSeries(legArr[6],rechargeSixDauList));

                var dauRateOption = barTemplate("日活占比",dateList,legArr);
                dauRateOption.series.push(stackBarStyleSeries(legArr[0],newUserDauRateList));
                dauRateOption.series.push(stackBarStyleSeries(legArr[1],rechargeOneDauRateList));
                dauRateOption.series.push(stackBarStyleSeries(legArr[2],rechargeTwoDauRateList));
                dauRateOption.series.push(stackBarStyleSeries(legArr[3],rechargeThreeDauRateList));
                dauRateOption.series.push(stackBarStyleSeries(legArr[4],rechargeFourDauRateList));
                dauRateOption.series.push(stackBarStyleSeries(legArr[5],rechargeFiveDauRateList));
                dauRateOption.series.push(stackBarStyleSeries(legArr[6],rechargeSixDauRateList));

                var totalCountOption = template("累计用户数",dateList,legArr);
                totalCountOption.series.push(areaStyleSeries(legArr[0],newUserTotalCountList));
                totalCountOption.series.push(areaStyleSeries(legArr[1],rechargeOneTotalCountList));
                totalCountOption.series.push(areaStyleSeries(legArr[2],rechargeTwoTotalCountList));
                totalCountOption.series.push(areaStyleSeries(legArr[3],rechargeThreeTotalCountList));
                totalCountOption.series.push(areaStyleSeries(legArr[4],rechargeFourTotalCountList));
                totalCountOption.series.push(areaStyleSeries(legArr[5],rechargeFiveTotalCountList));
                totalCountOption.series.push(areaStyleSeries(legArr[6],rechargeSixTotalCountList));

                var totalRateOption = rateTemplate("活跃率",dateList,legArr);
                totalRateOption.series.push(lineStyleSeries(legArr[0],newUserTotalRateList));
                totalRateOption.series.push(lineStyleSeries(legArr[1],rechargeOneTotalRateList));
                totalRateOption.series.push(lineStyleSeries(legArr[2],rechargeTwoTotalRateList));
                totalRateOption.series.push(lineStyleSeries(legArr[3],rechargeThreeTotalRateList));
                totalRateOption.series.push(lineStyleSeries(legArr[4],rechargeFourTotalRateList));
                totalRateOption.series.push(lineStyleSeries(legArr[5],rechargeFiveTotalRateList));
                totalRateOption.series.push(lineStyleSeries(legArr[6],rechargeSixTotalRateList));

                var loginCountOption = template("人均登录次数",dateList,legArr);
                loginCountOption.series.push(lineStyleSeries(legArr[0],newUserLoginCountList));
                loginCountOption.series.push(lineStyleSeries(legArr[1],rechargeOneLoginCountList));
                loginCountOption.series.push(lineStyleSeries(legArr[2],rechargeTwoLoginCountList));
                loginCountOption.series.push(lineStyleSeries(legArr[3],rechargeThreeLoginCountList));
                loginCountOption.series.push(lineStyleSeries(legArr[4],rechargeFourLoginCountList));
                loginCountOption.series.push(lineStyleSeries(legArr[5],rechargeFiveLoginCountList));
                loginCountOption.series.push(lineStyleSeries(legArr[6],rechargeSixLoginCountList));

                //活跃数据
                var activeOption = [dauOption,dauRateOption,totalCountOption,totalRateOption,loginCountOption];

                for (var j=0;j<activeOption.length;j++) {
                    me.echarts = echarts.init(Ext.get("userClassifyActive"+j).dom);
                    me.echarts.setOption(activeOption[j]);
                }
            }

            if (tabId == 'classifyBetting') {
                //6、投注流水
                var newUserBettingAmountList = [];
                var rechargeOneBettingAmountList = [];
                var rechargeTwoBettingAmountList = [];
                var rechargeThreeBettingAmountList = [];
                var rechargeFourBettingAmountList = [];
                var rechargeFiveBettingAmountList = [];
                var rechargeSixBettingAmountList = [];

                //7、流水差
                var newUserDiffAmountList = [];
                var rechargeOneDiffAmountList = [];
                var rechargeTwoDiffAmountList = [];
                var rechargeThreeDiffAmountList = [];
                var rechargeFourDiffAmountList = [];
                var rechargeFiveDiffAmountList = [];
                var rechargeSixDiffAmountList = [];

                //8、返奖率
                var newUserResultRateList = [];
                var rechargeOneResultRateList = [];
                var rechargeTwoResultRateList = [];
                var rechargeThreeResultRateList = [];
                var rechargeFourResultRateList = [];
                var rechargeFiveResultRateList = [];
                var rechargeSixResultRateList = [];

                //9、投注转化率
                var newUserBettingRateList = [];
                var rechargeOneBettingRateList = [];
                var rechargeTwoBettingRateList = [];
                var rechargeThreeBettingRateList = [];
                var rechargeFourBettingRateList = [];
                var rechargeFiveBettingRateList = [];
                var rechargeSixBettingRateList = [];

                //10、投注人数
                var newUserBettingUserCountList = [];
                var rechargeOneBettingUserCountList = [];
                var rechargeTwoBettingUserCountList = [];
                var rechargeThreeBettingUserCountList = [];
                var rechargeFourBettingUserCountList = [];
                var rechargeFiveBettingUserCountList = [];
                var rechargeSixBettingUserCountList = [];

                //11、投注ARPU
                var newUserBettingArpuList = [];
                var rechargeOneBettingArpuList = [];
                var rechargeTwoBettingArpuList = [];
                var rechargeThreeBettingArpuList = [];
                var rechargeFourBettingArpuList = [];
                var rechargeFiveBettingArpuList = [];
                var rechargeSixBettingArpuList = [];

                //12、总投注笔数
                var newUserBettingCountList = [];
                var rechargeOneBettingCountList = [];
                var rechargeTwoBettingCountList = [];
                var rechargeThreeBettingCountList = [];
                var rechargeFourBettingCountList = [];
                var rechargeFiveBettingCountList = [];
                var rechargeSixBettingCountList = [];

                //13、人均投注笔数
                var newUserAvgBettingCountList = [];
                var rechargeOneAvgBettingCountList = [];
                var rechargeTwoAvgBettingCountList = [];
                var rechargeThreeAvgBettingCountList = [];
                var rechargeFourAvgBettingCountList = [];
                var rechargeFiveAvgBettingCountList = [];
                var rechargeSixAvgBettingCountList = [];

                //14、人均投注ASP
                var newUserAvgBettingAspList = [];
                var rechargeOneAvgBettingAspList = [];
                var rechargeTwoAvgBettingAspList = [];
                var rechargeThreeAvgBettingAspList = [];
                var rechargeFourAvgBettingAspList = [];
                var rechargeFiveAvgBettingAspList = [];
                var rechargeSixAvgBettingAspList = [];

                for (var i=0;i<size;i++) {
                    newUserBettingAmountList[i] = newUserList[i].bettingAmount;
                    rechargeOneBettingAmountList[i] = rechargeOneList[i].bettingAmount;
                    rechargeTwoBettingAmountList[i] = rechargeTwoList[i].bettingAmount;
                    rechargeThreeBettingAmountList[i] = rechargeThreeList[i].bettingAmount;
                    rechargeFourBettingAmountList[i] = rechargeFourList[i].bettingAmount;
                    rechargeFiveBettingAmountList[i] = rechargeFiveList[i].bettingAmount;
                    rechargeSixBettingAmountList[i] = rechargeSixList[i].bettingAmount;

                    newUserDiffAmountList[i] = newUserList[i].diffAmount;
                    rechargeOneDiffAmountList[i] = rechargeOneList[i].diffAmount;
                    rechargeTwoDiffAmountList[i] = rechargeTwoList[i].diffAmount;
                    rechargeThreeDiffAmountList[i] = rechargeThreeList[i].diffAmount;
                    rechargeFourDiffAmountList[i] = rechargeFourList[i].diffAmount;
                    rechargeFiveDiffAmountList[i] = rechargeFiveList[i].diffAmount;
                    rechargeSixDiffAmountList[i] = rechargeSixList[i].diffAmount;

                    newUserResultRateList[i] = newUserList[i].resultRate;
                    rechargeOneResultRateList[i] = rechargeOneList[i].resultRate;
                    rechargeTwoResultRateList[i] = rechargeTwoList[i].resultRate;
                    rechargeThreeResultRateList[i] = rechargeThreeList[i].resultRate;
                    rechargeFourResultRateList[i] = rechargeFourList[i].resultRate;
                    rechargeFiveResultRateList[i] = rechargeFiveList[i].resultRate;
                    rechargeSixResultRateList[i] = rechargeSixList[i].resultRate;

                    newUserBettingRateList[i] = newUserList[i].bettingRate;
                    rechargeOneBettingRateList[i] = rechargeOneList[i].bettingRate;
                    rechargeTwoBettingRateList[i] = rechargeTwoList[i].bettingRate;
                    rechargeThreeBettingRateList[i] = rechargeThreeList[i].bettingRate;
                    rechargeFourBettingRateList[i] = rechargeFourList[i].bettingRate;
                    rechargeFiveBettingRateList[i] = rechargeFiveList[i].bettingRate;
                    rechargeSixBettingRateList[i] = rechargeSixList[i].bettingRate;

                    newUserBettingUserCountList[i] = newUserList[i].bettingUserCount;
                    rechargeOneBettingUserCountList[i] = rechargeOneList[i].bettingUserCount;
                    rechargeTwoBettingUserCountList[i] = rechargeTwoList[i].bettingUserCount;
                    rechargeThreeBettingUserCountList[i] = rechargeThreeList[i].bettingUserCount;
                    rechargeFourBettingUserCountList[i] = rechargeFourList[i].bettingUserCount;
                    rechargeFiveBettingUserCountList[i] = rechargeFiveList[i].bettingUserCount;
                    rechargeSixBettingUserCountList[i] = rechargeSixList[i].bettingUserCount;

                    newUserBettingArpuList[i] = newUserList[i].bettingArpu;
                    rechargeOneBettingArpuList[i] = rechargeOneList[i].bettingArpu;
                    rechargeTwoBettingArpuList[i] = rechargeTwoList[i].bettingArpu;
                    rechargeThreeBettingArpuList[i] = rechargeThreeList[i].bettingArpu;
                    rechargeFourBettingArpuList[i] = rechargeFourList[i].bettingArpu;
                    rechargeFiveBettingArpuList[i] = rechargeFiveList[i].bettingArpu;
                    rechargeSixBettingArpuList[i] = rechargeSixList[i].bettingArpu;

                    newUserBettingCountList[i] = newUserList[i].bettingCount;
                    rechargeOneBettingCountList[i] = rechargeOneList[i].bettingCount;
                    rechargeTwoBettingCountList[i] = rechargeTwoList[i].bettingCount;
                    rechargeThreeBettingCountList[i] = rechargeThreeList[i].bettingCount;
                    rechargeFourBettingCountList[i] = rechargeFourList[i].bettingCount;
                    rechargeFiveBettingCountList[i] = rechargeFiveList[i].bettingCount;
                    rechargeSixBettingCountList[i] = rechargeSixList[i].bettingCount;

                    newUserAvgBettingCountList[i] = newUserList[i].averageBettingCount;
                    rechargeOneAvgBettingCountList[i] = rechargeOneList[i].averageBettingCount;
                    rechargeTwoAvgBettingCountList[i] = rechargeTwoList[i].averageBettingCount;
                    rechargeThreeAvgBettingCountList[i] = rechargeThreeList[i].averageBettingCount;
                    rechargeFourAvgBettingCountList[i] = rechargeFourList[i].averageBettingCount;
                    rechargeFiveAvgBettingCountList[i] = rechargeFiveList[i].averageBettingCount;
                    rechargeSixAvgBettingCountList[i] = rechargeSixList[i].averageBettingCount;

                    newUserAvgBettingAspList[i] = newUserList[i].averageBettingAsp;
                    rechargeOneAvgBettingAspList[i] = rechargeOneList[i].averageBettingAsp;
                    rechargeTwoAvgBettingAspList[i] = rechargeTwoList[i].averageBettingAsp;
                    rechargeThreeAvgBettingAspList[i] = rechargeThreeList[i].averageBettingAsp;
                    rechargeFourAvgBettingAspList[i] = rechargeFourList[i].averageBettingAsp;
                    rechargeFiveAvgBettingAspList[i] = rechargeFiveList[i].averageBettingAsp;
                    rechargeSixAvgBettingAspList[i] = rechargeSixList[i].averageBettingAsp;
                }

                var bettingAmountOption = moneyTemplate("投注流水",dateList,legArr);
                bettingAmountOption.series.push(areaStyleSeries(legArr[0],newUserBettingAmountList));
                bettingAmountOption.series.push(areaStyleSeries(legArr[1],rechargeOneBettingAmountList));
                bettingAmountOption.series.push(areaStyleSeries(legArr[2],rechargeTwoBettingAmountList));
                bettingAmountOption.series.push(areaStyleSeries(legArr[3],rechargeThreeBettingAmountList));
                bettingAmountOption.series.push(areaStyleSeries(legArr[4],rechargeFourBettingAmountList));
                bettingAmountOption.series.push(areaStyleSeries(legArr[5],rechargeFiveBettingAmountList));
                bettingAmountOption.series.push(areaStyleSeries(legArr[6],rechargeSixBettingAmountList));

                var diffAmountOption = moneyTemplate("流水差",dateList,legArr);
                diffAmountOption.series.push(areaStyleSeries(legArr[0],newUserDiffAmountList));
                diffAmountOption.series.push(areaStyleSeries(legArr[1],rechargeOneDiffAmountList));
                diffAmountOption.series.push(areaStyleSeries(legArr[2],rechargeTwoDiffAmountList));
                diffAmountOption.series.push(areaStyleSeries(legArr[3],rechargeThreeDiffAmountList));
                diffAmountOption.series.push(areaStyleSeries(legArr[4],rechargeFourDiffAmountList));
                diffAmountOption.series.push(areaStyleSeries(legArr[5],rechargeFiveDiffAmountList));
                diffAmountOption.series.push(areaStyleSeries(legArr[6],rechargeSixDiffAmountList));

                var resultRateOption = returnTemplate("返奖率",dateList,legArr);
                resultRateOption.series.push(lineStyleSeries(legArr[0],newUserResultRateList));
                resultRateOption.series.push(lineStyleSeries(legArr[1],rechargeOneResultRateList));
                resultRateOption.series.push(lineStyleSeries(legArr[2],rechargeTwoResultRateList));
                resultRateOption.series.push(lineStyleSeries(legArr[3],rechargeThreeResultRateList));
                resultRateOption.series.push(lineStyleSeries(legArr[4],rechargeFourResultRateList));
                resultRateOption.series.push(lineStyleSeries(legArr[5],rechargeFiveResultRateList));
                resultRateOption.series.push(lineStyleSeries(legArr[6],rechargeSixResultRateList));

                var bettingRateOption = rateTemplate("投注转化率",dateList,legArr);
                bettingRateOption.series.push(lineStyleSeries(legArr[0],newUserBettingRateList));
                bettingRateOption.series.push(lineStyleSeries(legArr[1],rechargeOneBettingRateList));
                bettingRateOption.series.push(lineStyleSeries(legArr[2],rechargeTwoBettingRateList));
                bettingRateOption.series.push(lineStyleSeries(legArr[3],rechargeThreeBettingRateList));
                bettingRateOption.series.push(lineStyleSeries(legArr[4],rechargeFourBettingRateList));
                bettingRateOption.series.push(lineStyleSeries(legArr[5],rechargeFiveBettingRateList));
                bettingRateOption.series.push(lineStyleSeries(legArr[6],rechargeSixBettingRateList));

                var bettingUserCountOption = template("投注人数",dateList,legArr);
                bettingUserCountOption.series.push(areaStyleSeries(legArr[0],newUserBettingUserCountList));
                bettingUserCountOption.series.push(areaStyleSeries(legArr[1],rechargeOneBettingUserCountList));
                bettingUserCountOption.series.push(areaStyleSeries(legArr[2],rechargeTwoBettingUserCountList));
                bettingUserCountOption.series.push(areaStyleSeries(legArr[3],rechargeThreeBettingUserCountList));
                bettingUserCountOption.series.push(areaStyleSeries(legArr[4],rechargeFourBettingUserCountList));
                bettingUserCountOption.series.push(areaStyleSeries(legArr[5],rechargeFiveBettingUserCountList));
                bettingUserCountOption.series.push(areaStyleSeries(legArr[6],rechargeSixBettingUserCountList));

                var bettingArpuOption = preciseMoneyTemplate("投注ARPU",dateList,legArr);
                bettingArpuOption.series.push(lineStyleSeries(legArr[0],newUserBettingArpuList));
                bettingArpuOption.series.push(lineStyleSeries(legArr[1],rechargeOneBettingArpuList));
                bettingArpuOption.series.push(lineStyleSeries(legArr[2],rechargeTwoBettingArpuList));
                bettingArpuOption.series.push(lineStyleSeries(legArr[3],rechargeThreeBettingArpuList));
                bettingArpuOption.series.push(lineStyleSeries(legArr[4],rechargeFourBettingArpuList));
                bettingArpuOption.series.push(lineStyleSeries(legArr[5],rechargeFiveBettingArpuList));
                bettingArpuOption.series.push(lineStyleSeries(legArr[6],rechargeSixBettingArpuList));

                var bettingCountOption = moneyTemplate("总投注笔数",dateList,legArr);
                bettingCountOption.series.push(areaStyleSeries(legArr[0],newUserBettingCountList));
                bettingCountOption.series.push(areaStyleSeries(legArr[1],rechargeOneBettingCountList));
                bettingCountOption.series.push(areaStyleSeries(legArr[2],rechargeTwoBettingCountList));
                bettingCountOption.series.push(areaStyleSeries(legArr[3],rechargeThreeBettingCountList));
                bettingCountOption.series.push(areaStyleSeries(legArr[4],rechargeFourBettingCountList));
                bettingCountOption.series.push(areaStyleSeries(legArr[5],rechargeFiveBettingCountList));
                bettingCountOption.series.push(areaStyleSeries(legArr[6],rechargeSixBettingCountList));

                var avgBettingCountOption = template("人均投注笔数",dateList,legArr);
                avgBettingCountOption.series.push(lineStyleSeries(legArr[0],newUserAvgBettingCountList));
                avgBettingCountOption.series.push(lineStyleSeries(legArr[1],rechargeOneAvgBettingCountList));
                avgBettingCountOption.series.push(lineStyleSeries(legArr[2],rechargeTwoAvgBettingCountList));
                avgBettingCountOption.series.push(lineStyleSeries(legArr[3],rechargeThreeAvgBettingCountList));
                avgBettingCountOption.series.push(lineStyleSeries(legArr[4],rechargeFourAvgBettingCountList));
                avgBettingCountOption.series.push(lineStyleSeries(legArr[5],rechargeFiveAvgBettingCountList));
                avgBettingCountOption.series.push(lineStyleSeries(legArr[6],rechargeSixAvgBettingCountList));

                var avgBettingAspOption = template("人均投注ASP",dateList,legArr);
                avgBettingAspOption.series.push(lineStyleSeries(legArr[0],newUserAvgBettingAspList));
                avgBettingAspOption.series.push(lineStyleSeries(legArr[1],rechargeOneAvgBettingAspList));
                avgBettingAspOption.series.push(lineStyleSeries(legArr[2],rechargeTwoAvgBettingAspList));
                avgBettingAspOption.series.push(lineStyleSeries(legArr[3],rechargeThreeAvgBettingAspList));
                avgBettingAspOption.series.push(lineStyleSeries(legArr[4],rechargeFourAvgBettingAspList));
                avgBettingAspOption.series.push(lineStyleSeries(legArr[5],rechargeFiveAvgBettingAspList));
                avgBettingAspOption.series.push(lineStyleSeries(legArr[6],rechargeSixAvgBettingAspList));

                //投注数据
                var bettingOption = [bettingAmountOption, diffAmountOption, resultRateOption,
                    bettingRateOption, bettingUserCountOption, bettingArpuOption,
                    bettingCountOption, avgBettingCountOption, avgBettingAspOption];

                for (var k = 0; k < bettingOption.length; k++) {
                    me.echarts = echarts.init(Ext.get("userClassifyBetting"+ k).dom);
                    me.echarts.setOption(bettingOption[k]);
                }
            }

            if (tabId == 'classifyPay') {
                //15、充值金额
                var newUserThirdAmountList = [];
                var rechargeOneThirdAmountList = [];
                var rechargeTwoThirdAmountList = [];
                var rechargeThreeThirdAmountList = [];
                var rechargeFourThirdAmountList = [];
                var rechargeFiveThirdAmountList = [];
                var rechargeSixThirdAmountList = [];

                //16、充值金额占比
                var newUserRechargeRateList = [];
                var rechargeOneRechargeRateList = [];
                var rechargeTwoRechargeRateList = [];
                var rechargeThreeRechargeRateList = [];
                var rechargeFourRechargeRateList = [];
                var rechargeFiveRechargeRateList = [];
                var rechargeSixRechargeRateList = [];

                //17、充值人数
                var newUserRechargeUserCountList = [];
                var rechargeOneRechargeUserCountList = [];
                var rechargeTwoRechargeUserCountList = [];
                var rechargeThreeRechargeUserCountList = [];
                var rechargeFourRechargeUserCountList = [];
                var rechargeFiveRechargeUserCountList = [];
                var rechargeSixRechargeUserCountList = [];

                //18、DAU付费率
                var newUserDauRechargeRateList = [];
                var rechargeOneDauRechargeRateList = [];
                var rechargeTwoDauRechargeRateList = [];
                var rechargeThreeDauRechargeRateList = [];
                var rechargeFourDauRechargeRateList = [];
                var rechargeFiveDauRechargeRateList = [];
                var rechargeSixDauRechargeRateList = [];

                //19、投注付费率
                var newUserBettingRechargeRateList = [];
                var rechargeOneBettingRechargeRateList = [];
                var rechargeTwoBettingRechargeRateList = [];
                var rechargeThreeBettingRechargeRateList = [];
                var rechargeFourBettingRechargeRateList = [];
                var rechargeFiveBettingRechargeRateList = [];
                var rechargeSixBettingRechargeRateList = [];

                //20、DARPPU
                var newUserDarppuRateList = [];
                var rechargeOneDarppuRateList = [];
                var rechargeTwoDarppuRateList = [];
                var rechargeThreeDarppuRateList = [];
                var rechargeFourDarppuRateList = [];
                var rechargeFiveDarppuRateList = [];
                var rechargeSixDarppuRateList = [];

                //21、点击支付率
                var newUserPointPayRateList = [];
                var rechargeOnePointPayRateList = [];
                var rechargeTwoPointPayRateList = [];
                var rechargeThreePointPayRateList = [];
                var rechargeFourPointPayRateList = [];
                var rechargeFivePointPayRateList = [];
                var rechargeSixPointPayRateList = [];

                //22、支付成功率
                var newUserPaySuccessRateList = [];
                var rechargeOnePaySuccessRateList = [];
                var rechargeTwoPaySuccessRateList = [];
                var rechargeThreePaySuccessRateList = [];
                var rechargeFourPaySuccessRateList = [];
                var rechargeFivePaySuccessRateList = [];
                var rechargeSixPaySuccessRateList = [];

                //23、人均充值次数
                var newUserAvgPayCountList = [];
                var rechargeOneAvgPayCountList = [];
                var rechargeTwoAvgPayCountList = [];
                var rechargeThreeAvgPayCountList = [];
                var rechargeFourAvgPayCountList = [];
                var rechargeFiveAvgPayCountList = [];
                var rechargeSixAvgPayCountList = [];

                //24、人均单次充值金额
                var newUserAvgPayAmountList = [];
                var rechargeOneAvgPayAmountList = [];
                var rechargeTwoAvgPayAmountList = [];
                var rechargeThreeAvgPayAmountList = [];
                var rechargeFourAvgPayAmountList = [];
                var rechargeFiveAvgPayAmountList = [];
                var rechargeSixAvgPayAmountList = [];

                for (var i=0;i<size;i++) {
                    newUserThirdAmountList[i] = newUserList[i].thirdAmount;
                    rechargeOneThirdAmountList[i] = rechargeOneList[i].thirdAmount;
                    rechargeTwoThirdAmountList[i] = rechargeTwoList[i].thirdAmount;
                    rechargeThreeThirdAmountList[i] = rechargeThreeList[i].thirdAmount;
                    rechargeFourThirdAmountList[i] = rechargeFourList[i].thirdAmount;
                    rechargeFiveThirdAmountList[i] = rechargeFiveList[i].thirdAmount;
                    rechargeSixThirdAmountList[i] = rechargeSixList[i].thirdAmount;

                    newUserRechargeRateList[i] = newUserList[i].rechargeRate;
                    rechargeOneRechargeRateList[i] = rechargeOneList[i].rechargeRate;
                    rechargeTwoRechargeRateList[i] = rechargeTwoList[i].rechargeRate;
                    rechargeThreeRechargeRateList[i] = rechargeThreeList[i].rechargeRate;
                    rechargeFourRechargeRateList[i] = rechargeFourList[i].rechargeRate;
                    rechargeFiveRechargeRateList[i] = rechargeFiveList[i].rechargeRate;
                    rechargeSixRechargeRateList[i] = rechargeSixList[i].rechargeRate;

                    newUserRechargeUserCountList[i] = newUserList[i].rechargeUserCount;
                    rechargeOneRechargeUserCountList[i] = rechargeOneList[i].rechargeUserCount;
                    rechargeTwoRechargeUserCountList[i] = rechargeTwoList[i].rechargeUserCount;
                    rechargeThreeRechargeUserCountList[i] = rechargeThreeList[i].rechargeUserCount;
                    rechargeFourRechargeUserCountList[i] = rechargeFourList[i].rechargeUserCount;
                    rechargeFiveRechargeUserCountList[i] = rechargeFiveList[i].rechargeUserCount;
                    rechargeSixRechargeUserCountList[i] = rechargeSixList[i].rechargeUserCount;

                    newUserDauRechargeRateList[i] = newUserList[i].dauRechargeRate;
                    rechargeOneDauRechargeRateList[i] = rechargeOneList[i].dauRechargeRate;
                    rechargeTwoDauRechargeRateList[i] = rechargeTwoList[i].dauRechargeRate;
                    rechargeThreeDauRechargeRateList[i] = rechargeThreeList[i].dauRechargeRate;
                    rechargeFourDauRechargeRateList[i] = rechargeFourList[i].dauRechargeRate;
                    rechargeFiveDauRechargeRateList[i] = rechargeFiveList[i].dauRechargeRate;
                    rechargeSixDauRechargeRateList[i] = rechargeSixList[i].dauRechargeRate;

                    newUserBettingRechargeRateList[i] = newUserList[i].bettingRechargeRate;
                    rechargeOneBettingRechargeRateList[i] = rechargeOneList[i].bettingRechargeRate;
                    rechargeTwoBettingRechargeRateList[i] = rechargeTwoList[i].bettingRechargeRate;
                    rechargeThreeBettingRechargeRateList[i] = rechargeThreeList[i].bettingRechargeRate;
                    rechargeFourBettingRechargeRateList[i] = rechargeFourList[i].bettingRechargeRate;
                    rechargeFiveBettingRechargeRateList[i] = rechargeFiveList[i].bettingRechargeRate;
                    rechargeSixBettingRechargeRateList[i] = rechargeSixList[i].bettingRechargeRate;

                    newUserDarppuRateList[i] = newUserList[i].darppuRate;
                    rechargeOneDarppuRateList[i] = rechargeOneList[i].darppuRate;
                    rechargeTwoDarppuRateList[i] = rechargeTwoList[i].darppuRate;
                    rechargeThreeDarppuRateList[i] = rechargeThreeList[i].darppuRate;
                    rechargeFourDarppuRateList[i] = rechargeFourList[i].darppuRate;
                    rechargeFiveDarppuRateList[i] = rechargeFiveList[i].darppuRate;
                    rechargeSixDarppuRateList[i] = rechargeSixList[i].darppuRate;

                    newUserPointPayRateList[i] = newUserList[i].pointPayRate;
                    rechargeOnePointPayRateList[i] = rechargeOneList[i].pointPayRate;
                    rechargeTwoPointPayRateList[i] = rechargeTwoList[i].pointPayRate;
                    rechargeThreePointPayRateList[i] = rechargeThreeList[i].pointPayRate;
                    rechargeFourPointPayRateList[i] = rechargeFourList[i].pointPayRate;
                    rechargeFivePointPayRateList[i] = rechargeFiveList[i].pointPayRate;
                    rechargeSixPointPayRateList[i] = rechargeSixList[i].pointPayRate;

                    newUserPaySuccessRateList[i] = newUserList[i].paySuccessRate;
                    rechargeOnePaySuccessRateList[i] = rechargeOneList[i].paySuccessRate;
                    rechargeTwoPaySuccessRateList[i] = rechargeTwoList[i].paySuccessRate;
                    rechargeThreePaySuccessRateList[i] = rechargeThreeList[i].paySuccessRate;
                    rechargeFourPaySuccessRateList[i] = rechargeFourList[i].paySuccessRate;
                    rechargeFivePaySuccessRateList[i] = rechargeFiveList[i].paySuccessRate;
                    rechargeSixPaySuccessRateList[i] = rechargeSixList[i].paySuccessRate;

                    newUserAvgPayCountList[i] = newUserList[i].averagePayCount;
                    rechargeOneAvgPayCountList[i] = rechargeOneList[i].averagePayCount;
                    rechargeTwoAvgPayCountList[i] = rechargeTwoList[i].averagePayCount;
                    rechargeThreeAvgPayCountList[i] = rechargeThreeList[i].averagePayCount;
                    rechargeFourAvgPayCountList[i] = rechargeFourList[i].averagePayCount;
                    rechargeFiveAvgPayCountList[i] = rechargeFiveList[i].averagePayCount;
                    rechargeSixAvgPayCountList[i] = rechargeSixList[i].averagePayCount;

                    newUserAvgPayAmountList[i] = newUserList[i].averagePayAmount;
                    rechargeOneAvgPayAmountList[i] = rechargeOneList[i].averagePayAmount;
                    rechargeTwoAvgPayAmountList[i] = rechargeTwoList[i].averagePayAmount;
                    rechargeThreeAvgPayAmountList[i] = rechargeThreeList[i].averagePayAmount;
                    rechargeFourAvgPayAmountList[i] = rechargeFourList[i].averagePayAmount;
                    rechargeFiveAvgPayAmountList[i] = rechargeFiveList[i].averagePayAmount;
                    rechargeSixAvgPayAmountList[i] = rechargeSixList[i].averagePayAmount;
                }

                var thirdAmountOption = rechargeTemplate("充值金额",dateList,legArr);
                thirdAmountOption.series.push(areaStyleSeries(legArr[0],newUserThirdAmountList));
                thirdAmountOption.series.push(areaStyleSeries(legArr[1],rechargeOneThirdAmountList));
                thirdAmountOption.series.push(areaStyleSeries(legArr[2],rechargeTwoThirdAmountList));
                thirdAmountOption.series.push(areaStyleSeries(legArr[3],rechargeThreeThirdAmountList));
                thirdAmountOption.series.push(areaStyleSeries(legArr[4],rechargeFourThirdAmountList));
                thirdAmountOption.series.push(areaStyleSeries(legArr[5],rechargeFiveThirdAmountList));
                thirdAmountOption.series.push(areaStyleSeries(legArr[6],rechargeSixThirdAmountList));

                var rechargeRateOption = barTemplate("充值金额占比",dateList,legArr);
                rechargeRateOption.series.push(stackBarStyleSeries(legArr[0],newUserRechargeRateList));
                rechargeRateOption.series.push(stackBarStyleSeries(legArr[1],rechargeOneRechargeRateList));
                rechargeRateOption.series.push(stackBarStyleSeries(legArr[2],rechargeTwoRechargeRateList));
                rechargeRateOption.series.push(stackBarStyleSeries(legArr[3],rechargeThreeRechargeRateList));
                rechargeRateOption.series.push(stackBarStyleSeries(legArr[4],rechargeFourRechargeRateList));
                rechargeRateOption.series.push(stackBarStyleSeries(legArr[5],rechargeFiveRechargeRateList));
                rechargeRateOption.series.push(stackBarStyleSeries(legArr[6],rechargeSixRechargeRateList));

                var rechargeUserCountOption = template("充值人数",dateList,legArr);
                rechargeUserCountOption.series.push(areaStyleSeries(legArr[0],newUserRechargeUserCountList));
                rechargeUserCountOption.series.push(areaStyleSeries(legArr[1],rechargeOneRechargeUserCountList));
                rechargeUserCountOption.series.push(areaStyleSeries(legArr[2],rechargeTwoRechargeUserCountList));
                rechargeUserCountOption.series.push(areaStyleSeries(legArr[3],rechargeThreeRechargeUserCountList));
                rechargeUserCountOption.series.push(areaStyleSeries(legArr[4],rechargeFourRechargeUserCountList));
                rechargeUserCountOption.series.push(areaStyleSeries(legArr[5],rechargeFiveRechargeUserCountList));
                rechargeUserCountOption.series.push(areaStyleSeries(legArr[6],rechargeSixRechargeUserCountList));

                var dauRechargeRateOption = rateTemplate("DAU付费率",dateList,legArr);
                dauRechargeRateOption.series.push(lineStyleSeries(legArr[0],newUserDauRechargeRateList));
                dauRechargeRateOption.series.push(lineStyleSeries(legArr[1],rechargeOneDauRechargeRateList));
                dauRechargeRateOption.series.push(lineStyleSeries(legArr[2],rechargeTwoDauRechargeRateList));
                dauRechargeRateOption.series.push(lineStyleSeries(legArr[3],rechargeThreeDauRechargeRateList));
                dauRechargeRateOption.series.push(lineStyleSeries(legArr[4],rechargeFourDauRechargeRateList));
                dauRechargeRateOption.series.push(lineStyleSeries(legArr[5],rechargeFiveDauRechargeRateList));
                dauRechargeRateOption.series.push(lineStyleSeries(legArr[6],rechargeSixDauRechargeRateList));

                var bettingRechargeRateOption = rateTemplate("投注付费率",dateList,legArr);
                bettingRechargeRateOption.series.push(lineStyleSeries(legArr[0],newUserBettingRechargeRateList));
                bettingRechargeRateOption.series.push(lineStyleSeries(legArr[1],rechargeOneBettingRechargeRateList));
                bettingRechargeRateOption.series.push(lineStyleSeries(legArr[2],rechargeTwoBettingRechargeRateList));
                bettingRechargeRateOption.series.push(lineStyleSeries(legArr[3],rechargeThreeBettingRechargeRateList));
                bettingRechargeRateOption.series.push(lineStyleSeries(legArr[4],rechargeFourBettingRechargeRateList));
                bettingRechargeRateOption.series.push(lineStyleSeries(legArr[5],rechargeFiveBettingRechargeRateList));
                bettingRechargeRateOption.series.push(lineStyleSeries(legArr[6],rechargeSixBettingRechargeRateList));

                var darppuRateOption = template("DARPPU",dateList,legArr);
                darppuRateOption.series.push(lineStyleSeries(legArr[0],newUserDarppuRateList));
                darppuRateOption.series.push(lineStyleSeries(legArr[1],rechargeOneDarppuRateList));
                darppuRateOption.series.push(lineStyleSeries(legArr[2],rechargeTwoDarppuRateList));
                darppuRateOption.series.push(lineStyleSeries(legArr[3],rechargeThreeDarppuRateList));
                darppuRateOption.series.push(lineStyleSeries(legArr[4],rechargeFourDarppuRateList));
                darppuRateOption.series.push(lineStyleSeries(legArr[5],rechargeFiveDarppuRateList));
                darppuRateOption.series.push(lineStyleSeries(legArr[6],rechargeSixDarppuRateList));

                var pointPayRateOption = rateTemplate("点击支持率",dateList,legArr);
                pointPayRateOption.series.push(lineStyleSeries(legArr[0],newUserPointPayRateList));
                pointPayRateOption.series.push(lineStyleSeries(legArr[1],rechargeOnePointPayRateList));
                pointPayRateOption.series.push(lineStyleSeries(legArr[2],rechargeTwoPointPayRateList));
                pointPayRateOption.series.push(lineStyleSeries(legArr[3],rechargeThreePointPayRateList));
                pointPayRateOption.series.push(lineStyleSeries(legArr[4],rechargeFourPointPayRateList));
                pointPayRateOption.series.push(lineStyleSeries(legArr[5],rechargeFivePointPayRateList));
                pointPayRateOption.series.push(lineStyleSeries(legArr[6],rechargeSixPointPayRateList));

                var paySuccessRateOption = rateTemplate("支付成功率",dateList,legArr);
                paySuccessRateOption.series.push(lineStyleSeries(legArr[0],newUserPaySuccessRateList));
                paySuccessRateOption.series.push(lineStyleSeries(legArr[1],rechargeOnePaySuccessRateList));
                paySuccessRateOption.series.push(lineStyleSeries(legArr[2],rechargeTwoPaySuccessRateList));
                paySuccessRateOption.series.push(lineStyleSeries(legArr[3],rechargeThreePaySuccessRateList));
                paySuccessRateOption.series.push(lineStyleSeries(legArr[4],rechargeFourPaySuccessRateList));
                paySuccessRateOption.series.push(lineStyleSeries(legArr[5],rechargeFivePaySuccessRateList));
                paySuccessRateOption.series.push(lineStyleSeries(legArr[6],rechargeSixPaySuccessRateList));

                var avgPayCountOption = template("人均充值次数",dateList,legArr);
                avgPayCountOption.series.push(lineStyleSeries(legArr[0],newUserAvgPayCountList));
                avgPayCountOption.series.push(lineStyleSeries(legArr[1],rechargeOneAvgPayCountList));
                avgPayCountOption.series.push(lineStyleSeries(legArr[2],rechargeTwoAvgPayCountList));
                avgPayCountOption.series.push(lineStyleSeries(legArr[3],rechargeThreeAvgPayCountList));
                avgPayCountOption.series.push(lineStyleSeries(legArr[4],rechargeFourAvgPayCountList));
                avgPayCountOption.series.push(lineStyleSeries(legArr[5],rechargeFiveAvgPayCountList));
                avgPayCountOption.series.push(lineStyleSeries(legArr[6],rechargeSixAvgPayCountList));

                var avgPayAmountOption = template("人均单次充值金额",dateList,legArr);
                avgPayAmountOption.series.push(lineStyleSeries(legArr[0],newUserAvgPayAmountList));
                avgPayAmountOption.series.push(lineStyleSeries(legArr[1],rechargeOneAvgPayAmountList));
                avgPayAmountOption.series.push(lineStyleSeries(legArr[2],rechargeTwoAvgPayAmountList));
                avgPayAmountOption.series.push(lineStyleSeries(legArr[3],rechargeThreeAvgPayAmountList));
                avgPayAmountOption.series.push(lineStyleSeries(legArr[4],rechargeFourAvgPayAmountList));
                avgPayAmountOption.series.push(lineStyleSeries(legArr[5],rechargeFiveAvgPayAmountList));
                avgPayAmountOption.series.push(lineStyleSeries(legArr[6],rechargeSixAvgPayAmountList));

                //付费数据
                var payOption = [thirdAmountOption,rechargeRateOption,rechargeUserCountOption,
                    dauRechargeRateOption,bettingRechargeRateOption,darppuRateOption,
                    pointPayRateOption,paySuccessRateOption,
                    avgPayCountOption,avgPayAmountOption];
                for (var l = 0; l < payOption.length; l++) {
                    me.echarts = echarts.init(Ext.get("userClassifyPay"+ l).dom);
                    me.echarts.setOption(payOption[l]);
                }
            }

            if (tabId == 'classifyRemain') {
                //25、次日留存
                var newUserDayRetentionList = [];
                var rechargeOneDayRetentionList = [];
                var rechargeTwoDayRetentionList = [];
                var rechargeThreeDayRetentionList = [];
                var rechargeFourDayRetentionList = [];
                var rechargeFiveDayRetentionList = [];
                var rechargeSixDayRetentionList = [];

                //26、七日留存
                var newUserWeekRetentionList = [];
                var rechargeOneWeekRetentionList = [];
                var rechargeTwoWeekRetentionList = [];
                var rechargeThreeWeekRetentionList = [];
                var rechargeFourWeekRetentionList = [];
                var rechargeFiveWeekRetentionList = [];
                var rechargeSixWeekRetentionList = [];

                for (var i=0;i<size;i++) {
                    newUserDayRetentionList[i] = newUserList[i].dayRetention;
                    rechargeOneDayRetentionList[i] = rechargeOneList[i].dayRetention;
                    rechargeTwoDayRetentionList[i] = rechargeTwoList[i].dayRetention;
                    rechargeThreeDayRetentionList[i] = rechargeThreeList[i].dayRetention;
                    rechargeFourDayRetentionList[i] = rechargeFourList[i].dayRetention;
                    rechargeFiveDayRetentionList[i] = rechargeFiveList[i].dayRetention;
                    rechargeSixDayRetentionList[i] = rechargeSixList[i].dayRetention;

                    newUserWeekRetentionList[i] = newUserList[i].weekRetention;
                    rechargeOneWeekRetentionList[i] = rechargeOneList[i].weekRetention;
                    rechargeTwoWeekRetentionList[i] = rechargeTwoList[i].weekRetention;
                    rechargeThreeWeekRetentionList[i] = rechargeThreeList[i].weekRetention;
                    rechargeFourWeekRetentionList[i] = rechargeFourList[i].weekRetention;
                    rechargeFiveWeekRetentionList[i] = rechargeFiveList[i].weekRetention;
                    rechargeSixWeekRetentionList[i] = rechargeSixList[i].weekRetention;
                }

                var dayRetentionOption = rateTemplate("次日留存",dayRetentionDateList,legArr);
                dayRetentionOption.series.push(lineStyleSeries(legArr[0],newUserDayRetentionList));
                dayRetentionOption.series.push(lineStyleSeries(legArr[1],rechargeOneDayRetentionList));
                dayRetentionOption.series.push(lineStyleSeries(legArr[2],rechargeTwoDayRetentionList));
                dayRetentionOption.series.push(lineStyleSeries(legArr[3],rechargeThreeDayRetentionList));
                dayRetentionOption.series.push(lineStyleSeries(legArr[4],rechargeFourDayRetentionList));
                dayRetentionOption.series.push(lineStyleSeries(legArr[5],rechargeFiveDayRetentionList));
                dayRetentionOption.series.push(lineStyleSeries(legArr[6],rechargeSixDayRetentionList));

                var weekRetentionOption = rateTemplate("7日留存",weekRetentionDateList,legArr);
                weekRetentionOption.series.push(lineStyleSeries(legArr[0],newUserWeekRetentionList));
                weekRetentionOption.series.push(lineStyleSeries(legArr[1],rechargeOneWeekRetentionList));
                weekRetentionOption.series.push(lineStyleSeries(legArr[2],rechargeTwoWeekRetentionList));
                weekRetentionOption.series.push(lineStyleSeries(legArr[3],rechargeThreeWeekRetentionList));
                weekRetentionOption.series.push(lineStyleSeries(legArr[4],rechargeFourWeekRetentionList));
                weekRetentionOption.series.push(lineStyleSeries(legArr[5],rechargeFiveWeekRetentionList));
                weekRetentionOption.series.push(lineStyleSeries(legArr[6],rechargeSixWeekRetentionList));
                //留存数据
                var remainOption = [dayRetentionOption,weekRetentionOption];
                for (var m = 0; m < remainOption.length; m++) {
                    me.echarts = echarts.init(Ext.get("userClassifyRemain"+ m).dom);
                    me.echarts.setOption(remainOption[m]);
                }
            }

            if (tabId == 'classifyLost') {
                //27、七日流失率
                var newUserWeekLostRateList = [];
                var rechargeOneWeekLostRateList = [];
                var rechargeTwoWeekLostRateList = [];
                var rechargeThreeWeekLostRateList = [];
                var rechargeFourWeekLostRateList = [];
                var rechargeFiveWeekLostRateList = [];
                var rechargeSixWeekLostRateList = [];

                for (var i=0;i<size;i++) {
                    newUserWeekLostRateList[i] = newUserList[i].weekLostRate;
                    rechargeOneWeekLostRateList[i] = rechargeOneList[i].weekLostRate;
                    rechargeTwoWeekLostRateList[i] = rechargeTwoList[i].weekLostRate;
                    rechargeThreeWeekLostRateList[i] = rechargeThreeList[i].weekLostRate;
                    rechargeFourWeekLostRateList[i] = rechargeFourList[i].weekLostRate;
                    rechargeFiveWeekLostRateList[i] = rechargeFiveList[i].weekLostRate;
                    rechargeSixWeekLostRateList[i] = rechargeSixList[i].weekLostRate;
                }

                var weekLostRateOption = rateTemplate("7日流失率",weekLostDateList,legArr);
                weekLostRateOption.series.push(lineStyleSeries(legArr[0],newUserWeekLostRateList));
                weekLostRateOption.series.push(lineStyleSeries(legArr[1],rechargeOneWeekLostRateList));
                weekLostRateOption.series.push(lineStyleSeries(legArr[2],rechargeTwoWeekLostRateList));
                weekLostRateOption.series.push(lineStyleSeries(legArr[3],rechargeThreeWeekLostRateList));
                weekLostRateOption.series.push(lineStyleSeries(legArr[4],rechargeFourWeekLostRateList));
                weekLostRateOption.series.push(lineStyleSeries(legArr[5],rechargeFiveWeekLostRateList));
                weekLostRateOption.series.push(lineStyleSeries(legArr[6],rechargeSixWeekLostRateList));

                //流失数据
                var lostOption = [weekLostRateOption];
                for (var n = 0; n < lostOption.length; n++) {
                    me.echarts = echarts.init(Ext.get("userClassifyLost"+ n).dom);
                    me.echarts.setOption(lostOption[n]);
                }
            }

        }

        function template(title,dateList,legArr){
            var option =
                {
                    title: {text:title,left:'center',
                        textStyle:{//标题内容的样式
                            fontStyle:'normal',//主标题文字字体风格，默认normal，有italic(斜体),oblique(斜体)
                            fontWeight:"bold",//可选normal(正常)，bold(加粗)，bolder(加粗)，lighter(变细)，100|200|300|400|500...
                            fontFamily:"san-serif",//主题文字字体，默认微软雅黑
                            fontSize:22//主题文字字体大小，默认为18px
                        }},
                    tooltip: {trigger: 'axis',
                        formatter: function (params) {
                            var str='';
                            for(var i = 0; i < params.length; i++){
                                str += '日期:'+params[i].name +"\t"+ params[i].seriesName +':' + params[i].value+"<br/>";
                            }
                            return str;
                        }
                    },
                    legend: {
                        data:legArr,
                        top:'10%'
                    },
                    toolbox: {
                        show : true,
                        feature : {
                            mark : {show: true}
                        }
                    },
                    calculable : true,
                    grid:{
                        left:'11%',
                        y:'25%'
                    },
                    xAxis: {
                        type : 'category',
                        boundaryGap : false,
                        data: dateList
                    },
                    yAxis: {
                        type : 'value',
                        axisLabel : {
                            formatter: '{value}'
                        }},
                    series: []
                };
            return option;
        }

        function rechargeTemplate(title,dateList,legArr){
            var option =
                {
                    title: {text:title,left:'center',
                        textStyle:{//标题内容的样式
                            fontStyle:'normal',//主标题文字字体风格，默认normal，有italic(斜体),oblique(斜体)
                            fontWeight:"bold",//可选normal(正常)，bold(加粗)，bolder(加粗)，lighter(变细)，100|200|300|400|500...
                            fontFamily:"san-serif",//主题文字字体，默认微软雅黑
                            fontSize:22//主题文字字体大小，默认为18px
                        }},
                    tooltip: {trigger: 'axis',
                        formatter: function (params) {
                            var str='';
                            for(var i = 0; i < params.length; i++){
                                str += '日期:'+params[i].name +"\t"+ params[i].seriesName +':' + format(params[i].value)+"<br/>";
                            }
                            return str;
                        }
                    },
                    legend: {
                        data:legArr,
                        top:'10%'
                    },
                    toolbox: {
                        show : true,
                        feature : {
                            mark : {show: true}
                        }
                    },
                    calculable : true,
                    grid:{
                        left:'11%',
                        y:'25%'
                    },
                    xAxis: {
                        type : 'category',
                        boundaryGap : false,
                        data: dateList
                    },
                    yAxis: {
                        type : 'value',
                        axisLabel : {
                            formatter: '{value}'
                        }},
                    series: []
                };
            return option;
        }

        function rateTemplate(title,dateList,legArr){
            var option =
                {
                    title: {text:title,left:'center',
                        textStyle:{//标题内容的样式
                            fontStyle:'normal',//主标题文字字体风格，默认normal，有italic(斜体),oblique(斜体)
                            fontWeight:"bold",//可选normal(正常)，bold(加粗)，bolder(加粗)，lighter(变细)，100|200|300|400|500...
                            fontFamily:"san-serif",//主题文字字体，默认微软雅黑
                            fontSize:22//主题文字字体大小，默认为18px
                        }},
                    tooltip: {trigger: 'axis',
                        formatter: function (params) {
                            var str='';
                            for(var i = 0; i < params.length; i++){
                                str += '日期:'+params[i].name +"\t"+ params[i].seriesName +':' + params[i].value+"%<br/>";
                            }
                            return str;
                        }
                    },
                    legend: {
                        data:legArr,
                        top:'10%'
                    },
                    toolbox: {
                        show : true,
                        feature : {
                            mark : {show: true}
                        }
                    },
                    calculable : true,
                    grid:{
                        left:'11%',
                        y:'25%'
                    },
                    xAxis: {
                        type : 'category',
                        boundaryGap : false,
                        data: dateList
                    },
                    yAxis: {
                        type : 'value',
                        axisLabel : {
                            formatter: '{value}%'
                        }},
                    series: []
                };
            return option;
        }

        function barTemplate(title,dateList,legArr){
            var option =
                {
                    title: {text:title,left:'center',
                        textStyle:{//标题内容的样式
                            fontStyle:'normal',//主标题文字字体风格，默认normal，有italic(斜体),oblique(斜体)
                            fontWeight:"bold",//可选normal(正常)，bold(加粗)，bolder(加粗)，lighter(变细)，100|200|300|400|500...
                            fontFamily:"san-serif",//主题文字字体，默认微软雅黑
                            fontSize:22//主题文字字体大小，默认为18px
                        }},
                    tooltip: {trigger: 'axis',
                        formatter: function (params) {
                            var str='';
                            for(var i = 0; i < params.length; i++){
                                str += '日期:'+params[i].name +"\t"+ params[i].seriesName +':' + params[i].value+"%<br/>";
                            }
                            return str;
                        }
                    },
                    legend: {
                        data:legArr,
                        top:'10%'
                    },
                    toolbox: {
                        show : true,
                        feature : {
                            mark : {show: true}
                        }
                    },
                    calculable : true,
                    grid:{
                        left:'11%',
                        y:'25%'
                    },
                    xAxis: {
                        type : 'category',
                        // boundaryGap : false,
                        data: dateList
                    },
                    yAxis: {
                        type : 'value',
                        axisLabel : {
                            formatter: '{value}%'
                        }},
                    series: []
                };
            return option;
        }

        function moneyTemplate(title,dateList,legArr){
            var option =
                {
                    title: {text:title,left:'center',
                        textStyle:{//标题内容的样式
                            fontStyle:'normal',//主标题文字字体风格，默认normal，有italic(斜体),oblique(斜体)
                            fontWeight:"bold",//可选normal(正常)，bold(加粗)，bolder(加粗)，lighter(变细)，100|200|300|400|500...
                            fontFamily:"san-serif",//主题文字字体，默认微软雅黑
                            fontSize:22//主题文字字体大小，默认为18px
                        }},
                    tooltip: {trigger: 'axis',
                        formatter: function (params) {
                            var str='';
                            for(var i = 0; i < params.length; i++){
                                str += '日期:'+params[i].name +"\t"+ params[i].seriesName +':' + format(params[i].value)+"<br/>";
                            }
                            return str;
                        }
                    },
                    legend: {
                        data:legArr,
                        top:'10%'
                    },
                    toolbox: {
                        show : true,
                        feature : {
                            mark : {show: true}
                        }
                    },
                    calculable : true,
                    grid:{
                        left:'16%',
                        y:'25%'
                    },
                    xAxis: {
                        type : 'category',
                        boundaryGap : false,
                        data: dateList
                    },
                    yAxis: {
                        type : 'value',
                        axisLabel : {
                            formatter: '{value}'
                        }},
                    series: []
                };
            return option;
        }

        function preciseMoneyTemplate(title,dateList,legArr){
            var option =
                {
                    title: {text:title,left:'center',
                        textStyle:{//标题内容的样式
                            fontStyle:'normal',//主标题文字字体风格，默认normal，有italic(斜体),oblique(斜体)
                            fontWeight:"bold",//可选normal(正常)，bold(加粗)，bolder(加粗)，lighter(变细)，100|200|300|400|500...
                            fontFamily:"san-serif",//主题文字字体，默认微软雅黑
                            fontSize:22//主题文字字体大小，默认为18px
                        }},
                    tooltip: {trigger: 'axis',
                        formatter: function (params) {
                            var str='';
                            for(var i = 0; i < params.length; i++){
                                str += '日期:'+params[i].name +"\t"+ params[i].seriesName +':' + formatDecimal(params[i].value)+"<br/>";
                            }
                            return str;
                        }
                    },
                    legend: {
                        data:legArr,
                        top:'10%'
                    },
                    toolbox: {
                        show : true,
                        feature : {
                            mark : {show: true}
                        }
                    },
                    calculable : true,
                    grid:{
                        left:'16%',
                        y:'25%'
                    },
                    xAxis: {
                        type : 'category',
                        boundaryGap : false,
                        data: dateList
                    },
                    yAxis: {
                        type : 'value',
                        axisLabel : {
                            formatter: '{value}'
                        }},
                    series: []
                };
            return option;
        }

        function returnTemplate(title,dateList,legArr){
            var option =
                {
                    title: {text:title,left:'center',
                        textStyle:{//标题内容的样式
                            fontStyle:'normal',//主标题文字字体风格，默认normal，有italic(斜体),oblique(斜体)
                            fontWeight:"bold",//可选normal(正常)，bold(加粗)，bolder(加粗)，lighter(变细)，100|200|300|400|500...
                            fontFamily:"san-serif",//主题文字字体，默认微软雅黑
                            fontSize:22//主题文字字体大小，默认为18px
                        }},
                    tooltip: {trigger: 'axis',
                        formatter: function (params) {
                            var str='';
                            for(var i = 0; i < params.length; i++){
                                str += '日期:'+params[i].name +"\t"+ params[i].seriesName +':' + params[i].value+"%<br/>";
                            }
                            return str;
                        }
                    },
                    legend: {
                        data:legArr,
                        top:'10%'
                    },
                    toolbox: {
                        show : true,
                        feature : {
                            mark : {show: true}
                        }
                    },
                    calculable : true,
                    grid:{
                        left:'16%',
                        y:'25%'
                    },
                    xAxis: {
                        type : 'category',
                        boundaryGap : false,
                        data: dateList
                    },
                    yAxis: {
                        type : 'value',
                        min : 70,
                        axisLabel : {
                            formatter: '{value}%'
                        }},
                    series: []
                };
            return option;
        }

        function areaStyleSeries(title,dataList){
            var series =  {
                name: title,
                type: 'line',
                smooth:true,
                data: dataList,
                itemStyle: {normal: {areaStyle: {type: 'default'}}},
                stack:'总量'
            };
            return series;
        }

        function stackBarStyleSeries(title,dataList){
            var series = {
                name: title,
                type: 'bar',
                smooth:true,
                data: dataList,
                stack:'占比'
            };
            return series;
        }

        function lineStyleSeries(title,dataList) {
            var series = {
                name: title,
                type: 'line',
                smooth:true,
                data: dataList
            };
            return series;
        }

        function format(value){
            return Ext.util.Format.number(value,"0,000");
        }

        function formatDecimal(value){
            return Ext.util.Format.number(value,"0,000.0");
        }
    }
});

