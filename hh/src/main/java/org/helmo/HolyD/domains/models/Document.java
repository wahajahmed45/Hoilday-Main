package org.helmo.HolyD.domains.models;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Data
@Builder
public class Document {
    private Long id;
    private String fileName;
    private String fileUri;
    private String units;
    private User user;
    private Vacation vacation;
}
