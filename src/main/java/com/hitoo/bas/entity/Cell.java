package com.hitoo.bas.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.hitoo.frame.common.util.TreeBean;

/** 
 * 单元格实体类
 */
@Entity
@Table(name="REPO_CELL")
@SuppressWarnings("serial")
public class Cell implements Serializable, TreeBean{
	//单元格ID
	@Id
	@Column(name="CELLID")
	private String cellID;
	//排号
	@Column(name="PARTNO")
	private Integer partNO;
	//列号
	@Column(name="LEVNO")
	private Integer levNO;
	//层号
	@Column(name="CELLNO")
	private Integer cellNO;
	//设备ID
	@Column(name="EQPTID")
	private String eqptID;
	
	//可放容量
	@Column(name="TOTALNUM")
	private Integer totalNum;
	//已用容量
	@Column(name="USEDNUM")
	private Integer usedNum;
	
   
	public String getCellID() {
		return cellID;
	}

	public void setCellID(String cellID) {
		this.cellID = cellID;
	}
	
	public Integer getPartNO() {
		return partNO;
	}

	public void setPartNO(Integer partNO) {
		this.partNO = partNO;
	}

	public Integer getLevNO() {
		return levNO;
	}

	public void setLevNO(Integer levNO) {
		this.levNO = levNO;
	}

	public Integer getCellNO() {
		return cellNO;
	}

	public void setCellNO(Integer cellNO) {
		this.cellNO = cellNO;
	}

	public String getEqptID() {
		return eqptID;
	}

	public void setEqptID(String eqptID) {
		this.eqptID = eqptID;
	}
	
	

	public Integer getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(Integer totalNum) {
		this.totalNum = totalNum;
	}

	public Integer getUsedNum() {
		return usedNum;
	}

	public void setUsedNum(Integer usedNum) {
		this.usedNum = usedNum;
	}

	@Override
	public String obtainTreeId() {
		return cellID;
	}

	@Override
	public String obtainTreeText() {
		return partNO+"排"+cellNO+"列"+levNO+"层";
	}

	@Override
	public String obtainTreeParentID() {
		return null;
	}

	@Override
	public String obtainTreeState() {
		return null;
	}

	@Override
	public int obtainTreeSeqNO() {
		//防止顺序乱，1-1-1 ，1-1-2 ，1-1-20  --> 1-1-001 1-1-002 1-1-120 
		String partNOStr = partNO==null?"":""+(partNO.intValue()) ; 
		String levNOStr = levNO==null?"":""+(10000 + levNO.intValue());
		String cellNOStr = cellNO==null?"":""+(100 + cellNO.intValue()) ;
		String tmpStr = partNOStr + levNOStr + cellNOStr;
		
		return Integer.parseInt((tmpStr==null||tmpStr.equals(""))?"0":tmpStr);
	}

	@Override
	public String obtainIconCls() {
		return null;
	}

	@Override
	public Map<String, String> obtainTreeAttributes() throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		map.put("eqptID", eqptID);
		map.put("partNO", partNO==null?"":""+partNO.intValue());
		map.put("levNO", levNO==null?"":""+levNO.intValue());
		map.put("cellNO", cellNO==null?"":""+cellNO.intValue());
		
		return map;
	}
	
}
