$(function(){
   $('.video-li').hover(function(){
       $(this).children('button').show();
   },function(){
       $(this).children('button').hide();
   });


   $('#add2car').click(function(){
       var cid=$("#course_cid").attr("data");
       $.get({
           url:'/wk/pay/add2car/'+cid,
           success:function(result){
               dialog.msg(result.msg,0,3000,false);
           }
       });
   });
   return false;
});


<!--展示评论点击事件-->
$("#comment").click(function(){
    var cid=$("#course_cid").attr("data");
    console.log("cid:\t"+cid);
    $.get({
        url:'/wk/course/showComment/'+cid,
        success:function(result){
            dialog.msg(result.msg,0,3000,false);
            if(result.status=='SUCCESS'){
                showComment(result.data);
            }
        }
    });
});


//评论按钮点击处理函数
function submitComment(data){
    var mid=0;
    $.post({
        url:'/wk/course/submitComment',
        async:false, //修改此方法为同步
        data:JSON.stringify(data),
        contentType:'application/json',
        success:function(result) {
            dialog.msg(result.msg, 0, 3000, false);
            if (result.status=='SUCCESS') {
                mid = result.data;
            }
        }
    });
    return mid;
}

//将时间毫秒数格式化
function changeTime(time){
    var commonTime = "";
    if(time){
        var unixTimestamp = new Date(time*1) ;
        commonTime = unixTimestamp.toLocaleString();
    }
    return commonTime;
}


//展示评论
function showComment(comments){
    $(".comment-show").children(".comment-show-con").remove();
    for(var i=0;i<comments.length;i++){
        var div=' <div class="comment-show-con clearfix">\n' +
            '                                    <div class="comment-show-con-img pull-left">' +
            '<img class="img-fluid rounded-circle" style="width: 50px;height: 50px" src="'+comments[i].user.avatar+'" alt=""></div>\n' +
            '                                    <div class="comment-show-con-list pull-left clearfix">\n' +
            '                                        <div class="pl-text clearfix">\n' +
            '                                            <a href="#" class="comment-size-name">'+comments[i].user.nickname+' : </a>\n' +
            '                                            <span class="my-pl-con">&nbsp;'+comments[i].content+'</span>\n' +
            '                                        </div>\n' +
            '                                        <div class="date-dz">\n' +
            '                                            <span class="date-dz-left pull-left comment-time">'+new Date(comments[i].add_time)+'</span>\n' +
            '                                            <div class="date-dz-right pull-right comment-pl-block">\n' +
            '                                                <a href="javascript:;" data="'+comments[i].mid+'" class="date-dz-z pull-left"><i class="date-dz-z-click-red"></i>赞 (<i class="z-num">'+comments[i].like_count+'</i>)</a>\n' +
            '                                            </div>\n' +
            '                                        </div>\n' +
            '                                        <div class="hf-list-con"></div>\n' +
            '                                    </div>\n' +
            '                                </div>';

        $(".comment-show").append(div);
    }
}


<!--展示笔记-->
$('#note').click(function(){
    var cid=$('#course_cid').attr("data");
    $.get({
        url:'/wk/course/showNote/'+cid,
        success:function (result) {
            dialog.msg(result.msg,0,3000,false);
            if(result.status == 'SUCCESS') {
                console.log("test");
                showNote(result.data);
            }
        }
    });
});

<!--展示笔记-->
function showNote(notes){
    console.log("test");
    $('#note_wrap').children().remove();
    if(notes.length==0){
        $('#note_wrap').append('<h3 class="font-italic" style="color:orangered;">暂时没有任何笔记哦!</h3>');
        return;
    }

    console.log("test");
    for(var i=0;i<notes.length;i++){
        var div=' <li class="list-group-item">\n' +
            '                    <div class="card">\n' +
            '                        <div class="card-header">\n' +
            '                            <h5><span><img src="'+notes[i].user.avatar+'" class="rounded-circle img-fluid"/></span>'+notes[i].user.nickname+'</h5>\n' +
            '                        </div>\n' +
            '                        <div class="card-body">\n' +
            '                            <p>'+notes[i].content+'</p>\n' +
            '                        </div>\n' +
            '                    </div>\n' +
            '                </li>';

        $('#note_wrap').append(div);
    }
}


