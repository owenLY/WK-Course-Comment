
var tid=null;   //类型id
var degree=null;    //难度
var sort=null;  //排序名
var start=0;    //分页起始下标
var size=10;    //每页显示数量

var currPage=1; //当前页
var pageCount=0;    //页数


$('#degree-btn').click(function(){
    degree=$('#degree').val();
    if(degree=='Choose...')
        return;
    queryCourse(null);
});

$('#sort-btn').click(function(){
    sort=$('#sort').val();
    if(degree=='Choose...')
        return;
    queryCourse(null);
});

$('#all-btn').click(function(){
    tid=null;
    degree=null;
    sort=null;
    queryCourse(null);
});



function showcourse(courses){
    console.log(courses);
    $('#course-table').children('tr').remove();
    for(var i=0;i<courses.length;i++){
        var course=courses[i];
        if(i==0 || i==5){
            var tr='<tr id="tr'+i+'"></tr>';
            $('#course-table').append(tr);
        }

        var degree;
        switch (course.degree) {
            case 1:
                degree='初级';
                break;
            case 2:
                degree='中级';
                break;
            case 3:
                degree="高级";
                break;
        }

        var td=' <td>\n' +
            '                    <div  class=" course" style="height: 289px;width:222px;background-color: whitesmoke;">\n' +
            '                        <img src="'+course.image+'" class="img-fluid" style="height: 168px;' +
            '    ">\n' +
            '                        <div class="text-center">\n' +
            '                            <p><a class="fork-link" target="_blank" href="/wk/course/details/'+course.cid+'">'+course.name+'</a></p>\n' +
            '                            <p><span class="text-danger">￥'+course.price+'</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span class="text-info">'+degree+'</span></p>\n' +
            '                        </div>\n' +
            '                    </div>\n' +
            '                </td>';

        var id;
        if(i<5)
            id='#tr0';
        else
            id='#tr5';

        $(id).append(td);
    }
}

//查找课程
function queryCourse(data){
    if(data==null){
        data={"tid":tid,"degree":degree,"sort":sort,"start":start,"size":size};
    }

    var url="queryCourse";
    $.post({
        url:url,
        data:JSON.stringify(data),
        contentType:'application/json',
        success:function(result){
            dialog.msg(result.msg,0,3000, false);
            if(result.status=='SUCCESS') {
                showcourse(result.data);
                var total=parseInt(result.total);
                console.log('total\t'+total);
                pageUtil(total);
            }
        }
    });
}
//当页面第一次加载完成后，进行一次查找
queryCourse(null);


$(".type-link").each(function(){
    $(this).click(function(){
        tid=$(this).attr("data");
        var data={"tid":tid,"degree":degree,"sort":sort,"start":start,"size":size};
        queryCourse(data);
        return false;
    });
});




function pageUtil(total){
    $('#page').children('li').remove();
    pageCount=Math.ceil(total/size);
    console.log("pageCount:\t"+pageCount);

    $('#page').append('<li class="page-item">\n' +
        '                    <a class="page-link" href="javascript:void(0)"  id="prev" onclick="prevClick()">上一页</a>\n' +
        '                </li>');
    for(var i=1;i<=pageCount;i++){
        $('#page').append('<li class="page-item"><a class="page-link page-click" href="javascript:void(0)" id="'+i+'"  onclick="pageClick(this)">'+i+'</a></li>');
    }
    $('#page').append(' <li class="page-item">\n' +
        '                    <a class="page-link" href="javascript:void(0)"  id="next" onclick="nextClick()">下一页</a>\n' +
        '                </li>');

    $('#page'+currPage).parent('li').attr('page-item active');

}

var prevClick;
var pageClick;
var nextClick;

$(function(){

    prevClick=function(){
        if(currPage<=1){
            dialog.msg('已经是首页了',0,3000,false);
        }
        else {
             currPage--;
            start=(Number(currPage)-1)*Number(size);
            queryCourse(null);
        }
        return false;
    }

    nextClick=function(){
        if(currPage>=pageCount){
            dialog.msg("已经是最后一页了哦",0,3000,false);
        }
        else {
            currPage++;
            start=(Number(currPage)-1)*Number(size);
            queryCourse(null);
        }
        return false;
    }

    pageClick=function(e){
        var n=$(e).attr('id');
        if(n==currPage){
            return false;
        }
        currPage=Number(parseInt(n+""));
        start=(Number(currPage)-1)*Number(size);
        queryCourse(null);
    }

});

