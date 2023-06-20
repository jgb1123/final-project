package com.solo.delivery.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderPostDto {
    @Valid
    private List<OrderDetailPostDto> orderDetails;

    private String address;

    @Pattern(regexp = "^\\d{3}-\\d{3,4}-\\d{4}$",
            message = "휴대폰 번호는 10~11자의 숫자와 '-'로 구성되어야 합니다")
    private String phone;

    @Pattern(regexp = "^[가-힣]+$",
            message = "이름은 한글만 가능합니다.")
    private String name;

    @Size(min = 5, max = 50, message = "요구사항은 5자이상 50자 이하이어야 합니다.")
    private String requirement;
}
