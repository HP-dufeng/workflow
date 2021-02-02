package dangqu.powertrade.automation.common.rest.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import dangqu.powertrade.automation.common.service.exception.BadRequestException;
import dangqu.powertrade.automation.common.service.exception.BaseModelerRestException;
import dangqu.powertrade.automation.common.service.exception.ConflictingRequestException;
import dangqu.powertrade.automation.common.service.exception.ErrorInfo;
import dangqu.powertrade.automation.common.service.exception.InternalServerErrorException;
import dangqu.powertrade.automation.common.service.exception.NotFoundException;
import dangqu.powertrade.automation.common.service.exception.NotPermittedException;
import dangqu.powertrade.automation.common.service.exception.UnauthorizedException;

import org.flowable.common.engine.api.FlowableIllegalArgumentException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.LockedException;

@ControllerAdvice
public class RestExceptionHandlerAdvice {

    private static final String UNAUTHORIZED_MESSAGE_KEY = "GENERAL.ERROR.UNAUTHORIZED";
    private static final String NOT_FOUND_MESSAGE_KEY = "GENERAL.ERROR.NOT-FOUND";
    private static final String BAD_REQUEST_MESSAGE_KEY = "GENERAL.ERROR.BAD-REQUEST";
    private static final String INTERNAL_SERVER_ERROR_MESSAGE_KEY = "GENERAL.ERROR.INTERNAL-SERVER_ERROR";
    private static final String FORBIDDEN_MESSAGE_KEY = "GENERAL.ERROR.FORBIDDEN";
    private static final String INACTIVE_USER_MESSAGE_KEY = "GENERAL.ERROR.INACTIVE_USER";

    @ResponseStatus(HttpStatus.NOT_FOUND) // 404
    @ExceptionHandler(NotFoundException.class)
    @ResponseBody
    public ErrorInfo handleNotFound(NotFoundException e) {
        return createInfoFromException(e, NOT_FOUND_MESSAGE_KEY);
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST) // 400
    @ExceptionHandler(BadRequestException.class)
    @ResponseBody
    public ErrorInfo handleBadRequest(BadRequestException e) {
        return createInfoFromException(e, BAD_REQUEST_MESSAGE_KEY);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // 500
    @ExceptionHandler(InternalServerErrorException.class)
    @ResponseBody
    public ErrorInfo handleInternalServerError(InternalServerErrorException e) {
        return createInfoFromException(e, INTERNAL_SERVER_ERROR_MESSAGE_KEY);
    }


    @ResponseStatus(HttpStatus.FORBIDDEN) // 403
    @ExceptionHandler(NotPermittedException.class)
    @ResponseBody
    public ErrorInfo handleNoPermission(NotPermittedException e) {
        return createInfoFromException(e, FORBIDDEN_MESSAGE_KEY);
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED) // 401
    @ExceptionHandler(LockedException.class)
    @ResponseBody
    public ErrorInfo handleLockedUser(LockedException e) {
        ErrorInfo result = new ErrorInfo(e.getMessage());
        result.setMessageKey(INACTIVE_USER_MESSAGE_KEY);
        return result;
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED) // 401
    @ExceptionHandler(UnauthorizedException.class)
    @ResponseBody
    public ErrorInfo handleUnauthorized(UnauthorizedException e) {
        return createInfoFromException(e, UNAUTHORIZED_MESSAGE_KEY);
    }

    @ResponseStatus(HttpStatus.CONFLICT) // 409
    @ExceptionHandler(ConflictingRequestException.class)
    @ResponseBody
    public ErrorInfo handleConflict(ConflictingRequestException e) {
        return createInfoFromException(e, BAD_REQUEST_MESSAGE_KEY);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST) // 400
    @ExceptionHandler(FlowableIllegalArgumentException.class)
    @ResponseBody
    public ErrorInfo handleIllegalArgument(FlowableIllegalArgumentException e) {
        return createInfoFromException(new BadRequestException(e.getMessage()), BAD_REQUEST_MESSAGE_KEY);
    }

    protected ErrorInfo createInfoFromException(BaseModelerRestException exception, String defaultMessageKey) {
        ErrorInfo result = null;
        result = new ErrorInfo(exception.getMessage());
        if (exception.getCustomData() != null) {
            result.setCustomData(exception.getCustomData());
        }
        if (exception.getMessageKey() != null) {
            result.setMessageKey(exception.getMessageKey());
        } else {
            result.setMessageKey(defaultMessageKey);
        }
        return result;
    }

    protected String getSafeMessageKey(String fragment) {
        if (fragment != null) {
            return fragment.toUpperCase();
        }
        return "";
    }

}
