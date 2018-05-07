Ext.define('WF.view.thirdChannel.channelFragmentMain', {
    extend: 'Ext.panel.Panel',
    title: '碎片成本',
    xtype: 'channelFragmentMain' + this.parameters + '',
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
            url: 'data/admin/channel/fragment/list.do',
            fields: ['userName', 'userId', 'activityName', 'phyAwardsName', 'rmbAmount', 'createTime'],
            baseParams: {
                parentId: me.parameters
            }
        });

        var channelUserStore = Ext.create('DCIS.Store', {
            autoLoad: true,
            url: 'data/admin/common/data/channelUserList.do',
            fields: [
                {name: 'id', type: 'string', display: '用户ID', show: true},
                {name: 'nickname', type: 'string', display: '用户昵称', show: true}],
            baseParams: {
                parentId: me.parameters
            }
        });

        var activityTypeStore = Ext.create('DCIS.Store', {
            autoLoad: true,
            url: 'data/admin/dict/getListByType.do',
            fields: ['value', 'label'],
            baseParams: {
                type: 'activity_type'
            }
        });

        var receiveTypeStore = Ext.create('DCIS.Store', {
            autoLoad: true,
            url: 'data/admin/dict/getListByType.do',
            fields: ['value', 'label'],
            baseParams: {
                type: 'receive_status'
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
            sumData: function () {
                callapi("data/admin/channel/fragment/sumData.do", me.down('dataform').form.getValues(), function (response) {
                    me.down("[name='sumData']").setValue(Ext.util.Format.number(response, "0,000.00") + " 元");
                });
            },
            export: function () {
                var parentId = me.down(("[name='parentId']")).value;
                var userId = me.down(("[name='userId']")).value;
                var activityType = me.down(("[name='activityType']")).value;
                var beginDate = me.down(("[name='beginDate']")).value;
                var endDate = me.down(("[name='endDate']")).value;
                if (userId == null || userId === undefined) {
                    userId = '';
                }
                if (activityType == null || activityType === undefined) {
                    activityType = '';
                }
                if (beginDate == null || beginDate === undefined) {
                    beginDate = '';
                }
                if (parentId == null || parentId === undefined) {
                    parentId = '';
                }
                if (endDate == null || endDate === undefined) {
                    endDate = '';
                }
                var url = '/data/admin/channel/fragment/export.do?parentId=' + parentId + '&userId=' + userId + '&activityType=' + activityType + '&beginDate=' + beginDate + '&endDate=' + endDate;
                window.location.href = url;
            },
            items: [{
                xtype: 'hiddenfield',
                name: 'parentId',
                value: me.parameters

            }, {
                colspan: 1,
                name: 'userId',
                xtype: 'searchfield',
                displayField: 'nickname',
                valueField: "id",
                queryMode: "local",
                store: channelUserStore,
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
                dataIndex: 'createTime',
                menuDisabled: true,
                sortable: false
            }]
        });
    }
});