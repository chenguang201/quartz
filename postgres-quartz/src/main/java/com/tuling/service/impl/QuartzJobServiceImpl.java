package com.tuling.service.impl;

import com.tuling.model.JobEnum;
import com.tuling.model.QuartzJob;
import com.tuling.service.QuartzJobService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Slf4j
public class QuartzJobServiceImpl implements QuartzJobService {


    private final Scheduler scheduler;

    public QuartzJobServiceImpl(Scheduler scheduler) {
        this.scheduler = scheduler;
    }


    @Override
    public void addJob(QuartzJob quartzJob) {
        log.info("添加任务:{}", quartzJob.getJobName());
        JobKey jobKey = new JobKey(quartzJob.getJobName(), quartzJob.getJobGroup());
        try {
            if (scheduler.checkExists(jobKey)){
                log.info("任务已存在:{}", quartzJob.getJobName());
                updateJob(quartzJob);
                return;
            }
            JobEnum byJobName = JobEnum.getByJobName(quartzJob.getJobName());
            if (Objects.isNull(byJobName)){
                log.info("任务不存在:{}", quartzJob.getJobName());
                return;
            }
            JobDetail jobDetail = JobBuilder.newJob(byJobName.getJobClass())
                    .withIdentity(quartzJob.getJobName())
                    .storeDurably(true)
                    .build();

            CronTrigger cronTrigger = TriggerBuilder.newTrigger()
                    .withIdentity(quartzJob.getJobName())
                    .withSchedule(CronScheduleBuilder.cronSchedule(quartzJob.getCron()))
                    .startNow()
                    .build();
            scheduler.scheduleJob(jobDetail, cronTrigger);

        } catch (Exception e) {
            log.error("添加任务异常:{}", e);
        }

    }

    @Override
    public void updateJob(QuartzJob quartzJob) {
        try {
            log.info("更新任务:{}", quartzJob.getJobName());
            TriggerKey triggerKey = TriggerKey.triggerKey(quartzJob.getJobName());
            CronTrigger trigger = (CronTrigger)scheduler.getTrigger(triggerKey);
            trigger = trigger.getTriggerBuilder()
                    .withIdentity(triggerKey)
                    .withSchedule(CronScheduleBuilder.cronSchedule(quartzJob.getCron()))
                    .build();
            scheduler.rescheduleJob(triggerKey, trigger);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
