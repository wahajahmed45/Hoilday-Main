package org.helmo.HolyD.controllers.exception;

import org.helmo.HolyD.domains.models.Error;

public class ActiviteNotFoundException extends RuntimeException {

    public static final String STATUCODE_ERROR = "400";
    public static final String ERROR_ERROR = "Bad request";
    public static final String MESSAGE_ERROR = "Could not find activite or user that ask for add an other user is not in";

    private final Error ERROR;

    public ActiviteNotFoundException() {
        super(MESSAGE_ERROR);
        this.ERROR = new Error(STATUCODE_ERROR, ERROR_ERROR, MESSAGE_ERROR);
    }
    public Error getERROR() {
        return ERROR;
    }
}
