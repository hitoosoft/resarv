package com.hitoo.sys.param;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.hitoo.frame.base.BusinessException;
import com.hitoo.sys.param.service.ParamService;

/**
 * 检查系统版本与数据库版本是否一致，启动时加载
 * @author Administrator
 *
 */
@Component
public class CheckVersionLoader {

	//只有明确指定不校验时才不校验版本与数据库版本，否则校验
	@Value("#{propertiesReader[CHECK_VERSION_FLAG]}")
	private String checkVersionFlag;
	
	@Value("#{propertiesReader[APP_VERSION]}")
	private String appVersion;
	
	
	@Autowired
	private ParamService paramService;
	
	/**
	 * 执行加载
	 */
	@PostConstruct
	public void load() throws Exception{
		if("0".equals(checkVersionFlag)){
			return ;
		}
		String dbVersion = paramService.queryValByCod("DBVERSION");
		if(!dbVersion.equals(appVersion)){
			throw new BusinessException("数据库版本与程序版本不一致：数据版本["+dbVersion+"],程序版本["+appVersion+"]");
		}
	}
	
}
