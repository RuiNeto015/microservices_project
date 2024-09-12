package com.isep.acme.adapters.controllers.http;

import com.isep.acme.adapters.controllers.ProductController;
import com.isep.acme.adapters.database.repositories.DatabaseException;
import com.isep.acme.adapters.dto.FetchProductDTO;
import com.isep.acme.domain.enums.ApprovalStatusEnum;
import org.springframework.http.HttpMethod;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class HateoasUtils {

    public static void addLinks(FetchProductDTO response) {
        try {
            response.add(linkTo(methodOn(ProductController.class)
                    .delete(response.getSku()))
                    .withRel("delete")
                    .withType(HttpMethod.DELETE.name()));

            response.add(linkTo(methodOn(ProductController.class)
                    .getCatalog())
                    .withSelfRel()
                    .withType(HttpMethod.GET.name()));

            if (!response.getApprovalStatus().equals(ApprovalStatusEnum.Pending.toString())) {
                response.add(linkTo(methodOn(ProductController.class)
                        .approve(response.getSku()))
                        .withRel("approve")
                        .withType(HttpMethod.PUT.name()));

                response.add(linkTo(methodOn(ProductController.class)
                        .reject(response.getSku(), "Reject"))
                        .withRel("reject")
                        .withType(HttpMethod.PUT.name()));
            }

            response.add(linkTo(methodOn(ProductController.class)
                    .create(null))
                    .withRel("create")
                    .withType(HttpMethod.POST.name()));

            response.add(linkTo(methodOn(ProductController.class)
                    .getAllByDesignation(null))
                    .withRel("getAllByDesignation")
                    .withType(HttpMethod.GET.name()));

            response.add(linkTo(methodOn(ProductController.class)
                    .getCatalog())
                    .withRel("getAll")
                    .withType(HttpMethod.GET.name()));

        } catch (DatabaseException ignored) {
        }
    }

    public static void addLinks(List<FetchProductDTO> response) {
        response.forEach(HateoasUtils::addLinks);
    }

}

