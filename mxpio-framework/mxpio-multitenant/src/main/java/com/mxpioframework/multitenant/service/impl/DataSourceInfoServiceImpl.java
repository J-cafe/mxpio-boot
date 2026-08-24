package com.mxpioframework.multitenant.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.mxpioframework.jpa.JpaUtil;
import com.mxpioframework.multitenant.domain.DataSourceInfo;
import com.mxpioframework.multitenant.domain.Organization;
import com.mxpioframework.multitenant.service.DataSourceInfoService;

@Service("mxpio.multitenant.dataSourceInfoService")
public class DataSourceInfoServiceImpl implements DataSourceInfoService {

	@Override
	@Transactional(readOnly = true)
	public DataSourceInfo get(Organization organization) {
		// 注意：不使用 addIf/addIfNot DSL —— 其对 String 的判定语义与 Javadoc
		// 相反（LinImpl 中 addIf(String) 在空串时生效），这里用普通分支保证语义明确。
		// 另外 doQuery 的 REQUIRES_NEW 事务中看不到调用方未提交的 Organization 行，
		// 因此已分配数据源的租户必须按 dataSourceInfoId 直查（该行早已提交）。
		if (StringUtils.isNotEmpty(organization.getDataSourceInfoId())) {
			return JpaUtil.linq(DataSourceInfo.class)
					.idEqual(organization.getDataSourceInfoId())
					.findOne();
		}
		return JpaUtil.linq(DataSourceInfo.class)
				.exists(Organization.class)
					.equalProperty("dataSourceInfoId", "id")
					.idEqual(organization.getId())
				.end()
				.findOne();
	}

	@Override
	@Transactional(readOnly = true)
	public DataSourceInfo allocate(Organization organization) {
		if (StringUtils.isEmpty(organization.getDataSourceInfoId())) {
			// 选择消耗指数最小（depletionIndex 无更小者）且可用可共享的数据源
			List<DataSourceInfo> list = JpaUtil.linq(DataSourceInfo.class)
					.isTrue("enabled")
					.isTrue("shared")
					.notExists(DataSourceInfo.class)
						.isTrue("enabled")
						.isTrue("shared")
						.lt("depletionIndex", "depletionIndex")
					.end()
					.list(0, 1);
			Assert.notEmpty(list,"DataSourceInfo must contain elements");
			return list.get(0);
		} else {
			return JpaUtil.linq(DataSourceInfo.class)
					.isTrue("enabled")
					.isTrue("shared")
					.idEqual(organization.getDataSourceInfoId())
					.findOne();
		}

	}

}
