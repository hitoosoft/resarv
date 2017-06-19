package com.hitoo.frame.common.storage.pool;

import com.hitoo.fastsource.fastdfs.StorageClient1;
import com.hitoo.fastsource.fastdfs.StorageServer;
import com.hitoo.fastsource.fastdfs.TrackerServer;

/**
 * 继承StorageClient1，增加getter方法，以达到在引用中获取trackerServer和storageServer的目的
 * @author zhangqh
 */
public class StorageClient extends StorageClient1 {
	
	public StorageClient() { }

	public StorageClient(TrackerServer trackerServer, StorageServer storageServer) {
		super(trackerServer, storageServer);
	}

	public TrackerServer getTrackerServer() {
		return this.trackerServer;
	}

	public StorageServer getStorageServer() {
		return this.storageServer;
	}

}