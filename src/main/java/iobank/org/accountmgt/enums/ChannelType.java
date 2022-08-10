package iobank.org.accountmgt.enums;

import java.util.HashMap;
import java.util.Map;

public enum ChannelType {
    CASH("Cash"),
    TRANSFER("Transfer"),
    CHEQUE("Cheque");


    public final String label;
    private static final Map<String, ChannelType> map = new HashMap<>();


    static {
        for (ChannelType e : values()) {
            map.put(e.label, e);
        }
    }
    /*//*/
    private ChannelType(String label) {
        this.label=label;

    }
    public static ChannelType valueOfName(String label) {
        return map.get(label);
    }
}
