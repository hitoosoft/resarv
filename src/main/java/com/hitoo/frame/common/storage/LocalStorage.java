package com.hitoo.frame.common.storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class LocalStorage implements FileHandler {
	
	@Value("#{propertiesReader[FILE_LOCAL_DIR]}")
	private String fileLocalDir;//文件本地存储根目录

	/**
	 * 存储文件，指定文件存放目录和文件名,filePath以 / 开头
	 */
	@Override
	public String storeFile(File file, String filePath) throws Exception {
		String aimPath = fileLocalDir + filePath;
		File aimFile = new File(aimPath);
		if(!aimFile.getParentFile().exists()){
			aimFile.getParentFile().mkdirs();
		}
        int byteread = 0; // 读取的字节数  
        InputStream in = new FileInputStream(file);
        OutputStream out = new FileOutputStream(aimFile);  
        byte[] buffer = new byte[1024];
        while ((byteread = in.read(buffer)) != -1) {
        	out.write(buffer, 0, byteread);
        }
        if(null != out){
        	out.close();
        }
        if(null != in){
        	in.close();
        }
        return filePath;
	}

	/**
	 * 存储文件，指定文件存放目录和文件名
	 */
	@Override
	public String storeFile(byte[] file, String filePath) throws Exception {
		String aimPath = fileLocalDir + filePath;
		File aimFile = new File(aimPath);
		if(!aimFile.getParentFile().exists()){
			aimFile.getParentFile().mkdirs();
		}
        OutputStream out = new FileOutputStream(aimFile);
        out.write(file);
        if(null != out){
        	out.close();
        }
        return filePath;
	}
	
	/**
	 * 下载文件，filePath为全路径名
	 */
	@Override
	public byte[] downLoadFile(String filePath) throws Exception {
		byte[] file = null;
		if(StringUtils.isNotBlank(filePath)){
			String srcPath = fileLocalDir + filePath;
			File aimFile = new File(srcPath);
			if(aimFile.exists()){
				InputStream is = new FileInputStream(srcPath);
				file = IOUtils.toByteArray(is);
				if(null != is){
					is.close();
				}
			}
		}
		return file;
	}
	
	/**
	 * 下载文件，获取输入流，filePath为：目录 + 文件名（包括扩展名）
	 */
	public InputStream streamLoadFile(String filePath) throws Exception{
		InputStream is = null;
		if(StringUtils.isNotBlank(filePath)){
			String srcPath = fileLocalDir + filePath;
			File aimFile = new File(srcPath);
			if(aimFile.exists()){
				is = new FileInputStream(srcPath);
			}
		}
		return is;
	}

	/**
	 * 删除文件，filePath为全路径名
	 */
	@Override
	public void deleteFile(String filePath) throws Exception {
		String srcPath = fileLocalDir + filePath;
		File srcFile = new File(srcPath);
		srcFile.delete();
		//删除空文件夹
		//deleteDir(srcPath.substring(0, srcPath.lastIndexOf("/")));
	}
	
	/*private void deleteDir(String srcPath) {
		File parentFile = new File(srcPath);
		if(parentFile.list().length == 0){//空文件夹
			parentFile.delete();
			String parentDir = srcPath.substring(0, srcPath.lastIndexOf("/"));
			deleteDir(parentDir);
		}
	}*/
	
	/**
	 * 替换文件，直接覆盖
	 */
	@Override
	public String replaceFile(byte[] file, String filePath) throws Exception {
		return storeFile(file, filePath);
	}

}