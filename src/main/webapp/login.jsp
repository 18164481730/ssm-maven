<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//Dtd HTML 4.01 Transitional//EN">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>ssm-maven系统登录</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jquery-easyui-1.3.3/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jquery-easyui-1.3.3/themes/icon.css">
    <script type="text/javascript" src="${pageContext.request.contextPath}/jquery-easyui-1.3.3/jquery.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/jquery-easyui-1.3.3/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/jquery-easyui-1.3.3/locale/easyui-lang-zh_CN.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/md5.js"></script>  
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/md5.min.js"></script>  
    <style type=text/css>
        body {
            text-align: center;
            padding-bottom: 0px;
            background-color: #ddeef2;
            margin: 0px;
            padding-left: 0px;
            padding-right: 0px;
            padding-top: 0px
        }

        A:link {
            COLOR: #000000;
            text-decoration: none
        }

        A:visited {
            COLOR: #000000;
            text-decoration: none
        }

        A:hover {
            COLOR: #ff0000;
            text-decoration: underline
        }

        A:active {
            text-decoration: none
        }

        .input {
            border-bottom: #ccc 1px solid;
            border-left: #ccc 1px solid;
            line-height: 20px;
            width: 182px;
            height: 20px;
            border-top: #ccc 1px solid;
            border-right: #ccc 1px solid
        }

        .input1 {
            border-bottom: #ccc 1px solid;
            border-left: #ccc 1px solid;
            line-height: 20px;
            width: 120px;
            height: 20px;
            border-top: #ccc 1px solid;
            border-right: #ccc 1px solid;
        }
    </style>
    <script type="text/javascript">
    	//登录模块
        function login() {
            var userName = $("#userName").val();
            var password = md5($("#password").val());
            var code = $("#code").val();
            if (userName == null || userName == "") {
                alert("用户名不能为空！");
                return;
            }
            if (password == null || password == "") {
                alert("密码不能为空！");
                return;
            }
            if (code == null || code == "") {
            	alert("验证码不能为空!");
            	return;
            }
            var data = {"userName":userName, "password":password, "code":code};
        	$.ajax({ 
        		type: "POST",
        		url: "${pageContext.request.contextPath}/user/login.do", 
        		data: data,
        		dataType: "json",
        		success: function(data){
        			if(data.msg == '0'){
        				$.messager.alert("系统提示", "用户名或密码不正确，请重新输入");
        			} else if(data.msg == '1'){
        				$.messager.alert("系统提示", "登录成功");
        				window.location.href="${pageContext.request.contextPath}/main.jsp";
        			} else if(data.msg == '2'){
        				$.messager.alert("系统提示", "验证码不能为空!");
        			} else if(data.msg == '3'){
        				$.messager.alert("系统提示", "验证码不正确，请重新输入!");
        			}
              	},
              	error: function(){
              		$.messager.alert("系统提示", "系统异常");
              	}
        	});
        }
    	//重置模块
    	function reset(){
    		$("#userName").val("");
    		$("#password").val("");
    		$("#code").val("");
    	}
    	//切换验证码
    	function changeImg() {
	        var imgSrc = $("#codeImg");
	        var src = imgSrc.attr("src");
	        imgSrc.attr("src", chgUrl(src));
    	}  
		//时间戳，去缓存机制
	    function chgUrl(url) {
	        var timestamp = (new Date()).valueOf();if ((url.indexOf("&") >= 0)) {
	            url = url + "&timestamp=" + timestamp;
	        } else {
	            url = url + "?timestamp=" + timestamp;
	        }
	        return url;
	    }
    </script>
</head>
<body>
    <table style="margin: auto; width: 100%; height: 100%" border=0 cellSpacing=0 cellPadding=0>
        <tbody>
        <tr>
            <td height=150>&nbsp;</td>
        </tr>
        <tr style="height: 254px">
            <td>
                <div style="background-color: #278296">
                    <div style="margin: 0px auto; width: 936px">
                        <div style="height: 155px">
                            <div style="text-align: left; width: 265px; float: right; height: 125px; _height: 95px">
                                <table border=0 cellSpacing=0 cellPadding=0 width="100%">
                                    <tbody>
                                    <tr>	
                                        <td style="height: 45px">
                                        	用户名:
                                        	<input type="text" class="input" name="userName" id="userName" style="width:80px;height:20px;"/>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                        	密&nbsp;&nbsp;码:
                                        	<input type="password" class="input" name="password" id="password" style="width:80px;height:20px;"/>
                                        </td>
                                    </tr>
                                    <tr> 
                                    	<td>
					                    	验证码：
					                        <input type="text" class="input" name="code" id="code" style="width:80px;height:20px;"/>
					                        <img id="codeImg" alt="验证码" src="${pageContext.request.contextPath}/admin/code.do" onclick="changeImg()"/>
					                    </td>             
					                </tr>
                                    </tbody>
                                </table>
                            </div>
                            <div style="height: 1px; clear: both"></div>
                            <div style="width: 300px; float: right; clear: both">
                                <table border=0 cellSpacing=0 cellPadding=0 width=300>
                                    <tbody>
                                    <tr>
                                        <td width=100 align=right>
                                        	<input style="border-right-width: 0px; border-top-width: 0px; border-bottom-width: 0px; border-left-width: 0px"
                                                   id=btnLogin src="${pageContext.request.contextPath}/images/btn1.jpg"
                                                   type=image name=btnLogin onclick="javascript:login();return false;"/>
                                        </td>
                                        <td width=100 align=middle>
                                        	<input style="border-right-width: 0px; border-top-width: 0px; border-bottom-width: 0px; border-left-width: 0px"
                                                   id=btnReset src="${pageContext.request.contextPath}/images/btn2.jpg"
                                                   type=image name=btnReset
                                                   onclick="javascript:reset();return false;"/>
                                        </td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </td>
        </tr>
        <tr style="height:30%"><td>&nbsp;</td></tr>
        </tbody>
    </table>
</body>
</html>
