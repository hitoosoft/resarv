package com.hitoo.frame.pub.easyuidatagrid.service.impl;

import org.springframework.stereotype.Service;

import com.hitoo.frame.base.BaseService;
import com.hitoo.frame.pub.easyuidatagrid.service.DatagridService;
import com.hitoo.frame.pub.model.PageInfo;
import com.hitoo.frame.pub.model.QueryModel;

@Service
public class DatagridServiceImpl extends BaseService implements DatagridService {

	@Override
	public PageInfo queryDataGridWithPage(QueryModel qm, PageInfo pi) throws Exception {
//		return commonDao.querySingleEntity(qm, pi);
		return null;
	}
}
