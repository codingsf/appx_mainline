function showDialog(params) {
    var div = document.createElement("div");
    div.id = "id" + new Date().getTime();
    document.body.appendChild(div);
    params.buttons = [{
        text: "关闭",
        handler: function () {
            $("#" + div.id).dialog("close");
        }
    }];

    $("#" + div.id).dialog(params);
}
//覆盖common中方法
function onError() {
    $.messager.alert("错误", "服务器错误", "error");
}

function zAlert(str, title, type) {
    $.messager.alert(title, str, type);
}

window.app_v = Math.random();
//Math.random();

//表格方法集
Dg_Method = {

    onBeforeLoad:function(p){
        p.pageNum=p.page;
        p.pageSize=p.rows;
        return true;
    },
    init: function () {
        if (!this.getSearchFormJson) {
            this.getSearchFormJson = Dg_Method.getSearchFormJson;
        }
        if (!this.getQueryParams) {
            this.getQueryParams = Dg_Method.getQueryParams;
        }
        if (!this.query) {
            this.query = Dg_Method.query;
        }
        if (!this.del) {
            this.del = Dg_Method.del;
        }
        if (!this.clearAll) {
            this.clearAll = Dg_Method.clearAll;
        }

        if (!this.refresh) {
            this.refresh = Dg_Method.refresh;
        }

        if (!this.mustSelectOne) {
            this.mustSelectOne = Dg_Method.mustSelectOne;
        }

        if(!this.onBeforeLoad){
            this.onBeforeLoad = Dg_Method.onBeforeLoad;
        }

        if (!this.dg) {
            this.dg = $("#dg");
        }
        if (!this.searchForm) {
            this.searchForm = $("#searchForm");
        }

        if (!this.d_tb) {
            this.d_tb = "#d_tb";
        }
        if (this.ctrlSelect == undefined) {
            this.ctrlSelect = true;
        }
        if (this.singleSelect == undefined) {
            this.singleSelect = false;
        }

        if (!this.action) {
            this.action = "queryPage";
        }
        if (this.pagination == undefined) {
            this.pagination = true;
        }
        if (this.fitColumns == undefined) {
            this.fitColumns = true;
        }


        if (!this.getDlg) {
            this.getDlg = Dg_Method.getDlg;
        }
        var me = this;
        this.dg = $(this.dg);
        this.dg.datagrid({
            method:"get",
            url: this.url,
            queryParams: this.getQueryParams(),

            onLoadError: function () {
                $.messager.alert("错误", "载入列表出错,请刷新页面后重试", "info");
            },
            loadFilter: function (data) {
                if (data.success === false) {
                    $.messager.alert("载入列表出错", data.msg, "info");
                    return null;
                }
                if (me.zloadFilterSuccess) {
                    me.zloadFilterSuccess(data);
                }
                if (data.list) {
                    return {
                        total:data.total,
                        rows:data.list
                    }
                } else {
                    return {
                        total: data.length,
                        rows: data
                    }
                }



            },
            fit: true,
            fitColumns: this.fitColumns,
            rownumbers: true,
            toolbar: this.d_tb,
            pageSize: 20,
            pagination: this.pagination,
            ctrlSelect: this.ctrlSelect,//ctrl多选
            singleSelect: this.singleSelect,//是否单选
            onClickRow: this.onClickRow,
            onUnselect: this.onUnselect,
            onBeforeLoad: this.onBeforeLoad,


            striped: true,//斑马条
            border: false,
            nowrap: this.nowrap,
            onDblClickRow: this.onDblClickRow ? this.onDblClickRow : function (rowIndex, rowData) {

                me.dg.datagrid("uncheckAll").datagrid("checkRow", rowIndex);

                var dlg = me.getDlg();
                if (dlg) {
                    dlg.showModify.call(dlg);
                }
            },
            columns: [this.getColumns()],
            view: this.view == undefined ? undefined : this.view,
            detailFormatter: this.detailFormatter == undefined ? undefined : this.detailFormatter,
            onExpandRow: this.onExpandRow == undefined ? undefined : this.onExpandRow

        });
        //   $.parser.parse(this.dg.parent());


        if (!this.searchForm.data("bind")) {//防止重复绑定
            var me = this;
            //绑定搜索表单提交事件
            var submitBtn = this.searchForm.find(".submit");
            this.searchForm.submit(function () {
                submitBtn.focus().click();
                return false;
            });
            submitBtn.click(function () {
                me.query();
            });
            this.searchForm.data("bind", true);
        }


    },
    //暂时性,这样做法容易导致混乱
    getDlg: function () {
        if (window.main && main.dlg) {
            return main.dlg;
        }
    },

    //获取表单内容json
    getSearchFormJson: function () {
        var queryParams = this.searchForm.serializeJson();
        if (queryParams.endTime && queryParams.endTime.length < 15) {
            var d = new Date(queryParams.endTime + " 00:00:00");
            d.setDate(d.getDate() + 1);
            queryParams.endTime = d.Format("yyyy-MM-dd HH:mm:ss");
        }
        return queryParams;
    },
    getQueryParams: function () {
        var params = this.getSearchFormJson();
        params.rnd = Math.random();
        return params;
        //return {action:"queryPage",query:JSON.stringify(this.getSearchFormJson())};
    },
    mustSelectOne: function () {
        var records = this.dg.datagrid("getSelections");
        if (records.length == 0) {
            $.messager.alert("提示", "请选择需要操作的数据", "info");
            return false;
        }
        if (records.length > 1) {
            $.messager.alert("提示", "只能选择一条数据", "info");
            return false;
        }
        return true;
    },
    //查询
    query: function () {
        this.dg.datagrid("load", this.getQueryParams());
    },
    //一次提交删除
    delx: function () {
        var records = this.dg.datagrid("getSelections");
        alert("功能尚未实现");
    },
    refresh:function(){
        this.dg.datagrid("reload");
    },
    //删除方法
    //callback,执行完成后回调方法,删除成功调用callback(true)否则调用false
    //getDataMethod,每次删除前封装提交参数的方法
    del: function (callback, getDataMethod) {
        var records = this.dg.datagrid("getSelections");
        if (records.length == 0) {
            $.messager.alert("提示", "请选择需要删除的数据", "info");
            return;
        }
        var ids = "";
        for(var i=0;i<records.length;i++){
            if(ids!=""){
                ids+=",";
            }
            ids += records[i].id;
        }

        var me = this;
        var msg = "确认要将这<strong style=color:red>" + records.length + "</strong>条数据删除吗?";
        $.messager.confirm("确认删除", msg, function (r) {
            if (r) {
                $.ajax({
                    url: me.url + "/" + ids,
                    type: "delete",
                    success:function(){
                        $.messager.show({
                            title: "成功",
                            msg: "删除成功"
                        });
                    },
                    error:function() {
                        $.messager.alert("错误", "系统错误", "error");
                    },
                    complete:function(){
                        $.messager.progress("close");
                        me.dg.datagrid("reload");
                        me.refresh();
                        if (callback) {
                            callback(false);
                        }
                    }
                });
            }
        });
    }
};


