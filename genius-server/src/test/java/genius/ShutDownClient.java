package genius;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

import edu.bit.dlde.genius.model.AdminForm;
import edu.bit.dlde.genius.model.Order;
import edu.bit.dlde.utils.DLDETools;

public class ShutDownClient {
	public ShutDownClient() {
	}

	public static long t = 0;

	public static void main(String[] args) {
		ShutDownClient c = new ShutDownClient();
		c.shutdown();
	}

	public void shutdown() {

		AdminForm admin = new AdminForm();
		admin.setShutdown(true);
		run(admin);

	}

	public void run(Order order) {
		Socket client = null;
		DataOutputStream out = null;
		DataInputStream in = null;
		try {
			client = new Socket("10.1.0.171", 9981);
			client.setSoTimeout(10000);
			out = new DataOutputStream((client.getOutputStream()));

			long t1 = System.currentTimeMillis();

			byte[] request = DLDETools.getBytes(order);
			System.out.println("request length:" + request.length);
			out.write(request);
			out.flush();
			client.shutdownOutput();

//			in = new DataInputStream(client.getInputStream());
//			byte[] reply = new byte[40960];
//			in.read(reply);
//			System.out.println(new String(reply));

			long t2 = System.currentTimeMillis();

			long tmp = t2 - t1;
			t = tmp;

//			in.close();
			out.close();
			client.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			System.out.println("use:" + t + "mills");
		}
	}

	// public static byte[] getBytes(Object obj) throws IOException {
	// byte[] arr=null;
	// try{
	// ByteArrayOutputStream bout = new ByteArrayOutputStream();
	// ObjectOutputStream out = new ObjectOutputStream(bout);
	// out.writeObject(obj);
	// out.flush();
	// arr = bout.toByteArray();
	// out.close();
	// bout.close();
	// } catch (Exception e) {
	//
	// }finally{
	// return arr;
	// }
	// }

}
