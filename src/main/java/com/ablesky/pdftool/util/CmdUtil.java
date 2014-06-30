package com.ablesky.pdftool.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;

public class CmdUtil {
	
	private static final Logger logger = Logger.getLogger(CmdUtil.class.getName());

	private CmdUtil() {}
	
	public static final Callback<Object> NOOP = new Callback<Object>() {
		@Override
		protected void process(String line, Map<String, Object> resultMap) {}
	};
	
	public static Process exec(String cmdPattern, Object... args) {
		return exec(MessageFormat.format(cmdPattern, args));
	}
	
	public static Process exec(String cmd) {
		logger.info("[CMD]: " + cmd);
		ProcessBuilder builder = new ProcessBuilder(Arrays.asList(cmd.split(" ")));
		builder.redirectErrorStream(true);
		Process process = null;
		try {
			process = builder.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return process;
	}
	
	public static <T> Map<String, T> consume(Process process, Callback<T> callback) {
		if(process == null) {
			return Collections.emptyMap();
		}
		Map<String, T> resultMap = new LinkedHashMap<String, T>();
		InputStream in = null;
		BufferedReader reader = null;
		String line = "";
		try {
			in = process.getInputStream();
			reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
			while((line = reader.readLine()) != null) {
				callback.process(line, resultMap);
			}
			callback.afterProcess(resultMap);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			IOUtils.closeQuietly(reader);
			IOUtils.closeQuietly(in);
		}
		return resultMap;
	}
	
	public static int waitFor(Process process) {
		if(process == null) {
			return -1;
		}
		// 之前见到转换视频的命令，不consume一下，就会卡死
		try {
			consume(process, NOOP);
			return process.waitFor();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	public static boolean isSuccess(int exitValue) {
		return exitValue == 0;
	}
	
	public static void destroy(Process process) {
		process.destroy();
	}
	
	public abstract static class Callback<T> {
		
		protected abstract void process(String line, Map<String, T> resultMap);
		
		protected void afterProcess(Map<String, T> resultMap) {/*---- defaultly do nothing ----*/}
		
	}
	
}
