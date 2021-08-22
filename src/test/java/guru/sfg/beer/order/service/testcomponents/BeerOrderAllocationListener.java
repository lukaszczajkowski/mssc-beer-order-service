package guru.sfg.beer.order.service.testcomponents;

import guru.sfg.beer.order.service.config.JmsConfig;
import guru.sfg.brewery.model.events.AllocateOrderRequest;
import guru.sfg.brewery.model.events.AllocationResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class BeerOrderAllocationListener {

    private final JmsTemplate jmsTemplate;

    @JmsListener(destination = JmsConfig.ALLOCATE_ORDER_QUEUE)
    public void listen(Message msg) {
        boolean isAllocationError = false;
        boolean isPendingInventory = false;

        AllocateOrderRequest request = (AllocateOrderRequest) msg.getPayload();

        if (request.getBeerOrderDto().getCustomerRef() != null) {
            if (request.getBeerOrderDto().getCustomerRef().equals("fail-allocation")) {
                isAllocationError = true;
            }

            if (request.getBeerOrderDto().getCustomerRef().equals("partial-allocation")) {
                isPendingInventory = true;
            }
        }

        boolean finalIsPendingInventory = isPendingInventory;

        request.getBeerOrderDto().getBeerOrderLines().forEach(beerOrderLineDto -> {
            if (finalIsPendingInventory) {
                beerOrderLineDto.setQuantityAllocated(beerOrderLineDto.getQuantityAllocated() - 1);
            } else {
                beerOrderLineDto.setQuantityAllocated(beerOrderLineDto.getQuantityAllocated());
            }
        });

        jmsTemplate.convertAndSend(JmsConfig.ALLOCATE_ORDER_RESULT_QUEUE, AllocationResult.builder()
                .allocationError(isAllocationError)
                .pendingInventory(isPendingInventory)
                .beerOrderDto(request.getBeerOrderDto())
                .build());
    }
}
