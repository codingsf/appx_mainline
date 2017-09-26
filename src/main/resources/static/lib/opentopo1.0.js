
function Stage(canvas){
    window.stage = this;
    this.canvas = canvas;
    this.ctx =canvas.getContext("2d");
    //this.ctx.fontStyle = "微软雅黑"
    var me = this;
    this.curScene = null;

    //屏蔽canvas的右键菜单
    canvas.oncontextmenu = function(ev){
        ev = ev || event;
        ev.returnValue = false;
    }

    canvas.onmousedown = function(ev){
        ev = ev || event;
        var px = (ev.clientX -canvas.offsetLeft+document.body.scrollLeft - canvas.width / 2) / me.curScene.scaleX + canvas.width / 2 - me.curScene.translateX;
        var py = (ev.clientY -canvas.offsetTop+document.body.scrollTop - canvas.height / 2) / me.curScene.scaleY + canvas.height / 2 - me.curScene.translateY;
        if(me.curScene.onClick){
            me.curScene.onClick({
                event:ev,
                pointX:px,
                pointY:py
            });
        }

        //var t = new TextNode(px+","+py);
        //t.setLocation(px,py);
        //me.curScene.add(t);

        me.curScene.currentElement = null;

        me.curScene.childs.forEach(function(ele){
            if(ele.elementType == "node" || ele.elementType=="container") {
                if (ele.inBound(px, py)) {
                    ele.selected = true;
                    me.curScene.currentElement = ele;
                    ele.pointX = px - ele.x;
                    ele.pointY = py - ele.y;
                } else {
                    if(!ev.ctrlKey){
                        ele.selected = false;
                    }
                }
            }
        });
        if(me.curScene.currentElement){
            me.canvas.style.cursor = "move";
            var curEle = me.curScene.currentElement;
            if(curEle.onClick){
                curEle.onClick({
                    event:ev,
                    pointX:curEle.pointX,
                    pointY:curEle.pointY
                })
            }
        }else{
            me.canvas.style.cursor = "pointer";
        }

        canvas.onmousemove = function(ev){
            ev = ev || event;

            canvas.mousemoveEventObj = ev;

            //var px2 = (ev.clientX -canvas.offsetLeft - canvas.width / 2) / me.curScene.scaleX + canvas.width / 2 - me.curScene.translateX;
            //var py2 = (ev.clientY -canvas.offsetTop - canvas.height / 2) / me.curScene.scaleY + canvas.height / 2 - me.curScene.translateY;
            var px2 = (ev.clientX -canvas.offsetLeft+document.body.scrollLeft - canvas.width / 2) / me.curScene.scaleX + canvas.width / 2 - me.curScene.translateX;
            var py2 = (ev.clientY -canvas.offsetTop+document.body.scrollTop - canvas.height / 2) / me.curScene.scaleY + canvas.height / 2 - me.curScene.translateY;
            var curEle = me.curScene.currentElement;

            canvas.px2 =px2;
            canvas.py2 = py2;


            if(curEle){
                var dx = px2-curEle.pointX;
                var dy = py2-curEle.pointY;
                if(curEle.childs){
                    curEle.childs.forEach(function(ele){
                        ele.x+=dx-curEle.x;
                        ele.y+=dy-curEle.y;
                    });
                }
                curEle.x = dx;
                curEle.y = dy;


            }else{
                if(me.curScene.dragEnable){
                    me.curScene.translateX +=px2-px;
                    me.curScene.translateY +=py2-py;
                }

            }
        }
        document.onmouseup = function(){
            canvas.onmousemove = null;
            canvas.onmouseup = null;
            me.canvas.style.cursor = "default";

        }
    };
    canvas.onmousewheel = function(ev){
        var ev = ev || window.event;
        if(ev.deltaY>0){
            me.curScene.scaleY *= me.curScene.wheelZoom;
            me.curScene.scaleX *= me.curScene.wheelZoom;
        }else{
            me.curScene.scaleY /= me.curScene.wheelZoom;
            me.curScene.scaleX /= me.curScene.wheelZoom;
        }
        //ev.preventDefault();
    }

    document.onmousemove = function(ev){
        ev = ev || event;
        me.curScene.focus = null;
        var px = (ev.clientX -canvas.offsetLeft+document.body.scrollLeft - canvas.width / 2) / me.curScene.scaleX + canvas.width / 2 - me.curScene.translateX;
        var py = (ev.clientY -canvas.offsetTop+document.body.scrollTop - canvas.height / 2) / me.curScene.scaleY + canvas.height / 2 - me.curScene.translateY;


        me.curScene.childs.forEach(function(ele){
            if(ele.elementType == "node" || ele.elementType == "link"){
                if(ele.inBound(px,py)){
                    me.curScene.focus = ele;
                }
            }

        });
    }


    void function(){
        if(me.curScene){
            //  me.canvas.height = parseInt(me.canvas.style.height)/me.curScene.scaleX;
            // me.canvas.width = parseInt(me.canvas.style.width)/me.curScene.scaleY;
            me.curScene.paint(me.ctx);
        }
        setTimeout(arguments.callee,20);
    }();
}
function Scene(stage){
    stage.curScene = this;
    this.stage = stage;
    this.childs = [];
    this.scaleX = 1;
    this.scaleY = 1;
    this.translateX = 0;
    this.translateY = 0;
    this.bgColor = "gray";
    this.alpha = 255;
    this.wheelZoom = 0.95;
    this.dragEnable = true;//是否可以拖拽
    this.onClick = function(params){
        console.log(params);
    }
    this.dbClick = function(params){
        console.log(params);
    }

    this.sort = function(){
        this.childs.sort(function(a,b){
            function ab(ele){
                switch(ele.elementType){
                    case "container":
                        return 0;
                    case "link":
                        return 1;
                    case "node":
                    case "text":
                        return 3;
                }
            }
            return ab(a)-ab(b);
        });
    }
    this.add = function(ele){
        if(this.childs.indexOf(ele)==-1){
            this.childs.push(ele);
        }
        ele.scene = this;
        this.sort();
    }

    this.paint = function(ctx){
        var canvas = this.stage.canvas;
        var ctx = this.stage.ctx;

        this.childs.forEach(function(ele){
            if(ele.elementType == "container"){
                ele.childs.forEach(function(node){
                    if(node.x<ele.x){
                        node.x = ele.x;
                    }else if(node.x+node.width>ele.x+ele.width){
                        node.x = ele.x+ele.width-node.width;
                    }
                    if(node.y<ele.y){
                        node.y = ele.y;
                    }
                    else if(node.y+node.height>ele.y+ele.height){
                        node.y = ele.y+ele.height-node.height;
                    }
                });
            }
        });


        ctx.save();

        //清除背景
        ctx.save();
        ctx.fillStyle = this.bgColor;
        ctx.fillRect(0,0,canvas.width,canvas.height);
        ctx.restore();

        ctx.translate(this.translateX,this.translateY);

        ctx.translate(canvas.width/2-this.translateX,canvas.height/2-this.translateY);
        ctx.scale(this.scaleX,this.scaleY);
        ctx.fillStyle = "black";
        var wh = 10;
        ctx.fillRect(-wh/2,-wh/2,wh,wh);

        ctx.translate(-canvas.width/2+this.translateX,-canvas.height/2+this.translateY);

        this.childs.forEach(function(ele) {
            if (ele.elementType == "node") {
                ctx.save();
                ctx.translate(ele.x, ele.y);
                ele.paint(ctx);
                ctx.restore();
            } else {
                ele.paint(ctx);
            }
        });
        // ctx.dashedLineTo(10,20,200,200);
        ctx.restore();

        //    var img = new Image();
        //      img.src = "fhq.png";
//        ctx.drawImage(img,100,100,200,200);
    }
}
OT = {};
imageCache = {};
function clone(arr){
    var a=[]; for(var i=0,l=arr.length;i<l;i++) a.push(arr[i]); return a;
}
void function (OT){
    var canvas_temp =document.createElement("canvas");
    var ctx_temp =canvas_temp.getContext("2d");
    function getImageByFilter(image,filterName,alpha){
        if(!image.width || !image.height){
            //图片还没有加载完成
            return null;
        }

        var cacheName = image.src+filterName;
        if(window.imageCache[cacheName]){
            return window.imageCache[cacheName];
        }

        canvas_temp.width = image.width;
        canvas_temp.height = image.height;
        ctx_temp.clearRect(0,0,canvas_temp.width,canvas_temp.height);
        ctx_temp.drawImage(image,0,0);
        var j = ctx_temp.getImageData(0,0,image.width,image.height);
        var k = j.data;
        var k_temp = clone(k);
        if(filterName == "mirror"){//镜像
            for(var x=0;x<image.width;x++) {
                for (var y = 0; y < image.height; y++) {
                    //   k[midx + 3] = alpha;
                    var n = 4 * (x + y * image.width);
                    var midx = (((image.width -1) - x) + y * image.width) * 4;
                    k[midx] = k_temp[n];
                    k[midx+1] = k_temp[n+1];
                    k[midx+2] = k_temp[n+2];
                    k[midx+3] = k_temp[n+3];
                }
            }

        }else{
            for(var x=0;x<image.width;x++){
                for(var y=0;y<image.height;y++){
                    var n = 4 * (x + y * image.width);
                    // k[n+3] =alpha;
                    switch (filterName){
                        case "inverse":
                            k[n] = 255-k[n];
                            k[n+1] = 255-k[n+1];
                            k[n+2] = 255-k[n+2];
                            break;
                        case "gray":
                            var grey = k[n] * 0.3 + k[n + 1] * 0.59 + k[n + 2] * 0.11;
                            k[n] = k[n + 1] = k[n + 2] = grey;
                            break;
                    }

                }
            }

        }

        ctx_temp.putImageData(j, 0, 0, 0, 0, canvas_temp.width, canvas_temp.height);
        var o = canvas_temp.toDataURL();
        var image0 = new Image();
        image0.src = o;
        window.imageCache[cacheName] = image0;
        return image0;
    }
    window.OT.util = {
        getImageByFilter:getImageByFilter
    }
}();


