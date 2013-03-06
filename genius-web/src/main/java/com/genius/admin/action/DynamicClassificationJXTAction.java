package com.genius.admin.action;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.genius.dao.CategoryDAOMongoImpl;
import com.genius.dao.ClassificationJXTDaoImpl;
import com.genius.dao.IClassificationJXTDao;
import com.genius.dao.PlsaDAOMongoImpl;
import com.genius.model.Category;

import edu.bit.dlde.analysis.classification_jxt_forweb.ClassificationResult;
import edu.bit.dlde.analysis.classification_jxt_forweb.Classifier;
import edu.bit.dlde.analysis.classification_jxt_forweb.Train;
import edu.bit.dlde.utils.DLDELogger;

//import com.genius.dao.GeniusDao;
//import com.genius.model.NewsReport;
//import com.genius.utils.Page;

/**
 * 分类类
 * 
 * @author xiaotian
 * 
 */
@Controller
@RequestMapping("/admin/classification_jxt")
public class DynamicClassificationJXTAction {
	private DLDELogger logger;
	private ClassificationJXTDaoImpl knnDAO;
	private PlsaDAOMongoImpl plsaDAO;
	private CategoryDAOMongoImpl catDAO;
	private List<String> localIds=null;
	private List<String> pageIds = null;
	
	private String filePath = "uploadify/uploadedFiles/classifyFileData/";
	private String tmpClassifyFileDir=null;
	private String classifyFileDir = null;

	public DynamicClassificationJXTAction(){}
	public DynamicClassificationJXTAction(List<String> localIds,
			List<String> pageIds,ClassificationJXTDaoImpl knnDAO)
	{
		this.knnDAO = knnDAO;
		this.localIds = localIds;
		this.pageIds = pageIds;
	}
	
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView List(HttpServletRequest request) throws IOException {
		List<Category> categories = knnDAO.loadCategories(true);
		
		knnDAO.getCatDAO().classificationState_Init();
		tmpClassifyFileDir = (request.getRealPath("/")+filePath+"tmpClassifyFiles/").replace("\\","/");
		classifyFileDir =    (request.getRealPath("/")+filePath+"classifyFiles/").replace("\\","/");
		File f = new File(tmpClassifyFileDir);
		if(!f.exists())
			f.mkdirs();
		f = new File(classifyFileDir);
		if(!f.exists())
			f.mkdirs();
		
		return new ModelAndView("classification_jxt_list").addObject("categories", 
				   categories).addObject("classifyState", knnDAO.getCatDAO()
				   .classificationState_getState().get("classifyState"));
	}
	
	//显示每一类的细目
	@RequestMapping(value = "/details", method = RequestMethod.GET)
	public ModelAndView Details(@RequestParam(value = "type") String type,
			HttpServletRequest request)throws IOException {
		List<ClassificationResult> oneTypeClassificationResults = knnDAO
				.getClassificationResults41Type(type);
		String contextPath = request.getRealPath("/");
		System.out.println("contextPath="+contextPath);
		for(int i=0;i<oneTypeClassificationResults.size();i++)
			{
				oneTypeClassificationResults.get(i).setId(oneTypeClassificationResults
						.get(i).getId().split("ROOT")[1]);
				System.out.println("i:"+i+" Id="+oneTypeClassificationResults.get(i).getId());
			}
					
		ModelAndView mav = new ModelAndView("classification_jxt_typeDetail");
		mav.addObject("oneTypeClassificationResults",oneTypeClassificationResults);
		return mav;
	}

