Ext.define('WF.view.data.board.gameDataViewMain', {
    extend: 'Ext.panel.Panel',
    title: '游戏数据总览',
    xtype: 'gameDataViewMain',
    closable: true,
    autoScroll: true,
    layout: {
        type: 'vbox',
        align: 'stretch'
    },
    initComponent: function () {
        this.callParent(arguments);
        var me = this;
        var store = Ext.create('DCIS.Store', {
            url: 'data/game/data/getList.do',
            autoload: false,
            fields: ["titles", "dateList", "chartsData"]
        });
        var parentChannelStore = Ext.create('DCIS.Store', {
            url: 'data/admin/common/data/getParentChannels.do',
            autoLoad: true,
            fields: ['id', 'name']
        });
        var gameTypeStore = Ext.create('DCIS.Store', {
            autoLoad: true,
            url: 'data/admin/dict/getListByType.do',
            fields: ['value', 'label'],
            baseParams: {
                type: 'game_type'
            }
        });
        // 查询模块
        me.add({
            border: false,
            store: store,
            xtype: 'searchpanel',
            title: '查询',
            collapsible: true,
            collapsed: false,
            columns: 2,
            buildField: "Manual",
            forceFit: false,
            todoReload: function () {
                doSearch(me.down(("[name='myTabs']")).activeTab.items.items)
            },
            export: function () {
                window.location.href = 'data/game/data/export.do?parentIds=' + me.down("[name='parentId']").getValue() +
                    '&gameTypes=' + me.down("[name='gameType']").getValue()+
                    '&startTime=' + Ext.util.Format.date(me.down("[name='startTime']").value, "Y-m-d")+
                    '&endTime=' + Ext.util.Format.date(me.down("[name='endTime']").value, "Y-m-d");
            },
            items: [{
                name: 'parentId',
                fieldLabel: '主渠道',
                xtype: 'combo',
                emptyText: "",
                displayField: 'name',
                valueField: "id",
                editable: false,
                multiSelect: true,
                queryMode: "local",
                store: parentChannelStore
            },
            {
                name: 'gameType',
                fieldLabel: '游戏',
                xtype: 'combobox',
                store: gameTypeStore,
                emptyText: "",
                displayField: 'label',
                multiSelect: true,
                valueField: "value",
                editable: false
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
                value: Ext.util.Format.date(Ext.Date.add(new Date(), Ext.Date.DAY, -14), "Y-m-d")
            }, {
                name: 'endTime',
                fieldLabel: '结束时间',
                xtype: 'datefield',
                format: 'Y-m-d',
                value: Ext.util.Format.date(Ext.Date.add(new Date(), Ext.Date.DAY, -1), "Y-m-d")
            }]
        });
        // 选项卡模块
        me.add({
            name: 'myTabs',
            xtype: 'tabpanel',
            layout: 'column',
            align: 'stretch',
            bodyStyle: 'border-width:0',
            items: [{
                title: '全量用户',
                itemId: 'allUsers',
                width: "10.5%",
                height: "100%",
                xtype: "panel",
                forceFit: true,
                bodyStyle: 'border-color:black',
                layout: 'column',
                items: getHtml(["DAU", "投注人数", "投注转化率", "投注流水", "流水差", "返奖率", "投注笔数", "投注ARPU", "投注ASP"]),
                listeners: {
                    'activate': function (tab) {
                        me.down(("[name='tabId']")).setValue(tab.itemId);
                        doSearch(tab.items.items);
                    }
                }
            }, {
                title: '新增用户',
                itemId: 'newUsers',
                width: "10.5%",
                height: "100%",
                xtype: "panel",
                forceFit: true,
                bodyStyle: 'border-color:black',
                layout: 'column',
                items: getHtml(["新增用户数","新增投注用户数","新增投注转化率","新增投注流水","新增流水差","新增返奖率","新增投注笔数","新增投注ARPU","新增投注ASP"]),
                listeners: {
                    'activate': function (tab) {
                        me.down(("[name='tabId']")).setValue(tab.itemId);
                        doSearch(tab.items.items);
                    }
                }
            }, {
                title: '留存数据',
                itemId: 'retention',
                width: "10.5%",
                height: "100%",
                xtype: "panel",
                forceFit: true,
                bodyStyle: 'border-color:black',
                layout: 'column',
                items: getHtml(["新增次留","新增三留","新增七留","全量次留","全量三留","全量七留"]),
                listeners: {
                    'activate': function (tab) {
                        me.down(("[name='tabId']")).setValue(tab.itemId);
                        doSearch(tab.items.items);
                    }
                }
            }, {
                title: '其它数据',
                itemId: 'other',
                width: "10.5%",
                height: "100%",
                xtype: "panel",
                forceFit: true,
                bodyStyle: 'border-color:black',
                layout: 'column',
                items: getHtml(["导入率","累计用户数"]),
                listeners: {
                    'activate': function (tab) {
                        me.down(("[name='tabId']")).setValue(tab.itemId);
                        doSearch(tab.items.items);
                    }
                }
            }]
        });

        function getHtml(names) {
            var items = [];
            for (var i = 0; i < names.length; i++) {
                items.push({
                    xtype: "panel",
                    layout: 'hbox',
                    name: names[i],
                    id: names[i],
                    width: Ext.ComponentQuery.query('maincontent_center')[0].body.dom.clientWidth * 0.5 - 10,
                    height: 430,
                    forceFit: true,
                    bodyStyle: 'border-width:0'
                });
            }
            return items;
        }

        function doSearch(items) {
            var value = {
                parentId: me.down("[name='parentId']").value,
                gameType: me.down("[name='gameType']").value,
                tabId: me.down("[name='tabId']").value,
                startTime: Ext.util.Format.date(me.down("[name='startTime']").value, "Y-m-d"),
                endTime: Ext.util.Format.date(me.down("[name='endTime']").value, "Y-m-d")
            };
            if (value.tabId==='') {
                value.tabId = me.down("[name='myTabs']").activeTab.itemId;
            }
            store.load({
                params: value,
                callback: function () {
                    toDoCreateCharts(items, store.getProxy().getReader().jsonData);
                }
            });
        }

        // 比率
        var _RATE_ARRAY = ["投注转化率", "返奖率", "新增返奖率","新增投注转化率","新增次留","新增三留","新增七留","全量次留","全量三留","全量七留","导入率"];
        function toDoCreateCharts(items, data) {
            for (var i = 0; i < items.length; i++) {
                me.echarts = echarts.init(Ext.get(items[i].id).dom);
                var myOption = option;

                if(Ext.Array.contains(_RATE_ARRAY,items[i].id)){
                    myOption =optionRate;
                }
                myOption.legend.data = data.chartsData.legends;
                myOption.title.text = data.titles[i];
                var mdDates = new Array();
                var mdDate;
                for (var z = 0; z < data.dateList.length; z++) {
                    mdDate = data.dateList[z];
                    if (items[i].id.indexOf("三留") > -1
                        && mdDate > Ext.util.Format.date(Ext.Date.add(new Date(), Ext.Date.DAY, -4), "Y-m-d")) {
                        break;
                    }
                    if (items[i].id.indexOf("七留") > -1
                        && mdDate > Ext.util.Format.date(Ext.Date.add(new Date(), Ext.Date.DAY, -8), "Y-m-d")) {
                        break;
                    }
                    // 截取yyyy-mm-dd格式日期的mm-dd部分
                    mdDates.push(mdDate.substring(5));
                }
                myOption.xAxis = [
                    {
                        type: 'category',
                        boundaryGap: false,
                        data: mdDates
                    }
                ];

                myOption.series = getSeries(data, items[i].id);

                me.echarts.setOption(myOption);
            }

        }

        function getSeries(data, item) {
            var series = [];
            var seriesData = [];
            // 浮点
            var _FLOAT_ARRAY = ["投注ARPU","投注ASP","新增投注ARPU","新增投注ASP"];
            seriesData = data.chartsData.series;
            for (var key in seriesData) {
                if (key == item) {

                    for (var i = 0; i < seriesData[key].length; i++) {
                        var aa =[];
                        for (var j = 0; j < seriesData[key][i].length; j++) {
                            if (item.indexOf("三留") > -1
                                && data.dateList[j] > Ext.util.Format.date(Ext.Date.add(new Date(), Ext.Date.DAY, -4), "Y-m-d")) {
                                break;
                            }
                            if (item.indexOf("七留") > -1
                                && data.dateList[j] > Ext.util.Format.date(Ext.Date.add(new Date(), Ext.Date.DAY, -8), "Y-m-d")) {
                                break;
                            }
                            var str = [];
                            str = seriesData[key][i][j][0].replace(";", "$").split("$");
                            aa.push({value : str[0], name : key + ":" +
                                // 比率加%
                                (Ext.Array.contains(_RATE_ARRAY, key) ? str[0] + "%" :
                                    // 浮点格式“0,000.00”
                                    (Ext.Array.contains(_FLOAT_ARRAY, key) ? Ext.util.Format.number(str[0], "0,000.00") :
                                        // 整数格式“0,000”
                                        Ext.util.Format.number(str[0], "0,000"))) + ";" + str[1]});
                        }
                        var temp = {
                            name: data.chartsData.legends[i],
                            type: 'line',
                            // data: seriesData[key][i]
                            data: aa
                        };
                        series.push(temp)
                    }
                }

            }

            return series;
        }
    }

});

