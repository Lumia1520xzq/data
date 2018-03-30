Ext.define('WF.view.business.costMain', {
    extend: 'Ext.panel.Panel',
    title: '实物成本查询',
    xtype: 'costMain',
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
            autoLoad: true,
            url: 'data/admin/channel/cost/list.do',
            fields: ['userName', 'userId', 'activityName', 'phyAwardsName', 'rmbAmount', 'sendTime','channelId']
        });

        var parentChannelStore = Ext.create('DCIS.Store', {
            url: 'data/admin/common/data/getParentChannels.do',
            autoLoad: true,
            fields: ['id', 'name']
        });

        var activityTypeStore = Ext.create('DCIS.Store', {
            autoLoad: true,
            url: 'data/admin/dict/getListByType.do',
            fields: ['value', 'label'],
            baseParams: {
                type: 'activity_type'
            }
        });

        var allChannelStore = Ext.create('DCIS.Store', {
            autoLoad: true,
            url: 'data/admin/common/data/getAllChannels.do',
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
            forceFit: false,
            sumData: function () {
                callapi("data/admin/channel/cost/sumData.do", me.down('dataform').form.getValues(), function (response) {
                    me.down("[name='sumData']").setValue(Ext.util.Format.number(response, "0,000.00") + " 元");
                });
            },
            items: [{
                name: 'channelId',
                fieldLabel: '主渠道',
                xtype: 'combo',
                emptyText: "--请选择--",
                displayField: 'name',
                valueField: "id",
                editable: true,
                queryMode: "local",
                store: parentChannelStore
            },{
                colspan: 1,
                name: 'userId',
                xtype: 'searchfield',
                displayField: 'nickname',
                valueField: "id",
                queryMode: "local",
                store: 'userStore',
                editable: false,
                fieldLabel: '用户昵称'
            }, {
                name: 'activityType',
                fieldLabel: '出口类别',
                xtype: 'combo',
                emptyText: "--请选择--",
                displayField: 'label',
                valueField: "value",
                editable: true,
                queryMode: "local",
                store: activityTypeStore
            },{
                colspan: 1,
                name: 'phyAwardsId',
                xtype: 'searchfield',
                displayField: 'name',
                valueField: "id",
                queryMode: "local",
                store: 'awardsInfoStore',
                editable: false,
                fieldLabel: '奖品名称'
            }, {
                xtype: 'datetimefield',
                name: 'beginDate',
                format: 'Y-m-d H:i:s',
                fieldLabel: '开始时间',
                value:Ext.util.Format.date(Ext.Date.add(new Date(),Ext.Date.DAY,-1),"Y-m-d 00:00:00")
            }, {
                xtype: 'datetimefield',
                name: 'endDate',
                format: 'Y-m-d H:i:s',
                fieldLabel: '结束时间',
                value:Ext.util.Format.date(Ext.Date.add(new Date(),Ext.Date.DAY,-1),"Y-m-d 23:59:59")
            }, {
                xtype: 'hiddenfield',
                name: 'receiveStatus',
                value: 3

            }]
        });

        me.add({
            items: [{
                xtype: 'displayfield',
                name: 'sumData',
                value: 0,
                fieldLabel: '<span style="font-size:14px;font-weight:bold">变动金额合计：</span>',
                padding: 5
            }],
            layout: 'hbox'
        });

        me.add({
            xtype: 'datagrid',
            store: store,
            buildField: "Manual",
            forceFit: true,
            columns: [{
                text: '用户昵称',
                width: 100,
                dataIndex: 'userName',
                menuDisabled: true,
                sortable: false
            }, {
                text: '用户ID',
                width: 30,
                dataIndex: 'userId',
                menuDisabled: true,
                sortable: false
            },{
                text: '充值渠道',
                width: 100,
                dataIndex: 'channelId',
                menuDisabled: true,
                sortable: false,
                renderer: function (value) {
                    var index = allChannelStore.find('id', value);
                    if (index != -1) {
                        var record = allChannelStore.getAt(index);
                        return record.data.name;
                    }
                    return '--';
                }
            }, {
                text: '出口类型',
                width: 30,
                dataIndex: 'activityName',
                menuDisabled: true,
                sortable: false
            }, {
                text: '奖品名称',
                width: 50,
                dataIndex: 'phyAwardsName',
                menuDisabled: true,
                sortable: false
            }, {
                text: '金额',
                width: 50,
                dataIndex: 'rmbAmount',
                menuDisabled: true,
                sortable: false,
                renderer:function (value) {
                    if(value != null){
                        return Ext.util.Format.number(value, "0,000.00");
                    }else {
                        return 0.00;
                    }
                }
            }, {
                text: '变更时间',
                width: 50,
                dataIndex: 'sendTime',
                menuDisabled: true,
                sortable: false
            }]
        });
    }
});