Ext.define("WF.view.data.event.eventExcelTemplateBtn", {
    extend: "Ext.Button",
    alias: "eventExcelTemplateBtn",
    text : '模版下载',
    iconCls : 'icon-add',//样式请参考webapp/resources/css/icon.css
    listeners : {
        click : function() {
            window.location.href = 'data/admin/event/importFile/template.do'
        }
    }
});