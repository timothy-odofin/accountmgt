package iobank.org.accountmgt.utils;

public class TransactionEndpoints {
    public final static String TRANSACTION_ROOT="/transaction";
    public final static String ADD_PATH="/add";
    public final static String LIST_PATH="/list";

    public final static String DEPOSIT_PATH="/deposit";
    public final static String WITHDRAWAL_PATH="/withdraw";
    public final static String TRANSACTION_LIST_BY_ACCOUNT=LIST_PATH+"/by-acccount-number";
    public final static String DATE_PARAM="tranDate";
    public final static String ACCOUNT_PARAM="accountNumber";

}
