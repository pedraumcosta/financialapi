package com.pedraumcosta;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class FinancialServer {

    public static void main(String[] args) {
        SpringApplication.run(FinancialServer.class, args);
    }

    class Message {
        private String message;

        public Message(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }

    @RequestMapping(path = "/financial/{message}", method= RequestMethod.GET)
    public Message financial(@PathVariable("message") String message) {
        return new Message(message);
    }

}
