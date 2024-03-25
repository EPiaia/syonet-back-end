package piaia.eduardo.syonet.record;

import java.math.BigDecimal;

public record StockHistory(Integer id, String product, String type, String user, String dateHour, BigDecimal quantity) {}
