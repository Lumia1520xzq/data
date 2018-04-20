Ext.define("WF.userManual.manualCenter", {
    extend: 'Ext.tab.Panel',
    plain: true,
    alias: "widget.maincontent_manualCenter",
    region: 'manualCenter',
    autoScroll:true,
    activeTab: 0,
    plugins:Ext.create("DCIS.TabCloseMenu"),
    initComponent: function () {
        this.callParent(arguments);
        this.add(Ext.create("WF.userManual.targetDefine"));
    }
});