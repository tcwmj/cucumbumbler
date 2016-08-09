package com.woxiangbo.projects.mycompare.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class CosineSimilarAlgorithm {
	public static double getSimilarity(String doc1, String doc2) {
		if (doc1 != null && doc1.trim().length() > 0 && doc2 != null
				&& doc2.trim().length() > 0) {
			Map<Integer, int[]> AlgorithmMap = new HashMap<Integer, int[]>();
			// 将两个字符串中的字符以及出现的总数封装到，AlgorithmMap中
			for (int i = 0; i < doc1.length(); i++) {
				char d1 = doc1.charAt(i);
				int charIndex = getAscii(d1); // ch在GB2312中的位置
				if (charIndex != -1) {
					int[] fq = AlgorithmMap.get(charIndex);
					if (fq != null && fq.length == 2) {
						fq[0]++;
					} else {
						fq = new int[2];
						fq[0] = 1;
						fq[1] = 0;
						AlgorithmMap.put(charIndex, fq);
					}
				}
			}

			for (int i = 0; i < doc2.length(); i++) {
				char d2 = doc2.charAt(i);
				int charIndex = getAscii(d2);
				if (charIndex != -1) {
					int[] fq = AlgorithmMap.get(charIndex);
					if (fq != null && fq.length == 2) {
						fq[1]++;
					} else {
						fq = new int[2];
						fq[0] = 0;
						fq[1] = 1;
						AlgorithmMap.put(charIndex, fq);
					}
				}
			}
			Iterator<Integer> iterator = AlgorithmMap.keySet().iterator();
			double sqdoc1 = 0;
			double sqdoc2 = 0;
			double denominator = 0;
			while (iterator.hasNext()) {
				int[] c = AlgorithmMap.get(iterator.next());
				denominator += c[0] * c[1];
				sqdoc1 += c[0] * c[0];
				sqdoc2 += c[1] * c[1];
			}
			return denominator / Math.sqrt(sqdoc1 * sqdoc2);
		} else {
			throw new NullPointerException(
					" the Document is null or have not cahrs!!");
		}
	}

	/**
	 * 根据输入的字符获取他的ascii 码
	 */

	private static Integer getAscii(char ch) {
		return Integer.parseInt(Integer.toString(ch));
	}
	
	public static void main(String[] args) {
		double d=getSimilarity("a,b,c,d,mm", "a,b,c,d,ee");
//		System.out.println(d);/
	}
}
