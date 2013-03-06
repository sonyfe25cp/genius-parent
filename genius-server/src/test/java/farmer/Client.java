package farmer;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import edu.bit.dlde.genius.core.Gift;
import edu.bit.dlde.genius.model.IndexForm;
import edu.bit.dlde.genius.model.Order;
import edu.bit.dlde.genius.model.QueryForm;
import edu.bit.dlde.utils.DLDETools;

public class Client {
	public Client() {
	}
	
	public static long t=0;
	

	public static void main(String[] args) {
		Client c=new Client();
		c.index();
//		c.query();
	}
	
	public void query(){
		String query = "应用";
		QueryForm form=new QueryForm();
		form.setKeyWords(query);
		run(form);
	}
	
	public void index(){
		Gift gift=new Gift();
		gift.setBody("北京是中国的首都");
//		gift.setBody("大学还是北京理工好！!! ");
		gift.setAuthor("chenjie");
		gift.setUniqueGiftId("by110");
		
		IndexForm form = new IndexForm();
		form.setGift(gift);
		run(form);
		
	}
	
	public void run(Order order){
		Socket client = null;
		DataOutputStream out = null;
		DataInputStream in = null;
		try {
			client = new Socket("localhost", 9981);
			client.setSoTimeout(10000);
//			client.setKeepAlive(true);
//			client.setReceiveBufferSize(100000);
//			client.set
			out = new DataOutputStream((client.getOutputStream()));

			long t1=System.currentTimeMillis();
		
			byte[] request =DLDETools.getBytes(order);
			System.out.println("request length:"+request.length);
			out.write(request);
			out.flush();
			client.shutdownOutput();
			
			in = new DataInputStream(client.getInputStream());
			byte[] reply = new byte[100000];
//			int len=in.read(reply);
			
			byte tmpp=(byte)in.read();
			int j=0;
			while(tmpp!=-1){
				reply[j]=tmpp;
				tmpp=(byte)in.read();
				j++;
			}
			
//			System.out.println("&&&&&&&&&&&&&&&&&&&&"+len);
			
//			byte[] r=new byte[len];
			
			
			String res=new String(reply);
			System.out.println(res);
			
//			FileWriter fw=new FileWriter("/data/res.txt");
//			fw.write(res);
//			fw.flush();
//			fw.close();
			

			long t2=System.currentTimeMillis();
			
			long tmp=t2-t1;
			t=tmp;
			
			in.close();
			out.close();
			client.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}finally{
			System.out.println("use:"+t+"mills");
		}
	}
	
	public static byte[] getBytes(Object obj) throws IOException {
		byte[] arr=null;
		try{
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(bout);
			out.writeObject(obj);
			out.flush();
			arr = bout.toByteArray();
			out.close();
			bout.close();
		} catch (Exception e) {
			
		}finally{
			return arr;
		}
	}

}
