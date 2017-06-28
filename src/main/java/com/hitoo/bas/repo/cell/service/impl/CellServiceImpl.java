package com.hitoo.bas.repo.cell.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hitoo.entity.arv.ArvBasInfo;
import com.hitoo.entity.arv.ArvVolInfo;
import com.hitoo.bas.entity.Cell;
import com.hitoo.bas.entity.CellNo;
import com.hitoo.bas.entity.CellArvtype;
import com.hitoo.bas.entity.Eqpt;
import com.hitoo.enumdic.EnumOrganizeModel;
import com.hitoo.enumdic.EnumRepoSta;
import com.hitoo.enumdic.EnumYesNo;
import com.hitoo.bas.repo.cell.service.CellService;
import com.hitoo.bas.repo.cell.dao.CellDao;
import com.hitoo.frame.base.BaseService;
import com.hitoo.frame.base.BusinessException;
import com.hitoo.frame.common.util.TreeUtil;
import com.hitoo.frame.pub.global.LoginInfo;
import com.hitoo.frame.pub.model.PageInfo;
import com.hitoo.frame.pub.model.TreeModel;

@Service
public class CellServiceImpl extends BaseService implements CellService {
	
	@Autowired
	private CellDao cellDao;
	
	/**
	 * 根据设备ID查询单元格
	 */
	@Override
	public Cell queryCellByEqptID(String eqptID) throws Exception {
		return cellDao.queryCountOfCellByEqptID(eqptID);
	}
	
	/**
	 * 保存库房单元格和档案分类的关系
	 */
	@Override
	public void saveOrUpdateCellArvTypeInPart(String arvTypeID, String selectedCellIDs, String eqptID, String partNO) throws Exception {
		List<String> cellIDList = null;
		if(StringUtils.isNotBlank(selectedCellIDs)){
			cellIDList = Arrays.asList(selectedCellIDs.split(","));
		}else{
			cellIDList = new ArrayList<String>();
		}
		cellDao.deleteCellArvType(arvTypeID, eqptID, partNO);
		for(String cellID : cellIDList){
			if(StringUtils.isNotBlank(cellID)){
				CellArvtype cellArvtype = new CellArvtype();
				cellArvtype.setCellID(cellID);
				cellArvtype.setArvTypeID(arvTypeID);
				cellArvtype.setPk(dBUtil.getCommonId());
				commonDao.saveEntity(cellArvtype);
			}
		}
	}
	
	/**
	 * 更新格子的容量
	 */
	public void updateCellVolumn(String selectedCellIDs, Integer totalNum) throws Exception {
		for(String cellID:selectedCellIDs.split(",")){
			if(StringUtils.isNotBlank(cellID)){
				Cell cell=commonDao.findEntityByID(Cell.class, cellID);
				if(cell.getUsedNum().intValue() > 0){
					throw new BusinessException("单元格已经存在档案，不能修改");
				}
				cell.setTotalNum(totalNum);
				commonDao.updateEntity(cell);
				
				//先查出顺序号，如果状态已用，则不能修改容量
				int count = cellDao.queryCellNoByCellID(cellID);
				if(count > 0){
					cellDao.deleteCellNoByCellID(cellID);
					//存在且没有使用，则可以先删除，后修改
					for(int i=1;i<=totalNum;i++){
						CellNo newcellno = new CellNo();
						newcellno.setPk(dBUtil.getCommonId());
						newcellno.setCellId(cell.getCellID());
						newcellno.setEqptid(cell.getEqptID());
						newcellno.setTotalnum(totalNum);
						newcellno.setExistFlag(EnumYesNo.no.getCode());
						newcellno.setSeqNO(i);
						commonDao.saveEntity(newcellno);
				    }
				}
		    }
		}
	}
	
	/**
	 * 清除库房单元格和档案分类的关系
	 */
	public void clearAllCellByArvTypeID(String arvTypeID ) throws Exception {
		cellDao.clearAllCellByArvTypeID(arvTypeID);
	}
	
