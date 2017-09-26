var main = null;
$(function () {
    main = new Main();
    main.init();
});
//主操作类
function Main() {

    this.init = function () {
        this.dg = new Dg();
        this.dg.init();

        this.dlg = new Dlg();
        this.dlg.init();

        $("#d_tb .btn_add").click(function () {
            main.dlg.showAdd();
        });
        $("#d_tb .btn_modify").click(function () {
            main.dlg.showModify();
        });
        $("#d_tb .btn_del").click(function () {
            main.dg.del();
        });
    };
}
//主表格类,继承Dg_Method的方法
function Dg() {
    this.url = "/api/scheduleJobs";
    this.init = Dg_Method.init;
    this.getColumns = function () {
        var columns = new Array();
        columns.push(
            { field: "id", title: "ID", hidden: false, checkbox: true },
            { field: "name", title: "名称", width: 100 },
            { field: "cron", title: "cron", width: 100 },
            { field: "groupName", title: "分组名称", width: 100 },
            { field: "async", title: "是否异步", width: 100 },
            { field: "status", title: "状态", width: 100,formatter:function(val,src){
                switch (val){
                    case 1:
                        return "<b>正常</b>";
                    case 2:
                        return "<span style='color:gray'>暂停</span>";
                }
                return '-';
            } },
            { field: "lastSuccessTime", title: "lastSuccessTime", width: 150,formatter:function(val,src){
                if(src.lastErrorTime && val && new Date(src.lastErrorTime)>new Date(val)){
                    return "<span style='color:gray'>"+val+"</span>"
                }
                return val;
            } },
            { field: "lastErrorTime", title: "lastErrorTime", width: 150,formatter:function(val,src){
                if(src.lastSuccessTime&&val&& new Date(src.lastSuccessTime)>new Date(val)){
                    return "<span style='color:gray;'>"+val+"</span>";
                }
                return val;

            } },
            { field: "errorMsg", title: "错误信息", width: 100 }

        );
        return columns;
    };
}
//主对话框类,继承Dg_Method的方法
function Dlg() {
    this.url = "/api/scheduleJobs";
    this.init = Dlg_Method.init;
}


