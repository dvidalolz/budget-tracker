package io.spring.training.corespring.personalbudgettracker.exceptions;

public class InputExceptions {

    public static class InputCreationException extends RuntimeException {
        public InputCreationException(String message) {
            super(message);
        }

        public InputCreationException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static class InputSaveException extends RuntimeException {
        public InputSaveException(String message) {
            super(message);
        }

        public InputSaveException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static class InputNotFoundException extends RuntimeException {
        public InputNotFoundException(String message) {
            super(message);
        }

        public InputNotFoundException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static class InputDeletionException extends RuntimeException {
        public InputDeletionException(String message) {
            super(message);
        }

        public InputDeletionException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static class InputRetrievalException extends RuntimeException {
        public InputRetrievalException(String message) {
            super(message);
        }

        public InputRetrievalException(String message, Throwable cause) {
            super(message, cause);
        }
    }

}
