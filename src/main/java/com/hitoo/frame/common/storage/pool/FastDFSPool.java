package com.hitoo.frame.common.storage.pool;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.pool.BasePoolableObjectFactory;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.commons.pool.impl.GenericObjectPool.Config;
import org.springframework.core.io.ClassPathResource;

import com.hitoo.fastsource.fastdfs.ClientGlobal;
import com.hitoo.fastsource.fastdfs.ProtoCommon;
import com.hitoo.fastsource.fastdfs.StorageServer;
import com.hitoo.fastsource.fastdfs.TrackerClient;
import com.hitoo.fastsource.fastdfs.TrackerServer;

/**
 * 通过common.pool实现fastDFS的连接池
 * @author zhangqh
 */
public class FastDFSPool {
	
	private static Log log = LogFactory.getLog(FastDFSPool.class);
	
	private static final FastDFSPool instance = new FastDFSPool();
	
	private FastDFSPool(){}
	
	private static GenericObjectPool pool;
	
	public static FastDFSPool getInstance(){
		if(null == pool){
			Config config = new Config();
			config.maxActive = 100;
			config.testWhileIdle = true;
			config.minEvictableIdleTimeMillis = 60000L;
			config.timeBetweenEvictionRunsMillis = 30000L;
			pool = new GenericObjectPool(new FastdfsClientFactory(), config);
		}
		return instance;
	}
	
	/**
	 * 从连接池中获取资源
	 */
	public StorageClient getResource() {
		StorageClient client = null;
		try {
			client = (StorageClient) pool.borrowObject();
		} catch (Exception e) {
			log.error("无法从连接池中获取资源", e);
		}
		return client;
	}

	/**
	 * 将资源归还给连接池
	 */
	public boolean returnResource(StorageClient resource) {
		boolean result = false;
		try {
			pool.returnObject(resource);
			result = true;
		} catch (Exception e) {
			log.error("无法将资源归还给连接池", e);
		}
		return result;
	}

	/**
	 * 销毁资源
	 */
	public boolean invalidateBrokenResource(StorageClient resource) {
		boolean result = false;
		try {
			pool.invalidateObject(resource);
			result = true;
		} catch (Exception e) {
			log.error("无法将资源销毁", e);
		}
		return result;
	}

	/**
	 * 销毁连接池
	 */
	public boolean destroy() {
		boolean result = false;
		try {
			pool.close();
			result = true;
		} catch (Exception e) {
			log.error("无法销毁连接池", e);
		}
		return result;
	}
	
	private static class FastdfsClientFactory extends BasePoolableObjectFactory {
		
		/**
		 * 创建一个新对象;当对象池中的对象个数不足时,将会使用此方法来"输出"一个新的"对象",并交付给对象池管理.
		 */
		public Object makeObject() throws Exception {
			ClassPathResource cpr = new ClassPathResource("fdfs_client.properties");
			ClientGlobal.init(cpr.getClassLoader().getResource("fdfs_client.properties").getPath());
			
			TrackerClient tracker = new TrackerClient();
			TrackerServer trackerServer = tracker.getConnection();
			StorageServer storageServer = null;
			StorageClient client = new StorageClient(trackerServer, storageServer);
			return client;
		}

		/**
		 * 销毁对象,如果对象池中检测到某个"对象"idle的时间超时,
		 * 或者操作者向对象池"归还对象"时检测到"对象"已经无效,那么此时将会导致"对象销毁"
		 */
		public void destroyObject(Object obj) throws Exception {
			if ((obj == null) || (!(obj instanceof StorageClient)))
				return;
			StorageClient storageClient = (StorageClient) obj;
			TrackerServer trackerServer = storageClient.getTrackerServer();
			StorageServer storageServer = storageClient.getStorageServer();
			trackerServer.close();
			storageServer.close();
		}

		/**
		 * 检测对象是否"有效"
		 */
		public boolean validateObject(Object obj) {
			StorageClient storageClient = (StorageClient) obj;
			try {
				return ProtoCommon.activeTest(storageClient.getTrackerServer().getSocket());
			} catch (IOException e) {
				e.printStackTrace();
			}
			return false;
		}
	}
}