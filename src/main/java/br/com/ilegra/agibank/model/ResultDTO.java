package br.com.ilegra.agibank.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class ResultDTO {

    private int totalCustomers;
    private int totalSellers;
    private Long idExpansiveSale;
    private String worstSeller;

    private BigDecimal highestPrice = new BigDecimal("0");
    private BigDecimal worstPrice = new BigDecimal("0");

    private List<UnknownItemDTO> unprocessedLines = new ArrayList<>();

    private List<String> errors = new ArrayList<>();

    //Verifica se é para gerar o arquivo de saída.
    private boolean outputFile = true;

    @Override
    public String toString() {
        return "ResultDTO {" +
                "\n totalCustomers = '" + totalCustomers + '\'' +
                ",\n totalSellers = '" + totalSellers + '\'' +
                ",\n idExpansiveSale = '" + idExpansiveSale + '\'' +
                ",\n worstSeller = '" + worstSeller + '\'' +
                "\n}" +
                "\n\n\n"+
                "List of all unprocessed lines: \n" +
                unprocessedLines.toString() +
                "\n\n\n"+
                "List of all errors: \n" +
                errors.toString()
                ;
    }

}
