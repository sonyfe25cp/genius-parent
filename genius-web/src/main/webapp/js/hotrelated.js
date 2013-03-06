$(document)
	.ready(
		function() {
			//点击进行ajax，显示相关热度词
			$("[class^='click']").live("click", function(event) {
			var node = event.target.nodeName;
			if(node=="a"||node=="A")
				return;
			var _class = $(this).attr("class");
			var _id = $(this).attr("id");
			if(_class=="click_show_tr"){
				$(this).attr("class", "click_hidden_tr");
				$(this).attr("style", "BACKGROUND-COLOR: #cceefa");
				$.ajax({
					type : "POST",
					dataType:"json",
					url : "/admin/hot/cooccurrence",
					data :  "term="+$(this).children("#term").text(),
					success : function(back) { 
						_class = $(this).attr("class");
						if(back != null){
							var append = '';
							append += '<tr  class="sub_tr" style="BACKGROUND-COLOR: #e6f0fc"';
							append += ' id="'+_id+'">"';
							append += '<td></td>';
							append = append +'<td>共现： ' +back.term0+'</td>';
							append = append +'<td>相关性： ' +back.relation0+'</td>';
							append = append +'<td></td>';
							append += '</tr>';
							append += '<tr  class="sub_tr" style="BACKGROUND-COLOR: #e6f0fc"';
							append += ' id="'+_id+'">"';
							append += '<td></td>';
							append = append +'<td>共现： ' +back.term1+'</td>';
							append = append +'<td>相关性： ' +back.relation1+'</td>';
							append = append +'<td></td>';
							append += '</tr>';
							append += '<tr  class="sub_tr" style="BACKGROUND-COLOR: #e6f0fc"';
							append += ' id="'+_id+'">"';
							append += '<td></td>';
							append = append +'<td>共现： ' +back.term2+'</td>';
							append = append +'<td>相关性： ' +back.relation2+'</td>';
							append = append +'<td></td>';
							append += '</tr>';
							append += '<tr  class="sub_tr" style="BACKGROUND-COLOR: #e6f0fc"';
							append += ' id="'+_id+'">"';
							append += '<td></td>';
							append = append +'<td>共现： ' +back.term3+'</td>';
							append = append +'<td>相关性： ' +back.relation3+'</td>';
							append = append +'<td></td>';
							append += '</tr>';
							append += '<tr  class="sub_tr" style="BACKGROUND-COLOR: #e6f0fc"';
							append += ' id="'+_id+'">"';
							append += '<td></td>';
							append = append +'<td>共现： ' +back.term4+'</td>';
							append = append +'<td>相关性： ' +back.relation4+'</td>';
							append = append +'<td></td>';
							append += '</tr>';
							 $(".click_hidden_tr#"+_id).after(append);
						}
					},
					error : function(back) {
					//alert(back.responseText);
					}
				});
			}
			else{
				//alert("hide");
				$(this).attr("class", "click_show_tr");
				$(this).attr("style", "BACKGROUND-COLOR: #0");	
				//alert($(".sub_tr#"+_id).attr("id"));
				$(".sub_tr").remove("#"+_id);
			}
			});
						
			//全选或全不选
			$("#checkboxall").click(function() {//当点击全选框时
				var flag = $("#checkboxall").attr("checked");//判断全选按钮的状态
				$("[id^='Item']").each(function() {//查找每一个Id以Item结尾的checkbox
					$(this).attr("checked", flag);//选中或者取消选中
				});
			});
						
			//如果全部选中勾上全选框，全部选中状态时取消了其中一个则取消全选框的选中状态
			$("[id^='Item']").each(
				function() {
					$(this).click(
						function() {
							if ($("[id^='Item']:checked").length == $("[id^='Item']").length) {
								$("#checkboxall").attr("checked","checked");
							} else
								$("#checkboxall").removeAttr("checked");
							});
				});
			});