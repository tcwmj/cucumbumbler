package com.woxiangbo.projects.mycompare.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.testng.Reporter;

import com.woxiangbo.projects.mycompare.config.ReportConfig;
import com.woxiangbo.projects.mycompare.pojo.CsvPojo;
import com.woxiangbo.projects.mycompare.pojo.ExcelPojo;
import com.woxiangbo.projects.mycompare.pojo.FilePojo;
import com.woxiangbo.projects.mycompare.pojo.LinePojo;

public class FileTool {
	private static Logger logger = Logger.getLogger(FileTool.class);
	public String goldenFiles[];
	public String goldenDirStr;
	public String resultDirStr;
	public String excludeStr;
	public String customKeysStr;
	public FilePojo golden;
	public FilePojo compare;

	public FileTool tool;

	public HashMap<String, LinePojo> goldenHash;
	public HashMap<String, LinePojo> compareHash;
	public Map<LinePojo, LinePojo> diffHash;

	public FileTool(String goldenDir, String resultDir, String excludeColumns,
			String KeyColumns) {
		this.goldenDirStr = goldenDir;
		this.resultDirStr = resultDir;
		this.excludeStr = excludeColumns;
		this.customKeysStr = KeyColumns;
		this.diffHash = new HashMap<LinePojo, LinePojo>();
	}

	/**
	 * 读取文件到HashMap<String,LinePojo>中
	 * 
	 * @param goldenFileName
	 */
	public boolean readFile(String goldenFileName) {
		File resultFile = new File(resultDirStr + goldenFileName.replace("_golden", ""));
		
		if (!resultFile.exists()) {
			Reporter.log(ReportConfig.fileNotFount(resultDirStr + goldenFileName.replace("_golden", "")));
			return false;
		}else{
			if (goldenFileName.endsWith(".xls")) {
				golden = new ExcelPojo(goldenDirStr, goldenFileName);
				compare = new ExcelPojo(resultDirStr, goldenFileName.replace("_golden", ""));
			} else if (goldenFileName.endsWith(".csv")) {
				golden = new CsvPojo(goldenDirStr, goldenFileName);
				compare = new CsvPojo(resultDirStr, goldenFileName.replace("_golden", ""));
			}
			golden.setFileLocation(goldenDirStr);
			compare.setFileLocation(resultDirStr);
			
			filterSurplusColumns(golden, compare);
			golden.readFileToMap(excludeStr, customKeysStr);
			compare.readFileToMap(excludeStr, customKeysStr);
	
			goldenHash = golden.getHash();
			compareHash = compare.getHash();
			return true;
		}
		
	}

	/**
	 * 找出两个文件中不同的列，把列的位置存入到List<Integer>中
	 * 
	 * @param golden
	 * @param compare
	 */
	public void filterSurplusColumns(FilePojo golden, FilePojo compare) {
		List<String> goldenAllHeaders = golden.getAllHeaders();
		List<String> compareAllHeaders = compare.getAllHeaders();
		for (int i = 0; i < goldenAllHeaders.size(); i++) {
			if (!compareAllHeaders.contains(goldenAllHeaders.get(i))) {
				golden.getSurplusColumns().add(i);
			}
		}
		for (int j = 0; j < compareAllHeaders.size(); j++) {
			if (!goldenAllHeaders.contains(compareAllHeaders.get(j))) {
				compare.getSurplusColumns().add(j);
			}
		}
	}

	/**
	 * 此时goldenFile 和 compareFile各自的Hash中，保存的都是相同列名的数据 存在以下几种情况:<br/>
	 * 1:Key相同，value的数据和重复次数都相同，--remove 2：把能进行对比的数据放入compareHash中，然后remove
	 * 
	 * @param strB
	 * @param goldenHash
	 * @param compareHash
	 * @return
	 */
	public void compareFile() {
		// 去掉相同的数据
		// 用compareHash去迭代goldenHash
		
		Iterator<String> it = compareHash.keySet().iterator();
		while (it.hasNext()) {
			String ckey = it.next();
			LinePojo cPojo = compareHash.get(ckey);
			if (goldenHash.containsKey(ckey)) {
				LinePojo gPojo = goldenHash.get(ckey);
				if (!(gPojo.getCompareData().equals(cPojo.getCompareData()))
						|| !(gPojo.getLineNums().size() == cPojo.getLineNums()
								.size())) {
					diffHash.put(gPojo, cPojo);
				}
				goldenHash.remove(ckey);
				it.remove();
			}
		}
		//此时 goldenHash 和 compareHash的Key互不相同
		//所以如果用户传有自定义Key的话，迭代则到此结束
		if(compare.getCustomKeys().size() > 0 ) return;
		
		//如果没有自定义Key，则需要比较Key的相似性
		it = compareHash.keySet().iterator();
		while (it.hasNext()) {
			String ckey = it.next();
			LinePojo cPojo = compareHash.get(ckey);

			Iterator<String> gIt = goldenHash.keySet().iterator();
			double similiar = 0;
			LinePojo gPojo = new LinePojo();
			while (gIt.hasNext()) {
				String gKey = gIt.next();
				double cos = CosineSimilarAlgorithm.getSimilarity(gKey, ckey);
				if (cos > similiar) {
					similiar = cos;
					gPojo = goldenHash.get(gKey);
				}
			}
			if (similiar > 0) {//如果匹配到相似度大于0的
				diffHash.put(gPojo, cPojo);
				goldenHash.remove(gPojo.getCompareData());
				it.remove();
			}
		}
		//迭代完以后，goldenHash 和 compareHash至少有一个已经为空 if(goldenHash.size() > 0){
		//在生成文件的时候，分别打印 diffHash,goldenHash,compareHash 即可
	}

	public void generateResult() {

		try {
			String targetDir = ReportConfig.getResultDir();
			File dir = new File(targetDir);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			//String caseId = golden.getFileName().split("_")[0];
			
			//if(diffHash.size() == 0 && goldenHash.size() == 0 && compareHash.size() == 0){
			 //   ExcelTool.setCaseStatus(caseId, "passed");
			 //   System.out.println("passed--->"+caseId);
			//	return;
			//}
			String resultFile = targetDir
					+ golden.getFileName().replace(".xls", "_xls.htm")
							.replace(".csv", "_csv.htm");
			FileWriter writer = new FileWriter(resultFile);

			StringBuffer strB = HtmlTool.printHeader();
			// 打印文件信息
			strB = HtmlTool.printFileProperties(strB, golden);
			strB = HtmlTool.printFileProperties(strB, compare);
			
			strB = HtmlTool.printTableFrame(strB);
			// 打印表头
			strB = HtmlTool.printTableHeader(strB,golden);
			
			//打印diffHash
			strB = HtmlTool.printDiffHash(strB, diffHash);
			
			//如果goldenHash还有数据，就打印
			strB = HtmlTool.goldenAndCompareHash(strB , goldenHash, compareHash);
			
			//打印</table>
			strB = HtmlTool.printTableEnd(strB);
			
			//打印Html尾部
			strB  = HtmlTool.printHtmlFooter(strB);
			writer.append(strB.toString());
			writer.flush();
			writer.close();
			//ExcelTool.setCaseStatus(caseId, "failed");
			//System.out.println("failed--->"+caseId);
			Reporter.log("<a href = \" file:///" + resultFile
					+ "\" target = \"_blank\">" + golden.getFileName() + "</a>");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
