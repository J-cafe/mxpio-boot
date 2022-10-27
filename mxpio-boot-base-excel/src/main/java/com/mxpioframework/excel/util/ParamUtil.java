package com.mxpioframework.excel.util;

import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;

import com.mxpioframework.jpa.JpaUtil;
import com.mxpioframework.jpa.query.Criteria;
import com.mxpioframework.jpa.query.CriteriaUtils;
import com.mxpioframework.jpa.query.Operator;
import com.mxpioframework.security.decision.manager.SecurityDecisionManager;
import com.mxpioframework.security.entity.DataResource;
import com.mxpioframework.security.service.DataResourceService;
import com.mxpioframework.security.service.DeptService;
import com.mxpioframework.security.util.ApplicationContextProvider;
import com.mxpioframework.security.util.SecurityUtils;

public class ParamUtil {

	public static Object getParamValue(Parameter methodParam, String[] values, String path){
		Object value = null;
		if("criteria".equals(methodParam.getName())){
			boolean decide = false;
			Criteria c = CriteriaUtils.json2Criteria(values[0]);
			DataResourceService dataResourceService = ApplicationContextProvider.getBean(DataResourceService.class);
			SecurityDecisionManager securityDecisionManager = ApplicationContextProvider.getBean(SecurityDecisionManager.class);
			List<DataResource> datas = dataResourceService.findAll();
			Map<String, List<DataResource>> dataResourceMap = JpaUtil.classify(datas, "path");
			List<DataResource> dataResources = dataResourceMap.get(path);
			if(CollectionUtils.isEmpty(dataResources)){
				dataResources = new ArrayList<>();
				decide = true;
			}
			for(DataResource dataResource : dataResources){
				if (securityDecisionManager.decide(dataResource)) {
					if(dataResource.getDataScope() != null){
						if(com.mxpioframework.security.Constants.DatascopeEnum.DEPT.getCode().equals(dataResource.getDataScope())){
							DeptService deptService = ApplicationContextProvider.getBean(DeptService.class);
							Set<String> deptCodes = deptService.getDeptKeysByUser(SecurityUtils.getLoginUsername(),"code");
							c.addCriterion("createDept", Operator.IN, deptCodes);
						}else if(com.mxpioframework.security.Constants.DatascopeEnum.USER.getCode().equals(dataResource.getDataScope())) {
							c.addCriterion("createBy", Operator.EQ, SecurityUtils.getLoginUsername());
						}
					}
					decide = true;
					break;
				}
			}
			if(!decide){
				c.addCriterion("createTime", Operator.EQ, new Date());
			}
			value = c;
		}else if(Integer.class.equals(methodParam.getType())){
			value = Integer.parseInt(values[0]);
		}else{
			value = values[0];
		}
		return value;
	}
}
