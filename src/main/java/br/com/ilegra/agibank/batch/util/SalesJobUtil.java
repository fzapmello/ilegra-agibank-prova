package br.com.ilegra.agibank.batch.util;

import br.com.ilegra.agibank.model.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.LineTokenizer;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SalesJobUtil {

    public static final String DEFAULT_DELIMITER = "ç";
    public static final int TOTAL_LINE_INDEXES = 4;
    public static final String REGEX_ITEM_DETAIL =  "\\[(.*?)\\]";
    public static final String DATA_IN_FOLDER = setDataInFolder();
    public static final String DATA_OUT_FOLDER = setDataOutFolder();
    public static final String ALLOWED_EXTENSION = "dat";
    public static final String KEY_RESULT_STORE = "resultDTO";

    public static boolean isSaleDTOLine(String classifiable) {
        String[] split = classifiable.split(DEFAULT_DELIMITER);
        String itens = split[2];
        if (StringUtils.isNotEmpty(itens)) {
            if (itens.length() < 2) {
                return false;
            } else if (itens.startsWith("[") && itens.endsWith("]")) {
                return true;
            }
        }
        return false;
    }

    public static boolean isCustomerDTOLine(String classifiable) {
        String[] split = classifiable.split(DEFAULT_DELIMITER);
        return split[1].length() == 14; // é CNPJ
    }

    public static boolean isSellerDTOLine(String classifiable) {
        String[] split = classifiable.split(DEFAULT_DELIMITER);
        return split[1].length() == 11; // é CPF
    }

    public static LineTokenizer createDTOLineTokenizer(String[] fields) {
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer(DEFAULT_DELIMITER);
        tokenizer.setNames(fields);
        return tokenizer;
    }

    public static FieldSetMapper<?> createDTOFieldSetMapper(Class clazzDTO) {
        BeanWrapperFieldSetMapper<?> fsm = new BeanWrapperFieldSetMapper<>();
        fsm.setTargetType(clazzDTO);
        return fsm;
    }

    public static List<ItemDetailDTO> formatItemDetailLine(String itemDetails) {
        List<ItemDetailDTO> itens = new ArrayList<>();
        Pattern p = Pattern.compile(REGEX_ITEM_DETAIL);
        Matcher m = p.matcher(itemDetails);

        while(m.find()) {
            String[] split = m.group(1).split(",");
            for (String itemDetail : split) {
                String[] split1 = itemDetail.split("-");
                ItemDetailDTO item = new ItemDetailDTO();
                item.setId(Long.parseLong(split1[0]));
                item.setQuantity(Integer.parseInt(split1[1]));
                item.setPrice(new BigDecimal(split1[2]));
                itens.add(item);
            }
        }
        return !itens.isEmpty() ?  itens : null;
    }

    private static String setDataInFolder() {
        return System.getProperty("user.home") + File.separator + "data" + File.separator + "in";
    }

    private static String setDataOutFolder() {
        return System.getProperty("user.home") + File.separator + "data" + File.separator + "out" + File.separator;
    }

    public static void processTotalSellers(ResultDTOStore resultDTOStore) {
        ResultDTO resultDTO = resultDTOStore.get(KEY_RESULT_STORE);
        Integer totalSellers = resultDTO.getTotalSellers();
        resultDTO.setTotalSellers(++totalSellers);
    }

    public static void processTotalCustomers(ResultDTOStore resultDTOStore) {
        ResultDTO resultDTO = resultDTOStore.get(KEY_RESULT_STORE);
        Integer totalCustomers = resultDTO.getTotalCustomers();
        resultDTO.setTotalCustomers(++totalCustomers);
    }

    public static void processHighLowSale(SaleDTO item, ResultDTOStore resultDTOStore) {
        SaleDTO sale = item;
        ResultDTO resultDTO = resultDTOStore.get(KEY_RESULT_STORE);
        for (ItemDetailDTO itemDetail : sale.getItensDetails()) {
            // Seta o ID da venda mais cara
            if (itemDetail.getPrice().compareTo(resultDTO.getHighestPrice()) > 0 ) {
                resultDTO.setHighestPrice(getTotalPrice(itemDetail));
                resultDTO.setIdExpansiveSale(sale.getSaleId());
            }

            // Seta o nome do pior vendedor
            if (resultDTO.getWorstPrice().compareTo(new BigDecimal("0")) == 0) {
                resultDTO.setWorstPrice(getTotalPrice(itemDetail));
                resultDTO.setWorstSeller(sale.getName());
            } else if (itemDetail.getPrice().compareTo(resultDTO.getWorstPrice()) < 0 ) {
                resultDTO.setWorstPrice(getTotalPrice(itemDetail));
                resultDTO.setWorstSeller(sale.getName());
            }
        }
    }

    private static BigDecimal getTotalPrice(ItemDetailDTO price) {
        return price.getPrice().multiply(BigDecimal.valueOf(price.getQuantity()));
    }

    public static void processUnkownItem(UnknownItemDTO item, ResultDTOStore resultDTOStore) {
        ResultDTO resultDTO = resultDTOStore.get(KEY_RESULT_STORE);
        resultDTO.getUnprocessedLines().add(item);
    }
}
