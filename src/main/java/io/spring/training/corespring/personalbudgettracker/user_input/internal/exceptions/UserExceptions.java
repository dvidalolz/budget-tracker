package io.spring.training.corespring.personalbudgettracker.user_input.internal.exceptions;

public class UserExceptions {

    public static class UserNotFoundException extends RuntimeException {
        public UserNotFoundException(String message) {
            super(message);
        }

        public UserNotFoundException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static class UserCreationException extends RuntimeException {
        public UserCreationException(String message) {
            super(message);
        }

        public UserCreationException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static class UserSaveException extends RuntimeException {
        public UserSaveException(String message) {
            super(message);
        }

        public UserSaveException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static class UserDeletionException extends RuntimeException {
        public UserDeletionException(String message) {
            super(message);
        }

        public UserDeletionException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
