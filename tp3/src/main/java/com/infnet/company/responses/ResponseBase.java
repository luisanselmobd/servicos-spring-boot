package com.infnet.company.responses;

public class ResponseBase<T> {
    public T getData() {
        return Data;
    }

    public String getMessage() {
        return Message;
    }

    private T Data;
    private String Message;
    public ResponseBase(T Data, String Message) {
        this.Data = Data;
        this.Message = Message;
    }
}
