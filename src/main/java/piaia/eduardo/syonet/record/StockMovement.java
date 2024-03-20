package piaia.eduardo.syonet.record;

import java.math.BigDecimal;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public record StockMovement(@NotEmpty Integer productId, @NotEmpty Integer userId, @NotNull BigDecimal quantity) {}
