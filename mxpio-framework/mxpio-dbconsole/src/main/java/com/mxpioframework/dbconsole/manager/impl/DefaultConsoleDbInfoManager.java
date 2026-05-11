package com.mxpioframework.dbconsole.manager.impl;

import java.util.List;
import java.util.Vector;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.mxpioframework.dbconsole.entity.DbInfo;
import com.mxpioframework.dbconsole.manager.IConsoleDbInfoManager;
import com.mxpioframework.jpa.JpaUtil;

@Component(IConsoleDbInfoManager.BEAN_ID)
public class DefaultConsoleDbInfoManager implements IConsoleDbInfoManager, InitializingBean, DisposableBean {
	private Vector<DbInfo> listDbInfo = new Vector<>();

	@Override
	@Transactional(readOnly = true)
	public List<DbInfo> findDbInfos() {
		
		return JpaUtil.linq(DbInfo.class).list();
	}

	@Override
	@Transactional(readOnly = true)
	public DbInfo findDbInfosById(String id) {
		return JpaUtil.linq(DbInfo.class).idEqual(id).findOne();
	}

	@Override
	@Transactional
	public void insertDbInfo(DbInfo dbInfo) {
		JpaUtil.save(dbInfo);
	}

	@Override
	@Transactional
	public void updateDbInfo(DbInfo dbInfo) {
		JpaUtil.update(dbInfo);
	}

	@Override
	@Transactional
	public void deleteDbInfoById(String id) {
		JpaUtil.lind(DbInfo.class).idEqual(id).delete();
	}

	@Override
	public void afterPropertiesSet() {
		listDbInfo = DbInfoConfig.readConfig();
	}

	@Override
	public void destroy() {
		DbInfoConfig.writeConfig(listDbInfo);
	}
}
