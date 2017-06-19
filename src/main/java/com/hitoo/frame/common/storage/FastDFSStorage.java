package com.hitoo.frame.common.storage;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.hitoo.frame.common.storage.pool.FastDFSPool;
import com.hitoo.frame.common.storage.pool.StorageClient;

@Component
public class FastDFSStorage implements FileHandler {

	/**
	 * 存储文件，指定文件存放目录和文件名，返回文件的实际存放路径
	 */
	@Override
	public String storeFile(File file, String filePath) throws Exception {
		InputStream is = new FileInputStream(file);
		return this.storeFile(IOUtils.toByteArray(is), filePath);
	}

	/**
	 * 存储文件，指定文件存放目录和文件名，返回文件的实际存放路径
	 */
	@Override
	public String storeFile(byte[] file, String filePath) throws Exception {
		FastDFSPool pool = FastDFSPool.getInstance();
		StorageClient client = pool.getResource();//获取连接
		try{
			String extName = filePath.substring(filePath.lastIndexOf(".") + 1);
			String fileID = client.upload_file1(file, extName, null);
			return fileID;
		}finally{
			if(null != client)
				pool.returnResource(client);//释放连接
		}
	}
	
	/**
	 * 下载文件
	 */
	@Override
	public byte[] downLoadFile(String filePath) throws Exception {
		if(StringUtils.isBlank(filePath)){
			return null;
		}
		FastDFSPool pool = FastDFSPool.getInstance();
		StorageClient client = pool.getResource();//获取连接
		try{
			byte[] file = client.download_file1(filePath);
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
		if(StringUtils.isBlank(filePath)){
			return null;
		}
		FastDFSPool pool = FastDFSPool.getInstance();
		StorageClient client = pool.getResource();//获取连接
		try{
			byte[] file = client.download_file1(filePath);
			InputStream is = new ByteArrayInputStream(file);
			return is;
		}finally{
			if(null != client)
				pool.returnResource(client);//释放连接
		}
	}

	/**
	 * 删除文件
	 */
	@Override
	public void deleteFile(String filePath) throws Exception {
		FastDFSPool pool = FastDFSPool.getInstance();
		StorageClient client = pool.getResource();
		try{
			client.delete_file1(filePath);
		}finally{
			if(null != client)
				pool.returnResource(client);//释放连接
		}
	}

	/**
	 * 替换文件，先删除原来文件再新增
	 */
	@Override
	public String replaceFile(byte[] file, String filePath) throws Exception {
		FastDFSPool pool = FastDFSPool.getInstance();
		StorageClient client = pool.getResource();
		try{
			client.delete_file1(filePath);
			String extName = filePath.substring(filePath.lastIndexOf(".") + 1);
			String fileID = client.upload_file1(file, extName, null);
			return fileID;
		}finally{
			if(null != client)
				pool.returnResource(client);//释放连接
		}
	}

}