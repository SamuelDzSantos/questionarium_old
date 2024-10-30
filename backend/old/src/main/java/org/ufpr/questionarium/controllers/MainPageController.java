package org.ufpr.questionarium.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

// Controler usado para fornecer o arquivo html base do angular index.html

@Controller
public class MainPageController {

    @GetMapping("/")
    public String index() {
        return "index";
    }
}
