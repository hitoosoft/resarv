package com.hitoo.bas.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/** 
 * 档案分类与单元格关系
 */
@Entity
@Table(name="REPO_CELL_ARVTYPE")
@SuppressWarnings("serial")
public class CellArvtype implements Serializable{

	@Id
	@Column(name="PK")
	private String pk;
	//单元格ID
	@Column(name="CELLID")
	private String cellID;
	
	//档案分类ID
	@Column(name="ARVTYPEID")
	private String arvTypeID;

	public String getPk() {
		return pk;
	}

	public void setPk(String pk) {
		this.pk = pk;
	}

	public String getCellID() {
		return cellID;
	}

	public void setCellID(String cellID) {
		this.cellID = cellID;
	}

	public String getArvTypeID() {
		return arvTypeID;
	}

	public void setArvTypeID(String arvTypeID) {
		this.arvTypeID = arvTypeID;
	}

}