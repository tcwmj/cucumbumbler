package com.woxiangbo.projects.mycompare;

import java.io.File;

import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.woxiangbo.projects.mycompare.utils.ExcelTool;
import com.woxiangbo.projects.mycompare.utils.FileTool;


public class SimpleTest {

	public String goldenFiles[];
	public FileTool tool;
	@BeforeSuite
	public void beforeSuite(){
		//准备统计数据的Excel文件
		ExcelTool.prepareStatisticsExcel();
	}

	@Parameters({"goldenDir", "resultDir", "folderName", "excludeColumns", "keyColumns" })
	@Test
	public void test(String goldenDir, String resultDir, String folderName, String excludeColumns, String KeyColumns) throws Exception {
		if(!goldenDir.endsWith("/")){
			goldenDir = goldenDir + "/";
		}
		if(!resultDir.endsWith("/")){
			resultDir = resultDir + "/";
		}
		if(!folderName.endsWith("/")){
			folderName = folderName + "/";
		}
		//System.out.println(" 111111111 : ---->" + resultDir);
		//System.out.println(" folderName ===> " + folderName);
		goldenDir = goldenDir + folderName ;
		resultDir = resultDir + folderName ;
		File golden = new File(goldenDir);
		goldenFiles = golden.list();
		
		
		//System.out.println(" 2222222222 : ---->" + resultDir);
		for (String goldenFileName : goldenFiles) {
			tool = new FileTool(goldenDir, resultDir, excludeColumns, KeyColumns);
			boolean bool = tool.readFile(goldenFileName);
			if(bool){
				tool.compareFile();
				tool.generateResult();
			}
		}
		
	}
	
	@AfterSuite
	public void afterSuite(){
		//完成统计数据的Excel文件的读写
		ExcelTool.close();
	}

}