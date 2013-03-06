/**
 * 
 */
package com.genius.utils;

import java.util.ArrayList;
import java.util.List;

import com.genius.model.NewsReport;

/**
 * @author horizon
 *
 */
public class ContentFormat {

	/**
	 * 
	 */
	public ContentFormat() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * 对sourceStr生成一个宽为letterspace个字符的摘要。
	 * @param sourceStr 需要生成摘要的字符串
	 * @param letterspace 摘要宽度，单位为半月字符宽度
	 * @return
	 */
	public static String cutString(String sourceStr,int letterspace)
	{
		if(sourceStr==null||sourceStr.length()*2 <= letterspace)
		{
			return sourceStr;
		}
		else {
			
			int left = letterspace-3;
			if(left <= 0)
			{
				if(letterspace<=0)
				{
					return null;
				}
				else
				{
					return letterspace==1?".":letterspace==2?"..":"...";
				}
			}
			
			int index = 0;
			//enough to fill
			char[] charArray = null;
			if(sourceStr.length()>letterspace)
			{
				charArray = (sourceStr.substring(0, letterspace)).toCharArray();
			}
			else
			{
				charArray = sourceStr.toCharArray();
			}
			
			StringBuilder sb = new StringBuilder();
			for(;left >0 && index<charArray.length;index++)
			{
				if(charArray[index] >=0 && charArray[index] <256)
				{
					left--;
				}
				else
				{
					left-=2;
				}
				if(left == -1)
				{
					sb.append("...");
					break;
				}
				sb.append(charArray[index]);
			}
			if(left == 0 && index<charArray.length)
			{
				sb.append("...");
			}
			return sb.toString();
		}
	}
	/**
	 * 对新闻进行格式化，生成摘要，以便于显示
	 */
	public static NewsReport FormatBoxNews(NewsReport news)
	{
		if(news == null)
		{
			return null;
		}
//		news.setFullUrl(news.getUrl());
//		news.setId(news.getId());
		news.setUrl(cutString(news.getUrl(),33));
		news.setTitle(cutString(news.getTitle(),17));
//		news.setPublishTime(news.getPublishTime());
		news.setContent(cutString(news.getContent(),243));
		news.setAuthor(cutString(news.getAuthor(),17));
		//description and keywords is not used
		return news;
	}
	/**
	 * 对新闻列表中的新闻进行格式化，生成摘要，以便于显示
	 */
	public static List<NewsReport> FormatBoxNews(List<NewsReport> newslist)
	{
		if(newslist == null)
		{
			return null;
		}
		else
		{
			List<NewsReport> result = new ArrayList<NewsReport>();
			for(NewsReport news:newslist)
			{
				result.add(FormatBoxNews((NewsReport)news));
			}
			return result;
		}
	}
	/**
	 * 对新闻进行格式化，生成摘要，以便于显示
	 */
	public static NewsReport FormatListNews(NewsReport news)
	{
		if(news == null)
		{
			return null;
		}
//		news.setFullUrl(news.getUrl());
//		news.setId(news.getId());
		news.setUrl(cutString(news.getUrl(),103));
		news.setTitle(cutString(news.getTitle(),63));
//		news.setDate(news.getDate());
		news.setContent(cutString(news.getContent(),603));
		news.setAuthor(cutString(news.getAuthor(),23));
		//description and keywords is not used
		return news;
	}
	/**
	 * 对新闻列表中的新闻进行格式化，生成摘要，以便于显示
	 */
	public static List<NewsReport> FormatListNews(List<NewsReport> newslist)
	{
		
		if(newslist == null)
		{
			return null;
		}
		else
		{
			List<NewsReport> result = new ArrayList<NewsReport>();
			for(NewsReport news:newslist)
			{
				result.add(FormatListNews((NewsReport)news));
			}
			return result;
		}
	}
}
