package com.hitoo.frame.common.storage.pool;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;

import com.hitoo.fastsource.common.NameValuePair;

@Component
public class FastDFSPoolManger{
	
	/**
	 * 上传文件返回文件ID路径
	 */
	public String uploadFile() {
		String fileId = null;
		try {
			File file = new File("E:\\image\\lefu.jpg");
			String fileName = file.getName();
			String fileExtName = fileName.substring(fileName.lastIndexOf(".") + 1);
			
			//获取连接
			FastDFSPool pool = FastDFSPool.getInstance();
			StorageClient client = pool.getResource();

			// 设置元信息
			NameValuePair[] metaList = new NameValuePair[3];
			metaList[0] = new NameValuePair("fileName", fileName);
			metaList[1] = new NameValuePair("fileExtName", fileExtName);
			metaList[2] = new NameValuePair("fileLength", String.valueOf(file.length()));

			InputStream is = new FileInputStream(file);
			fileId = client.upload_file1(IOUtils.toByteArray(is), fileExtName, metaList);
			System.out.println("上传fastDFS成功，fileId = " + fileId);
			
			pool.returnResource(client);//释放连接
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fileId;
	}
	
	public void downloadFile(String fileId){
		try{
			//获取连接
			FastDFSPool pool = FastDFSPool.getInstance();
			StorageClient client = pool.getResource();
			byte[] file = client.download_file1(fileId);
			if(null == file){
				System.out.println("获取到缩略图为空");
			}
			OutputStream os = new FileOutputStream("E:\\image\\bb.jpg");
			IOUtils.write(file, os);
			pool.returnResource(client);//释放连接
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args){
		FastDFSPoolManger manger = new FastDFSPoolManger();
		for(int i = 0; i < 8; i ++){
			manger.downloadFile("group1/M02/7D/E3/CnMLmFkDEbiAF7_7AAA-kGY3s0c826.jpg");
		}
	}
	
	public void deleteFile(String fileId){
		try {
			FastDFSPool pool = FastDFSPool.getInstance();
			StorageClient client = pool.getResource();
			client.delete_file1(fileId);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}