package com.hitoo.entity.arv;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Table(name = "ARV_VOLINFO")
@SuppressWarnings("serial")
public class ArvVolInfo implements Serializable{
	@Id
	@Column(name = "VOLID")
	private String volID;
	@Column(name = "VOLNO")
	private String volNO;//卷号
	@Column(name = "VOLOBJ")
	private String volObj ;  //卷所属对象，比如卷归属的某个社区，但是该社区有编号
	@Column(name = "ARVCATEGORY")
	private String arvCategory;// 类目
	@Column(name = "VOLNAM")
	@NotBlank
	private String volNam;//卷名字
	@Column(name = "VOLREM")
	
	private String volRem;//卷的备考信息
	@Column(name = "SECLEV")
	private String secLev;
	@Column(name = "KEEPTERM")
	private String keepTerm;
	
	@Column(name = "CRTUNIT")
	@NotBlank
	private String crtUnit;//立卷单位
	@Column(name = "CRTUSRID")
	private String crtUsrID;   //立卷人
	
	@Temporal(TemporalType.DATE)
	@Column(name = "CRTDTE")
	@NotNull
	private Date crtDte;       //立卷时间
	
	@Column(name = "VOLSTA")
	private String volSta;
	@Column(name = "VOLORD")
	private Integer volOrd;//卷号的最后序号，即xxxx-10，排序用
	@Column(name = "ARVNUM")
	private Integer arvNum;
	@Column(name = "PAGNUM")
	private Integer pagNum;
	//--------------位置信息
	@Column(name = "ARVTYPEID")
	private String arvTypeID;
	@Column(name = "REPOSTA")
	private String repoSta;
	@Column(name = "CELLID")
	private String cellID;
	
	@Column(name = "CELLNOID")
	private String cellnoId;
	
	@Column(name = "DESCR")
	private String descr;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TS")
	private Date ts;
	
	@Column(name = "RELPK")
	private String relPk;
	
	// 组卷模式
	@Column(name = "VOLMOD")
	private String volMod;
	
	//档案或者案卷的后续处理状态
	@Column(name = "MGRSTA")
	private String mgrSta;
	
	//提前录入卷索引的档案都是 volDigitalSta = '0' ,导入到历史系统之后，更新为9 ，从历史档案导回到eams系统时，更新为5(6)。
	//当数据化加工开始从eams选择数据导入到eamshis时，volDigitalSta作为选择数据的必要条件 ，
	//再eamshis加工完毕，数据更新到eams时，再把volDigitalSta的状态更新为1，表明此类的卷时数据化加工已完成。
	// 0 提前录入的卷索引，还没开始数字化   ； 9 正在数据化加工过程中 ；
	//导回之后，eams的案卷标记为5，同时eamshis的标记为6
	//4不需要数据化加工的卷（或者卷为空盒或者里面的内容不需要加工）
	@Column(name = "VOLDIGITALSTA")
	private String volDigitalSta ;
	
	
	@Column(name = "RECID") 
	private String recid;//交接单ID
	
	@Column(name = "REASONS") 
	private String reasons;//回退原因
	
	
	public String getReasons() {
		return reasons;
	}

	public void setReasons(String reasons) {
		this.reasons = reasons;
	}

	@Transient 
	private String repoCellInfo;//库房位置描述


	public String getRecid() {
		return recid;
	}

	public void setRecid(String recid) {
		this.recid = recid;
	}

	public String getVolID() {
		return volID;
	}

	public void setVolID(String volID) {
		this.volID = volID;
	}

	public String getVolNO() {
		return volNO;
	}

	public void setVolNO(String volNO) {
		this.volNO = volNO;
	}

	public String getVolObj() {
		return volObj;
	}

	public void setVolObj(String volObj) {
		this.volObj = volObj;
	}
	
	

	public String getArvCategory() {
		return arvCategory;
	}

	public void setArvCategory(String arvCategory) {
		this.arvCategory = arvCategory;
	}

	public String getVolNam() {
		return volNam;
	}

	public void setVolNam(String volNam) {
		this.volNam = volNam;
	}

	public String getVolRem() {
		return volRem;
	}

	public void setVolRem(String volRem) {
		this.volRem = volRem;
	}

	public String getSecLev() {
		return secLev;
	}

	public void setSecLev(String secLev) {
		this.secLev = secLev;
	}

	public String getKeepTerm() {
		return keepTerm;
	}

	public void setKeepTerm(String keepTerm) {
		this.keepTerm = keepTerm;
	}

	public String getCrtUnit() {
		return crtUnit;
	}

	public void setCrtUnit(String crtUnit) {
		this.crtUnit = crtUnit;
	}

	public String getCrtUsrID() {
		return crtUsrID;
	}

	public void setCrtUsrID(String crtUsrID) {
		this.crtUsrID = crtUsrID;
	}

	public Date getCrtDte() {
		return crtDte;
	}

	public void setCrtDte(Date crtDte) {
		this.crtDte = crtDte;
	}

	public String getVolSta() {
		return volSta;
	}

	public void setVolSta(String volSta) {
		this.volSta = volSta;
	}

	public Integer getVolOrd() {
		return volOrd;
	}

	public void setVolOrd(Integer volOrd) {
		this.volOrd = volOrd;
	}

	public Integer getArvNum() {
		return arvNum;
	}

	public void setArvNum(Integer arvNum) {
		this.arvNum = arvNum;
	}

	public Integer getPagNum() {
		return pagNum;
	}

	public void setPagNum(Integer pagNum) {
		this.pagNum = pagNum;
	}

	public String getRepoSta() {
		return repoSta;
	}

	public void setRepoSta(String repoSta) {
		this.repoSta = repoSta;
	}

	public String getCellID() {
		return cellID;
	}

	public void setCellID(String cellID) {
		this.cellID = cellID;
	}

	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	public Date getTs() {
		return ts;
	}

	public void setTs(Date ts) {
		this.ts = ts;
	}

	public String getRelPk() {
		return relPk;
	}

	public void setRelPk(String relPk) {
		this.relPk = relPk;
	}

	public String getVolMod() {
		return volMod;
	}

	public void setVolMod(String volMod) {
		this.volMod = volMod;
	}

	public String getVolDigitalSta() {
		return volDigitalSta;
	}

	public void setVolDigitalSta(String volDigitalSta) {
		this.volDigitalSta = volDigitalSta;
	}

	public String getArvTypeID() {
		return arvTypeID;
	}

	public void setArvTypeID(String arvTypeID) {
		this.arvTypeID = arvTypeID;
	}

	public String getMgrSta() {
		return mgrSta;
	}

	public void setMgrSta(String mgrSta) {
		this.mgrSta = mgrSta;
	}

	public String getRepoCellInfo() {
		return repoCellInfo;
	}

	public void setRepoCellInfo(String repoCellInfo) {
		this.repoCellInfo = repoCellInfo;
	}

	public String getCellnoId() {
		return cellnoId;
	}

	public void setCellnoId(String cellnoId) {
		this.cellnoId = cellnoId;
	}

	
	
	
}