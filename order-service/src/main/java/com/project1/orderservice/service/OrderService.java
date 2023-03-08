package com.project1.orderservice.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	
	public void placeOrder(OrderRequest orderRequest) {
		Order order = new Order();
		order.setOrderNumber(UUID.randomUUID().toString());
		log.info(orderRequest.toString());
		List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDto()
			.stream()
			.map(orderLineItemsDto -> mapToDto(orderLineItemsDto))
			.toList();
		log.info(orderLineItems.toString());
		order.setOrderLineItems(orderLineItems);
		
		orderRepo.save(order);
	}
	
	private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {
		OrderLineItems orderLineItems = new OrderLineItems();
		orderLineItems.setPrice(orderLineItemsDto.getPrice());
		orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
		orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
		return orderLineItems;
	}
	
}
