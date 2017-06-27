package com.hitoo.bas.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotBlank;

import com.hitoo.frame.common.util.TreeBean;
import com.hitoo.frame.common.validator.MaxUtf8Length;

@Entity
@Table(name="REPO_EQPT")
@SuppressWarnings("serial")
public class Eqpt implements Serializable, TreeBean{
	
	//设备ID
	@Id
	@Column(name = "EQPTID")
	@MaxUtf8Length(value=32, message="设备ID")
	private String eqptID;
	
	//库房ID
	@Column(name = "REPOID")
	@MaxUtf8Length(value=32, message="库房ID")
	@NotBlank
	private String repoID;
	
	//设备名称
	@Column(name = "EQPTNAM")
	@MaxUtf8Length(value=100, message="设备名称")
	@NotBlank
	private String eqptNam;
	
	//设备排数
	@Column(name = "PARTNUM")
	private Integer partNum;
	
	//设备列数
	@Column(name = "LEVNUM")
	private Integer levNum;
	
	//顺序号
	@Column(name="SEQNO")
	private Integer seqNO;
	
	//设备描述
	@Column(name = "DESCR", length=1000)
	@MaxUtf8Length(value=1000, message="设备描述")
	private String descr;
	
	public String getEqptID() {
		return eqptID;
	}
	public void setEqptID(String eqptID) {
		this.eqptID = eqptID;
	}
	
	public String getEqptNam() {
		return eqptNam;
	}
	public void setEqptNam(String eqptNam) {
		this.eqptNam = eqptNam;
	}
	
	public String getRepoID() {
		return repoID;
	}
	public void setRepoID(String repoID) {
		this.repoID = repoID;
	}
	
	public Integer getPartNum() {
		return partNum;
	}
	public void setPartNum(Integer partNum) {
		this.partNum = partNum;
	}
	
	public Integer getLevNum() {
		return levNum;
	}
	public void setLevNum(Integer levNum) {
		this.levNum = levNum;
	}
	
	public Integer getSeqNO() {
		return seqNO==null?0:seqNO;
	}
	public void setSeqNO(Integer seqNO) {
		this.seqNO = seqNO;
	}
	
	public String getDescr() {
		return descr;
	}
	public void setDescr(String descr) {
		this.descr = descr;
	}
	
	@Override
	public String obtainTreeId() {
		return eqptID;
	}
	@Override
	public String obtainTreeText() {
		return eqptNam;
	}
	@Override
	public String obtainTreeParentID() {
		return repoID;
	}
	@Override
	public String obtainTreeState() {
		return null;
	}
	@Override
	public int obtainTreeSeqNO() {
		return seqNO==null?0:seqNO;
	}
	@Override
	public String obtainIconCls() {
		return null;
	}
	@Override
	public Map<String, String> obtainTreeAttributes() throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		map.put("repoID", repoID);
		map.put("eqptNam",eqptNam);
		map.put("partNum", partNum==null?"":""+partNum.intValue());
		map.put("levNum", levNum==null?"":""+levNum.intValue());
		map.put("descr", descr);
		
		return map;
	}
	
}
