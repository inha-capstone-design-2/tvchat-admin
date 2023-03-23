package com.capstone.tvchat.api.channel.controller;

import com.capstone.tvchat.api.channel.domain.dto.ChannelSearchRequest;
import com.capstone.tvchat.api.channel.service.ChannelService;
import com.capstone.tvchat.common.BaseEntity.JsonResultData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.query.Jpa21Utils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Api("채널 API Controller")
@RequestMapping("/api/channel")
public class ChannelController {

    private final ChannelService channelService;

    @ApiOperation(value = "채널 전체 조회 API")
    @GetMapping("/")
    public ResponseEntity<?> getChannels() {
        return ResponseEntity.ok(
                JsonResultData.successResultBuilder()
                        .data(channelService.getChannels())
                        .build()
        );
    }
}