package com.capstone.tvchat.api.member.auth.controller;

import com.capstone.tvchat.api.member.auth.domain.dto.*;
import com.capstone.tvchat.api.member.auth.domain.dto.response.MemberResponseDto;
import com.capstone.tvchat.api.member.auth.service.AuthService;
import com.capstone.tvchat.api.member.domain.enums.MemberErrorCode;
import com.capstone.tvchat.common.domain.JsonResultData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@RestController
@RequestMapping("/api/auth")
@Api("인증 관련 API Controller")
@RequiredArgsConstructor
public class AuthController {

    private final String BASIC_PREFIX = "Basic ";
    private final AuthService authService;

    @ApiOperation(value = "회원가입 API")
    @PostMapping("/signup")
    public ResponseEntity<?> signup(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization,
            @Valid @RequestBody MemberJoinDto memberJoinDto) {
        if (authorization != null) {
            String authBasic = authorization.substring(BASIC_PREFIX.length());

            String decodedAuthBasic = new String(Base64.getDecoder().decode(authBasic), StandardCharsets.UTF_8);
            String[] authUserInfo = decodedAuthBasic.split(":");

            String email = authUserInfo[0];
            String password = authUserInfo[1];

            memberJoinDto.setEmail(email);
            memberJoinDto.setPassword(password);

            MemberResponseDto memberResponseDto = authService.signup(memberJoinDto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(JsonResultData.successResultBuilder()
                            .data(memberResponseDto)
                            .build());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(JsonResultData.failResultBuilder()
                            .errorMessage(MemberErrorCode.NO_EMAIL_OR_PASSWORD.getMessage())
                            .errorCode(MemberErrorCode.NO_EMAIL_OR_PASSWORD.getCode())
                            .build());
        }
    }

    @ApiOperation(value = "로그인 API")
    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        if (authorization != null) {
            String authBasic = authorization.substring(BASIC_PREFIX.length());

            String decodedAuthBasic = new String(Base64.getDecoder().decode(authBasic), StandardCharsets.UTF_8);
            String[] authUserInfo = decodedAuthBasic.split(":");

            String email = authUserInfo[0];
            String password = authUserInfo[1];

            MemberLoginDto memberLoginDto = MemberLoginDto.of()
                    .email(email)
                    .password(password)
                    .build();

            TokenDto tokenDto = authService.login(memberLoginDto);
            return ResponseEntity.ok(JsonResultData.successResultBuilder()
                    .data(tokenDto)
                    .build());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(JsonResultData.failResultBuilder()
                            .errorMessage(MemberErrorCode.NO_EMAIL_OR_PASSWORD.getMessage())
                            .errorCode(MemberErrorCode.NO_EMAIL_OR_PASSWORD.getCode())
                            .build());
        }
    }

    @ApiOperation(value = "토큰 재발급 API")
    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(@Valid @RequestBody TokenRequestDto tokenRequestDto) {
        TokenDto tokenDto = authService.reissue(tokenRequestDto);
        return ResponseEntity.ok(JsonResultData.successResultBuilder()
                .data(tokenDto)
                .build());
    }

    @ApiOperation(value = "로그아웃 API")
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@Valid @RequestBody TokenRequestDto tokenRequestDto) {
        authService.logout(tokenRequestDto);
        return ResponseEntity.ok(JsonResultData.successResultBuilder()
                .data(null)
                .build());
    }

    @ApiOperation(value = "비밀번호 변경 API")
    @PatchMapping("/password")
    public ResponseEntity<?> updatePassword(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        if (authorization != null) {
            String authBasic = authorization.substring(BASIC_PREFIX.length());

            String decodedAuthBasic = new String(Base64.getDecoder().decode(authBasic), StandardCharsets.UTF_8);
            String[] authUserInfo = decodedAuthBasic.split(":");

            String email = authUserInfo[0];
            String password = authUserInfo[1];

            PasswordUpdateRequest passwordUpdateRequest = new PasswordUpdateRequest();
            passwordUpdateRequest.setEmail(email);
            passwordUpdateRequest.setPassword(password);

            authService.updatePassword(passwordUpdateRequest);
            return ResponseEntity.ok(JsonResultData.successResultBuilder()
                    .data(null)
                    .build());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(JsonResultData.failResultBuilder()
                            .errorMessage(MemberErrorCode.NO_EMAIL_OR_PASSWORD.getMessage())
                            .errorCode(MemberErrorCode.NO_EMAIL_OR_PASSWORD.getCode())
                            .build());
        }
    }
}