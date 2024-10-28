package org.helmo.HolyD.controllers.exception;

import org.helmo.HolyD.domains.models.Error;

public class IntervalActiviteIsNotInIntervalVacanceException extends RuntimeException {

    public static final String STATUCODE_ERROR = "400";
    public static final String ERROR_ERROR = "Bad request";
    public static final String MESSAGE_ERROR = "La date de l'interval de l'activité n'est compris dans la date de l'interval des vacances";

    private final Error ERROR;

    public IntervalActiviteIsNotInIntervalVacanceException(){
        super(MESSAGE_ERROR);
        this.ERROR = new Error(STATUCODE_ERROR, ERROR_ERROR, MESSAGE_ERROR);
    }
    public Error getERROR() {
        return ERROR;
    }
}