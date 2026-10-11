package com.memberconnect;

import jakarta.persistence.*;

@Entity
public class Offer {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
  public String title;
  @Column(length=1000) public String description;
  public String category;
  public int minimumPoints;
  public String eligibleCustomerId;
  protected Offer() {}
  public Offer(String title, String description, String category, int minimumPoints, String eligibleCustomerId) {
    this.title=title; this.description=description; this.category=category; this.minimumPoints=minimumPoints; this.eligibleCustomerId=eligibleCustomerId;
  }
}
