/*
 * 导航菜单
 */

Ext.define("Demo.view.main.Nav", {
	extend: "Ext.tree.Panel",
	xtype: "nav",
	id: "nav",
	initComponent: function() {

		//导航菜单Store
		var navStore = Ext.create("Ext.data.TreeStore", {
			model: "Ext.data.TreeModel",
			proxy: {
				type: "ajax",
				reader: "json",
				url: "/tree"
			}
		});
		
		Ext.apply(this, {
			autoScroll: true,
            ui:'nav',
			//margin: "0 5 0 5",
			width: 225,
			border: false,
			rootVisible: false,
			store: navStore,
			listeners: {
				itemclick: function(view, rec, item, index) {
                    var me = this;
					rec.data.mod = "user";
					rec.data.modUrl = "user.User";

                    if(rec.data.mod && rec.data.modUrl) {
                        var mod = rec.data.mod;
                        var modUrl = "Demo.view." + rec.data.modUrl;
                        Ext.require([modUrl], function() {
                        	//Ext.getCmp("").loadModule(mod);
                            //me.loadModule(mod);
                            var me = this;
                            if(!me.con) {
                                me.con = Ext.getCmp("ttt");
                            }
                            //me.con.removeAll();
                            me.con.add({xtype: mod});
                        });
                    }else {
                        //Ext.Msg.alert("错误", "加载模块失败！");
                    }
                }
			}
		});
		this.callParent(arguments);
	}
});
