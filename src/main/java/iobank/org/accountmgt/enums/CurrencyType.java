package iobank.org.accountmgt.enums;

import java.util.HashMap;
import java.util.Map;

public enum CurrencyType {
    NAIRA("NGN"),
    DOLLAR("USD"),
    EURO("EUR"),
    POUNDS("GBP");


    public final String label;
    private static final Map<String, CurrencyType> map = new HashMap<>();


    static {
        for (CurrencyType e : values()) {
            map.put(e.label, e);
        }
    }
    /*//*/
    private CurrencyType(String label) {
        this.label=label;

    }
    public static CurrencyType valueOfName(String label) {
        return map.get(label);
    }
}
