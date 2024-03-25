package piaia.eduardo.syonet.util;

import java.util.Arrays;

public enum MovementType {

    STOCK_IN {
        public Integer getId() {
            return 1;
        }

        public String getDescription() {
            return "Stock In";
        }
        
        public String getPtDescription() {
            return "Entrada";
        }
    }, STOCK_OUT {
        public Integer getId() {
            return 2;
        }
        
        public String getDescription() {
            return "Stock Out";
        }

        public String getPtDescription() {
            return "Saída";
        }
    };

    public abstract Integer getId();

    public abstract String getDescription();

    public abstract String getPtDescription();

    public static MovementType getTypeById(int id) {
        return Arrays.stream(values()).filter(type -> type.getId().equals(id)).findFirst().orElse(null);
    }
}
