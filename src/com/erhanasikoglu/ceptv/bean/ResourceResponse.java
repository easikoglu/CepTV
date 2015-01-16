package com.erhanasikoglu.ceptv.bean;


/**
 * Created with IntelliJ IDEA.
 * User: eerhasi
 * Date: 10.12.2013
 * Time: 10:38
 * To change this template use File | Settings | File Templates.
 */
 public class ResourceResponse<T>  {

    private T response;
    private Meta meta;
    private Notification notification;


    public ResourceResponse() {
    }

    public ResourceResponse(T response, Meta meta) {
        this.response = response;
        this.meta = meta;
    }

    public ResourceResponse(Meta meta, Notification notification) {
        this.meta = meta;
        this.notification = notification;
    }

    public ResourceResponse(Meta meta) {
        this.meta = meta;
    }

    public ResourceResponse(T response) {
        this.response = response;
    }

    public T getResponse() {
        return response;
    }

    public void setResponse(T response) {
        this.response = response;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public Notification getNotification() {
        return notification;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }
}
