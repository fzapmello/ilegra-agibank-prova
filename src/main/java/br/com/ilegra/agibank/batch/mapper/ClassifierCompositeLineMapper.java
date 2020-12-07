package br.com.ilegra.agibank.batch.mapper;

import br.com.ilegra.agibank.model.ResultDTOStore;
import br.com.ilegra.agibank.batch.util.SalesJobUtil;
import br.com.ilegra.agibank.config.ApplicationContextHolder;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.transform.IncorrectTokenCountException;
import org.springframework.classify.Classifier;

public class ClassifierCompositeLineMapper implements LineMapper<Object> {

    private Classifier<String, LineMapper<?>> classifier;

    public ClassifierCompositeLineMapper(Classifier<String, LineMapper<?>> classifier) {
        this.classifier = classifier;
    }

    @Override
    public Object mapLine(String line, int lineNumber)  {
        try {
            return classifier.classify(line).mapLine(line, lineNumber);
        } catch (IncorrectTokenCountException e) {
            ApplicationContextHolder.getContext().getBean(ResultDTOStore.class).get(SalesJobUtil.KEY_RESULT_STORE).getErrors().add("Número incorreto de campos na linha: " + line);
        } catch (Exception e) {
            ApplicationContextHolder.getContext().getBean(ResultDTOStore.class).get(SalesJobUtil.KEY_RESULT_STORE).getErrors().add("Erro ao processar linha: " + line);
            e.printStackTrace();
        }
        return null;
    }
}
