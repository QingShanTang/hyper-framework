package org.qingshan.utils.validate;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class User {
    @NotNull
    private Long id;
    @NotBlank
    private String name;
    private Integer age;
}
