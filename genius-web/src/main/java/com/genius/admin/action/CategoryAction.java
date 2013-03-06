package com.genius.admin.action;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.ServletContext;

import com.genius.dao.CategoryDAOMongoImpl;
import com.genius.model.Category;
import com.genius.model.TrainingFile;
import com.google.gson.JsonObject;

import edu.bit.dlde.analysis.classification_jxt_forweb.Train;
import edu.bit.dlde.utils.DLDELogger;
/**
 *	@author xiaotian
 **/
@Controller
@RequestMapping("/admin/category")
public class CategoryAction extends Thread {
	private DLDELogger logger;
	private CategoryDAOMongoImpl catDAO;
	private int fileNumPerPage=10;
	private ArrayList<Boolean> fileState41Cat;
	private List<TrainingFile> TF41Cat;
	
	private String filePath = "uploadify/uploadedFiles/TrainingFileData/";
	private String tmpTrainingFileDir=null;
	private String trainingFileDir = null;
	
	public CategoryAction(CategoryDAOMongoImpl  catDAO){
		this.catDAO = catDAO;
	}
	public CategoryAction(){}
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView categoryList(HttpServletRequest request,
			@RequestParam(value="pageNo",required=false,defaultValue="1") int pageNo,
			@RequestParam(value="uploadResult",required=false) String uploadResult) throws IOException {
		catDAO.classificationState_Init();
		if(uploadResult!=null)
			uploadResult=URLDecoder.decode(uploadResult, "UTF-8");
		ArrayList<Category> categories_all=(ArrayList<Category>) catDAO.loadCategoriesByStatus(true);
		System.out.println("Action=List,uploadResult=|"+uploadResult+"|");
		int total=categories_all.size();
		int totalP=(total%fileNumPerPage==0)?(total/fileNumPerPage):((total/fileNumPerPage)+1);
		ArrayList<Category> categories = (ArrayList<Category>) catDAO.loadCategories(
				(pageNo-1)*fileNumPerPage, fileNumPerPage);
		System.out.println("trainState="+catDAO.classificationState_getState().get("trainState"));
		
		tmpTrainingFileDir = (request.getRealPath("/")+filePath+"tmpTrainingFiles/").replace("\\","/");
		trainingFileDir = (request.getRealPath("/")+filePath+"trainingFiles/").replace("\\","/");
		
		File f = new File(tmpTrainingFileDir);
		if(!f.exists())
			f.mkdirs();
		f = new File(trainingFileDir);
		if(!f.exists())
			f.mkdirs();
	
		return new ModelAndView("category-list").addObject("categories",categories)
			.addObject("pageNo", pageNo).addObject("totalP", totalP)
			.addObject("uploadResult", uploadResult)
			.addObject("trainState", catDAO.classificationState_getState().get("trainState"));
	}

