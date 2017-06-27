package com.hitoo.bas.repo.def.service;

import java.util.List;

import com.hitoo.bas.entity.Repo;
import com.hitoo.frame.pub.model.TreeModel;

public interface DefService {
	
	/*
	 * 加载库房
	 */
	public List<TreeModel> queryDefTree() throws Exception ;
	
	/*
	 * 根据ID查询库房信息
	 */
	public Repo queryDefByID(String repoID) throws Exception ;
	
	/*
	 * 新增库房
	 */
	public String addDef(Repo def) throws Exception;
	
	/*
	 * 删除库房
	 */
	public void deleteDef(String repoID) throws Exception ;
	
	/*
	 * 修改库房
	 */
	public String modifyDef(Repo def) throws Exception;
	
}
