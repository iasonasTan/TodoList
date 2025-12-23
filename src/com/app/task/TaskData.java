package com.app.task;

import java.io.Serializable;

public record TaskData(String name, String desc) implements Serializable {

    public TaskData (String name) {
        this(name, "[blank]");
    }

}
