package edu.bit.dlde.genius.handler;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import edu.bit.dlde.eventserver.adapter.EventAdapter;
import edu.bit.dlde.eventserver.model.Request;
import edu.bit.dlde.eventserver.model.Response;
import edu.bit.dlde.genius.core.Genius;
import edu.bit.dlde.genius.model.AdminForm;
import edu.bit.dlde.genius.model.IndexForm;
import edu.bit.dlde.genius.model.Order;
import edu.bit.dlde.genius.model.QueryForm;
import edu.bit.dlde.genius.model.ResultsUnit;
import edu.bit.dlde.genius.utils.GsonUtils;
import edu.bit.dlde.utils.DLDELogger;
import edu.bit.dlde.utils.DLDETools;

public class SearchServerHandler extends EventAdapter {

	private DLDELogger logger = new DLDELogger();

	private ResultsUnit unit;

	private String json;

//	private Order order;

	private QueryForm queryForm;

	private IndexForm indexForm;

	private AdminForm adminForm;

	Genius genius = Genius.getInstance();

	public SearchServerHandler() {

	}

	public void onRead(Request request) throws Exception {
		logger.info("--- i receive the query---");
		// String keyword = new String(request.getDataInput());
		// logger.info("keywords:" + keyword);

		logger.info("do nothing on Read");

		logger.info("--- i recored this query to local---");
	}

	public void onWrite(Request request, Response response) {
		logger.info("--- i receive the query ---");
		Order order =null;
		try {
			order = (Order) DLDETools.byteArraytoObject(request.getDataInput());
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("convert error!");
		}
		if (order == null) {
			logger.error(" error !!");
			return;
		}

		int type = order.getType();
		logger.info("order.type:"+type);
		switch (type) {

		case Order.QUERY:

			queryForm = (QueryForm) order;

			logger.info("keywords:" + queryForm.getKeyWords());

			logger.info("--- i begin to retrieve this query ---");
			
			genius.setQueryForm(queryForm);
			
			genius.bingo();

			logger.info("--- i return back the answers ---");

			unit = genius.getUnit();

			json = GsonUtils.getResultsUnitJson(unit);
//			logger.info(json);
			try {
				logger.info("send back to client"
						+ json.getBytes("UTF-8").length);
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}

			try {
				response.send(json.getBytes("UTF-8"));
			} catch (IOException e) {
				e.printStackTrace();
			}

			break;
		case Order.INDEX:

			logger.info("---- i begin to add new gift to genius ----");
			indexForm = (IndexForm) order;
			genius.addGift(indexForm.getGift());

			break;
		case Order.ADMIN:

			logger.info("---- admin want to do something ----");
			adminForm = (AdminForm) order;
			boolean flag = adminForm.isShutdown();
			if (flag) {
				genius.shutDown();
			}

			break;
			
		case Order.UPDATE:
			logger.info("update a document");
			break;
		case Order.DELETE:
			logger.info("delete a document");
			break;
		}
	}

}
