package com.mxpioframework.security.enums;

public interface SecurityEnums {
	
	/**
	 * 部门级别
	 * @author mxpio
	 *
	 */
	enum DeptLevel{
		ONE("01", "一级"), TWO("02", "二级"),THREE("03","三级");//MB_SYSTEM_DEPT_LEVEL

		DeptLevel(String code, String name) {
			this.code = code;
			this.name = name;
		}

		private String code;

		private String name;

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}
	

}
