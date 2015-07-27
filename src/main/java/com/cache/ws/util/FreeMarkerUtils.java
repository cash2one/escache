package com.cache.ws.util;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.log4j.Logger;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * 访问FreeMarker的工具类
 * 
 */
public class FreeMarkerUtils {

	private static final Logger LOG = Logger.getLogger(FreeMarkerUtils.class);

	/** 应用所在路径 */
	private static String appPath = FreeMarkerUtils.class.getClassLoader().getResource("templates").getPath();

	/** 编码格式 UTF-8 */
	private static final String ENCODING = "UTF-8";

	/** FreeMarker配置 */
	private static Configuration config = new Configuration();

	/** 路径分割符 */
	public static final String PATH_SEPARATOR = File.separator;

	static {
		try {
			
			// 加载模版
			File file = new File(new StringBuffer(appPath).append(
					File.separator).toString());
			// 设置要解析的模板所在的目录，并加载模板文件
			config.setDirectoryForTemplateLoading(file);
			// 设置文件编码为UTF-8
			config.setEncoding(Locale.getDefault(), ENCODING);

		} catch (IOException e) {
			LOG.error("初始化FreeMarker配置出错", e);
		}
	}

	/**
	 * 根据模板名称获取DSL
	 * 
	 * @param data
	 * @param templateFileName
	 * @return
	 */
	public static String getDSL(Map<String, Object> data,
			String templateFileName) {

		StringWriter out = null;
		String dsl = "";
		try {
			// 获取模板,并设置编码方式，这个编码必须要与页面中的编码格式一致
			Template template = config.getTemplate(templateFileName, ENCODING);
			template.setEncoding(ENCODING);

			out = new StringWriter();

			// 处理模版
			template.process(data, out);

			dsl = out.toString();

		} catch (Exception e) {
			 LOG.error("处理模板文件 "+ templateFileName + " 出错", e);
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				 LOG.error("关闭StringWriter对象出错", e);
			}
		}

		return dsl;
	}

	public static void main(String[] args) throws Exception {

		Map<String, Object> data = new HashMap<String, Object>();
		data.put("start", "loc");
		data.put("end", "loc");

		System.out.print(getDSL(data, "period_hour.ftl"));

	}

}