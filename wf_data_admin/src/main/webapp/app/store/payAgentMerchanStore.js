Ext.define("WF.store.payAgentMerchanStore", {
    extend: "DCIS.Store",
    url: 'data/admin/common/data/getPayAgentMerchants.do',
    fields: [
        {name: 'merchantCode', type: 'string', display: '商户号', show: true},
        {name: 'company', type: 'string', display: '归属主体', show: true}],
    autoLoad: true
});