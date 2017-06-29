package com.hitoo.frame.pub.global;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 登录用户信息类
 */
@SuppressWarnings("serial")
public class LoginInfo implements Serializable {
	/**
	 * 缓存后台登录用户对象的属性名称
	 */
	public static final String LOGIN_USER = "login_user";
	
	private String usrId;
	private String usrCod;
	private String usrNam;
	private String password;
	private String email;
	private String ip;
	private String logonTime;
	private String orgId;
	private String orgCod;
	private String orgNam;
	private String userType;
	private boolean isSuperAdmin;
	private boolean isOwnAllDatAut;
	
	private List<String> roleIDList;//用户的角色列表
	private Set<String> urlList;//用户的功能权限url列表
	private Map<String, Set<String>> btnMap ;//按钮权限
	
	public String getUsrId() {
		return usrId;
	}
	public void setUsrId(String usrId) {
		this.usrId = usrId;
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
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getLogonTime() {
		return logonTime;
	}
	public void setLogonTime(String logonTime) {
		this.logonTime = logonTime;
	}
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public String getOrgCod() {
		return orgCod;
	}
	public void setOrgCod(String orgCod) {
		this.orgCod = orgCod;
	}
	public String getOrgNam() {
		return orgNam;
	}
	public void setOrgNam(String orgNam) {
		this.orgNam = orgNam;
	}
	
	public boolean isOwnAllDatAut() {
		if(this.isSuperAdmin()){
			return true;
		}
		return isOwnAllDatAut;
	}

	public void setOwnAllDatAut(boolean isOwnAllDatAut) {
		this.isOwnAllDatAut = isOwnAllDatAut;
	}

	public List<String> getRoleIDList() {
		return roleIDList;
	}
	public void setRoleIDList(List<String> roleIDList) {
		this.roleIDList = roleIDList;
	}
	public Set<String> getUrlList() {
		return urlList;
	}
	public void setUrlList(Set<String> urlList) {
		this.urlList = urlList;
	}

	public Map<String, Set<String>> getBtnMap() {
		return btnMap;
	}

	public void setBtnMap(Map<String, Set<String>> btnMap) {
		this.btnMap = btnMap;
	}

	public boolean isSuperAdmin() {
		return isSuperAdmin;
	}

	public void setSuperAdmin(boolean isSuperAdmin) {
		this.isSuperAdmin = isSuperAdmin;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}

	
	
}