package org.helmo.HolyD.domains.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Data
@Builder
@Table(name = "helmo_participant")
public class ParticipantEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Email
    @Size(min = 2, max = 100, message = "Wrong email size min=2 max=100")
    @Column(length = 100, unique = true, nullable = false)
    private String email;
    @Size(min = 2, max = 30, message = "Wrong lastname size min=2 max=30")
    @Column(length = 30, nullable = false, name = "nom")
    private String lastname;
    @Size(min = 2, max = 30, message = "Wrong firstname size min=2 max=30")
    @Column(length = 30, nullable = false, name = "prenom")
    private String firstname;


}
