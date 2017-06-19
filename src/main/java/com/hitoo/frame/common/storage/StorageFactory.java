package com.hitoo.frame.common.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.hitoo.frame.common.spring.BeanFactory;

@Component
public class StorageFactory {
	
	@Value("#{propertiesReader[FILE_STORAGE_MODE]}")
	private String fileStorageMode;//文件存储方式 1 fastDFS，2 ftp，3 本地存储
	
	/**
	 * 根据系统参数获取文件存储方式的操作对象
	 */
	public FileHandler getFileHandler() {
		if("1".equals(fileStorageMode)){
			return BeanFactory.getBean(FastDFSStorage.class);
		}else if("2".equals(fileStorageMode)){
			return BeanFactory.getBean(FtpStorage.class);
		}else{
			return BeanFactory.getBean(LocalStorage.class);
		}
	}
}