package com.hitoo.frame.common.storage;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hitoo.frame.common.util.FtpPool;

@Component
public class FtpStorage implements FileHandler {
	
	@Autowired
	private FtpProperty ftpProperty;

	/**
	 * 存储文件，指定文件存放目录和文件名
	 */
	@Override
	public String storeFile(File file, String filePath) throws Exception {
		InputStream is = new FileInputStream(file);
		return this.storeFile(is, filePath);
	}
	
	/**
	 * 存储文件，指定文件存放目录和文件名
	 */
	@Override
	public String storeFile(byte[] file, String filePath) throws Exception {
		InputStream is = new ByteArrayInputStream(file);
		return this.storeFile(is, filePath);
	}
	
	/**
	 * 存储文件，指定文件存放目录和文件名，filePath为：目录 + 文件名（包括扩展名）
	 */
	private String storeFile(InputStream is, String filePath) throws Exception {
		FtpPool pool = FtpPool.getInstance(ftpProperty.getFtpIp(), ftpProperty.getFtpPort(), ftpProperty.getFtpUser(), ftpProperty.getFtpPsw());
		FTPClient client = pool.getResource();//获取连接
		try{
			String dirPath = filePath.substring(0, filePath.lastIndexOf("/"));//目录
			String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);//文件名
			String[] dirArr = dirPath.split("/");
			for(String dir : dirArr){
				if(StringUtils.isBlank(dir)){
					continue;
				}
				if (!client.changeWorkingDirectory(dir)) {
					client.makeDirectory(dir);
					client.changeWorkingDirectory(dir);
				}	
			}
			client.storeFile(fileName, is);
			client.changeWorkingDirectory("/");
			return filePath;
		}finally{
			if(null != client)
				pool.returnResource(client);//释放连接
			IOUtils.closeQuietly(is);
		}
	}
	
	/**
	 * 验证当前目录下某一子目录是否存在
	 */
	private boolean existDirectory(FTPClient client, String path) throws Exception {
		FTPFile[] ftpFileArr = client.listFiles();
		for (FTPFile ftpFile : ftpFileArr) {
			if (ftpFile.isDirectory() && ftpFile.getName().equalsIgnoreCase(path)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 下载文件，filePath为：目录 + 文件名（包括扩展名）
	 */
	@Override
	public byte[] downLoadFile(String filePath) throws Exception {
		byte[] file = null;
		if(StringUtils.isBlank(filePath)){
			return file;
		}
		FtpPool pool = FtpPool.getInstance(ftpProperty.getFtpIp(), ftpProperty.getFtpPort(), ftpProperty.getFtpUser(), ftpProperty.getFtpPsw());
		FTPClient client = pool.getResource();//获取连接
		try{
			client.enterLocalPassiveMode();
			InputStream is = client.retrieveFileStream(filePath);
			if(null != is){
				file = IOUtils.toByteArray(is);
				is.close();
				client.getReply();
			}
			return file;
		}finally{
			if(null != client)
				pool.returnResource(client);//释放连接
		}
	}

	/**
	 * 下载文件，获取输入流，filePath为：目录 + 文件名（包括扩展名）
	 */
	public InputStream streamLoadFile(String filePath) throws Exception{
		InputStream rntInstream = null;
		if(StringUtils.isBlank(filePath)){
			return rntInstream;
		}
		FtpPool pool = FtpPool.getInstance(ftpProperty.getFtpIp(), ftpProperty.getFtpPort(), ftpProperty.getFtpUser(), ftpProperty.getFtpPsw());
		FTPClient client = pool.getResource();//获取连接
		try{
			client.enterLocalPassiveMode();
			InputStream is = client.retrieveFileStream(filePath);
			if(is!=null){
				BufferedInputStream bfs = new BufferedInputStream(is);
				ByteArrayOutputStream bOutputStream = new ByteArrayOutputStream();
				byte[] buffer = new byte[102400];
				int len = 0;
				while ((len = bfs.read(buffer)) != -1) {
					bOutputStream.write(buffer, 0, len);
					buffer = null;
					buffer = new byte[102400];
				}
				bfs.close();
				bOutputStream.flush();
				bOutputStream.close();
				rntInstream = new ByteArrayInputStream(bOutputStream.toByteArray());

				if(null != is){
					is.close();
				}
				client.getReply();
			}
			return rntInstream;
		}finally{
			if(null != client)
				pool.returnResource(client);//释放连接
		}
	}
	
	/**
	 * 删除文件，filePath为：目录 + 文件名（包括扩展名）
	 */
	@Override
	public void deleteFile(String filePath) throws Exception {
		FtpPool pool = FtpPool.getInstance(ftpProperty.getFtpIp(), ftpProperty.getFtpPort(), ftpProperty.getFtpUser(), ftpProperty.getFtpPsw());
		FTPClient client = pool.getResource();//获取连接
		try{
			client.deleteFile(filePath);
			//删除空文件夹？
		}finally{
			if(null != client)
				pool.returnResource(client);//释放连接
		}
	}
	
	/**
	 * 替换文件，直接上传覆盖
	 */
	@Override
	public String replaceFile(byte[] file, String filePath) throws Exception {
		return storeFile(file, filePath);
	}

}