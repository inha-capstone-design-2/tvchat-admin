package com.capstone.tvchat.api.channel.domain.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ModifyChannelRequest {
    private String channelName;
}
