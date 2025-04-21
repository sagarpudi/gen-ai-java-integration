package com.epam.training.gen.ai.semantic.plugins;

import com.microsoft.semantickernel.semanticfunctions.annotations.DefineKernelFunction;
import com.microsoft.semantickernel.semanticfunctions.annotations.KernelFunctionParameter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WeatherForecastPlugin {

    @DefineKernelFunction(name = "getWeatherForecast", description = "Provides a mocked weather forecast.")
    public String getWeatherForecast(
            @KernelFunctionParameter(description = "City name", name = "city") String city) {
        String forecast = "Sunny, 25°C in " + city;
        log.info("Weather forecast for [{}]: [{}]", city, forecast);
        return forecast;
    }
}