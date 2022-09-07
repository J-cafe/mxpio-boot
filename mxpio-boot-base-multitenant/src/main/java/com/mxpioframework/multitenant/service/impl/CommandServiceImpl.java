package com.mxpioframework.multitenant.service.impl;

import com.mxpioframework.multitenant.command.Command;
import com.mxpioframework.multitenant.command.CommandNeedReturn;
import com.mxpioframework.multitenant.service.CommandService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CommandServiceImpl implements CommandService {
	
	@Override
	public <T> T executeQueryCommand(CommandNeedReturn<T> command) {
		return command.execute();
	}
	
	@Override
	public void executeQueryCommand(Command command) {
		command.execute();
	}
	
	@Override
	@Transactional
	public <T> T executeNonQueryCommand(CommandNeedReturn<T> command) {
		return command.execute();
	}
	
	@Override
	@Transactional
	public void executeNonQueryCommand(Command command) {
		command.execute();
	}

}
