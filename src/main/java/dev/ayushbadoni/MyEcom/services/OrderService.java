package dev.ayushbadoni.MyEcom.services;

import dev.ayushbadoni.MyEcom.dto.OrderResponse;
import dev.ayushbadoni.MyEcom.entities.User;
import dev.ayushbadoni.MyEcom.dto.*;
import dev.ayushbadoni.MyEcom.entities.*;
import dev.ayushbadoni.MyEcom.repositories.OrderRepository;
import jakarta.transaction.Transactional;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class OrderService {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    ProductService productService;

    @Autowired
    RazorpayService razorpayService;

    @Transactional
    public OrderResponse createOrder(OrderRequest orderRequest, Principal loggedInUser) throws Exception {
        User user = (User) userDetailsService.loadUserByUsername(loggedInUser.getName());
        Address address = user.getAddressList().stream()
                .filter(address1 -> orderRequest.getAddressId().equals(address1.getId())).findFirst()
                .orElseThrow(BadRequestException::new);

        Order order = Order.builder()
                .user(user)
                .address(address)
                .totalAmount(orderRequest.getTotalAmount())
                .orderDate(orderRequest.getOrderDate())
                .expectedDeliveryDate(orderRequest.getExpectedDeliveryDate())
                .paymentMethod(orderRequest.getPaymentMethod())
                .orderStatus(OrderStatus.PENDING)
                .build();

        List<OrderItem> orderItems = orderRequest.getOrderItemRequests().stream().map(orderItemRequest -> {
            try {
                Product product = productService.fetchProductById(orderItemRequest.getProductId());
                ProductVariant variant = product.getProductVariants().stream()
                        .filter(v -> v.getId().equals(orderItemRequest.getProductVariantId()))
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("Variant not found"));
//                OrderItem orderItem =

                return OrderItem.builder()
                        .product(product)
                        .productVariantId(orderItemRequest.getProductVariantId())
                        .quantity(orderItemRequest.getQuantity())
                        .itemPrice(product.getPrice().doubleValue())
                        .order(order)
                        .build();

            } catch (Exception e) {
                throw new RuntimeException(e);//ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid product or variant", e);
            }
        }).toList();

        order.setOrderItemList(orderItems);

        Payment payment = Payment.builder()
                .paymentStatus(PaymentStatus.PENDING)
                .paymentDate(new Date())
                .order(order)
                .amount(order.getTotalAmount())
                .paymentMethod(order.getPaymentMethod())
                .build();

        order.setPayment(payment);

        Order savedOrder = orderRepository.save(order);

        OrderResponse orderResponse = OrderResponse.builder()
                .paymentMethod(orderRequest.getPaymentMethod())
                .orderId(savedOrder.getId())
                .message("Order create successfully")
                .build();

        if ("CARD".equalsIgnoreCase(orderRequest.getPaymentMethod())){
            Map<String,String> razorpayOrder = razorpayService.createRazorpayOrder(order.getTotalAmount());
            orderResponse.setRazorpayOrderId(razorpayOrder.get("razorpayOrderId"));
            orderResponse.setCurrency(razorpayOrder.get("currency"));

            //converting paise to rupee
            double amountInRupees = Double.parseDouble(razorpayOrder.get("amount"));
            orderResponse.setAmount(String.valueOf(amountInRupees/100));
        }
        return orderResponse;

    }


    public List<OrderDetails> getOrderByUser(String name) {
        User user = (User) userDetailsService.loadUserByUsername(name);
        List<Order> orders = orderRepository.findByUser(user);

//        return orders.stream().map(order ->{
//            return OrderDetails.builder()
//                    .id(order.getId())
//                    .orderDate(order.getOrderDate())
//                    .orderStatus(order.getOrderStatus())
//                    .address(mapToAddressRequest(order.getAddress()))
//                    .totalAmount(order.getTotalAmount())
//                    .shipmentNumber(order.getShipmentTrackingNumber())
//                    .orderItemList(getItemDetails(order.getOrderItemList()))
//                    .expectedDelivery(order.getExpectedDeliveryDate())
//                    .build();
//        } ).toList();
        return orders.stream()
                .filter(order -> order.getAddress() != null) // skip orders with null address
                .map(order -> {
                    return OrderDetails.builder()
                            .id(order.getId())
                            .orderDate(order.getOrderDate())
                            .orderStatus(order.getOrderStatus())
                            .address(mapToAddressRequest(order.getAddress()))
                            .totalAmount(order.getTotalAmount())
                            .shipmentNumber(order.getShipmentTrackingNumber())
                            .orderItemList(getItemDetails(order.getOrderItemList()))
                            .expectedDelivery(order.getExpectedDeliveryDate())
                            .build();
                }).toList();
    }

    private AddressRequest mapToAddressRequest(Address address) {
        return AddressRequest.builder()
                .name(address.getName())
                .street(address.getStreet())
                .city(address.getCity())
                .state(address.getState())
                .zipCode(address.getZipCode())
                .phoneNumber(address.getPhoneNumber())
                .build();
    }

    private List<OrderItemDetails> getItemDetails(List<OrderItem> orderItemList) {

        return orderItemList.stream().map(orderItem -> {
            return OrderItemDetails.builder()
                    .id(orderItem.getId())
                    .itemPrice(orderItem.getItemPrice())
                    .productVariantId(orderItem.getProductVariantId())
                    .product(orderItem.getProduct())
                    .quantity(orderItem.getQuantity())
                    .build();
        }).toList();
    }



    public void cancelOrder(UUID id, Principal loggedInUser) {
        User user = (User) userDetailsService.loadUserByUsername(loggedInUser.getName());
        Order order = orderRepository.findById(id).get();
        if (null != order && order.getUser().getId().equals(user.getId())){
            order.setOrderStatus(OrderStatus.CANCELLED);
            orderRepository.save(order);
        }else {
            new RuntimeException("Invalid Request");
        }
    }

    @Value("${razorpay.key_secret}")
    private String razorpaySecret;

    public OrderResponse updatePaymentStatus(PaymentStatusUpdateRequest request) {
        boolean isValid = razorpayService.verifySignature(
                request.getRazorpayOrderId(),
                request.getRazorpayPaymentId(),
                request.getRazorpaySignature(),
                razorpaySecret
        );
        if (!isValid) {
            throw new RuntimeException("Invalid Razorpay credentials");
        }

        Order order = orderRepository.findByRazorpayOrderId(request.getRazorpayOrderId()).orElseThrow(() -> new RuntimeException("Order Not found"));

        Payment payment = order.getPayment();
        payment.setPaymentStatus(PaymentStatus.COMPLETED);
        payment.setPaymentDate(new Date());
        payment.setPaymentMethod("CARD");

        order.setOrderStatus(OrderStatus.IN_PROGRESS);
        order.setPayment(payment);


        Order updatedOrder = orderRepository.save(order);

        return OrderResponse.builder()
                .orderId(updatedOrder.getId())
                .paymentMethod(updatedOrder.getPaymentMethod())
                .message("Payment status updated successfully")
                .build();
    }
}
