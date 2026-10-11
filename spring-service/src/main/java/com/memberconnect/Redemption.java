package com.memberconnect;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
public class Redemption {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
  public Long offerId;
  public String customerId;
  public Instant redeemedAt;
  protected Redemption() {}
  public Redemption(Long offerId, String customerId) { this.offerId=offerId; this.customerId=customerId; this.redeemedAt=Instant.now(); }
}
