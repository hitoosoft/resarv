package com.hitoo.bas.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotBlank;

import com.hitoo.frame.common.validator.MaxUtf8Length;

@Entity
@Table(name = "BAS_ROLE_GNL_DATAUT")
@SuppressWarnings("serial")
public class RoleGnlDataut implements Serializable {
	// PK
	@Column(name = "PK")
	@MaxUtf8Length(value = 32, message = "PK")
	private String Pk;

	// 全宗ID
	@Column(name = "GNLARVID")
	@MaxUtf8Length(value = 32, message = "全宗ID")
	@NotBlank
	private String gnlArvId;

	// 全宗名称
	@Column(name = "ROLEID")
	@MaxUtf8Length(value = 32, message = "角色ID")
	@NotBlank
	private String roleId;

	public String getPk() {
		return Pk;
	}

	public void setPk(String pk) {
		Pk = pk;
	}

	public String getGnlArvId() {
		return gnlArvId;
	}

	public void setGnlArvId(String gnlArvId) {
		this.gnlArvId = gnlArvId;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
    
}
