package com.hitoo.bas.repo.eqpt.service;

import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.hitoo.bas.entity.Eqpt;
import com.hitoo.frame.pub.model.PageInfo;
import com.hitoo.frame.pub.model.TreeModel;

public interface EqptService {
	
	/**
	 * 查询设备树(带库房)
	 */
	public List<TreeModel> queryRepoDefAndEqptTree(String gnlArvID) throws Exception;
	
	/**
	 * 查询设备列表，不分页
	 */
	public PageInfo queryEqptDgWithoutPage(String repoID) throws Exception ;
	
	/**
	 * 根据ID查询设备信息
	 */
	public Eqpt queryEqptByID(String eqptID) throws Exception ;
	
	/**
	 * 根据设备ID，查找该设备，构造设备节数目的combobox
	 */
	public JSONArray queryEqptPartCombobox(String eqptID) throws Exception ;
	
	/**
	 * 根据设备ID和设备的某一个节，构造设备层数目的combobox
	 */
	public JSONArray queryEqptLevCombobox(String eqptID, String partNO) throws Exception ;
	
	/**
	 * 新增设备
	 */
	public String addEqpt(Eqpt eqpt) throws Exception ;
	
	/**
	 * 删除设备
	 */
	public void deleteEqpt(String eqptID) throws Exception ;
	
	/**
	 * 修改设备
	 */
	public String modifyEqpt(Eqpt eqpt) throws Exception ;
	
	/**
	 * 设备排、层同步到单元格
	 */
	public void delPartNOAndByID(String eqptID, String aftPartNO, String aftLevNO) throws Exception ;
	
}
