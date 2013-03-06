package com.genius.task.help;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import com.genius.dao.IExtractConfigurationDAO;
import com.genius.model.ExtractConfiguration;

import edu.bit.dlde.extractor.BlockExtractor;
import edu.bit.dlde.extractor.ForumExtractor;
import edu.bit.dlde.extractor.NewsExtractor;
import edu.bit.dlde.extractor.xpathcfg.Rule;
import edu.bit.dlde.extractor.xpathcfg.XmlConfigurationImpl;

/**
 * 用来正文抽取的类，链式的处理输入的网页字符串。调用extract(String str, String uri) 进行抽取， 接着通过public
 * String get(String key)获得需要的部分，例如key=“title”。
 * 
 * @author lins
 */
public class ContentExtractorChain {
	// 各种抽取器
	private NewsExtractor ne;
	private ForumExtractor fe;
	private BlockExtractor be;
	// 用来保存抽取结果的map
	private HashMap<String, String> map;
	// 抽取配置的dao
	private IExtractConfigurationDAO ecmDao;
	// 记录被抽取网页的类型
	private String type;
	// 标记上一次抽取是否成功
	private boolean isExtracted;

	/**
	 * 初始化链上所有的抽取器，默认isExtracted为false
	 */
	public ContentExtractorChain() {
		ne = new NewsExtractor();
		fe = new ForumExtractor();
		be = new BlockExtractor();
		isExtracted = false;
	}

	/**
	 * @return 被抽取网页的类型，例如news和forum
	 */
	public String getTheParsedType() {
		return type;
	}

	/**
	 * @return 上一次抽取任务是否完成。
	 */
	public boolean isExtracted() {
		return isExtracted;
	}

	public IExtractConfigurationDAO getEcmDao() {
		return ecmDao;
	}

	/**
	 * 从数据库读取抽取配置，用来配置NewsExtractor和ForumExtractor
	 * 
	 * @param ecmDao
	 *            抽取配置dao
	 */
	public void setEcmDao(IExtractConfigurationDAO ecmDao) {
		this.ecmDao = ecmDao;
		// 用来解析配置xml的类
		XmlConfigurationImpl xci = new XmlConfigurationImpl();
		// 从数据库读出所有的配置
		List<ExtractConfiguration> ecfgs = ecmDao.loadExtCfgs(0,
				Integer.MAX_VALUE);
		// 对每一个配置进行解析,并将解析后的配置放入抽取器
		for (ExtractConfiguration ecfg : ecfgs) {
			Rule r = xci.getConfig(ecfg.getXml());
			r._enabled = ecfg.isEnabled();
			r._siteType = ecfg.getType();
			r._uriRegex = ecfg.getUriRegx();

			if (ecfg.getType().equals("forum")) {
				fe.addRule(r);
			} else if (ecfg.getType().equals("news")) {
				ne.addRule(r);
			}
		}
	}

	/**
	 * 抽取的结果保存为<K,V>对，通过给定的key获得对应的内容 假如不存在则返回null
	 */
	public String get(String key) {
		if (map == null)
			return null;
		String ttl = "";
		// 由于map里面的使用的键值事实上是"key-idx"，例如"title-0"
		// 所以给定的key事实上是需要扩展到多个key‘的
		// 这么做的好处是统一处理了论坛和一般网页
		for (int i = 0;; i++) {
			String tmp = map.get(Integer.toString(i) + '-' + key);
			if (tmp == null)
				break;
			ttl += tmp.trim();
			ttl += "\n";
		}
		return ttl;
	}

	/**
	 * chain上每个抽取器抽取完毕后调用 完成检验是否正确抽取；填补title等工作
	 */
	private void postProcess(String str, String uri) {
		// 检验是否正确抽取
		// 标准一：map是否为空
		if (map == null) {
			isExtracted = false;
			return;
		}
		// 标准二：抽取的长度>100
		int ttl = 0;
		Iterator<Entry<String, String>> it = map.entrySet().iterator();
		while (it.hasNext()) {
			ttl += it.next().getValue().length();
		}
		if (ttl > 100)
			isExtracted = true;
		else {
			isExtracted = false;
			return;
		}

		// 填补title
		String title = map.get("0-title");
		if (title == null || title.trim().equals("")) {
			try {
				// 直接使用/html/head下面的title作为title
				BufferedReader br = new BufferedReader(new StringReader(str));

				StringBuilder sb = new StringBuilder();
				String tmp = br.readLine();
				while (tmp != null) {
					int idx = tmp.indexOf("<title");
					if (idx != -1) {
						// 防止換行，所有无脑多加两行
						tmp += br.readLine();
						tmp += br.readLine();
						// 获得<title></title>内部的东西，出于保险的考虑从<title开始
						char[] chars = tmp.substring(idx + 5).toCharArray();
						boolean findGt = false;
						for (char c : chars) {
							if (c == '<')
								break;
							if (findGt)
								sb.append(c);
							if (c == '>')
								findGt = true;
						}
						break;
					}
					tmp = br.readLine();
				}
				// 放入结果
				map.put("0-title", sb.toString());
				br.close();
			} catch (IOException e) {
				// 出现异常则把uri当作title
				map.put("0-title", uri);
			}
		}
	}

	/**
	 * 进行抽取，将结果保存至map
	 * 
	 * @param str
	 *            网页字符串
	 * @param uri
	 *            对应的统一资源定位付
	 */
	public void extract(String str, String uri, String type) {
		this.type = type;
		map = null;

		/*** ne, fe, be链式处理 ***/
		if (type.equals("NEWS")) {
			ne.setResource(new StringReader(str), uri);
			map = ne.extract();
		} else if (type.equals("FORUM")) {
			fe.setResource(new StringReader(str), uri);
			map = fe.extract();
		} else if (type.equals("WEIBO")) {
			return;
		}
		
		// be永远都是最后被执行
		be.setResource(new StringReader(str), uri);
		map = be.extract();
		if (map != null) {
			type = "NEWS";
		}

		postProcess(str, uri);
	}

	// public static void main(String[] args) throws IOException{
	// FileReader reader = new FileReader(new
	// File("/home/lins/data/test/test"));
	// try {
	// BufferedReader br = new BufferedReader(reader);
	//
	// StringBuilder sb = new StringBuilder();
	// String tmp = br.readLine();
	// while(tmp!=null){
	// int idx = tmp.indexOf("<title");
	// if(idx!=-1){
	// // 防止換行
	// tmp += br.readLine();
	// tmp += br.readLine();
	// char[] chars = tmp.substring(idx+5).toCharArray();
	// boolean findGt = false;
	// for(char c: chars){
	// if(c=='<')
	// break;
	// if(findGt)
	// sb.append(c);
	// if(c=='>')
	// findGt = true;
	// }
	// break;
	// }
	// tmp = br.readLine();
	// }
	// System.out.println(sb.toString());
	// //map.put("0-title",sb.toString());
	// } catch (IOException e) {
	// //map.put("0-title", uri);
	// System.out.println("io exception");
	// }
	// }
}