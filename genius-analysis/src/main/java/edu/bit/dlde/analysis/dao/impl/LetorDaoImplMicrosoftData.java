package edu.bit.dlde.analysis.dao.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import edu.bit.dlde.analysis.dao.ILetorDao;
import edu.bit.dlde.analysis.model.LetorModel;

public class LetorDaoImplMicrosoftData implements ILetorDao {

	public List<LetorModel> readDatatoList(File file) throws IOException {
		List<LetorModel> list = new ArrayList<LetorModel>();
		List<String> tmpList = readLines(file);
		LetorModel model = null;
		double[] vector = null;
		for (String line : tmpList) {
			String[] tmpData = StringUtils.split(line);// every line is a
														// array
			int i = -2;
			model = new LetorModel();
			vector = new double[136];
			for (String data : tmpData) {
				if (i == -2) {// relvance
					model.setRelevance(Integer.parseInt(data));
				} else if (i == -1) {// query id
					model.setqId(Integer.parseInt(data.split(":")[1]));
				} else {
					String vec = data.split(":")[1];
					vector[i] = Double.parseDouble(vec);
				}
				i++;
			}
			model.setVector(vector);
			list.add(model);
		}
		return list;
	}

	public List<String> readLines(File file){
		List<String> tmpList=null;
		try {
			tmpList = FileUtils.readLines(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return tmpList;
	}
	
	public List<String> nioReadLines(File file) {
		return null;
	}
	
	
	public Map<Integer, List<LetorModel>> readData(File file) {
		Map<Integer, List<LetorModel>> map = new HashMap<Integer, List<LetorModel>>();
		List<LetorModel> list = null;
		List<String> tmpList;
		tmpList = readLines(file);
		LetorModel model = null;
		double[] vector = null;
		for (String line : tmpList) {
			String[] tmpData = StringUtils.split(line);// every line is a array
			int i = -2;
			model = new LetorModel();
			vector = new double[136];
			for (String data : tmpData) {
				if (i == -2) {// relvance
					model.setRelevance(Integer.parseInt(data));
				} else if (i == -1) {// query id
					model.setqId(Integer.parseInt(data.split(":")[1]));
				} else {
					String vec = data.split(":")[1];
					vector[i] = Double.parseDouble(vec);
				}
				i++;
			}
			model.setVector(vector);
			if(map.containsKey(model.getqId())){
				list=map.get(model.getqId());
				list.add(model);
			}else{
				list= new ArrayList<LetorModel>();
				list.add(model);
				map.put(model.getqId(), list);
			}
		}
		return map;
	}
}
