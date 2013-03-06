$(document).ready(function(){
//    $.post("GetSession", {
//        Attribute: "UserName"
//    }, function(data){
//        if (data != "!NU") {
//            $("a#user").html(data);
//        }
//        else {
//            $("a#user").html("登录");
//        }
//    });
    $("li.ccl").mouseover(function(){
        $(this).children(".cclc").css("border-color", "#F55");
        //$(this).find(".cclbs").css("visibility", "visible");
        
    });
    $("li.ccl").mouseout(function(){
        $(this).children(".cclc").css("border-color", "#555");
        //$(this).find(".cclbs").css("visibility", "hidden");
        
    });
    
    $(".cclha").click(function(){
        var $this = $(this).closest(".cclc").find(".ccldmi");
        $.post("addhistory", {
            sourceId: $this.html()
        }, function(data){
              //alert(data);
        	  $this.html(data);
        });
    });
    
    $(".cclbs").click(function(){
        var $this = $(this);
        if($this.html()=="true"){
        	//alert("send: cancelcluster"+$this.closest(".cclc").find(".ccldmi").html());
	        $.post("cancelcluster", {
	            sourceId: $this.closest(".cclc").find(".ccldmi").html()
	        }, function(data){
	    	   // alert(data);
		//      $this.html(data);
	        	$this.html("false");
	        });
        }
        else
        {
        	//alert("send selectcluster:"+$this.closest(".cclc").find(".ccldmi").html());
	        $.post("selectcluster", {
	            sourceId: $this.closest(".cclc").find(".ccldmi").html()
	        }, function(data){
	        	//alert(data);
		  //      $this.html(data);
	        	$this.html("true");
	        });
        }
    });
});
