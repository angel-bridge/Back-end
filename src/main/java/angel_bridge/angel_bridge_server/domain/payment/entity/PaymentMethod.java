package angel_bridge.angel_bridge_server.domain.payment.entity;

import angel_bridge.angel_bridge_server.global.exception.ApplicationException;
import lombok.Getter;
import lombok.ToString;

import static angel_bridge.angel_bridge_server.global.exception.ExceptionCode.NOT_FOUND_PAYMENT_METHOD;

@Getter
@ToString
public enum PaymentMethod {

    VIRTUAL_ACCOUNT("가상계좌"),
    SIMPLE_PAYMENT("간편결제"),
    GAME_CULTURE_GIFT_CARD("게임문화상품권"),
    BANK_TRANSFER("계좌이체"),
    BOOK_CULTURE_GIFT_CARD("도서문화상품권"),
    CULTURE_GIFT_CARD("문화상품권"),
    CREDIT_CARD("카드"),
    MOBILE_PAYMENT("휴대폰");

    private final String description;

    PaymentMethod(String description) {
        this.description = description;
    }

    public static PaymentMethod fromDescription(String description) {
        for (PaymentMethod method : PaymentMethod.values()) {
            if (method.getDescription().equals(description))
                return method;
        }
        throw new ApplicationException(NOT_FOUND_PAYMENT_METHOD);
    }
}
