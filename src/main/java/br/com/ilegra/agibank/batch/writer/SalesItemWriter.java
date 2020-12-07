package br.com.ilegra.agibank.batch.writer;

import br.com.ilegra.agibank.model.AbstractItemDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

public class SalesItemWriter implements ItemWriter<AbstractItemDTO> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SalesItemWriter.class);

    @Override
    public void write(List<? extends AbstractItemDTO> list)  {
        LOGGER.info("AbstractItemDTO list: {}", list);
    }
}