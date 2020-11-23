package com.epam.esm.util.generator;

import com.epam.esm.domain.Certificate;
import com.epam.esm.domain.User;
import com.epam.esm.service.OrderService;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderGenerate {
    private static final int MIN_COUNT_ORDER = 1;
    private static final int MAX_COUNT_ORDER = 3;
    private final OrderService orderService;

    @Autowired
    public OrderGenerate(OrderService orderService) {
        this.orderService = orderService;
    }

    public void generateOrders(int countOrders, List<Certificate> certificates, List<User> users) {
        int usersSize = users.size();
        int certificatesSize = certificates.size();
        for (int i = 0; i < countOrders; i++) {
            List<Certificate> certificatesToCreate = new ArrayList<>();
            int countCertificatesToOrder = RandomUtils.nextInt(MIN_COUNT_ORDER, MAX_COUNT_ORDER);
            for (int j = 0; j < countCertificatesToOrder; j++) {
                int randomCertificate = RandomUtils.nextInt(0, certificatesSize - 1);
                certificatesToCreate.add(certificates.get(randomCertificate));
            }
            Long randomUser = RandomUtils.nextLong(1, usersSize - 1);
            orderService.makeOrder(randomUser, certificatesToCreate);
        }
    }
}
