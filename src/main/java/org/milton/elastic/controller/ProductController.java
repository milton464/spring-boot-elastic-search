package org.milton.elastic.controller;

import java.io.IOException;
import java.util.List;

import org.milton.elastic.model.Product;
import org.milton.elastic.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 
 * @author MILTON
 *
 */
@RestController
@RequestMapping("product")
public class ProductController {

	@Autowired
	private ProductRepository productRepository;

	@GetMapping("/all")
	public ResponseEntity<List<Product>> getAllProducts() throws IOException {
		List<Product> products = productRepository.findAll();
		return new ResponseEntity<>(products, HttpStatus.OK);
	}

	@PostMapping("/add")
	public ResponseEntity<String> createOrUpdate(@RequestBody Product product) throws IOException {
		String message = productRepository.createOrUpdateDocument(product);
		return new ResponseEntity<>(message, HttpStatus.CREATED);
	}

	@GetMapping("/{productId}")
	public ResponseEntity<Product> getProductById(@PathVariable String productId) throws IOException{
		Product product =  productRepository.getDocument(productId);
		return new ResponseEntity<>(product, HttpStatus.OK);
		
	}
	
}
