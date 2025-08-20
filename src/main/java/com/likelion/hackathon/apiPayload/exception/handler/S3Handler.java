package com.likelion.hackathon.apiPayload.exception.handler;

import com.likelion.hackathon.apiPayload.code.BaseErrorCode;
import com.likelion.hackathon.apiPayload.exception.GeneralException;

public class S3Handler extends GeneralException {
    public S3Handler(BaseErrorCode code) {
        super(code);
    }
}