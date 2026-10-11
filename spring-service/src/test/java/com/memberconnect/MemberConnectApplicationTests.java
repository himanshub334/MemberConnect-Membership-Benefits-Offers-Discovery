package com.memberconnect;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class MemberConnectApplicationTests {
  @Test void offerCanBeConstructed() {
    Offer offer = new Offer("Coffee Club","Free drink","Dining",10,"customer-1");
    assertEquals("Coffee Club", offer.title);
    assertEquals(10, offer.minimumPoints);
  }
}
