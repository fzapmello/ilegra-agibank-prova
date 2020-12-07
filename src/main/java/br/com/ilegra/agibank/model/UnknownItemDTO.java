package br.com.ilegra.agibank.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UnknownItemDTO extends AbstractItemDTO {
    private String value1;
    private String value2;
    private String value3;
    private String value4;
}