function Node(text){
    this.elementType = "node";
    this.scene = null;
    this.text = text;
    this.x = 0;
    this.y = 0;
    this.width = 50;
    this.height = 50;
    this.fillStyle ="rgba(0,0,255,1)";
    this.fontStyle = "black";
    this.font = "10px 微软雅黑";
    this.borderWidth = 10;
    this.image = null;
    this.alpha = 255;
    this.imageFilterName = "normal";//inverse:反色;gray:灰色;mirror:镜像
    this.tooltipText = null;//提示文本
    this.tooltipAlway = false;//总是显示提示文本

    this.onClick = function(params){
        console.log(params);

    }
    this.onDbClick = function(params){

    }
    this.onFocus = function(params){

    }

    this.inBound = function(pointX,pointY){
        return pointX>=this.x && pointX<=this.x+this.width && pointY>=this.y&&pointY<=this.y+this.height;
    };
    this.setImage = function(url){
        var img = new Image();
        img.onload = function(){
            img.loadSuccess = true;
        }
        img.src = url;
        this.image = img;
    }
    this.setLocation = function(x,y){
        this.x = x;
        this.y = y;
    }
    this.setBound = function(x,y,width,height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
    this.paint = function(ctx){
        ctx.save();
        //画外框
        this.paintBorder(ctx);
        //画内容

        var image = null;
        if(this.image && this.image.loadSuccess){
            image = OT.util.getImageByFilter(this.image,this.imageFilterName,this.alpha);
        }
        if(image){
            ctx.drawImage(image,0,0,this.width,this.height);
        }else{
            ctx.fillStyle = this.fillStyle;
            ctx.fillRect(0,0,this.width,this.height);
        }

        //画文本
        this.paintText(ctx);

        if((this.scene.focus == this || this.tooltipAlway)&&this.tooltipText){
            this.paintTooltip(ctx);
        }
        ctx.restore();
    }

    this.paintTooltip = function(ctx){
        ctx.save();
        ctx.fillStyle = "white"


        ctx.strokeStyle = "white";
        ctx.translate(this.width/2,0);

        var textWidth = ctx.measureText(this.tooltipText).width;
        var textHeight = ctx.measureText("田").width;
        ctx.beginPath();
        ctx.lineJoin = "round";
        ctx.lineCap = "round"
        ctx.moveTo(0,-1);
        ctx.lineTo(-5,-5);
        ctx.lineTo(-textWidth/2-10,-5);
        ctx.lineTo(-textWidth/2-10,-5-textHeight-10);
        ctx.lineTo(textWidth/2+10,-5-textHeight-10);
        ctx.lineTo(textWidth/2+10,-5);
        ctx.lineTo(2,-5);
        ctx.lineTo(0,-1);
        ctx.stroke();
        ctx.closePath();
        // ctx.fill();
        ctx.fillStyle = "rgba(0,100,0,0.5)";
        ctx.fill();
        ctx.fillStyle = "red";
        ctx.fillText(this.tooltipText,-textWidth/2,-11);

        ctx.restore();
    }

    this.paintBorder = function(ctx){
        var alp = 0;
        if(this.selected){
            alp+=0.5;
        }
        if(this.scene.focus == this){
            alp += 0.5;
        }
        if(alp){
            ctx.fillStyle = "rgba(200,200,200,"+alp+")";
            ctx.fillRect(-this.borderWidth / 2, -this.borderWidth / 2, this.width + this.borderWidth, this.height + this.borderWidth);
        }
    }
    this.paintText = function(ctx){
        if(this.text) {
            ctx.save();
            ctx.translate(this.width / 2, this.height + this.borderWidth / 2);
            ctx.fillStyle = this.fontStyle;

            ctx.textAlign = "center";
            ctx.font = this.font;
            ctx.textBaseline = "top";
            ctx.fillText(this.text, 0, 0);

            ctx.restore();
        }
    }

}
function Link(nodeA,nodeZ){
    this.elementType = "link";
    this.nodeA = nodeA;
    this.nodeZ = nodeZ;
    this.stokeStyle = "red";
    this.textStyle = "blue";
    this.lineWidth = 2;
    this.scene = null;
    this.text = null;
    this.arrowsRadius = 10;
    this.dashedPattern = null;//虚线
    this.fold = false;//折线
    this.direction = null;//方向,vertical为垂直方向,其它为水平方向
    this.vertical = false;
    this.inBound = function(pointX,pointY){

        var sPort = {
            x:this.nodeA.x+this.nodeA.width/2,
            y:this.nodeA.y+this.nodeA.height/2
        }
        var dPort = {
            x:this.nodeZ.x+this.nodeZ.width/2,
            y:this.nodeZ.y+this.nodeZ.height/2
        }

        var cPort = null;
        if(this.fold){
            if(this.direction && this.direction=="vertical"){
                cPort = {
                    x:dPort.x,
                    y:sPort.y
                }
            }else{
                cPort = {
                    x:sPort.x,
                    y:dPort.y
                }
            }
        }

        //不太精细

        if(cPort){
            var ju =PointLine_Disp(pointX,pointY,sPort.x,sPort.y,cPort.x,cPort.y);
            if(ju<3){
                return true;
            }
            ju = PointLine_Disp(pointX,pointY,cPort.x,cPort.y,dPort.x,dPort.y);
            if(ju<3){
                return true;
            }
        }else{
            var ju = PointLine_Disp(pointX,pointY,sPort.x,sPort.y,dPort.x,dPort.y);
            if(ju<3){
                return true;
            }
        }

        return false;
    };
    this.drawLine = function(dotXY, ops) {
        this.ctx.lineWidth = 10;
        this.ctx.beginPath();
        for (var att in ops) this.ctx[att] = ops[att];
        dotXY = dotXY.constructor == Object ? [dotXY || { x: 0, y: 0}] : dotXY;
        this.ctx.moveTo(dotXY[0].x, dotXY[0].y);
        for (var i = 1, len = dotXY.length; i < len; i++) this.ctx.lineTo(dotXY[i].x, dotXY[i].y);
        this.ctx.stroke();
    };
    this.paint = function(){
        var ctx = this.scene.stage.ctx;
        var canvas = this.scene.stage.canvas;
        var sPort ={
            x:this.nodeA.x+this.nodeA.width/2,
            y:this.nodeA.y+this.nodeA.height/2
        };

        var dPort = {
            x:this.nodeZ.x+this.nodeZ.width/2,
            y:this.nodeZ.y+this.nodeZ.height/2
        }
        var cPort = sPort;

        if(this.fold){
            if(this.direction && this.direction=="vertical"){
                cPort = {
                    x:dPort.x,
                    y:sPort.y
                }
            }else{
                cPort = {
                    x:sPort.x,
                    y:dPort.y
                }
            }

        }

        if(this.arrowsRadius) {
            var xju = dPort.x - cPort.x;
            var yju = dPort.y - cPort.y;

            var westPoint = {//西边
                x: this.nodeZ.x,
                y: cPort.y+ (yju / xju) * (xju - this.nodeZ.width / 2)
            }
            var eastPoint = {//东边
                x: this.nodeZ.x + this.nodeZ.width,
                y: cPort.y + (yju / xju) * (xju + this.nodeZ.width / 2)
            }

            var northPoint = {//北边重叠
                x: cPort.x + (xju / yju) * (yju - this.nodeZ.height / 2),
                y: this.nodeZ.y,
            }

            var sourthPoint = {//南边
                x: cPort.x + (xju / yju) * (yju + this.nodeZ.height / 2),
                y: this.nodeZ.y + this.nodeZ.height
            }


            var pos = [westPoint, eastPoint, northPoint, sourthPoint];
            for (var i = 0; i < pos.length; i++) {
                var point = pos[i];
                if (
                    point.x >= this.nodeZ.x
                    && point.x <= this.nodeZ.x + this.nodeZ.width
                    && point.y >= this.nodeZ.y
                    && point.y <= this.nodeZ.y + this.nodeZ.height
                ) {
                    if (Math.abs(dPort.x - cPort.x) + Math.abs(dPort.y - cPort.y) > Math.abs(point.x - cPort.x) + Math.abs(point.y - cPort.y)) {
                        dPort = point;
                    }
                }
            }
        }



        ctx.save();
        //画线

        ctx.beginPath();
        ctx.lineWidth = this.lineWidth;
        ctx.strokeStyle = this.stokeStyle;

        if(this.scene.focus == this){
            ctx.shadowBlur = 5;
            ctx.shadowColor="black";
        }
        ctx.moveTo(sPort.x,sPort.y);

        if(this.fold){
            ctx.dashedLineTo(sPort.x,sPort.y,cPort.x,cPort.y,this.dashedPattern);
            sPort = {
                x:cPort.x,
                y:cPort.y
            }
        }
        ctx.dashedLineTo(sPort.x,sPort.y,dPort.x,dPort.y,this.dashedPattern);

        ctx.stroke();
        ctx.closePath();
        /*        if(ctx.isPointInPath(0,0)){
         alert();
         }*/

        //画箭头
        if(this.arrowsRadius) {
            ctx.save();

            ctx.beginPath();
            ctx.strokeStyle = this.strokeStyle;
            ctx.lineWidth = this.lineWidth;
            ctx.translate(dPort.x, dPort.y);
            ctx.rotate(Math.atan2(dPort.y - sPort.y, dPort.x - sPort.x));
            ctx.moveTo(-this.arrowsRadius, this.arrowsRadius / 2);
            ctx.lineTo(0, 0);
            ctx.lineTo(-this.arrowsRadius, -this.arrowsRadius / 2);
            ctx.stroke();
            ctx.closePath();
            ctx.restore();

        }




//画文本
        if(this.text) {
            var lx = (sPort.x + dPort.x) / 2;
            var ly = (sPort.y + dPort.y) / 2;
            ctx.translate(lx,ly);
            ctx.rotate(Math.atan2(dPort.y-sPort.y,dPort.x-sPort.x));
            ctx.textAlign = "center";
            ctx.fillStyle = this.textStyle;
            ctx.fillText(this.text,0,0);
        }


        ctx.restore();


    }
}

function TextNode(text){
    this.elementType = "text";
    this.text = text;
    this.x = null;
    this.y = null;
    this.fillStyle = "white";
    this.scene = null;
    this.position = "absolute";
    this.setLocation = function(x,y){
        this.x = x;
        this.y = y;
    }
    this.paint = function(){
        var ctx = this.scene.stage.ctx;
        var canvas = this.scene.stage.canvas;

        ctx.save();
        ctx.fillStyle = this.fillStyle;
        ctx.textBaseline = "top"

        var x = this.x;
        var y = this.y;
        if(this.position == "flex"){
            x = (this.x - canvas.width / 2) / this.scene.scaleX + canvas.width / 2 - this.scene.translateX;
            y = (this.y - canvas.height / 2) / this.scene.scaleY + canvas.height / 2 - this.scene.translateY;
        }

        ctx.fillText(this.text,x,y);
        ctx.restore();
    }
}

function Container(text){
    this.elementType = "container";
    this.text = text;
    this.x = null;
    this.y = null;
    this.width = null;
    this.height = null;
    this.childs = [];
    this.scene = null;
    this.bgColor ="rgba(10,10,100,0.6)";
    this.textStyle = "white",
        this.add = function(ele){
            this.childs.push(ele);
        }
    this.setBound = function(x,y,width,height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
    this.paint = function(){
        var ctx = this.scene.stage.ctx;
        ctx.save();
        ctx.fillStyle = this.bgColor;
        ctx.translate(this.x,this.y);
        ctx.fillRect(0,0,this.width,this.height);
        if(this.text){
            ctx.fillStyle = this.textStyle;
            ctx.fillText(this.text,0,0);
        }

        ctx.restore();
    }
    this.inBound = function(pointX,pointY){
        return pointX>=this.x && pointX<=this.x+this.width && pointY>=this.y&&pointY<=this.y+this.height;
    }
}
