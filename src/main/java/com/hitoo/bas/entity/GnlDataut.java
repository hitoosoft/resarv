package com.hitoo.bas.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name = "BAS_GNL_DATAUT")
@SuppressWarnings("serial")
public class GnlDataut implements Serializable {
	// 对应关系ID
	@Id
	@Column(name = "PK")
	private String pk;

	// 角色ID
	@Column(name = "ROLEID")
	private String roleID;

	// 全宗ID
	@Column(name = "GNLARVID")
	private String gnlArvID;

	public String getPk() {
		return pk;
	}

	public void setPk(String pk) {
		this.pk = pk;
	}

	public String getRoleID() {
		return roleID;
	}

	public void setRoleID(String roleID) {
		this.roleID = roleID;
	}

	public String getGnlArvID() {
		return gnlArvID;
	}

	public void setGnlArvID(String gnlArvID) {
		this.gnlArvID = gnlArvID;
	}
	
}
