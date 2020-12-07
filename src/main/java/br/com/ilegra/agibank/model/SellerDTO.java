package br.com.ilegra.agibank.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SellerDTO extends AbstractItemDTO {

    private String cpf;
    private String salary;

    @Override
    public String toString() {
        return "SellerDTO{" +
                "id='" + super.getId() + '\'' +
                ", cpf='" + cpf + '\'' +
                ", name='" + super.getName() + '\'' +
                ", salary='" + salary + '\'' +
                '}';
    }

}