	/**
	 * 新增单元格
	 */
	@Override
	public void addCell(Cell cell) throws Exception {
		String eqptID = cell.getEqptID();
		Integer partNO = cell.getPartNO();
		Integer levNO = cell.getLevNO();
		//根据设备ID，设备排号，查询设备最大列号
		if(levNO > cellDao.queryMaxLevNO(eqptID, partNO)){
			cell.setCellNO(1);
		}else{
			//根据设备ID，设备的排号和列号，查询该排最大的层号
			Integer maxCellNO = cellDao.queryMaxCellNO(eqptID, partNO, levNO);
			cell.setCellNO(maxCellNO + 1);
		}
		cell.setCellID(dBUtil.getCommonId());
		if(null == cell.getTotalNum()){
			cell.setTotalNum(0);
		}
		if(null == cell.getUsedNum()){
			cell.setUsedNum(0);
		}
		commonDao.saveEntity(cell);
		//新增单元格的每一个序号
		if(cell.getTotalNum() > 0){
		    for(int i=1;i<=cell.getTotalNum();i++){
				CellNo cellno = new CellNo();
				cellno.setPk(dBUtil.getCommonId());
				cellno.setCellId(cell.getCellID());
				cellno.setEqptid(cell.getEqptID());
				cellno.setTotalnum(cell.getTotalNum());
				cellno.setExistFlag(EnumYesNo.no.getCode());
				cellno.setSeqNO(i);
				commonDao.saveEntity(cellno);
		     }
		}
	}
	
	/**
	 * 删除单元格
	 */
	@Override
	public void deleteCell(String cellIDs) throws Exception {
		for(String cellID : cellIDs.split(",")){
			//验证
			if(commonDao.findEntityByID(Cell.class, cellID)==null){
				throw new BusinessException("单元格不存在，请刷新后重新选择！");
			}
			//验证单元格中是否有档案
			if(cellDao.queryFirstArvtypeUsingCellID(cellID)!=null){
				throw new BusinessException("单元格中存有档案，不能删除！");
			}
			
			//验证单元格中是否有档案
			if(cellDao.queryFirstVolUsingCellID(cellID)!=null){
				throw new BusinessException("单元格中存有案卷或案件，不能删除！");
			}
			commonDao.delEntityById(Cell.class, cellID);
			//删除单元格中的顺序号
			cellDao.deleteCellNoByCellID(cellID);
		}
	}
	
	/**
	 * 智能划分单元格
	 */
	@Override
	public String addAutoCell(Cell cell) throws Exception {
		String eqptID = cell.getEqptID() ;
		Integer cellNO = cell.getCellNO() ; //每列包含的单元格数量
		
		if(cellNO==null||cellNO<=0){
			throw new BusinessException("每层包含的单元格数量不能为空，并且必须大于零！");
		}
		Eqpt eqpt = commonDao.findEntityByID(Eqpt.class, eqptID);
		//设备排数
		Integer partNum = eqpt.getPartNum();
		//设备列数
		Integer levNum = eqpt.getLevNum();
		//查看设备是否已划分了单元格
		if(cellDao.queryCountOfCellByEqptID(eqptID)!=null){
			throw new BusinessException("设备已存在单元格，不能再继续智能划分！");
		}
		
		//划分单元格：智能划分的单元格数=  排 * 层 * 每层包含的单元格数量
		for(int i=1 ; i<=partNum;i++){
			for(int j=1 ; j<=levNum;j++){
				for(int m=1;m<=cellNO;m++){
					Cell newCell = new Cell();
					newCell.setCellID(dBUtil.getCommonId());
					newCell.setEqptID(eqptID);
					newCell.setPartNO(i);
					newCell.setLevNO(j);
					newCell.setCellNO(m);
					if(null == cell.getTotalNum()){
						newCell.setTotalNum(0);
					}else{
						newCell.setTotalNum(cell.getTotalNum());
					}
					newCell.setUsedNum(0);
					commonDao.saveEntity(newCell);
					//新增新的单元格的顺序号
					if(cell.getTotalNum() > 0){
					    for(int k=1;k<=cell.getTotalNum();k++){
							CellNo cellno = new CellNo();
							cellno.setPk(dBUtil.getCommonId());
							cellno.setCellId(newCell.getCellID());
							cellno.setEqptid(newCell.getEqptID());
							cellno.setTotalnum(cell.getTotalNum());
							cellno.setExistFlag(EnumYesNo.no.getCode());
							cellno.setSeqNO(k);
							commonDao.saveEntity(cellno);
					     }
					}
				}
			}
		}
		return eqptID;
	}
	
