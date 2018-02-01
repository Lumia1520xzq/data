Ext.define("WF.view.data.daily.dailyExportTemplateBtn", {
    extend: "Ext.Button",
    alias: "dailyExportTemplateBtn",
    text : '模版下载',
    iconCls : 'icon-add',//样式请参考webapp/resources/css/icon.css
    listeners : {
        click : function() {
            window.location.href = 'data/admin/dailyRecord/importFile/template.do'
        }
    }
});