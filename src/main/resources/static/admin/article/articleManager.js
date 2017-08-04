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
    this.url = "/api/articles";
    this.init = Dg_Method.init;
    this.getColumns = function () {
        var columns = new Array();
        columns.push(
            { field: "id", title: "ID", hidden: false, checkbox: true },
            { field: "title", title: "标题", width: 100,formatter:function(val,src){
                if(src.url){
                    return "<a target='_blank' href='"+src.url+"'>"+val+"</a>";
                }
                return val;
            } },

            { field: "articleGroupFlag", title: "分组", width: 100 },
            { field: "createTime", title: "创建时间", width: 100 },
            { field: "modifyTime", title: "修改时间", width: 100 },{
                field: "occTime", title: "发生时间", width: 100
            }
        );
        return columns;
    };
}

var ps = {
    onOpen:function(){
        $(".window").css("z-index","1001");
        $(".window-shadow").css("z-index","900");
    },
    onResize:function(){
        $(".window").css("z-index","1001");
        $(".window-shadow").css("z-index","900");
    },
    onMove:function(){
        $(".window").css("z-index","1001");
        $(".window-shadow").css("z-index","900");
    },
    modal:false
}
//主对话框类,继承Dg_Method的方法
function Dlg() {
    this.url = "/api/articles";
    this.init = Dlg_Method.init;
    this.resetDlg = function(){
        Dlg_Method.resetDlg.call(this,arguments);
        UE.getEditor('editor').setContent('');
    }
    this.getFormJson = function(){
        var fj = Dlg_Method.getFormJson.call(this);
        fj.content =UE.getEditor('editor').getContent();
        return fj;
    },

    this.showAdd = function(){
        Dlg_Method.showAdd.call(this,ps);
    }
    this.showModify = function(){
        Dlg_Method.showModify.call(this,ps);
    }
    this.loadForm = function(){
        //#TODO
        Dlg_Method.loadForm.call(this,arguments);
        var record = this.getSelectedRecord();
       // alert(record.content);
        setTimeout(function(){
            UE.getEditor('editor').setContent(record.content);
        },100)
    }

}


