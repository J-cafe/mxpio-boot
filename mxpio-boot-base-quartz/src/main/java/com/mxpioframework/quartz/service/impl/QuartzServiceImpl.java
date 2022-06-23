package com.mxpioframework.quartz.service.impl;

import java.util.List;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mxpioframework.jpa.JpaUtil;
import com.mxpioframework.jpa.query.Criteria;
import com.mxpioframework.quartz.entity.QuartzJob;
import com.mxpioframework.quartz.service.QuartzService;

@Service
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
	@Transactional(readOnly = false)
	public boolean addJob(QuartzJob quartzJob) {
		if("RUNNING".equals(quartzJob.getStatus())) {
			addScheduler(quartzJob.getJobClassName(), quartzJob.getJobCron(), quartzJob.getJobParams());
		}
		JpaUtil.save(quartzJob);
		return true;
	}

	@Override
	@Transactional(readOnly = false)
	public boolean editJob(QuartzJob quartzJob) {
		if("RUNNING".equals(quartzJob.getStatus())) {
			removeScheduler(quartzJob.getJobClassName());
			addScheduler(quartzJob.getJobClassName(), quartzJob.getJobCron(), quartzJob.getJobParams());
		}else if("PAUSE".equals(quartzJob.getStatus())){
			pause(quartzJob);
		}else {
			removeScheduler(quartzJob.getJobClassName());
		}
		JpaUtil.update(quartzJob);
		return true;
	}

	@Override
	@Transactional(readOnly = false)
	public boolean deleteJob(String jobId) {
		QuartzJob job = JpaUtil.linq(QuartzJob.class).idEqual(jobId).findOne();
		removeScheduler(job.getJobClassName());
		JpaUtil.delete(job);
		return true;
	}

	@Override
	@Transactional(readOnly = false)
	public boolean resumeJob(QuartzJob quartzJob) {
		removeScheduler(quartzJob.getJobClassName());
		addScheduler(quartzJob.getJobClassName(), quartzJob.getJobCron(), quartzJob.getJobParams());
		quartzJob.setStatus("RUNNING");
		JpaUtil.update(quartzJob);
		return true;
	}

	@Override
	@Transactional(readOnly = false)
	public boolean pause(QuartzJob quartzJob) {
		quartzJob.setStatus("PAUSE");
		JpaUtil.update(quartzJob);
		return true;
	}
	
	@Override
	@Transactional(readOnly = false)
	public boolean execute(QuartzJob quartzJob) {
		return true;
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
			e.printStackTrace();
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
			e.printStackTrace();
			return false;
		}
	}
	
	

}