<!--textarea高度自适应-->
$(function () {
    $('.content').flexText();
});
<!--textarea限制字数-->

function keyUP(t){
    var len = $(t).val().length;
    if(len > 139){
        $(t).val($(t).val().substring(0,140));
    }
}

<!--点击评论创建评论条-->
$('.commentAll').on('click','.plBtn',function(){
    var uid=$("#user_uid").attr('data');
    var cid=$("#course_cid").attr("data");
    var nickname=$("#user_nickname").attr("data");
    var avatar=$("#user_avatar").attr("data");
    if(!uid){
        dialog.msg('评论请先登录!',0,3000,false);
        return false;
    }

    var myDate = new Date();
    //获取当前年
    var year=myDate.getFullYear();
    //获取当前月
    var month=myDate.getMonth()+1;
    //获取当前日
    var date=myDate.getDate();
    var h=myDate.getHours();       //获取当前小时数(0-23)
    var m=myDate.getMinutes();     //获取当前分钟数(0-59)
    if(m<10) m = '0' + m;
    var s=myDate.getSeconds();
    if(s<10) s = '0' + s;
    var now=year+'-'+month+"-"+date+" "+h+':'+m+":"+s;
    //获取输入内容
    var oSize = $(this).siblings('.flex-text-wrap').find('.comment-input').val();
    console.log(oSize);

    if(oSize==null || oSize==''){
        dialog.msg('评价内容不能为空',0,3000,false);
        return false;
    }

    var data={'cid':cid,'uid':uid,'content':oSize,'add_time':now};
    var mid=submitComment(data);
    console.log("mid:\t"+mid);
    if(mid==0){
        return false;
    }


    //动态创建评论模块
    oHtml = '<div class="comment-show-con clearfix"><div class="comment-show-con-img pull-left"><img src="'+avatar+'" alt="" class="rounded-circle img-fluid"></div> <div class="comment-show-con-list pull-left clearfix"><div class="pl-text clearfix"> <a href="#" class="comment-size-name">'+nickname+' : </a> <span class="my-pl-con">&nbsp;'+ oSize +'</span> </div> <div class="date-dz"> <span class="date-dz-left pull-left comment-time">'+now+'</span> <div class="date-dz-right pull-right comment-pl-block">   <a href="javascript:;" data="'+mid+'" class="date-dz-z pull-left"><i class="date-dz-z-click-red"></i>赞 (<i class="z-num">0</i>)</a> </div> </div><div class="hf-list-con"></div></div> </div>';
    if(oSize.replace(/(^\s*)|(\s*$)/g, "") != ''){
        $(this).parents('.reviewArea ').siblings('.comment-show').prepend(oHtml);
        $(this).siblings('.flex-text-wrap').find('.comment-input').prop('value','').siblings('pre').find('span').text('');
    }
});

<!--点赞-->
$('.comment-show').on('click','.date-dz-z',function(){
    var user_uid=$('#user_uid').attr('data');
    if(!user_uid){
        dialog.msg('请先登录!',0,3000,false);
        return false;
    }
    var zNum = $(this).find('.z-num').html();
    var mid=$(this).attr('data');
    var cid=$('#course_cid').attr('data');
    if($(this).is('.date-dz-z-click')){
        zNum--;
        $(this).removeClass('date-dz-z-click red');
        $(this).find('.z-num').html(zNum);
        $(this).find('.date-dz-z-click-red').removeClass('red');
        $.get({
            url:'/wk/course/comment/unlike/'+cid+'/'+mid
        });
    }else {
        zNum++;
        $(this).addClass('date-dz-z-click');
        $(this).find('.z-num').html(zNum);
        $(this).find('.date-dz-z-click-red').addClass('red');
        $.get({
            url:'/wk/course/comment/like/'+cid+'/'+mid
        });
    }
})