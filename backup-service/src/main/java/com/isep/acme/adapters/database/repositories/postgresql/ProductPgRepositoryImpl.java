package com.isep.acme.adapters.database.repositories.postgresql;

import com.isep.acme.adapters.database.models.postgresql.ProductPg;
import com.isep.acme.adapters.database.models.postgresql.ReviewPg;
import com.isep.acme.adapters.database.models.postgresql.UserPg;
import com.isep.acme.adapters.database.repositories.DatabaseException;
import com.isep.acme.adapters.database.repositories.postgresql.mappers.ProductMapper;
import com.isep.acme.adapters.database.springRepositories.postgresql.ProductRepositoryPostgresql;
import com.isep.acme.adapters.database.springRepositories.postgresql.ReviewRepositoryPostgresql;
import com.isep.acme.adapters.database.springRepositories.postgresql.UserRepositoryPostgresql;
import com.isep.acme.adapters.database.springRepositories.postgresql.VoteRepositoryPostgresql;
import com.isep.acme.applicationServices.interfaces.repositories.IProductRepository;
import com.isep.acme.domain.aggregates.product.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component("ProductRepositoryCapsule")
@ConditionalOnProperty(value = "spring.repositories.targetPackage", havingValue = "postgresql")
public class ProductPgRepositoryImpl implements IProductRepository {

    private final ProductRepositoryPostgresql productRepository;
    private final ProductMapper mapper;
    private final VoteRepositoryPostgresql voteRepository;
    private final ReviewRepositoryPostgresql reviewRepository;
    private final UserRepositoryPostgresql userRepository;

    @Autowired
    public ProductPgRepositoryImpl(ProductRepositoryPostgresql productRepository, ProductMapper mapper,
                                   VoteRepositoryPostgresql voteRepository,
                                   ReviewRepositoryPostgresql reviewRepository, UserRepositoryPostgresql userRepository) {
        this.productRepository = productRepository;
        this.mapper = mapper;
        this.voteRepository = voteRepository;
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void create(Product product) throws DatabaseException {
        Optional<ProductPg> productById
                = this.productRepository.findById(product.getSku());

        if (productById.isPresent()) {
            throw new DatabaseException("Duplicate id violation.");
        }

        this.productRepository.save(this.mapper.toDatabaseObject(product));
    }

    @Override
    public void update(Product product) throws DatabaseException {
        Optional<ProductPg> matchingProduct = this.productRepository.findById(product.getSku());

        if (matchingProduct.isEmpty()) {
            throw new DatabaseException("Update target does not exist.");
        }

        this.reviewsListUpdate(matchingProduct.get(), this.mapper.reviewsToDomain(matchingProduct.get().getReviews()),
                product.getReviews());
        this.productRepository.save(this.mapper.toDatabaseObject(product));
    }

    @Override
    public Product findBySku(String sku) {
        Optional<ProductPg> product = this.productRepository.findById(sku);
        return product.map(this.mapper::toDomainObject).orElse(null);
    }

    private void reviewsListUpdate(ProductPg product,
                                   List<com.isep.acme.domain.aggregates.product.Review> preStateReviews,
                                   List<com.isep.acme.domain.aggregates.product.Review> postStateReviews) {

        for (com.isep.acme.domain.aggregates.product.Review r : preStateReviews) {
            if (!postStateReviews.contains(r)) { //delete review
                Optional<ReviewPg> databaseReview = this.reviewRepository.findById(r.getId());

                if (databaseReview.isPresent()) {
                    this.voteRepository.deleteAll(databaseReview.get().getVotes());
                    this.reviewRepository.deleteById(r.getId());
                }
            }
        }

        for (com.isep.acme.domain.aggregates.product.Review r : postStateReviews) {
            if (!preStateReviews.contains(r)) { // add review
                Optional<UserPg> user = this.userRepository.findById(r.getUserId());

                if (user.isPresent()) {
                    ReviewPg databaseReview = new ReviewPg(r.getId(), r.getApprovalStatus().name(),
                            r.getText(), r.getReport(), r.getPublishingDate(), r.getFunFact(), r.getRating(),
                            product, user.get(), r.getNumApprovals());
                    this.reviewRepository.save(databaseReview);
                }
            }
        }
    }

    @Override
    public List<Product> findAll() {
        List<ProductPg> databaseProducts =
                (List<ProductPg>) this.productRepository.findAll();

        List<Product> domainProducts = new ArrayList<>();

        for (ProductPg p : databaseProducts) {
            domainProducts.add(this.mapper.toDomainObject(p));
        }
        return domainProducts;
    }

    @Override
    public Product delete(String sku) throws DatabaseException {
        Optional<ProductPg> matchingProductData
                = this.productRepository.findById(sku);

        if (matchingProductData.isPresent()) {
            List<ReviewPg> productReviews = matchingProductData.get().getReviews();

            for (ReviewPg r : productReviews) {
                this.voteRepository.deleteAll(r.getVotes());
            }
            this.reviewRepository.deleteAll(productReviews);
            this.productRepository.delete(matchingProductData.get());
            return this.mapper.toDomainObject(matchingProductData.get());
        } else {
            throw new DatabaseException("Product reference not found");
        }
    }
}
