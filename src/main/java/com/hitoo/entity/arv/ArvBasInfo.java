package com.hitoo.entity.arv;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.validator.constraints.NotBlank;

/**
 * 类描述:档案基本信息表
 * @author qinchao 创建时间 2013-9-29
 */
@Entity
@Table(name = "ARV_BASINFO")
@SuppressWarnings("serial")
public class ArvBasInfo implements Serializable {
	
	// -------------档案通用信息-----------------
	@Id
	@Column(name = "ARVID")
	@NotBlank
	private String arvID;// 档案ID
	@Column(name = "ARVNO")
	private String arvNO;// 档号
	@Column(name = "ARVNAM")
	private String arvNam;// 档案题名
	@Column(name = "SECLEV")
	private String secLev;// 密级
	@Column(name = "GNLARVID")
	private String gnlArvID;// 全宗ID
	@Column(name = "ARVTYPEID")
	@NotBlank
	private String arvTypeID;// 档案分类ID
	@Temporal(TemporalType.DATE)
	@Column(name = "ARVDTE")
	private Date arvDte;// 归档日期:产生档案的日期
	@Column(name = "ARVDTEYEAR")
	private String arvDteYear;// 归档年度
	@Column(name = "KEYWDS")
	private String keyWds;// 关键字
	@Column(name = "KEEPTERM")
	private String keepTerm;// 保管期限
	@Column(name = "REM")
	private String rem;// 备注
	@Column(name = "RELPK")
	private String relPk;// 关联索引即页面条码
	@Column(name = "ARVCATEGORY")
	private String arvCategory;// 类目
	@Column(name = "PAGNUM")
	private Integer pagNum;// 总页数
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TS")
	private Date ts;// 时间戳:上次维护时间
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TSNEWADD")
	private Date tsNewAdd;// 时间戳：第一次新增时间
	@Column(name = "ROLLOUTDTE")
	private String rolloutDte;//档案转出日期
	@Column(name = "ARVSELFNO")
	private String arvSelfNO;//档案自编号，用于备注历史档案编号，方便用户习惯性查找
	
	//---------人事档案和业务档案共用-----
	@Column(name = "OBJID")
	private String objID;// 所属对象标识
	@Column(name = "OBJNAM")
	private String objNam;// 所属对象名称
	@Column(name = "RESSTA")
	private String resSta;//人事档案状态
	@Column(name = "ARCTYPE")
	private String arcType;//存档类别
	@Column(name = "ARCCOMP")
	private String arcComp;//存档单位
	
	//---------业务档案特有----
	@Column(name = "BUSINESSID")
	private String businessID; //业务ID
	
