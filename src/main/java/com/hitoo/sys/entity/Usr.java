package com.hitoo.sys.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.validator.constraints.NotBlank;

import com.hitoo.frame.common.validator.MaxUtf8Length;

/**
 * 用户表
 * @author qinchao
 */
@Entity
@Table(name = "SYS_USR")
@SuppressWarnings("serial")
public class Usr implements Serializable{
	@Id
	@Column(name = "USRID")
	@MaxUtf8Length(value=32, message="用户ID")
	private String usrID;//用户ID
	
	@Column(name = "USRCOD")
	@MaxUtf8Length(value=32, message="用户编码")
	@NotBlank
	private String usrCod;//用户编码
	
	@Column(name = "USRNAM")
	@MaxUtf8Length(value=100, message="用户名称")
	@NotBlank
	private String usrNam;//用户名称
	
	@Column(name = "ORGID")
	@NotBlank
	private String orgID;//所属机构
	
	//密码
	@Column(name = "USRPWD")
	private String usrPwd;
	
	//用户状态
	@Column(name = "USRSTA")
	@MaxUtf8Length(value=1, message="用户状态")
	private String usrSta;
	
	//人员类别
	@Column(name = "USRTYP")
	@MaxUtf8Length(value=3, message="用户类别")
	@NotBlank
	private String usrTyp;
	
	//身份证号
	@Column(name = "IDENTITYNO")
	@MaxUtf8Length(value=18, message="身份证号")
	private String identityNO;
	
	//性别
	@Column(name = "SEX")
	@MaxUtf8Length(value=1, message="性别")
	private String sex;
	
	//出生日期
	@Temporal(TemporalType.DATE)
	@Column(name = "BIRTHDTE")
	private Date birthDte;
	
	//电子邮件
	@Column(name = "EMAIL")
	@MaxUtf8Length(value=50, message="电子邮件")
	private String email;
	
	//联系电话
	@Column(name = "TELNO")
	@MaxUtf8Length(value=20, message="联系电话")
	private String telNO;
	
	//地址
	@Column(name = "ADDR")
	@MaxUtf8Length(value=100, message="地址")
	private String addr;
	
	//描述
	@Column(name = "DESCR")
	@MaxUtf8Length(value=1000, message="用户描述信息")
	private String descr;
	
	//用户启用数据权限，默认不启用（0=不启用代表拥有全部数据权限）
    @Column(name = "ISOWNALLDATAUT")
	private String isOwnAllDatAut;
	
	///------非数据库字段
	@Transient
	private String orgNam ;

	public String getUsrID() {
		return usrID;
	}

	public void setUsrID(String usrID) {
		this.usrID = usrID;
	}

	public String getUsrCod() {
		return usrCod;
	}

	public void setUsrCod(String usrCod) {
		this.usrCod = usrCod;
	}
	
	public String getUsrNam() {
		return usrNam;
	}

	public void setUsrNam(String usrNam) {
		this.usrNam = usrNam;
	}

	public String getOrgID() {
		return orgID;
	}

	public void setOrgID(String orgID) {
		this.orgID = orgID;
	}

	public String getUsrPwd() {
		return usrPwd;
	}

	public void setUsrPwd(String usrPwd) {
		this.usrPwd = usrPwd;
	}

	public String getIdentityNO() {
		return identityNO;
	}

	public void setIdentityNO(String identityNO) {
		this.identityNO = identityNO;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public Date getBirthDte() {
		return birthDte;
	}

	public void setBirthDte(Date birthDte) {
		this.birthDte = birthDte;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelNO() {
		return telNO;
	}

	public void setTelNO(String telNO) {
		this.telNO = telNO;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	public String getUsrSta() {
		return usrSta;
	}

	public void setUsrSta(String usrSta) {
		this.usrSta = usrSta;
	}

	public String getUsrTyp() {
		return usrTyp;
	}

	public void setUsrTyp(String usrTyp) {
		this.usrTyp = usrTyp;
	}

	public String getOrgNam() {
		return orgNam;
	}

	public void setOrgNam(String orgNam) {
		this.orgNam = orgNam;
	}

	public String getIsOwnAllDatAut() {
		return isOwnAllDatAut;
	}

	public void setIsOwnAllDatAut(String isOwnAllDatAut) {
		this.isOwnAllDatAut = isOwnAllDatAut;
	}

}