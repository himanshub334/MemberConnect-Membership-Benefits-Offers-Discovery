package com.memberconnect;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import java.util.List;

@SpringBootApplication
public class MemberConnectApplication {
  public static void main(String[] args) { SpringApplication.run(MemberConnectApplication.class, args); }

  @Bean CommandLineRunner seed(OfferRepository offers, BenefitRepository benefits) {
    return args -> {
      if (offers.count() == 0) offers.saveAll(List.of(
        new Offer("Coffee Club", "Free drink with eligible purchase", "Dining", 10, "customer-1"),
        new Offer("Travel Bonus", "Earn bonus points on selected travel", "Travel", 20, "customer-1"),
        new Offer("Wellness Perk", "Save on selected wellness services", "Wellness", 5, "customer-2"),
        new Offer("Streaming Offer", "Discount on a monthly subscription", "Entertainment", 0, "customer-1")
      ));
      if (benefits.count() == 0) benefits.saveAll(List.of(
        new Benefit("Member support", "Priority access to member assistance", "Services"),
        new Benefit("Reward points", "Earn points on eligible purchases", "Rewards"),
        new Benefit("Travel privileges", "Explore eligible travel benefits", "Travel")
      ));
    };
  }
}
