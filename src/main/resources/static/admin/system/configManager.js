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
    this.url = "/api/configs";
    this.init = Dg_Method.init;
    this.getColumns = function () {
        var columns = new Array();
        columns.push(
            { field: "id", title: "ID", hidden: false, checkbox: true },
            { field: "name", title: "名称", width: 100},
            { field: "value", title: "值", width: 100},
            { field: "remark", title: "备注", width: 100 },
            { field: "useCount", title: "使用次数", width: 100 },
            { field: "lastUseTime", title: "最后使用时间", width: 100 },
            { field: "createTime", title: "创建时间", width: 100 },
            { field: "modifyTime", title: "修改时间", width: 100 }

        );
        return columns;
    };
}
//主对话框类,继承Dg_Method的方法
function Dlg() {
    this.url = "/api/configs";
    this.init = Dlg_Method.init;
}


