package com.tuling.model;


import com.tuling.job.MyJob;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.scheduling.quartz.QuartzJobBean;

@Getter
@AllArgsConstructor
public enum JobEnum {

    MY_JOB("myJob", MyJob.class);

    private final String jobName;
    private final Class<? extends QuartzJobBean> jobClass;

    public static JobEnum getByJobName(String jobName) {
        for (JobEnum jobEnum : JobEnum.values()) {
            if (jobEnum.getJobName().equals(jobName)) {
                return jobEnum;
            }
        }
        return null;
    }
}
