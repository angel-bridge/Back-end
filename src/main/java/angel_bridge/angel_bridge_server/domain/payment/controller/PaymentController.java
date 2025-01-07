package angel_bridge.angel_bridge_server.domain.payment.controller;

import angel_bridge.angel_bridge_server.domain.payment.dto.request.ConfirmPaymentRequestDto;
import angel_bridge.angel_bridge_server.domain.payment.service.PaymentService;
import angel_bridge.angel_bridge_server.global.common.response.CommonResponse;
import angel_bridge.angel_bridge_server.global.oauth2.dto.CustomOAuth2User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/payments")
@Tag(name = "Toss_Payment", description = "toss 결제 관련 API")
public class PaymentController {

    private final PaymentService paymentService;

    @Operation(summary = "결제 승인", description = "결제 승인 API")
    @PostMapping("/confirm/{educationId}")
    public CommonResponse<Void> confirmPayment(
            @RequestBody ConfirmPaymentRequestDto confirmPaymentRequestDto,
            @PathVariable Long educationId,
            @AuthenticationPrincipal CustomOAuth2User userDetails) throws Exception{

        paymentService.confirmPayment(confirmPaymentRequestDto, userDetails.getMemberId(), educationId);
        return new CommonResponse<>("결제 승인이 완료되었습니다.");
    }
}
