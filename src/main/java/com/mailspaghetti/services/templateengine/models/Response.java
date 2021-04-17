package com.mailspaghetti.services.templateengine.models;

import java.io.PrintWriter;
import java.io.StringWriter;

public class Response {

    protected boolean success;
    protected String  message;

    public Response(boolean success, String message){
        this.message = message;
        this.success = success;
    }

    public Response(){
        this.success = true;
        this.message = "";
    }

    public Response(Exception exception) {
        this.success = false;
        StringWriter sw = new StringWriter();
        exception.printStackTrace(new PrintWriter(sw));
        this.message = sw.toString();
	}

	public boolean getSuccess(){
        return this.success;
    }

    public String getMessage(){
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}