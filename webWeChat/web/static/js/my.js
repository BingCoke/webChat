

function getUser() {
    $.ajax({
        type: "GET",
        url: "/user/getUser",
        success: function (data) {
            if (data.code == 200) {
                console.log(data)
                debugger
                var user = data.data
                if (user.power == 0) {
                    $.removeCookie("user", {
                        path: "/"
                    })
                    window.location.href = "/seal.jsp"
                } else if (user.power == 1 || user.power == 2) {
                    sessionStorage.setItem('user', JSON.stringify(data.data))
                }

            } else if (data.code == 501) {
                layer.msg("没有找到用户")
                location.href = "/index.jsp"
            } else {
                location.href = "/index.jsp"
            }
        },
        async: false
    });
}


function format(time) {
    var time = new Date(time);
    var y = time.getFullYear();
    var m = time.getMonth() + 1;
    var d = time.getDate();
    var h = time.getHours();
    var mm = time.getMinutes();
    var s = time.getSeconds();
    return y + '-' + m + '-' + d + ' ' + h + ':' + mm + ':' + s;
}


class Msg {
    constructor(msgType, from, to, content) {
        this.msgType = msgType;
        this.fromId = from;
        this.toId = to;
        this.content = content;
    }
    fileName = ""
    status = 0;
}

function msgFunction(data) {
    var msg = data.msg
    switch (msg.msgType) {
        case 1:
            if (who.id != msg.fromId) {
                $("#span" + msg.fromId).attr("class", "layui-badge");

                var longCount = friendCount['' + msg.fromId] + 1;
                friendCount['' + msg.fromId] = longCount;
                $("#span" + msg.fromId).html(longCount)
            } else {
                deleteMyFriendMsg()
            }
            friendChatMap.get("" + msg.fromId).push(JSON.stringify(msg))
            getChatArea()
            break
        case 2:
            if (who.id != msg.toId) {
                $("#spanGroup" + msg.toId).attr("class", "layui-badge");
                var count = groupCount['' + msg.toId] + 1;
                groupCount['' + msg.toId] = count;
                $("#spanGroup" + msg.toId).html(count)
            } else {
                deleteMyGroupMsg()
            }
            groupChatMap.get("" + msg.toId).push(JSON.stringify(msg))
            getChatArea()
            break
        case 3:
            getFriendApplication()
            break
        case 4:
            getGroupApplication()
            break
        case 5:
            break
        case 7:
            layer.msg(msg.content, { offset: "rt" }, function () {
            })
            break
        case 9:
            layer.msg(msg.content)
            break
        case 10:
            getGroupAuthorities()
            break
        case 11:
            getUserAuthorities()
            break
        case 12:
            getFriends()
            getFriendChat()
            break
        case 13:
            getGroups()
            getGroupChat()
            break
        case 14:
            groupAnnouncement.set("" + msg.toId, msg.content)
            break
        case 15:
            getUser()
    }
}


function sendMsg(content) {
    var msg = new Msg(who.mtype, user.id, who.id, content)
    if (who.mtype == 1) {
        var msgs = friendChatMap.get("" + msg.toId);
        msgs.push(JSON.stringify(msg))
    } else if (who.mtype == 2) {
        var msgs = groupChatMap.get("" + msg.toId);
        msgs.push(JSON.stringify(msg))
    }
    ws.send(JSON.stringify(msg))
}

