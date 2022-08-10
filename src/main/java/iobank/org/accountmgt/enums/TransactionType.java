package iobank.org.accountmgt.enums;

import java.util.HashMap;
import java.util.Map;

public enum TransactionType {
    DEPOSIT("Deposit"),
    WITHDRAWAL("Withdrawal");

    public final String label;
    private static final Map<String, TransactionType> map = new HashMap<>();


    static {
        for (TransactionType e : values()) {
            map.put(e.label, e);
        }
    }
    /*//*/
    private TransactionType(String label) {
        this.label=label;

    }
    public static TransactionType valueOfName(String label) {
        return map.get(label);
    }
}
