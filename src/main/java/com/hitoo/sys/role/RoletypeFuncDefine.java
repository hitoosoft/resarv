package com.hitoo.sys.role;

import java.util.EnumSet;

/**
 * 功能权限表
 */
public enum RoletypeFuncDefine{
	
	FUNCAUT("FUNC","功能权限","SYS_ROLE_FUNC"),
	DATAUT_ARVTYPE("ARVTYPE","档案分类权限","SYS_ROLE_ARVTYPE"),
	DATAUT("DATAUT","档案数据权限","BAS_ROLE_GNL_DATAUT");

	// 编码 及名字
	private final String code;
	private final String label;
	private final String relativeTable;

	/**
	 * 构造函数
	 * 
	 * @param code
	 */
	RoletypeFuncDefine(String code ,String label,String relativeTable) {
		this.code = code;
		this.label = label ;
		this.relativeTable = relativeTable;
	}

	public String getLabel() {
		return label;
	}
	
	public String getCode() {
		return code;
	}
	
	public String getRelativeTable() {
		return relativeTable;
	}
	
	
	public static String getLabelByCode(String code) {
		for(RoletypeFuncDefine s : EnumSet.allOf(RoletypeFuncDefine.class)){
	         if(s.code.equals(code))
	        	 return s.label;
	    }
		return null;
	}
}
