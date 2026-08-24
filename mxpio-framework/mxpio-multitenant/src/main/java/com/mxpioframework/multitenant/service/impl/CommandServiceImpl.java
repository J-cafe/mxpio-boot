package com.mxpioframework.multitenant.service.impl;

import com.mxpioframework.multitenant.command.Command;
import com.mxpioframework.multitenant.command.CommandNeedReturn;
import com.mxpioframework.multitenant.service.CommandService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 跨租户命令执行器。每个命令都是独立事务（REQUIRES_NEW）：
 * 挂起调用方事务后，MultitenantJpaTransactionManager 会重新按当前租户上下文
 * 解析目标 EMF/DataSource，保证租户操作落在正确的数据库上。
 * <p>
 * 注意：跨租户操作与调用方事务<b>非原子</b>（内层先提交，外层回滚不会回滚
 * 租户库操作），需要补偿/最终一致的场景由调用方自行设计。
 */
@Service("mxpio.multitenant.commandService")
@Transactional(readOnly = true)
public class CommandServiceImpl implements CommandService {

	@Override
	@Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
	public <T> T executeQueryCommand(CommandNeedReturn<T> command) {
		return command.execute();
	}

	@Override
	@Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
	public void executeQueryCommand(Command command) {
		command.execute();
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public <T> T executeNonQueryCommand(CommandNeedReturn<T> command) {
		return command.execute();
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void executeNonQueryCommand(Command command) {
		command.execute();
	}

}
