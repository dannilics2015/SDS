package com.programmerscuriosity;

import java.util.LinkedList;
import java.util.List;

public class ServerErrorCaptureTask {

    protected static Integer errorsFromPost = 0;
    protected static Integer errorsFromGet = 0;

    public Integer getErrorsFromPost() {
        return errorsFromPost;
    }

    public Integer getErrorsFromGet() {
        return errorsFromGet;
    }
}
