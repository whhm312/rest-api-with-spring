package com.study.restapi.commons;

import java.io.IOException;

import org.springframework.boot.jackson.JsonComponent;
import org.springframework.validation.Errors;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

@JsonComponent
public class ErrosSerializer extends JsonSerializer<Errors> {

	@Override
	public void serialize(Errors errors, JsonGenerator gen, SerializerProvider serializers) throws IOException {
		gen.writeStartArray();
		errors.getFieldErrors().stream().forEach(e -> {
			try {
				gen.writeStartObject();
				gen.writeStringField("field", e.getField());
				gen.writeStringField("objectName", e.getCode());
				gen.writeStringField("code", e.getCode());
				gen.writeStringField("defaultMessage", e.getDefaultMessage());

				Object rejectedValue = e.getRejectedValue();
				if (rejectedValue != null) {
					gen.writeStringField("rejectValue", rejectedValue.toString());
				}
				gen.writeEndObject();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		});
		
		errors.getGlobalErrors().forEach(e -> {
			try {
				gen.writeStartObject();
				gen.writeStringField("objectName", e.getCode());
				gen.writeStringField("code", e.getCode());
				gen.writeStringField("defaultMessage", e.getDefaultMessage());
				gen.writeEndObject();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		});
		gen.writeEndArray();

	}

}
