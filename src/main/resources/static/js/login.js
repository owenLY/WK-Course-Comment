$(function(){
    //将表单数据转为json字符串
    function form2Json(formId){
        var paramArray=$("#"+formId).serializeArray();
        var data={};
        $(paramArray).each(function(){
            data[this.name]=this.value;
        });
        return JSON.stringify(data);
    }


    $("#login-submit").click(function(){
        $.post({
            url:"user/login",
            data:form2Json("login-form"),
            contentType:'application/json',
            success:function(result){
                console.log(result);
                if(result.status=="SUCCESS") {
                    dialog.msg("登录成功,3秒后跳转", null, 0, setInterval(function(){
                        window.location.href="index.html";
                    }),5000);
                }
                else
                    dialog.msg("登录失败",null,3000,false);
            }
        });
        return false;
    });
});