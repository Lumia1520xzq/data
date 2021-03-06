Ext.define('WF.view.thirdChannel.channelFishMain', {
    extend: 'Ext.panel.Panel',
    title: '游戏数据',
    xtype: 'channelFishMain' + this.parameters + '',
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
            url: 'data/admin/appfish/list.do',
            fields: ['dates', 'channelName', 'activenum', 'regmemnum', 'lognum', 'newlogmem', 'paymemnum',
                'payamount', /*'exchangeamount',*/ 'newsecondretentRate', 'allsecondretentRate'],
            baseParams: {
                parentId: [me.parameters]
            }
        });

        var childChannelStore = Ext.create('DCIS.Store', {
            autoLoad: true,
            url: 'data/admin/appuic/channel/getChildChannels.do',
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
            columns: 3,
            buildField: "Manual",
            forceFit: false,
            export: function () {
                var parentId = [me.parameters];
                var channelId = me.down(("[name='channelId']")).value;
                var beginDate = me.down(("[name='beginDate']")).value;
                var endDate = me.down(("[name='endDate']")).value;

                if (channelId == null || channelId === undefined) {
                    channelId = [];
                }
                /*if (parentId == null || parentId === undefined) {
                    parentId = [me.parameters];
                }*/
                if (beginDate == null || beginDate === undefined) {
                    beginDate = '';
                }
                if (endDate == null || endDate === undefined) {
                    endDate = '';
                }
                var url = 'data/admin/appfish/channelExportFile.do?parentId=' + parentId + '&channelId=' + channelId + '&beginDate=' + beginDate + '&endDate=' + endDate;
                window.location.href = url;
            },
            items: [/*{
                xtype: 'hiddenfield',
                name: 'parentId',
                value: me.parameters

            }, */{
                name: 'channelId',
                fieldLabel: '子渠道',
                xtype: 'combo',
                // emptyText: "--请选择--",
                displayField: 'name',
                valueField: "id",
                editable: true,
                multiSelect: true,
                queryMode: "local",
                store: childChannelStore
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
                value: Ext.util.Format.date(Ext.Date.add(new Date(), Ext.Date.DAY), "Y-m-d")
            }]
        });


        me.add({
            xtype: 'datagrid',
            store: store,
            buildField: "Manual",
            forceFit: true,
            showPaging:false,
            columns: [{
                text: '日期',
                dataIndex: 'dates',
                width: 30,
                menuDisabled: true,
                sortable: false
            }, {
                text: '渠道名称',
                width: 30,
                dataIndex: 'channelName',
                // menuDisabled: true,
                // sortable: false,
                renderer: function (value) {
                    if (value != null) {
                        return value;
                    } else {
                        return '';
                    }
                }
            }, {
                text: '激活',
                width: 30,
                dataIndex: 'activenum',
                // menuDisabled: true,
                // sortable: false,
                renderer: function (value) {
                    if (value != null) {
                        return Ext.util.Format.number(value, "0,000");
                    } else {
                        return 0;
                    }
                }
            }, {
                text: '注册',
                width: 30,
                dataIndex: 'regmemnum',
                // menuDisabled: true,
                // sortable: false,
                renderer: function (value) {
                    if (value != null) {
                        return Ext.util.Format.number(value, "0,000");
                    } else {
                        return 0;
                    }
                }
            }, {
                text: '登陆',
                width: 30,
                dataIndex: 'lognum',
                // menuDisabled: true,
                // sortable: false,
                renderer: function (value) {
                    if (value != null) {
                        return Ext.util.Format.number(value, "0,000");
                    } else {
                        return 0.00;
                    }
                }
            }, {
                text: '新增登陆',
                width: 30,
                dataIndex: 'newlogmem',
                // menuDisabled: true,
                // sortable: false,
                renderer: function (value) {
                    if (value != null) {
                        return Ext.util.Format.number(value, "0,000");
                    } else {
                        return 0;
                    }
                }
            }, {
                text: '充值人数',
                width: 30,
                dataIndex: 'paymemnum',
                // menuDisabled: true,
                // sortable: false,
                renderer: function (value) {
                    if (value != null) {
                        return Ext.util.Format.number(value, "0,000");
                    } else {
                        return 0;
                    }
                }
            }, {
                text: '充值金额',
                width: 30,
                dataIndex: 'payamount',
                // menuDisabled: true,
                // sortable: false,
                renderer: function (value) {
                    if (value != null) {
                        return Ext.util.Format.number(value, "0,000.00");
                    } else {
                        return 0;
                    }
                }
            }, /*{
                text: '成本',
                width: 30,
                dataIndex: 'exchangeamount',
                menuDisabled: true,
                sortable: false,
                renderer: function (value) {
                    if (value != null) {
                        return Ext.util.Format.number(value, "0,000.00");
                    } else {
                        return 0.00;
                    }
                }
            },*/ {
                text: '新用户次留',
                width: 30,
                dataIndex: 'newsecondretentRate',
                // menuDisabled: true,
                // sortable: false,
                renderer: function (value) {
                    if (value != null) {
                        return Ext.util.Format.number(value, "0,000.00") + "%";
                    } else {
                        return 0.00 + "%";
                    }
                }
            }, {
                text: '全量次留',
                width: 30,
                dataIndex: 'allsecondretentRate',
                // menuDisabled: true,
                // sortable: false,
                renderer: function (value) {
                    if (value != null) {
                        return Ext.util.Format.number(value, "0,000.00") + "%";
                    } else {
                        return 0.00 + "%";
                    }
                }
            }]
        });
    }
});