package com.example.sample.service;

//import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class DiscountServiceImpl implements DiscountService {
    /** 기존 코드
    @Value("${discount.rate}")//할인율
    private double discountRate;

    @Value("${discount.max}") //할인 한도
    private int discountMax;
    */
    private final DiscountProperties discountProperties;

    public DiscountServiceImpl(DiscountProperties discountProperties) {
        this.discountProperties = discountProperties;
    }

    @Override
    public int calculateDiscountPrice(int originalPrice) {
        double discountRate = discountProperties.getRate();
        int discountMax = discountProperties.getMax();

        System.out.println("할인율:" + discountRate);
        System.out.println("할인한도:" + discountMax);

        int discount = (int)(originalPrice * discountRate);

        if (discount > discountMax) {
            System.out.println("할인액이 할인한도를 초과했습니다. discount=" + discount + " max=" + discountMax);
            discount = discountMax;
        }

        return originalPrice - discount;
    }
}
