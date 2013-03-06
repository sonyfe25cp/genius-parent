//package utils;
//
//import junit.framework.TestCase;
//
//public class TestBytesJson extends TestCase{
//
//	
////	private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
////	
////	public static String getTBJson(TB tb) {
////		String json=gson.toJson(tb, TB.class);
////		return json;
////	}
////	public static TB getTBFromJson(String json){
////		TB unit=gson.fromJson(json, new TypeToken<TB>(){}.getType());
////		return unit;
////	}
////	
////	/**
////	 * @param args
////	 * Mar 8, 2012
////	 */
////	@Test
////	public void testmain(String[] args) {
////		TB tb=new TB();
////		String s= "htt://www.bit.edu.cn?1=!";
////		try {
////			tb.cb=s.getBytes("UTF-8");
////		} catch (UnsupportedEncodingException e) {
////			e.printStackTrace();
////		}
////		String json=getTBJson(tb);
////		System.out.println(json);
////		
////		TB tb2=getTBFromJson(json);
////		
////		try {
////			System.out.println(new String(tb2.cb,"UTF-8"));
////		} catch (UnsupportedEncodingException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		}
////		
////
////	}
////
////}
////
////class TB{
////	byte[] cb;
//}
