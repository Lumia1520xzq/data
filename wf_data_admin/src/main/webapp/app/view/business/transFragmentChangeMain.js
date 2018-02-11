Ext.define('WF.view.business.transFragmentChangeMain', {
    extend: 'Ext.panel.Panel',
    title: '碎片流水',
    xtype: 'transFragmentChangeMain',
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
            url: 'data/admin/business/transFragmentChange/list.do',
            fields: [
                'userName', 'userId', 'changeNum', 'changeType', 'businessType', 'channelId', 'createTime', 'activityName', 'phyAwardsName', 'rmbAmount'
            ]
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
            fields: ['id', 'name']
        });

        var childChannelStore = Ext.create('DCIS.Store', {
            autoLoad: true,
            url: 'data/admin/common/data/getChildChannels.do',
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

        var changeTypeStore = Ext.create('DCIS.Store', {
            autoLoad: true,
            fields: ['value', 'name'],
            data: [
                {value: 1, name: "增加"},
                {value: -1, name: "减少"}
            ]
        });

        var fragmentTypeStore = Ext.create('DCIS.Store', {
            autoLoad: true,
            url: 'data/admin/business/transFragmentChange/getAllFragmentType.do',
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
                callapi("data/admin/business/transFragmentChange/sumData.do", me.down('dataform').form.getValues(), function (response) {
                    me.down("[name='sumData']").setValue(Ext.util.Format.number(response, "0,000.00") + " 元");
                });
            },
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
                afterLabelTextTpl: required,
                allowBlank: false,
                xtype: 'datefield',
                name: 'beginDate',
                format: 'Y-m-d',
                fieldLabel: '开始时间',
                value:Ext.util.Format.date(Ext.Date.add(new Date(),Ext.Date.DAY,-1),"Y-m-d")
            }, {
                afterLabelTextTpl: required,
                allowBlank: false,
                xtype: 'datefield',
                name: 'endDate',
                format: 'Y-m-d',
                fieldLabel: '结束时间',
                value:Ext.util.Format.date(Ext.Date.add(new Date(),Ext.Date.DAY,-1),"Y-m-d")
            }, {
                name: 'fragmentType',
                fieldLabel: '碎片类型',
                xtype: 'combobox',
                store: fragmentTypeStore,
                emptyText: "--请选择--",
                displayField: 'name',
                valueField: "id",
                editable: false
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
            columns: [
                {
                    text: '用户昵称',
                    width: 50,
                    dataIndex: 'userName',
                    menuDisabled: true,
                    sortable: false
                },
                {
                    text: '游戏用户ID',
                    dataIndex: 'userId',
                    width: 50,
                    menuDisabled: true,
                    sortable: false
                }, {
                    text: '碎片变化数量',
                    width: 50,
                    dataIndex: 'changeNum',
                    menuDisabled: true,
                    sortable: false
                }, {
                    text: '碎片变动类型',
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
                    text: '出口类型',
                    width: 30,
                    dataIndex: 'activityName',
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
                    renderer: function (value) {
                        if (value != null) {
                            return Ext.util.Format.number(value, "0,000.00");
                        } else {
                            return 0.00;
                        }
                    }
                }, {
                    text: '创建时间',
                    width: 100,
                    dataIndex: 'createTime',
                    menuDisabled: true,
                    sortable: false
                }
            ]
        });
    }
});