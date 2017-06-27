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
@Table(name="REPO_DEF")
@SuppressWarnings("serial")
public class Repo implements TreeBean ,Serializable{
	
	//库房ID
	@Id
	@Column(name = "REPOID")
	@MaxUtf8Length(value=32, message="库房ID")
	private String repoID;
	
	//库房名称
	@Column(name = "REPONAM")
	@MaxUtf8Length(value=100, message="库房名称")
	@NotBlank
	private String repoNam;
	
	//所属全宗
	@Column(name = "GNLARVID")
	@MaxUtf8Length(value=100, message="所属全宗")
	@NotBlank
	private String gnlArvID;
	
	//联系人
	@Column(name = "CONTACTS")
	@MaxUtf8Length(value=100, message="联系人")
	@NotBlank
	private String contacts;
	
	//顺序号
	@Column(name = "SEQNO")
	private Integer seqNO;

	//描述信息
	@Column(name = "DESCR")
	@MaxUtf8Length(value=1000, message="描述信息")
	private String descr;

	public String getRepoID() {
		return repoID;
	}

	public void setRepoID(String repoID) {
		this.repoID = repoID;
	}

	public String getRepoNam() {
		return repoNam;
	}

	public void setRepoNam(String repoNam) {
		this.repoNam = repoNam;
	}

	public String getGnlArvID() {
		return gnlArvID;
	}

	public void setGnlArvID(String gnlArvID) {
		this.gnlArvID = gnlArvID;
	}

	public String getContacts() {
		return contacts;
	}

	public void setContacts(String contacts) {
		this.contacts = contacts;
	}
	
	public Integer getSeqNO() {
		return seqNO;
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
	
	/*****************************************
	 * tree
	 ********************************************/
	@Override
	public String obtainTreeId() {
		return this.repoID;
	}

	@Override
	public String obtainTreeText() {
		return this.repoNam;
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
		map.put("repoNam", repoNam);
		map.put("gnlArvID", gnlArvID);
		map.put("contacts", contacts);
		map.put("seqNO", seqNO==null?"":""+seqNO);
		map.put("descr", descr);
		return map;
	}
		
}
