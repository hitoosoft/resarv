package com.hitoo.bas.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 格内顺序表
 */
@Entity
@Table(name="REPO_CELL_NO")
@SuppressWarnings("serial")
public class CellNo implements Serializable{
	
	//ID
	@Id
	@Column(name = "PK")
	private String Pk;
	
	//单元格ID
	@Column(name = "CELLID")
	private String cellId;
	
	//档案ID
	@Column(name = "ARVID")
	private String arvID;
	
	//总数
	@Column(name = "TOTALNUM")
	private Integer totalnum;
	
	//序号
	@Column(name = "SEQNO")
	private Integer seqNO;
	
	//是否已用
	@Column(name="EXISTFLAG")
	private String existFlag;
	
	//设备描述
	@Column(name = "COMDESC", length=100)
	private String comddesc;
	
	//设备ID
	@Column(name = "EQPTID")
	private String eqptid;

	public String getPk() {
		return Pk;
	}

	public void setPk(String pk) {
		Pk = pk;
	}

	public String getCellId() {
		return cellId;
	}

	public void setCellId(String cellId) {
		this.cellId = cellId;
	}

	public String getArvID() {
		return arvID;
	}

	public void setArvID(String arvID) {
		this.arvID = arvID;
	}

	public Integer getTotalnum() {
		return totalnum;
	}

	public void setTotalnum(Integer totalnum) {
		this.totalnum = totalnum;
	}

	public Integer getSeqNO() {
		return seqNO;
	}

	public void setSeqNO(Integer seqNO) {
		this.seqNO = seqNO;
	}

	public String getExistFlag() {
		return existFlag;
	}

	public void setExistFlag(String existFlag) {
		this.existFlag = existFlag;
	}

	public String getComddesc() {
		return comddesc;
	}

	public void setComddesc(String comddesc) {
		this.comddesc = comddesc;
	}

	public String getEqptid() {
		return eqptid;
	}

	public void setEqptid(String eqptid) {
		this.eqptid = eqptid;
	}
	
}