	@RequestMapping(value = "/update/{operation}", method = RequestMethod.GET)
	public ModelAndView categoryList(
			@PathVariable("operation") String operation,
			@RequestParam(value="category",required=false) String category) {
		System.out.println("in update {operation}="+operation+"|category="+category);
		String currentCategory=null;
		List<String> cats=new ArrayList<String>();
		List<Category> cs=null;
		
		if(operation.equals("new")){
			cs = catDAO.loadCategoriesByStatus(false);
			for(int i=0;i<cs.size();i++)
				cats.add(cs.get(i).getName());
			if(cats.size()!=0&&cats!=null)
			{
				if(category==null)
					currentCategory = cats.get(0);
				else 
					currentCategory = category;
			}
		}
		if(operation.equals("modify")||operation.equals("view")){
			cs = catDAO.loadCategoriesByStatus(true);
			currentCategory=category;
			for(int i=0;i<cs.size();i++)
				cats.add(cs.get(i).getName());
		}
		if(currentCategory!=null)
		{
			TF41Cat = catDAO.loadTrainingFile(currentCategory);
			List<TrainingFile> TF41CatAndPage = TF41Cat.subList(0, fileNumPerPage);
			System.out.println("TF41CatAndPage.size="+TF41CatAndPage.size());
//			List<CategoryFileStateChange> changeList41Page = catDAO.loadFileState(1);
			//获得此页面的checkbox的选中状态
			fileState41Cat = catDAO.loadFileState41Cat(currentCategory);
			List<Boolean> cbCheckState = fileState41Cat.subList(0, fileNumPerPage);
			System.out.println("cbCheckState.size="+cbCheckState.size());
//			if(changeList41Page==null||changeList41Page.size()==0)
//			{
//				for(int i=0;i<TF41CatAndPage.size();i++)
//					cbCheckState.add(TF41CatAndPage.get(i).isEnabled());
//			}
//			else 
//			for(int i=0;i<TF41CatAndPage.size();i++)
//			{
//				boolean isIn = false;
//				for(int j=0;j<changeList41Page.size();j++)
//					if(changeList41Page.get(j).getPosNo()==i)
//						{
//							isIn=true;
//							cbCheckState.add(changeList41Page.get(j).isEnabled());
//							break;
//						}
//				if(isIn==false)
//					cbCheckState.add(TF41CatAndPage.get(i).isEnabled());
//			}
			int total=TF41Cat.size();
			for(int i=0;i<TF41CatAndPage.size();i++)
				TF41CatAndPage.get(i).setBody(null);
			
			System.out.println("JSONArray.fromObject="+JSONArray.fromObject(TF41CatAndPage));
			
			int totalP=(total%fileNumPerPage==0)?(total/fileNumPerPage):((total/fileNumPerPage)+1);
			return new ModelAndView("category-update")
						.addObject("cats", cats)
						.addObject("TF41CatAndPage", JSONArray.fromObject(TF41CatAndPage))
						.addObject("cbCheckState",JSONArray.fromObject(cbCheckState))
						.addObject("currentCategory", currentCategory)
						.addObject("totalP", totalP)
						.addObject("operation", operation)
						.addObject("pageNo", 1);
		}
		return new ModelAndView("category-update").addObject("pageNo", 1);
	}
	
//	@RequestMapping(value="/update/changeCategory/{operation}",method=RequestMethod.GET)
//	public ModelAndView changeCategory(
//			@PathVariable("operation") String operation,
//			@RequestParam(value="wantedCategory",required=true)String wantedCategory){
//		System.out.println("{operation}="+operation);
//		List<String> cats=new ArrayList<String>();
//		List<Category> cs=null;
//		if(operation.equals("new"))
//		{
//			cs = catDAO.loadCategoriesByStatus(false);
//			for(int i=0;i<cs.size();i++)
//				cats.add(cs.get(i).getName());
//		}
//		if(operation.equals("modify")||operation.equals("view"))
//		{
//			cs = catDAO.loadCategoriesByStatus(true);
//			for(int i=0;i<cs.size();i++)
//				cats.add(cs.get(i).getName());
//		}
//		catDAO.removeFileStateChange();
//		List<TrainingFile> TF41Cat = catDAO.loadTrainingFile(wantedCategory);
//		List<TrainingFile> TF41CatAndPage= TF41Cat.subList(0, fileNumPerPage);
////		List<CategoryFileStateChange> changeList41Page = catDAO.loadFileState(1);
//		//获取需要获得的页面的checkbox的选中状态
//		List<Boolean> cbCheckState = new ArrayList<Boolean>();
//		if(changeList41Page==null||changeList41Page.size()==0)
//			for(int i=0;i<TF41CatAndPage.size();i++)
//				cbCheckState.add(TF41CatAndPage.get(i).isEnabled());
//		else 
//			for(int i=0;i<TF41CatAndPage.size();i++)
//				cbCheckState.add(TF41CatAndPage.get(i).isEnabled());
//		int total=TF41Cat.size();
//		int totalP=(total%fileNumPerPage==0)?(total/fileNumPerPage):((total/fileNumPerPage)+1);
//		return new ModelAndView("category-update").addObject("cats", cats)
//				.addObject("TF41CatAndPage", TF41CatAndPage).addObject("currentCategory", wantedCategory)
//				.addObject("pageNo", 1).addObject("totalP", totalP).addObject("cbCheckState", cbCheckState)
//				.addObject("operation", operation);
//	}
	
//	@RequestMapping(value="/update/changePage/{operation}",method=RequestMethod.GET)
//	public ModelAndView changeCategory(
//			@PathVariable("operation") String operation,
//			@RequestParam(value="wantedCategory",required=true) String currentCategory,
//			@RequestParam(value="currentPageNo",required=true)String currentPN,
//			@RequestParam(value="wantedPageNo",required=true)String wantedPN,
//			@RequestParam(value="stateChangeStr",required=true) String stateChangeStr)
//	{
//		System.out.println("{operation}="+operation);
//		int currentPageNo = Integer.parseInt(currentPN);
//		int wantedPageNo = Integer.parseInt(wantedPN);
//		
//		List<String> cats=new ArrayList<String>();
//		List<Category> cs=null;
//		if(operation.equals("new"))
//		{
//			cs = catDAO.loadCategoriesByStatus(false);
//			for(int i=0;i<cs.size();i++)
//				cats.add(cs.get(i).getName());
//		}
//		if(operation.equals("modify")||operation.equals("view"))
//		{
//			cs = catDAO.loadCategoriesByStatus(true);
//			for(int i=0;i<cs.size();i++)
//				cats.add(cs.get(i).getName());
//		}
//		catDAO.analyzeAndSaveFileState(stateChangeStr,currentPageNo);
//		List<TrainingFile> TF41Cat = catDAO.loadTrainingFile(currentCategory);
//		List<TrainingFile> TF41CatAndPage= TF41Cat.subList((wantedPageNo-1)*fileNumPerPage, wantedPageNo*fileNumPerPage);
//		List<CategoryFileStateChange> changeList41Page = catDAO.loadFileState(wantedPageNo);
//		//获取需要获得的页面的checkbox的选中状态
//		List<Boolean> cbCheckState = new ArrayList<Boolean>();
//		if(changeList41Page==null||changeList41Page.size()==0)
//		{
//			for(int i=0;i<TF41CatAndPage.size();i++)
//				cbCheckState.add(TF41CatAndPage.get(i).isEnabled());
//		}
//		else 
//		for(int i=0;i<TF41CatAndPage.size();i++)
//		{
//			
//			boolean isIn = false;
//			for(int j=0;j<changeList41Page.size();j++)
//				if(changeList41Page.get(j).getPosNo()==i)
//					{
//						isIn=true;
//						cbCheckState.add(changeList41Page.get(j).isEnabled());
//						break;
//					}
//			if(isIn==false)
//				cbCheckState.add(TF41CatAndPage.get(i).isEnabled());
//		}
//		int total=TF41Cat.size();
//		int totalP=(total%fileNumPerPage==0)?(total/fileNumPerPage):((total/fileNumPerPage)+1);
//		return new ModelAndView("category-update").addObject("cats", cats)
//				.addObject("TF41CatAndPage", TF41CatAndPage).addObject("currentCategory",
//						currentCategory).addObject("totalP", totalP).addObject("pageNo", wantedPageNo)
//						.addObject("cbCheckState", cbCheckState).addObject("operation", operation);
//		
//	}
	
