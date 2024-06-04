package com.mxpioframework.camunda.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class AllTaskRetVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<TaskVO> allTasks;

    private Long count;
}
