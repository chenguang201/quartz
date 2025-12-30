package com.tuling.service;

import com.tuling.model.QuartzJob;

public interface QuartzJobService {

    void addJob(QuartzJob quartzJob);
    void updateJob(QuartzJob quartzJob);
}
