package br.com.ilegra.agibank.batch.listener;

import br.com.ilegra.agibank.model.ResultDTO;
import br.com.ilegra.agibank.model.ResultDTOStore;
import br.com.ilegra.agibank.batch.util.SalesJobUtil;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


/**
 * Classe responsável por gravar o arquivo de saída assim que a job
 * é finalizada.
 */
public class SalesJobExecutionListener implements JobExecutionListener {

    private static final Logger log = LoggerFactory.getLogger(SalesJobExecutionListener.class);

    @Autowired
    private ResultDTOStore resultDTOStore;

    @Override
    public void beforeJob(JobExecution jobExecution) {
        resultDTOStore.put(SalesJobUtil.KEY_RESULT_STORE, new ResultDTO());
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        if (resultDTOStore.get(SalesJobUtil.KEY_RESULT_STORE).isOutputFile()) {
            Path path = Paths.get(SalesJobUtil.DATA_OUT_FOLDER + String.format("%s.done.%s", RandomStringUtils.randomAlphanumeric(8), SalesJobUtil.ALLOWED_EXTENSION));

            try {
                Files.createDirectories(Path.of(SalesJobUtil.DATA_OUT_FOLDER));
            } catch (IOException e) {
                log.error("Falha criar diretório de saída padrão em:" + SalesJobUtil.DATA_OUT_FOLDER);
                e.printStackTrace();
            }

            try (BufferedWriter writer = Files.newBufferedWriter(path)) {
                writer.write(resultDTOStore.get(SalesJobUtil.KEY_RESULT_STORE).toString());
            } catch (IOException e) {
                log.error("Falha ao gravar arquivo de saída.");
                e.printStackTrace();
            }

            log.info("-- Arquivo de saída gravado com sucesso em: " + path + " --");
        } else {
            log.info("-- Nenhum arquivo .dat foi carregado --");
        }
    }
}