function getChatArea() {
    $("#addExpression").attr("style", 'display: none;')
    $("#chatDiv").empty()
    if (who.mtype == 1) {
        $("#FriendCard").attr("style", "display: inline")
        $("#at").attr("style", "display: none")
        var msgs = friendChatMap.get("" + who.id)
        for (var i = 0; i < msgs.length; i++) {
            var msg = JSON.parse(msgs[i])
            var content;
            if (msg.status == 0) {
                content = msg.content
            } else if (msg.status == 1) {
                content = '<a href="' + msg.content + '" download="' + msg.fileName + '">' + msg.fileName + '</a>'
            } else if (msg.status == 2) {
                $.ajax({
                    type: "GET",
                    url: "/user/selectUser?id=" + msg.content,
                    success: function (res) {
                        content = '<div class="layui-panel" id="friendCard' + msg.id + '" ><div style="padding: 30px;">' +
                            '<img src="/img/' + res.data.profile + '" id="profile" class="layui-nav-img">' +
                            res.data.name +
                            '<hr>' +
                            "点击申请好友" +
                            '</div></div>'
                    },
                    async: false
                });
            }
            if (msg.fromId == user.id) {
                $("#chatDiv").append(
                    '<div class="chat-receiver">' +
                    '<div><img src="/img/' + user.profile + '" ></div>' +
                    '<div>' + user.name + '</div>' +
                    '<div>' +
                    '<div class="chat-right_triangle"></div>' +
                    '<span>' + content + '</span>' +
                    '</div>' +
                    '</div>'
                )
            } else {
                var myFriend = JSON.parse(myFriends.get("" + msg.fromId))
                $("#chatDiv").append(
                    '<div class="chat-sender">' +
                    '<div><img src="/img/' + myFriend.profile + '"></div>' +
                    '<div>' + myFriend.remark + '</div>' +
                    '<div style="max-width: 500px;">' +
                    '<div class="chat-left_triangle"></div>' +
                    '<span>' + content + '</span>' +
                    '</div>' +
                    '</div>'
                )
            }
            if (msg.status == 2) {
                $("#friendCard" + msg.id).attr("theId", msg.content)
                $("#friendCard" + msg.id).click(function () {
                    var msg = new Msg(3, user.id, parseInt($(this).attr("theId")), "申请你为好友")
                    ws.send(JSON.stringify(msg))
                })
            }

        }
    } else if (who.mtype == 2) {
        $("#FriendCard").attr("style", "display: none")
        $("#at").attr("style", "display: inline")
        var msgs = groupChatMap.get("" + who.id)
        for (var i = 0; i < msgs.length; i++) {
            var msg = JSON.parse(msgs[i])
            if (msg.status == 0) {
                content = msg.content
            } else if (msg.status == 1) {
                content = '<a href="' + msg.content + '" download="' + msg.fileName + '">' + msg.fileName + '</a>'
            }
            if (msg.fromId == user.id) {
                $("#chatDiv").append(
                    '<div class="chat-receiver">' +
                    '<div><img src="/img/' + user.profile + '"></div>' +
                    '<div>' + user.name + '</div>' +
                    '<div>' +
                    '<div class="chat-right_triangle"></div>' +
                    '<span>' + content + '</span>' +
                    '</div>' +
                    '</div>'
                )
            } else {
                $("#chatDiv").append(
                    '<div class="chat-sender">' +
                    '<div><img src="/img/' + msg.profile + '"></div>' +
                    '<div>' + msg.vest + '</div>' +
                    '<div>' +
                    '<div class="chat-left_triangle"></div>' +
                    '<span>' + content + '</span>' +
                    '</div>' +
                    '</div>'
                )
            }

        }
    }
}


function getFriendApplication() {
    $.get("/friend/getFriendApplication", function (res) {
        for (var i = 0; i < res.data.length; i++) {
            $("#friendApplication").append(
                '<tr>' +
                "<td>" + '<img src="/img/' + res.data[i].profile + '" class="layui-nav-img">' + "</td>" +
                "<td>" + res.data[i].name + "</td>" +
                "<td>" + res.data[i].content + "</td>" +
                "<td>" + '<button class="layui-btn layui-btn-sm" id="refuse' + res.data[i].userId + '">拒绝</button>' + "</td>" +
                "<td>" + ' <button class="layui-btn layui-btn-sm" id="agree' + res.data[i].userId + '">同意</button>' + "</td>" +
                "</tr>"
            )
            $("#agree" + res.data[i].userId).attr("data", JSON.stringify(res.data[i]))
            $("#refuse" + res.data[i].userId).attr("data", JSON.stringify(res.data[i]))
            $("#agree" + res.data[i].userId).click(function () {
                $(this).parent().parent().remove();

                $.post("/friend/passApplication", JSON.parse($(this).attr("data")), function (res) {
                    layer.msg(res.msg)
                    if(res.code == 200){
                        getFriends()
                    }
                })
            })
            $("#refuse" + res.data[i].userId).click(function () {
                $(this).parent().parent().remove();
                $.post("/friend/refuseApplication", JSON.parse($(this).attr("data")), function (res) {
                    layer.msg(res.msg)
                })
            })
        }

    })
}



function getGroupAuthorities() {
    $.ajax({
        url: "/group/getAuthorities",
        async: false,
        success: function (res) {
            groupAuthorities = res.data;
        }
    });
}

function getUserAuthorities() {
    $.ajax({
        url: "/user/getAuthorities",
        async: false,
        success: function (res) {
            userAuthorities = res.data;
        }
    });
}

