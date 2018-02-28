Ext.define('WF.view.thirdChannel.channelNewUserMain', {
    extend: 'Ext.panel.Panel',
    title: '新用户数据',
    xtype: 'channelNewUserMain' + this.parameters + '',
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
            url: 'data/admin/channel/userdata/newuser/list.do',
            fields: ['businessDate', 'dau', 'newUsersNum'],
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
            items: [{
                xtype: 'hiddenfield',
                name: 'parentId',
                value: me.parameters
            }, {
                name: 'channelId',
                fieldLabel: '子渠道',
                xtype: 'combo',
                columns: 2,
                emptyText: "--请选择--",
                displayField: 'name',
                valueField: "id",
                editable: true,
                queryMode: "local",
                store: childChannelStore
            }, {
                xtype: 'datefield',
                name: 'beginDate',
                format: 'Y-m-d',
                fieldLabel: '开始时间',
                value: Ext.util.Format.date(Ext.Date.add(new Date(), Ext.Date.DAY, -6), "Y-m-d")
            }, {
                xtype: 'datefield',
                name: 'endDate',
                format: 'Y-m-d',
                fieldLabel: '结束时间',
                value: Ext.util.Format.date(Ext.Date.add(new Date(), Ext.Date.DAY), "Y-m-d")
            }]
        });


        me.add({
            xtype: 'datagrid',
            store: store,
            buildField: "Manual",
            forceFit: true,
            columns: [{
                text: '日期',
                dataIndex: 'businessDate',
                width: 30,
                menuDisabled: true,
                sortable: false
            }, {
                text: 'DAU',
                width: 30,
                dataIndex: 'dau',
                menuDisabled: true,
                sortable: false,
                renderer: function (value) {
                    if (value != null) {
                        return Ext.util.Format.number(value, "0,000");
                    } else {
                        return 0;
                    }
                }
            }, {
                text: '新增用户数',
                width: 30,
                dataIndex: 'newUsersNum',
                menuDisabled: true,
                sortable: false
            }]
        });
    }
});