<%@ page language="java" contentType="textml;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>管理员界面</title>
<link href="/styles/layout.css" rel="stylesheet" type="text/css" />
<link href="/styles/wysiwyg.css" rel="stylesheet" type="text/css" />
<!-- Theme Start -->
<link href="/themes/blue/styles.css" rel="stylesheet" type="text/css" />
<!-- Theme End -->
</head>
<body style="background:none">
      <!-- Left Dark Bar Start -->
    <div id="leftside">
    	<div class="user">
        	<img src="/img/avatar.png" width="44" height="44" class="hoverimg" alt="Avatar" />
            <p>用户角色:</p>
            <p class="username">系统管理员</p>
<!--             <p class="userbtn"><a href="#" title="">资料</a></p> -->
            <p class="userbtn"><a href="/admin/logout" title="">退出</a></p>
        </div>
<!--         <div class="notifications"> -->
<!--         	<p class="notifycount"><a href="" title="" class="notifypop">10</a></p> -->
<!--             <p><a href="" title="" class="notifypop">新到系统通知</a></p> -->
<!--             <p class="smltxt">(点击打开通知列表)</p> -->
<!--         </div> -->
        <ul id="nav">
        	<li>
        		<a class="collapsed heading">资源获取配置</a>
                <ul class="navigation">
                    <li><a href="/admin/seed/list" target="mainFrame">数据采集器列表</a></li>
                    <li><a href="/admin/crawler/status" target="mainFrame">后台程序控制器</a></li>
                    <li><a href="/admin/extract/list" target="mainFrame">信息解析配置</a></li>
                    <li><a href="/admin/faq/" target="mainFrame">使用帮助</a></li>
                </ul>
            </li>
            
            <li>
                <a class="collapsed heading">信息资源配置</a>
                 <ul class="navigation">
                 	<li><a href="/admin/xnews/show" title="" target="mainFrame">资源列表</a></li>
                 	<li><a href="/admin/hot/list" title="" target="mainFrame">热点词监测</a></li>
                 	<li><a href="/admin/sensitive/list" title="" target="mainFrame">敏感词监测</a></li>
                    <li><a href="/admin/category/list" title="" target="mainFrame">特征词配置</a></li>
                    <!-- <li><a href="/admin/category/uploadifyTest" title="" target="mainFrame">uploadifyTest</a></li>  -->                
                    <!-- <li><a href="/admin/plsa/list" title="" target="mainFrame">PLSA分类</a></li> -->
                    <li><a href="/admin/classification_jxt/list" title="" target="mainFrame">分类</a></li>
                </ul>
            </li>
            <li><a class="collapsed heading">搜索引擎信息</a>
                <ul class="navigation">
                    <!--  <li><a href="/admin/analysis/cloud" title="" target="mainFrame" class="likelogin">热门搜索</a></li>  -->
<!--                     <li><a href="#" title="">用户交互信息</a></li> -->
                    <li><a href="/admin/chart/getchart" target="mainFrame">查询热度统计</a></li>
                    <li><a href="/admin/pdf/getpdf" target="mainFrame">统计报告</a></li>
                </ul>
            </li> 
            <li><a class="collapsed heading">推荐管理</a>
                <ul class="navigation">
                    <li><a href="/admin/user/show?pageNo=1&pageSize=10" title="" target="mainFrame" class="likelogin">用户管理</a></li>
                    <!-- <li><a href="/admin/xnews/show?pageNo=1&pageSize=10" title="" target="mainFrame" class="likelogin">新闻管理</a></li> -->
                    <li><a href="/admin/xnewscluster/show" title="" target="mainFrame" class="likelogin">聚类信息</a></li>
                </ul>
            </li> 
        </ul>
    </div>
    <!-- Left Dark Bar End --> 
    
    <!-- Notifications Box/Pop-Up Start --> 
    <div id="notificationsbox">
        <h4>Notifications</h4>
        <ul>
            <li>
            	<a href="#" title=""><img src="/img/icons/icon_square_close.png" alt="Close" class="closenot" /></a>
            	<h5><a href="#" title="">New member registration</a></h5>
                <p>Admin eve joined on 18.12.2010</p>
            </li>
            <li>
            	<a href="#" title=""><img src="/img/icons/icon_square_close.png" alt="Close" class="closenot" /></a>
            	<h5><a href="#" title="">New member registration</a></h5>
                <p>Jackson Michael joined on 16.12.2010</p>
            </li>
        </ul>
        <p class="loadmore"><a href="#" title="">Load more notifications</a></p>
    </div>
    <!-- Notifications Box/Pop-Up End --> 
    
    <script type="text/javascript" src="/scripts/enhance.js"></script>	
    <script type='text/javascript' src='/scripts/excanvas.js'></script>
	<script type='text/javascript' src='/scripts/jquery.min.js'></script>
    <script type='text/javascript' src='/scripts/jquery-ui.min.js'></script>
	<script type='text/javascript' src='/scripts/jquery.wysiwyg.js'></script>
    <script type='text/javascript' src='/scripts/visualize.jQuery.js'></script>
    <script type="text/javascript" src='/scripts/functions.js'></script>
    
    <!--[if IE 6]>
    <script type='text/javascript' src='/scripts/png_fix.js'></script>
    <script type='text/javascript'>
      DD_belatedPNG.fix('img, .notifycount, .selected');
    </script>
    <![endif]--> 
</body>
</html>