package com.mxpioframework.quartz.model;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.mxpioframework.common.util.BeanReflectionUtils;
import com.mxpioframework.common.util.SpringUtil;
import com.mxpioframework.quartz.entity.QuartzJob;

public class MxpioJob implements Job {
	
	private Method method;
	
	private Object bean;
	
	private Object[] paramsAry;
	
	public static MxpioJob create(QuartzJob quartzJob){
		MxpioJob job = new MxpioJob();
		try{
			if("Class".equals(quartzJob.getJobType())){
				Class<?> jobClass = Class.forName(quartzJob.getJobClassName());
				job.bean = BeanReflectionUtils.newInstance(jobClass);
			}else if("SpringBean".equals(quartzJob.getJobType())){
				job.bean = SpringUtil.getBean(quartzJob.getJobClassName());
			}
			Method[] methods = job.bean.getClass().getMethods();
			for(Method m : methods){
				if(m.getName().equals(quartzJob.getJobMethodName())){
					job.method = m;
					break;
				}
			}
		}catch (Exception e) {
			e.fillInStackTrace();
		}
		return job;
	}

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			method.invoke(bean, paramsAry);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.fillInStackTrace();
		}
	}

}
