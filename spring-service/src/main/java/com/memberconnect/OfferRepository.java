package com.memberconnect;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface OfferRepository extends JpaRepository<Offer, Long> {
  List<Offer> findByEligibleCustomerIdOrEligibleCustomerIdIsNull(String customerId);
}
