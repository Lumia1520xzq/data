Ext.define("WF.view.sys.behavior.addBehaviorTypeBtn", {
    extend: "Ext.Button",
    alias: "addBehaviorTypeBtn",
    text : '新增',
    iconCls : 'icon-add',//样式请参考webapp/resources/css/icon.css
    listeners : {
        click : function() {
            var main = Ext.ComponentQuery.query("behaviorTypeMain")[0];
            var doRefresh = main.down('datagrid').store;
            var win = Ext.create("WF.view.sys.behavior.addBehaviorType", {doRefresh: doRefresh});
            win.show();
        }
    }
});