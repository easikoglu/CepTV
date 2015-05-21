package com.eatech.ceptv.bean;

import java.util.List;

public class Meta {

    private Integer code;
    private String errorType;
    private String errorMessage;
    private String errorDetail;
    private List<ValidationError> validationErrors;


    public Meta(Integer code) {
        this.code = code;
    }

    public Meta() {
    }

    public Meta(Integer code, String errorType, String errorMessage, String errorDetail) {
        this.code = code;
        this.errorType = errorType;
        this.errorMessage = errorMessage;
        this.errorDetail = errorDetail;
    }

    public Meta(Integer code, String errorType, String errorMessage, String errorDetail, List<ValidationError> validationErrors) {
        this.code = code;
        this.errorType = errorType;
        this.errorMessage = errorMessage;
        this.errorDetail = errorDetail;
        this.validationErrors = validationErrors;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getErrorType() {
        return errorType;
    }

    public void setErrorType(String errorType) {
        this.errorType = errorType;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorDetail() {
        return errorDetail;
    }

    public void setErrorDetail(String errorDetail) {
        this.errorDetail = errorDetail;
    }

    public List<ValidationError> getValidationErrors() {
        return validationErrors;
    }

    public void setValidationErrors(List<ValidationError> validationErrors) {
        this.validationErrors = validationErrors;
    }
}
