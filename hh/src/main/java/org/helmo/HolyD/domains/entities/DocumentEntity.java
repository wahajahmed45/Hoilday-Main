package org.helmo.HolyD.domains.entities;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Data
@Builder
@Entity
@Table(name = "helmo_documents")
public class DocumentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String fileName;
    @Column(nullable = false)
    private String fileUri;
    @Column()
    private String units;
    @ManyToOne()
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity userEntity;
    @ManyToOne()
    @JoinColumn(name = "vacation_id", nullable = false)
    private VacationEntity vacationEntity;

}
