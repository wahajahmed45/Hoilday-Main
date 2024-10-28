package org.helmo.HolyD.controllers.exception;
import lombok.Getter;
import org.helmo.HolyD.domains.models.Error;

@Getter
public class UserAlreadyExistException extends RuntimeException {

    public static final String STATUCODE_ERROR = "400";
    public static final String ERROR_ERROR = "Bad request";
    public static final String MESSAGE_ERROR = "User already exist";

    private final Error ERROR;

    public UserAlreadyExistException(){
        super(MESSAGE_ERROR);
        this.ERROR = new Error(STATUCODE_ERROR, ERROR_ERROR, MESSAGE_ERROR);
    }
}
