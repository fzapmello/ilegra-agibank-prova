package br.com.ilegra.agibank.batch;

import br.com.ilegra.agibank.batch.listener.SalesJobExecutionListener;
import br.com.ilegra.agibank.batch.processor.SalesJobProcessor;
import br.com.ilegra.agibank.batch.reader.SalesJobReader;
import br.com.ilegra.agibank.batch.writer.SalesItemWriter;
import br.com.ilegra.agibank.model.AbstractItemDTO;
import br.com.ilegra.agibank.model.ResultDTOStore;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.ResourceAwareItemReaderItemStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static br.com.ilegra.agibank.batch.util.SalesJobUtil.*;

/**
 * Classe responsável por configurar a Job do Spring Batch
 */
@Configuration
@EnableBatchProcessing
public class SalesJobConfig {

    @Autowired
    private ResultDTOStore resultDTOStore;

    private static final Logger log = LoggerFactory.getLogger(SalesJobConfig.class);

    @Bean
    public Job salesJob(Step salesJobStep,
                        JobBuilderFactory jobBuilderFactory) {
        return jobBuilderFactory.get("salesJob")
                .incrementer(new RunIdIncrementer())
                .listener(listerner())
                .flow(salesJobStep)
                .end()
                .build();
    }

    @Bean
    public Step salesJobStep(ItemReader<AbstractItemDTO> reader,
                             ItemWriter<AbstractItemDTO> writer,
                             StepBuilderFactory stepBuilderFactory) {
        return stepBuilderFactory.get("salesJobStep")
                .<AbstractItemDTO, AbstractItemDTO>chunk(1)
                .reader(reader)
                .processor(processor())
                .writer(writer)
                .build();
    }

    public ItemReader<AbstractItemDTO> itemReader() {
        return new SalesJobReader();
    }

    @Bean
    public ItemWriter<AbstractItemDTO> itemWriter() {
        return new SalesItemWriter();
    }

    @Bean
    public ItemProcessor<AbstractItemDTO, AbstractItemDTO> processor() {
        return new SalesJobProcessor();
    }

    @Bean
    public JobExecutionListener listerner() {
        return new SalesJobExecutionListener();
    }

    @Bean
    @Qualifier("multiResourceReader")
    @StepScope
    public MultiResourceItemReader<AbstractItemDTO> multiResourceItemReader(@Value("#{jobParameters[inputDirectory]}") String inputDirectory)  {
        Resource[] resources = null;

        MultiResourceItemReader<AbstractItemDTO> resourceItemReader = new MultiResourceItemReader<>();

        if (StringUtils.isEmpty(inputDirectory)) {
            resources = loadDefaultLocation();
        } else {
            resources = loadFromPath(inputDirectory);
        }

        resourceItemReader.setResources(resources);
        resourceItemReader.setDelegate((ResourceAwareItemReaderItemStream<? extends AbstractItemDTO>) itemReader());
        return resourceItemReader;
    }

    private Resource[] loadFromPath(String inputDirectory) {
        Resource[] resources = null;
        ClassLoader loader = this.getClass().getClassLoader();
        ResourcePatternResolver patternResolver = new PathMatchingResourcePatternResolver(loader);
        try {
             resources = patternResolver.getResources(inputDirectory);
        } catch (IOException e) {
            log.error("Falha ao carregar arquivos em: " + inputDirectory);
            e.printStackTrace();
        }
        return resources;
    }

    private Resource[] loadDefaultLocation() {
        List<Resource> defaultResources = new ArrayList<>();
        try {
            Files.createDirectories(Path.of(DATA_IN_FOLDER));
        } catch (IOException e) {
            log.error("Falha ao criar diretório de entrada padrão em: " + DATA_IN_FOLDER);
            e.printStackTrace();
        }

        try {
            List<File> filesInFolder = Files.walk(Paths.get(DATA_IN_FOLDER))
                    .filter(Files::isRegularFile)
                    .map(Path::toFile)
                    .collect(Collectors.toList());

            for (File file : filesInFolder) {
                if (FilenameUtils.getExtension(file.getName()).equals(ALLOWED_EXTENSION)) {
                    defaultResources.add(new FileSystemResource(file));
                }
            }
        } catch (IOException e) {
            log.error("Erro ao ler arquivos no diretório padrão");
            e.printStackTrace();
        }

        if (defaultResources.isEmpty()) {
            resultDTOStore.get(KEY_RESULT_STORE).setOutputFile(false);
        }

        return defaultResources.toArray(new Resource[defaultResources.size()]);
    }
}