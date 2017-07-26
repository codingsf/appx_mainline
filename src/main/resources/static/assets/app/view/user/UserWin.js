/*
 * 窗口视图 - 用户管理
 */

Ext.define("Demo.view.user.UserWin", {
	extend: "Ext.window.Window",
	xtype: "userwin",
	
	requires: ["Demo.view.user.UserModel", "Demo.view.user.UserController","Demo.store.User"],
	viewModel: {
		type: "user"
	},
	controller: "user",
	
	title: "新增用户",
	width: 600,
	height: 350,
	resizable: false,
	constrain: true,
	modal: true,
	/*
	tools: [{
		type: "refresh",
		tooltip: "刷新数据"
	}],
	*/
	
	initComponent: function() {
		Ext.apply(this, {
			items: [{
				xtype: "form",
				url:"/user",
				store:"{user}",
				reference: "userForm",
				id:"aa",
				margin: 10,
				defaults: {
					anchor: "100%"
				},
				fieldDefaults: {
					flex: 1,
					margin: 10,
					labelWidth: 60,
					allowBlank: false
				},
				items: [{
					xtype: "container",
					layout: {
						type: "hbox",
						align: "stretch"
					},
					items: [{
						xtype: "hiddenfield",
						name: "id",
						value: ""
					}, {
						xtype: "textfield",
						name: "username",
						fieldLabel: "用户名称"
					}]
				}]
			}],
			buttonAlign: "center",
			buttons: [{
				text: "保存",
				handler: "save"
			}, {
				text: "取消",
				handler: "cancel"
			}]
		});
		this.callParent(arguments);
	}
});
