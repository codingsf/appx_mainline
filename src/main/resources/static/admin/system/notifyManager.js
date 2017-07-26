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
    this.url = "/api/notify";
    this.init = Dg_Method.init;
    this.getColumns = function () {
        var columns = new Array();
        columns.push(
            { field: "id", title: "ID", hidden: false, checkbox: true },
            { field: "target", title: "目标", width: 100},
            { field: "title", title: "标题", width: 300},
            { field: "type", title: "类型", width: 100,formatter:function(val,src){
                switch (val){
                    case "email":
                        return "邮箱";
                    case "sms":
                        return "短信";
                }
                return '-';

            } },
            { field: "status", title: "状态", width: 100,formatter:function(val,src){
                switch (val){
                    case "success":
                        return "成功";
                }
                return '-';
            } },

            { field: "errorMsg", title: "错误信息", width: 100,formatter:function(val){
                if(val){
                    return "<a title='"+val+"'>"+val+"</a>";
                }
                return val;
            } }
       );
        return columns;
    };
}
//主对话框类,继承Dg_Method的方法
function Dlg() {
    this.url = "/api/notify";
    this.init = Dlg_Method.init;
}


