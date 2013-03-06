<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link href="/styles/layout.css" rel="stylesheet" type="text/css" />
<link href="/styles/wysiwyg.css" rel="stylesheet" type="text/css" />
<link href="/css/genius-admin.css" rel="stylesheet" type="text/css" />
<!-- Theme Start -->
<link href="/themes/blue/styles.css" rel="stylesheet" type="text/css" />
<link href="/uploadify/uploadify.css" rel="stylesheet" type="text/css"/>

<script type="text/javascript" src="/js/jquery-1.7.1.js"></script> 
<script type="text/javascript" src="/js/classify.js"> </script>
<!-- <script type="text/javascript" src="http://code.jquery.com/jquery-1.7.2.min.js"></script> -->
<script type="text/javascript" src="/uploadify/jquery.uploadify-3.1.min.js"></script>

<script type="text/javascript">
$(function() {
	　　$('#file_upload').uploadify({
	　　   'swf' : '/uploadify/uploadify.swf', //指定上传控件的主体文件，默认‘uploader.swf’
		'uploader' : '/admin/classification_jxt/uploadify;jsessionid=${pageContext.session.id}',
		'fileObjName' : 'file',
		/* 		
 'uploader' : '<c:url value="/student/image"/>;jsessionid=${pageContext.session.id}', */
	　       /*'uploader' : '/admin/category/uploadify;jsessionid='+${pageContext.session.id},//指定服务器端上传处理文件，默认‘upload.php’ */
	   /* 	'uploader' : '/admin/category/uploadify',
	   	'formdata' : {'jsessionid':"${pageContext.session.id}",'ttt':'111'}, */
	　       'auto' : true, //选定文件后是否自动上传，默认false
	　　  'multi' : true, //是否允许同时上传多文件，默认false
	　　  'fileTypeDesc' : 'txt文件', //出现在上传对话框中的文件类型描述
	　　  'fileTypeExts' : '*.txt', //控制可上传文件的扩展名，启用本项时需同时声明fileDesc
	　　  'fileSizeLimit': 86400, //控制上传文件的大小，单位byte
	　　  'uploadLimit' : 7000,//多文件上传时，同时上传文件数目限制
	    'buttonText' : '添加本地待分类数据',//浏览按钮的文本，默认值：BROWSE
	    'queueSizeLimit' : 8,
	    'onQueueComplete' : function(queueData){
		  window.location.href="/admin/classification_jxt/uploadifySucceed";
	   }
	　　});
	});
</script>

<script language= "JavaScript">
function load(classifyState)
{
	//alert("classifyState="+classifyState);
	if(classifyState=="CLASSIFYING"||classifyState=="CLASSIFY_DISABLED")
		{
			var sbmt = document.getElementById("sbmt");
			var plsa = document.getElementById("classify_plsa");
			//alert("sbmt="+sbmt);
			if(classifyState=="CLASSIFYING")
				sbmt.value="分类：分类中...";
			if(classifyState=="CLASSIFY_DISABLED")
				sbmt.value="分类：训练未完成";
			sbmt.disabled=true;
			sbmt.className = "btnalt";
			plsa.disabled=true;
			plsa.className = "btnalt";
		}
}
</script>

<script type="text/javascript">
var request;
function createRequest(){
	try{
		request = new XMLHttpRequest();
	}catch(tryMS){
		try{
			request = new ActiveXObject("Msxml2.XMLHTTP");
		}catch(otherMS){
			try{
				request = new ActiveXObject("Microsoft.XMLHTTP");
			}catch(failed){
				request = null;
			}
		}
	}
	return request;
}

function showCompleted(){
	if(request.readyState==4&&request.status==200){
			alert("分类完成");
			window.location.reload();
		}
}

function classify(){
	request = createRequest();
	if(request == null)
		alert("Unable to create request");
	else{
		var url = "/admin/classification_jxt/classify_KNN";
		request.open("POST",url,true);
		request.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
		request.onreadystatechange = showCompleted;
		load("CLASSIFYING");
		request.send("pageIdsString="+document.getElementById("wysiwyg").value);
	}
}
</script>
</head>
<body style="background: none" onload="load('${classifyState}');">
	<div class="contentcontainer">
		<div class="headings altheading">
			<h2>分类列表${classifyState}</h2>
		</div>
		<div class="contentbox">
			<table width="100%">
				<thead>
					<tr>
						<th>序号</th>
						<th>类别</th>
						<th>文件数目</th>
						<th>详单</th>
					</tr>
				</thead>
				<tbody>
					<c:set var="number" value = "1"/>
					<c:forEach var="cat" items="${categories}">
					<tr>
						<td>${number}</td>
						<td><c:out value="${cat.name}"/></td>
						<td><c:out value="${cat.classifiedFileNum}"/></td>
						<td>
						<c:url value="/admin/classification_jxt/details" var="catchedUrl">
							<c:param name="type" value="${cat.name}"/>
						</c:url>
						<a href="${catchedUrl}">...</a>
						</td>
						<c:set var="number" value = "${number+1}"/>
					</tr>
					</c:forEach>
				</tbody>
			</table>
			<div id="classify"> 
				<input type="button" id="sbmt" value="KNN分类" class="btn" onClick="classify();"/>
				<input type="button" value="PLSA分类 " class="btn" id="classify_plsa"/>
				<input type="file" name="file_upload" id="file_upload" />
			</div>
				<div style="clear: both;"></div>
	    </div>
		<div class="contentbox">
			<h3>添加待分类的Genius页面</h3>
			<textarea class="inputbox" id="wysiwyg" name="pageIdsString"
					 rows="12" cols="260"></textarea>
	   </div>
	   <h3>KNN分类说明：</h3>
	   在训练集完成训练过程之后，分类按钮由黑色变为蓝色，表示分类可用，分类可以对本地文件/网页进行，点击“KNN分类”按钮即可。
	</div>
</body>
</html>