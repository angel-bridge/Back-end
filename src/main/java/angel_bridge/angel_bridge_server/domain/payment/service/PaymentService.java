package angel_bridge.angel_bridge_server.domain.payment.service;

import angel_bridge.angel_bridge_server.domain.education.entity.Education;
import angel_bridge.angel_bridge_server.domain.enrollment.entity.Enrollment;
import angel_bridge.angel_bridge_server.domain.enrollment.entity.EnrollmentStatus;
import angel_bridge.angel_bridge_server.domain.member.entity.Member;
import angel_bridge.angel_bridge_server.domain.payment.dto.request.ConfirmPaymentRequestDto;
import angel_bridge.angel_bridge_server.domain.payment.dto.response.ConfirmPaymentResponseDto;
import angel_bridge.angel_bridge_server.domain.payment.entity.TossPayment;
import angel_bridge.angel_bridge_server.global.exception.ApplicationException;
import angel_bridge.angel_bridge_server.global.repository.EducationRepository;
import angel_bridge.angel_bridge_server.global.repository.EnrollmentRepository;
import angel_bridge.angel_bridge_server.global.repository.MemberRepository;
import angel_bridge.angel_bridge_server.global.repository.TossPaymentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static angel_bridge.angel_bridge_server.global.exception.ExceptionCode.*;

@Service
public class PaymentService {

    private final WebClient webClient;
    private final EnrollmentRepository enrollmentRepository;
    private final MemberRepository memberRepository;
    private final EducationRepository educationRepository;
    private final TossPaymentRepository tossPaymentRepository;

    @Autowired
    public PaymentService(WebClient.Builder webClientBuilder,
                          EnrollmentRepository enrollmentRepository,
                          MemberRepository memberRepository,
                          EducationRepository educationRepository,
                          TossPaymentRepository tossPaymentRepository) {
        this.webClient = webClientBuilder.build();
        this.enrollmentRepository = enrollmentRepository;
        this.memberRepository = memberRepository;
        this.educationRepository = educationRepository;
        this.tossPaymentRepository = tossPaymentRepository;
    }

    // 결제 승인
    @Transactional
    public void confirmPayment(ConfirmPaymentRequestDto confirmPaymentRequestDto, Long memberId, Long educationId) throws Exception {

        String data = webClient.post()
                .uri("/confirm")
                .bodyValue(confirmPaymentRequestDto)
                .retrieve()
                .onStatus(status -> status.value() != 200, clientResponse -> {
                    return Mono.error(new ApplicationException(PAYMENT_API_FAIL));
                })
                .bodyToMono(String.class)
                .block();

        if (data != null) {
            ObjectMapper objectMapper = new ObjectMapper();
            ConfirmPaymentResponseDto response = objectMapper.readValue(data, ConfirmPaymentResponseDto.class);
            savePayment(response, memberId, educationId);
        } else {
            throw new ApplicationException(PAYMENT_API_FAIL);
        }
    }

    @Transactional
    public void savePayment(ConfirmPaymentResponseDto response, Long memberId, Long educationId) {

        // enrollment 생성
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ApplicationException(NOT_FOUND_USER));
        Education education = educationRepository.findById(educationId)
                .orElseThrow(() -> new ApplicationException(NOT_FOUND_EDUCATION_ID));
        Enrollment enrollment = Enrollment.builder()
                .member(member)
                .education(education)
                .isPaid(true)
                .enrollmentStatus(
                        EnrollmentStatus.getStatusFromEducationStartDate(
                                education.getEducationStartDate(),
                                education.getEducationEndDate()))
                .build();
        enrollmentRepository.save(enrollment);

        // payment 생성
        TossPayment tossPayment = TossPayment.builder()
                .enrollment(enrollment)
                .tossOrderId(response.orderId())
                .tossPaymentKey(response.paymentKey())
                .tossPaymentMethod(response.getPaymentMethod())
                .tossPaymentStatus(response.getPaymentStatus())
                .totalAmount(response.totalAmount())
                .requestAt(response.getRequestedAt())
                .approvedAt(response.getApprovedAt())
                .build();
        tossPaymentRepository.save(tossPayment);
    }
}
