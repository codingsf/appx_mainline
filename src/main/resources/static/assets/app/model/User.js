/* 
 * 数据模型 - 用户
 */

Ext.define("Demo.model.User", {
	extend: "Ext.data.Model",
	fields: [
	    {name: "id"},
	    {name: "username"},
	    {name: "nickname"},
	    {name: "userState"},
	    {name: "remark"}
	]
});