	@RequestMapping(value="/update/changePage/{operation}",method=RequestMethod.POST)
	public @ResponseBody JSONObject changePage(
			@PathVariable("operation") String operation,
			@RequestParam(value="wantedCategory",required=true) String currentCategory,
			@RequestParam(value="currentPageNo",required=true)String currentPN,
			@RequestParam(value="wantedPageNo",required=true)String wantedPN,
			@RequestParam(value="stateChangeStr",required=true) String stateChangeStr)
	{
		System.out.println();
		int currentPageNo = Integer.parseInt(currentPN);
		int wantedPageNo = Integer.parseInt(wantedPN);
		System.out.println("in changePage,{operation}="+operation+",stateChangeStr="+stateChangeStr
				+"currentPageNo="+currentPN+"wantedPageNo="+wantedPN);
		
		List<String> cats=new ArrayList<String>();
		List<Category> cs=null;
		if(operation.equals("new"))
		{
			cs = catDAO.loadCategoriesByStatus(false);
			for(int i=0;i<cs.size();i++)
				cats.add(cs.get(i).getName());
		}
		if(operation.equals("modify")||operation.equals("view"))
		{
			cs = catDAO.loadCategoriesByStatus(true);
			for(int i=0;i<cs.size();i++)
				cats.add(cs.get(i).getName());
		}
		String[] stateChangeSplit = stateChangeStr.split("/");
		//只记录TrainingFile的最终enabled信息
		System.out.println("before fileState41Cat="+fileState41Cat);
		for(int i=0;i<stateChangeSplit.length;i++)
		{
			if(stateChangeSplit[i].equals("1"))
				fileState41Cat.set((currentPageNo-1)*fileNumPerPage+i,true);
			else fileState41Cat.set((currentPageNo-1)*fileNumPerPage+i,false);
		}
//		System.out.println((currentPageNo-1)*fileNumPerPage+"->"+currentPageNo*fileNumPerPage+":"+
//				TF41Cat.subList((currentPageNo-1)*fileNumPerPage, currentPageNo*fileNumPerPage));
//		
		System.out.println("after fileState41Cat="+fileState41Cat);
		
		List<TrainingFile> TF41CatAndPage= TF41Cat.subList((wantedPageNo-1)*fileNumPerPage, wantedPageNo*fileNumPerPage);
		//获取需要获得的页面的checkbox的选中状态
		List<Boolean> cbCheckState = fileState41Cat.subList((wantedPageNo-1)*fileNumPerPage, wantedPageNo*fileNumPerPage);
		System.out.println("cbCheckState="+cbCheckState);
		//		if(changeList41Page==null||changeList41Page.size()==0)
//		{
//			for(int i=0;i<TF41CatAndPage.size();i++)
//				cbCheckState.add(TF41CatAndPage.get(i).isEnabled());
//		}
//		else 
//		for(int i=0;i<TF41CatAndPage.size();i++)
//		{
//			boolean isIn = false;
//			for(int j=0;j<changeList41Page.size();j++)
//				if(changeList41Page.get(j).getPosNo()==i)
//					{
//						isIn=true;
//						cbCheckState.add(changeList41Page.get(j).isEnabled());
//						break;
//					}
//			if(isIn==false)
//				cbCheckState.add(TF41CatAndPage.get(i).isEnabled());
//		}
		for(int i=0;i<TF41CatAndPage.size();i++)
			TF41CatAndPage.get(i).setBody(null);
		
		int total=TF41Cat.size();
		int totalP=(total%fileNumPerPage==0)?(total/fileNumPerPage):((total/fileNumPerPage)+1);
		System.out.println("inserting to  map");
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("TF41CatAndPage", TF41CatAndPage);
		map.put("currentCategory",currentCategory);
		map.put("totalP", totalP);
		map.put("pageNo", wantedPageNo);
		map.put("cbCheckState", cbCheckState);
		map.put("operation", operation);
//		System.out.println("map="+map);
//		System.out.println(JSONObject.fromObject(map));
		System.out.println("leaving changePage");
		return JSONObject.fromObject(map);
		
//		return new ModelAndView("category-update").addObject("cats", cats)
//				.addObject("TF41CatAndPage", TF41CatAndPage).addObject("currentCategory",
//						currentCategory).addObject("totalP", totalP).addObject("pageNo", wantedPageNo)
//						.addObject("cbCheckState", cbCheckState).addObject("operation", operation);
		
	}
	
