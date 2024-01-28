package org.milton.elastic.model;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class Contact {

	@Field(type = FieldType.Text, name = "id")
	private String id;

	@Field(type = FieldType.Text, name = "user_id")
	private BigInteger userId;

	@Field(type = FieldType.Text, name = "first_name")
	private String firstName;

	@Field(type = FieldType.Text, name = "last_name")
	private String lastName;

	@Field(type = FieldType.Text, name = "number")
	private String number;

	@Field(type = FieldType.Text, name = "email")
	private String email;

	@Field(type = FieldType.Text, name = "address")
	private String address;
	
	@Field(type = FieldType.Byte, name = "status")
	private Byte status;

}
