Ext.define('WF.view.business.payAgentMerchanMain', {
    extend: 'Ext.panel.Panel',
    title: '商户充值汇总查询',
    xtype: 'payAgentMerchanMain',
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
            url: 'data/admin/trans/payAgentMerchant/list.do',
            fields: ['merchantCode', 'thirdAmount', 'rechargeCount']
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
                callapi("data/admin/trans/payAgentMerchant/sumData.do", me.down('dataform').form.getValues(), function (response) {
                    me.down("[name='sumData']").setValue(Ext.util.Format.number(response.sumData, "0,000.00") + " 元");
                    me.down("[name='sumDataRmb']").setValue(Ext.util.Format.number(response.sumRmb, "0,000.00") + " 元");
                    me.down("[name='sumDataLeaf']").setValue(Ext.util.Format.number(response.sumLeaf, "0,000.00") + " 元");
                });
            },
            items: [{
                colspan: 1,
                name: 'merchantCode',
                xtype: 'searchfield',
                displayField: 'merchantCode',
                valueField: "merchantCode",
                queryMode: "local",
                store: 'payAgentMerchanStore',
                editable: false,
                fieldLabel: '商户'
            }, {
                xtype: 'datefield',
                name: 'startTime',
                format: 'Y-m-d',
                fieldLabel: '开始时间',
                value:Ext.util.Format.date(Ext.Date.add(new Date()),"Y-m-d")
            }, {
                xtype: 'datefield',
                name: 'endTime',
                format: 'Y-m-d',
                fieldLabel: '结束时间',
                value:Ext.util.Format.date(Ext.Date.add(new Date()),"Y-m-d")
            }]
        });

        me.add({
            items: [{
                xtype: 'displayfield',
                name: 'sumDataRmb',
                value: 0,
                fieldLabel: '<span style="font-size:14px;font-weight:bold;">RMB合计：</span>',
                padding: 10,
                labelWidth:100
            },{
                xtype: 'displayfield',
                name: 'sumDataLeaf',
                value: 0,
                fieldLabel: '<span style="font-size:14px;font-weight:bold">金叶子合计：</span>',
                padding: 10,
                labelWidth:110
            },{
                xtype: 'displayfield',
                name: 'sumData',
                value: 0,
                fieldLabel: '<span style="font-size:14px;font-weight:bold;color: red">总计：</span>',
                padding: 10,
                labelWidth:100
            }],
            layout: 'hbox'
        });

        me.add({
            xtype: 'datagrid',
            store: store,
            buildField: "Manual",
            forceFit: true,
            columns: [{
                text: '商户',
                dataIndex: 'merchantCode',
                width: 80,
                menuDisabled: true,
                sortable: false
            }, {
                text: '充值总额',
                width: 100,
                dataIndex: 'thirdAmount',
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
                text: '充值笔数',
                width: 30,
                dataIndex: 'rechargeCount',
                menuDisabled: true,
                sortable: false,
                renderer: function (value) {
                    if (value != null) {
                        return Ext.util.Format.number(value, "0,000.00");
                    } else {
                        return 0;
                    }
                }
            }]
        });
    }
});