	/**
	 * 新增设备排
	 */
	@Override
	public void addEqptPartNO(String eqptID) throws Exception {
		//根据设备ID查询cell中最大排号
		Integer partNO = cellDao.queryMaxPartNO(eqptID);
		//新增单元格
		Cell cell = new Cell();
		cell.setCellID(dBUtil.getCommonId());
		cell.setPartNO(partNO+1);
		cell.setCellNO(1);
		cell.setLevNO(1);
		cell.setEqptID(eqptID);
		commonDao.saveEntity(cell);
	}
	
	/**
	 * 根据设备ID查询单元格排
	 */
	@Override
	public List<TreeModel> queryCellPartNOByEqptID(String eqptID) throws Exception {
		return TreeUtil.setTree(cellDao.queryCellPartNOByEqptID(eqptID));
	}
	
	/**
	 * 1.根据设备ID、排数查单元格
	 * 2.查询列的最大的单元格数量
	 */
	@Override
	public Map<String, Object> queryCellByEqptIDAndPartNO(String eqptID, Integer partNO,String arvTypeID) throws Exception {
		return cellDao.BuildCellByHtmlByEqptIDAndPartNO(eqptID, partNO,arvTypeID);
	}
	
	/**
	 * 获取可用的单元格
	 */
	private Cell getCanRepoCell(List<Cell> cellList){
		for(Cell cell : cellList){
			if(cell.getTotalNum()==null){
				return cell;
			}else{
				if(cell.getUsedNum()!=null){
					if(cell.getTotalNum() - cell.getUsedNum() > 0){
						return cell;
					}
				}
			}
			
		}
		return null;
	}
	
	/**
	 * 获取可用的单元格
	 */
	private CellNo getCanRepoCellNo(List<CellNo> cellnoList){
		for(CellNo cellno : cellnoList){
			if(cellno.getExistFlag().equals(EnumYesNo.no.getCode())){
				return cellno;
			}
		}
		return null;
	}
	
