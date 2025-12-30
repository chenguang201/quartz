package com.tuling.job;


import com.tuling.config.QuartzJobProperties;
import com.tuling.model.QuartzJob;
import com.tuling.service.QuartzJobService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
@Slf4j
public class QuartzJobInitializer {

    private final QuartzJobProperties properties;
    private final QuartzJobService quartzJobService;

    public QuartzJobInitializer(QuartzJobProperties properties, QuartzJobService quartzJobService) {
        this.properties = properties;
        this.quartzJobService = quartzJobService;
    }
    @PostConstruct
    public void init() {
        log.info("初始化定时任务");
        properties.getCron().forEach((jobName, cron) -> {
            log.info("初始化定时任务:{}", jobName);
            QuartzJob quartzJob = new QuartzJob();
            quartzJob.setJobName(jobName);
            quartzJob.setCron(cron);
            quartzJobService.addJob(quartzJob);
        });
    }
}
