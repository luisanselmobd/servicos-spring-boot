package com.api.calculadora.controllers;

import com.api.calculadora.services.OperationService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/operations")
public class OperationController {

    private final OperationService operationService;

    public OperationController(OperationService operationService) {
        this.operationService = operationService;
    }
    @RequestMapping(value = "/add", method = {RequestMethod.GET, RequestMethod.POST})
    public double add(@RequestParam double a, @RequestParam double b) {
        return operationService.add(a, b);
    }

    @RequestMapping(value = "/subtract", method = {RequestMethod.GET, RequestMethod.POST})
    public double subtract(@RequestParam double a, @RequestParam double b) {
        return operationService.subtract(a, b);
    }

    @RequestMapping(value = "/multiply", method = {RequestMethod.GET, RequestMethod.POST})
    public double multiply(@RequestParam double a, @RequestParam double b) {
        return operationService.multiply(a, b);
    }

    @RequestMapping(value = "/divide", method = {RequestMethod.GET, RequestMethod.POST})
    public double divide(@RequestParam double a, @RequestParam double b) {
        return operationService.divide(a, b);
    }

    @RequestMapping(value = "/power", method = {RequestMethod.GET, RequestMethod.POST})
    public double power(@RequestParam double a, @RequestParam double b) {
        return operationService.power(a, b);
    }

}
