/*
 * 视图模型 - 用户管理
 */

Ext.define("Demo.view.user.UserModel", {
	extend: "Ext.app.ViewModel",
	alias: "viewmodel.user",
	requires: ["Demo.store.User"],
	
	data: {},
	
	stores: {
		user: {
			type: "user",
			pageSize: 10,
			autoLoad: true
		},
		state: Ext.create("Ext.data.Store", {
			fields: ["stateId", "stateName"],
			data: [
				["0", "启用"],
				["1", "禁用"]
			]
		})
	}
});
