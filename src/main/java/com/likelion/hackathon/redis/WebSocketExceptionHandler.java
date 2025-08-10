package com.likelion.hackathon.redis;

import com.likelion.hackathon.apiPayload.ApiResponse;
import com.likelion.hackathon.apiPayload.code.status.ErrorStatus;
import com.likelion.hackathon.apiPayload.exception.GeneralException;
import com.likelion.hackathon.apiPayload.exception.handler.ChatHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class WebSocketExceptionHandler {

    @MessageExceptionHandler(ChatHandler.class)
    @SendToUser("/queue/errors")
    public ApiResponse<Object> handleChatHandler(ChatHandler ex, SimpMessageHeaderAccessor headerAccessor) {
        System.out.println("handleChatHandler ChatHandler");
        System.out.println("Session ID: " + headerAccessor.getSessionId());
        return ApiResponse.onFailure(
                ex.getErrorReasonHttpStatus().getCode(),
                ex.getErrorReasonHttpStatus().getMessage(),
                null
        );
    }

    @MessageExceptionHandler(GeneralException.class)
    @SendToUser("/queue/errors")
    public ApiResponse<Object> handleGeneralException(GeneralException ex) {

        System.out.println("handleChatHandler GeneralException");
        return ApiResponse.onFailure(
                ex.getErrorReasonHttpStatus().getCode(),
                ex.getErrorReasonHttpStatus().getMessage(),
                null
        );
    }

    @MessageExceptionHandler(Exception.class)
    @SendToUser("/queue/errors")
    public ApiResponse<Object> handleOtherException(Exception ex) {

        System.out.println("handleChatHandler Exception");
        return ApiResponse.onFailure(
                ErrorStatus._INTERNAL_SERVER_ERROR.getCode(),
                ErrorStatus._INTERNAL_SERVER_ERROR.getMessage(),
                ex.getMessage()
        );
    }
}