function getFriends() {
    $.get("/friend/getMyFriends", function name(res) {
        $("#nowFriend").empty()
        myFriends.clear()
        for (var i = 0; i < res.data.length; i++) {
            myFriends.set("" + res.data[i].userId, JSON.stringify(res.data[i]))
            $("#nowFriend").append(
                "<tr>" +
                "<td>" + '<img src="/img/' + res.data[i].profile + '" id="profile" class="layui-nav-img">' + "</td>" +
                "<td>" + res.data[i].remark + "</td>" +
                "<td>" + "<button class='layui-btn layui-btn-xs' id='look" + res.data[i].userId + "'>查看</button>" + "</td>" +
                "</tr>"
            )
            $("#look" + res.data[i].userId).attr("data", JSON.stringify(res.data[i]));

            $("#look" + res.data[i].userId).click(function () {
                $.ajax({
                    url: "/friend/toLook",
                    type: 'POST',
                    data: JSON.parse(this.getAttribute("data")),
                    dataType: 'JSON',
                    success: function (res) {
                        if (res.code == 200) {
                            layer.open({
                                type: 2,
                                content: ['/friend/toLookDo', 'no'],
                                area: ['500px', '430px'],
                                cancel: function (index, layero) {
                                    getFriends()
                                }
                            });
                        }
                    }
                });
            })
        }
    })
}

function getFriendChat() {
    $.get("/friend/getMyFriends", function (res) {
        $("#friendChat").empty()
        for (var i = 0; i < res.data.length; i++) {

            $("#friendChat").append(
                '<li id="friend' + res.data[i].userId + '" >' +
                '<img src="/img/' + res.data[i].profile + '" id="profile" class="layui-nav-img">' + res.data[i].remark + '<span id="span' + res.data[i].userId + '"></span>' +
                '</li>'
            )
            $("#friend" + res.data[i].userId).attr("data", JSON.stringify(res.data[i]))
            $("#friend" + res.data[i].userId).click(function () {
                $("#free").empty()
                var date = JSON.parse($(this).attr("data"))
                who.mtype = 1;
                who.id = date.userId
                if (userAuthorities["" + date.userId] == 1) {
                    $("#sendMsg").css("display", 'none')
                } else {
                    $("#sendMsg").css("display", 'block')
                }
                getChatArea()
                deleteMyFriendMsg()
                $("#span" + who.id).html("")
                $("#span" + who.id).attr("class", "")
                friendCount['' + who.id] = 0;
            })
            if (friendChatMap["" + res.data[i].userId] == undefined) {
                friendCount['' + res.data[i].userId] = 0;
                friendChatMap.set("" + res.data[i].userId, new Array())
            }
        }
    })
}

function getGroupChat() {
    $.get("/group/getMyGroups", function (res) {
        $("#groupChat").empty()
        for (var i = 0; i < res.data.length; i++) {

            $("#groupChat").append(
                '<li id="group' + res.data[i].id + '" >' +
                '<img src="/img/group.jpg" id="profile" class="layui-nav-img">' + res.data[i].name + '<span id="spanGroup' + res.data[i].id + '"></span>' +
                '</li>'
            )
            $("#group" + res.data[i].id).attr("data", JSON.stringify(res.data[i]))
            $("#group" + res.data[i].id).click(function () {
                $("#free").empty()
                var date = JSON.parse($(this).attr("data"))
                who.mtype = 2;
                who.id = date.id;
                if (groupAuthorities["" + date.id] == 0) {
                    $("#sendMsg").css("display", 'none')
                } else {
                    $("#sendMsg").css("display", 'block')
                }
                getChatArea()
                deleteMyGroupMsg()
                $("#spanGroup" + who.id).html("")
                $("#spanGroup" + who.id).attr("class", "")
                if (groupAnnouncement.get("" + who.id) != "" && groupAnnouncement.get("" + who.id) != undefined) {
                    layer.open({
                        type: 1
                        , title: false //不显示标题栏
                        , closeBtn: false
                        , area: '300px;'
                        , shade: 0.8
                        , id: 'LAY_layuipro' //设定一个id，防止重复弹出
                        , btn: ['确定']
                        , btnAlign: 'c'
                        , moveType: 1 //拖拽模式，0或者1
                        , content: groupAnnouncement.get("" + who.id)
                        , success: function (layero) {
                            var btn = layero.find('.layui-layer-btn');
                            $(btn).click(function () {
                                $.get("/group/deleteMyAnnouncement?groupId=" + who.id)
                                groupAnnouncement.set("" + who.id, "")
                            })
                        }
                    });
                }
            })
            if (groupAnnouncement.get("" + who.id) != "" || groupAnnouncement.get("" + who.id) == undefined) {
                groupAnnouncement.set("" + res.data[i].id, "")
            }
            if (groupChatMap["" + res.data[i]] == undefined) {
                groupChatMap.set("" + res.data[i].id, new Array())
                groupCount['' + res.data[i].id] = 0;
            }
        }
    })
}


