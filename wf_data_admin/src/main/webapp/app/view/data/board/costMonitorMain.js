Ext.define('WF.view.data.board.costMonitorMain', {
    extend: 'Ext.panel.Panel',
    title: '成本监控',
    xtype: 'costMonitorMain',
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
            url: 'data/game/cost/getList.do',
            autoload: false,
            fields: ["titles", "dateList", "chartsData"]
        });
        var parentChannelStore = Ext.create('DCIS.Store', {
            url: 'data/admin/common/data/getParentChannels.do',
            autoLoad: true,
            fields: ['id', 'name']
        });
        // 出口类型
        var activityTypeStore = Ext.create('DCIS.Store', {
            autoLoad: true,
            url: 'data/admin/dict/getListByType.do',
            fields: ['value', 'label'],
            baseParams: {
                type: 'activity_type'
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
            /*export: function () {
                window.location.href = 'data/game/data/export.do?parentIds=' + me.down("[name='parentId']").getValue() +
                    '&activityType=' + me.down("[name='activityType']").getValue()+
                    '&startTime=' + Ext.util.Format.date(me.down("[name='startTime']").value, "Y-m-d")+
                    '&endTime=' + Ext.util.Format.date(me.down("[name='endTime']").value, "Y-m-d");
            },*/
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
                name: 'activityType',
                fieldLabel: '出口类型',
                xtype: 'combo',
                emptyText: "--请选择--",
                displayField: 'label',
                valueField: "value",
                editable: true,
                multiSelect: true,
                queryMode: "local",
                store: activityTypeStore
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
                title: '渠道监控',
                itemId: 'channelMonitor',
                width: "10.5%",
                height: "100%",
                xtype: "panel",
                forceFit: true,
                bodyStyle: 'border-color:black',
                layout: 'column',
                items: getHtml(["成本", "成本占比", "出口人数", "出口率", "人均出口成本"]),
                listeners: {
                    'activate': function (tab) {
                        me.down(("[name='tabId']")).setValue(tab.itemId);
                        doSearch(tab.items.items);
                    }
                }
            }, {
                title: '出口监控',
                itemId: 'activityMonitor',
                width: "10.5%",
                height: "100%",
                xtype: "panel",
                forceFit: true,
                bodyStyle: 'border-color:black',
                layout: 'column',
                items: getHtml(["成本", "成本份额占比", "出口人数", "出口人数份额占比", "人均出口成本"]),
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
                    width: Ext.ComponentQuery.query('maincontent_center')[0].body.dom.clientWidth * 0.31,
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
                activityType: me.down("[name='activityType']").value,
                tabId: me.down("[name='tabId']").value,
                startTime: Ext.util.Format.date(me.down("[name='startTime']").value, "Y-m-d"),
                endTime: Ext.util.Format.date(me.down("[name='endTime']").value, "Y-m-d")
            };
            if (value.parentId.length > 1 && value.activityType.length > 1) {
                Ext.Msg.alert('提示', '[主渠道]和[出口类型]不能同时多选！');
                return ;
            }
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
        var _RATE_ARRAY = ["成本占比", "出口率", "成本份额占比","出口人数份额占比"];
        function toDoCreateCharts(items, data) {
            console.dir(data);
            for (var i = 0; i < items.length; i++) {
                me.echarts = echarts.init(Ext.get(items[i].id).dom);
                var myOption = option;

                if(Ext.Array.contains(_RATE_ARRAY,items[i].id)){
                    myOption =optionRate;
                }
                myOption.legend.data = data.chartsData.legends;
                myOption.title.text = data.titles[i];
                var mdDates = new Array();
                for (var z = 0; z < data.dateList.length; z++) {
                    mdDates.push(data.dateList[z].substring(5));
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
            // 浮点
            var _FLOAT_ARRAY = ["人均出口成本"];
            var _ONE_FLOAT_ARRAY = ["成本"];
            var seriesData = data.chartsData.series;
            for (var key in seriesData) {
                if (key == item) {
                    for (var i = 0; i < seriesData[key].length; i++) {
                        var aa =[];
                        for (var j = 0; j < seriesData[key][i].length; j++) {
                            var str = seriesData[key][i][j][0];
                            aa.push({value : str, name : key + ":" +
                                // 比率加%
                                (Ext.Array.contains(_RATE_ARRAY, key) ? str + "%" :
                                    // 浮点格式“0,000.00”
                                    (Ext.Array.contains(_FLOAT_ARRAY, key) ? Ext.util.Format.number(str, "0,000.00") :
                                        (Ext.Array.contains(_ONE_FLOAT_ARRAY, key) ? Ext.util.Format.number(str, "0,000.0") :
                                        // 整数格式“0,000”
                                        Ext.util.Format.number(str, "0,000"))))});
                        }
                        var temp = {
                            name: data.chartsData.legends[i],
                            type: 'line',
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
        top:25,
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
        top:25,
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