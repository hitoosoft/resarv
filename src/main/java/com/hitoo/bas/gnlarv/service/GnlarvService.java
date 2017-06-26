package com.hitoo.bas.gnlarv.service;

import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.hitoo.bas.entity.Gnlarv;
import com.hitoo.frame.pub.global.LoginInfo;
import com.hitoo.frame.pub.model.TreeModel;

public interface GnlarvService {

	/*
	 * 加载全宗树
	 */
	public  List<TreeModel> queryGnlArvTree(LoginInfo loginUser ,String gnlarvID) throws Exception;
	
	/*
	 * 根据全宗ID查询全宗信息
	 */
	public Gnlarv queryGnlarvByID(String gnlarvID) throws Exception;
	
	/*
	 * 新增全宗
	 */
	public void addGnlArv(Gnlarv gnlarv) throws Exception;
	
	/*
	 * 修改全宗
	 */
	public void modifyGnlarv(Gnlarv gnlarv) throws Exception;
	
	/*
	 * 删除全宗
	 */
	public void deleteGnlArv(String gnlarvID) throws Exception;
	
	/*
	 * 查询所有全宗，组装下拉列表
	 */
	public JSONArray queryGnlArv() throws Exception;
	
}
