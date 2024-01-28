package org.milton.elastic.model;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
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
@Document(indexName = "products")
public class Product {

	@Field(type = FieldType.Text, name = "id")
	private String id;

	@Field(type = FieldType.Text, name = "name")
	private String name;

	@Field(type = FieldType.Text, name = "category")
	private String category;

	@Field(type = FieldType.Double, name = "price")
	private double price;

	@Field(type = FieldType.Boolean, name = "is_stock")
	private boolean isStock;
}
