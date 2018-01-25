Ext.define('WF.view.thirdChannel.regChannelRechargeMain', {
    extend: 'Ext.panel.Panel',
    title: '注册渠道充值查询',
    xtype: 'regChannelRechargeMain' + this.parameters + '',
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
            url: 'data/admin/channel/regChannelRecharge/list.do',
            fields: ['userName', 'userId', 'productPrice', 'createTime'],
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

        var childChannelStore = Ext.create('DCIS.Store', {
            autoLoad: true,
            url: 'data/admin/common/data/getChildChannels.do',
            fields: ['id', 'name'],
            baseParams: {
                parentId: me.parameters
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
                callapi("data/admin/channel/regChannelRecharge/sumData.do", me.down('dataform').form.getValues(), function (response) {
                    me.down("[name='sumData']").setValue(Ext.util.Format.number(response, "0,000.00") + " 元");
                });
            },
            items: [{
                xtype: 'hiddenfield',
                name: 'parentId',
                value: me.parameters

            }, {
                name: 'channelId',
                fieldLabel: '注册渠道',
                xtype: 'combo',
                emptyText: "--请选择--",
                displayField: 'name',
                valueField: "id",
                editable: true,
                queryMode: "local",
                store: childChannelStore
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
                xtype: 'datetimefield',
                name: 'startTime',
                format: 'Y-m-d H:i:s',
                fieldLabel: '开始时间'
            }, {
                xtype: 'datetimefield',
                name: 'endTime',
                format: 'Y-m-d H:i:s',
                fieldLabel: '结束时间'
            }]
        });

        me.add({
            items: [{
                xtype: 'displayfield',
                name: 'sumData',
                value: 0,
                fieldLabel: '<span style="font-size:14px;font-weight:bold">充值金额合计：</span>',
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
                text: '游戏用户ID',
                width: 30,
                dataIndex: 'userId',
                menuDisabled: true,
                sortable: false
            }, {
                text: '充值金额',
                width: 30,
                dataIndex: 'productPrice',
                menuDisabled: true,
                sortable: false
            }, {
                text: '充值时间',
                width: 50,
                dataIndex: 'createTime',
                menuDisabled: true,
                sortable: false
            }]
        });
    }
});