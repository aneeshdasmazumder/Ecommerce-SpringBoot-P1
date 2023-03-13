package com.project1.orderservice.service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;

import com.project1.orderservice.dto.InventoryResponse;
import com.project1.orderservice.dto.OrderLineItemsDto;
import com.project1.orderservice.dto.OrderRequest;
import com.project1.orderservice.model.Order;
import com.project1.orderservice.model.OrderLineItems;
import com.project1.orderservice.repository.Orderrepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class OrderService {
	
	private final Orderrepository orderRepo;
	
	private final WebClient.Builder webClientBuilder;
	
	public void placeOrder(OrderRequest orderRequest) {
		Order order = new Order();
		order.setOrderNumber(UUID.randomUUID().toString());
		log.info(orderRequest.toString());
		List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDto()
			.stream()
			.map(orderLineItemsDto -> mapToDto(orderLineItemsDto))
			.toList();
		log.info(orderLineItems.toString());
		order.setOrderLineItemsList(orderLineItems);
		
		List<String> skuCodes =  order.getOrderLineItemsList().stream()
					.map(OrderLineItems::getSkuCode)
					.toList();
		
		log.info("skuCodes {} ",skuCodes);
		
		InventoryResponse[] inventoryResponseArray = webClientBuilder.build().get()
				.uri("http://inventory-service/api/inventory",
						uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
				.retrieve()
				.bodyToMono(InventoryResponse[].class)
				.block();
		
		boolean allProductsInStock = Arrays.stream(inventoryResponseArray)
				.allMatch(InventoryResponse::getIsInStock);
		
		if(allProductsInStock)
			orderRepo.save(order);
		else
			throw new IllegalArgumentException("Product is not in stock, please try again later");
		
		
	}
	
	private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {
		OrderLineItems orderLineItems = new OrderLineItems();
		orderLineItems.setPrice(orderLineItemsDto.getPrice());
		orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
		orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
		return orderLineItems;
	}
	
}
