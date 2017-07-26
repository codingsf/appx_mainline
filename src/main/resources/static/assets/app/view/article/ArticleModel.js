/*
 * 视图模型 - 用户管理
 */

Ext.define("Demo.view.article.ArticleModel", {
	extend: "Ext.app.ViewModel",
	alias: "viewmodel.article",
	requires: ["Demo.store.Article"],
	
	data: {},
	
	stores: {
		user: {
			type: "aticle",
			pageSize: 10,
			autoLoad: true
		},
		role: {
			type: "role",
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
