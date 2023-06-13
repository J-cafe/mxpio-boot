package com.mxpioframework.camunda.enums;

public interface BpmnEnums {
	
	/**
	 * 流程定义发布状态
	 * @author mxpio
	 *
	 */
	enum DeployStatusEnums{
		NEW("01", "新增"), DEPLOY("02", "已发布"),UPDATE("03","更新");

		DeployStatusEnums(String code, String name) {
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
	
	/**
	 * 流程操作
	 * @author mxpio
	 *
	 */
	enum Purc{
		OK("01", "同意"), REFUSE("02", "拒绝"),REJECT("03","驳回");

		Purc(String code, String name) {
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
