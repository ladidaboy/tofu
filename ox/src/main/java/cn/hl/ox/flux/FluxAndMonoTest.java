package cn.hl.ox.flux;

import reactor.core.publisher.Mono;

import java.util.Optional;

public class FluxAndMonoTest {
    public static void main(String[] args) {
        Mono.just("hello").flatMap(in-> Mono.just(in + " world")).subscribe(System.out::print);
    }
}
