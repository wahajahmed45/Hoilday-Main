package org.helmo.HolyD.domains.requests;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Data
@Builder
public class DocumentDTO {
    private Long id;
    private String fileName;
    private String fileUri;
    private String units;
}
