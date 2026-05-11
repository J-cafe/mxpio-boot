package com.mxpioframework.multitenant.service;


import com.mxpioframework.multitenant.command.Command;
import com.mxpioframework.multitenant.command.CommandNeedReturn;

public interface CommandService {

	<T> T executeQueryCommand(CommandNeedReturn<T> command);

	void executeQueryCommand(Command command);

	<T> T executeNonQueryCommand(CommandNeedReturn<T> command);

	void executeNonQueryCommand(Command command);


}
