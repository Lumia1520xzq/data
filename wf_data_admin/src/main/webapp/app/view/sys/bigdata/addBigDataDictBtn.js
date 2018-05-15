Ext.define("WF.view.sys.bigdata.addBigDataDictBtn", {
    extend: "Ext.Button",
    alias: "addBigDataDictBtn",
    text : '新增',
    iconCls : 'icon-add',//样式请参考webapp/resources/css/icon.css
    listeners : {
        click : function() {
            var main = Ext.ComponentQuery.query("sysBigDataDictMain")[0];
            var doRefresh = main.down('datagrid').store;
            var win = Ext.create("WF.view.sys.bigdata.addBigDataDict", {doRefresh: doRefresh});
            win.show();
        }
    }
});