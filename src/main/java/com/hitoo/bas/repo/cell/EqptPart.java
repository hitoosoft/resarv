package com.hitoo.bas.repo.cell;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.hitoo.frame.common.util.TreeBean;

@SuppressWarnings("serial")
public class EqptPart implements TreeBean, Serializable{
	//排号
	private Integer partNO;
	
	//设备ID
	private String eqptID;
	
	public Integer getPartNO() {
		return partNO;
	}

	public void setPartNO(Integer partNO) {
		this.partNO = partNO;
	}

	public String getEqptID() {
		return eqptID;
	}

	public void setEqptID(String eqptID) {
		this.eqptID = eqptID;
	}

	@Override
	public String obtainTreeId() {
		return ""+partNO;
	}

	@Override
	public String obtainTreeText() {
		return "第"+partNO+"排";
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
		return partNO.intValue();
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
		return map;
	}
	
}
