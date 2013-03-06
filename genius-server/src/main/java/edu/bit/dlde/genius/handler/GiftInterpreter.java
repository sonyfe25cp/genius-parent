package edu.bit.dlde.genius.handler;

import org.apache.lucene.analysis.Analyzer;

import proj.zoie.api.indexing.ZoieIndexable;
import proj.zoie.api.indexing.ZoieIndexableInterpreter;
import edu.bit.dlde.genius.core.Gift;

public class GiftInterpreter implements ZoieIndexableInterpreter<Gift>{

	public static int count=0;
	
	long _delay;
    final Analyzer _analyzer;
	
	public GiftInterpreter() {
		 this(0,null);
	}
	public GiftInterpreter(long delay) {
		this(delay,null);
	}
	public GiftInterpreter(long delay, Analyzer analyzer) {
		super();
		this._delay = delay;
		this._analyzer = analyzer;
	}

	public ZoieIndexable convertAndInterpret(Gift gift) {
		count++;
		return new GiftIndexable(count,gift,_delay,_analyzer);
	}
}
