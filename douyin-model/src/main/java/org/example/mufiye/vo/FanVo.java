package org.example.mufiye.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class FanVo {
    private String fanId;
    private String nickname;
    private String face;
    private boolean isFriend = false;
}
