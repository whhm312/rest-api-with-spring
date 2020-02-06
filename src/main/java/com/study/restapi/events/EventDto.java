package com.study.restapi.events;

import java.time.LocalDateTime;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 입력받는 dto 분리해서 값 체크
 * 장점 : 도메인 안에 너무 많은 어노테이션으로 혼란/복잡/헷갈림
 * 단점 : 중복 생김
 * @author hyemin
 *
 */
@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class EventDto {

	@NotEmpty
	private String name;
	@NotEmpty
	private String description;
	@NotNull
	private LocalDateTime beginEnrollmentDateTime;
	@NotNull
	private LocalDateTime closeEnrollmentDateTime;
	@NotNull
	private LocalDateTime beginEventDateTime;
	@NotNull
	private LocalDateTime endEventDateTime;
	private String location; // (optional) 이게 없으면 온라인 모임
	@Min(0)
	private int basePrice; // (optional)
	@Min(0)
	private int maxPrice; // (optional)
	@Min(0)
	private int limitOfEnrollment;
	
}
