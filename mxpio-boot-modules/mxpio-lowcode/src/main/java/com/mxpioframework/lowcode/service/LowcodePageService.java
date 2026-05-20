package com.mxpioframework.lowcode.service;

import java.util.List;

import com.mxpioframework.lowcode.entity.LowcodePage;

public interface LowcodePageService {

	LowcodePage getByCode(String pageCode);

	List<LowcodePage> listAll();

	LowcodePage save(LowcodePage page);

	LowcodePage update(String pageCode, LowcodePage page);

	void delete(String pageCode);

	void publish(String pageCode);
}
