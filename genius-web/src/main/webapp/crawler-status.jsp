<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="/styles/layout.css" rel="stylesheet" type="text/css" />
<link href="/styles/wysiwyg.css" rel="stylesheet" type="text/css" />
<!-- Theme Start -->
<link href="/themes/blue/styles.css" rel="stylesheet" type="text/css" />
<title>Insert title here</title>
</head>
<body style="background: none">

	<div class="contentcontainer">
		<div class="headings altheading">
			<h2>后台控制</h2>
		</div>
		<div class="contentbox">
			<div class="noticeboxalt">
				<div class="innernotice">

					<h1 style="font-size: 20px; font-weight: bold">爬虫</h1>
					<table style="font-size: 16px" width="100%">
						<tr>
							<td align="left">开启状态：</td>
							<td align="right"><c:if test="${status==true}">开启
				<a href="/admin/crawler/changec?switch=false" title="已开启，点击关闭爬虫"><img
										src="/img/icons/icon_approve.png" alt="已开启，点击关闭爬虫" />  </a>
								</c:if> <c:if test="${status==false}">关闭	
					<a href="/admin/crawler/changec?switch=true" title="已关闭，点击开启爬虫"><img
										src="/img/icons/icon_unapprove.png" alt="已关闭，点击开启爬虫" /> </a>
								</c:if>
							</td>
						</tr>
						<tr>
						<td align="left">总共种子数：</td>
						<td align="right">${totalSCount}</td>
						</tr>
						<tr>
						<td align="left">新闻种子数：</td>
						<td align="right">${newsSCount}</td>
						</tr>
						<tr>
						<td align="left">论坛种子数：</td>
						<td align="right">${forumSCount}</td>
						</tr>
						<tr>
						<td align="left">微博种子数：</td>
						<td align="right">${weiboSCount}</td>
						</tr>
						<tr>
						<td align="left">采集网页数：</td>
						<td align="right">${pageCount}</td>
						</tr>
						<tr>
						<td align="left">自启动时间：</td>
						<td align="right">00:00</td>
						</tr>
					</table>
				</div>
			</div>
			<div class="noticebox">
				<div class="innernotice">

					<h1 style="font-size: 20px; font-weight: bold">解析</h1>
					<table style="font-size: 16px" width="100%">
						<tr>
							<td align="left">开启状态：</td>
							<td align="right"><c:if test="${pstatus==true}">开启
				<a href="/admin/crawler/changep?switch=false" title="已开启，点击关闭解析"><img
										src="/img/icons/icon_approve.png" alt="已开启，点击关闭爬虫" />  </a>
								</c:if> <c:if test="${pstatus==false}">关闭	
					<a href="/admin/crawler/changep?switch=true" title="已关闭，点击开启解析"><img
										src="/img/icons/icon_unapprove.png" alt="已关闭，点击开启爬虫" /> </a>
								</c:if>
							</td>
						</tr>
						<tr>
						<td align="left">总共配置数：</td>
						<td align="right">${totalXCount}</td>
						</tr>
						<tr>
						<td align="left">新闻配置数：</td>
						<td align="right">${newsXCount}</td>
						</tr>
						<tr>
						<td align="left">论坛配置数：</td>
						<td align="right">${forumXCount}</td>
						</tr>
						<tr>
						<td align="left">解析网页数：</td>
						<td align="right">${pageXCount}</td>
						</tr>
						<tr>
						<td align="left">自启动时间：</td>
						<td align="right">00:00</td>
						</tr>
						<tr><td>&nbsp;</td><td>&nbsp;</td></tr>
					</table>
				</div>
			</div>
		</div>
	</div>


</body>
</html>