	@RequestMapping(value="/update/submit/{operation}",method=RequestMethod.GET)
	public String submitAdd(
			@PathVariable("operation") String operation,
		@RequestParam(value="submitedCategory",required=true) String submitedCategory){
		System.out.println("in submit...");
		System.out.println("submitedCategory="+submitedCategory);
		System.out.println("fileState41Cat="+fileState41Cat);
		catDAO.syncFileStateChange(submitedCategory,TF41Cat,fileState41Cat);
		System.out.println("sync completed!");
		catDAO.changeCategoryState(submitedCategory,true);
		System.out.println("operation="+operation);
		if(!operation.equals("view"))
		{
			catDAO.classificationState_setState("trainState", "NOT_TRAINED");
			catDAO.classificationState_setState("classifyState", "CLASSIFY_DISABLED");
		}
		System.out.println("leaving submit...");
		return "redirect:/admin/category/list";
	}
	
//	@RequestMapping(value="/trainModel",method=RequestMethod.GET)
//	public String trainModel()throws IOException{
//		CategoryAction ca = new CategoryAction(catDAO);
//		System.out.println("train starting");
//		catDAO.classificationState_setState("trainState", "TRAINING");
//
//		System.out.println("training");
//		ca.start();
//		return "redirect:/admin/category/list";
//	}
	
