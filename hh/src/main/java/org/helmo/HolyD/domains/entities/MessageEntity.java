package org.helmo.HolyD.domains.entities;


import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.OffsetDateTime;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "MESSAGE")
public class MessageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_Message")
    @SequenceGenerator(name = "id_Message", sequenceName = "ID_MESSAGE", allocationSize = 1)
    private Long id;

    @Size(min = 1, max = 800, message = "Wrong content message size min=1 max=800")
    @Column(length = 800, nullable = false)
    private String content;

    @Column(nullable = false)
    private OffsetDateTime sendingDate;

    @ManyToOne
    @JoinColumn(nullable = false)
    private UserEntity sender;

    @ManyToOne(optional = false)
    private VacationEntity vacance;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MessageEntity message = (MessageEntity) o;
        return id.equals(message.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "MessageDTO{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", sendingDate=" + sendingDate +
                ", sender=" + sender +
                ", vacance=" + vacance +
                '}';
    }
}
