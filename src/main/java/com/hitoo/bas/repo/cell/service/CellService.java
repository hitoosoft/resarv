package com.hitoo.bas.repo.cell.service;

import java.util.List;
import java.util.Map;

import com.hitoo.bas.entity.Cell;
import com.hitoo.entity.arv.ArvBasInfo;
import com.hitoo.frame.pub.global.LoginInfo;
import com.hitoo.frame.pub.model.PageInfo;
import com.hitoo.frame.pub.model.TreeModel;

public interface CellService {
	
	/**
	 * 根据设备ID查询单元格
	 */
	public Cell queryCellByEqptID(String eqptID) throws Exception;
	
	/**
	 * 根据设备ID查询排
	 */
	public List<TreeModel> queryCellPartNOByEqptID(String eqptID) throws Exception;
	
	/**
	 * 1.根据设备ID、排数查单元格
	 * 2.查询列的最大的单元格数量
	 */
	public Map<String, Object> queryCellByEqptIDAndPartNO(String eqptID, Integer partNO,String arvTypeID) throws Exception ;
	
	/**
	 * 保存库房单元格和档案分类的关系
	 */
	public void saveOrUpdateCellArvTypeInPart(String arvTypeID, String selectedCellIDs, String eqptID, String partNO) throws Exception ;
	
	/**
	 * 更新格子的容量
	 */
	public void updateCellVolumn(String selectedCellIDs,Integer totalNum) throws Exception ;
	
	/**
	 * 清除库房单元格和档案分类的关系
	 */
	public void clearAllCellByArvTypeID(String arvTypeID ) throws Exception ;
	
	/**
	 * 新增设备排
	 */
	public void addEqptPartNO(String eqptID) throws Exception ;
	
	/**
	 * 新增单元格
	 */
	public void addCell(Cell cell) throws Exception ;
	
	/**
	 * 删除单元格
	 */
	public void deleteCell(String cellIDs) throws Exception ;
	
	/**
	 * 智能划分单元格
	 */
	public String addAutoCell(Cell cell) throws Exception ;
	
	/**
	 * 入库保存
	 */
	public void saveInRepo(LoginInfo user, String entityType ,String entityIDs,String cellIDs) throws Exception ;
	
	/**
	 * 查询单元格的内容
	 */
	public PageInfo queryCellContent(String cellID) throws Exception ;
	

	/**
	 * 案卷位置
	 */
	public String queryRepoCellInfo(String cellID) throws Exception;
}
