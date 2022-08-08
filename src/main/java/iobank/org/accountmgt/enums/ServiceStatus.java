package iobank.org.accountmgt.enums;

import java.util.HashMap;
import java.util.Map;

public enum ServiceStatus {
    APPROVE("Approved"),
    ACTIVE("Active"),
    REJECT("Rejected"),
    DECLINE("Declined"),
    CREDIT("Credit"),
    INCOME("Income"),
    WALLET_TRANSER("Wallet transfer"),
    DEBIT("Debit"),
    PENDING("Pending"),

    CARD("Card"),
    CATEGORY_MERCHANT("Merchant TR"),
    COMPLETE("Completed");


    public final String label;
    private static final Map<String, ServiceStatus> map = new HashMap<>();


    static {
        for (ServiceStatus e : values()) {
            map.put(e.label, e);
        }
    }
    /*//*/
    private ServiceStatus(String label) {
        this.label=label;

    }
    public static ServiceStatus valueOfName(String label) {
        return map.get(label);
    }
}
