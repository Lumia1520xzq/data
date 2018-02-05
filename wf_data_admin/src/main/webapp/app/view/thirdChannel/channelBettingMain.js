Ext.define('WF.view.thirdChannel.channelBettingMain', {
    extend: 'Ext.panel.Panel',
    title: '投注数据',
    xtype: 'channelBettingMain' + this.parameters + '',
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
            url: 'data/admin/channel/userdata/betting/list.do',
            fields: ['businessDate', 'dau', 'bettingUserCount', 'bettingCount', 'bettingAmount', 'diffAmount','bettingArpu','bettingAsp','returnRate'],
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
        // me.down("[name='parentId']").setValue(me.parameters);
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
                value: Ext.util.Format.date(Ext.Date.add(new Date(),Ext.Date.DAY,-6),"Y-m-d")
            }, {
                xtype: 'datefield',
                name: 'endDate',
                format: 'Y-m-d',
                fieldLabel: '结束时间',
                value: Ext.util.Format.date(Ext.Date.add(new Date(),Ext.Date.DAY),"Y-m-d")
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
                renderer:function (value) {
                    if(value != null){
                        return Ext.util.Format.number(value, "0,000.00");
                    }else {
                        return 0.00;
                    }
                }
            }, {
                text: '投注人数',
                width: 30,
                dataIndex: 'bettingUserCount',
                menuDisabled: true,
                sortable: false,
                renderer:function (value) {
                    if(value != null){
                        return Ext.util.Format.number(value, "0,000");
                    }else {
                        return 0.00;
                    }
                }
            },{
                text: '投注笔数',
                width: 30,
                dataIndex: 'bettingCount',
                menuDisabled: true,
                sortable: false,
                renderer:function (value) {
                    if(value != null){
                        return Ext.util.Format.number(value, "0,000");
                    }else {
                        return 0.00;
                    }
                }
            }, {
                text: '投注金额',
                width: 30,
                dataIndex: 'bettingAmount',
                menuDisabled: true,
                sortable: false,
                renderer:function (value) {
                    if(value != null){
                        return Ext.util.Format.number(value, "0,000");
                    }else {
                        return 0.00;
                    }
                }
            }, {
                text: '投注ARPU',
                width: 30,
                dataIndex: 'bettingArpu',
                menuDisabled: true,
                sortable: false,
                renderer:function (value) {
                    if(value != null){
                        return Ext.util.Format.number(value, "0,000.0");
                    }else {
                        return 0.00;
                    }
                }
            }, {
                text: '投注ASP',
                width: 30,
                dataIndex: 'bettingAsp',
                menuDisabled: true,
                sortable: false,
                renderer:function (value) {
                    if(value != null){
                        return Ext.util.Format.number(value, "0,000.0");
                    }else {
                        return 0.00;
                    }
                }
            }, {
                text: '流水差',
                width: 30,
                dataIndex: 'diffAmount',
                menuDisabled: true,
                sortable: false,
                renderer:function (value) {
                    if(value != null){
                        return Ext.util.Format.number(value, "0,000");
                    }else {
                        return 0.00;
                    }
                }
            }, {
                text: '返奖率',
                width: 30,
                dataIndex: 'returnRate',
                menuDisabled: true,
                sortable: false
            }]
        });
    }
});