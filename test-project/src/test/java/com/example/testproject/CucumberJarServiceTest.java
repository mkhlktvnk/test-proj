package com.example.testproject;

import com.example.testproject.config.JarProperties;
import com.example.testproject.model.Cucumber;
import com.example.testproject.service.CucumberJarService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CucumberJarServiceTest {

    @Autowired
    private JarProperties jarProperties;

    @Autowired
    private CucumberJarService cucumberJarService;

    @Test
    void groupCucumbersIntoJars_shouldReturnExpectedResponse() {
        jarProperties.setMaxVolume(BigDecimal.valueOf(100));
        var cucumberFlux = getCucumbers();

        var actualJars = cucumberJarService.groupCucumbersIntoJars(cucumberFlux).toStream().toList();

        assertThat(actualJars).hasSize(6);
    }

    private Flux<Cucumber> getCucumbers() {
        return Flux.fromIterable(List.of(
            Cucumber.builder().volume(BigDecimal.valueOf(120)).build(),
            Cucumber.builder().volume(BigDecimal.valueOf(130)).build(),
            Cucumber.builder().volume(BigDecimal.valueOf(140)).build()
        ));
    }
}
