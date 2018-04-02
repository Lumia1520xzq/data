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
        var store = Ext.create('DCIS.Store', {
            url: 'data/game/view/getList.do',
            autoload: false,
            fields: ["todData", "yesData", "historyData"]
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
            items: [{
                name: 'parentId',
                fieldLabel: '主渠道',
                xtype: 'combo',
                emptyText: "--请选择--",
                displayField: 'name',
                valueField: "id",
                editable: true,
                queryMode: "local",
                store: parentChannelStore
            },
                {
                    name: 'gameType',
                    fieldLabel: '游戏',
                    xtype: 'combobox',
                    store: gameTypeStore,
                    emptyText: "--请选择--",
                    displayField: 'label',
                    valueField: "value",
                    editable: false
                },
                {
                    name: 'tabId',
                    xtype: 'hiddenfield',
                    value: '',
                },
                {
                    name: 'startTime',
                    id: "viewStart",
                    fieldLabel: '开始时间',
                    xtype: 'datefield',
                    format: 'Y-m-d',
                    value: Ext.util.Format.date(Ext.Date.add(new Date(), Ext.Date.DAY, -14), "Y-m-d")
                }, {
                    name: 'endTime',
                    id: "viewEnd",
                    fieldLabel: '结束时间',
                    xtype: 'datefield',
                    format: 'Y-m-d',
                    value: Ext.util.Format.date(Ext.Date.add(new Date(), Ext.Date.DAY, -1), "Y-m-d")
                }]
        });
        me.add({
            name:'myTabs',
            xtype: 'tabpanel',
            layout: 'column',
            align: 'stretch',
            bodyStyle: 'border-width:0',
            items: [{
                title: '全量用户数据',
                itemId: 'home',
                width: "16.5%",
                height: "100%",
                xtype: "panel",
                forceFit: true,
                bodyStyle: 'border-color:black',
                layout: 'column',
                items: getHtml("name", 3),
                listeners: {
                    'activate': function (tab) {
                        me.down(("[name='tabId']")).setValue(tab.itemId);
                        // doSearch();
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
                    'activate': function (tab) {
                        me.down(("[name='tabId']")).setValue(tab.itemId);
                        // doSearch();
                        /*tab.removeAll();
                        tab.items.add(Ext.create("WF.view.data.board.monthlyDataViewMain", {height: 850}));*/
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
                    'activate': function (tab) {
                        tab.removeAll();
                        console.dir(tab.itemId);
                        me.down(("[name='tabId']")).setValue(tab.itemId);
                        doSearch();
                       /* tab.removeAll();
                        this.items.add(Ext.create("WF.view.data.board.filterDataViewMain", {height: 850}));*/
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
                    'activate': function (tab) {
                        tab.removeAll();
                        me.down(("[name='tabId']")).setValue(tab.itemId);
                        doSearch();
                        /*tab.removeAll();
                        this.items.add(Ext.create("WF.view.data.board.GameMonitorViewMain", {height: 850}));*/
                    }
                }
            }]
        });

        function getHtml(name, len) {
            var items = [];
            for (var i = 0; i < len; i++) {
                items.push({
                    xtype: "panel",
                    layout: 'hbox',
                    name: name + i,
                    id: name + i,
                    width: 550,
                    height: 300,
                    forceFit: true,
                    bodyStyle: 'border-width:0'
                });
            }
            return items;
        };

        function doSearch() {
            var value = {
                parentId:me.down("[name='parentId']").value,
                gameType:me.down("[name='gameType']").value,
                tabId:me.down("[name='tabId']").value,
                startTime:Ext.util.Format.date(me.down("[name='startTime']").value, "Y-m-d"),
                endTime:Ext.util.Format.date(me.down("[name='endTime']").value, "Y-m-d"),
            }
            console.dir(value)
            store.load({
                params: value,
                callback:function(){
                    console.dir(store)
                    for(var i=0;i<store.getCount();i++){
                        var re=store.getAt(i);
                        console.dir(re.data.historyData)
                    }
                }
            });
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
