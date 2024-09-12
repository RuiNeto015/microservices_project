package com.isep.acme.domain.skuGenerators;


import com.isep.acme.applicationServices.interfaces.domain.ISkuGenerator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component()
@ConditionalOnProperty(value = "spring.sku.generator", havingValue = "implementation2")
public class SkuGeneratorImpl2 implements ISkuGenerator {

    @Override
    public String generateSku(String designation) {

        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        byte[] hashBytes = md.digest(designation.getBytes());

        StringBuilder hexStringBuilder = new StringBuilder();
        for (byte b : hashBytes) {
            hexStringBuilder.append(String.format("%02x", b));
        }

        String hexResult = hexStringBuilder.toString();
        int middleIndex = (hexResult.length() - 10) / 2;
        String middleHex = hexResult.substring(middleIndex, middleIndex + 10);

        return middleHex;
    }

}
