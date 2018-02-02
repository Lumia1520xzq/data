Ext.define('WF.view.data.board.ArpuViewMain', {
    extend: 'Ext.panel.Panel',
    title: '数据概览-用户累计ARPU',
    xtype: 'ArpuViewMain',
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
            url: 'data/arpu/view/getList.do',
            fields: ["channelName"]
        });
        var parentChannelStore = Ext.create('DCIS.Store', {
            url: 'data/admin/common/data/getParentChannels.do',
            autoLoad: true,
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
            forceFit: true,
            items: [
            {
                name: 'parentId',
                fieldLabel: '主渠道',
                xtype: 'combo',
                emptyText: "--请选择--",
                displayField: 'name',
                valueField: "id",
                editable: true,
                queryMode: "local",
                store: parentChannelStore
            },{
                name: 'beginDate',
                fieldLabel: '开始日期',
                xtype: 'datefield',
                format: 'Y-m-d'

            },{
                name: 'endDate',
                fieldLabel: '结束日期',
                xtype: 'datefield',
                format: 'Y-m-d'

            }]
        });

        me.add({
            xtype: 'datagrid',
            store: store,
            buildField: "Manual",
            forceFit: true,
            columns: [
            {
                text: '渠道',
                dataIndex: 'channelName',
                width: 50,
                menuDisabled: true,
                sortable: false
            }
            // , {
            //     text: 'DAU',
            //     width: 50,
            //     dataIndex: 'dau',
            //     menuDisabled: true,
            //     sortable: false
            // }, {
            //     text: '导入率',
            //     width: 50,
            //     dataIndex: 'importRate',
            //     menuDisabled: true,
            //     sortable: false
            // }, {
            //     text: '投注人数',
            //     width: 50,
            //     dataIndex: 'bettingUserCount',
            //     menuDisabled: true,
            //     sortable: false
            // }, {
            //     text: '投注笔数',
            //     width: 50,
            //     dataIndex: 'bettingCount',
            //     menuDisabled: true,
            //     sortable: false
            // }, {
            //     text: '投注流水',
            //     width: 50,
            //     dataIndex: 'bettingAmount',
            //     menuDisabled: true,
            //     sortable: false
            // },{
            //     text: '流水差',
            //     width: 50,
            //     dataIndex: 'amountGap',
            //     menuDisabled: true,
            //     sortable: false
            // },
            // {
            //     text: '投注转化率',
            //     width: 50,
            //     dataIndex: 'bettingRate',
            //     menuDisabled: true,
            //     sortable: false
            // },
            // {
            //     text: '返奖率',
            //     width: 50,
            //     dataIndex: 'returnRate',
            //     menuDisabled: true,
            //     sortable: false
            // },
            // {
            //     text: '投注ARPU',
            //     width: 50,
            //     dataIndex: 'arpu',
            //     menuDisabled: true,
            //     sortable: false
            // },
            // {
            //     text: '投注ASP',
            //     width: 50,
            //     dataIndex: 'asp',
            //     menuDisabled: true,
            //     sortable: false
            // },
            // {
            //     text: '人均频次',
            //     width: 50,
            //     dataIndex: 'avgBettingCount',
            //     menuDisabled: true,
            //     sortable: false
            // }
            ]
        });

    }
});