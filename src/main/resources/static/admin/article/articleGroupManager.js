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
    this.url = "/api/articleGroups";
    this.init = Dg_Method.init;
    this.getColumns = function () {
        var columns = new Array();
        columns.push(
            { field: "id", title: "ID", hidden: false, checkbox: true },
            { field: "ida", title: "ID",formatter:function(val,src){return src.id;}},
            { field: "name", title: "名称", width: 100},
            { field: "flag", title: "标识", width: 100},
            { field: "createTime", title: "创建时间", width: 100 },
            { field: "modifyTime", title: "修改时间", width: 100 }
        );
        return columns;
    };
}
//主对话框类,继承Dg_Method的方法
function Dlg() {
    this.url = "/api/articleGroups";
    this.init = Dlg_Method.init;
}


