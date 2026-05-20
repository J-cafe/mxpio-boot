package com.mxpioframework.lowcode.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.mxpioframework.jpa.JpaUtil;
import com.mxpioframework.lowcode.entity.LowcodePage;
import com.mxpioframework.lowcode.service.LowcodePageService;

@Service("mxpio.lowcode.pageService")
@Transactional
public class LowcodePageServiceImpl implements LowcodePageService {

	@Override
	@Transactional(readOnly = true)
	public LowcodePage getByCode(String pageCode) {
		return JpaUtil.linq(LowcodePage.class)
				.equal("pageCode", pageCode)
				.findOne();
	}

	@Override
	@Transactional(readOnly = true)
	public List<LowcodePage> listAll() {
		return JpaUtil.linq(LowcodePage.class).list();
	}

	@Override
	public LowcodePage save(LowcodePage page) {
		Assert.hasText(page.getPageCode(), "页面编码不能为空");
		JpaUtil.persistAndFlush(page);
		return page;
	}

	@Override
	public LowcodePage update(String pageCode, LowcodePage updated) {
		LowcodePage existing = getByCode(pageCode);
		Assert.notNull(existing, "页面不存在: " + pageCode);

		existing.setPageName(updated.getPageName());
		existing.setModelCode(updated.getModelCode());
		existing.setPageType(updated.getPageType());
		existing.setWidgetConfig(updated.getWidgetConfig());
		existing.setDataSetConfig(updated.getDataSetConfig());
		existing.setButtonConfig(updated.getButtonConfig());
		existing.setEventConfig(updated.getEventConfig());

		JpaUtil.persistAndFlush(existing);
		return existing;
	}

	@Override
	public void delete(String pageCode) {
		LowcodePage page = getByCode(pageCode);
		if (page != null) {
			JpaUtil.delete(page);
		}
	}

	@Override
	public void publish(String pageCode) {
		LowcodePage page = getByCode(pageCode);
		Assert.notNull(page, "页面不存在: " + pageCode);
		page.setVersion(page.getVersion() != null ? page.getVersion() + 1 : 1);
		JpaUtil.persistAndFlush(page);
	}
}
