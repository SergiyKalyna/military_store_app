package com.militarystore.product;

import com.militarystore.entity.product.Product;
import com.militarystore.exception.MsNotFoundException;
import com.militarystore.port.in.product.ProductUseCase;
import com.militarystore.port.out.basket.BasketPort;
import com.militarystore.port.out.product.ProductFeedbackPort;
import com.militarystore.port.out.product.ProductPort;
import com.militarystore.port.out.product.ProductRatePort;
import com.militarystore.port.out.product.ProductStockDetailsPort;
import com.militarystore.port.out.wishlist.WishlistPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.militarystore.product.ProductValidator.validateProduct;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService implements ProductUseCase {

    private final ProductPort productPort;
    private final ProductStockDetailsPort productStockDetailsPort;
    private final ProductRatePort productRatePort;
    private final ProductFeedbackPort productFeedbackPort;
    private final WishlistPort wishlistPort;
    private final BasketPort basketPort;

    @Override
    public Integer addProduct(Product product) {
        validateProduct(product);

        var productId = productPort.addProduct(product);
        productStockDetailsPort.addProductStockDetails(productId, product.stockDetails());

        log.info("New product was added with id - {}", productId);
        return productId;
    }

    @Override
    public Product getProductById(Integer productId,  Integer userId) {
        checkProductExisting(productId);

        var product = productPort.getProductById(productId);
        var stockDetails = productStockDetailsPort.getProductStockDetailsByProductId(productId);
        var avgRate = productRatePort.getAverageRateByProductId(productId);
        var feedbacks = productFeedbackPort.getFeedbacksByProductId(productId);
        var isProductInUserWishlist = wishlistPort.isProductInUserWishlist(productId, userId);

        return product
            .stockDetails(stockDetails)
            .avgRate(avgRate)
            .feedbacks(feedbacks)
            .isProductInUserWishlist(isProductInUserWishlist)
            .build();
    }

    @Override
    public List<Product> getProductsBySubcategoryId(Integer subcategoryId) {
        return productPort.getProductsBySubcategoryId(subcategoryId);
    }

    @Override
    public List<Product> getProductsByName(String productName) {
        return productPort.getProductsByName(productName);
    }

    @Override
    public void deleteProduct(Integer productId) {
        checkProductExisting(productId);

        basketPort.deleteProductFromAllBaskets(productId);
        productStockDetailsPort.deleteProductStockDetails(productId);
        wishlistPort.deleteProductFromWishlist(productId);
        productRatePort.deleteRate(productId);
        productFeedbackPort.deleteFeedbacksByProductId(productId);
        productPort.deleteProduct(productId);

        log.info("Product with id - '{}' was deleted ", productId);
    }

    @Override
    public void updateProduct(Product product) {
        checkProductExisting(product.id());
        validateProduct(product);

        productPort.updateProduct(product);
        productStockDetailsPort.updateProductStockDetails(product.stockDetails());

        log.info("Product with id - '{}' was updated", product.id());
    }

    private void checkProductExisting(Integer productId) {
        if (!productPort.isProductExist(productId)) {
            throw new MsNotFoundException("Product does not exist with id: " + productId);
        }
    }
}
