/* 
 * 数据存储 - 用户
 */

Ext.define("Demo.store.User", {
	extend: "Ext.data.Store",
	alias: "store.user",
	
	model: "Demo.model.User",
	proxy: {
		type: "rest",
		url: "/user", //模拟后台访问地址
		reader: {
			type: "json",
			rootProperty: "list"
		}
	}
});