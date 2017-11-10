<html>
<head>
    <title>Mybatis分页插件 - 测试页面</title>
    <script src="${request.contextPath}/static/js/jquery-1.11.1.min.js"></script>
    <link href="${request.contextPath}/static/css/style.css" rel="stylesheet" type="text/css"/>
    <style type="text/css">
        .pageDetail {
            display: none;
        }

        .show {
            display: table-row;
        }
        
     
		li{float: left; list-style: none;margin: 10px;}
		.active{background-color: red}

    </style>

</head>
<body>
<div class="wrapper">
<a href="/uaa/logout" ">登出</a>
    <div class="middle">
        <h1 style="padding: 50px 0 20px;">用户列表</h1>
 <form action="${request.contextPath}/users" method="post">
            <table class="gridtable" style="width:100%;">
                <tr>
                    <th>用户名：</th>
                    <td><input type="text" name="username"
                               value="<#if username??>${username}</#if>"/></td>
                  
                    <td rowspan="2"><input type="submit" value="查询"/></td>
                </tr>
                
            </table>
        </form>

    <#if page??>

        <table class="gridtable" style="width:100%;">
            <thead>
            <tr>
                <th colspan="4">查询结果 - [<a href="${request.contextPath}/users/add">新增用户</a>]</th>
            </tr>
            <tr>
                <th>ID</th>
                <th>用户</th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody>
                <#list page.list as user>
                <tr>
                    <td>${user.id}</td>
                    <td>${user.username}</td>
                    <td style="text-align:center;">
                        [<a href="${request.contextPath}/users/delete/${user.id}">删除</a>]
                    </td>
                </tr>
                </#list>
            </tbody>
        </table>
   <div class="message">
			共<i class="blue">${page.total}</i>条记录，当前显示第 <i
				class="blue">${page.pageNum}/${page.pages}</i> 页
		</div>
		<div style="text-align:center;">
			<ul class="pagination">
				
				<#if !page.isFirstPage >
					<li><a href="${request.contextPath}/users?page=${page.firstPage}"><<</a></li>
					<li><a href="${request.contextPath}/users?page=${page.prePage}"><</a></li>
				</#if>
				<#list page.navigatepageNums as navigatepageNum>
					<#if navigatepageNum==page.pageNum>
						<li class="active"><a href="${request.contextPath}/users?page=${navigatepageNum}">${navigatepageNum}</a></li>
					</#if>
					<#if navigatepageNum!=page.pageNum>
						<li><a href="${request.contextPath}/users?page=${navigatepageNum}">${navigatepageNum}</a></li>
					</#if>
				</#list>
				<#if !page.isLastPage>
					<li><a href="${request.contextPath}/users?page=${page.nextPage}">></a></li>
					<li><a href="${request.contextPath}/users?page=${page.lastPage}">>></a></li>
				</#if>
				
			</ul>
	</div>
    </#if>
    </div>
    <div class="push"></div>
</div>
</body>
</html>