

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Set;

import junit.framework.TestCase;

import org.junit.Test;

import edu.bit.dlde.genius.model.QueryForm;
import edu.bit.dlde.utils.DLDELogger;

public class SimpleClient extends TestCase{

	private DLDELogger logger=new DLDELogger();
	
	private InetSocketAddress address;
	private ByteBuffer buffer;
	private Selector selector;
	private boolean mark;
	

	private String message;
	
	public SimpleClient(String ip,int port){
		this.address=new InetSocketAddress(ip, port);
		this.buffer= ByteBuffer.allocate(409600);
	}
	
	public void start() {
		long start=System.currentTimeMillis();
		String resstring=null;
		try {
			
			SocketChannel sc = SocketChannel.open();
			selector = Selector.open();
			
			sc.configureBlocking(false);
			sc.register(selector, SelectionKey.OP_CONNECT);

			sc.connect(address);
			
			while((selector.isOpen())&&(selector.select() > 0)){//判断是否open和是否有key
				Set<SelectionKey> readyKey = selector.selectedKeys();
				for(SelectionKey key: readyKey){
					if(key.isConnectable()){
						SocketChannel channel = (SocketChannel) key.channel();
						channel.configureBlocking(false);

						if(channel.isConnectionPending()){
							try{
								mark = channel.finishConnect();
							}catch (ConnectException e) {
								logger.error("该服务器没有开,address:"+address);
//								return null;
							}
						}
						if(mark){
							System.out.println("连接搜索服务器成功！");
							QueryForm form=new QueryForm();
							form.setKeyWords(message);
							channel.write(getByteBuffer(form));//发送查询
						}
						channel.register(selector, SelectionKey.OP_READ);
					}else if(key.isReadable()){
						SocketChannel channel = (SocketChannel) key.channel();
						if(channel.isConnected()&&channel.isOpen()){
							if(channel.read(buffer) > 0){
								buffer.flip();
								ByteArrayInputStream bin = new ByteArrayInputStream(buffer.array());
								ObjectInputStream in = new ObjectInputStream(bin);
	
								try {
									resstring=(String) in.readObject();//获得到搜索结果
									logger.info("resString:"+resstring);
								} catch (ClassNotFoundException e) {
									e.printStackTrace();
								}
								buffer.clear();
								
								key.channel().close();
								key.cancel();
								selector.close();
							}else{
								logger.info("服务器无返回结果！");
								key.channel().close();
								key.cancel();
								selector.close();
								System.out.println("shutdown");
							}
						}else{
							logger.error("服务器已关闭");
						}
					}
					readyKey.remove(key);
				}
			}
			sc.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		long end=System.currentTimeMillis();
		System.out.println("此线程耗时:"+(end-start));
	}
	private ByteBuffer getByteBuffer(Object obj) throws IOException {
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		ObjectOutputStream out = new ObjectOutputStream(bout);
		out.writeObject(obj);
		out.flush();
		byte[] arr = bout.toByteArray();
		ByteBuffer buffer = ByteBuffer.wrap(arr);
		out.close();
		return buffer;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Test
	public void testSimpleClient(){
		SimpleClient sc=new SimpleClient("localhost",9901);
		sc.setMessage("北京");
		sc.start();
	}
	
	
	
	
	
	

	/**
	 * test normal c-s
	 * @param args
	 */
	public static void main(String[] args) {
//		HashMap<String,String> map=new HashMap<String, String>();
//		map.put("chen", "jie");
//		map.put("omar", "in406");
//		map.put("mikki", "嘿嘿");
//		map.put("huihui", null);
//		Client client=new Client("127.0.0.1", 8888, map);
//		client.start();
//		
		
		long t1=System.currentTimeMillis();
//		for(int i=1;i<1000;i++){
			SimpleClient sc=new SimpleClient("localhost",8891);
			sc.setMessage("北京");
			sc.start();
//		}
		long t2=System.currentTimeMillis();
		System.out.println("use:"+(t2-t1)+"mills");
	}

}
