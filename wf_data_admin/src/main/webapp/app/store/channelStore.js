Ext.define("WF.store.channelStore", {
    extend: "DCIS.Store",
    url: 'data/admin/common/data/getAllChannels.do',
    fields: [
        {name: 'id', type: 'string', display: '渠道ID', show: true},
        {name: 'name', type: 'string', display: '渠道名称', show: true}],
    autoLoad: true
});