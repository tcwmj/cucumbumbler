package com.woxiangbo.projects.mycompare.pojo;

import java.util.ArrayList;
import java.util.List;

public class LinePojo {
	private List<Integer> lineNums;//该行所处的位置
	private List<String> compareDataList;//该行用来比较的数据 的List 形式
	private String compareData;// 该行用来比较的数据 的 String 形式
	
	public LinePojo() {};

	public LinePojo(String data) {
		init();
		setCompareData(data);
	}
	
	public void setCompareDataList(List<String> compareDataList) {
		this.compareDataList = compareDataList;
	}

	public void init(){
		this.lineNums = new ArrayList<Integer>();//该行所处的位置
		this.compareDataList = new ArrayList<String>();//改行用来比较的数据
	}

	public void setCompareData(String compareData) {
		this.compareData = compareData;
	}

	public List<String> getCompareDataList() {
		return compareDataList;
	}

	public String getCompareData() {
		return compareData;
	}

	public List<Integer> getLineNums() {
		return lineNums;
	}

	@Override
	public int hashCode() {
		return this.compareData.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		LinePojo pojo = (LinePojo) obj;
		if (this.getCompareData().equals(pojo.getCompareData())) {
			return true;
		} else {
			return false;
		}
	}

}
