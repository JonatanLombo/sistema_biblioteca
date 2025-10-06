package com.proyecto.biblioteca.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class PrestamoInfoDTO {

    private Long codigoPrestamo;
    private LocalDate fechaInicio;
    private LocalDate fechaEntrega;
    private List<String> titulos;
    private String nomUsuario;

}