	// 分类后把结果放到mongoDB的ClassificationResult中，返回：重定向到list页面。
	//然后list读取ClassificationResult表并显示
//	@RequestMapping(value = "/classify_KNN", method = RequestMethod.POST)
//	public String classify_KNN(@RequestParam("pageIdsString") String pageIdsString,
//			HttpServletRequest request)
//			throws IOException {
//		if(pageIdsString!=null)
//		{
//			System.out.println("classifyFileDir_c_K="+classifyFileDir);
//			localIds = new ArrayList<String>();
//			localIds.add(classifyFileDir);
//			List<String> pageIds = this.splitAsLine(pageIdsString);
//			knnDAO.deleteClassificationResultsAll();
//			DynamicClassificationJXTAction dca = new DynamicClassificationJXTAction
//					(localIds, pageIds, knnDAO);
//			knnDAO.getCatDAO().classificationState_setState("classifyState", "CLASSIFYING");
//			System.out.println("tmpClassifyFileDir_c_KNN="+tmpClassifyFileDir);
//			dca.start();
//		}
//		return "redirect:/admin/classification_jxt/list";
//	}
	@RequestMapping(value = "/classify_KNN", method = RequestMethod.POST)
	public @ResponseBody String classify_KNN(@RequestParam("pageIdsString") 
				String pageIdsString) throws IOException {
		System.out.println("pageIdsString="+pageIdsString);
		if(pageIdsString!=null)
		{
			System.out.println("classifyFileDir_c_K="+classifyFileDir);
			localIds = new ArrayList<String>();
			localIds.add(classifyFileDir);
			List<String> pageIds = this.splitAsLine(pageIdsString);
			knnDAO.deleteClassificationResultsAll();
			knnDAO.getCatDAO().classificationState_setState("classifyState", "CLASSIFYING");
			
			try {
				System.out.println("classify thread on the run");
				List<ClassificationResult> classificationResults = Classifier.Classify(
						localIds,pageIds,knnDAO.getDatastore());
				System.out.println("get_classificationResults:"+classificationResults);
				knnDAO.saveClassificationResults(classificationResults);
				knnDAO.getCatDAO().classificationState_setState("classifyState", "CLASSIFIED");
				//分类完成后删除文件
				System.out.println("2");
				DeleteDirAndLeaveEmpty(classifyFileDir);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			System.out.println("4");
		}
		System.out.println("5");
//		return "redirect:/admin/classification_jxt/list";
		System.out.println("OK");
		return "OK";
	}
	@RequestMapping(value="/uploadify",method=RequestMethod.POST)
	public void uploadify(@RequestParam MultipartFile file,
			   ModelMap modelMap,HttpServletRequest request,HttpServletResponse response)
			throws IllegalStateException, IOException{
//		System.out.println("request.getSession().id="+request.getSession().getId());
//		System.out.println(file.getOriginalFilename());
		String filePath = tmpClassifyFileDir+file.getOriginalFilename();
		System.out.println("filePath="+filePath);
		File localFile = new File(filePath);
		file.transferTo(localFile);
	}

	@RequestMapping(value="/uploadifySucceed",method=RequestMethod.GET)
	public String uploadifySucceed(HttpServletRequest request) throws IOException
	{
		ArrayList<File> tmpFiles = new ArrayList<File>();
    	//广度遍历：不是目录->添加，是目录->广度遍历其中的所有文件
		Queue<File> fileQueue1 = new LinkedList<File>();
		fileQueue1.offer(new File(tmpClassifyFileDir));
		System.out.println("tmpClassifyFileDir_uS="+tmpClassifyFileDir);
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
    	
    	//广度遍历读出classifyFiles中的文件
		Set<String> classifyFileNameSet = new HashSet<String>();
		Queue<File> fileQueue2 = new LinkedList<File>();
		fileQueue2.offer(new File(classifyFileDir));
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
				classifyFileNameSet.add(file1.getName());
		}
		
    	for(int i=0;i<tmpFiles.size();i++)
    		if(classifyFileNameSet.contains(tmpFiles.get(i).getName()))
    				{
    					tmpFiles.get(i).delete();
        				tmpFiles.remove(i);
        				i--;
    				}
    	
		//从tmp中移动文件
    	for(int i=0;i<tmpFiles.size();i++)
			FileUtils.copyFileToDirectory(tmpFiles.get(i), new File(classifyFileDir));
    	DeleteDirAndLeaveEmpty(tmpClassifyFileDir);
		
    	return "redirect:/admin/classification_jxt/list";
	}
	
