package iobank.org.accountmgt.enums;

import java.util.HashMap;
import java.util.Map;

public enum AccountType {
    SAVINGS("Savings"),
    CURRENT("Current"),
    FIXED("Fixed");


    public final String label;
    private static final Map<String, AccountType> map = new HashMap<>();


    static {
        for (AccountType e : values()) {
            map.put(e.label, e);
        }
    }
    /*//*/
    private AccountType(String label) {
        this.label=label;

    }
    public static AccountType valueOfName(String label) {
        return map.get(label);
    }
}
