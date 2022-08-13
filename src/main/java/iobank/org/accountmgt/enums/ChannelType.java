package iobank.org.accountmgt.enums;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    public static List<String> list(){
        return map.values().stream().map(rs->rs.label).collect(Collectors.toList());
    }
}
