package org.example.mufiye;

import lombok.extern.slf4j.Slf4j;
import org.example.mufiye.base.RabbitMQConfig;
import org.example.mufiye.enums.MessageEnum;
import org.example.mufiye.exception.GraceException;
import org.example.mufiye.mo.MessageMo;
import org.example.mufiye.result.ResponseStatusEnum;
import org.example.mufiye.service.MsgService;
import org.example.mufiye.utils.JsonUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RabbitMQConsumer {
    @Autowired
    private MsgService msgService;

    @RabbitListener(queues = {RabbitMQConfig.QUEUE_SYS_MSG})
    public void watchQueue(String payload, Message message) {
        log.info(payload);

        MessageMo messageMo = JsonUtils.jsonToPojo(payload, MessageMo.class);

        String routingKey = message.getMessageProperties().getReceivedRoutingKey();
        log.info(routingKey);

        // TODO: 下面这段代码可以优化，一个地方是参数优化，另外是枚举的判断优化

        if (routingKey.equalsIgnoreCase("sys.msg." + MessageEnum.FOLLOW_YOU.enValue)) {
            msgService.createMsg(messageMo.getFromUserId(),
                                 messageMo.getToUserId(),
                                 MessageEnum.FOLLOW_YOU.type,
                                 null);
        } else if (routingKey.equalsIgnoreCase("sys.msg." + MessageEnum.LIKE_VLOG.enValue)) {
            msgService.createMsg(messageMo.getFromUserId(),
                                 messageMo.getToUserId(),
                                 MessageEnum.LIKE_VLOG.type,
                                 messageMo.getMsgContent());
        } else if (routingKey.equalsIgnoreCase("sys.msg." + MessageEnum.COMMENT_VLOG.enValue)) {
            msgService.createMsg(messageMo.getFromUserId(),
                                 messageMo.getToUserId(),
                                 MessageEnum.COMMENT_VLOG.type,
                                 messageMo.getMsgContent());
        } else if (routingKey.equalsIgnoreCase("sys.msg." + MessageEnum.REPLY_YOU.enValue)) {
            msgService.createMsg(messageMo.getFromUserId(),
                                 messageMo.getToUserId(),
                                 MessageEnum.REPLY_YOU.type,
                                 messageMo.getMsgContent());
        } else if (routingKey.equalsIgnoreCase("sys.msg." + MessageEnum.LIKE_COMMENT.enValue)) {
            msgService.createMsg(messageMo.getFromUserId(),
                                 messageMo.getToUserId(),
                                 MessageEnum.LIKE_COMMENT.type,
                                 messageMo.getMsgContent());
        } else {
            GraceException.display(ResponseStatusEnum.SYSTEM_OPERATION_ERROR);
        }

    }
}
