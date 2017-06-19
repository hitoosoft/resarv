package com.hitoo.frame.common.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FtpProperty {

	@Value("#{propertiesReader[FTP_IP]}")
	private String ftpIp;

	@Value("#{propertiesReader[FTP_PORT]}")
	private Integer ftpPort;

	@Value("#{propertiesReader[FTP_USER]}")
	private String ftpUser;

	@Value("#{propertiesReader[FTP_PSW]}")
	private String ftpPsw;

	public String getFtpIp() {
		return ftpIp;
	}

	public void setFtpIp(String ftpIp) {
		this.ftpIp = ftpIp;
	}

	public Integer getFtpPort() {
		return ftpPort;
	}

	public void setFtpPort(Integer ftpPort) {
		this.ftpPort = ftpPort;
	}

	public String getFtpUser() {
		return ftpUser;
	}

	public void setFtpUser(String ftpUser) {
		this.ftpUser = ftpUser;
	}

	public String getFtpPsw() {
		return ftpPsw;
	}

	public void setFtpPsw(String ftpPsw) {
		this.ftpPsw = ftpPsw;
	}

}