package org.helmo.HolyD.domains.models;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Error {

    private String statusCode;
    private String message;
    private String error;

    @Override
    public String toString() {
        return "Error{" +
                "statusCode='" + statusCode + '\'' +
                ", error='" + error + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
