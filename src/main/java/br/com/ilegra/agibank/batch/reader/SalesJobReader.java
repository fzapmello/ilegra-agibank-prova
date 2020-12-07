package br.com.ilegra.agibank.batch.reader;

import br.com.ilegra.agibank.batch.mapper.ClassifierCompositeLineMapper;
import br.com.ilegra.agibank.batch.mapper.SaleDTOLineMapper;
import br.com.ilegra.agibank.model.AbstractItemDTO;
import br.com.ilegra.agibank.model.CustomerDTO;
import br.com.ilegra.agibank.model.SellerDTO;
import br.com.ilegra.agibank.model.UnknownItemDTO;
import br.com.ilegra.agibank.batch.util.SalesJobUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.classify.Classifier;


/**
 *  Classe responsável por configurar a etapa de leitura da job.
 */
public class SalesJobReader extends FlatFileItemReader<AbstractItemDTO> {

    public SalesJobReader() {
        this.setLinesToSkip(0); //Arquivo não possui cabeçalho
        LineMapper<?> compositeLineMapper = createCompositeLineMapper();
        this.setLineMapper((LineMapper<AbstractItemDTO>) compositeLineMapper);
        this.setEncoding("UTF-8");
    }

    /**
     * Método responsável por classificar o tipo de objeto lido conforme o layout da linha
     *
     * @return ClassifierCompositeLineMapper
     */
    private LineMapper<?> createCompositeLineMapper() {
        Classifier<String, LineMapper<?>> classifier = (Classifier<String, LineMapper<? extends Object>>) classifiable -> {
            if (assertBasicFormat(classifiable)) {
                if (SalesJobUtil.isSellerDTOLine(classifiable)) {
                    DefaultLineMapper sellerDTOLineMapper = new DefaultLineMapper<AbstractItemDTO>();
                    sellerDTOLineMapper.setLineTokenizer(SalesJobUtil.createDTOLineTokenizer(new String[] { "id", "cpf", "name", "salary"}));
                    sellerDTOLineMapper.setFieldSetMapper(SalesJobUtil.createDTOFieldSetMapper(SellerDTO.class));
                    return sellerDTOLineMapper;
                } else if (SalesJobUtil.isCustomerDTOLine(classifiable)) {
                    DefaultLineMapper customerDTOLineMapper = new DefaultLineMapper<AbstractItemDTO>();
                    customerDTOLineMapper.setLineTokenizer(SalesJobUtil.createDTOLineTokenizer(new String[] { "id", "cnpj", "name", "businessArea"}));
                    customerDTOLineMapper.setFieldSetMapper(SalesJobUtil.createDTOFieldSetMapper(CustomerDTO.class));
                    return customerDTOLineMapper;
                } else if (SalesJobUtil.isSaleDTOLine(classifiable)) {
                    return new SaleDTOLineMapper();
                }
            }

            DefaultLineMapper defaultLine = new DefaultLineMapper<AbstractItemDTO>();
            defaultLine.setLineTokenizer(SalesJobUtil.createDTOLineTokenizer(new String[] { "value1", "value2", "value3", "value4"}));
            defaultLine.setFieldSetMapper(SalesJobUtil.createDTOFieldSetMapper(UnknownItemDTO.class));
            return defaultLine;
        };
        return new ClassifierCompositeLineMapper(classifier);
    }

    private Boolean assertBasicFormat(String classifiable) {
        if (StringUtils.isEmpty(classifiable)) {
            return false;
        }
        if (classifiable.split(SalesJobUtil.DEFAULT_DELIMITER).length != SalesJobUtil.TOTAL_LINE_INDEXES) {
            return false;
        }
        return true;
    }
}
