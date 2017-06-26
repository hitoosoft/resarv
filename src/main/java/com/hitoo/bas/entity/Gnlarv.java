package com.hitoo.bas.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.validator.constraints.NotBlank;

import com.hitoo.frame.common.util.TreeBean;
import com.hitoo.frame.common.validator.MaxUtf8Length;

@Entity
@Table(name = "BAS_GNLARV")
@SuppressWarnings("serial")
public class Gnlarv implements TreeBean, Serializable {
	// 全宗ID
	@Id
	@Column(name = "GNLARVID")
	@MaxUtf8Length(value = 32, message = "全宗ID")
	private String gnlArvID;

	// 全宗编码
	@Column(name = "GNLARVCOD")
	@MaxUtf8Length(value = 32, message = "全宗编码")
	@NotBlank
	private String gnlArvCod;

	// 全宗名称
	@Column(name = "GNLARVNAM")
	@MaxUtf8Length(value = 100, message = "全宗名称")
	@NotBlank
	private String gnlArvNam;

	// 立档单位
	@Column(name = "ORGID")
	@NotBlank
	private String orgID;

	// 全宗序号
	@Column(name = "SEQNO")
	private Integer seqNO;

	// 全宗描述
	@Column(name = "DESCR")
	@MaxUtf8Length(value = 1000, message = "描述信息")
	private String descr;

	// 立档单位名称（非数据库字段）
	@Transient
	private String orgNam;

	public String getGnlArvID() {
		return gnlArvID;
	}

	public void setGnlArvID(String gnlArvID) {
		this.gnlArvID = gnlArvID;
	}

	public String getGnlArvCod() {
		return gnlArvCod;
	}

	public void setGnlArvCod(String gnlArvCod) {
		this.gnlArvCod = gnlArvCod;
	}

	public String getGnlArvNam() {
		return gnlArvNam;
	}

	public void setGnlArvNam(String gnlArvNam) {
		this.gnlArvNam = gnlArvNam;
	}

	public String getOrgID() {
		return orgID;
	}

	public void setOrgID(String orgID) {
		this.orgID = orgID;
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

	public String getOrgNam() {
		return orgNam;
	}

	public void setOrgNam(String orgNam) {
		this.orgNam = orgNam;
	}

	/*******************************************
	 * Tree
	 ******************************************/
	
	@Override
	public String obtainTreeId() {
		return this.gnlArvID;
	}

	@Override
	public String obtainTreeText() {
		return this.gnlArvNam;
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
		return "icon-chart-organisation";
	}

	@Override
	public Map<String, String> obtainTreeAttributes() throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		map.put("gnlArvID", gnlArvID);
		map.put("gnlArvCod", gnlArvCod);
		map.put("code", gnlArvCod);  //为了统一的travelTree
		map.put("gnlArvNam", gnlArvNam);
		map.put("orgID", orgID);
		map.put("orgNam", orgNam);
		map.put("seqNO", "" + seqNO);
		map.put("descr", descr);
		return map;
	}

}
