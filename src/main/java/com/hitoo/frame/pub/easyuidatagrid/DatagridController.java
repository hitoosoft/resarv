package com.hitoo.frame.pub.easyuidatagrid;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hitoo.frame.base.BaseController;
import com.hitoo.frame.pub.easyuidatagrid.service.DatagridService;
import com.hitoo.frame.pub.model.PageInfo;
import com.hitoo.frame.pub.model.QueryModel;

@Controller
public class DatagridController extends BaseController {
	@Autowired
	private DatagridService dgService;

	/**
	 * 封装的dataGrid的通用查询 ，按前台组织的条件查询
	 * 
	 * @author 李徐承
	 * @date 创建时间 2013-9-14
	 */
	@RequestMapping("/queryDatagridInfoWithPage")
	@ResponseBody
	public PageInfo queryDatagridInfoWithPage(@RequestBody QueryModel qm,
			HttpServletRequest request) throws Exception {
		PageInfo pi = super.getPageInfo(request);
		return dgService.queryDataGridWithPage(qm, pi);
	}

	/**
	 * 封装的dataGrid的通用初始化Datagrid ，查询所有数据 ，可以有排序字段等
	 * 
	 * @author 李徐承
	 * @date 创建时间 2013-9-14
	 */
	@RequestMapping("/initDatagridInfoWithPage")
	@ResponseBody
	public PageInfo initDatagridInfoWithPage(QueryModel qm, HttpServletRequest request) throws Exception {
		qm.setQueryType(request.getParameter("queryType"));
		PageInfo pi = super.getPageInfo(request);

		return dgService.queryDataGridWithPage(qm, pi);
	}

	/**
	 * dataGrid的查询，返回查询对象，放到map中
	 * 
	 * @author qinchao
	 * @date 创建时间 2013-9-14
	 */
	@RequestMapping("/queryDatagridMapDataWithPage")
	@ResponseBody
	public Map<String, Object> queryDatagridMapDataWithPage(@RequestBody QueryModel qm, 
			HttpServletRequest request) throws Exception {
		PageInfo pi = super.getPageInfo(request);
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("dataGridObj", dgService.queryDataGridWithPage(qm, pi));
		return writeSuccMsg("", paramMap);
	}

}