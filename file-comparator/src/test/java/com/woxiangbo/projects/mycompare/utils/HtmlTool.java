package com.woxiangbo.projects.mycompare.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.woxiangbo.projects.mycompare.pojo.FilePojo;
import com.woxiangbo.projects.mycompare.pojo.LinePojo;

public class HtmlTool {
	/**
	 * 打印 html 头部
	 * 
	 * @return
	 */
	public static StringBuffer printHeader() {
		StringBuffer strB = new StringBuffer("<html><head></head><body>");
		return strB;
	}
	
	public static StringBuffer printHtmlFooter(StringBuffer strB) {
		return strB.append("</body></html>");
	}

	/**
	 * 打印文件信息 文件名、总共的行数、多余的列，其它一些错误信息
	 * 
	 * @param strB
	 * @param filePojo
	 * @return
	 */
	public static StringBuffer printFileProperties(StringBuffer strB,
			FilePojo filePojo) {
		strB.append(filePojo.getFileLocation() + " : <b>" + filePojo.getFileName() + "</b><br/>");
		strB.append("<b>total lines : </b>" + (filePojo.rows - 1) + "<br/>");
		if (filePojo.getSurplusColumns().size() > 0) {
			strB.append("surplus Columns : ");
			for (Integer i : filePojo.getSurplusColumns()) {
				strB.append(filePojo.getAllHeaders().get(i)).append(",");
			}
		}
		strB.append("<br/>");
		for (String s : filePojo.getErrorLogs()) {
			strB.append(s).append("<br/>");
		}
		return strB;
	}

	public static StringBuffer printTableFrame(StringBuffer strB) {
		return strB
				.append("<table border = \" 1 \"style = \"border:1px solid #808080; border-collapse: collapse;font-family: Arial,Helvetica,sans-serif;width: 100%;margin : 10px\">");
	}
	
	public static StringBuffer printTableEnd(StringBuffer strB) {
		return strB.append("</table>");
	}

	public static StringBuffer printTableHeader(StringBuffer strB, FilePojo pojo) {
		List<String> headers = pojo.getCompareHeaders();
		strB.append("<thead><tr>");
		strB.append("<th nowrap>").append("&nbsp;").append("</th>");//td 对应部分用来放 行数
		for (String s : headers) {
			strB.append("<th nowrap>").append(s).append("</th>");
		}
		strB.append("</tr></thead>");
		return strB;
	}
	/**
	 * 打印Table 的 Tr，例如 <tr><td></td> .... </tr>
	 * 
	 * @param strB
	 * @param pojo
	 * @return
	 */
	public static StringBuffer printDiffHash(StringBuffer strB , Map<LinePojo,LinePojo> diffHash) {
		if(diffHash.size() == 0 ) return strB;
		Iterator<LinePojo> it = diffHash.keySet().iterator();
		while (it.hasNext()) {
			int columnSize = 0;
			LinePojo gPojo = it.next();
			LinePojo cPojo = diffHash.get(gPojo);
			
			//打印第一行 golden的数据
			columnSize = gPojo.getCompareDataList().size();
			strB.append("<tr>");
			strB.append("<td>");
			for (Integer i : gPojo.getLineNums()) {
				strB.append(i).append(",");
			}
			strB.append("</td>");
			for (String s : gPojo.getCompareDataList()) {
				if ("".equals(s)) {
					s = "&nbsp;";
				}
				strB.append("<td nowrap >" + s + "</td>");
			}
			strB.append("</tr>");

			//打印第2行 compare的数据
			strB.append("<tr>");
			strB.append("<td>");
			for (Integer i : cPojo.getLineNums()) {
				strB.append(i).append(",");
			}
			strB.append("</td>");
			for(int i = 0; i < cPojo.getCompareDataList().size(); i++){
				String goldenStr = gPojo.getCompareDataList().get(i);
				String compareStr = cPojo.getCompareDataList().get(i);
				if ("".equals(compareStr)) {
					compareStr = "&nbsp;";
				}
				if(!compareStr.equals(goldenStr)){
					strB.append("<td nowrap style=\"color:red\">" + compareStr + "</td>");
				}else{
					strB.append("<td nowrap >" + compareStr + "</td>");
				}
			}
			for (String s : cPojo.getCompareDataList()) {
				if ("".equals(s)) {
					s = "&nbsp;";
				}
				
			}
			strB.append("</tr>");
		
			strB.append("<tr><td height = \"30\" colspan=\" " + columnSize+1
				+ "\"></td></tr>");
		}
		return strB;
	}

	
	public static StringBuffer goldenAndCompareHash(StringBuffer strB , HashMap<String,LinePojo> goldenHash,HashMap<String,LinePojo> compareHash) {
		if(goldenHash.size() > 0){
			Iterator<String> gIt = goldenHash.keySet().iterator();
			int columnSize = 0;
			while(gIt.hasNext()){
				String key = gIt.next();
				LinePojo gPojo = goldenHash.get(key);
				columnSize = gPojo.getCompareDataList().size();
				strB.append("<tr>");
				strB.append("<td>");
				for (Integer i : gPojo.getLineNums()) {
					strB.append(i).append(",");
				}
				strB.append("</td>");
				for (String s : gPojo.getCompareDataList()) {
					if ("".equals(s)) {
						s = "&nbsp";
					}
					strB.append("<td nowrap >" + s + "</td>");
				}
				strB.append("</tr>");
				
				//compare 的 用空补齐
				strB.append("<tr>");
				for(int i = 0; i < columnSize + 1; i++){
					strB.append("<td>&nbsp;</td>");
				}
				strB.append("</tr>");
				strB.append("<tr><td height = \"30\" colspan=\" " + columnSize+1
						+ "\"></td></tr>");
			}
			
		}
		
		//如果compare还有数据，就打印
		if(compareHash.size() > 0){
			Iterator<String> gIt = compareHash.keySet().iterator();
			while(gIt.hasNext()){
				int columnSize = 0;
				String key = gIt.next();
				LinePojo cPojo = compareHash.get(key);
				columnSize = cPojo.getCompareDataList().size();
				
				//golden 的 用空补齐
				strB.append("<tr>");
				for(int i = 0; i < columnSize + 1; i++){
					strB.append("<td>&nbsp;</td>");
				}
				strB.append("</tr>");
				//打印 compare
				strB.append("<tr>");
				strB.append("<td>");
				for (Integer i : cPojo.getLineNums()) {
					strB.append(i).append(",");
				}
				strB.append("</td>");
				for (String s : cPojo.getCompareDataList()) {
					if ("".equals(s)) {
						s = "&nbsp";
					}
					strB.append("<td nowrap >" + s + "</td>");
				}
				strB.append("</tr>");
				strB.append("<tr><td height = \"30\" colspan=\" " + columnSize+1
						+ "\"></td></tr>");
				
			}
		}
		
		return strB;
	}
}
		
