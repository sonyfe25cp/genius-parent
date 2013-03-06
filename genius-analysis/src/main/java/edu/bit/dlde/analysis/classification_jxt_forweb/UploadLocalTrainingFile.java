package edu.bit.dlde.analysis.classification_jxt_forweb;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import com.google.code.morphia.Datastore;
import com.google.code.morphia.Morphia;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

public class UploadLocalTrainingFile {
	public void saveLocalFile2TrainingFile(List<String> dirs) throws UnknownHostException, MongoException {
		ArrayList<TrainingFile> trainingFiles = new ArrayList<TrainingFile>();
		ArrayList<File> localFiles = new ArrayList<File>();
		Map<String,Integer> typeDocNum =  new HashMap<String,Integer>();
        for(int i=0;i<dirs.size();i++)
        {
        	File file = new File(dirs.get(i));
        	//不是目录->添加，是目录->广度遍历其中的所有文件
        	if(file.isDirectory())
        	{
        		Queue<File> fileQueue = new LinkedList<File>();
        		fileQueue.offer(file);
        		while(fileQueue.size()!=0)
        		{
        			File file1 = fileQueue.poll();
        			if(file1.isDirectory())
        			{
        				File[] file1s = file1.listFiles();
        				for(int j=0;j<file1s.length;j++)
        					fileQueue.offer(file1s[j]);
        			}
        			else
        				localFiles.add(file1);
        		}
        	}
        	else
        		localFiles.add(file);
        }
        
        for(File file:localFiles)
        {
        	String body="",tmp="";
        	try {
        		BufferedReader reader = new BufferedReader(new InputStreamReader(
						new FileInputStream(file),"UTF-8"));
        		while((tmp=reader.readLine())!=null)
        			body+=tmp;
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	String beforeSplit = file.getName().replace(".txt", ""); 
   	        String[] splited = beforeSplit.split("@");
   	        String type = null;
   	        String title = null;
   	        for(int k = 0; k<splited.length ; k++){
	    	  if(k == 0)
	    		type =splited[k];
	          if(k == splited.length-1)
	        	title =splited[k];
	      }
   	        
   	     if(typeDocNum.containsKey(type))
   	    	typeDocNum.put(type, typeDocNum.get(type)+1);
   	     else typeDocNum.put(type, 1);
   	    	
   	     trainingFiles.add(new TrainingFile(beforeSplit, title, type, body, null));
        }
        String[] keySet = typeDocNum.keySet().toArray(new String[0]);
        for(int i=0;i<keySet.length;i++)
        {
        	System.out.print(typeDocNum.get(keySet[i])+"|");
        }
        
        System.out.println("trainingFiles_Size="+trainingFiles.size());
        Mongo mongo = new Mongo("127.0.0.1", 27017);
		Morphia morphia = new Morphia();
		Datastore ds = morphia.createDatastore(mongo, "genius");
        String[]typeDocNumKeySet = typeDocNum.keySet().toArray(new String[0]);
        for(int i=0;i<typeDocNumKeySet.length;i++)
        {
        	if(ds.find(Category.class).field("name").equal(typeDocNumKeySet[i]).get()!=null)
        		{
        			Category c = ds.find(Category.class).field("name").equal(typeDocNumKeySet[i]).get();
        			ds.findAndModify(ds.find(Category.class).field("name").equal(typeDocNumKeySet[i])
        				, ds.createUpdateOperations(Category.class).set("trainingSetNum", 
        						c.getTrainingSetNum()+typeDocNum.get(typeDocNumKeySet[i])));
        		}
        	else 
        		{
//        			Category c = new Category(typeDocNumKeySet[i], typeDocNum.get(typeDocNumKeySet[i]));
        			Category c = new Category(typeDocNumKeySet[i], typeDocNum.get(typeDocNumKeySet[i]),true);
        			ds.save(c);
        		}
        }
        ds.save(trainingFiles);
//        if(ds.find(Category.class).field("name").equals(obj))
	}
	public UploadLocalTrainingFile(){};
	
	public static void main(String[] args) throws UnknownHostException, MongoException 
	{
		List<String> dirs = new ArrayList<String>();
		dirs.add("D:\\WorkSpace\\genius-analysis\\trunk\\Classification_Corpus\\trainingSet");
		UploadLocalTrainingFile uploadFile = new UploadLocalTrainingFile();
		uploadFile.saveLocalFile2TrainingFile(dirs);
	}
}
