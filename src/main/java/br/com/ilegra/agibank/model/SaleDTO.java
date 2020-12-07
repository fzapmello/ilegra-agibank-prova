package br.com.ilegra.agibank.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class SaleDTO extends AbstractItemDTO {

    private Long saleId;
    private List<ItemDetailDTO> itensDetails;

    @Override
    public String toString() {
        return "SaleDTO{" +
                "id='" + super.getId() + '\'' +
                ", saleId='" + saleId + '\'' +
                ", itensDetails='[ " + itensDetails.toString() +  "]" + '\'' +
                ", salesmanName='" + super.getName() + '\'' +
                '}';
    }

}
