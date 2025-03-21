package jdr.generator.api.controllers;

public class RestPreconditions {
    public static <T> T checkFound(T data) throws Exception {
        if (data == null) {
            throw new InvalidContextException();
        }
        return data;
    }
}

class InvalidContextException extends RuntimeException {
    InvalidContextException() {
        super("> Invalid data context found !");
    }
}