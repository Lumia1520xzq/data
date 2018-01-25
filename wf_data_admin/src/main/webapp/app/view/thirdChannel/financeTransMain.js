Ext.define('WF.view.thirdChannel.financeTransMain', {
    extend: 'Ext.panel.Panel',
    title: '资金流水',
    xtype: 'financeTransMain' + this.parameters + '',
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
            url: 'data/admin/channel/financetrans/list.do',
            fields: [
                'userName', 'userId', 'changeType',
                'changeMoney', 'changeBefore', 'changeAfter',
                'businessType', 'businessId', 'businessMoney',
                'channelId', 'remark', 'createTime'
            ],
            baseParams: {
                parentId: me.parameters
            }
        });

        var businessTypeStore = Ext.create('DCIS.Store', {
            autoLoad: true,
            url: 'data/admin/dict/getListByType.do',
            fields: ['value', 'label'],
            baseParams: {
                type: 'business_type'
            }
        });

        var parentChannelStore = Ext.create('DCIS.Store', {
            url: 'data/admin/common/data/getParentChannels.do',
            autoLoad: true,
            fields: ['id', 'name']
        });

        var allChannelStore = Ext.create('DCIS.Store', {
            autoLoad: true,
            url: 'data/admin/common/data/getAllChannels.do',
            fields: ['id', 'name'],
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

        var changeTypeStore = Ext.create('DCIS.Store', {
            autoLoad: true,
            fields: ['value', 'name'],
            data: [
                {value: 1, name: "增加"},
                {value: -1, name: "减少"}
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
            forceFit: false,
            sumData: function () {
                callapi("data/admin/channel/financetrans/sumData.do", me.down('dataform').form.getValues(), function (response) {
                    me.down("[name='changeAmount']").setValue(Ext.util.Format.number(response.changeAmount, "0,000") + " 元");
                    me.down("[name='businessAmount']").setValue(Ext.util.Format.number(response.businessAmount, "0,000") + " 元");
                });
            },
            items: [{
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
                colspan: 1,
                name: 'userId',
                xtype: 'searchfield',
                displayField: 'nickname',
                valueField: "id",
                queryMode: "local",
                store: 'userStore',
                editable: false,
                fieldLabel: '用户'
            }, {
                name: 'businessType',
                fieldLabel: '业务类型',
                xtype: 'combobox',
                store: businessTypeStore,
                emptyText: "--请选择--",
                displayField: 'label',
                valueField: "value",
                editable: false
            }, {
                name: 'changeType',
                fieldLabel: '资金变动类型',
                xtype: 'combobox',
                store: changeTypeStore,
                emptyText: "--请选择--",
                displayField: 'name',
                valueField: "value",
                editable: false
            }, {
                name: 'changeMoneyLow',
                fieldLabel: '变动金额下限'
            }, {
                name: 'changeMoneyHigh',
                fieldLabel: '变动金额上限'
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
            items: [{
                xtype: 'displayfield',
                name: 'changeAmount',
                value: 0,
                fieldLabel: '<span style="font-size:14px;font-weight:bold">变动金额合计：</span>',
                padding: 5
            }, {
                xtype: 'displayfield',
                name: 'businessAmount',
                value: 0,
                fieldLabel: '<span style="font-size:14px;font-weight:bold">业务金额合计：</span>',
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
                width: 50,
                dataIndex: 'userName',
                menuDisabled: true,
                sortable: false
            }, {
                text: '游戏用户ID',
                dataIndex: 'userId',
                width: 50,
                menuDisabled: true,
                sortable: false
            }, {
                text: '资金变动类型',
                width: 50,
                dataIndex: 'changeType',
                menuDisabled: true,
                sortable: false,
                renderer: function (value) {
                    var record = changeTypeStore.findRecord('value', value, 0, false, false, true);
                    if (record == null) {
                        return '--';
                    } else {
                        return record.data.name;
                    }
                }
            }, {
                text: '变动金额',
                width: 50,
                dataIndex: 'changeMoney',
                menuDisabled: true,
                sortable: false
            }, {
                text: '变动前金额',
                width: 50,
                dataIndex: 'changeBefore',
                menuDisabled: true,
                sortable: false
            }, {
                text: '变动后金额',
                width: 50,
                dataIndex: 'changeAfter',
                menuDisabled: true,
                sortable: false
            }, {
                text: '业务类型',
                width: 50,
                dataIndex: 'businessType',
                menuDisabled: true,
                sortable: false,
                renderer: function (value) {
                    var record = businessTypeStore.findRecord('value', value, 0, false, false, true);
                    if (record == null) {
                        return '--';
                    } else {
                        return record.data.label;
                    }
                }
            }, {
                text: '业务ID',
                width: 30,
                dataIndex: 'businessId',
                menuDisabled: true,
                sortable: false
            }, {
                text: '业务金额',
                width: 50,
                dataIndex: 'businessMoney',
                menuDisabled: true,
                sortable: false
            }, {
                text: '渠道',
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
                text: '备注',
                width: 100,
                dataIndex: 'remark',
                menuDisabled: true,
                sortable: false
            }, {
                text: '创建时间',
                width: 100,
                dataIndex: 'createTime',
                menuDisabled: true,
                sortable: false
            }]
        });
    }
});