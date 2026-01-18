package com.audiobook.dto.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseEvent {
    private String userId;
    private String audiobookId;
    private String purchaseId;
    private BigDecimal amountPaid;
    private String paymentMethod;
    private LocalDateTime purchaseDate;
}
