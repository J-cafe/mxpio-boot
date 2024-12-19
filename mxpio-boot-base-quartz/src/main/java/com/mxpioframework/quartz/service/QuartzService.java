package com.mxpioframework.quartz.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mxpioframework.jpa.query.Criteria;
import com.mxpioframework.quartz.entity.QuartzJob;

public interface QuartzService {
	
	/**
	 * 分页查询任务列表
	 * @param page 分页对象
	 * @param c 查询构造器
	 * @return 调度任务清单
	 */
	Page<QuartzJob> page(Pageable page, Criteria c);
	
	/**
	 * 查询任务列表
	 * @param c 查询构造器
	 * @return 调度任务清单
	 */
	List<QuartzJob> list(Criteria c);

	/**
	 * 新增任务
	 * @param quartzJob 调度任务
	 * @return 新增结果
	 */
	boolean addJob(QuartzJob quartzJob);

	/**
	 * 编辑任务
	 * @param quartzJob 调度任务
	 * @return 编辑结果
	 */
	boolean editJob(QuartzJob quartzJob);

	/**
	 * 删除任务
	 * @param jobId 调度ID
	 * @return 删除结果
	 */
	boolean deleteJob(String jobId);

	/**
	 * 任务复位
	 * @param jobId 调度ID
	 * @return 复位结果
	 */
	boolean resume(String jobId);

	/**
	 * 执行一次
	 * @param quartzJob 任务调度
	 * @return 执行结果
	 */
	boolean execute(QuartzJob quartzJob);

	/**
	 * 暂停任务
	 * @param jobId 调度ID
	 * @return 暂停结果
	 */
	boolean pause(String jobId);
	
	/**
	 * 添加任务调度
	 * @param jobClassName 任务类名
	 * @param cron 定时器
	 * @param params 参数
	 * @return 添加结果
	 */
	boolean addScheduler(String jobClassName, String cron, String params);
	
	/**
	 * 删除调度
	 * @param jobClassName 任务类名
	 * @return 删除结果
	 */
	boolean removeScheduler(String jobClassName);

    QuartzJob getById(String id);
}
