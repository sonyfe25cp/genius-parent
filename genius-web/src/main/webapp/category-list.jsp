<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
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
<script type="text/javascript" src="/js/category.js"> </script>
<!--<script type="text/javascript" src="/js/jquery-1.7.2.min.js"></script>-->
<script type="text/javascript" src="/uploadify/jquery.uploadify-3.1.min.js"></script>

<script type="text/javascript">
$(function() {
	　　$('#file_upload').uploadify({
	　　   'swf' : '/uploadify/uploadify.swf', //指定上传控件的主体文件，默认‘uploader.swf’
		'uploader' : '/admin/category/uploadify;jsessionid=${pageContext.session.id}',
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
	    'buttonText' : '上传训练文档',//浏览按钮的文本，默认值：BROWSE
	    'queueSizeLimit' : 8,
	    'onQueueComplete' : function(queueData){
		  window.location.href="/admin/category/uploadifySucceed";
	   }
	　　});
	});
</script>

<script language= "JavaScript">
	function load(uploadResult,trainState){ 
	if(uploadResult!=null&&uploadResult!=""&&uploadResult!="$$$")
		alert(uploadResult.replace(/\|/g,"\n"));
	if(uploadResult=="$$$")
		alert("路径不正确");
	if(trainState=="TRAINED"||trainState=="TRAINING")
	{
		var trainModel = document.getElementById("trainModel");
		var trainModel_plsa = document.getElementById("trainModel_plsa");
		if(trainState=="TRAINED"){
			trainModel.value="训练:已完成";
		}			
		if(trainState=="TRAINING")
			{
				trainModel.value="训练:训练中...";
				trainModel_plsa.disabled=true;
				trainModel_plsa.className = "btnalt";
				var newCat = document.getElementById("newCat");
				var	uploadTrainingFile = document.getElementById("uploadTrainingFile");
				newCat.disabled=true;
				newCat.className = "btnalt";
				uploadTrainingFile.disabled=true;
				uploadTrainingFile.className = "btnalt";
				var edtDel = document.getElementsByName("edtDel");
				//alert("edtDel="+edtDel+"length="+edtDel.length);
				for(var i=0;i<edtDel.length;i++)
					edtDel[i].href="";
			}
		trainModel.disabled=true;
		trainModel.className = "btnalt";
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
			alert("训练模型完成");
			window.location.reload();
		}
}

function trainModel(){
	request = createRequest();
	if(request == null)
		alert("Unable to create request");
	else{
		var url = "/admin/category/trainModel";
		request.open("GET",url,true);
		request.onreadystatechange = showCompleted;
		load(null,"TRAINING");
		request.send(null);
	}
}
</script>
</head>
<body style="background:none" onload="load('${uploadResult}','${trainState}');">
	<div class="contentcontainer">
		<div class="headings altheading">
			<h2>特征词配置列表<%--   trainState=${trainState}&jsessionId=${pageContext.session.id} --%></h2>
		</div>
		<div class="contentbox">
			<table width="100%">
				<thead>
					<tr>
						<th>序号</th>
						<th>特征词名称</th>
						<th>训练集数目</th>
						<th>更改/删除</th>
					</tr>
				</thead>
				<tbody>
				<c:set var="index" value="1"/>
				<c:forEach var="category" items="${categories}">
					<tr>
						<td>${index}</td>
						<td><a href="/admin/category/update/view?category=${category.name}">${category.name}</a></td>
						<td><a href="/admin/category/update/view?category=${category.name}">${category.trainingSetNum}</a></td>
						<td>
							<a name="edtDel" href="/admin/category/update/modify?category=${category.name}"><img src="/img/icons/icon_edit.png" alt="编辑" /></a>
							<a name="edtDel" href="/admin/category/delete/${category.name}" title=""><img src="/img/icons/icon_delete.png" alt="删除" /></a>
						</td>
						<c:set var="index" value="${index+1}"/>
					</tr>
				</c:forEach>
				</tbody>
			</table>
			<ul class="pagination">				
				<c:if test="${pageNo!= 1}">
					<li class="text"><a href="/admin/category/list?pageNo=${pageNo-1}">Previous</a></li>
				</c:if>
				<c:forEach var="num" begin="1" end="${totalP}" step="1">				
					<c:if test="${num == pageNo}">
						<li class="page"><a href="#" title="">${num}</a></li>
					</c:if>
					<c:if test="${num != pageNo}">
						<li><a href="/admin/category/list?pageNo=${num}" title="">${num}</a></li>
					</c:if>
				</c:forEach>
				<c:if test="${(pageNo != totalP)&&totalP!=0}"><!-- syl -->
					<li class="text"><a href="/admin/category/list?pageNo=${pageNo+1}">Next</a></li>
				</c:if>
			</ul>
			<div style="clear: both;"></div>
		</div>
	</div>
	<input onclick="window.location.href='/admin/category/update/new'" type="button" class="btn" id="newCat" value="添加新的类别" name="newCat">
	<input onclick="updateTrainingFile();" type="button" class="btn" value="上传新的训练集" id="uploadTrainingFile" name="uploadTrainingFile">
	<input onclick="trainModel();" type="button" id="trainModel" class="btn" value="KNN：训练模型" name="trainModel">
	<input type="button" class="btn" value="PLSA训练" name="trainModel_plsa" id="trainModel_plsa" />
	<input type="file" name="file_upload" id="file_upload" />
	<h2>KNN训练说明:</h2>
	<p>通过“上传新的训练集”按钮可以上传新的可用训练数据（默认为可用），如果训练数据属于新的类别，则会增加新的可用类别。</p>
	<p>点击“添加新的类别”按钮以从可用训练类别中添加新的类别以及选择此类别对应的训练数据。</p>
	<p>点击“KNN：训练模型”按钮可以对选择出的类别进行训练，获得训练后的模型。</p>
	<p>训练完成后不需要再次进行训练，除非对类别或类别中的训练文件进行了更改。</p>
	<p></p>
</body>
</html>