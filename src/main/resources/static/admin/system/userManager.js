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
    this.url = "/api/users";
    this.init = Dg_Method.init;
    this.getColumns = function () {
        var columns = new Array();
        columns.push(
            { field: "id", title: "ID", hidden: false, checkbox: true },
            { field: "username", title: "用户名", width: 100},
            { field: "nickname", title: "昵称", width: 100},
            { field: "email", title: "邮箱", width: 100 },
            { field: "phone", title: "手机", width: 100 },
            { field: "qq", title: "qq", width: 100 },
            { field: "money", title: "积分", width: 100 },
            { field: "createTime", title: "注册时间", width: 100 }
        );
        return columns;
    };
}
//主对话框类,继承Dg_Method的方法
function Dlg() {
    this.url = "/api/users";
    this.init = Dlg_Method.init;
}


