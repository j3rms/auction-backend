package auction.services;

import auction.entities.Bid;
import auction.entities.Payment;
import auction.entities.User;
import auction.entities.DTO.PaymentDTO;
import auction.entities.enums.PaymentStatus;
import auction.repositories.BidRepository;
import auction.repositories.PaymentRepository;
import auction.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final BidRepository bidRepository;
    private final UserRepository userRepository;

    /**
     * Creates a new payment entry when a bid is won.
     */
    public PaymentDTO createPayment(Long bidId) {
        Bid bid = bidRepository.findById(bidId)
                .orElseThrow(() -> new EntityNotFoundException("Bid not found"));

        User customer = bid.getCustomer();
        User seller = bid.getItem().getSeller();  // Retrieve seller from the item
        BigDecimal amount = bid.getBidAmount();

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Invalid bid amount for payment.");
        }

        Payment payment = Payment.builder()
                .bid(bid)
                .customer(customer)
                .seller(seller)
                .amount(amount)
                .paymentStatus(PaymentStatus.COMPLETED) // Default payment status  (NO PAYMENT LOGIC)
                .transactionTime(LocalDateTime.now())
                .build();

        return new PaymentDTO(paymentRepository.save(payment));
    }

    /**
     * Updates the payment status (e.g., UNPAID → COMPLETED).
     */
    public PaymentDTO updatePaymentStatus(Long paymentId, PaymentStatus status) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new EntityNotFoundException("Payment not found"));

        payment.setPaymentStatus(status);
        return new PaymentDTO(paymentRepository.save(payment));
    }

    /**
     * Retrieves all payments made by a customer.
     */
    public List<PaymentDTO> getPaymentsByCustomer(Long customerId) {
        return paymentRepository.findByCustomerId(customerId)
                .stream()
                .map(PaymentDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all payments received by a seller.
     */
    public List<PaymentDTO> getPaymentsBySeller(Long sellerId) {
        return paymentRepository.findBySellerId(sellerId)
                .stream()
                .map(PaymentDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a single payment by ID.
     */
    public PaymentDTO getPaymentById(Long paymentId) {
        return paymentRepository.findById(paymentId)
                .map(PaymentDTO::new)
                .orElseThrow(() -> new EntityNotFoundException("Payment not found"));
    }
}