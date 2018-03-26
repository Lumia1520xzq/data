Ext.define('WF.view.data.board.userClassifyViewMain', {
    extend: 'Ext.panel.Panel',
    title: '数据概览-用户分层分析',
    xtype: 'userClassifyViewMain',
    closable: true,
    autoScroll:true,
    layout: {
        type: 'vbox',
        align: 'stretch'
    },

    initComponent: function () {
        var me = this;
        me.callParent(arguments);
        var store= Ext.create('DCIS.Store', {
            url:'data/user/classify/getList.do',
            autoload:false,
            fields: []
        });

        var parentChannelStore = Ext.create('DCIS.Store', {
            url: 'data/admin/common/data/getFilterChannels.do',
            autoLoad: true,
            fields: ['id', 'name']
        });

        var userTypeStore = Ext.create('DCIS.Store', {
            autoLoad: true,
            fields: ['value','name'],
            data:[
                {value:0,name:"新注册用户"},
                {value:1,name:"累充0"},
                {value:2,name:"累充1-100"},
                {value:3,name:"累充100-1000"},
                {value:4,name:"累充1000-10000"},
                {value:5,name:"累充10000-100000"},
                {value:6,name:"累充10万以上"}
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
                width:275,
                store: parentChannelStore
            },
            {
                name: 'userType',
                fieldLabel: '用户类型',
                xtype: 'combobox',
                store: userTypeStore,
                emptyText: "--请选择--",
                displayField: 'name',
                valueField: "value",
                editable: false
            },
            {
                name: 'startTime',
                id:"viewStart",
                fieldLabel: '开始时间',
                xtype: 'datefield',
                format: 'Y-m-d',
                value:Ext.util.Format.date(Ext.Date.add(new Date(),Ext.Date.DAY,-14),"Y-m-d")
            },
            {
                name: 'endTime',
                id:"viewEnd",
                fieldLabel: '结束时间',
                xtype: 'datefield',
                format: 'Y-m-d',
                value:Ext.util.Format.date(Ext.Date.add(new Date(),Ext.Date.DAY,-1),"Y-m-d")
            }]
        });

        me.add({
            xtype:'panel',
            layout:'vbox',
            align : 'stretch',
            bodyStyle:'border-width:0 0 0 0;',
            items: [{
                title: '活跃数据',collapsible: true,align:'stretch',height:300,width:"100%",xtype:"panel",layout:'hbox',forceFit:true,bodyStyle:'border-width:0;',
                items:[
                    {width:"100%",height:300,xtype:"panel",layout:'hbox',forceFit:true,bodyStyle:'border-width:0',items:[
                        {id:'userClassify0',width:"33.33%",height:300,xtype:"panel",layout:'vbox',forceFit:true},
                        {id:'userClassify1',width:"33.33%",height:300,xtype:"panel",layout:'vbox',forceFit:true},
                        {id:'userClassify2',width:"33.33%",height:300,xtype:"panel",forceFit:true,bodyStyle:'border-width:0'}
                    ]
                    }
                  ]
                }
            ]
        });


        store.addListener('datachanged',function(){
                fun1();
            }
        );

        store.load ({
            callback:function(){
                fun1();
            }
        });

        function fun1(){
            alert(123);
        }

    }
});

