package iobank.org.accountmgt.exception;

public class DuplicationRecordException extends RuntimeException{
    private String message;
    public DuplicationRecordException(String message){
        super(message);
        this.message=message;
    }
    public DuplicationRecordException(){

    }

}
