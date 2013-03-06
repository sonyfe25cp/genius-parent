//
//import org.junit.Assert;  
//import org.junit.Test;  
//import org.springframework.mock.web.MockHttpServletRequest;  
//import org.springframework.mock.web.MockHttpServletResponse;  
//import org.springframework.web.servlet.ModelAndView;   
//  
///**  
//* 说明： 测试OrderAction的例子 
//*  
//* @author  赵磊  
//* @version 创建时间：2011-2-2 下午10:26:55   
//*/   
//  
//public class SearchActionTest extends JUnitActionBase {  
//    @Test  
//    public void testAdd() throws Exception {  
//    MockHttpServletRequest request = new MockHttpServletRequest();  
//        MockHttpServletResponse response = new MockHttpServletResponse();  
//        request.setRequestURI("/g");  
//        request.addParameter("q", "120102");  
//        request.setMethod("GET");  
//        // 执行URI对应的action  
//        final ModelAndView mav = this.excuteAction(request, response);  
//        // Assert logic  
////        Assert.assertEquals("order/add", mav.getViewName());  
//        String msg=(String)request.getAttribute("msg");  
//        System.out.println(msg);  
//    }  
//}   