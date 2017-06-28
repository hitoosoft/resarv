package com.hitoo.bas.repo.eqpt.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hitoo.bas.entity.Eqpt;
import com.hitoo.bas.entity.Repo;
import com.hitoo.bas.repo.eqpt.dao.EqptDao;
import com.hitoo.bas.repo.cell.dao.CellDao;
import com.hitoo.bas.repo.eqpt.service.EqptService;
import com.hitoo.frame.base.BaseService;
import com.hitoo.frame.base.BusinessException;
import com.hitoo.frame.common.util.TreeUtil;
import com.hitoo.frame.pub.model.PageInfo;
import com.hitoo.frame.pub.model.TreeModel;

@Service
public class EqptServiceImpl extends BaseService implements EqptService {
	
	@Autowired
	private EqptDao eqptDao;
	
	@Autowired
	private CellDao cellDao;
	
	/**
	 * 查询库房设备
	 */
	@Override
	public List<TreeModel> queryRepoDefAndEqptTree(String gnlArvID) throws Exception {
		List<TreeModel> repoTree = new ArrayList<TreeModel>();//库房树
		if(StringUtils.isBlank(gnlArvID)){
			repoTree = TreeUtil.setTree(commonDao.findEntityList(Repo.class));
		}else{
			repoTree = TreeUtil.setTree(commonDao.findEntityList(Repo.class, "gnlArvID", gnlArvID));
		}
		//设备树
		List<TreeModel> eqptTree = TreeUtil.setTree(commonDao.findEntityList(Eqpt.class));
		//合并树
		List<TreeModel> repoAndEqptTree = TreeUtil.mergeTree(repoTree, eqptTree);
		return repoAndEqptTree; 
	}
	
	/**
	 * 查询设备列表，不分页
	 */
	@Override
	public PageInfo queryEqptDgWithoutPage(String repoID) throws Exception {
		PageInfo pi = new PageInfo();
		List<Eqpt> ls = commonDao.findEntityList(Eqpt.class, "repoID", repoID);
		pi.setTotal(ls==null?""+0:""+ls.size());
		pi.setRows(ls);
		return pi;
	}
	
	/**
	 * 根据ID查询设备信息
	 */
	@Override
	public Eqpt queryEqptByID(String eqptID) throws Exception {
		return commonDao.findEntityByID(Eqpt.class, eqptID);
	}
	
	/**
	 * 根据设备ID，查找该设备，构造设备节数目的combobox
	 */
	@Override
	public JSONArray queryEqptPartCombobox(String eqptID) throws Exception {
		
		JSONArray rtnJsonAry = new JSONArray();
		JSONObject jsonObj;

		if (eqptID == null || "".equals(eqptID)) {
			throw new BusinessException("设备ID不能为空！");
		}
//		Eqpt eqpt = commonDao.findEntityByID(Eqpt.class, eqptID);
		Integer partNO = cellDao.queryMaxPartNO(eqptID);
		for (int i = 1; i <= partNO.intValue(); i++) {
			jsonObj = new JSONObject();
			jsonObj.put("id", i);
			jsonObj.put("text", "" + i);
			rtnJsonAry.add(jsonObj);
		}
		return rtnJsonAry;
	}
	
	/**
	 * 根据设备ID和设备的某一个节，构造设备层数目的combobox
	 */
	@Override
	public JSONArray queryEqptLevCombobox(String eqptID, String partNO)
			throws Exception {
		
		//根据设备ID和排号，查看该设备的该排有多少层
		Integer maxLevNum = eqptDao.queryMaxLevNO(eqptID, partNO);
		
		JSONArray rtnJsonAry = new JSONArray();
		JSONObject jsonObj;
		
		for (int i = 1; i <= maxLevNum+1; i++) {
			jsonObj = new JSONObject();
			jsonObj.put("id", i);
			jsonObj.put("text", "" + i);
			rtnJsonAry.add(jsonObj);
		}
		
		return rtnJsonAry;
	}
	
	/**
	 * 新增设备
	 */
	@Override
	public String addEqpt(Eqpt eqpt) throws Exception {
		String eqptNam, repoID;
		Integer seqNO = eqpt.getSeqNO();
		eqptNam = eqpt.getEqptNam();
		repoID = eqpt.getRepoID();
		
		//验证库房是否存在
		if(commonDao.findEntityByID(Repo.class, repoID)==null){
			throw new BusinessException("库房不存在！");
		}
		//验证库房设备名称是否重复
		if(eqptDao.queryEqptIDByNam(repoID, eqptNam)!=null){
			throw new BusinessException("同一库房下设备名称重复，请重新输入！");
		}
		//seqNO排序
		if(seqNO==0){
			seqNO = eqptDao.queryEqptCountByRepoID(repoID)+1;
		}
		eqpt.setEqptID(dBUtil.getCommonId());
		eqpt.setSeqNO(seqNO);
		commonDao.saveEntity(eqpt);
		return repoID;
	}
	
	/**
	 * 删除设备
	 */
	@Override
	public void deleteEqpt(String eqptID) throws Exception {
		
		//删除前验证设备下单元格是否存有档案
		if(eqptDao.queryFirstArvtypeUsingCellByEqptID(eqptID)!=null){
			throw new BusinessException("该设备单元格中存有档案分类，不能删除！");
		}
		//删除设备要同时删除单元格
		eqptDao.deleteCellByEqptID(eqptID);
		//删除设备要同时删除单元格序号
		eqptDao.deleteCellNoByEqptID(eqptID);
		commonDao.delEntityById(Eqpt.class, eqptID);
	}
	
	/**
	 * 修改设备
	 */
	@Override
	public String modifyEqpt(Eqpt eqpt) throws Exception {
		String repoID, eqptID, eqptNam;
		repoID = eqpt.getRepoID();
		eqptID = eqpt.getEqptID();
		eqptNam = eqpt.getRepoID();
		
		//验证库房设备名称是否重复
		if(eqptDao.queryEqptIDModifyByNam(repoID, eqptID, eqptNam)!=null){
			throw new BusinessException("设备名称重复，请重新输入！");
		}
		commonDao.updateEntity(eqpt);
		return eqptID;
	}
	
	/**
	 * 设备排、层同步到单元格
	 */
	@Override
	public void delPartNOAndByID(String eqptID, String aftPartNO,
			String aftLevNO) throws Exception {
		eqptDao.delPartNOAndByID(eqptID, aftPartNO, aftLevNO);
	}

}
