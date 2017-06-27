package com.hitoo.bas.repo.def.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hitoo.bas.entity.Repo;
import com.hitoo.bas.entity.Eqpt;
import com.hitoo.bas.repo.def.dao.DefDao;
import com.hitoo.bas.repo.def.service.DefService;
import com.hitoo.frame.base.BaseService;
import com.hitoo.frame.base.BusinessException;
import com.hitoo.frame.common.util.TreeUtil;
import com.hitoo.frame.pub.model.TreeModel;
@Service
public class DefServiceImpl extends BaseService implements DefService{
	
	@Autowired
	private DefDao defDao;
	
	/**
	 * 加载库房树
	 */
	@Override
	public List<TreeModel> queryDefTree() throws Exception {
		return TreeUtil.setTree(defDao.queryDefTree());
	}
	
	/**
	 * 根据ID查询库房信息
	 */
	@Override
	public Repo queryDefByID(String repoID) throws Exception {
		return commonDao.findEntityByID(Repo.class, repoID);
	}
	
	/**
	 * 新增库房
	 */
	@Override
	public String addDef(Repo def) throws Exception {
		String repoNam = def.getRepoNam();
		Integer seqNO = def.getSeqNO();
		//验证库房是否重名
		if(commonDao.findEntityList(Repo.class, "repoNam", repoNam).size()!=0){
			throw new BusinessException("库房名称重复，请重新输入！");
		}
		if(seqNO==null){
			seqNO = commonDao.findEntityList(Repo.class).size()+1;
		}
		def.setRepoID(dBUtil.getCommonId());
		def.setSeqNO(seqNO);
		commonDao.saveEntity(def);
		return def.getRepoID();
	}
	
	/**
	 * 删除库房
	 */
	@Override
	public void deleteDef(String repoID) throws Exception {
		//验证库房中是否有设备
		if(commonDao.findEntityList(Eqpt.class, "repoID", repoID).size()!=0){
			throw new BusinessException("库房中存有设备，不能删除！");
		}
		commonDao.delEntityById(Repo.class, repoID);
	}
	
	/**
	 * 修改库房
	 */
	@Override
	public String modifyDef(Repo def) throws Exception {
		String repoID, repoNam;
		repoID = def.getRepoID();
		repoNam = def.getRepoNam();
		
		//验证库房是否重名
		if(defDao.queryDefByNam(repoID, repoNam)!=null){
			throw new BusinessException("库房名称重复，请重新输入！");
		}
		commonDao.updateEntity(def);
		return def.getRepoID();
	}
	
}
