package org.example.mufiye.serviceImpl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.example.mufiye.base.BaseInfoProperties;
import org.example.mufiye.enums.MessageEnum;
import org.example.mufiye.mo.MessageMo;
import org.example.mufiye.pojo.DyUser;
import org.example.mufiye.repository.MessageRepository;
import org.example.mufiye.service.MsgService;
import org.example.mufiye.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class MsgServiceImpl extends BaseInfoProperties implements MsgService {
    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserService userService;

    @Override
    public void createMsg(final String fromUserId, final String toUserId, final Integer type, final Map msgContent) {
        MessageMo messageMo = new MessageMo();
//        messageMo.setId();  // 自己会生成
        messageMo.setFromUserId(fromUserId);

        DyUser dyUser = userService.getUser(fromUserId);
        messageMo.setFromNickname(dyUser.getNickname());
        messageMo.setFromFace(dyUser.getFace());

        messageMo.setToUserId(toUserId);

        messageMo.setMsgType(type);
        if(msgContent != null) {
            messageMo.setMsgContent(msgContent);
        }

        messageMo.setCreateTime(new Date());

        messageRepository.save(messageMo);
    }

    @Override
    public List<MessageMo> queryList(final String toUserId, final Integer page, final Integer pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize, Sort.Direction.DESC, "createTime");
        List<MessageMo> list = messageRepository.findAllByToUserIdOrderByCreateTimeDesc(toUserId, pageable);
        for (MessageMo msg : list) {
            if(msg.getMsgType() != null && msg.getMsgType() == MessageEnum.FOLLOW_YOU.type) {
                Map map = msg.getMsgContent();
                if (map == null) {
                    map = new HashMap();
                }
                String relationship = redis.get(REDIS_FANS_AND_VLOGGER_RELATIONSHIP + ":" + msg.getToUserId() + ":" + msg.getFromUserId());
                if(StringUtils.isNotBlank(relationship) && relationship.equalsIgnoreCase("1")) {
                    map.put("isFriend", true);
                } else {
                    map.put("isFriend", false);
                }
                msg.setMsgContent(map);
            }
        }
        return list;
    }
}
