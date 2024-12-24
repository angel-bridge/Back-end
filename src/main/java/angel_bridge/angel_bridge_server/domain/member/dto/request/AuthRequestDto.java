package angel_bridge.angel_bridge_server.domain.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record AuthRequestDto(

        @NotNull
        @Email
        @Schema(description = "이메일", example = "angelbridge@gmail.com")
        String email,

        @NotNull
        @Pattern(regexp = "^010-[0-9]{4}-[0-9]{4}$")
        @Schema(description = "전화번호", example = "010-1234-1234")
        String phoneNumber,

        @Schema(description = "이메일 수신 동의 여부", defaultValue = "false")
        Boolean isSelect
) {
}
