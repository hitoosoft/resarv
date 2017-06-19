package com.hitoo.frame.common.storage;

import java.io.File;
import java.io.InputStream;

/**
 * 文件操作方法
 * @author zhangqh
 */
public interface FileHandler {
	
	/**
	 * 存储文件，指定文件存放目录和文件名，返回文件的实际存放路径。<br>
	 * filePath为：目录 + 文件名（包括扩展名）<br>
	 * fastDFS会改变文件存放路径，ftp和本地存储与传入路径一致
	 */
	public String storeFile(File file, String filePath) throws Exception;
	
	/**
	 * 存储文件，指定文件存放目录和文件名，返回文件的实际存放路径。<br>
	 * filePath为：目录 + 文件名（包括扩展名）<br>
	 * fastDFS会改变文件存放路径，ftp和本地存储与传入路径一致
	 */
	public String storeFile(byte[] file, String filePath) throws Exception;
	
	/**
	 * 下载文件，filePath为：目录 + 文件名（包括扩展名）
	 */
	public byte[] downLoadFile(String filePath) throws Exception;
	
	/**
	 * 下载文件，获取输入流，filePath为：目录 + 文件名（包括扩展名）
	 */
	public InputStream streamLoadFile(String filePath) throws Exception;

	/**
	 * 删除文件，filePath为：目录 + 文件名（包括扩展名）
	 */
	public void deleteFile(String filePath) throws Exception;
	
	/**
	 * 替换文件
	 */
	public String replaceFile(byte[] file, String filePath) throws Exception;
}