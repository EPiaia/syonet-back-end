package piaia.eduardo.syonet.util;

public enum MovementType {

    STOCK_IN {
        public Integer getId() {
            return 1;
        }

        public String getDescription() {
            return "Stock In";
        }
    }, STOCK_OUT {
        public Integer getId() {
            return 2;
        }

        public String getDescription() {
            return "Stock Out";
        }
    };

    public abstract Integer getId();

    public abstract String getDescription();
}
