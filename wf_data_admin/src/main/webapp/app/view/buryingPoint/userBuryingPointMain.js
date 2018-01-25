Ext.define('WF.view.buryingPoint.userBuryingPointMain', {
    extend: 'Ext.panel.Panel',
    title: '用户埋点信息查询',
    xtype: 'userBuryingPointMain',
    closable: true,
    layout: {
        type: 'vbox',
        align: 'stretch'
    },
    initComponent: function () {
        this.callParent(arguments);
        var me = this;
        var store = Ext.create('DCIS.Store', {
            autoLoad: true,
            url: 'data/admin/userBuryingPoint/list.do',
            fields: ['behaviorName', 'behaviorEventId', 'behaviorCount', 'behaviorUserCount', 'channelName']
        });

        var parentChannelStore = Ext.create('DCIS.Store', {
            url: 'data/admin/common/data/getParentChannels.do',
            autoLoad: true,
            fields: ['id', 'name']
        });

        var childChannelStore = Ext.create('DCIS.Store', {
            autoLoad: true,
            url: 'data/admin/common/data/getChildChannels.do',
            fields: ['id', 'name']
        });

        var parentEventIdStore = Ext.create('DCIS.Store', {
            autoLoad: true,
            url: 'data/admin/behaviorType/getParentEvents.do',
            fields: ['eventId', 'name']
        });

        var childEventIdStore = Ext.create('DCIS.Store', {
            autoLoad: true,
            url: 'data/admin/behaviorType/getChildEvents.do',
            fields: ['eventId', 'name']
        });

        var userTypeStore = Ext.create('DCIS.Store', {
            fields: ['userType', 'name'],
            data: [
                {userType: 1, name: "所有用户"},
                {userType: 2, name: "新增用户"}
            ]
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
                store: parentChannelStore,
                listeners: {
                    change: function (obj, val) {
                        childChannelStore.load({
                            params: {
                                parentId: val
                            }
                        });
                    }
                }
            }, {
                name: 'channelId',
                fieldLabel: '子渠道',
                xtype: 'combo',
                emptyText: "--请选择--",
                displayField: 'name',
                valueField: "id",
                editable: true,
                queryMode: "local",
                store: childChannelStore
            }, {
                name: 'parentEventId',
                fieldLabel: '主埋点',
                xtype: 'combo',
                emptyText: "--请选择--",
                displayField: 'name',
                valueField: "eventId",
                editable: true,
                queryMode: "local",
                store: parentEventIdStore,
                listeners: {
                    change: function (obj, val) {
                        childEventIdStore.load({
                            params: {
                                parentEventId: val
                            }
                        });
                    }
                }
            }, {
                name: 'eventId',
                fieldLabel: '子埋点',
                xtype: 'combo',
                emptyText: "--请选择--",
                displayField: 'name',
                valueField: "eventId",
                editable: true,
                queryMode: "local",
                store: childEventIdStore
            }, {
                colspan: 2,
                name: 'userType',
                xtype: 'combo',
                displayField: 'name',
                valueField: "userType",
                emptyText: "所有用户",
                queryMode: "remote",
                store: userTypeStore,
                editable: false,
                fieldLabel: '用户类型'
            }, {
                xtype: 'datefield',
                name: 'beginDate',
                format: 'Y-m-d',
                fieldLabel: '开始时间',
                value: Ext.util.Format.date(Ext.Date.add(new Date(), Ext.Date.DAY, -1), "Y-m-d")
            }, {
                xtype: 'datefield',
                name: 'endDate',
                format: 'Y-m-d',
                fieldLabel: '结束时间',
                value: Ext.util.Format.date(Ext.Date.add(new Date(), Ext.Date.DAY, -1), "Y-m-d")
            }]
        });
        me.add({
            xtype: 'datagrid',
            store: store,
            buildField: "Manual",
            forceFit: true,
            columns: [{
                text: '事件名称',
                dataIndex: 'behaviorName',
                width: 100,
                menuDisabled: true,
                sortable: false
            }, {
                text: '事件ID',
                width: 50,
                dataIndex: 'behaviorEventId',
                menuDisabled: true,
                sortable: false
            }, {
                text: '触发数量',
                width: 50,
                dataIndex: 'behaviorCount',
                menuDisabled: true,
                sortable: false
            }, {
                text: '触发用户数',
                dataIndex: 'behaviorUserCount',
                width: 120,
                menuDisabled: true,
                sortable: false
            }, {
                text: '所属渠道',
                dataIndex: 'channelName',
                width: 120,
                menuDisabled: true,
                sortable: false
            }]
        });
    }
});