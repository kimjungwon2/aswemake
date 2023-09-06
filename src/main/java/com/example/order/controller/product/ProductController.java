package com.example.order.controller.product;

import com.example.order.controller.product.dto.ProductCreateRequest;
import com.example.order.service.product.ProductService;
import com.example.order.service.product.response.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping("/product/new")
    public ProductResponse createProduct(@Validated @RequestBody ProductCreateRequest request){
        return productService.createProduct(request);
    }

}
