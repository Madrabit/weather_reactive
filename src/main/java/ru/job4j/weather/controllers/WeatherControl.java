package ru.job4j.weather.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import ru.job4j.weather.domain.Weather;
import ru.job4j.weather.services.WeatherService;

import java.time.Duration;

@RestController
public class WeatherControl {

    private final WeatherService weathers;

    public WeatherControl(WeatherService weathers) {
        this.weathers = weathers;
    }

    @GetMapping(value = "/all", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Weather> all() {
        Flux<Weather> data = weathers.all();
        Flux<Long> delay = Flux.interval(Duration.ofSeconds(3));
        return Flux.zip(data, delay).map(Tuple2::getT1);
    }

    @GetMapping(value = "/get/{id}")
    public Mono<Weather> get(@PathVariable Integer id) {
        return weathers.findById(id);
    }

    @GetMapping("/hottest")
    public Mono<Weather> findMaxTemp() {
        return weathers.findMaxTemp();
    }

    @GetMapping("/cityGreatThen/{temp}")
    public Flux<Weather> findByTemp(@PathVariable Integer temp) {
        Flux<Weather> data = weathers.findByTemp(temp);
        Flux<Long> delay = Flux.interval(Duration.ofSeconds(3));
        return Flux.zip(data, delay).map(Tuple2::getT1);
    }
}
