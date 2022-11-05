package com.mxpioframework.excel.swfviewer.handler;

import java.io.File;
import java.util.Map;

public interface ISwfFileHandler {

	public String getHandlerName();

	public String getHandlerDesc();

	public File execute(Map<String, String> parameter) throws Exception;
}
