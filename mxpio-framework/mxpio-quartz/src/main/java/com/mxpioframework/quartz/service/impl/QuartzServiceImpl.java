package com.mxpioframework.quartz.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mxpioframework.jpa.JpaUtil;
import com.mxpioframework.jpa.query.Criteria;
import com.mxpioframework.quartz.entity.QuartzJob;
import com.mxpioframework.quartz.service.QuartzService;

@Service("mxpio.quartz.quartzService")
public class QuartzServiceImpl implements QuartzService {
	
	@Autowired
	private Scheduler scheduler;

	@Override
	@Transactional(readOnly = true)
	public Page<QuartzJob> page(Pageable page, Criteria c) {
		return JpaUtil.linq(QuartzJob.class).where(c).paging(page);
	}

	@Override
	@Transactional(readOnly = true)
	public List<QuartzJob> list(Criteria c) {
		return JpaUtil.linq(QuartzJob.class).where(c).list();
	}

	@Override
	@Transactional(readOnly = true)
	public QuartzJob getById(String id) {
		return JpaUtil.getOne(QuartzJob.class,id);
	}

	@Override
	@Transactional
	public boolean addJob(QuartzJob quartzJob) {
		if("RUNNING".equals(quartzJob.getStatus())) {
			addScheduler(quartzJob.getJobClassName(), quartzJob.getJobCron(), quartzJob.getJobParams());
		}
		JpaUtil.save(quartzJob);
		return true;
	}

	@Override
	@Transactional
	public boolean editJob(QuartzJob quartzJob) {
		if("RUNNING".equals(quartzJob.getStatus())) {
			removeScheduler(quartzJob.getJobClassName());
			addScheduler(quartzJob.getJobClassName(), quartzJob.getJobCron(), quartzJob.getJobParams());
		}else if("PAUSE".equals(quartzJob.getStatus())){
			pause(quartzJob.getId());
		}else {
			removeScheduler(quartzJob.getJobClassName());
		}
		JpaUtil.update(quartzJob);
		return true;
	}

	@Override
	@Transactional
	public boolean deleteJob(String jobId) {
		QuartzJob job = JpaUtil.linq(QuartzJob.class).idEqual(jobId).findOne();
		removeScheduler(job.getJobClassName());
		JpaUtil.delete(job);
		return true;
	}

	@Override
	@Transactional
	public boolean resume(String jobId) {
		QuartzJob quartzJob = JpaUtil.linq(QuartzJob.class).idEqual(jobId).findOne();
		removeScheduler(quartzJob.getJobClassName());
		addScheduler(quartzJob.getJobClassName(), quartzJob.getJobCron(), quartzJob.getJobParams());
		quartzJob.setStatus("RUNNING");
		JpaUtil.update(quartzJob);
		return true;
	}

	@Override
	@Transactional
	public boolean pause(String jobId) {
		QuartzJob quartzJob = JpaUtil.linq(QuartzJob.class).idEqual(jobId).findOne();
		removeScheduler(quartzJob.getJobClassName());

		quartzJob.setStatus("PAUSE");
		JpaUtil.update(quartzJob);
		return true;
	}
	
	@Override
	public boolean execute(QuartzJob quartzJob) {
		try{
			String jobName = quartzJob.getJobClassName().trim();
			Class<?> jobClass = Class.forName(jobName);
			if(!Job.class.isAssignableFrom(jobClass)) {
				return false;
			}
			Date startDate = new Date();
			String ymd = DateFormatUtils.format(startDate,"yyyyMMddHHmmss");
			String identity =  jobName + ymd;
			//3秒后执行 只执行一次
			startDate.setTime(startDate.getTime()+3000L);
			// 定义一个Trigger
			SimpleTrigger trigger = (SimpleTrigger)TriggerBuilder.newTrigger()
					.withIdentity(identity)
					.startAt(startDate)
					.build();
			// 构建job信息
			JobDetail jobDetail = JobBuilder.newJob((Class<? extends Job>) jobClass).withIdentity(identity).usingJobData("params", quartzJob.getJobParams()).build();
			// 将trigger和 jobDetail 加入这个调度
			scheduler.scheduleJob(jobDetail, trigger);
			// 启动scheduler
			scheduler.start();
			return true;
		}catch (Exception e){
			e.fillInStackTrace();
			return false;
		}
	}

	@Override
	public boolean addScheduler(String jobClassName, String cron, String params) {
		try {
			Class<?> jobClass = Class.forName(jobClassName);
			if(!Job.class.isAssignableFrom(jobClass)) {
				return false;
			}else {
				scheduler.start();
				@SuppressWarnings("unchecked")
				JobDetail jobDetail = JobBuilder.newJob((Class<? extends Job>) jobClass).withIdentity(jobClassName).usingJobData("params", params).build();
				CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cron);
				CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(jobClassName).withSchedule(scheduleBuilder).build();
				scheduler.scheduleJob(jobDetail, trigger);
				return true;
			}
			
		}catch (Exception e) {
			e.fillInStackTrace();
			return false;
		}
	}

	@Override
	public boolean removeScheduler(String jobClassName) {
		try {
			scheduler.pauseTrigger(TriggerKey.triggerKey(jobClassName));
			scheduler.unscheduleJob(TriggerKey.triggerKey(jobClassName));
			scheduler.deleteJob(JobKey.jobKey(jobClassName));
			return true;
		} catch (Exception e) {
			e.fillInStackTrace();
			return false;
		}
	}
}
