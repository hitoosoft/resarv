package com.hitoo.frame.pub.easyuidatagrid.service;

import com.hitoo.frame.pub.model.PageInfo;
import com.hitoo.frame.pub.model.QueryModel;

public interface DatagridService {
	
	/**
	 * 根据条件分页查询
	 */
	public PageInfo queryDataGridWithPage(QueryModel qm, PageInfo pi) throws Exception;
}