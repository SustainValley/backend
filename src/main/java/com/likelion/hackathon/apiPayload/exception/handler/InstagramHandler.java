package com.likelion.hackathon.apiPayload.exception.handler;

import com.likelion.hackathon.apiPayload.code.BaseErrorCode;
import com.likelion.hackathon.apiPayload.exception.GeneralException;

public class InstagramHandler extends GeneralException {
    public InstagramHandler(BaseErrorCode code) {
        super(code);
    }
}
