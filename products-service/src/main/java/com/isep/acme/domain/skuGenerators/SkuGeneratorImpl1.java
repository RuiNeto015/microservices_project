package com.isep.acme.domain.skuGenerators;


import com.isep.acme.applicationServices.interfaces.domain.ISkuGenerator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component()
@ConditionalOnProperty(value = "spring.sku.generator", havingValue = "implementation1")
public class SkuGeneratorImpl1 implements ISkuGenerator {
    @Override
    public String generateSku(String designation) {
        StringBuilder sku = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 3; i++) {
            sku.append(random.nextInt(10));
            sku.append((char) (random.nextInt(26) + 'A'));
        }

        for (int i = 0; i < 2; i++) {
            sku.append((char) (random.nextInt(26) + 'A'));
            sku.append(random.nextInt(10));
        }

        char specialChar = generateSpecialCharacter();
        sku.append(specialChar);

        return sku.toString();
    }

    private char generateSpecialCharacter() {
        String specialChars = "!@#$%^&*()_+[]{}|;':,.<>?/";
        Random random = new Random();
        return specialChars.charAt(random.nextInt(specialChars.length()));
    }
}
