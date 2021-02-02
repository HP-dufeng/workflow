package dangqu.powertrade.automation.common.service.exception;

/**
 * This is needed for when the REST resource is stated to return for example image/png, but needs to throw a 404 In this case, the default ExceptionHandlerAdvice will create a message for the 404. But
 * that will lead to a Spring exception and the end result is that it will be transmitted as a 500.
 * 
 */
public class NonJsonResourceNotFoundException extends BaseModelerRestException {

    private static final long serialVersionUID = 1L;

    public NonJsonResourceNotFoundException() {
    }

}