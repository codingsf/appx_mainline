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
    this.url = "/api/collectParam";
    this.init = Dg_Method.init;
    this.getColumns = function () {
        var columns = new Array();
        columns.push(
            { field: "id", title: "ID", hidden: false, checkbox: true },
            { field: "name", title: "名称", width: 100},
            { field: "twitterName", title: "推特名称", width: 100},
            { field: "articleGroupFlag", title: "articleGroupFlag", width: 150 },
            { field: "cron", title: "cron", width: 100 },
            { field: "listUrl", title: "listUrl", width: 100},
            { field: "articleASel", title: "articleASel", width: 100},
            { field: "timeSel", title: "timeSel", width: 100 },
            { field: "titleSel", title: "titleSel", width: 100 },
            { field: "contentSel", title: "内容选择器", width: 100 },
            { field: "articleGroupFlag", title: "文章分组", width: 100 },
            { field: "type", title: "type", width: 50,formatter:function(val,src){
                switch(val){
                    case 'common':
                        return "<span style=''>普通</span>";
                    case "wechat":
                        return "<span style='color:blue;'>微信</span>";
                    case "twitter":
                        return "<span style='color:green'>推特</span>";
                    default:
                        return "-";
                }
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
            { field: "cookieStr", title: "cookieStr", width: 100 },
            { field: "status", title: "状态", width: 50,formatter:function(val,src){
                switch (val){
                    case 1:
                        return "<b>正常</b>";
                    case 2:
                        return "<span style='color:gray'>暂停</span>";
                }
                return '-';
            } },
            { field: "errorMsg", title: "errorMsg", width: 100,formatter:function(val,src){

                if(val) {
                    return "<a title='" + val + "'>" + val + "</a>"
                }else{
                    return "";
                }

            } }

        );
        return columns;
    };
}
//主对话框类,继承Dg_Method的方法
function Dlg() {
    this.url = "/api/collectParam";
    this.init = Dlg_Method.init;
}


