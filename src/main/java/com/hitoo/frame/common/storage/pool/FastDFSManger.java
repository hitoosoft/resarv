package com.hitoo.frame.common.storage.pool;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.hitoo.fastsource.common.NameValuePair;
import com.hitoo.fastsource.fastdfs.ClientGlobal;
import com.hitoo.fastsource.fastdfs.StorageClient1;
import com.hitoo.fastsource.fastdfs.StorageServer;
import com.hitoo.fastsource.fastdfs.TrackerClient;
import com.hitoo.fastsource.fastdfs.TrackerServer;

@Component
public class FastDFSManger{
	private static TrackerClient tracker;
	static{
		try {
			ClassPathResource cpr = new ClassPathResource("fdfs_client.properties");
			ClientGlobal.init(cpr.getClassLoader().getResource("fdfs_client.properties").getPath());
			
			tracker = new TrackerClient();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 上传文件返回文件ID路径
	 */
	public String uploadFile() {
		String fileId = null;
		try {
			File file = new File("E:\\image\\lefu.jpg");
			String fileName = file.getName();
			String fileExtName = fileName.substring(fileName.lastIndexOf(".") + 1);
			
			// 建立连接
			TrackerServer trackerServer = tracker.getConnection();
			StorageServer storageServer = null;
			StorageClient1 client = new StorageClient1(trackerServer, storageServer);

			// 设置元信息
			NameValuePair[] metaList = new NameValuePair[3];
			metaList[0] = new NameValuePair("fileName", fileName);
			metaList[1] = new NameValuePair("fileExtName", fileExtName);
			metaList[2] = new NameValuePair("fileLength", String.valueOf(file.length()));

			InputStream is = new FileInputStream(file);
			fileId = client.upload_file1(IOUtils.toByteArray(is), fileExtName, metaList);
			String fileId1 = client.upload_file1(fileId, "_small", IOUtils.toByteArray(is), fileExtName, metaList);
			System.out.println("上传fastDFS成功，fileId = " + fileId);
			System.out.println("fileId1 = " + fileId1);
			trackerServer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fileId;
	}
	
	public String uploadFileExt(){
		String fileId = null;
		try {
			File file = new File("E:\\image\\guoannv.jpg");
			String fileName = file.getName();
			String fileExtName = fileName.substring(fileName.lastIndexOf(".") + 1);
			
			// 建立连接
			TrackerServer trackerServer = tracker.getConnection();
			StorageServer storageServer = null;
			StorageClient1 client = new StorageClient1(trackerServer, storageServer);

			// 设置元信息
			NameValuePair[] metaList = new NameValuePair[3];
			metaList[0] = new NameValuePair("fileName", fileName);
			metaList[1] = new NameValuePair("fileExtName", fileExtName);
			metaList[2] = new NameValuePair("fileLength", String.valueOf(file.length()));

			InputStream is = new FileInputStream(file);
			fileId = client.upload_file1("group1", IOUtils.toByteArray(is), fileExtName, metaList);
			System.out.println("上传fastDFS成功，fileId = " + fileId);
			trackerServer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fileId;
	}
	
	public void downloadFile(String fileId){
		try{
			// 建立连接
			TrackerServer trackerServer = tracker.getConnection();
			StorageServer storageServer = null;
			StorageClient1 client = new StorageClient1(trackerServer, storageServer);
			byte[] file = client.download_file1(fileId);
			if(null == file){
				System.out.println("获取到缩略图为空");
			}
			OutputStream os = new FileOutputStream("E:\\image\\b.jpg");
			IOUtils.write(file, os);
			trackerServer.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void downloadFileByGroup(){
		try{
			// 建立连接
			TrackerServer trackerServer = tracker.getConnection();
			StorageServer storageServer = null;
			StorageClient1 client = new StorageClient1(trackerServer, storageServer);
			byte[] file = client.download_file("group1", "M00/00/00/wKgB8Fd_IiWAQa-UAADK35IvWoE499.jpg");
			OutputStream os = new FileOutputStream("E:\\image\\c.jpg");
			IOUtils.write(file, os);
			trackerServer.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args){
		FastDFSManger manger = new FastDFSManger();
		for(int i = 0; i < 8; i ++){
			manger.downloadFile("group1/M00/82/6E/CnMLmVkDFFKAFGRfAAKHZFesqxA855.jpg");
		}
		
	}
}