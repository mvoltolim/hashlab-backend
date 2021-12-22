package com.hash.hashlab.grpc;

import com.proto.discount.DiscountGrpc;
import com.proto.discount.GetDiscountRequest;
import com.proto.discount.GetDiscountResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j
@Component
public class DiscountClient {

    private final String serverAddress;

    public DiscountClient(@Value("${grpc.discountServer.address:}") final String serverAddress) {
        this.serverAddress = serverAddress;
        if (StringUtils.hasLength(serverAddress)) {
            log.info(String.format("DiscountServerAddress: '%s'", serverAddress));
        } else {
            log.warn("DiscountServerAddress isn't informed!");
        }
    }

    public float getPercentDiscount(Integer productId) {
        try {
            ManagedChannel channel = buildChannel();

            GetDiscountResponse response = DiscountGrpc.newBlockingStub(channel).getDiscount(buildRequest(productId));

            channel.shutdown();

            return response.getPercentage();
        } catch (StatusRuntimeException ex) {
            log.warn("discount-server-grpc is offline: " + ex.getMessage());
            return 0.0F;
        } catch (Exception ex) {
            log.error("Unexpected error!", ex);
            return 0.0F;
        }
    }

    private ManagedChannel buildChannel() {
        return ManagedChannelBuilder.forTarget(serverAddress).usePlaintext().build();
    }

    private static GetDiscountRequest buildRequest(Integer productId) {
        return GetDiscountRequest.newBuilder().setProductID(productId).build();
    }

}