///对话框方法集
Dlg_Method = {
    //对话框重载方法
    init: function () {
        var me = this;
        if (!this.showAdd) {
            this.showAdd = Dlg_Method.showAdd;
        }
        if (!this.showModify) {
            this.showModify = Dlg_Method.showModify;
        }
        if (!this.submit) {
            this.submit = Dlg_Method.submit;
        }
        if (!this.getFormJson) {
            this.getFormJson = Dlg_Method.getFormJson;
        }
        if (!this.dlg) {
            this.dlg = $("#dlg");//默认
        } else {
            this.dlg = $(this.dlg);
        }
        if (!this.action) {
            this.action = "";//默认
        }
        if (!this.dg && window.main && main.dg) {
            this.dg = main.dg;
        };
        if (!this.mainForm) {
            this.mainForm = $(this.dlg).find(".mainForm");
        }

        if (!this.getSubmitData) {
            this.getSubmitData = Dlg_Method.getSubmitData;
        }

        if (!this.getSelectedRecord) {
            this.getSelectedRecord = Dlg_Method.getSelectedRecord;
        }
        if (!this.mustSelectOne) {
            this.mustSelectOne = Dlg_Method.mustSelectOne;
        }

        if (!this.refresh) {
            this.refresh = Dlg_Method.refresh;
        }

        if (!this.validForm) {
            this.validForm = Dlg_Method.validForm;
        }
        if (this.resizable === undefined) {
            this.resizable = false;
        }

        if (!this.resetDlg) {
            this.resetDlg = Dlg_Method.resetDlg;
        }

        if (!this.loadForm) {
            this.loadForm = Dlg_Method.loadForm;
        }


        if (this.buttons === undefined) {

            this.buttons = [];
            if (!this.justQuery) {
                this.buttons.push({
                    text: "确认",
                    iconCls: "icon-save",
                    handler: function () {
                        me.submit();
                    }
                })
            }

            this.buttons.push({
                text: "关闭",
                iconCls: "icon-cancel",
                handler: function () {
                    me.dlg.dialog("close");
                }
            });

        }


        var submitBtn = $(this.dlg).find(".submit");
        var me = this;
        //绑定对话框中表单的提交事件
        $(this.dlg).find(".mainForm").submit(function (e) {
            $(this).find(".easyui-textbox").each(function (index) {
                $(this).next().children(".textbox-value").val($(this).next().children(".textbox-text").val());
            });
            me.submit();
            return false;
        });


    },
    resetDlg: function () {
        this.mainForm.form("reset");
    },

    //显示添加对话框方法
    showAdd: function (ps) {
        if (!ps) {
            ps = {};
        }

        this.resetDlg();

        var params = {
            title: ps.title ? ps.title : (this.title_showAdd ? this.title_showAdd : "添加"),
            top: ps.top ? ps.top : undefined,
            left: ps.left ? ps.left : undefined,
            iconCls: ps.iconCls ? ps.iconCls : "icon-add",
            modal: ps.modal === undefined ? true : ps.modal,
            resizable: this.resizable,
            onOpen:ps.onOpen? ps.onOpen :undefined,
            onResize:ps.onResize? ps.onResize :undefined,
            onMove:ps.onMove? ps.onMove :undefined
        };

        if (ps.buttons) {
            params.buttons = ps.buttons;
        } else if (this.buttons) {
            params.buttons = this.buttons;
        }
        this.dlg.dialog(params);
        this.action = "add";
        var ts = this.dlg.find(".textbox-text");
        if (ts.length > 0) {
            $(ts[0]).focus();
        }
        var me = this;
        setTimeout(function () {
            me.dlg.dialog("center");
        })
        me.dlg.dialog("center");
        
    },
    mustSelectOne: function () {
        return this.dg.mustSelectOne()
    },
    loadForm: function () {
        var record = this.getSelectedRecord();
        this.resetDlg();
        if (record) {
            this.mainForm.form("load").form("load", record);
        }
    },
    //显示修改对话框方法
    showModify: function (ps) {
        if (!ps) {
            ps = {};
        }

        if (!this.mustSelectOne()) {
            return false;
        }

        var params = {
            title: ps.title ? ps.title : (this.justQuery ? (this.title_showModifyQuery ? this.title_showModifyQuery : "查看") : (this.title_showModify ? this.title_showModify : "修改")),
            top: ps.top ? ps.top : undefined,
            left: ps.left ? ps.left : undefined,
            iconCls: ps.iconCls ? ps.iconCls : (this.justQuery ? "icon-search" : "icon-edit"),
            modal: ps.modal === undefined ? true : ps.modal,
            resizable: this.resizable,
            onOpen:ps.onOpen? ps.onOpen :undefined,
            onResize:ps.onResize? ps.onResize :undefined,
            onMove:ps.onMove? ps.onMove :undefined
        };
        if (ps.buttons) {
            params.buttons = ps.buttons;
        } else if (this.buttons) {
            params.buttons = this.buttons;
        }
        this.dlg.dialog(params);
        this.loadForm();
        this.action = "modify";
        return true;
    },
    getSelectedRecord: function () {
        var record = this.dg.dg.datagrid("getSelected");
        return record;
    },

    getFormJson: function () {
        return this.mainForm.serializeJson();
    },
    getSubmitData: function () {
        var model = this.getFormJson();
        return model;
      /*  var submitData = {
            action: this.action,
            model: JSON.stringify(model)
        };
        return submitData;*/
    },
    refresh: function () {
        this.dg.refresh();
    },
    validForm: function () {
        var isValid = this.mainForm.form('validate');
        if (!isValid) {
            $.messager.alert("提示", "请正确填写每一项", "info");
            return false;
        }
        return true;
    },
    //提交对话框方法
    //successMethod,提交成功后调用的方法,如果没有提供则按默认处理,关闭对话框并刷新主表格
    submit: function (successMethod) {
        if (this.justQuery) {
            return true;
        }

        if (this.validForm() == false) {
            return false;
        }

        var submitData = this.getSubmitData();
        $.messager.progress();
        var me = this;
        $.ajax({
            type:this.action=="add"?"POST":"PUT",
            url:this.url,
            data:submitData,
            success:function(){
                $.messager.show({
                    title: "成功",
                    msg: "操作成功"
                });
                me.dlg.dialog("close");
            },
            error:function(data){
                $.messager.alert("失败", data.responseText?data.responseText:"系统错误", "info");
            }
        }).complete(function(){
            $.messager.progress("close");
            me.dg.refresh();
        })
    },

};





