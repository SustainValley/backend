package com.likelion.hackathon.apiPayload.exception.handler;

import com.likelion.hackathon.apiPayload.code.BaseErrorCode;
import com.likelion.hackathon.apiPayload.exception.GeneralException;

public class ReservationHandler extends GeneralException {
    public ReservationHandler(BaseErrorCode code) {
        super(code);
    }
}
