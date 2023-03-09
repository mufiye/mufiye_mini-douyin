package org.example.mufiye.exception;

import org.example.mufiye.result.ResponseStatusEnum;

public class GraceException {

    public static void display(ResponseStatusEnum responseStatusEnum) {
        throw new MyCustomException(responseStatusEnum);
    }

}