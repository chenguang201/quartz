package com.tuling.model;


import lombok.Data;

@Data
public class QuartzJob {

    private String jobName;
    private String jobGroup;
    private String cron;
}
