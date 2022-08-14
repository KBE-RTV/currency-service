package com.currencyservice;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CurrencyApplication implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(CurrencyApplication.class, args);
    }

    @Override
    public void run(String... args) {
        System.out.println("Waiting..." + "\n");
        //new Scanner(System.in).nextLine();
    }
}

