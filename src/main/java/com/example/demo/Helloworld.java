package com.example.demo;
import org.springframework.web.bind.annotation.RequestMapping; 
import org.springframework.web.bind.annotation.RestController; 
// Add the controller
@RestController
class Helloworld {
        @RequestMapping("/api/greet")
        public String greet() {
                return "Hello User..";
        }
        @RequestMapping("/public/hi")
        public String hi() {
                return "Hi User..";
        }
}