	@RequestMapping(value="/trainModel",method=RequestMethod.GET)
	public @ResponseBody String trainModel()throws IOException{
		System.out.println("train starting");
		catDAO.classificationState_setState("trainState", "TRAINING");
		Train.trainModel(catDAO.getDatastore());
		catDAO.classificationState_setState("trainState", "TRAINED");
		catDAO.classificationState_setState("classifyState", "CLASSIFIED");
		return "succeed";
	}
	
	@RequestMapping(value="/trainModel_plsa",method=RequestMethod.GET)
	public String trainModel_plsa()throws IOException{
		catDAO.classificationState_setState("trainState", "TRAINING");
		catDAO.plsa_training();
		catDAO.classificationState_setState("trainState", "TRAINED");
		catDAO.classificationState_setState("classifyState", "CLASSIFIED");
		return "redirect:/admin/category/list";
	}
	
//	@RequestMapping(value="/{name}",method=RequestMethod.GET)
//	public ModelAndView edit(@PathVariable("name") String name){
//		logger.info("begin to show "+name);
//		Category category=catDAO.loadCategory(name);
//		return new ModelAndView("category-update").addObject("category", category).addObject("seeds",null);
//	}
	
	@RequestMapping(value="/select/{name}",method=RequestMethod.GET)
	public ModelAndView edit(@PathVariable("name") String name){
		logger.info("begin to show "+name);
		List<TrainingFile> trainingFiles = catDAO.loadTrainingFile(name);
		List<Category> categories = catDAO.loadCategories();
		return new ModelAndView("category-update").addObject("categories", categories)
				.addObject("seeds",null).addObject("trainingFiles",trainingFiles)
				.addObject("currentCategory", name);
	}
	
	@RequestMapping(value="/uploadTrainingFile",method=RequestMethod.GET)
	public String uploadTrainingFile(
			@RequestParam(value="dir",required=true) String dir) throws UnsupportedEncodingException 
	{
		String uploadResult=null;
		File f = new File(dir);
		if(!f.isDirectory())
			return "redirect:/admin/category/list?uploadResult="+URLEncoder.encode("$$$", "UTF-8");
		List<String> dirs = new ArrayList<String>();
		dirs.add(dir);
		for(int i=0;i<dirs.size();i++)
		 	dirs.get(i).replace("\\","/");
		uploadResult = catDAO.uploadTrainingFile(dirs);
		System.out.println("uploadResult="+uploadResult);
		return "redirect:/admin/category/list?uploadResult="+URLEncoder.encode(uploadResult, "UTF-8");

	}
	
