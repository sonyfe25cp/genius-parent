package farmer;
import java.io.File;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import bit.crawl.store.PageStoreReader;
import bit.crawl.store.StoredPage;
import edu.bit.dlde.extractor.BlockExtractor;
import edu.bit.dlde.genius.core.Gift;
import edu.bit.dlde.genius.model.IndexForm;


public class Parser {
	
	public static void main(String[] args){
		parse();
	}
	
	public static void parse(){

		String folderPath="/home/coder/software/CrawlerEngine/build/dist/crawlerengine/crawled-pages";
		String bakPath="/home/coder/data/finance-bak/";
		File folder=new File(folderPath);
		for(File file:folder.listFiles()){
			String filename=file.getName();
			System.out.println(filename);
			PageStoreReader psr=new PageStoreReader(file);
			ArrayList<StoredPage> array=psr.loadAll();
			BlockExtractor be=new BlockExtractor();
			for(StoredPage page:array){
				
				be.setReader(new StringReader(page.getContent()));
				be.extract();
				String content=be.getContent();
				
				String title=extractTitle(page.getContent());
				
				if(title==null||title.length()==0||content==null||content.length()<30){
					continue;
				}
				
				Gift gift=new Gift();
				gift.setBody(content);
				gift.setTitle(title);
				gift.setUniqueGiftId(page.getHeader("URL"));
				gift.setUrl(page.getHeader("URL"));
				gift.setDate(extractTime(filename));
				
				IndexForm form = new IndexForm();
				form.setGift(gift);
				Client client=new Client();
				client.run(form);
				
			}
			if(!new File(bakPath).exists()){
				new File(bakPath).mkdirs();
			}
			file.renameTo(new File(bakPath+filename));
		}
	
	}
	public static String extractTime(String str){
		str=str.substring(str.indexOf("_")+1, str.lastIndexOf("."));
		str=str.substring(0,10);
		return str;
	}
	public static String extractTitle(String html) {
		String titleRegex="<(title|TITLE)>.*?</(title|TITLE)>";
		Pattern pattern=null;
		Matcher matcher=null;
		pattern=Pattern.compile(titleRegex);
		matcher=pattern.matcher(html);
		String title="";
		if(matcher.find()){
			title=matcher.group();
			title=title.replaceAll("<.*?>",	"");
		}
		return title;
	}
	
	public static void log(String str){
		System.out.println(str);
	}

}
