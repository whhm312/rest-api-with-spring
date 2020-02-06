package com.study.restapi.events;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.restapi.commons.TestDescription;

@RunWith(SpringRunner.class)
@SpringBootTest // 모든 빈 등록, 실제 에플리케이션 실행했을때와 가장 유사하게 환경 구성이 됨.
@AutoConfigureMockMvc
public class EventControallerTests {

	@Autowired
	MockMvc mockMvc;
	
	@Autowired
	ObjectMapper objectMapper;

	@Test
	@TestDescription("정상적으로 이벤트를 생성하는 테스트")
	public void createEvent() throws Exception {
		EventDto event = EventDto.builder()
				.name("Spring")
				.description("REST API Development with Spring")
				.beginEnrollmentDateTime(LocalDateTime.of(2020, 2, 5, 14, 30))
				.closeEnrollmentDateTime(LocalDateTime.of(2020, 2, 10, 14, 30))
				.beginEventDateTime(LocalDateTime.of(2020, 2, 15, 14, 30))
				.endEventDateTime(LocalDateTime.of(2020, 2, 15, 20, 00))
				.basePrice(100)
				.maxPrice(200)
				.limitOfEnrollment(100)
				.location("강남역 D2 스타텁 팩토리")
				.build();
		
		mockMvc.perform(post("/api/events/")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaTypes.HAL_JSON)
				.content(objectMapper.writeValueAsString(event))
				)
			.andDo(print())
			.andExpect(status().isCreated())
			.andExpect(jsonPath("id").exists())
			.andExpect(header().exists(HttpHeaders.LOCATION))
			.andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
			.andExpect(jsonPath("id").value(Matchers.not(100)))
			.andExpect(jsonPath("free").value(Matchers.not(true)))
			.andExpect(jsonPath("eventStatus").value(EventStatus.DRAFT.name()))
			;
	}
	
	@Test
	@TestDescription("입력 값이 비어있는 경우에 에러가 발생하는 테스트")
	public void createEventBadRequestEmptyInput() throws Exception {
		
		mockMvc.perform(post("/api/events/")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaTypes.HAL_JSON)
				.content(objectMapper.writeValueAsString(Event.builder().build()))
				)
		.andDo(print())
		.andExpect(status().isBadRequest())
		;
	}
	
	@Test
	@TestDescription("입력 값 외에 항목을 더 입력한 경우에 에러가 발생하는 테스트")
	public void createEventBadRequest() throws Exception {
		Event event = Event.builder()
				.name("Spring")
				.description("REST API Development with Spring")
				.beginEnrollmentDateTime(LocalDateTime.of(2020, 2, 5, 14, 30))
				.closeEnrollmentDateTime(LocalDateTime.of(2020, 2, 10, 14, 30))
				.beginEventDateTime(LocalDateTime.of(2020, 2, 15, 14, 30))
				.endEventDateTime(LocalDateTime.of(2020, 2, 15, 20, 00))
				.basePrice(100)
				.maxPrice(200)
				.limitOfEnrollment(100)
				.location("강남역 D2 스타텁 팩토리")
				.free(true)
				.offline(false)
				.eventStatus(EventStatus.PUBLISHED)
				.build()
				;
		
		mockMvc.perform(post("/api/events/")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaTypes.HAL_JSON)
				.content(objectMapper.writeValueAsString(event))
				)
		.andDo(print())
		.andExpect(status().isBadRequest())
		;
	}
	
	@Test
	@TestDescription("입력 값이 잘못 입력되어 있는 경우에 에러가 발생하는 테스트")
	public void createEventBadRequestEmpyInput() throws Exception {
		EventDto eventDto = EventDto.builder()
				.name("Spring")
				.description("REST API Development with Spring")
				.beginEnrollmentDateTime(LocalDateTime.of(2020, 2, 15, 14, 30))
				.closeEnrollmentDateTime(LocalDateTime.of(2020, 2, 1, 14, 30))
				.beginEventDateTime(LocalDateTime.of(2020, 2, 15, 14, 30))
				.endEventDateTime(LocalDateTime.of(2020, 2, 15, 20, 00))
				.basePrice(1000000)
				.maxPrice(200)
				.limitOfEnrollment(100)
				.location("강남역 D2 스타텁 팩토리")
				.build();
		
		this.mockMvc.perform(post("/api/events")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(this.objectMapper.writeValueAsString(eventDto)))
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$[0].objectName").exists())
			.andExpect(jsonPath("$[0].defaultMessage").exists())
			.andExpect(jsonPath("$[0].code").exists())

			//.andExpect(jsonPath("$[0].field").exists())
			//.andExpect(jsonPath("$[0].rejetedValue").exists())
			;
	}
}