	@RequestMapping(value="/uploadify",method=RequestMethod.POST)
	public void uploadify(@RequestParam MultipartFile file,
			   ModelMap modelMap,HttpServletRequest request,HttpServletResponse response)
			throws IllegalStateException, IOException{
//		System.out.println("request.getSession().id="+request.getSession().getId());
//		System.out.println(file.getOriginalFilename());
		String filePath = tmpTrainingFileDir+file.getOriginalFilename();
		System.out.println("filePath="+filePath);
		File localFile = new File(filePath);
		file.transferTo(localFile);
	}

	@RequestMapping(value="/uploadifySucceed",method=RequestMethod.GET)
	public String uploadifySucceed(HttpServletRequest request) throws IOException
	{
		List<String> duplicateFilelist= new ArrayList<String>();
		List<String> dirs = new ArrayList<String>();
		ArrayList<File> tmpFiles = new ArrayList<File>();
		
	    //对于tmpTrainingFiles目录下的所有文件广度遍历，去除已经在TrainingFiles此目录(先前上传)中有(且不在
		//tmpTrainingFile目录中)的文件。然后将剩余的不重复文件上传到mongodb，最后把不重复文件存到TrainingFiles中。
    	//广度遍历：不是目录->添加，是目录->广度遍历其中的所有文件
		Queue<File> fileQueue1 = new LinkedList<File>();
		fileQueue1.offer(new File(tmpTrainingFileDir));
		while(fileQueue1.size()!=0)
		{
			File file1 = fileQueue1.poll();
			if(file1.isDirectory())
			{
				File[] file1s = file1.listFiles();
				for(int j=0;j<file1s.length;j++)
					fileQueue1.offer(file1s[j]);
			}
			else
				tmpFiles.add(file1);
		}
		
		System.out.println("tmpFile:");
		for(int i=0;i<tmpFiles.size();i++)
			System.out.println(i+":"+tmpFiles.get(i));
		
    	//广度遍历读出TrainingFiles中的文件
		Set<String> trainingFileNameSet = new HashSet<String>();
		Queue<File> fileQueue2 = new LinkedList<File>();
		fileQueue2.offer(new File(trainingFileDir));
		while(fileQueue2.size()!=0)
		{
			File file1 = fileQueue2.poll();
			if(file1.isDirectory())
			{
				File[] file1s = file1.listFiles();
				for(int j=0;j<file1s.length;j++)
					fileQueue2.offer(file1s[j]);
			}
			else
				trainingFileNameSet.add(file1.getName());
		}
    	
    	for(int i=0;i<tmpFiles.size();i++)
    		if(trainingFileNameSet.contains(tmpFiles.get(i).getName()))
    			{
    				duplicateFilelist.add(tmpFiles.get(i).getName());
    				tmpFiles.get(i).delete();
    				tmpFiles.remove(i);
    				i--;
    			}
    	System.out.println("trainingFileNameSet="+trainingFileNameSet);
    	
    	//上传剩余文件
		dirs.add(tmpTrainingFileDir);
		System.out.println("path="+tmpTrainingFileDir);
		for(int i=0;i<dirs.size();i++)
		 	dirs.get(i).replace("\\","/");
		String uploadResult = catDAO.uploadTrainingFile(dirs);
		if(duplicateFilelist.size()!=0)
		{
			uploadResult+="|以下文件重复,未上传: ";
			for(int i=0;i<duplicateFilelist.size();i++)
				uploadResult+="|	"+duplicateFilelist.get(i);
		}
		
		System.out.println("uploadResult_1="+uploadResult);
		for(int i=0;i<tmpFiles.size();i++)
			FileUtils.copyFileToDirectory(tmpFiles.get(i), new File(trainingFileDir));
//		FileUtils.forceDelete(new File(tmpTrainingFileDir));
//			cleanDirectory(new File(tmpTrainingFileDir));
//			tmpFiles.get(i).renameTo(new File(trainingFileDir+tmpFiles.get(i).getName()));
		System.out.println("uploadResult_2="+uploadResult);
		//删除tmp中的文件
		Stack<File> fileStackD = new Stack<File>();
		fileStackD.push(new File(tmpTrainingFileDir));
		while(fileStackD.size()!=0)
		{
			File file1 = fileStackD.pop();
			System.out.println("Poping:"+file1.getName());
			if(file1.isDirectory())
			{
				File[] file1s = file1.listFiles();
				for(int j=0;j<file1s.length;j++)
					fileStackD.push(file1s[j]);
			}
			else
				{
					System.out.print("|"+file1.getName());
					file1.delete();
				}
		}
		System.out.println("uploadResult_3="+uploadResult);
		
		return "redirect:/admin/category/list?uploadResult="+URLEncoder.encode(uploadResult, "UTF-8");
	}
	
