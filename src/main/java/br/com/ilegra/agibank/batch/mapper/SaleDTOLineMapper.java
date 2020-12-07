package br.com.ilegra.agibank.batch.mapper;

import br.com.ilegra.agibank.model.SaleDTO;
import br.com.ilegra.agibank.batch.util.SalesJobUtil;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;

public class SaleDTOLineMapper extends DefaultLineMapper<SaleDTO> {

    @Override
    public SaleDTO mapLine(String line, int lineNumber) {
        SaleDTO dto = new SaleDTO();
        String[] split = line.split(SalesJobUtil.DEFAULT_DELIMITER);
        dto.setId(Long.parseLong(split[0]));
        dto.setSaleId(Long.parseLong(split[1]));
        dto.setItensDetails(SalesJobUtil.formatItemDetailLine(split[2]));
        dto.setName(split[3]);
        return dto;
    }
}
