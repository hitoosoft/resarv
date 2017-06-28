package com.hitoo.enumdic;

import java.util.EnumSet;

/**
 * 是否上架
 */
public enum EnumRepoSta{
 
	UNREPO("0", "未入库"),
	INREPO("1", "已入库"),
	OUTREPO("2", "已借出");

	// 编码 及名字
	private final String code;
	private final String label;

	/**
	 * 构造函数
	 * 
	 * @param code
	 */
	EnumRepoSta(String code ,String label) {
		this.code = code;
		this.label = label ;
	}

	public String getLabel() {
		return label;
	}
	
	public String getCode() {
		return code;
	}
	
	public static String getLabelByCode(String code) {
		for(EnumRepoSta s : EnumSet.allOf(EnumRepoSta.class)){
	         if(s.code.equals(code))
	        	 return s.label;
	    }
		return null;
	}
}
