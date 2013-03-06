<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>FAQ</title>
<link href="/styles/layout.css" rel="stylesheet" type="text/css" />
<link href="/styles/wysiwyg.css" rel="stylesheet" type="text/css" />
<!-- Theme Start -->
<link href="/themes/blue/styles.css" rel="stylesheet" type="text/css" />
</head>
<body style="background: none">
	<div class="contentcontainer">
		<div class="headings altheading">
			<h2>使用帮助</h2>
		</div>
		<div class="contentbox">
			<h2>如何配置抽取规则</h2>
			<p>对应种子名称：抽取规则对应的种子</p>
			<p>抽取目标类型：主要分为论坛和新闻，不同的类型对应不同的配置xml	</p>
			<p>对应uri正则：需要处理的网页url对应的正则表达式	</p>
			<p> 新闻类网页的配置：
				<textarea class="text-input textarea" id="textfield-xml" name="xml"
						rows="11" cols="75" readonly="readonly" >
&lt;!--该例是例是新浪抽取规则的内容。根元素为rule--&gt;
&lt;rule&gt;
       &lt;!--子元素需要由标签名和标签体表示抽取的元素和对应的xpath
例如title为元素名，//*[@id='artibodyTitle']为对应的xpath 元素名可自由定义，xpath需要准确给予--&gt;
        &lt;title&gt;//*[@id='artibodyTitle']&lt;/title&gt;
        &lt;ref&gt;//*[@id='art_source']/a&lt;/ref&gt;
        &lt;content&gt;//*[@id='artibody']/p&lt;/content&gt;
        &lt;publictime&gt;//*[@id='pub_date']&lt;/publictime&gt;
        &lt;reply&gt;.//*[@id='comment_t_show1']/a/span&lt;/reply&gt;
&lt;/rule&gt; 
				</textarea></p>
				<p>
				 论坛类网页的配置：
				 <textarea class="text-input textarea" id="textfield-xml" name="xml"
						rows="10" cols="75" readonly="true">
&lt;rule&gt;
&lt;!--该例是急速论坛抽取规则的内容。不同点在于论坛的配置文件必须在配置文件最初就给出一个名为mainframe的元素。
我们将每一个论坛网页视为多个框的组成，mainframe里面的xpath指定了这些框。 --&gt;
       &lt;title&gt;.//*[@id='thread_subject']&lt;/title&gt;
	&lt;mainframe&gt;.//*[@id]/tbody &lt;/mainframe&gt; &lt;!--其余元素同新闻类
	--&gt; &lt;username&gt;.//tr[1]/td[1]/div[1]/a/font&lt;/username&gt;
	&lt;content&gt;.//tr/td/div/div/div/table/tbody/tr/td&lt;/content&gt;
&lt;/rule&gt;
				</textarea>
			</p>
		</div>
	</div>
</body>
</html>