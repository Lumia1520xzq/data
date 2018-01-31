Ext.define("WF.view.data.event.importEvent", {
    extend: "Ext.Button",
    alias: "importEvent",
    text : '导入',
    iconCls : 'icon-add',//样式请参考webapp/resources/css/icon.css
    listeners : {
        click : function() {
            uploadExcel("data/admin/event/importFile.do", function (result) {
                if (result.success) {
                    alert(result.data.msg);
                } else {
                    alert("导入失败");
                }});
        }
    }
});