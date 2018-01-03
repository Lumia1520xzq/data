Ext.define('WF.view.game.tcard.showTips', {
    extend: 'Ext.window.Window',
    alias: 'sysAddDict',
    title: '指标说明',
    modal: true,
    layout: {
        type: 'vbox',
        align: 'stretch'
    },
    initComponent: function () {
        this.callParent(arguments);
        var me = this;
        me.add({
            xtype: 'dataform',
            baseCls: 'x-plain',
            border: true,
            columns: 2,
            items: [
                {
                allowBlank: true,
                name: 'description',
                xtype: 'textarea', height:200,
                colspan: 2, fieldLabel:'指标说明',
                readOnly:true, value:
                    '投注流水：含桌费\n返奖率1：含桌费\n返奖率2：不含桌费\n' +
                    '投注ARPU：(投注-桌费)/投注人数\n投注ASP：(投注-桌费)/投注笔数'
            }
            ]
        });
    }
});