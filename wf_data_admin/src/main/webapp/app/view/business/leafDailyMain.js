Ext.define('WF.view.business.leafDailyMain', {
    extend: 'Ext.panel.Panel',
    title: '金叶子流转数据报表',
    xtype: 'leafDailyMain',
    closable: true,
    autoScroll: true,
    layout: {
        type: 'vbox',
        align: 'stretch'
    },
    initComponent: function () {
        this.callParent(arguments);
        var me = this;

        var parentChannelStore = Ext.create('DCIS.Store', {
            url: 'data/admin/common/data/getParentChannels.do',
            autoLoad: true,
            fields: ['id', 'name']
        });

        var allChannelStore = Ext.create('DCIS.Store', {
            autoLoad: true,
            url: 'data/admin/common/data/getAllChannels.do',
            fields: ['id', 'name']
        });

        var showChannelStore = Ext.create('DCIS.Store', {
            fields: ['name', 'value'],
            data: [
                {name: '是', value: 1},
                {name: '否', value: 0}
            ]
        });

        var showTypeStore = Ext.create('DCIS.Store', {
            fields: ['name', 'value'],
            data: [
                {name: '按日', value: 0},
                {name: '按月', value: 1}
            ]
        });

        var store = Ext.create('Ext.data.Store', {
            fields: [
                'businessDate', 'channelId','rechargeAmountRmb',
                'rechargePresentedAmount', 'otherwaysGoldAmount', 'consumeAmount',
                'returnAmount', 'diffAmount','surplusAmount'
            ],
            pageSize: 20,
            proxy: {
                type: 'dcisproxy',
                url: 'data/admin/business/leafDaily/list.do',
                // extraParams: param,
                reader: {
                    type: "json",
                    idProperty: "ID",
                    root: "data"
                },
                writer: "json"
            },
            listeners: {
                beforeload: function (store, operation, eOpts) {
                    // 表格翻页的场合
                    if (me.down("[name='pageChange']").value) {
                        Ext.applyIf(operation, {
                            params: {
                                parentId: me.down("[name='parentId']").value,
                                showChannel: me.down("[name='showChannel']").value,
                                searchType: me.down("[name='searchType']").value,
                                tabId: me.down("[name='tabId']").value,
                                beginDate: Ext.util.Format.date(me.down("[name='beginDate']").value, "Y-m-d"),
                                endDate: Ext.util.Format.date(me.down("[name='endDate']").value, "Y-m-d")
                            }
                        });
                    } else {
                        operation.start = 0;
                        operation.page = 1;
                        store.currentPage = 1;
                        me.down("[name='pageChange']").value = true;
                    }
                }
            }
        });

        me.add({
            border: false,
            xtype: 'searchpanel',
            store: store,
            title: '查询',
            collapsible: true,
            collapsed: false,
            columns: 2,
            buildField: "Manual",
            forceFit: false,
            items: [
                {
                    colspan: 2,
                    name: 'parentId',
                    fieldLabel: '主渠道：',
                    xtype: 'combo',
                    emptyText: "全部",
                    displayField: 'name',
                    valueField: "id",
                    editable: false,
                    multiSelect: true,
                    queryMode: "local",
                    store: parentChannelStore
                }, {
                    name: 'showChannel',
                    fieldLabel: '显示渠道：',
                    xtype: 'combo',
                    displayField: 'name',
                    valueField: "value",
                    editable: false,
                    queryMode: "local",
                    store: showChannelStore,
                    listeners: {
                        afterRender: function (obj) {
                            // 默认显示渠道
                            obj.setValue(1);
                        },
                        change: function (obj, nv, ov) {
                            if (nv === null) {
                                obj.setValue(ov);
                            }
                        }
                    }
                }, {
                    name: 'searchType',
                    fieldLabel: '查询方式：',
                    xtype: 'combo',
                    displayField: 'name',
                    valueField: "value",
                    editable: false,
                    queryMode: "local",
                    store: showTypeStore,
                    listeners: {
                        afterRender: function (obj) {
                            // 默认按天查询
                            obj.setValue(0);
                        },
                        change: function (obj, nv, ov) {
                            if (nv === null) {
                                obj.setValue(ov);
                            }
                        }
                    }
                }, {
                    xtype: 'datefield',
                    name: 'beginDate',
                    format: 'Y-m-d',
                    editable: false,
                    value:Ext.util.Format.date(Ext.Date.add(new Date(),Ext.Date.DAY,-7),"Y-m-d"),
                    fieldLabel: '开始日期：'
                }, {
                    xtype: 'datefield',
                    name: 'endDate',
                    format: 'Y-m-d',
                    editable: false,
                    value:Ext.util.Format.date(Ext.Date.add(new Date(),Ext.Date.DAY,-1),"Y-m-d"),
                    fieldLabel: '结束日期：'
                }, {// 标签页ID
                    name: 'tabId',
                    xtype: 'hiddenfield',
                    value: ''
                }, {// 翻页标识
                    name: 'pageChange',
                    xtype: 'hiddenfield',
                    value: false
                }
            ],
            todoReload: function () {
                doSearchProcess(me.down("[name='tabId']").value);
            },
            export: function () {
                window.location.href = 'data/admin/business/leafDaily/export.do?parentId=' + me.down("[name='parentId']").value +
                    '&showChannel=' + me.down("[name='showChannel']").value +
                    '&searchType=' + me.down("[name='searchType']").value +
                    '&tabId=' + me.down("[name='tabId']").value +
                    '&beginDate=' + Ext.util.Format.date(me.down("[name='beginDate']").value, "Y-m-d") +
                    '&endDate=' + Ext.util.Format.date(me.down("[name='endDate']").value, "Y-m-d");
            }
        });

        me.add({
            name: 'leafTabs',
            xtype: 'tabpanel',
            layout: {
                type: 'vbox',
                align: 'stretch'
            },
            bodyStyle: 'border-width:0',
            items: [
                {
                    title: '　　　　充值　　　　',
                    itemId: 'rechargeTab',
                    xtype: "panel",
                    forceFit: true,
                    bodyStyle: 'border-width:0',
                    layout: {
                        type: 'vbox',
                        align: 'stretch'
                    },
                    items: [{
                        xtype: 'datagrid',
                        store: store,
                        buildField: "Manual",
                        forceFit: true,
                        height: 730,
                        columns: [
                            {
                                text: '日期',
                                flex: 1,
                                dataIndex: 'businessDate',
                                menuDisabled: true,
                                sortable: false
                            },
                            {
                                text: '渠道',
                                flex: 1,
                                dataIndex: 'channelId',
                                menuDisabled: true,
                                sortable: false,
                                renderer: function (value) {
                                    return getChannelName(value);
                                }
                            },
                            {
                                text: '充值购买',
                                flex: 1,
                                dataIndex: 'rechargeAmountRmb',
                                menuDisabled: true,
                                sortable: false,
                                renderer: function (value) {
                                    return Ext.util.Format.number(value * 1000, '0,000');
                                }
                            },
                            {
                                text: '充值赠送',
                                flex: 1,
                                dataIndex: 'rechargePresentedAmount',
                                menuDisabled: true,
                                sortable: false,
                                renderer: function (value) {
                                    return Ext.util.Format.number(value, '0,000');
                                }
                            }
                        ]
                    }],
                    listeners: {
                        'activate': function (tab) {
                            me.down("[name='tabId']").value = tab.itemId;
                            doSearchProcess(tab.itemId);
                        }
                    }
                },
                {
                    title: '　　　　流转　　　　',
                    itemId: 'flowTab',
                    xtype: "panel",
                    forceFit: true,
                    bodyStyle: 'border-width:0',
                    layout: {
                        type: 'vbox',
                        align: 'stretch'
                    },
                    items: [{
                        xtype: 'datagrid',
                        store: store,
                        buildField: "Manual",
                        forceFit: true,
                        height: 730,
                        columns: [
                            {
                                text: '日期',
                                flex: 1,
                                dataIndex: 'businessDate',
                                menuDisabled: true,
                                sortable: false
                            },
                            {
                                text: '渠道',
                                flex: 1,
                                dataIndex: 'channelId',
                                menuDisabled: true,
                                sortable: false,
                                renderer: function (value) {
                                    return getChannelName(value);
                                }
                            },
                            {
                                text: '金叶子其他来源',
                                flex: 1,
                                dataIndex: 'otherwaysGoldAmount',
                                menuDisabled: true,
                                sortable: false,
                                renderer: function (value) {
                                    return Ext.util.Format.number(value, '0,000');
                                }
                            },
                            {
                                text: '金叶子消耗',
                                flex: 1,
                                dataIndex: 'consumeAmount',
                                menuDisabled: true,
                                sortable: false,
                                renderer: function (value) {
                                    return Ext.util.Format.number(value, '0,000');
                                }
                            },
                            {
                                text: '返奖金叶子',
                                flex: 1,
                                dataIndex: 'returnAmount',
                                menuDisabled: true,
                                sortable: false,
                                renderer: function (value) {
                                    return Ext.util.Format.number(value, '0,000');
                                }
                            },
                            {
                                text: '投注流水差',
                                flex: 1,
                                dataIndex: 'diffAmount',
                                menuDisabled: true,
                                sortable: false,
                                renderer: function (value) {
                                    return Ext.util.Format.number(value, '0,000');
                                }
                            }
                        ]
                    }],
                    listeners: {
                        'activate': function (tab) {
                            me.down("[name='tabId']").value = tab.itemId;
                            doSearchProcess(tab.itemId);
                        }
                    }
                },
                {
                    title: '　　　　存量　　　　',
                    itemId: 'remainTab',
                    xtype: "panel",
                    forceFit: true,
                    bodyStyle: 'border-width:0',
                    layout: {
                        type: 'vbox',
                        align: 'stretch'
                    },
                    items: [{
                        xtype: 'datagrid',
                        store: store,
                        forceFit: true,
                        buildField: "Manual",
                        height: 730,
                        columns: [
                            {
                                text: '日期',
                                flex: 1,
                                dataIndex: 'businessDate',
                                menuDisabled: true,
                                sortable: false
                            },
                            {
                                text: '渠道',
                                flex: 1,
                                dataIndex: 'channelId',
                                menuDisabled: true,
                                sortable: false,
                                renderer: function (value) {
                                    return getChannelName(value);
                                }
                            },
                            {
                                text: '用户金叶子存量',
                                flex: 1.5,
                                dataIndex: 'surplusAmount',
                                menuDisabled: true,
                                sortable: false,
                                renderer: function (value) {
                                    return Ext.util.Format.number(value, '0,000');
                                }
                            }
                        ]
                    }],
                    listeners: {
                        'activate': function (tab) {
                            me.down("[name='tabId']").value = tab.itemId;
                            doSearchProcess(tab.itemId);
                        }
                    }
                }
            ]
        });

        /**
         * 查询处理方法
         * @param tabId 标签页的ID
         */
        function doSearchProcess(tabId) {
            // 不是翻页操作
            me.down("[name='pageChange']").value = false;
            var value = {
                parentId: me.down("[name='parentId']").value,
                showChannel: me.down("[name='showChannel']").value,
                searchType: me.down("[name='searchType']").value,
                tabId: tabId,
                beginDate: Ext.util.Format.date(me.down("[name='beginDate']").value, "Y-m-d"),
                endDate: Ext.util.Format.date(me.down("[name='endDate']").value, "Y-m-d")
            };
            store.load({
                params: value/*,
                callback: function () {// 回调
                }*/
            });
        }

        /**
         * 获取渠道显示名称
         * @param value
         * @returns {*}
         */
        function getChannelName(value) {
            // 渠道
            var parentId = me.down("[name='parentId']").value;
            // 渠道选择“全部”的场合
            if (parentId == "") {
                return '全部';
            }
            // 显示渠道
            var show = me.down("[name='showChannel']").value;
            if (!show) {
                return '汇总';
            }
            var index = allChannelStore.find('id', value);
            if (index != -1) {
                var record = allChannelStore.getAt(index);
                return record.data.name;
            }
            return '--';
        }
    }
});