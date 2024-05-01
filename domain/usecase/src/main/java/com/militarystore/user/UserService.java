package com.militarystore.user;

import com.militarystore.entity.user.User;
import com.militarystore.exception.MsNotFoundException;
import com.militarystore.exception.MsValidationException;
import com.militarystore.port.in.user.CreateUserUseCase;
import com.militarystore.port.in.user.DeleteUserUseCase;
import com.militarystore.port.in.user.GetUserUseCase;
import com.militarystore.port.out.basket.BasketPort;
import com.militarystore.port.out.product.ProductFeedbackPort;
import com.militarystore.port.out.product.ProductRatePort;
import com.militarystore.port.out.user.UserPort;
import com.militarystore.port.out.wishlist.WishlistPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements CreateUserUseCase, GetUserUseCase, DeleteUserUseCase {

    private final UserPort userPort;
    private final UserValidationService userValidationService;
    private final ProductRatePort productRatePort;
    private final ProductFeedbackPort productFeedbackPort;
    private final WishlistPort wishlistPort;
    private final BasketPort basketPort;

    public Integer saveUser(User user) {
        checkIfLoginExist(user.login());
        userValidationService.validateNewUser(user);

        var userId = userPort.saveUser(user);
        log.info("User with login '{}' was created with id '{}'", user.login(), userId);

        return userId;
    }

    public void deleteUser(int userId) {
        checkIfUserExist(userId);

        productFeedbackPort.deleteFeedbacksByUserId(userId);
        productRatePort.deleteRatesByUserId(userId);
        basketPort.deleteUserProductsFromBasket(userId);
        wishlistPort.deleteAllUserProductsFromWishlist(userId);
        userPort.deleteUser(userId);

        log.info("User with id '{}' was deleted", userId);
    }

    public User getUserById(int userId) {
        var user = userPort.getUserById(userId);

        if (isNull(user)) {
            throw new MsNotFoundException(String.format("User with id [%d] is not found", userId));
        }

        return user;
    }

    public List<User> getUsers() {
        return userPort.getUsers();
    }

    void checkIfUserExist(int userId) {
        var isUserExist = userPort.isUserExist(userId);
        if (!isUserExist) {
            throw new MsNotFoundException(String.format("User with id [%d] is not found", userId));
        }
    }

    private void checkIfLoginExist(String login) {
        var isLoginExists = userPort.isLoginExists(login);
        if (isLoginExists) {
            throw new MsValidationException("User with login [" + login + "] is already exists");
        }
    }
}
