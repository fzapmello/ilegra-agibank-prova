package br.com.ilegra.agibank;

import br.com.ilegra.agibank.model.ResultDTO;
import br.com.ilegra.agibank.model.ResultDTOStore;
import br.com.ilegra.agibank.batch.util.SalesJobUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBatchTest
@SpringBootTest
@RunWith(SpringRunner.class)
class IlegraAgibankApplicationTests {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;
    @Autowired
    private JobRepositoryTestUtils jobRepositoryTestUtils;
    @Autowired
    private ResultDTOStore resultDTOStore;

    Map<String, Object> expectedValues = new HashMap<>();

    @BeforeEach
    public void setUp() {
        expectedValues.put("totalCustomers", 6);
        expectedValues.put("totalSellers", 3);
        expectedValues.put("idExpansiveSale", 10L);
        expectedValues.put("worstSeller", "Marcos");
    }

    @AfterEach
    public void cleanUp() {
        jobRepositoryTestUtils.removeJobExecutions();
    }

    @Test
    public void launchJob() throws Exception {
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(getTestJobParameters());
        ResultDTO r = resultDTOStore.get(SalesJobUtil.KEY_RESULT_STORE);

        assertEquals(expectedValues.get("totalCustomers"), r.getTotalCustomers());
        assertEquals(expectedValues.get("totalSellers"), r.getTotalSellers());
        assertEquals(expectedValues.get("idExpansiveSale"), r.getIdExpansiveSale());
        assertEquals(expectedValues.get("worstSeller"), r.getWorstSeller());

        assertEquals(BatchStatus.COMPLETED, jobExecution.getStatus());
    }

    private JobParameters getTestJobParameters() {
        Map<String, JobParameter> parameters = new HashMap<>();

        JobParameter parameter = new JobParameter(new Date());
        parameters.put("currentTime", parameter);

        JobParameter parameter2 = new JobParameter("files/*." + SalesJobUtil.ALLOWED_EXTENSION);
        parameters.put("inputDirectory", parameter2);

        return new JobParameters(parameters);
    }

}
