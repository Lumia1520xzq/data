Ext.define('WF.view.business.csConvertMain', {
    extend: 'Ext.panel.Panel',
    title: '充值查询',
    xtype: 'csConvertMain',
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
            url: 'data/admin/business/convert/list.do',
            fields: ['userId','userName', 'thirdAmount', 'bizType', 'orderSn', 'channelId', 'createTime', 'updateTime']
        });

        var productTypeStore = Ext.create('DCIS.Store', {
            autoLoad: true,
            url: 'data/admin/dict/getListByType.do',
            fields: ['value', 'label'],
            baseParams: {
                type: 'product_type'
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
                listeners :{
                    change:function(obj,val){
                        childChannelStore.load({
                            params: {
                                parentId : val
                            }
                        });
                        // me.down('#channelId').setValue('')

                    }
                }
            },{
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
            },{
                name: 'bizType',
                fieldLabel: '充值类型',
                xtype: 'combobox',
                store: productTypeStore,
                emptyText: "--请选择--",
                displayField: 'label',
                valueField: "value",
                editable: false
            }, {
                xtype: 'datetimefield',
                name: 'startTime',
                format: 'Y-m-d H:i:s',
                fieldLabel: '开始时间',
            }, {
                xtype: 'datetimefield',
                name: 'endTime',
                format: 'Y-m-d H:i:s',
                fieldLabel: '结束时间',
            }]
        });
        me.add({
            xtype: 'datagrid',
            store: store,
            buildField: "Manual",
            forceFit: true,
            columns: [{
                text: '游戏用户ID',
                dataIndex: 'userId',
                width: 80,
                menuDisabled: true,
                sortable: false
            }, {
                text: '用户昵称',
                width: 100,
                dataIndex: 'userName',
                menuDisabled: true,
                sortable: false
            }, {
                text: '充值金额',
                width: 30,
                dataIndex: 'thirdAmount',
                menuDisabled: true,
                sortable: false
            }, {
                text: '业务类型',
                width: 50,
                dataIndex: 'bizType',
                menuDisabled: true,
                sortable: false,
                renderer: function (value) {
                    var index = productTypeStore.find('value', value);
                    if (index != -1) {
                        var record = productTypeStore.getAt(index);
                        return record.data.label;
                    }
                    return '--';
                }
            }, {
                text: '订单号',
                width: 120,
                dataIndex: 'orderSn',
                menuDisabled: true,
                sortable: false
            }, {
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
                text: '创建时间',
                width: 100,
                dataIndex: 'createTime',
                menuDisabled: true,
                sortable: false
            }, {
                text: '到账时间',
                width: 100,
                dataIndex: 'updateTime',
                menuDisabled: true,
                sortable: false
            }]
        });
    }
});