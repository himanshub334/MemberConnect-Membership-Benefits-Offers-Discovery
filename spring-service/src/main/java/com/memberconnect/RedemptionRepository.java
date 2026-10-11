package com.memberconnect;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface RedemptionRepository extends JpaRepository<Redemption, Long> {
  List<Redemption> findByCustomerIdOrderByRedeemedAtDesc(String customerId);
  boolean existsByOfferIdAndCustomerId(Long offerId, String customerId);
}
