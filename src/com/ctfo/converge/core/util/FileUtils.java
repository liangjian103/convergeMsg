package com.ctfo.converge.core.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class FileUtils {
	protected static Logger logger = Logger.getLogger(FileUtils.class);

	public static List<String> loadLineFile(String fileName) {
		List<String> list = new ArrayList<String>();
		String path = FileUtils.class.getClassLoader().getResource(fileName).getPath();
		File file = new File(path);
		BufferedReader buf = null;
		try {
			if (file.exists()) {
				buf = new BufferedReader(new FileReader(file));
				String readLine = null;
				while ((readLine = buf.readLine()) != null) {
					if (!readLine.startsWith("#")) { // skip文件注释
						list.add(readLine);
					}
				}// End while
			}
		} catch (IOException e) {
			logger.error("加载 数据文件失败 " + fileName + ".", e);
		} finally {
			if (buf != null) {
				try {
					buf.close();
				} catch (IOException e) {
					logger.error("关闭文件出错.", e);
				}
			}
		}
		return list;
	}
}
