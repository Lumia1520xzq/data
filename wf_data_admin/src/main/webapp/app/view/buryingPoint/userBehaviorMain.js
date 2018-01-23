Ext.define('WF.view.buryingPoint.userBehaviorMain', {
    extend: 'Ext.panel.Panel',
    title: '用户埋点信息查询',
    xtype: 'userBehaviorMain',
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
            url: 'data/admin/userBehavior/list.do',
            fields: ['nickname', 'userId', 'channelName', 'channelId', 'eventName', 'behaviorEventId', 'createTime']
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
                name: 'userId',
                xtype: 'searchfield',
                displayField: 'nickname',
                valueField: "id",
                queryMode: "local",
                store: 'userStore',
                editable: false,
                fieldLabel: '用户'
            }, {
                xtype: 'datetimefield',
                name: 'beginDate',
                format: 'Y-m-d H:i:s',
                fieldLabel: '开始时间'
            }, {
                xtype: 'datetimefield',
                name: 'endDate',
                format: 'Y-m-d H:i:s',
                fieldLabel: '结束时间'
            }]
        });
        me.add({
            xtype: 'datagrid',
            store: store,
            buildField: "Manual",
            forceFit: true,
            columns: [{
                text: '用户昵称',
                dataIndex: 'nickname',
                width: 100,
                menuDisabled: true,
                sortable: false
            }, {
                text: '游戏用户ID',
                width: 50,
                dataIndex: 'userId',
                menuDisabled: true,
                sortable: false
            }, {
                text: '渠道名称',
                width: 50,
                dataIndex: 'channelName',
                menuDisabled: true,
                sortable: false
            }, {
                text: '渠道ID',
                dataIndex: 'channelId',
                width: 120,
                menuDisabled: true,
                sortable: false
            }, {
                text: '事件名称',
                dataIndex: 'eventName',
                width: 120,
                menuDisabled: true,
                sortable: false
            }, {
                text: '事件ID',
                dataIndex: 'behaviorEventId',
                width: 120,
                menuDisabled: true,
                sortable: false
            }, {
                text: '时间',
                dataIndex: 'createTime',
                width: 120,
                menuDisabled: true,
                sortable: false
            }]
        });
    }
});