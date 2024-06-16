package com.militarystore.product;

import com.militarystore.entity.product.Product;
import com.militarystore.entity.product.ProductDetails;
import com.militarystore.exception.MsNotFoundException;
import com.militarystore.port.in.image.ImageUseCase;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.militarystore.product.ProductValidator.validateProduct;
import static java.util.Objects.nonNull;

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
    private final ImageUseCase imageUseCase;

    @Override
    public Integer addProduct(Product product, List<MultipartFile> images) {
        validateProduct(product);

        var productId = productPort.addProduct(product);
        productStockDetailsPort.addProductStockDetails(productId, product.stockDetails());
        imageUseCase.saveProductImages(productId, images);

        log.info("New product was added with id - {}", productId);
        return productId;
    }

    @Override
    public ProductDetails getProductById(Integer productId,  Integer userId) {
        checkProductExisting(productId);

        var product = getProduct(productId, userId);
        var images = imageUseCase.downloadProductImages(productId);

        return ProductDetails.builder()
            .product(product)
            .images(images)
            .build();
    }

    @Override
    public List<ProductDetails> getProductsBySubcategoryId(Integer subcategoryId) {
        var products = productPort.getProductsBySubcategoryId(subcategoryId);

        return getProductDetails(products);
    }

    @Override
    public List<ProductDetails> getProductsByName(String productName) {
        var products = productPort.getProductsByName(productName);

        return getProductDetails(products);
    }

    @Override
    public void deleteProduct(Integer productId) {
        checkProductExisting(productId);

        basketPort.deleteProductFromAllBaskets(productId);
        productStockDetailsPort.deleteProductStockDetails(productId);
        wishlistPort.deleteProductFromWishlist(productId);
        productRatePort.deleteRate(productId);
        productFeedbackPort.deleteFeedbacksByProductId(productId);
        imageUseCase.deleteProductImages(productId);
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

    private Product getProduct(Integer productId, Integer userId) {
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

    private List<ProductDetails> getProductDetails(List<Product> products) {
        var productIds = products.stream().map(Product::id).toList();
        var primaryImages = imageUseCase.getPrimaryProductsImages(productIds);

        return products.stream()
            .map(product -> {
                var image = primaryImages.get(product.id());
                return ProductDetails.builder()
                    .product(product)
                    .images(nonNull(image) ? List.of(image) : List.of())
                    .build();
            })
            .toList();
    }
}
