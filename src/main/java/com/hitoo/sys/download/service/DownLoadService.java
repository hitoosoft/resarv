package com.hitoo.sys.download.service;

import com.hitoo.sys.entity.DownLoad;

public interface DownLoadService {
	
	/**
	 * 常用下载
	 */
	public DownLoad querydownLoadAssistFile(String fileID) throws Exception;

}