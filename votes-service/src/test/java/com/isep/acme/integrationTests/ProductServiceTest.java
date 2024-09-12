// package com.isep.acme.integrationTests;

// import com.isep.acme.adapters.database.repositories.DatabaseException;
// import com.isep.acme.integrationTests.config.ContainersEnvironment;
// import org.junit.jupiter.api.*;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.testcontainers.junit.jupiter.Testcontainers;

// @Testcontainers
// @SpringBootTest
// @TestInstance(TestInstance.Lifecycle.PER_CLASS)
// @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
// public class ProductServiceTest extends ContainersEnvironment {

//     @Autowired
//     ProductService productService;

//     @BeforeAll
//     void init() throws Exception {
//         CreateProductDTO productDTO = new CreateProductDTO("123456789112", "Playstation 5",
//                 "Great to play video games");
//         this.productService.create(productDTO);
//     }

//     @Test
//     @Order(1)
//     void createProduct() throws DatabaseException {
//         CreateProductDTO productDTO = new CreateProductDTO("123456789111", "MACBOOK PRO",
//                 "Great computer");
//         this.productService.create(productDTO);
//         var x = this.productService.findAll();
//         assert this.productService.findAll().size() == 2;
//     }

//     @Test
//     @Order(2)
//     void createProductWithExistingId() {
//         CreateProductDTO createProductDTO = new CreateProductDTO("123456789111", "MACBOOK PRO",
//                 "Great computer");
//         Assertions.assertThrows(DatabaseException.class, () -> this.productService.create(createProductDTO));
//     }

//     @Test
//     @Order(3)
//     void findByDesignation() {
//         assert this.productService.findByDesignation("MACBOOK PRO").size() == 1;
//     }

//     @Test
//     @Order(4)
//     void findBySku() {
//         assert this.productService.findBySku("123456789111") != null;
//     }

//     @Test
//     @Order(5)
//     void updateProduct() throws DatabaseException {
//         CreateProductDTO updateProductDTO = new CreateProductDTO("123456789111", "MACBOOK PRO",
//                 "Great computer with great screen");

//         this.productService.update(updateProductDTO);
//         assert this.productService.findBySku("123456789111").getDescription().equals("Great computer with great screen");
//     }

//     @Test
//     @Order(6)
//     void updateNonExistingProduct() {
//         CreateProductDTO updateProductDTO = new CreateProductDTO("123456789115", "MACBOOK PRO",
//                 "Great computer with great screen");

//         Assertions.assertThrows(Exception.class, () -> this.productService.update(updateProductDTO));
//     }

//     @Test
//     @Order(7)
//     void delete() throws DatabaseException {
//         this.productService.delete("123456789111");
//         assert this.productService.findAll().size() == 1;
//     }
// }
