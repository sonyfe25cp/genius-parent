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
<link rel="stylesheet" type="text/css" href="/css/cssreset.css">
<!-- Theme End -->
</head>
<body id="homepage">
	<div id="header">
    	<a href="" title=""><img src="/img/cp_logo.png" alt="Control Panel" class="logo" /></a>
<!--     	<div id="searcharea"> -->
<!--             <p class="left smltxt"><a href="#" title="">高级</a></p> -->
<!--             <input type="text" class="searchbox" value="Search control panel..." onclick="if (this.value =='Search control panel...'){this.value=''}"/> -->
<!--             <input type="submit" value="Search" class="searchbtn" /> -->
<!--         </div> -->
    </div>
        
	<iframe style="float:left" name="leftFrame" src="/admin/menu" width="220px" height="700px"  scrolling="no" frameborder="0"></iframe>
    <!-- Top Breadcrumb Start -->
<!--     <div id="breadcrumb"> -->
<!--     	<ul>	 -->
<!--         	<li><img src="/img/icons/icon_breadcrumb.png" alt="Location" /></li> -->
<!--         	<li><strong>当前位置:</strong></li> -->
<!--             <li><a href="#" title="">Sub Section</a></li> -->
<!--             <li>/</li> -->
<!--             <li class="current">Control Panel</li> -->
<!--         </ul> -->
<!--     </div> -->
    <!-- Top Breadcrumb End -->
	    <!-- Right Side/Main Content Start -->
	    <div id="rightside">
	        <iframe name="mainFrame" src="/admin/welcome" width="100%" height="120%"  scrolling="yes" frameborder="0" ></iframe> 
	        <div id="footer">
	        	&copy; Copyright 2010 Genius Search Engine
	        </div> 
	    </div>
	    <!-- Right Side/Main Content End -->
	    
    
	<script type='text/javascript' src='/scripts/jquery.min.js'></script>
    <script type="text/javascript" src="/scripts/enhance.js"></script>	
    <script type='text/javascript' src='/scripts/excanvas.js'></script>
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
