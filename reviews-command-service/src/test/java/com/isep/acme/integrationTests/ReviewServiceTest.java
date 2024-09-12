
// package com.isep.acme.integrationTests;

// import com.isep.acme.adapters.database.repositories.DatabaseException;
// import com.isep.acme.adapters.dto.*;
// import com.isep.acme.applicationServices.ReviewService;
// import com.isep.acme.applicationServices.UserService;
// import com.isep.acme.applicationServices.interfaces.repositories.IProductRepository;
// import com.isep.acme.applicationServices.interfaces.repositories.IReviewRepository;
// import com.isep.acme.domain.aggregates.product.Product;
// import com.isep.acme.domain.aggregates.review.Review;
// import com.isep.acme.domain.enums.ApprovalStatusEnum;
// import com.isep.acme.domain.enums.RoleEnum;
// import com.isep.acme.domain.enums.VoteEnum;
// import com.isep.acme.integrationTests.config.ContainersEnvironment;
// import org.junit.jupiter.api.*;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.test.annotation.IfProfileValue;
// import org.testcontainers.junit.jupiter.Testcontainers;

// import java.util.List;

// @Testcontainers
// @SpringBootTest
// @TestInstance(TestInstance.Lifecycle.PER_CLASS)
// @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
// public class ReviewServiceTest extends ContainersEnvironment {

//     @Autowired
//     ReviewService reviewService;

//     @Autowired
//     IProductRepository productRepository;

//     @Autowired
//     IReviewRepository reviewRepository;

//     @Autowired
//     UserService userService;

//     @BeforeAll
//     void init() throws DatabaseException {
//         Product product = new Product("123456789112", ApprovalStatusEnum.Approved, 2);
//         this.productRepository.create(product);

//         CreateUserDTO user1 = new CreateUserDTO("simao@gmail.com", "abcde",
//                 "Simao Santos", "123456789", "R. xpto", RoleEnum.RegisteredUser.name());
//         this.userService.create(user1);
//     }

//     @Test
//     @Order(1)
//     void createReview() throws Exception {
//         FetchUserDTO user = this.userService.findByEmail("simao@gmail.com");

//         CreateReviewDTO createReviewDTO = new CreateReviewDTO("123456789112", "To much fun",
//                 5);
//         this.reviewService.create(user.getId(), createReviewDTO);
//     }

//     @Test
//     @Order(2)
//     void createReviewForSameProduct() throws Exception {
//         FetchUserDTO user = this.userService.findByEmail("simao@gmail.com");

//         CreateReviewDTO createReviewDTO = new CreateReviewDTO("123456789182", "To much fun", 5);
//         Assertions.assertThrows(Exception.class, () -> this.reviewService.create(user.getId(), createReviewDTO));
//     }

//     @Test
//     @Order(3)
//     void findByUser() {
//         FetchUserDTO user = this.userService.findByEmail("simao@gmail.com");
//         assert this.reviewRepository.findByUser(user.getId()) != null;
//     }

//     @Test
//     @Order(4)
//     void update() throws Exception {
//         FetchUserDTO user = this.userService.findByEmail("simao@gmail.com");

//         CreateReviewDTO createReviewDTO = new CreateReviewDTO("123456789112", "To much fun", 5);
//         FetchReviewDTO reviewDTO = this.reviewService.create(user.getId(), createReviewDTO);

//         this.reviewService.update(new UpdateReviewDTO(reviewDTO.getId(), "To much fun!", 5));
//         Review reviewDTO1 = this.reviewRepository.findById(reviewDTO.getId());
//         assert reviewDTO1.getText().equals("To much fun!");
//     }

//     @Test
//     @Order(5)
//     void rejectReview() throws Exception {
//         FetchUserDTO user = this.userService.findByEmail("simao@gmail.com");

//         CreateReviewDTO createReviewDTO = new CreateReviewDTO("123456789112", "To much fun", 5);
//         FetchReviewDTO fetchReviewDTO = this.reviewService.create(user.getId(), createReviewDTO);

//         this.reviewService.reject(fetchReviewDTO.getId(), "Your review is to great");
//         Review reviews2 = this.reviewRepository.findById(fetchReviewDTO.getId());

//         assert reviews2.getApprovalStatus().equals(ApprovalStatusEnum.Rejected);
//     }

// }
