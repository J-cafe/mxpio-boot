package com.mxpioframework.camunda.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "FormModelController", description = "表单管理")
@RestController("mxpio.camunda.FormModelController")
@RequestMapping("/camunda/form/")
public class FormModelController {

}
