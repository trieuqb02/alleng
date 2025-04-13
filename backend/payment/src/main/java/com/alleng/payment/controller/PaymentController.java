package com.alleng.payment.controller;

import com.alleng.commonlibrary.constant.PaginationConstant;
import com.alleng.commonlibrary.payload.ApiVM;
import com.alleng.commonlibrary.payload.PaginationMV;
import com.alleng.payment.constant.PaymentStatus;
import com.alleng.payment.payload.response.PaymentMV;
import com.alleng.payment.service.IPaymentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequestMapping("${com.alleng.prefix.api:/api/v1}/payment")
public class PaymentController {

    IPaymentService paymentService;

    @GetMapping("/vnpay-payment-return")
    public ResponseEntity<?> handleVnPayCallback(
            @RequestParam("vnp_TxnRef") UUID txnRef,
            @RequestParam("vnp_ResponseCode") String responseCode
    ) {
        PaymentMV paymentMV;
        if ("00".equals(responseCode)) {
            paymentMV = paymentService.updatePayment(txnRef, PaymentStatus.SUCCESS);
        } else {
            paymentMV = paymentService.updatePayment(txnRef, PaymentStatus.FAILED);
        }

        URI redirectUrl = URI.create("http://localhost:4200/payment-result"
                + "?vnp_ResponseCode=" + paymentMV.paymentStatus()
                + "&amount=" + paymentMV.amount()
                + "&id=" + paymentMV.id()
        );
        return ResponseEntity.status(HttpStatus.FOUND).location(redirectUrl).build();
    }

    @GetMapping("")
    public ResponseEntity<ApiVM<PaginationMV<PaymentMV>>> getPayments(
            @RequestParam(name = "page", defaultValue = PaginationConstant.DEFAULT_PAGE) int page,
            @RequestParam(name = "limit", defaultValue = PaginationConstant.DEFAULT_LIMIT) int limit,
            @RequestParam(name = "sortDir", defaultValue = PaginationConstant.DEFAULT_DIR) String sortDir,
            @RequestParam(name = "sortBy", defaultValue = PaginationConstant.DEFAULT_ID) String sortBy
    ) {
        PaginationMV<PaymentMV> paginationMV = paymentService.getPaymentList(page, limit, sortDir, sortBy);
        ApiVM<PaginationMV<PaymentMV>> apiVM = new ApiVM<>(paginationMV);
        return ResponseEntity.status(HttpStatus.OK).body(apiVM);
    }

    @PutMapping("/{paymentId}")
    public ResponseEntity<ApiVM<PaymentMV>> updatePayment(@PathVariable("paymentId") UUID paymentId, @RequestParam("status") PaymentStatus status) {
        PaymentMV paymentMV = paymentService.updatePayment(paymentId, status);
        ApiVM<PaymentMV> paymentMVApiVM = new ApiVM<>(paymentMV);
        return ResponseEntity.status(HttpStatus.OK).body(paymentMVApiVM);
    }
}
