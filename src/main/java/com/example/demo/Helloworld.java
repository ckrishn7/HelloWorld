package com.example.demo;
import org.springframework.web.bind.annotation.RequestMapping; 
import org.springframework.web.bind.annotation.RestController; 
// Add the controller
@RestController
class Helloworld {
        @RequestMapping("/greet")
        public String greet() {
                return "Hello User..";
        }
}