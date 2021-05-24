<%--
  Created by IntelliJ IDEA.
  User: Z
  Date: 21/4/9
  Time: 19:50
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>登录界面</title>
    <link rel="stylesheet" href="/static/layui/css/layui.css"><link rel="stylesheet" href="/static/layui/css/layui.css">
</head>
<body>

<img class="layui-nav-img"  width="150" src="https://dss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=3730772664,138405132&fm=26&gp=0.jpg" id="profile" >

<form action="/user/userLogin" onsubmit="return login()" id="loginForm" >
    用户名<input type="text" name="username" id="username"><br>
    密码<input type="password" name="password" id="password"><br>
    验证码<input type="text" name="verifyCode" id="verifyCode" ><img src="/user/verify" id="verifyImage"><br>
    <input type="submit">
</form>
<a href="register.jsp">注册用户</a>
<a href="/findPassword.html" >找回密码</a>
</body>
<script src="static/js/jquery.js"></script>
<script src="static/js/jqueryCookie.js"></script>
<script src="./static/layui/layui.js"></script>
<script>

    let isHave = false
    $("#username").blur(function () {
        $.get("/user/findProfile?username=" + $("#username").val(),function (data) {
            console.log(data)
            isHave = data.isHave
            if(isHave){
                $("#profile").attr("src","/img/"+ data.msg)
            }else {
                $("#profile").attr("src","https://dss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=3730772664,138405132&fm=26&gp=0.jpg")
            }
        } )
    })


    function login() {
        $.post("/user/userToLogin",$("#loginForm").serialize(),function (data) {
            if(data.code != 200) {
              layer.msg(data.msg)
            } else {
                $.removeCookie("user")
                data.data.password = $("#password").val()
                $.cookie("user",JSON.stringify(data.data),{ expires: 7 });
                sessionStorage.setItem('user',JSON.stringify(data.data))
                location.href = "/menu.html"
            }
        })
        return false
    }

    $("#verifyImage").click(function () {
        $("#verifyImage").attr("src","/user/verify?i="+ Math.random())
    })

    $("#verifyImage").ready(function () {
        if($.cookie("user")){
            $.post("/user/autoLogin",JSON.parse($.cookie("user")),function (data) {
                console.log(1)
                console.log(data)
                if (data.code == 200){
                    var user = JSON.parse($.cookie("user"))
                    $.removeCookie("user")
                    $.cookie("user",JSON.stringify(user),{ expires: 7 });
                    sessionStorage.setItem('user',JSON.stringify(user))
                    location.href = "/menu.html"
                } else {
                    layer.msg(data.msg)
                }
            },'json')
        }
    })


</script>
</html>
