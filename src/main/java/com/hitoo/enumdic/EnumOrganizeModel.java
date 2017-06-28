package com.hitoo.enumdic;

////-------------------------------------档案组建模式
public enum EnumOrganizeModel{
	arv("0","单份档案"), 
	vol("1","卷"), 
	piece("2","件");

	// 编码 及名字
	private final String code;
	private final String label;

	/**
	 * 构造函数
	 * 
	 * @param code
	 */
	EnumOrganizeModel(String code ,String label) {
		this.code = code;
		this.label = label ;
	}

	public String getLabel() {
		return label;
	}
	
	public String getCode() {
		return code;
	}
}
