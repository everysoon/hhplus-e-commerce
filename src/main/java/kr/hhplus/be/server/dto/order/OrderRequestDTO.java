package kr.hhplus.be.server.dto.order;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderRequestDTO {
    @NotNull
    private Long userId;
    @NotNull
    private List<OrderProductRequestDTO> products;
    private List<UUID> couponId;
}
