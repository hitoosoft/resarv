package com.hitoo.sys.role;

import java.util.EnumSet;

/**
 * 功能权限表
 */
public enum RoletypeFuncDefine{
	
	FUNCAUT("FUNC","功能权限"),
	DATAUT("DATAUT","档案数据权限");

	// 编码 及名字
	private final String code;
	private final String label;

	/**
	 * 构造函数
	 * 
	 * @param code
	 */
	RoletypeFuncDefine(String code ,String label) {
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
		for(RoletypeFuncDefine s : EnumSet.allOf(RoletypeFuncDefine.class)){
	         if(s.code.equals(code))
	        	 return s.label;
	    }
		return null;
	}
}
