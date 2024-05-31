package com.mxpioframework.camunda.vo;

import lombok.Data;
import org.camunda.bpm.engine.task.Task;

import java.io.Serializable;
import java.util.List;

@Data
public class AllTaskRetVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<Task> allTasks;

    private Long count;
}
