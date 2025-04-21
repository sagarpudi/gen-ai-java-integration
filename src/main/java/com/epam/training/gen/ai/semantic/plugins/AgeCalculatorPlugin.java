package com.epam.training.gen.ai.semantic.plugins;

import com.microsoft.semantickernel.semanticfunctions.annotations.DefineKernelFunction;
import com.microsoft.semantickernel.semanticfunctions.annotations.KernelFunctionParameter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;

@Slf4j
public class AgeCalculatorPlugin {

    @DefineKernelFunction(name = "calculateAge", description = "Calculates age based on the provided birth year.")
    public int calculateAge(
            @KernelFunctionParameter(description = "Year of birth", name = "birthYear") int birthYear) {
        int currentYear = LocalDate.now().getYear();
        int age = currentYear - birthYear;
        log.info("Age calculated: [{}]", age);
        return age;
    }
}