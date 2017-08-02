function initWS(){
    ws = new WebSocket("ws://"+document.location.host+"/chat");
    ws.onopen = function(){
        $.ajax({
            method:"POST",
            url:"/api/users/icard",
            success:function(result){
                var msg = {
                    type:"init",
                    data:{
                        icard :result.icard
                    }
                };
                ws.send(JSON.stringify(msg));
            },
            error:function(){
                ws.send(JSON.stringify({type:"init",data:{}}));
            }
        });

        console.log("open success")
        layer.msg("聊天器连接服务器成功");
    }
    ws.onerror = function(){
        ws.close();
    }
    ws.onclose = function(){
       // ws.close();
        layer.msg("连接被关闭,3秒后重连");
        setTimeout(function(){
            initWS();
        },3000);
    };
    ws.onmessage = function(event){
        var raw_data = event.data;
        console.log(raw_data)
        var msg = eval("("+raw_data+")");
        switch (msg.type){
            case "chat":
                var data = msg.data;
                layim.getMessage({
                    username: data.userNickname,
                    avatar: data.avatar,
                    id: data.type == "friend" ? data.userId : data.target,
                    type: data.type,
                    content: data.content
                });
                break;
            case "onlinesInfo":
                var data = msg.data;
                ///#TODO 暂时解决方案
                setTimeout(function(){
                    for(var i=0;i<data.list.length;i++){
                        var u = data.list[i];
                        layim.addList({
                            type: 'friend'
                            ,avatar: u.avatar
                            ,username: u.username
                            ,groupid: 0
                            ,id: u.id
                        });
                    }
                },2000);

                break;
            case "userLeave":
                var data= msg.data;
                layim.removeList({
                    type:"friend",
                    id:data.id
                });
                break;
            case "userOnline":
                var u = msg.data;
                layim.addList({
                    type: 'friend'
                    ,avatar: u.avatar
                    ,username: u.username
                    ,groupid: 0
                    ,id: u.id
                });
                break;
            case "recInit":
                var u = msg.data;
               // alert("本机用户:"+u.nickname);
                break;

        }

    }
}

layui.use('layim', function(layim){
    window.layim = layim;
    //基础配置
    layim.config({
        //初始化接口
        init: {
            url: '/api/chat_user/getInitList'
            ,data: {}
        }
        //查看群员接口
        ,members: {
            url: 'json/getMembers.json'
            ,data: {}
        }

        //上传图片接口
        ,uploadImage: {
            url: '/upload/image' //（返回的数据格式见下文）
            ,type: '' //默认post
        }

        //上传文件接口
        ,uploadFile: {
            url: '/upload/file' //（返回的数据格式见下文）
            ,type: '' //默认post
        }

        //扩展工具栏
        ,tool: [{
            alias: 'code'
            ,title: '代码'
            ,icon: '&#xe64e;'
        }]

        //,brief: true //是否简约模式（若开启则不显示主面板）

        ,title: '燎火聊天器' //自定义主面板最小化时的标题
        //,right: '100px' //主面板相对浏览器右侧距离
        //,minRight: '90px' //聊天面板最小化时相对浏览器右侧距离
        ,initSkin: '5.jpg' //1-5 设置初始背景
        //,skin: ['aaa.jpg'] //新增皮肤
        //,isfriend: false //是否开启好友
        //,isgroup: false //是否开启群组
        ,min: false //是否最小化主面板，默认false
        ,notice: true //是否开启桌面消息提醒，默认false
        ,voice:  'default.wav'//声音提醒，默认开启，声音文件为：default.wav

        ,msgbox: layui.cache.dir + 'css/modules/layim/html/msgbox.html' //消息盒子页面地址，若不开启，剔除该项即可
        ,find: layui.cache.dir + 'css/modules/layim/html/find.html' //发现页面地址，若不开启，剔除该项即可
        ,chatLog: layui.cache.dir + 'css/modules/layim/html/chatLog.html' //聊天记录页面地址，若不开启，剔除该项即可

    });

    /*
     layim.chat({
     name: '在线客服-小苍'
     ,type: 'kefu'
     ,avatar: 'http://tva3.sinaimg.cn/crop.0.0.180.180.180/7f5f6861jw1e8qgp5bmzyj2050050aa8.jpg'
     ,id: -1
     });
     layim.chat({
     name: '在线客服-心心'
     ,type: 'kefu'
     ,avatar: 'http://tva1.sinaimg.cn/crop.219.144.555.555.180/0068iARejw8esk724mra6j30rs0rstap.jpg'
     ,id: -2
     });
     layim.setChatMin();*/

    //监听在线状态的切换事件
    layim.on('online', function(data){
        //console.log(data);
    });

    //监听签名修改
    layim.on('sign', function(value){
        //console.log(value);
    });

    //监听自定义工具栏点击，以添加代码为例
    layim.on('tool(code)', function(insert){
        layer.prompt({
            title: '插入代码'
            ,formType: 2
            ,shade: 0
        }, function(text, index){
            layer.close(index);
            insert('[pre class=layui-code]' + text + '[/pre]'); //将内容插入到编辑器
        });
    });

    //监听layim建立就绪
    layim.on('ready', function(res){
        //layim.msgbox(0); //模拟消息盒子有新消息，实际使用时，一般是动态获得
        //添加好友（如果检测到该socket）


    });

    //监听发送消息
    layim.on('sendMessage', function(data){
        var params = {
            type:"chat",
            data: {
                target: data.to.id,
                content: data.mine.content,
                type: data.to.type
            }
        }
        ws.send(JSON.stringify(params));
        return false;
    });

    //监听查看群员
    layim.on('members', function(data){
        //console.log(data);
    });

    //监听聊天窗口的切换
    layim.on('chatChange', function(res){
        var type = res.data.type;
        console.log(res.data.id)
        if(type === 'friend'){
            //模拟标注好友状态
            //layim.setChatStatus('<span style="color:#FF5722;">在线</span>');
        } else if(type === 'group'){
            //模拟系统消息
            /*  layim.getMessage({
             system: true
             ,id: res.data.id
             ,type: "group"
             ,content: '模拟群员'+(Math.random()*100|0) + '加入群聊'
             });*/
        }
    });

});
initWS();