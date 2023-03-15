package com.mxpioframework.dbconsole.manager.impl;

import java.rmi.dgc.VMID;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.mxpioframework.dbconsole.manager.IConsoleDbInfoManager;
import com.mxpioframework.dbconsole.model.DbInfo;
import com.mxpioframework.security.util.SecurityUtils;

@Component(IConsoleDbInfoManager.BEAN_ID)
public class DefaultConsoleDbInfoManager implements IConsoleDbInfoManager, InitializingBean, DisposableBean {
	private Vector<DbInfo> listDbInfo = new Vector<DbInfo>();

	@Override
	public List<DbInfo> findDbInfosByUser(String username) throws Exception {
		List<DbInfo> userDbInfo = new ArrayList<DbInfo>();
		if (StringUtils.hasText(username)) {
			for (DbInfo dbInfo : listDbInfo) {
				String user = dbInfo.getCreateUser();
				if (user != null) {
					if (user.equals(username)) {
						userDbInfo.add(dbInfo);
					}
				}

			}
		}
		return userDbInfo;
	}

	@Override
	public DbInfo findDbInfosById(String id) throws Exception {
		if (StringUtils.hasText(id)) {
			for (DbInfo dbInfo : listDbInfo) {
				if (dbInfo.getId().equals(id)) {
					return dbInfo;
				}
			}
		}
		return null;
	}

	@Override
	public void insertDbInfo(DbInfo dbInfo) throws Exception {
		String username = SecurityUtils.getLoginUsername();
		if (!StringUtils.hasText(dbInfo.getId())) {
			dbInfo.setId(new VMID().toString());
		}
		dbInfo.setCreateDate(new Date());
		dbInfo.setCreateUser(username);
		listDbInfo.add(dbInfo);
	}

	@Override
	public void updateDbInfo(DbInfo dbInfo) throws Exception {
		this.deleteDbInfoById(dbInfo.getId());
		String username = SecurityUtils.getLoginUsername();
		dbInfo.setCreateUser(username);
		listDbInfo.add(dbInfo);
	}

	@Override
	public void deleteDbInfoById(String id) throws Exception {
		if (StringUtils.hasText(id)) {
			DbInfo info = null;
			for (DbInfo dbInfo : listDbInfo) {
				if (dbInfo.getId().equals(id)) {
					info = dbInfo;
					break;
				}
			}
			if (info != null) {
				listDbInfo.remove(info);
			}
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		listDbInfo = DbInfoConfig.readConfig();
	}

	@Override
	public void destroy() {
		System.out.println("--------------------------------------------");
		DbInfoConfig.writeConfig(listDbInfo);
	}
}
