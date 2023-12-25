package ua.nure.nosqlpractice.security;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class SecurityController {

    @GetMapping("/login")
    String login(){
        return "security/login";
    }




}
