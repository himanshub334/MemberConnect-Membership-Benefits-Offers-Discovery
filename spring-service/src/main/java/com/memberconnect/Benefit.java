package com.memberconnect;

import jakarta.persistence.*;

@Entity
public class Benefit {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
  public String title;
  @Column(length=1000) public String description;
  public String category;
  protected Benefit() {}
  public Benefit(String title, String description, String category) { this.title=title; this.description=description; this.category=category; }
}
