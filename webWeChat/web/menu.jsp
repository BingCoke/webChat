<%--
  Created by IntelliJ IDEA.
  User: Z
  Date: 21/4/13
  Time: 19:42
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>$</title>
    <link rel="stylesheet" href="/static/layui/css/layui.css">
</head>
<style>
    .dis{
        display: none;
    }
    .current{
        display: block;
    }
</style>
<body>

<ul class="layui-nav" >
    <li class="layui-nav-item" >
        <a class="nav" >聊天<span class="layui-badge">9</span></a>
    </li>
    <li class="layui-nav-item">
        <a class="nav" >通讯录<span class="layui-badge-dot"></span></a>
    </li>
    <li class="layui-nav-item">
        <a class="nav" >朋友圈<span class="layui-badge-dot"></span></a>
    </li>
    <li class="layui-nav-item " >
        <a id="my"></a>
        <dl class="layui-nav-child">
            <dd><a>修改个人信息</a></dd>
            <dd><a href="javascript:;">安全管理</a></dd>
            <dd><a id="exit" href="javascript:void(0)">退了</a></dd>
        </dl>
    </li>
</ul>
<%--聊天相关的界面--%>
<div class="current nav-div">
    1
</div>

<%--通讯录相关的界面--%>
<div class="dis nav-div">
    <div class="layui-tab">
        <ul class="layui-tab-title">
            <li class="layui-this">好友</li>
            <li>群</li>
            <li>添加好友</li>
            <li>添加/创建群</li>
            <li>好友申请</li>
        </ul>
        <div class="layui-tab-content">
            <%--      当前的好友 --%>
            <div class="layui-tab-item layui-show">当前的好友</div>
            <%--      当前的群          --%>
            <div class="layui-tab-item">群</div>
            <%--      好友添加          --%>
            <div class="layui-tab-item">添加好友
            <input maxlength="10" id="findUser"><br>
                <button id="findByUsername" type="button">根据用户名查找</button>
                <button id="findByName" type="button">根据名字查找</button>
                <table>
                    <thead>
                    <tr>
                        <th></th>
                        <th>用户名</th>
                        <th>名称</th>
                        <th>查看</th>
                    </tr>
                    </thead>
                    <tbody id="usersTable">
                    </tbody>
                </table>
                总页数 : <div id="nowTotalPages"></div>
                每页数 : <input type="text" id="nowDataSize" value="1">
                当前页 : <input type="text" id="nowCurrentPage" value="1"><button id="change">改变</button><br>
                <button id="lastPage">上一页</button>
                <button id="nextPage">下一页</button>

                <form style="display: none" id="putForm">
                    <input name="currentPage" id="currentPage" value="1">
                    <input name="dataSize" id="dataSize" value="10">
                    <input name="totalPages" id="totalPages" value="">
                    <input name="totalDateSize" id="totalDateSize" value="">
                    <input name="" id="select" value="">
                </form>

            </div>

            <%--      添加创建群          --%>
            <div class="layui-tab-item">添加/创建群</div>
            <div class="layui-tab-item">好友申请</div>
        </div>
    </div>
</div>

<%--朋友圈相关的界面--%>
<div class="dis nav-div">
    3
</div>

<script src="static/layui/layui.js"></script>
<script src="/static/js/jquery.js"></script>
<script src="static/js/jqueryCookie.js"></script>
<script src="static/js/xss.js"></script>
<script>

    for(var i=0 ; i < $(".nav").length;i++){
        $(".nav")[i].setAttribute("index",i)
        $($(".nav")[i]).click(function(){
            for(var j = 0;j < $(".nav-div").length; j++){
                $(".nav-div")[j].className = "dis nav-div"
            }
            var index = this.getAttribute("index")
            $(".nav-div")[index].className = "current nav-div"
        })
    }

    var user = JSON.parse(sessionStorage.getItem('user'))
    if (user == undefined){
        location.href = "/login.jsp"
    } else {
        $("#my").html('<img src="/img/'+ user.profile +'" id="profile" class="layui-nav-img">'+ user.name)
    }


    $("#exit").click(function () {
         $.removeCookie("user")
         location.href = "/login.jsp"
    })


    for(var i=0 ; i < $(".nav").length;i++){
        $(".nav")[i].setAttribute("index",i)
        $($(".nav")[i]).click(function(){
            for(var j = 0;j < $(".nav-div").length; j++){
                $(".nav-div")[j].className = "dis nav-div"
            }
            var index = this.getAttribute("index")
            $(".nav-div")[index].className = "current nav-div"
        })
    }

        <%---聊天相关---%>





        <%---通讯录相关---%>
    layui.use('element', function(){
        var element = layui.element;

        //…
    });

    function pageFunction(type){
        if(type == "lastPage"){
            $("#currentPage").val(parseInt($("#currentPage").val()) == 1 ? 1 : parseInt($("#currentPage").val()) -1)
        } else if(type == "nextPage"){
            $("#currentPage").val((parseInt($("#currentPage").val()) == parseInt($("#totalPages").val()) ? parseInt($("#totalPages").val()) : parseInt($("#currentPage").val()) + 1))
        } else if(type == "search"){
            if ( isEmpty($("#name").val()) || isEmpty($("#startPrice").val()) || isEmpty($("#endPrice"))){
                $("#currentPage").val("1");
            }
        }
        var data0 ;
        $.post("/user/findUser",$("#putForm").serialize(),function (data) {
            $("#booksTable").empty()
            data0 = data
            console.log(data)

            if (data.code == 200){

                for (let i = 0 ; i < data.data.length ; i++){
                    $("#usersTable").append("<tr>" +
                        "<td>"+ data.data[i].name +"</td> " +
                        "<td>"+ data.data[i].author +"</td> " +
                        "<td>"+ data.data[i].publish +"</td> " +
                        "<td>"+ data.data[i].price +"</td>" +
                        "</tr>")
                }

                $("#currentPage").val(data.currentPage)
                $("#dataSize").val(data.dataSize)
                $("#totalPages").val(data.totalPages)
                $("#totalDateSize").val(data.totalDateSize)

                $("#nowTotalPages").text($("#totalPages").val())
                $("#nowDataSize").val($("#dataSize").val())
                $("#nowCurrentPage").val($("#currentPage").val())
                $("#select").attr("name","")
            }

        })
    };


    $("#findByUsername").click(function () {
        $("#select").attr("name","username")
        $("#select").val($("#findUser").val())
        $("#currentPage").val("1");
        pageFunction()
    })
    $("#findByName").click(function () {
        $("#select").attr("name","name")
        $("#select").val($("#findUser").val())
        $("#currentPage").val("1");
        pageFunction()
    })


    $("#lastPage").click(function () {
        pageFunction("lastPage")
    })

    $("#nextPage").click(function () {
        pageFunction("nextPage")
    })

    $("#change").click(function () {
        if(isNaN($("#nowCurrentPage").val()) && isNaN($("#nowDataSize").val())){
            alert("请输入正确的信息")
        }else {
            $("#currentPage").val($("#nowCurrentPage").val())
            $("#dataSize").val($("#nowDataSize").val())
        }
        pageFunction("")
    })
        //添加好友





        <%---朋友圈相关---%>


</script>
</body>
</html>
