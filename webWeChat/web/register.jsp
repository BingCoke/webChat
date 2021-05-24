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
    <title>注册</title>
</head>
<body>
<form id="register" onsubmit="return submitForm()">
    用户名：<input type="text" name="username" id="username" required value=""><span id="usernameJudge"></span><br>
    密码：<input type="password" name="password" id="password" required><br>
    再次输入密码: <input type="password" name="passwordAgain" id="passwordAgain" required><span id="passwordJudge"></span><br>
    名字：<input type="text" name="name" maxlength="10" required><br>
    性别：男 <input type="radio" name="gender" value="男" required>
    女 <input type="radio" name="gender" value="女"><br>
    手机号 <input type="text" name="phone"  id="phone" required><br>
    头像<input type="file" name="profile" id="profile" accept="image/*"><br>
    邮箱 <input type="email" name="mail" id="mail"  required>
    邮箱验证码 <input type="text" name="mailCode"><br>
    <div id="time"></div><br>
    <button type="button" id="send">发送邮箱</button><br>
    <input type="submit" value="提交">
</form>
</body>
<script src="static/layui/layui.js"></script>
<script src="/static/js/jquery.js"></script>
<script>

    let isHave = true
    //对于用户名的合法性的校准
    const username = document.getElementById("username")
    username.addEventListener("blur",function () {
        if (document.getElementById("username").value === ""){
            return
        }
        if (/[a-zA-Z0-9_]{5,15}$/.test(username.value) == false) {
            document.getElementById("usernameJudge").innerText = "用户名只能字母数字下划线！5-15位"
        } else {
            var url = "/user/checkUser?username=" + username.value
            $.get(url,function (data) {
                console.log(data)
                document.getElementById("usernameJudge").innerText = data.msg
                isHave = data.isHave
            })
        }
    })
    username.addEventListener("focus", function () {
        document.getElementById("usernameJudge").innerText = ""
    })

    // 密码的校验
    const password = document.getElementById("password")
    const passwordAgain = document.getElementById("passwordAgain")
    const passwordJudge = document.getElementById("passwordJudge")

    const judge = function () {
        if ( passwordAgain.value != "") {
            if(!/^(?![a-zA-Z]+$)(?![0-9]+$)[0-9A-Za-z\W]{6,18}$/.test(password.value)){
                console.log(/^(?![a-zA-Z]+$)(?![0-9]+$)[0-9A-Za-z\W]{6,18}$/.test(password.value))
                passwordJudge.innerText = "密码要6-18位，有字母数字，可以包含特殊字符"
            } else if(password.value != passwordAgain.value) {
                passwordJudge.innerText = "密码不一致！"
            } else if (password.value == passwordAgain.value) {
                passwordJudge.innerText = "密码一致"
            }
        } else {
            passwordJudge.innerText = ""
        }
    }

    password.addEventListener("keyup",judge)
    passwordAgain.addEventListener("keyup",judge)

    $("#send").click(function () {
        if ($("#mail").val() == ""){
            alert("请填写你的邮箱地址")
        } else if(!/^[A-Za-z0-9\u4e00-\u9fa5]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/.test($("#mail").val())){
            alert("请填写正确的邮箱地址")
        } else {
            $.post("/user/sendRegisterCode",{ mail : $("#mail").val()},function (data) {

                if (data.code == 200){
                    layui.use('util', function(){
                        var util = layui.util;
                        var endTime = new Date().getTime()+60*1000 //结束日期
                            ,serverTime = new Date().getTime(); //服务器时间
                        var i = 0
                        util.countdown(endTime, serverTime, function(date, serverTime, timer){
                            if (i == 0){
                                i = 1
                            }else {
                                var str = date[3] + '秒';
                                layui.$('#time').html('验证码失效时间'+ str);
                                if (date[3] == -1){
                                    alert("邮箱验证码已经失效了")
                                }
                            }
                        });
                    });
                } else {
                    alert(data.msg)
                }
            })
        }
    })

    function submitForm(){
        if(password.value != passwordAgain.value || !/[a-zA-Z0-9_]{5,15}$/.test(username.value) || isHave || !/^(?![a-zA-Z]+$)(?![0-9]+$)[0-9A-Za-z\W]{6,18}$/.test(password.value)){
            alert("请检查信息填写的正确性")
        } else {
            $.ajax({
                type: 'POST',
                url : "/user/userRegister",
                contentType : false,
                processData: false,
                data : new FormData(document.getElementById("register")),
                success : function (data) {
                    alert(data.msg)
                    if (data.code == 200){
                        location.href = "/login.jsp"
                    }
                }
            })
        }
        return false;
    }







</script>
</html>