var option = {
    title: {
        text: ''
    },
    tooltip: {
        trigger: 'axis',
        position: ['10%', '10%'],
        formatter: function (datas) {
            var str='';
            str +='时间 :'+datas[0].name+'<br/>';
            for(var i = 0; i < datas.length; i++){
                if(datas[i].data != undefined && datas[i].data.name != 0)
                    str += datas[i].seriesName +'<br/>'+datas[i].data.name+'<br/>';
            }
            return str;
        }
    },
    grid:{
        left:'20%',
        y:'25%'
    },
    legend: {
        top:20,
        data: []
    },
    toolbox: {
        show: true,
        feature: {
            mark: {show: true}
        }
    },
    calculable: true,
    xAxis: [
        {
            type: 'category',
            boundaryGap: false,
            data: []
        }
    ],
    yAxis: [
        {
            type: 'value'
        }
    ],
    series: []
};

// 比率图表
var optionRate = {
    title: {
        text: ''
    },
    tooltip: {
        trigger: 'axis',
        position: ['10%', '10%'],
        formatter: function (datas) {
            var str='';
            str +='时间 :'+datas[0].name+'<br/>';
            for (var i = 0; i < datas.length; i++) {
                if(datas[i].data != undefined && datas[i].data.name != 0)
                    str += datas[i].seriesName +'<br/>'+datas[i].data.name+'<br/>';
            }
            return str;
        }
    },
    grid:{
        left:'20%',
        y:'25%'
    },
    legend: {
        top:20,
        data: []
    },
    toolbox: {
        show: true,
        feature: {
            mark: {show: true}
        }
    },
    calculable: true,
    xAxis: [
        {
            type: 'category',
            boundaryGap: false,
            data: []
        }
    ],
    yAxis: [
        {
            type: 'value',
            axisLabel: {
                formatter: '{value} %'
            }
        }
    ],
    series: []
};