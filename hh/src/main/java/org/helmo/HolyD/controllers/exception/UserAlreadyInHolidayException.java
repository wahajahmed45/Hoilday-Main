package org.helmo.HolyD.controllers.exception;

import org.helmo.HolyD.domains.models.Error;

public class UserAlreadyInHolidayException extends RuntimeException {

    public static final String STATUCODE_ERROR = "400";
    public static final String ERROR_ERROR = "Bad request";
    public static final String MESSAGE_ERROR = "L'utilisateur a déjà une date de vacance a cette date";

    private final Error ERROR;

    public UserAlreadyInHolidayException() {
        super(MESSAGE_ERROR);
        this.ERROR = new Error(STATUCODE_ERROR,MESSAGE_ERROR ,ERROR_ERROR );
    }
    public Error getERROR() {
        return ERROR;
    }
}