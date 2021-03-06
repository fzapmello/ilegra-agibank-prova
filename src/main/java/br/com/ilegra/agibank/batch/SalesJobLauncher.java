package br.com.ilegra.agibank.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Bean responsável por agendar e executar as Jobs do Spring Batch.
 */
@Component
@EnableScheduling
public class SalesJobLauncher {

    private static final Logger LOGGER = LoggerFactory.getLogger(SalesJobLauncher.class);

    private final Job job;
    private final JobLauncher jobLauncher;

    @Autowired
    public SalesJobLauncher(Job job, JobLauncher jobLauncher) {
        this.job = job;
        this.jobLauncher = jobLauncher;
    }

    /**
     *
     * Executa a Job a cada 30 segundos
     *
     * @throws JobParametersInvalidException
     * @throws JobExecutionAlreadyRunningException
     * @throws JobRestartException
     * @throws JobInstanceAlreadyCompleteException
     */
    @Scheduled(cron = "0/30 * * * * *")
    public void runSpringBatchJob() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        LOGGER.info("-- Job do Spring Batch Iniciada --");
        jobLauncher.run(job, novaExecucao());
        LOGGER.info("-- Job do Spring Batch encerrada --");
    }

    private JobParameters novaExecucao() {
        Map<String, JobParameter> parameters = new HashMap<>();

        JobParameter parameter = new JobParameter(new Date());
        parameters.put("currentTime", parameter);

        return new JobParameters(parameters);
    }
}
