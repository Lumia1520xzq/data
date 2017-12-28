Ext.define("WF.view.sys.config.addSysConfigBtn", {
    extend: "Ext.Button",
    alias: "addSysConfigBtn",
    text : '新增',
    iconCls : 'icon-add',//样式请参考webapp/resources/css/icon.css
    listeners : {
        click : function() {
            var main = Ext.ComponentQuery.query("sysConfigMain")[0];
            var doRefresh = main.down('datagrid').store;
            var win = Ext.create("WF.view.sys.config.addSysConfig", {doRefresh: doRefresh});
            win.show();
        }
    }
});