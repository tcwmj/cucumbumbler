package com.woxiangbo.projects.mycompare.pojo;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.testng.Reporter;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import com.woxiangbo.projects.mycompare.config.ReportConfig;

public class ExcelPojo extends FilePojo {
	private Workbook workbook;// workbook对象，用来操作excel
	private Sheet sheet;
	private Integer defaultSheet = 0;// 默认只读第一个sheet

	/**
	 * ExcelPojo 初始化
	 * 
	 * @param fileDir
	 *            文件所对应的目录
	 * @param fileName
	 *            文件名
	 * @param strExclude
	 *            不需要比较的列，用 | 分隔
	 * @param strCustomKeys
	 *            用户自定义 Key ,用 | 分隔
	 * @throws Exception
	 */
	public ExcelPojo(String fileDir, String fileName) {
		this.fileName = fileName;
		try {
			workbook = Workbook.getWorkbook(new File(fileDir + "/" + fileName));
		} catch (Exception e) {
			e.printStackTrace();
		}
		sheet = workbook.getSheet(defaultSheet);
		rows = sheet.getRows();
		columns = sheet.getColumns();
		Cell header[] = sheet.getRow(0);
		for(Cell c : header){
			allHeaders.add(c.getContents());
		}
	}

	/**
	 * 配置需要忽略的列 1:文件自身多余的列要加入忽略list 2：用户传进来的列也要加入忽略list
	 * 
	 * @param strExclude
	 */
	private void configExcludeKeys(String strExclude) {
		for (Integer i : surplusColumns) {
			if (!excludeKeys.contains(i)) {
				excludeKeys.add(i);
			}
		}
		if (strExclude.equals(""))
			return;
		for (String e : strExclude.split("\\|")) {
			Cell cell = sheet.findCell(e, 0, 0, columns - 1, 0, true);
			if (cell != null) {
				Integer column = cell.getColumn();
				if (!excludeKeys.contains(column)) {
					excludeKeys.add(column);
				}
			}
		}
	}

	/**
	 * 配置用户自定义Key
	 * 
	 * @param strCustomKeys
	 * @throws Exception
	 */
	private void configCustomKeys(String strCustomKeys) {
		if (strCustomKeys.equals(""))
			return;
		for (String e : strCustomKeys.split("\\|")) {
			Cell cell = sheet.findCell(e, 0, 0, columns - 1, 0, true);
			if (cell != null) {
				Integer column = cell.getColumn();
				if (!customKeys.contains(column)) {
					customKeys.add(column);
				}
			} else {
				errorLogs.add(ReportConfig.keyNotFound(e, fileName));
			}
		}
	}
	/**
	 * 配置要比较的列的列名
	 */
	private void configCompareHeaders() {
		Cell header[] = sheet.getRow(0);
		for (int i = 0; i < header.length; i++) {
			if (!excludeKeys.contains(i)) {
				compareHeaders.add(header[i].getContents());
			}
		}
	}

	/**
	 * 读取文件的每一行到 HashMap中 1: 如果用户传了关键字，则使用用户关键字作为key去读取数据
	 * 
	 * 2：用户没有传关键字，则使用相同的列作为key
	 */
	@Override
	public void readFileToMap(String strExclude, String strCustomKeys) {
		configExcludeKeys(strExclude);

		configCustomKeys(strCustomKeys);

		for (Integer i : customKeys) {
			if (excludeKeys.contains(i)) {
				errorLogs.add(ReportConfig.customConflictWithExclude());
			}
		}

		if (errorLogs.size() > 0) {
			return;
		}
		configCompareHeaders();

		for (int i = 1; i < rows; i++) {

			StringBuffer keyBuffer = new StringBuffer();
			StringBuffer lineStrBuffer = new StringBuffer();
			List<String> compareDataList = new ArrayList<String>();

			for (int m = 0; m < columns; m++) {
				if (!excludeKeys.contains(m)) {
					
					String content = sheet.getCell(m, i).getContents().trim();
					lineStrBuffer.append(content);
					compareDataList.add(content);
				}
			}
			if (customKeys.size() > 0) {
				for (Integer column : customKeys) {
					String value = sheet.getCell(column, i).getContents()
							.trim();
					keyBuffer.append(value);
				}
			} else {
				keyBuffer = lineStrBuffer;
			}

			String strKey = keyBuffer.toString();
			String lineStr = lineStrBuffer.toString();

			if (hash.containsKey(strKey)) {
				LinePojo pojo = hash.get(strKey);
				if (!pojo.getCompareData().equals(lineStr)) {
					errorLogs.add(ReportConfig.keysNotUnique(fileName));
				} else {
					pojo.getLineNums().add(i);
				}
			} else {
				LinePojo p = new LinePojo(lineStr);
				p.getLineNums().add(i);
				p.setCompareDataList(compareDataList);
				hash.put(strKey, p);
			}
		}
		workbook.close();
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
