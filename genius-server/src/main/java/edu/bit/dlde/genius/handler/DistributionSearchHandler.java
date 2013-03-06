package edu.bit.dlde.genius.handler;

import edu.bit.dlde.eventserver.adapter.EventAdapter;
import edu.bit.dlde.eventserver.model.Request;
import edu.bit.dlde.eventserver.model.Response;
import edu.bit.dlde.utils.DLDELogger;

public class DistributionSearchHandler extends EventAdapter {

	private DLDELogger logger = new DLDELogger();

	public void onRead(Request request) throws Exception {
		String keyword = new String(request.getDataInput());
		logger.info("keywords:" + keyword);
	}

	public void onWrite(Request request, Response response) {
		logger.info("--i got the request ----");
		/**
		 * 
		 */
		logger.info("-- i send this request to searchers ---");
		/**
		 * 
		 */
		logger.info("-- i receive all the answers ---");
		/**
		 * 
		 */
		logger.info("-- i sort answers ");
		/**
		 * 
		 */
		logger.info("-- i send answers to client---");
		/**
		 * 
		 */
		logger.info("-- over --");

	}
}
