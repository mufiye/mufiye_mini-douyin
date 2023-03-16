package org.example.mufiye.service;

import java.util.List;
import java.util.Map;
import org.example.mufiye.mo.MessageMo;

public interface MsgService {
    // 创建消息
    public void createMsg(String fromUserId, String toUserId, Integer type, Map msgContent);

    // 消息展示
    public List<MessageMo> queryList(String toUserId, Integer page, Integer pageSize);
}