	@RequestMapping(value = "/classify_plsa", method = RequestMethod.POST)
	public String classify_plsa(
//			@RequestParam("localIdsString") String localIdsString,
			@RequestParam("pageIdsString") String pageIdsString)
			throws IOException {
		if(pageIdsString!=null){
			localIds = new ArrayList<String>();
			localIds.add(classifyFileDir);
//			List<String> localIds = this.splitAsLine(localIdsString);
			List<String> pageIds = this.splitAsLine(pageIdsString);
			knnDAO.deleteClassificationResultsAll();
			System.out.println("localIds:"+localIds.get(0));
	//		System.out.println("pageIds:"+pageIds.get(0));
			knnDAO.getCatDAO().classificationState_setState("classifyState", "CLASSIFYING");
			List<ClassificationResult> classificationResults = plsaDAO.classify_plsa(localIds, pageIds);
			System.out.println("get_classificationResults:"+classificationResults);
			knnDAO.saveClassificationResults(classificationResults);
			catDAO.classificationState_setState("classifyState", "CLASSIFIED");
			DeleteDirAndLeaveEmpty(classifyFileDir);
			}
		return "redirect:/admin/classification_jxt/list";
	}
	
	private List<String> splitAsLine(String text) {
		if (text == null || text.length() == 0)
			return null;
		List<String> line = new ArrayList<String>();
		String str[] = text.split("\n");
		for (int i = 0; i < str.length; i++) {
			if (str[i].contains("\r")) {
				String str2[] = str[i].split("\r");
				for (int j = 0; j < str2.length; j++)
					line.add(str2[j]);
			} else
				line.add(str[i]);
		}
		return line;
	}

	public DLDELogger getLogger() {
		return logger;
	}

	public void setLogger(DLDELogger logger) {
		this.logger = logger;
	}
	public ClassificationJXTDaoImpl getKnnDAO() {
		return knnDAO;
	}

	public void setKnnDAO(ClassificationJXTDaoImpl knnDAO) {
		this.knnDAO = knnDAO;
	}
	
	public List<String> getLocalIds() {
		return localIds;
	}
	public void setLocalIds(List<String> localIds) {
		this.localIds = localIds;
	}
	public List<String> getPageIds() {
		return pageIds;
	}
	public void setPageIds(List<String> pageIds) {
		this.pageIds = pageIds;
	}
	public PlsaDAOMongoImpl getPlsaDAO() {
		return plsaDAO;
	}
	public void setPlsaDAO(PlsaDAOMongoImpl plsaDAO) {
		this.plsaDAO = plsaDAO;
	}
	public CategoryDAOMongoImpl getCatDAO() {
		return catDAO;
	}
	public void setCatDAO(CategoryDAOMongoImpl catDAO) {
		this.catDAO = catDAO;
	}
	
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getTmpClassifyFileDir() {
		return tmpClassifyFileDir;
	}
	public void setTmpClassifyFileDir(String tmpClassifyFileDir) {
		this.tmpClassifyFileDir = tmpClassifyFileDir;
	}
	public String getClassifyFileDir() {
		return classifyFileDir;
	}
	public void setClassifyFileDir(String classifyFileDir) {
		this.classifyFileDir = classifyFileDir;
	}
	
	public void run(){
		try {
			System.out.println("classify thread on the run");
			List<ClassificationResult> classificationResults = Classifier.Classify(
					localIds,pageIds,knnDAO.getDatastore());
			System.out.println("get_classificationResults:"+classificationResults);
			knnDAO.saveClassificationResults(classificationResults);
			knnDAO.getCatDAO().classificationState_setState("classifyState", "CLASSIFIED");
			//分类完成后删除文件
			DeleteDirAndLeaveEmpty(classifyFileDir);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	public void DeleteDirAndLeaveEmpty(String path){
		Stack<File> fileStackD = new Stack<File>();
		fileStackD.push(new File(path));
		while(fileStackD.size()!=0)
		{
			File file1 = fileStackD.pop();
			if(file1.isDirectory()&&file1.listFiles().length!=0)
			{ 
				fileStackD.push(file1);
				File[] file1s = file1.listFiles();
				for(int j=0;j<file1s.length;j++)
					fileStackD.push(file1s[j]);
			}
			else
					file1.delete();
		}
		new File(path).mkdirs();
	}
}