	@RequestMapping(value="/delete/{name}",method=RequestMethod.GET)
	public String remove(@PathVariable("name") String name){
		logger.info("begin to delete "+name);
		catDAO.removeCategory(name);
		catDAO.classificationState_setState("trainState", "NOT_TRAINED");
		catDAO.classificationState_setState("classifyState", "CLASSIFY_DISABLED");
		return "redirect:/admin/category/list";
	}
	
	@RequestMapping(value = "/set-enabled/{name}", method = RequestMethod.GET)
	public String categorySetEnabled(@PathVariable("name") String name) {
		catDAO.changeCategoryStatus(name);
		return "redirect:/admin/category/list";
	}
	
	@RequestMapping(value = "/set-disabled/{name}", method = RequestMethod.GET)
	public String categorySetDisabled(@PathVariable("name") String name) {
		catDAO.changeCategoryStatus(name);
		return "redirect:/admin/category/list";
	}
	
	public void run(){
		try {
			System.out.println("train thread on the run");
			Train.trainModel(catDAO.getDatastore());
			catDAO.classificationState_setState("trainState", "TRAINED");
			catDAO.classificationState_setState("classifyState", "CLASSIFIED");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public DLDELogger getLogger() {
		return logger;
	}
	
	public void setLogger(DLDELogger logger) {
		this.logger = logger;
	}
	
	public CategoryDAOMongoImpl getCatDAO() {
		return catDAO;
	}
	
	public void setCatDAO(CategoryDAOMongoImpl catDAO) {
		this.catDAO = catDAO;
	}
	public int getNumPerPage() {
		return fileNumPerPage;
	}

	public void setNumPerPage(int fileNumPerPage) {
		this.fileNumPerPage = fileNumPerPage;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getTmpTrainingFileDir() {
		return tmpTrainingFileDir;
	}
	public void setTmpTrainingFileDir(String tmpTrainingFileDir) {
		this.tmpTrainingFileDir = tmpTrainingFileDir;
	}
	public String getTrainingFileDir() {
		return trainingFileDir;
	}
	public void setTrainingFileDir(String trainingFileDir) {
		this.trainingFileDir = trainingFileDir;
	}
	
	public int getFileNumPerPage() {
		return fileNumPerPage;
	}
	public void setFileNumPerPage(int fileNumPerPage) {
		this.fileNumPerPage = fileNumPerPage;
	}
	public ArrayList<Boolean> getFileState41Cat() {
		return fileState41Cat;
	}
	public void setFileState41Cat(ArrayList<Boolean> fileState41Cat) {
		this.fileState41Cat = fileState41Cat;
	}
	public List<TrainingFile> getTF41Cat() {
		return TF41Cat;
	}
	public void setTF41Cat(List<TrainingFile> tF41Cat) {
		TF41Cat = tF41Cat;
	}
	public static void main(String[] args)
	{
	
	}
}