	/**
	 * 入库保存
	 */
	@Override
	public void saveInRepo(LoginInfo user, String entityType, String entityIDs, String cellIDs) throws Exception {
		String[] ids = entityIDs.split(",");
		List<Cell> cellList = cellDao.queryCellByIDs(cellIDs);
		List<CellNo> cellnoList = cellDao.queryCellNoByCells(cellList);
		/* 由于用户管理不规范，不限制用户入库
		int canContainSize = 0;
		for(Cell cell : cellList){
			if(null == cell.getTotalNum()){
				throw new BusinessException("单元格未设置容量！");
			}
			canContainSize += cell.getTotalNum() - cell.getUsedNum();
		}
		if(canContainSize < ids.length){
			throw new BusinessException("所放档案超出单元格容量！");
		}*/
		
		if(EnumOrganizeModel.arv.getCode().equals(entityType)){
			for (String arvID : ids) {
				ArvBasInfo arvInfo =commonDao.findEntityByID(ArvBasInfo.class, arvID);
				if(StringUtils.isNotBlank(arvInfo.getCellID())){
					Cell oldcell = commonDao.findEntityByID(Cell.class, arvInfo.getCellID());
					if(oldcell!=null){
						if(oldcell.getUsedNum()!=null&&oldcell.getUsedNum()>0){
							oldcell.setUsedNum(oldcell.getUsedNum()-1);
							commonDao.updateEntity(oldcell);
						}
					}
				}
				if(StringUtils.isNotBlank(arvInfo.getCellnoID())){
					CellNo oldcellno = commonDao.findEntityByID(CellNo.class, arvInfo.getCellnoID());
					if(oldcellno!=null){
						oldcellno.setExistFlag(EnumYesNo.no.getCode());
						commonDao.updateEntity(oldcellno);
					}
				}
				Cell cell = getCanRepoCell(cellList);
				arvInfo.setCellID(cell.getCellID());
				arvInfo.setRepoSta(EnumRepoSta.INREPO.getCode());
				cell.setUsedNum((cell.getUsedNum()==null ? 1 : cell.getUsedNum()) + 1);
				commonDao.updateEntity(cell);
				//获得可用的序号并修改保存
				CellNo cellno = getCanRepoCellNo(cellnoList);
				if(cellno!=null){
					cellno.setArvID(arvID);
					cellno.setExistFlag(EnumYesNo.yes.getCode());
					commonDao.updateEntity(cellno);
					arvInfo.setCellnoID(cellno.getPk());
				}
				commonDao.updateEntity(arvInfo);
			}
		}else if(EnumOrganizeModel.vol.getCode().equals(entityType)){
			for(String volID : ids){
				ArvVolInfo volInfo =commonDao.findEntityByID(ArvVolInfo.class, volID);
				volInfo.setVolMod(EnumOrganizeModel.vol.getCode());
				if(StringUtils.isNotBlank(volInfo.getCellID())){
					Cell oldcell = commonDao.findEntityByID(Cell.class, volInfo.getCellID());
					if(oldcell!=null){
						if(oldcell.getUsedNum()!=null&&oldcell.getUsedNum()>0){
							oldcell.setUsedNum(oldcell.getUsedNum()-1);
							commonDao.updateEntity(oldcell);
						}
					}
				}
				if(StringUtils.isNotBlank(volInfo.getCellnoId())){
					CellNo oldcellno = commonDao.findEntityByID(CellNo.class, volInfo.getCellnoId());
					if(oldcellno!=null){
						oldcellno.setExistFlag(EnumYesNo.no.getCode());
						commonDao.updateEntity(oldcellno);
					}
				}
				Cell cell = getCanRepoCell(cellList);
				volInfo.setCellID(cell.getCellID());
				volInfo.setRepoSta(EnumRepoSta.INREPO.getCode());
				//获得可用的序号并修改
				CellNo cellno = getCanRepoCellNo(cellnoList);
				if(cellno!=null){
					cellno.setArvID(volID);
					cellno.setExistFlag(EnumYesNo.yes.getCode());
					commonDao.updateEntity(cellno);
					volInfo.setCellnoId(cellno.getPk());
				}
				List<ArvBasInfo> arvInfoList=commonDao.findEntityList(ArvBasInfo.class, "volID", volID);
				for(ArvBasInfo arvInfo : arvInfoList){
					arvInfo.setCellID(cell.getCellID());
					arvInfo.setRepoSta(EnumRepoSta.INREPO.getCode());
					if(null != cellno){
						arvInfo.setCellnoID(cellno.getPk());
					}
					commonDao.updateEntity(arvInfo);
				}
				commonDao.updateEntity(volInfo);
				cell.setUsedNum((cell.getUsedNum()==null?0:cell.getUsedNum())+ 1);
				commonDao.updateEntity(cell);
			}
		}else if(EnumOrganizeModel.piece.getCode().equals(entityType)){
			for(String volid:ids){
				ArvVolInfo volInfo =commonDao.findEntityByID(ArvVolInfo.class, volid);
				volInfo.setVolMod(EnumOrganizeModel.piece.getCode());
			}
			throw new BusinessException("未实现");
		}
	}
	
	/**
	 * 查询单元格的内容
	 */
	public PageInfo queryCellContent(String cellID) throws Exception {
		return cellDao.queryCellContent(cellID);
	}
	
	/**
	 * 案卷位置
	 */
	@Override
	public String queryRepoCellInfo(String cellID) throws Exception {
		return cellDao.queryRepoCellInfo(cellID);
	}

}
