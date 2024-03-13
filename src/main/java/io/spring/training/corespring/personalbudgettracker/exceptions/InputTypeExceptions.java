package io.spring.training.corespring.personalbudgettracker.exceptions;

public class InputTypeExceptions {

    public static class InputTypeCreationException extends RuntimeException {
        public InputTypeCreationException(String message) {
            super(message);
        }

        public InputTypeCreationException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static class InputTypeSaveException extends RuntimeException {
        public InputTypeSaveException(String message) {
            super(message);
        }

        public InputTypeSaveException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static class InputTypeNotFoundException extends RuntimeException {
        public InputTypeNotFoundException(String message) {
            super(message);
        }

        public InputTypeNotFoundException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static class InputTypeDeletionException extends RuntimeException {
        public InputTypeDeletionException(String message) {
            super(message);
        }

        public InputTypeDeletionException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static class InputSubTypeCreationException extends RuntimeException {
        public InputSubTypeCreationException(String message) {
            super(message);
        }

        public InputSubTypeCreationException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static class InputSubTypeSaveException extends RuntimeException {
        public InputSubTypeSaveException(String message) {
            super(message);
        }

        public InputSubTypeSaveException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static class InputSubTypeNotFoundException extends RuntimeException {
        public InputSubTypeNotFoundException(String message) {
            super(message);
        }

        public InputSubTypeNotFoundException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static class InputSubTypeDeletionException extends RuntimeException {
        public InputSubTypeDeletionException(String message) {
            super(message);
        }

        public InputSubTypeDeletionException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
