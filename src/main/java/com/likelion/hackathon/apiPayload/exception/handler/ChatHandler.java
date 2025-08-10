package com.likelion.hackathon.apiPayload.exception.handler;

import com.likelion.hackathon.apiPayload.code.BaseErrorCode;
import com.likelion.hackathon.apiPayload.exception.GeneralException;

public class ChatHandler extends GeneralException {
    public ChatHandler(BaseErrorCode code) {
        super(code);
    }
}

