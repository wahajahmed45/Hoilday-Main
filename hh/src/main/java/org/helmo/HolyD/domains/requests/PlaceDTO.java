package org.helmo.HolyD.domains.requests;


import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Data
@Builder
public class PlaceDTO {
    private Long id;
    @DecimalMin(value = "-180.000000", message = "Wrong longitude size min=-180")
    @DecimalMax(value = "180.000000", message = "Wrong longitude size max=180")
    private BigDecimal longitude;
    @DecimalMin(value = "-90.000000", message = "Wrong latitude size max=90")
    @DecimalMax(value = "90.000000", message = "Wrong latitude size max=90")
    private BigDecimal latitude;
    @Size(max = 70, message = "Wrong street size max=70")
    private String rue;

    @Size(max = 15, message = "Wrong street number size max=15")
    private String rueNumero;

    @Size(max = 15, message = "Wrong postal code size max=15")
    private String codePostal;
    @Size(max = 50, message = "Wrong city size max=50")
    private String ville;
    @Size(min = 2, max = 50, message = "Wrong country size min=2 max=50")
    private String pays;

}
