package br.com.ilegra.agibank.batch.processor;

import br.com.ilegra.agibank.batch.util.SalesJobUtil;
import br.com.ilegra.agibank.model.*;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *  Classe responsável por processar os registros.
 */
public class SalesJobProcessor implements ItemProcessor<AbstractItemDTO, AbstractItemDTO> {

    @Autowired
    private ResultDTOStore resultDTOStore;

    @Override
    public AbstractItemDTO process(AbstractItemDTO item) {
        //Total de vendedores
        if (item instanceof SellerDTO) { SalesJobUtil.processTotalSellers(resultDTOStore); }
        //Total de clientes
        if (item instanceof CustomerDTO) { SalesJobUtil.processTotalCustomers(resultDTOStore); }
        //Venda mais cara e pior vendedor
        if (item instanceof SaleDTO) { SalesJobUtil.processHighLowSale((SaleDTO) item, resultDTOStore); }
        //Layout desconhecido
        if (item instanceof UnknownItemDTO) { SalesJobUtil.processUnkownItem((UnknownItemDTO) item, resultDTOStore); }

        return item;
    }
}
