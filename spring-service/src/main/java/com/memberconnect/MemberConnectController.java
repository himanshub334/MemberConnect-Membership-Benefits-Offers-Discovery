package com.memberconnect;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3001"})
public class MemberConnectController {
  private final OfferRepository offers;
  private final BenefitRepository benefits;
  private final RedemptionRepository redemptions;
  public MemberConnectController(OfferRepository o, BenefitRepository b, RedemptionRepository r) { offers=o; benefits=b; redemptions=r; }

  @GetMapping("/health") public Map<String,String> health() { return Map.of("status","ok","service","memberconnect"); }
  @GetMapping("/offers") public List<Offer> allOffers(@RequestParam(defaultValue="customer-1") String customerId) {
    return offers.findByEligibleCustomerIdOrEligibleCustomerIdIsNull(customerId);
  }
  @GetMapping("/offers/{id}") public ResponseEntity<Offer> offer(@PathVariable Long id) {
    return offers.findById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
  }
  @GetMapping("/benefits") public List<Benefit> benefits() { return benefits.findAll(); }
  @GetMapping("/benefits/{id}") public ResponseEntity<Benefit> benefit(@PathVariable Long id) {
    return benefits.findById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
  }
  @GetMapping("/offers/recommended") public List<Offer> recommended(@RequestParam(defaultValue="customer-1") String customerId) {
    List<Offer> list = offers.findByEligibleCustomerIdOrEligibleCustomerIdIsNull(customerId);
    return list.stream().limit(3).toList();
  }
  @PostMapping("/offers/{id}/redeem") @Transactional
  public ResponseEntity<?> redeem(@PathVariable Long id, @RequestParam(defaultValue="customer-1") String customerId) {
    Optional<Offer> offer = offers.findById(id);
    if (offer.isEmpty()) return ResponseEntity.notFound().build();
    Offer o = offer.get();
    if (o.eligibleCustomerId != null && !o.eligibleCustomerId.equals(customerId))
      return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error","Customer is not eligible for this offer"));
    if (redemptions.existsByOfferIdAndCustomerId(id, customerId))
      return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error","Offer already redeemed"));
    redemptions.save(new Redemption(id, customerId));
    return ResponseEntity.ok(Map.of("status","redeemed","offerId",id,"customerId",customerId));
  }
  @GetMapping("/customers/{customerId}/redemptions")
  public List<Redemption> history(@PathVariable String customerId) { return redemptions.findByCustomerIdOrderByRedeemedAtDesc(customerId); }
}
