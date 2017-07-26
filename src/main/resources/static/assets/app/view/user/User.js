/*
 * 视图 - 用户管理
 */

Ext.define("Demo.view.user.User", {
    extend: "Ext.grid.Panel",
    xtype: "user",
    id: "userGrid",

    requires: ["Demo.view.user.UserModel", "Demo.view.user.UserController"],
    viewModel: {
        type: "user"
    },
    controller: "user",

    bind: {
        store: "{user}"
    },
    initComponent: function() {
        Ext.apply(this, {
            selType: "checkboxmodel",
            title: "用户管理",
            border: true,
            columns: [{
                text: "用户Id",
                dataIndex: "id",
                hidden: true
            }, {
                text: "用户名称",
                dataIndex: "username",
                flex: 1
            },{
                text: "状态",
                dataIndex: "userState",
                flex: 1,
                renderer: function(val) {
                    if(val=="0" || val=="启用") {
                        return "<span style='color:green;'>启用</span>";
                    }else if(val=="1" || val=="禁用") {
                        return "<span style='color:red;'>禁用</span>";
                    }
                    return val;
                }
            }, {
                text: "备注",
                dataIndex: "remark",
                flex: 2
            }, {
                xtype: "actioncolumn",
                text: "操作",
                width: 100,
                align: "center",
                sortable: false,
                menuDisabled: true,
                items: [{
                    iconCls: "opt-edit",
                    tooltip: "编辑",
                    handler: "edit"
                }, {
                    iconCls: "opt-delete",
                    tooltip: "删除",
                    handler: "del"
                }]
            }],
            tbar: [{
                xtype: "textfield",
                maxWidth: 205,
                fieldLabel: "用户名称",
                labelWidth: 60
            },{
                xtype: "combobox",
                maxWidth: 180,
                fieldLabel: "状态",
                labelWidth: 35,
                bind: {
                    store: "{state}"
                },
                displayField: "stateName",
                valueField: "stateId",
                queryMode: "local"
            }, {
                xtype: "button",
                text: "搜索",
                glyph: 0xf002,
                handler: "search"
            }, "->", "->", {
                xtype: "button",
                text: "新增",
                glyph: 0xf067,
                handler: "add"
            }, {
                xtype: "button",
                text: "批量删除",
                glyph: 0xf00d,
                handler: "batchDel"
            }],
            bbar: {
                xtype: "pagingtoolbar",
                bind: {
                    store: "{user}"
                },
                displayInfo: true
            },
            listeners: {
                itemclick: function(view, record, item, index, e, eOpts) {}
            }
        });
        this.callParent(arguments);
    }
});