function deleteMyFriendMsg() {
    $.get("/friend/deleteMyFriendMsg?friendId=" + who.id, function () {
    })
}


function deleteMyGroupMsg() {
    $.get("/group/deleteMyGroupMsg?groupId=" + who.id, function () {

    })
}


function getGroupApplication() {
    $.get("/group/getGroupApplication", function (res) {
        for (var i = 0; i < res.data.length; i++) {
            console.log(res.data[i])
            //groupApplication
            $("#groupApplication").empty()
            $("#groupApplication").append(
                "<tr>" +
                "<td>" + '<img src="/img/' + res.data[i].profile + '" id="profile" class="layui-nav-img">' + "</td>" +
                "<td>" + res.data[i].name + "</td>" +
                "<td>" + res.data[i].content + "</td>" +
                "<td>" + "<button class='layui-btn layui-btn-xs' id='refuseGroup" + res.data[i].msgId + "'>拒绝</button>" + "</td>" +
                "<td>" + "<button class='layui-btn layui-btn-xs' id='passGroup" + res.data[i].msgId + "'>同意</button>" + "</td>" +
                "</tr>"
            )
            $("#refuseGroup" + res.data[i].msgId).attr("data", JSON.stringify(res.data[i]))
            $("#passGroup" + res.data[i].msgId).attr("data", JSON.stringify(res.data[i]))
            $("#refuseGroup" + res.data[i].msgId).click(function () {
                $(this).parent().parent().remove();
                $.post("/group/refuseApplication", JSON.parse($(this).attr("data")), function (res) {
                    layer.msg(res.msg)
                })
            })
            $("#passGroup" + res.data[i].msgId).click(function () {
                $(this).parent().parent().remove();
                $.post("/group/passApplication", JSON.parse($(this).attr("data")), function (res) {
                    layer.msg(res.msg)
                })
            })
        }
    })
}

function getGroups() {

    $.get("/group/getMyGroups", function (res) {
        $("#nowGroup").empty()

        for (var i = 0; i < res.data.length; i++) {
            var str =
                "<tr>" +
                "<td>" + '<img src="/img/group.jpg" id="profile" class="layui-nav-img">' + "</td>" +
                "<td>" + res.data[i].id + "</td>" +
                "<td>" + res.data[i].name + "</td>" +
                "<td>" + "<button class='layui-btn layui-btn-xs' id='lookGroup" + res.data[i].id + "'>查看</button>" + "</td>"

            str = str + "<td>" + "<button class='layui-btn layui-btn-xs' id='groupVest" + res.data[i].id + "'>更改群名片</button>" + "</td>"
            if( groupAnnouncement["" + res.data[i].id] != 3 ){
                str = str + "<td>" + "<button class='layui-btn layui-btn-xs' id='gtoupDelete" + res.data[i].id + "'>退群！</button>" + "</td>"
            }
           
            str = str + "</tr>"
            $("#nowGroup").append(str)
            $("#lookGroup" + res.data[i].id).attr("data", JSON.stringify(res.data[i]))
            $("#groupVest" + res.data[i].id).attr("data", JSON.stringify(res.data[i]))
            $("#gtoupDelete" + res.data[i].id).attr("data", res.data[i].id)

            if( groupAnnouncement["" + res.data[i].id] != 3  ){
                $("#gtoupDelete" + res.data[i].id).click(function(){
                    $.get("/group/delete?id=" + $(this).attr("data"),function(res){
                        if(res.code == 200){
                            $(this).parent().parent().remove()
                            getGroups()
                        }
                        layer.msg(res.msg)
                    })
                })
            }
            $("#lookGroup" + res.data[i].id).click(function () {
                $.post("/group/toLookGroup", JSON.parse($(this).attr("data")), function () {
                    layer.open({
                        type: 2,
                        content: ['/group/lookGroup?ver=' + Math.random()],
                        area: ['800px', '430px'],
                        cancel: function () {
                            getGroups()
                        }
                    });
                })
            })
                $("#groupVest" + res.data[i].id).click(function () {
                    $.post("/group/toLookGroupVest", JSON.parse($(this).attr("data")), function () {
                        layer.open({
                            type: 2,
                            content: ['/group/lookGroupVest?ver=' + Math.random()],
                            area: ['400px', '330px']
                        });
                    })
                })
        }
    })
}
