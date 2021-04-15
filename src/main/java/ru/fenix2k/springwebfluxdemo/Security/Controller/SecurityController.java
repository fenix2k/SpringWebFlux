package ru.fenix2k.springwebfluxdemo.Security.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.security.Principal;

@RestController
@RequestMapping("/security")
public class SecurityController {

    @GetMapping("")
    public Mono<String> mainPage(Principal principal) {
        return Mono.just("Hello " + getPrincipalName(principal) + ", from a main page");
    }

    @GetMapping("/public")
    public Mono<String> publicPage(Principal principal) {
        return Mono.just("Hello " + getPrincipalName(principal) + ", from a public page");
    }

    @GetMapping("/private")
    public Mono<String> privatePage(Principal principal) {
        return Mono.just("Hello " + getPrincipalName(principal) + ", from a private page");
    }

    private String getPrincipalName(Principal principal) {
        return principal == null ? "anonymous" : principal.getName();
    }

}