	//---------文书档案特有-----
	@Column(name = "CHARGER")
	private String charger;//责任者
	@Column(name = "DOCNUM")
	private String docNum;//文号
	@Column(name = "PIECENUM")
	private String pieceNum;//件号
	@Column(name = "DOCSTA")
	private String docSta;//文书档案状态
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "COMEDATE")
	private Date comeDate;//形成日期
	@Column(name = "COMEORG")
	private String comeOrg;//来文机关
	@Column(name = "ISSUE")
	private String issue;//问题
	@Column(name = "OFFICETYPE")
	private String officeType;//文书类型 FW发文  LW来文
	@Column(name = "DOCINFOID")
	private String docInfoID;//文书ID
	
	//---------状态，用于清点交接等
	@Column(name = "ARVSTA")
	private String arvSta;//档案状态，参考枚举类
	@Column(name = "DELIVERYTYPE")
	private String deliveryType;//新增档案时,是否走清点交接，或者清点交接的类型，参考常量的字典
	@Column(name = "ISCANEDIT")
	private String isCanEdit;// 档案信息是否可编辑：普通新增、从gs系统新增、从hsu同步新增  ,,, 0 不可编辑 1 可编辑可删除 2可编辑不可删除 3 可删除不可编辑。
	@Column(name = "REPOSTA")
	private String repoSta;//纸质档案库房状态：0 未入库， 1在库，2，借出
	@Column(name = "MGRSTA")
	private String mgrSta; //档案或者案卷的后续处理状态
	
	// --------后续操作产生的信息：组卷（件）、上架
	@Column(name = "VOLSTA")
	private String volSta; //组卷状态：0：待组卷，1：已组卷
	@Column(name = "VOLID")
	private String volID;// 案卷（件）ID
	@Column(name = "VOLMOD")
	private String volMod;// 组卷模式
	@Column(name = "SEQNOINVOL")
	private Integer seqNOInVol;// 档案在卷内的顺序号
	@Column(name = "CELLID")
	private String cellID;// 档案库房位置，所在单元格，通过系统入库上架生成
	@Column(name = "REPOINFO")
	private String repoInfo;//档案库房位置，位置描述，用于各地市库房信息备注
	
	@Column(name = "DELETEFLAG")
	private String deleteFlag;//删除标志，删除档案时，实际不删除档案的信息，只是添加档案的标志位。

	@Column(name = "CELLNOID")
	private String cellnoID;// 档案库房位置，所在单元格的序号

	//---------------------- 非表列--------------
	@Transient
	private String gnlArvNam;// 全宗
	@Transient 
	private String volNam;//所属案卷名称
	@Transient 
	private String volNO;//所属案卷卷号
	@Transient 
	private String repoCellInfo;//库房位置描述
	@Transient 
	private String arvTypeNam;
	@Transient
	private String dtlID;//清点明细ID
	@Transient
	private Integer dtlSeqNO;//清点明细ID--seqno
	@Transient
	private Map<String, Object> perInfo;//人事信息，用于系统新增
	@Transient
	private String arcCompNam;//存档单位名称
	@Transient
	private String deliveryWay;//投递方式
	@Transient
	private String confidentNum;//机要号
	@Transient
	private String reason;//不合格原因
	
	public String getArcCompNam() {
		return arcCompNam;
	}
	public void setArcCompNam(String arcCompNam) {
		this.arcCompNam = arcCompNam;
	}
	public String getArcComp() {
		return arcComp;
	}
	public void setArcComp(String arcComp) {
		this.arcComp = arcComp;
	}
	public String getArcType() {
		return arcType;
	}
	public void setArcType(String arcType) {
		this.arcType = arcType;
	}
	public String getDtlID() {
		return dtlID;
	}
	public String getCellnoID() {
		return cellnoID;
	}
	public void setCellnoID(String cellnoID) {
		this.cellnoID = cellnoID;
	}
	public void setDtlID(String dtlID) {
		this.dtlID = dtlID;
	}
	public String getArvID() {
		return arvID;
	}
	public void setArvID(String arvID) {
		this.arvID = arvID;
	}
	/**
	 * 如果档案自编号不为空，则显示档案自编号
	 */
	public String getArvNO() {
		/*
		///不能这么写，如果这样写的话，修改时会把arvno重置为arvselfno 
		 if(StringUtils.isNotBlank(arvSelfNO)){
			return arvSelfNO;
		}*/
		return arvNO;
	}
	public void setArvNO(String arvNO) {
		this.arvNO = arvNO;
	}
	public String getArvNam() {
		return arvNam;
	}
	public void setArvNam(String arvNam) {
		this.arvNam = arvNam;
	}
	public String getSecLev() {
		return secLev;
	}
	public void setSecLev(String secLev) {
		this.secLev = secLev;
	}
	public String getObjID() {
		return objID;
	}
	public void setObjID(String objID) {
		this.objID = objID;
	}
	public String getObjNam() {
		return objNam;
	}
	public void setObjNam(String objNam) {
		this.objNam = objNam;
	}
	public String getGnlArvID() {
		return gnlArvID;
	}
	public void setGnlArvID(String gnlArvID) {
		this.gnlArvID = gnlArvID;
	}
	public String getArvTypeID() {
		return arvTypeID;
	}
	public void setArvTypeID(String arvTypeID) {
		this.arvTypeID = arvTypeID;
	}
	public Date getArvDte() {
		return arvDte;
	}
	public void setArvDte(Date arvDte) {
		this.arvDte = arvDte;
	}
	public String getArvCategory() {
		return arvCategory;
	}
	public void setArvCategory(String arvCategory) {
		this.arvCategory = arvCategory;
	}
	public String getArvDteYear() {
		return arvDteYear;
	}
	public void setArvDteYear(String arvDteYear) {
		this.arvDteYear = arvDteYear;
	}
	public String getKeyWds() {
		return keyWds;
	}
	public void setKeyWds(String keyWds) {
		this.keyWds = keyWds;
	}
	public String getKeepTerm() {
		return keepTerm;
	}
	public void setKeepTerm(String keepTerm) {
		this.keepTerm = keepTerm;
	}
	public String getRelPk() {
		return relPk;
	}
	public void setRelPk(String relPk) {
		this.relPk = relPk;
	}
	public String getMgrSta() {
		return mgrSta;
	}
	public void setMgrSta(String mgrSta) {
		this.mgrSta = mgrSta;
	}
	public String getArvSta() {
		return arvSta;
	}
	public void setArvSta(String arvSta) {
		this.arvSta = arvSta;
	}
	public String getDeliveryType() {
		return deliveryType;
	}
	public void setDeliveryType(String deliveryType) {
		this.deliveryType = deliveryType;
	}
	public String getIsCanEdit() {
		return isCanEdit;
	}
	public void setIsCanEdit(String isCanEdit) {
		this.isCanEdit = isCanEdit;
	}
	public Date getTs() {
		return ts;
	}
	public void setTs(Date ts) {
		this.ts = ts;
	}
	public Date getTsNewAdd() {
		return tsNewAdd;
	}
	public void setTsNewAdd(Date tsNewAdd) {
		this.tsNewAdd = tsNewAdd;
	}
	public Integer getPagNum() {
		return pagNum;
	}
	public void setPagNum(Integer pagNum) {
		this.pagNum = pagNum;
	}
	public String getVolID() {
		return volID;
	}
	public void setVolID(String volID) {
		this.volID = volID;
	}
	public Integer getSeqNOInVol() {
		return seqNOInVol;
	}
	public void setSeqNOInVol(Integer seqNOInVol) {
		this.seqNOInVol = seqNOInVol;
	}
	public String getCellID() {
		return cellID;
	}
	public void setCellID(String cellID) {
		this.cellID = cellID;
	}
	public String getRepoSta() {
		return repoSta;
	}
	public void setRepoSta(String repoSta) {
		this.repoSta = repoSta;
	}
	public String getVolMod() {
		return volMod;
	}
	public void setVolMod(String volMod) {
		this.volMod = volMod;
	}
	public String getArvTypeNam() {
		return arvTypeNam;
	}
	public void setArvTypeNam(String arvTypeNam) {
		this.arvTypeNam = arvTypeNam;
	}
	public String getRem() {
		return rem;
	}
	public void setRem(String rem) {
		this.rem = rem;
	}
	public Integer getDtlSeqNO() {
		return dtlSeqNO;
	}
	public void setDtlSeqNO(Integer dtlSeqNO) {
		this.dtlSeqNO = dtlSeqNO;
	}
	
	public String getVolNam() {
		return volNam;
	}
	public void setVolNam(String volNam) {
		this.volNam = volNam;
	}
	public String getVolNO() {
		return volNO;
	}
	public void setVolNO(String volNO) {
		this.volNO = volNO;
	}
	public String getRepoCellInfo() {
		return repoCellInfo;
	}
	public void setRepoCellInfo(String repoCellInfo) {
		this.repoCellInfo = repoCellInfo;
	}
	public String getBusinessID() {
		return businessID;
	}
	public void setBusinessID(String businessID) {
		this.businessID = businessID;
	}
	public String getCharger() {
		return charger;
	}
	public void setCharger(String charger) {
		this.charger = charger;
	}
	public String getDocNum() {
		return docNum;
	}
	public void setDocNum(String docNum) {
		this.docNum = docNum;
	}
	public String getPieceNum() {
		return pieceNum;
	}
	public void setPieceNum(String pieceNum) {
		this.pieceNum = pieceNum;
	}
	public Date getComeDate() {
		return comeDate;
	}
	public void setComeDate(Date comeDate) {
		this.comeDate = comeDate;
	}
	public Map<String, Object> getPerInfo() {
		return perInfo;
	}
	public void setPerInfo(Map<String, Object> perInfo) {
		this.perInfo = perInfo;
	}
	public String getRepoInfo() {
		return repoInfo;
	}
	public void setRepoInfo(String repoInfo) {
		this.repoInfo = repoInfo;
	}
	public String getResSta() {
		return resSta;
	}
	public void setResSta(String resSta) {
		this.resSta = resSta;
	}
	public String getDocSta() {
		return docSta;
	}
	public void setDocSta(String docSta) {
		this.docSta = docSta;
	}
	public String getComeOrg() {
		return comeOrg;
	}
	public void setComeOrg(String comeOrg) {
		this.comeOrg = comeOrg;
	}
	public String getIssue() {
		return issue;
	}
	public void setIssue(String issue) {
		this.issue = issue;
	}
	public String getOfficeType() {
		return officeType;
	}
	public void setOfficeType(String officeType) {
		this.officeType = officeType;
	}
	public String getVolSta() {
		return volSta;
	}
	public void setVolSta(String volSta) {
		this.volSta = volSta;
	}
	public String getDeleteFlag() {
		return deleteFlag;
	}
	public void setDeleteFlag(String deleteFlag) {
		this.deleteFlag = deleteFlag;
	}
	public String getRolloutDte() {
		return rolloutDte;
	}
	public void setRolloutDte(String rolloutDte) {
		this.rolloutDte = rolloutDte;
	}
	public String getArvSelfNO() {
		return arvSelfNO;
	}
	public void setArvSelfNO(String arvSelfNO) {
		this.arvSelfNO = arvSelfNO;
	}
	public String getDeliveryWay() {
		return deliveryWay;
	}
	public void setDeliveryWay(String deliveryWay) {
		this.deliveryWay = deliveryWay;
	}
	public String getConfidentNum() {
		return confidentNum;
	}
	public void setConfidentNum(String confidentNum) {
		this.confidentNum = confidentNum;
	}
	public String getGnlArvNam() {
		return gnlArvNam;
	}
	public void setGnlArvNam(String gnlArvNam) {
		this.gnlArvNam = gnlArvNam;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getDocInfoID() {
		return docInfoID;
	}
	public void setDocInfoID(String docInfoID) {
		this.docInfoID = docInfoID;
	}
	
}