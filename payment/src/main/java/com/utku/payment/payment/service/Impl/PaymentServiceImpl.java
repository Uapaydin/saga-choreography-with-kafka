package com.utku.payment.payment.service.Impl;

import com.utku.payment.payment.data.dto.CheckBudgetAndCompletePaymentDto;
import com.utku.payment.payment.data.entity.Customer;
import com.utku.payment.payment.data.entity.Payment;
import com.utku.payment.payment.data.repo.CustomerRepository;
import com.utku.payment.payment.data.repo.PaymentRepository;
import com.utku.payment.payment.service.PaymentService;
import com.utku.saga.exception.BaseException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author tcuapaydin
 * @created 15/04/2022 - 14:49
 */
@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final CustomerRepository customerRepository;

    public PaymentServiceImpl(PaymentRepository paymentRepository, CustomerRepository customerRepository) {
        this.paymentRepository = paymentRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public void checkBudgetAndCompletePayment(CheckBudgetAndCompletePaymentDto checkBudgetAndCompletePaymentDto) {
        Optional<Customer> foundCustomer = customerRepository.findById(checkBudgetAndCompletePaymentDto.getCustomerId());
        if(foundCustomer.isPresent()){
            if(foundCustomer.get().getBudget() >= checkBudgetAndCompletePaymentDto.getTotalPrice()){
                Payment newPayment = new Payment();
                newPayment.setCustomerId(foundCustomer.get().getId());
                newPayment.setInventoryId(checkBudgetAndCompletePaymentDto.getInventoryId());
                newPayment.setTotalPrice(checkBudgetAndCompletePaymentDto.getTotalPrice());
                paymentRepository.saveAndFlush(newPayment);
                foundCustomer.get().setBudget(foundCustomer.get().getBudget() - checkBudgetAndCompletePaymentDto.getTotalPrice());
                customerRepository.saveAndFlush(foundCustomer.get());
            }else{
                throw new BaseException(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }else{
            throw new BaseException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
