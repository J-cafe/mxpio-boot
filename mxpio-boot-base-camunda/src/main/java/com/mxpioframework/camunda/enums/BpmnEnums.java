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
	enum OperationStatusEnums{
		START("Start", "启动"),
		APPROVE("Approve", "同意"),
		REJECT("Reject","驳回上一节点"),
		REFUSE("Refuse", "不同意"),
		DELEGATE("Delegate", "委托"),
		CANCEL("Cancel", "撤回");

		OperationStatusEnums(String code, String name) {
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
