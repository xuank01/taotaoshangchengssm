var E3MALL = {
	checkLogin : function(){
		var _ticket = $.cookie("token");
		if(!_ticket){
			return ;
		}
		$.ajax({
			url : "http://localhost:8088/user/token/" + _ticket,
			dataType : "jsonp",
			type : "GET",
			success : function(data){
				if(data.status == 200){
					var username = data.data.username;
					var html = username + "，欢迎来到淘淘！<a href=\"\" id=\"logout\" class=\"link-logout\">[退出]</a>";
					$("#loginbar").html(html);
					$("#logout").click(function(){
						   $.ajax({
						       url : "http://localhost:8088/user/logout/" + _ticket,
						       dataType : "jsonp",
						       type : "GET",
						       error: function(request) {  //失败
						           alert("登出失败！");
						           window.location.href="http://localhost:8082"
						       },
						       success: function(data) {  //成功
						       if (data.status == 200){
						           alert("退出成功！");
						           window.location.href="http://localhost:8088/page/login"
						       }
						   }
						 });
					});
				}
			}
		});
	}
}

$(function(){
	// 查看是否已经登录，如果已经登录查询登录信息
	E3MALL.checkLogin();
});