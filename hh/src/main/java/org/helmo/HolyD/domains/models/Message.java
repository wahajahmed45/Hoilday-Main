package org.helmo.HolyD.domains.models;


import lombok.*;

import java.time.OffsetDateTime;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Data
@Builder
public class Message {

    private Long id;
    private String content;
    private OffsetDateTime sendingDate;
    private Participant sender;

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", sendingDate=" + sendingDate +
                ", sender=" + sender +
                '}';
    }
}
