// package com.isep.acme.integrationTests;

// import com.isep.acme.adapters.database.repositories.DatabaseException;
// import com.isep.acme.adapters.dto.*;
// import com.isep.acme.applicationServices.VoteService;
// import com.isep.acme.applicationServices.UserService;
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
//     VoteService reviewService;

//     @Autowired
//     ProductService productService;

//     @Autowired
//     UserService userService;

//     @BeforeAll
//     void init() throws DatabaseException {
//         CreateProductDTO product1 = new CreateProductDTO("123456789112", "Playstation 5",
//                 "Great to play video games");
//         this.productService.create(product1);
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
//         assert this.reviewService.findBySkuAndStatus("123456789112", ApprovalStatusEnum.Pending).size() == 1;
//     }

//     @Test
//     @Order(2)
//     void createReviewForSameProduct() throws Exception {
//         FetchUserDTO user = this.userService.findByEmail("simao@gmail.com");

//         CreateReviewDTO createReviewDTO = new CreateReviewDTO("123456789112", "To much fun",
//                 5);
//         Assertions.assertThrows(Exception.class, () ->this.reviewService.create(user.getId(), createReviewDTO));
//     }

//     @Test
//     @Order(3)
//     void findByUser() {
//         FetchUserDTO user = this.userService.findByEmail("simao@gmail.com");
//         assert this.reviewService.findByUser(user.getId()).size() == 1;
//     }

//     @Test
//     @Order(4)
//     void update() throws DatabaseException {
//         FetchUserDTO user = this.userService.findByEmail("simao@gmail.com");
//         List<FetchReviewDTO> reviews = this.reviewService.findByUser(user.getId());
//         this.reviewService.update(new UpdateReviewDTO(reviews.get(0).getId(), "To much fun!", 5));
//         assert this.reviewService.findByUser(user.getId()).get(0).getText().equals("To much fun!");
//     }

//     @Test
//     @Order(5)
//     void rejectReview() throws DatabaseException {
//         List<FetchReviewDTO> reviews = this.reviewService.findBySkuAndStatus("123456789112",
//                 ApprovalStatusEnum.Pending);

//         this.reviewService.reject(reviews.get(0).getId(), "Your review is to great");
//         assert this.reviewService.findBySkuAndStatus("123456789112", ApprovalStatusEnum.Pending).isEmpty();
//         assert this.reviewService.findBySkuAndStatus("123456789112", ApprovalStatusEnum.Rejected).size() == 1;
//     }

//     @Test
//     @Order(6)
//     @IfProfileValue(name = "spring.reviews.recommendation", value = "implementation2")
//     void getReviewsRecommendationImpl2() throws Exception {
//         CreateProductDTO product1 = new CreateProductDTO("123456789113", "Playstation 5",
//                 "Great to play video games");
//         this.productService.create(product1);

//         CreateUserDTO user1 = new CreateUserDTO("user1@gmail.com", "abcde",
//                 "user1", "123456781", "R. xpto", RoleEnum.RegisteredUser.name());
//         this.userService.create(user1);
//         CreateUserDTO user2 = new CreateUserDTO("user2@gmail.com", "abcde",
//                 "user2", "123456782", "R. xpto", RoleEnum.RegisteredUser.name());
//         this.userService.create(user2);
//         CreateUserDTO user3 = new CreateUserDTO("user3@gmail.com", "abcde",
//                 "user3", "123456783", "R. xpto", RoleEnum.RegisteredUser.name());
//         this.userService.create(user3);

//         FetchUserDTO user11 = this.userService.findByEmail("user1@gmail.com");
//         FetchUserDTO user22 = this.userService.findByEmail("user2@gmail.com");
//         FetchUserDTO user33 = this.userService.findByEmail("user3@gmail.com");

//         CreateReviewDTO createReviewDTO1 = new CreateReviewDTO("123456789112", "To much fun 1",
//                 5);
//         CreateReviewDTO createReviewDTO2 = new CreateReviewDTO("123456789112", "To much fun 2",
//                 5);
//         CreateReviewDTO createReviewDTO3 = new CreateReviewDTO("123456789112", "To much fun 3",
//                 5);

//         FetchReviewDTO frDTO1 = this.reviewService.create(user11.getId(), createReviewDTO1);
//         FetchReviewDTO frDTO2 = this.reviewService.create(user22.getId(), createReviewDTO2);
//         FetchReviewDTO frDTO3 = this.reviewService.create(user33.getId(), createReviewDTO3);

//         reviewService.approve(frDTO1.getId());
//         reviewService.approve(frDTO2.getId());
//         reviewService.approve(frDTO3.getId());

//         reviewService.vote(user11.getId(), frDTO1.getId(), String.valueOf(VoteEnum.UpVote));
//         reviewService.vote(user11.getId(), frDTO2.getId(), String.valueOf(VoteEnum.UpVote));
//         reviewService.vote(user11.getId(), frDTO3.getId(), String.valueOf(VoteEnum.UpVote));

//         reviewService.vote(user22.getId(), frDTO1.getId(), String.valueOf(VoteEnum.UpVote));
//         reviewService.vote(user22.getId(), frDTO2.getId(), String.valueOf(VoteEnum.DownVote));
//         reviewService.vote(user22.getId(), frDTO3.getId(), String.valueOf(VoteEnum.DownVote));

//         reviewService.vote(user33.getId(), frDTO1.getId(), String.valueOf(VoteEnum.UpVote));
//         reviewService.vote(user33.getId(), frDTO2.getId(), String.valueOf(VoteEnum.UpVote));
//         reviewService.vote(user33.getId(), frDTO3.getId(), String.valueOf(VoteEnum.DownVote));

//         this.reviewService.getRecommendations(user11.getId());

//         assert this.reviewService.getRecommendations(user11.getId()).size() == 1;
//     }
// }
