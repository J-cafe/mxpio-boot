package com.mxpioframework.dingtalk.enums;

public interface DingTalkEnums {
	
	enum MsgType{
		LINK("link", "链接消息"), TEXT("text", "文本消息"),IMAGE("image","图片消息"),FILE("file","文件消息");

		MsgType(String code, String name) {
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
