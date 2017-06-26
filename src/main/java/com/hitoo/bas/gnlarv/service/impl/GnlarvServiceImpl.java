package com.hitoo.bas.gnlarv.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hitoo.bas.gnlarv.dao.GnlarvDao;
import com.hitoo.bas.gnlarv.service.GnlarvService;
import com.hitoo.bas.entity.Gnlarv;
import com.hitoo.frame.base.BaseService;
import com.hitoo.frame.base.BusinessException;
import com.hitoo.frame.common.util.TreeUtil;
import com.hitoo.frame.pub.global.LoginInfo;
import com.hitoo.frame.pub.model.TreeModel;

@Service
public class GnlarvServiceImpl extends BaseService implements GnlarvService{
	
	@Autowired
	private GnlarvDao gnlarvDao;
	
	/*
	 * 加载全宗树,暂时不加权限控制
	 */
	@Override
	public List<TreeModel> queryGnlArvTree(LoginInfo loginUser ,String gnlarvID) throws Exception {
		List<Gnlarv> ls =null;
        ls = gnlarvDao.queryAllGnlarv();
		return TreeUtil.setTree(ls);
	}
	
	/*
	 * 新增全宗
	 */
	@Override
	public void addGnlArv(Gnlarv gnlarv) throws Exception {
		String gnlArvCod,gnlArvNam;
		gnlArvCod = gnlarv.getGnlArvCod();
		gnlArvNam = gnlarv.getGnlArvNam();
		Integer seqNO = gnlarv.getSeqNO();
		//验证全宗编码是否重复
		if(commonDao.findEntityList(Gnlarv.class, "gnlArvCod", gnlArvCod).size()!=0){
			throw new BusinessException("全宗编码已存在，请重新输入！");
		}
		//验证全宗名称是否重复
		if(commonDao.findEntityList(Gnlarv.class, "gnlArvNam", gnlArvNam).size()!=0){
			throw new BusinessException("全宗名称已存在，请重新输入！");
		}
		//一个立档单位只能归属一个全宗
		if(gnlarvDao.queryGnlarvByOrg(gnlarv.getOrgID(),"")!=null){
			throw new BusinessException("请选择其他机构作为立档单位，当前机构已有全宗！");
		}
		if(seqNO==null){
			seqNO = commonDao.findEntityList(Gnlarv.class).size()+1;
		}
		gnlarv.setGnlArvID(dBUtil.getCommonId());
		gnlarv.setSeqNO(seqNO);
		commonDao.saveEntity(gnlarv);
	}
	
	/*
	 * 根据全宗ID查询全宗信息
	 */
	@Override
	public Gnlarv queryGnlarvByID(String gnlarvID) throws Exception {
		return gnlarvDao.queryGnlarvByID(gnlarvID);
	}
	
	/*
	 * 修改全宗
	 */
	@Override
	public void modifyGnlarv(Gnlarv gnlarv) throws Exception {
		String gnlarvID = gnlarv.getGnlArvID();
		String gnlArvCod = gnlarv.getGnlArvCod();
		String gnlArvNam = gnlarv.getGnlArvNam();
		
		//验证全宗编码是否重复
		if(gnlarvDao.queryGnlarvByCod(gnlArvCod, gnlarvID).size()!=0){
			throw new BusinessException("全宗编码已存在，请重新输入！");
		}
		//验证全宗名称是否重复
		if(gnlarvDao.queryGnlarvByNam(gnlArvNam, gnlarvID).size()!=0){
			throw new BusinessException("全宗名称已存在，请重新输入！");
		}
		//一个立档单位只能归属一个全宗
		if(gnlarvDao.queryGnlarvByOrg(gnlarv.getOrgID(),gnlarvID)!=null){
			throw new BusinessException("请选择其他机构作为立档单位，当前机构已有全宗！");
		}
		commonDao.updateEntity(gnlarv);
	}
	
	/*
	 * 删除全宗
	 */
	@Override
	public void deleteGnlArv(String gnlarvID) throws Exception {
		commonDao.delEntityById(Gnlarv.class, gnlarvID);
	}

	@Override
	public JSONArray queryGnlArv() throws Exception {
		JSONArray jsonArray = new JSONArray();
		List<Gnlarv> gnlarvs = commonDao.findEntityList(Gnlarv.class);
		for(int i = 0; i < gnlarvs.size(); i++){
			JSONObject object = new JSONObject();
			object.put("id", gnlarvs.get(i).getGnlArvID());
			object.put("text", gnlarvs.get(i).getGnlArvNam());
			jsonArray.add(object);
		}
		return jsonArray;
	}
	
}
