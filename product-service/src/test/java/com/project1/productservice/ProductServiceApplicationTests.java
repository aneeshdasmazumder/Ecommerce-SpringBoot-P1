package com.project1.productservice;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project1.productservice.dto.ProductRequest;

@SpringBootTest
@AutoConfigureMockMvc
class ProductServiceApplicationTests {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	
	@Test
	void createTestProducts() throws Exception {
		
		ProductRequest productRequest = getProductRequest();
		
		System.out.println(productRequest);
		
		String productRequestString = objectMapper.writeValueAsString(productRequest);
		
		mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
							.contentType(MediaType.APPLICATION_JSON)
							.content(productRequestString))
		.andExpectAll(
			       status().isCreated());
				
		
	}

	private ProductRequest getProductRequest() {
		return ProductRequest.builder()
				.name("Iphone 12")
				.description("Iphone 12")
				.price(BigDecimal.valueOf(50000))
				.build();
		
	}

}
