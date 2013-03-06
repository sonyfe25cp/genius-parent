package com.genius.model;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.bson.types.ObjectId;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Indexed;

/**
 * column name is the key which is assured in DAO class.
 * 
 * @author lins
 */
@Entity
public class ExtractConfiguration {
	@Id
	private ObjectId id = null;
	@Indexed
	private String name = null;
	@Indexed
	private String seed = null;

	private String type = null;
	private String xml = null;
	private String uriRegx = null;

	public String getUriRegx() {
		return uriRegx;
	}

	public void setUriRegx(String uriRegx) {
		this.uriRegx = uriRegx;
	}

	private boolean enabled = false;

	public String getSeed() {
		return seed;
	}

	public void setSeed(String seed) {
		this.seed = seed;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getXml() {
		return xml;
	}

	public void setXml(String xml) {
		this.xml = xml;
	}

	public String validate() {
		if (name == null || type == null || xml == null || seed == null
				|| uriRegx == null) {
			System.err.println("not fullfilled");
			return "请填满表单!";
		}
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory
					.newInstance();
			docFactory.setNamespaceAware(true); // never forget this!
			Document doc = docFactory.newDocumentBuilder().parse(new ByteArrayInputStream(xml.getBytes()));
			XPath xpath = XPathFactory.newInstance().newXPath();
			XPathExpression xPathExpr1 = xpath.compile("/rule/title");
			NodeList nodes = (NodeList) xPathExpr1.evaluate(doc,
					XPathConstants.NODESET);
			if (nodes == null || nodes.getLength() == 0) {
				System.err.println("error in news:/rule/title.");
				return "新闻类抽取配置需要设置title节点";
			}
			if (type.equals("news")) {
				xPathExpr1 = xpath.compile("rule/content");
				nodes = (NodeList) xPathExpr1.evaluate(doc,
						XPathConstants.NODESET);
				if (nodes == null || nodes.getLength() == 0) {
					System.err.println("error in news:/rule/content.");
					return "新闻类抽取配置需要设置content节点";
				}
			}
			if (type.equals("forum")) {
				xPathExpr1 = xpath.compile("rule/mainframe");
				nodes = (NodeList) xPathExpr1.evaluate(doc,
						XPathConstants.NODESET);
				if (nodes == null || nodes.getLength() == 0) {
					System.err.println("error in forum:/rule/mainframe.");
					return "论坛类抽取配置需要设置mainframe节点";
				}
				xPathExpr1 = xpath.compile("rule/content");
				nodes = (NodeList) xPathExpr1.evaluate(doc,
						XPathConstants.NODESET);
				if (nodes == null || nodes.getLength() == 0) {
					System.err.println("error in forum:/rule/content.");
					return "论坛类抽取配置需要设置content节点";
				}
				xPathExpr1 = xpath.compile("rule/title");
				nodes = (NodeList) xPathExpr1.evaluate(doc,
						XPathConstants.NODESET);
				if (nodes == null || nodes.getLength() == 0) {
					System.err.println("error in forum:/rule/title.");
					return "论坛类抽取配置需要设置title节点";
				}
			}
		} catch (IOException e) {
			System.err.println("error in xml.");
			return "XML格式错误";
		} catch (SAXException e) {
			System.err.println("error in xml.");
			return "XML格式错误";
		} catch (ParserConfigurationException e) {
			System.err.println("error in xml.");
			return "XML格式错误";
		} catch (XPathExpressionException e) {
			System.err.println("error in xml.");
			return "XML格式错误";
		}
		return "";
	}

//	public static boolean validate(String xml, String type) {
//		try {
//			DocumentBuilderFactory docFactory = DocumentBuilderFactory
//					.newInstance();
//			docFactory.setNamespaceAware(true); // never forget this!
//			Document doc = docFactory.newDocumentBuilder().parse(new ByteArrayInputStream(xml.getBytes()));
//			XPath xpath = XPathFactory.newInstance().newXPath();
//			XPathExpression xPathExpr1 = xpath.compile("/rule/title");
//			NodeList nodes = (NodeList) xPathExpr1.evaluate(doc,
//					XPathConstants.NODESET);
//			if (nodes == null || nodes.getLength() == 0) {
//				System.err.println("error in news:/rule/title.");
//				return false;
//			}
//			if (type.equals("news")) {
//				xPathExpr1 = xpath.compile("rule/content");
//				nodes = (NodeList) xPathExpr1.evaluate(doc,
//						XPathConstants.NODESET);
//				if (nodes == null || nodes.getLength() == 0) {
//					System.err.println("error in news:/rule/content.");
//					return false;
//				}
//			}
//			if (type.equals("forum")) {
//				xPathExpr1 = xpath.compile("rule/mainframe");
//				nodes = (NodeList) xPathExpr1.evaluate(doc,
//						XPathConstants.NODESET);
//				if (nodes == null || nodes.getLength() == 0) {
//					System.err.println("error in forum:/rule/mainframe.");
//					return false;
//				}
//				xPathExpr1 = xpath.compile("rule/content");
//				nodes = (NodeList) xPathExpr1.evaluate(doc,
//						XPathConstants.NODESET);
//				if (nodes == null || nodes.getLength() == 0) {
//					System.err.println("error in forum:/rule/content.");
//					return false;
//				}
//			}
//		} catch (IOException e) {
//			System.err.println("error in xml.");
//			return false;
//		} catch (SAXException e) {
//			System.err.println("error in xml.");
//			return false;
//		} catch (ParserConfigurationException e) {
//			System.err.println("error in xml.");
//			return false;
//		} catch (XPathExpressionException e) {
//			System.err.println("error in xml.");
//			return false;
//		}
//		return true;
//	}
//
//	public static void main(String[] args) {
//		String xml = "<!--该例是例是新浪抽取规则的内容。根元素为rule--><rule>       <!--子元素需要由标签名和标签体表示抽取的元素和对应的xpath例如title为元素名，//*[@id='artibodyTitle']为对应的xpath 元素名可自由定义，xpath需要准确给予-->        <title>//*[@id='artibodyTitle']</title>        <ref>//*[@id='art_source']/a</ref>        <content>//*[@id='artibody']/p</content>        <publictime>//*[@id='pub_date']</publictime>        <reply>.//*[@id='comment_t_show1']/a/span</reply></rule> ";
//		ExtractConfiguration.validate(xml, "news");
//	}
}
