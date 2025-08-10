package com.likelion.hackathon.apiPayload.code.status;

import com.likelion.hackathon.apiPayload.code.BaseErrorCode;
import com.likelion.hackathon.apiPayload.code.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {
    // 가장 일반적인 응답
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST,"COMMON400","잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED,"COMMON401","인증이 필요합니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),

    // 유저 오류
    _USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER404", "유저 정보를 찾을 수 없습니다."),

    // 채팅 오류
    _CHAT_MESSAGE_NOT_FOUND(HttpStatus.BAD_REQUEST, "CHAT-MESSAGE404", "이전 채팅 내용을 찾을 수 없습니다."),
    _CHAT_ROOM_NOT_FOUND(HttpStatus.BAD_REQUEST, "CHAT-ROOM404", "채팅방을 찾을 수 없습니다."),
    _CHAT_ROOM_ALREADY_EXISTS(HttpStatus.CONFLICT, "CHAT-ROOM409", "채팅방이 이미 존재합니다."),



    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ErrorReasonDTO getReason() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .build();
    }

    @Override
    public ErrorReasonDTO getReasonHttpStatus() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build();
    }

}
