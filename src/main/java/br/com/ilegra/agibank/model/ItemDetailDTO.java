package br.com.ilegra.agibank.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
@Data
@EqualsAndHashCode(callSuper = true)
public class ItemDetailDTO extends AbstractItemDTO {

    private Integer quantity;
    private BigDecimal price;

    @Override
    public String toString() {
        return "ItemDetailDTO{" +
                "id='" + super.getId() + '\'' +
                ", quantity='" + quantity + '\'' +
                ", price='" + price + '\'' +
                '}';
    }

}
