package edu.bit.dlde.genius.handler;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;

import proj.zoie.api.indexing.AbstractZoieIndexable;
import edu.bit.dlde.genius.core.Gift;

public class GiftIndexable extends AbstractZoieIndexable{

	private int count;
	private Gift gift;
	private long _delay;
	private Analyzer analyzer;
	public GiftIndexable(int count,Gift gift,long _delay,Analyzer analyzer){
		this.count=count;
		this.gift=gift;
		this._delay=_delay;
		this.analyzer=analyzer;
	}
	
	public Document buildDocument(){
		Document doc=new Document();
		doc.add(new Field("id",gift.getUniqueGiftId(),Store.YES,Index.NO));
		doc.add(new Field("body",gift.getBody(),Store.YES,Index.ANALYZED, Field.TermVector.YES));
		if(gift.getTitle()!=null)
			doc.add(new Field("title",gift.getTitle(),Store.YES,Index.ANALYZED));
		if(gift.getAuthor()!=null)
			doc.add(new Field("author",gift.getAuthor(),Store.YES,Index.ANALYZED));
		if(gift.getUrl()!=null)
			doc.add(new Field("url",gift.getUrl(),Store.YES,Index.NOT_ANALYZED));
		doc.add(new Field("date",gift.getDate()!=null?gift.getDate():today(),Store.YES,Index.NO));
		if(gift.getCommentsToString()!=null)
			doc.add(new Field("comments",gift.getCommentsToString(),Store.YES,Index.ANALYZED, Field.TermVector.YES));
		if(gift.getSeedName()!=null)
			doc.add(new Field("seedname",gift.getCommentsToString(),Store.YES,Index.NOT_ANALYZED));
		try
        {
          Thread.sleep(_delay); // slow down indexing process
        }
        catch (InterruptedException e)
        {
        }
        return doc;
	}
	
	private String today(){
		DateFormat df=new SimpleDateFormat("yyyy-mm-dd");
		String d=df.format(new Date());
		return d;
	}
	
	public IndexingReq[] buildIndexingReqs() {
		return new IndexingReq[]{new IndexingReq(buildDocument(),getAnalyzer())};
	}

	
	public long getUID() {
		return count;
	}

	
	public boolean isDeleted() {
		return false;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public Gift getGift() {
		return gift;
	}

	public void setGift(Gift gift) {
		this.gift = gift;
	}

	public long get_delay() {
		return _delay;
	}

	public void set_delay(long _delay) {
		this._delay = _delay;
	}

	public Analyzer getAnalyzer() {
		return analyzer;
	}

	public void setAnalyzer(Analyzer analyzer) {
		this.analyzer = analyzer;
	}

}
