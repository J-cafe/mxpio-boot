package com.mxpioframework.common;

import java.util.ArrayList;
import java.util.List;

import com.mxpioframework.common.vo.ModuleVO;

public class CommonConstant {

	// http code
	public static final Integer HTTP_OK = 200;
	public static final Integer HTTP_SERVER_ERROR = 500;
	public static final Integer HTTP_NO_AUTHZ_401 = 401;
	public static final Integer HTTP_NO_AUTHZ_403 = 403;
	public static final Integer HTTP_NO_AUTHZ_40101 = 40101;
	
	public static final String DICT_TEXT_SUFFIX = "$DICT_TEXT_";
	
	private static final List<ModuleVO> modules = new ArrayList<>();

	public static List<ModuleVO> getModules() {
		return modules;
	}

	public static boolean isIncludeModule(String moduleName) {
		for (ModuleVO module : modules) {
			if (module.getModuleName().equals(moduleName)) {
				return true;
			}
		}
		return false;
	}

	public static void addModule(ModuleVO module) {
		getModules().add(module);
	}

}
