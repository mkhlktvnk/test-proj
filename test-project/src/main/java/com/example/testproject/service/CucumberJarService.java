package com.example.testproject.service;

import com.example.testproject.config.JarProperties;
import com.example.testproject.model.Cucumber;
import com.example.testproject.model.Jar;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class CucumberJarService {

    private final JarProperties jarProperties;

    public Flux<Jar> groupCucumbersIntoJars(@NonNull Flux<Cucumber> cucumbers) {
        return cucumbers.flatMap(this::splitCucumberIfNeeded)
            .scan(new Jar(new ArrayList<>(), jarProperties.getMaxVolume()), this::addCucumberToJar)
            .filter(jar -> !jar.getCucumbers().isEmpty());
    }

    @NonNull
    private Flux<Cucumber> splitCucumberIfNeeded(@NonNull Cucumber cucumber) {
        var cucumbers = new ArrayList<Cucumber>();
        var cucumberVolume = cucumber.getVolume();
        var remainingVolume = jarProperties.getMaxVolume();

        while (cucumberVolume.compareTo(remainingVolume) > 0) {
            cucumbers.add(new Cucumber(remainingVolume));
            cucumberVolume = cucumberVolume.subtract(remainingVolume);
        }

        if (cucumberVolume.compareTo(BigDecimal.ZERO) > 0) {
            cucumbers.add(new Cucumber(cucumberVolume));
        }

        return Flux.fromIterable(cucumbers);
    }

    @NonNull
    private Jar addCucumberToJar(@NonNull Jar jar, @NonNull Cucumber cucumber) {
        BigDecimal currentVolume = jar.getCucumbers().stream()
            .map(Cucumber::getVolume)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (currentVolume.add(cucumber.getVolume()).compareTo(jar.getMaxVolume()) <= 0) {
            jar.getCucumbers().add(cucumber);
        } else {
            jar = new Jar(new ArrayList<>(), jar.getMaxVolume());
            jar.getCucumbers().add(cucumber);
        }

        return jar;
    }
}


