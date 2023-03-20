package Model;

import java.math.BigDecimal;

public class TradingRecord {



    private String coinType;



    private BigDecimal value;

    private BigDecimal amount;



    public String getCoinType() {
        return coinType;
    }

    public void setCoinType(String coinType) {
        this.coinType = coinType;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "TradingRecord{" +
                "coinType='" + coinType + '\'' +
                ", value=" + value +
                ", amount=" + amount +
                '}';
    }
}
