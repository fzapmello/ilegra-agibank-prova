package br.com.ilegra.agibank.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CustomerDTO extends AbstractItemDTO {

    private String cnpj;
    private String businessArea;

    @Override
    public String toString() {
        return "CustomerDTO{" +
                "id='" + super.getId() + '\'' +
                ", cnpj='" + cnpj + '\'' +
                ", businessArea='" + businessArea + '\'' +
                '}';
    }
}
