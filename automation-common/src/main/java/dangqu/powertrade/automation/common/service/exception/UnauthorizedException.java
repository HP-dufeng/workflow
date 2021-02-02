package dangqu.powertrade.automation.common.service.exception;

/**
 * Exception thrown when the request that is performed need authorization, but no valid ticket has been passed along with it.
 * 
 */
public class UnauthorizedException extends BaseModelerRestException {

    private static final long serialVersionUID = 1L;

    public UnauthorizedException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnauthorizedException(String message) {
        super(message);
    }

    public UnauthorizedException(Throwable cause) {
        super(cause);
    }
}