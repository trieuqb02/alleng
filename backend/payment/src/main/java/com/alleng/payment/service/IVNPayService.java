package com.alleng.payment.service;

import java.math.BigDecimal;
import java.util.UUID;

public interface IVNPayService {

    String vnpayUrl(UUID paymentId, BigDecimal price, String urlReturn);
}
