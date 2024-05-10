package com.militarystore.user;

import com.militarystore.entity.user.User;
import com.militarystore.exception.MsNotFoundException;
import com.militarystore.exception.MsValidationException;
import com.militarystore.port.out.basket.BasketPort;
import com.militarystore.port.out.discount.DiscountPort;
import com.militarystore.port.out.product.ProductFeedbackPort;
import com.militarystore.port.out.product.ProductRatePort;
import com.militarystore.port.out.user.UserPort;
import com.militarystore.port.out.wishlist.WishlistPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    private static final int USER_ID = 1;
    private static final User USER = User.builder().id(USER_ID).login("login").password("password").build();

    @Mock
    private UserValidationService userValidationService;

    @Mock
    private UserPort userPort;

    @Mock
    private ProductFeedbackPort productFeedbackPort;

    @Mock
    private ProductRatePort productRatePort;

    @Mock
    private WishlistPort wishlistPort;

    @Mock
    private BasketPort basketPort;

    @Mock
    private DiscountPort discountPort;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(
            userPort,
            userValidationService,
            productRatePort,
            productFeedbackPort,
            wishlistPort,
            basketPort,
            discountPort,
            bCryptPasswordEncoder
        );
    }

    @Test
    void saveUser_whenLoginAlreadyExist_shouldTrowException() {
        when(userPort.isLoginExists("login")).thenReturn(true);

        assertThatCode(() -> userService.saveUser(USER))
            .isInstanceOf(MsValidationException.class)
            .hasMessageContaining("User with login [login] is already exist");
    }

    @Test
    void saveUser_whenUserIsInvalid_shouldTrowException() {
        when(userPort.isLoginExists("login")).thenReturn(false);
        doThrow(MsValidationException.class).when(userValidationService).validateNewUser(USER);

        assertThatCode(() -> userService.saveUser(USER))
            .isInstanceOf(MsValidationException.class);
    }

    @Test
    void saveUser_whenUserIsValid_shouldSaveUser() {
        var encodedPassword = "encodedPassword";

        when(userPort.isLoginExists("login")).thenReturn(false);
        when(bCryptPasswordEncoder.encode("password")).thenReturn(encodedPassword);
        when(userPort.saveUser(USER, encodedPassword)).thenReturn(USER_ID);

        var userId = userService.saveUser(USER);

        assertThat(userId).isEqualTo(USER_ID);
    }

    @Test
    void deleteUser() {
        when(userPort.isUserExist(USER_ID)).thenReturn(true);

        userService.deleteUser(USER_ID);

        verify(productFeedbackPort).deleteFeedbacksByUserId(USER_ID);
        verify(discountPort).deleteUserDiscounts(USER_ID);
        verify(productRatePort).deleteRatesByUserId(USER_ID);
        verify(wishlistPort).deleteAllUserProductsFromWishlist(USER_ID);
        verify(basketPort).deleteUserProductsFromBasket(USER_ID);
        verify(userPort).deleteUser(USER_ID);
    }

    @Test
    void deleteUser_whenUserDoesntExist_shouldThrowException() {
        when(userPort.isUserExist(USER_ID)).thenReturn(false);

        assertThatCode(() -> userService.deleteUser(USER_ID))
            .isInstanceOf(MsNotFoundException.class)
            .hasMessageContaining("User with id [" + USER_ID + "] is not found");
    }

    @Test
    void getUserById() {
        when(userPort.getUserById(USER_ID)).thenReturn(USER);

        var user = userService.getUserById(USER_ID);

        assertThat(user).isEqualTo(USER);
    }

    @Test
    void getUserById_whenUserDoesntExist_shouldThrowException() {
        when(userPort.getUserById(USER_ID)).thenReturn(null);

        assertThatCode(() -> userService.getUserById(USER_ID))
            .isInstanceOf(MsNotFoundException.class)
            .hasMessageContaining("User with id [" + USER_ID + "] is not found");
    }

    @Test
    void getUsers() {
        var users = List.of(USER);

        when(userPort.getUsers()).thenReturn(users);

        assertThat(userService.getUsers()).isEqualTo(users);
    }

    @Test
    void getUsers_whenReturnsEmptyList() {
        List<User> users = List.of();

        when(userPort.getUsers()).thenReturn(users);

        assertThat(userService.getUsers()).isEqualTo(users);
    }
}