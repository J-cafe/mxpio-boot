package com.mxpioframework.quartz.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mxpioframework.jpa.query.Criteria;
import com.mxpioframework.quartz.entity.QuartzJob;

public interface QuartzService {
	
	/**
	 * 分页查询任务列表
	 * @param page
	 * @param c
	 * @return
	 */
	Page<QuartzJob> page(Pageable page, Criteria c);
	
	/**
	 * 查询任务列表
	 * @param c
	 * @return
	 */
	List<QuartzJob> list(Criteria c);

	/**
	 * 新增任务
	 * @param quartzJob
	 * @return
	 */
	boolean addJob(QuartzJob quartzJob);

	/**
	 * 编辑任务
	 * @param quartzJob
	 * @return
	 */
	boolean editJob(QuartzJob quartzJob);

	/**
	 * 删除任务
	 * @param jobId
	 * @return
	 */
	boolean deleteJob(String jobId);

	/**
	 * 任务复位
	 * @param quartzJob
	 * @return
	 */
	boolean resumeJob(QuartzJob quartzJob);

	/**
	 * 执行一次
	 * @param quartzJob
	 * @return
	 */
	boolean execute(QuartzJob quartzJob);

	/**
	 * 暂停任务
	 * @param quartzJob
	 * @return
	 */
	boolean pause(QuartzJob quartzJob);
	
	/**
	 * 添加任务调度
	 * @param jobClassName
	 * @param cron
	 * @param params
	 * @return
	 */
	boolean addScheduler(String jobClassName, String cron, String params);
	
	/**
	 * 删除调度
	 * @param jobClassName
	 * @return
	 */
	boolean removeScheduler(String jobClassName);
}
