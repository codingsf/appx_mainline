$(function(){
    var canvas = document.getElementById("mainCanvas");
    var stage = new Stage(canvas);
    var scene = new Scene(stage);
    scene.bgColor = "#fff";
    scene.wheelZoom = 1;
    scene.dragEnable = false;
    $.ajax({
        url:"./goodss",
        success:function(data){

            for(var i=0;i<data.length;i++){
                var goods = data[i];
                var node = new Node(goods.name+"("+goods.num+")");
                node.x = i*70+50;
                node.y = 50;
                scene.add(node);
            }
        }
    })

})
