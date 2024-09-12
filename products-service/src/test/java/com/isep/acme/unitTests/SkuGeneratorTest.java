/*
package com.isep.acme.unitTests;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SkuGeneratorTest {

    @Autowired
    private ISkuGenerator skuGenerator;

    @Test
    public void testFirstSkuGeneratorImplementation() {
        String sku = skuGenerator.generateSku("CAR");
        boolean isMatch = sku.matches("\\d[A-Z]\\d[A-Z]\\d[A-Z][A-Z]\\d[A-Z]\\d[^\\dA-Z]");
        assertTrue(isMatch);
    }

    @Test
    public void testSecondSkuGeneratorImplementation() {
        String sku = skuGenerator.generateSku("WHEELCHAIR");
        assertEquals("8f53bae458", sku);
    }
}*/
