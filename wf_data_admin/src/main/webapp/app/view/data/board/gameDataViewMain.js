Ext.define('WF.view.data.board.gameDataViewMain', {
    extend: 'Ext.panel.Panel',
    title: '游戏数据图形分析',
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
        me.add({
            border: false,
            store: {},
            xtype: 'searchpanel',
            title: '查询',
            collapsible: true,
            collapsed: false,
            columns: 2,
            buildField: "Manual",
            forceFit: true,
            items: [{
                name: 'searchDate',
                fieldLabel: '日期',
                xtype: 'datefield',
                format: 'Y-m-d',
                value: Ext.util.Format.date(Ext.Date.add(new Date(), Ext.Date.DAY, -1), "Y-m-d")
            }
            ]
        });

        me.add({
            xtype: 'tabpanel',
            layout:'column',
            align : 'stretch',
            bodyStyle:'border-width:0',
            items: [{
                title: '全量用户数据',
                itemId: 'home',
                width:"16.5%",
                height:"100%",
                xtype:"panel",
                forceFit:true,
                bodyStyle:'border-color:black',
                layout:'column',
                items: getHtml("name", 3),
                listeners: {
                    'activate': function (tab) {
                        for (var i = 0; i < tab.items.items.length; i++) {
                            me.echarts = echarts.init(Ext.get(tab.items.items[i].id).dom);
                            me.echarts.setOption(option);
                        }
                    }
                }
            }, {
                title: '新增用户数据',
                html: 'Users',
                itemId: 'users',
                autoScroll: true,
                closable: false,
                items: [],
                listeners: {
                    'show': function (tab) {
                        tab.removeAll();
                        tab.items.add(Ext.create("WF.view.data.board.monthlyDataViewMain", {height: 850}));
                    }
                }

            }, {
                title: '留存数据',
                html: 'Tickets',
                itemId: 'tickets',
                autoScroll: true,
                closable: false,
                items: [],
                listeners: {
                    'show': function (tab) {
                        tab.removeAll();
                        this.items.add(Ext.create("WF.view.data.board.filterDataViewMain", {height: 850}));
                    }
                }
            }, {
                title: '其它数据',
                html: 'aa',
                itemId: 'aa',
                autoScroll: true,
                closable: false,
                items: [],
                listeners: {
                    'show': function (tab) {
                        console.dir(tab.itemId)
                        tab.removeAll();
                        this.items.add(Ext.create("WF.view.data.board.GameMonitorViewMain", {height: 850}));
                    }
                }
            }]
        });

        function getHtml(name, len) {
            var items = [];
            for (var i = 0; i < len; i++) {
                items.push({
                    xtype:"panel",
                    layout:'hbox',
                    title: name + i,
                    id: name + i,
                    width:300,
                    height:300,
                    forceFit:true,
                    bodyStyle:'border-width:0'
                });
            }
            return items;
        }
    }


});


var option = {
    tooltip: {
        trigger: 'axis'
    },
    legend: {
        data: ['邮件营销', '联盟广告', '视频广告', '直接访问', '搜索引擎']
    },
    toolbox: {
        show: true,
        feature: {
            mark: {show: true},
            dataView: {show: true, readOnly: false},
            magicType: {show: true, type: ['line', 'bar', 'stack', 'tiled']},
            restore: {show: true},
            saveAsImage: {show: true}
        }
    },
    calculable: true,
    xAxis: [
        {
            type: 'category',
            boundaryGap: false,
            data: ['周一', '周二', '周三', '周四', '周五', '周六', '周日']
        }
    ],
    yAxis: [
        {
            type: 'value'
        }
    ],
    series: [
        {
            name: '邮件营销',
            type: 'line',
            stack: '总量',
            data: [120, 132, 101, 134, 90, 230, 210]
        },
        {
            name: '联盟广告',
            type: 'line',
            stack: '总量',
            data: [220, 182, 191, 234, 290, 330, 310]
        },
        {
            name: '视频广告',
            type: 'line',
            stack: '总量',
            data: [150, 232, 201, 154, 190, 330, 410]
        },
        {
            name: '直接访问',
            type: 'line',
            stack: '总量',
            data: [320, 332, 301, 334, 390, 330, 320]
        },
        {
            name: '搜索引擎',
            type: 'line',
            stack: '总量',
            data: [820, 932, 901, 934, 1290, 1330, 1320]
        }
    ]
};
