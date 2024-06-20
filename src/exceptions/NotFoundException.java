package exceptions;

public class NotFoundException extends NullPointerException {
        public NotFoundException(final String message) {
            super(message);
        }
    }