package com.ablesky.pdftool.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

public class ConfigUtil {

	private ConfigUtil() {}
	
	/**
	 * 默认优先从${catalina.home}/conf/路径下加载文件
	 * 如果加载不到，则从classpath下加载
	 * @param configFileName	被加载的配置文件的文件名
	 */
	public static Properties loadConfigInfo(String configFileName) {
		Properties config = new Properties();
		InputStream in = null;
		try {
			String catalinaHome = System.getProperty("catalina.home");
			File file = null;
			if(StringUtils.isNotBlank(catalinaHome)) {
				file = new File(FilenameUtils.concat(catalinaHome, "conf/" + configFileName));
			}
			if(file != null && file.isFile()) {
				in = new FileInputStream(file);
			} else {
				in = ConfigUtil.class.getClassLoader().getResourceAsStream(configFileName);
			}
			config.load(in);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(in);
		}
		return config;
	}
}
