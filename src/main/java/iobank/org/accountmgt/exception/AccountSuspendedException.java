package iobank.org.accountmgt.exception;

public class AccountSuspendedException extends RuntimeException{
    private String message;
    public AccountSuspendedException(String message){
        super(message);
        this.message=message;
    }
    public AccountSuspendedException(){

    }

}
