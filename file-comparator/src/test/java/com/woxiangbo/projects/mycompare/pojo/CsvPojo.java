package com.woxiangbo.projects.mycompare.pojo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import jxl.common.Logger;

import com.woxiangbo.projects.mycompare.config.ReportConfig;
import com.woxiangbo.projects.mycompare.utils.CsvReader;

public class CsvPojo extends FilePojo{
	private static Logger logger = Logger.getLogger(CsvPojo.class);
	private String[] headers;// 所有列的列名
	private CsvReader reader;// 读取CSV对象的reader

	public CsvPojo(String fileDir, String strFileName) {
		this.fileName = strFileName;
		try {
			reader = new CsvReader(fileDir + "/" + fileName,ReportConfig.csvSeparateSymbol);
			reader.readHeaders();
			headers = reader.getHeaders();
			allHeaders = Arrays.asList(headers);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 配置需要忽略的列
	 * 
	 * @param strExclude
	 */
	private void configExcludeKeys(String strExclude) {
		for(Integer i : surplusColumns){
			if(!excludeKeys.contains(i)){
				excludeKeys.add(i);
			}
		}
		if(strExclude.equals("")) return;
		for (String e : strExclude.split("\\|")) {
			for (int t = 0; t < headers.length; t++) {
				if (headers[t].equals(e) && !excludeKeys.contains(headers[t])) {
					excludeKeys.add(t);
				}
			}
		}
	}

	/**
	 * 配置用户传进来的自定义Key
	 * 
	 * @param strCustomKeys
	 * @throws Exception
	 */
	private void configCustomKeys(String strCustomKeys) {
		if(strCustomKeys.equals("")) return;
		for (String e : strCustomKeys.split("\\|")) {
			boolean exists = false;
			for (int t = 0; t < headers.length; t++) {
				if (headers[t].equals(e) && !customKeys.contains(headers[t])) {
					customKeys.add(t);
					exists = true;
				}
			}
			if (!exists) {
				errorLogs.add(ReportConfig.keyNotFound(e, fileName));
			}
		}
	}
	
	/**
	 * 配置要比较的列的列名
	 */
	private void configCompareHeaders() {
		for (int i = 0; i < headers.length; i++) {
			if (!excludeKeys.contains(i)) {
				compareHeaders.add(headers[i]);
			}
		}
	}

	/**
	 * 读取文件的每一行到 HashMap中 1: 如果用户传了关键字，则使用用户关键字作为key去读取数据
	 * 
	 * 2：用户没有传关键字，则使用相同的列作为key
	 */
	@Override
	public void readFileToMap(String strExclude,String strCustomKeys){
		
		configExcludeKeys(strExclude);

		configCustomKeys(strCustomKeys);

		for (Integer i : customKeys) {
			if (excludeKeys.contains(i)) {
				errorLogs.add(ReportConfig.customConflictWithExclude());
			}
		}
		if (errorLogs.size() > 0){
			return;
		}
		
		configCompareHeaders();
		
		int rowNum = 1; // CSV 数据默认从第2行开始
		try {
			while (reader.readRecord()) {
				logger.debug(fileName + " rowNum==>" + rowNum);
				StringBuffer strKeyBuffer = new StringBuffer();
				StringBuffer lineStrBuffer = new StringBuffer();

				String lineValues[] = reader.getValues();// 该行的数据数组
				List<String> compareDataList = new ArrayList<String>();

				// 取得该行需要对比的列的值，顺序放入List中
				for (int i = 0; i < lineValues.length; i++) {
					if (!excludeKeys.contains(i)) {
						String value = lineValues[i].trim();
						lineStrBuffer.append(value);
						compareDataList.add(value);
					}
				}

				// 取得 Key
				if (customKeys.size() > 0) {
					for (Integer i : customKeys) {
						strKeyBuffer.append(lineValues[i]);
					}
				} else {
					strKeyBuffer = lineStrBuffer;
				}

				String strKey = strKeyBuffer.toString();
				String strLine = lineStrBuffer.toString();
				
				if (hash.containsKey(strKey)) {
					LinePojo pojo = hash.get(strKey);
					
					if(!pojo.getCompareData().equals(strLine)){
						errorLogs.add(ReportConfig.keysNotUnique(fileName));
					}else{
						pojo.getLineNums().add(rowNum);
					}
				} else {
					LinePojo p = new LinePojo(strLine);
					p.setCompareDataList(compareDataList);
					p.getLineNums().add(rowNum);
					hash.put(strKey, p);
				}
				rowNum++;
			}
			this.rows = rowNum;
		} catch (IOException e) {
			e.printStackTrace();
		}
		reader.close();
	}
	
	@Override
	public String getFileName() {
		return this.fileName;
	}

	@Override
	public HashMap<String, LinePojo> getHash() {
		return this.hash;
	}

	@Override
	public List<Integer> getExcludeKeys() {
		return this.excludeKeys;
	}

	@Override
	public List<Integer> getCustomKeys() {
		return this.customKeys;
	}



	@Override
	public List<String> getErrorLogs() {
		return this.errorLogs;
	}

	@Override
	public Integer getRows() {
		return this.rows;
	}

	@Override
	public Integer getColumns() {
		return this.columns;
	}

	@Override
	public List<Integer> getSurplusColumns() {
		return this.surplusColumns;
	}

	@Override
	public List<String> getCompareHeaders() {
		return this.compareHeaders;
	}

	@Override
	public List<String> getAllHeaders() {
		return this.allHeaders;
	}

}
