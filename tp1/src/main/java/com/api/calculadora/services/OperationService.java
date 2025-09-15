package com.api.calculadora.services;

import org.springframework.stereotype.Service;

@Service
public class OperationService {
    public double add(double a, double b) {
        return a + b;
    }

    public double subtract(double a, double b) {
        return a - b;
    }

    public double multiply(double a, double b) {
        return a * b;
    }

    public double divide(double a, double b) {
        if (b == 0) {
            throw new IllegalArgumentException("Divisão por zero não permitida");
        }
        return a / b;
    }

    public double power(double a, double b) {
        return Math.pow(a, b);
    }
}
