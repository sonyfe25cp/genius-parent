package edu.bit.dlde.analysis.classification_jxt_forweb;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.wltea.analyzer.lucene.IKAnalyzer;

public class ReadGenius {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Directory dir = FSDirectory.open(new File("C:\\Users\\jiang\\data\\index\\genius"));
		IndexReader testSetReader = IndexReader.open(dir);
		System.out.println("DocsNum:"+testSetReader.numDocs());
//		Directory dir3 = FSDirectory.open(new File("C:\\Users\\jiang\\data\\index\\genius2"));
//		IndexReader testSetReader3 = IndexReader.open(dir3);
//		System.out.println("DocsNum2:"+testSetReader3.numDocs());
		
		Directory dir2 = FSDirectory.open(new File("C:\\Users\\jiang\\data\\index\\genius2"));
		IndexWriter writer = new IndexWriter(dir2, new IKAnalyzer(), true ,IndexWriter.MaxFieldLength.UNLIMITED);
		 
		for (int i = 0; i < testSetReader.numDocs(); i++)
		{
			System.out.println("---------------------------------------------");
			Document doc=new Document();
			
			String id = testSetReader.document(i).getField("id").stringValue();
			System.out.println("id="+testSetReader.document(i).getField("id").stringValue());
			doc.add(new Field("id",id,Store.YES,Index.NO));
			
			String body = testSetReader.document(i).getField("body").stringValue();
			System.out.println("body="+testSetReader.document(i).getField("body").stringValue());
			//doc.add(new Field("body",body,Store.YES,Index.ANALYZED,Field.TermVector.WITH_POSITIONS_OFFSETS));
			
			if(testSetReader.document(i).getField("title")!=null)
			{
				String title = testSetReader.document(i).getField("title").stringValue();
				System.out.println("title="+title);
				//doc.add(new Field("title",title,Store.YES,Index.ANALYZED));
			}
			if(testSetReader.document(i).getField("author")!=null)
			{
				String author = testSetReader.document(i).getField("author").stringValue();
				System.out.println("author="+author);
				//doc.add(new Field("author",author,Store.YES,Index.ANALYZED));
			}
			if(testSetReader.document(i).getField("url")!=null)
			{
				String url = testSetReader.document(i).getField("url").stringValue();
				System.out.println("url="+url);
				//doc.add(new Field("url",url,Store.YES,Index.NO));
			}
			if(testSetReader.document(i).getField("date")!=null)
			{
				String date = testSetReader.document(i).getField("date").stringValue();
				System.out.println("date="+date);
				//doc.add(new Field("date",date,Store.YES,Index.NO));
			}
			if(testSetReader.document(i).getField("comments")!=null)
			{
				String comments = testSetReader.document(i).getField("comments").stringValue();
				System.out.println("comments="+comments);
				//doc.add(new Field("comments",comments,Store.YES,Index.ANALYZED));
			}
			//writer.addDocument(doc);
		}
		 writer.close();
}}
