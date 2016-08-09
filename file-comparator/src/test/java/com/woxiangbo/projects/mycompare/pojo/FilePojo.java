package com.woxiangbo.projects.mycompare.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class FilePojo {

	public String fileName;// 文件名
	public HashMap<String, LinePojo> hash;// 存储行数据的集合
	public List<Integer> excludeKeys;// 不需要检查的列的位置
	public List<Integer> customKeys;// 用户自定义key
	public List<String> compareHeaders;// 用来进行比较的列的列名
	public List<String> allHeaders;//所有列的列名
	public List<String> errorLogs;// 所有的错误信息集合
	public Integer rows;// 总共的行数
	public Integer columns;// 总共的列数
	public List<Integer> surplusColumns;// 多余的列
	public String fileLocation;// file location
	
	

	public String getFileLocation() {
		return fileLocation;
	}

	public void setFileLocation(String fileLocation) {
		this.fileLocation = fileLocation;
	}

	public abstract String getFileName();

	public abstract HashMap<String, LinePojo> getHash();

	public abstract List<Integer> getExcludeKeys();

	public abstract List<Integer> getCustomKeys();

	public abstract List<String> getCompareHeaders();

	public abstract List<String> getErrorLogs();

	public abstract Integer getRows();

	public abstract Integer getColumns();

	public abstract List<Integer> getSurplusColumns();

	public abstract void readFileToMap(String excludeStr, String customKeysStr);
	
	public abstract List<String> getAllHeaders();

	public FilePojo() {
		excludeKeys = new ArrayList<Integer>();
		customKeys = new ArrayList<Integer>();
		compareHeaders = new ArrayList<String>();
		errorLogs = new ArrayList<String>();
		surplusColumns = new ArrayList<Integer>();
		hash = new HashMap<String, LinePojo>();
		allHeaders = new ArrayList<String>();
	}
}
