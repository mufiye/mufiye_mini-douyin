package org.example.mufiye.serviceImpl;

import com.github.pagehelper.PageHelper;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.units.qual.C;
import org.example.mufiye.base.BaseInfoProperties;
import org.example.mufiye.base.RabbitMQConfig;
import org.example.mufiye.bo.CommentBo;
import org.example.mufiye.enums.MessageEnum;
import org.example.mufiye.enums.YesOrNo;
import org.example.mufiye.mapper.CommentMapper;
import org.example.mufiye.mapper.CommentMapperCustom;
import org.example.mufiye.mo.MessageMo;
import org.example.mufiye.pojo.Comment;
import org.example.mufiye.pojo.Vlog;
import org.example.mufiye.service.CommentService;
import org.example.mufiye.service.MsgService;
import org.example.mufiye.service.VlogService;
import org.example.mufiye.utils.JsonUtils;
import org.example.mufiye.utils.PagedGridResult;
import org.example.mufiye.vo.CommentVo;
import org.n3r.idworker.Sid;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl extends BaseInfoProperties implements CommentService {
    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private CommentMapperCustom commentMapperCustom;

    @Autowired
    private MsgService msgService;

    @Autowired
    private VlogService vlogService;


    @Autowired
    public RabbitTemplate rabbitTemplate;

    @Autowired
    private Sid sid;

    @Override
    public CommentVo createComment(final CommentBo commentBo) {
        String commentId = sid.nextShort();
        Comment comment = new Comment();

        comment.setId(commentId);
        comment.setVlogId(commentBo.getVlogId());
        comment.setVlogerId(commentBo.getVlogerId());
        comment.setCommentUserId(commentBo.getCommentUserId());
        comment.setFatherCommentId(commentBo.getFatherCommentId());
        comment.setContent(commentBo.getContent());

        comment.setLikeCounts(0);
        comment.setCreateTime(new Date());

        commentMapper.insert(comment);

        redis.increment(REDIS_VLOG_COMMENT_COUNTS + ":" + commentBo.getVlogId(), 1);

        // 留言后的最新评论需要返回给前端进行展示
        CommentVo commentVo = new CommentVo();
        BeanUtils.copyProperties(comment, commentVo);

        // 系统消息提示：评论/回复
        Integer type = MessageEnum.COMMENT_VLOG.type;
        String routeType = MessageEnum.COMMENT_VLOG.enValue;
        if (StringUtils.isNotBlank(commentBo.getFatherCommentId()) && !commentBo.getFatherCommentId()
                                                                               .equalsIgnoreCase("0")) {
            type = MessageEnum.REPLY_YOU.type;
            routeType = MessageEnum.REPLY_YOU.enValue;
        }

        Vlog vlog = vlogService.getVlog(commentBo.getVlogId());
        Map msgContent = new HashMap<>();
        msgContent.put("vlogId", vlog.getId());
        msgContent.put("vlogCover", vlog.getCover());
        msgContent.put("commentId", commentId);
        msgContent.put("comment", commentBo.getContent());

//        msgService.createMsg(
//            commentBo.getCommentUserId(),
//            commentBo.getVlogerId(),
//            type,
//            msgContent
//        );

        // 使用mq进行处理
        MessageMo messageMo = new MessageMo();
        messageMo.setFromUserId(commentBo.getCommentUserId());
        messageMo.setToUserId(commentBo.getVlogerId());
        messageMo.setMsgContent(msgContent);
        rabbitTemplate.convertAndSend(
            RabbitMQConfig.EXCHANGE_MSG,
            "sys.msg." + routeType,
            JsonUtils.objectToJson(messageMo));
        return commentVo;
    }

    @Override
    public PagedGridResult queryVlogComments(final String vlogId,
                                             final String userId,
                                             final Integer page,
                                             final Integer pageSize) {
        Map<String, Object> map = new HashMap<>();
        map.put("vlogId", vlogId);

        PageHelper.startPage(page, pageSize);
        List<CommentVo> list = commentMapperCustom.getCommentList(map);
        for (CommentVo commentVo : list) {
            String commentId = commentVo.getCommentId();

            // 获取当前短视频某个评论的点赞总数
            String countsStr = redis.getHashValue(REDIS_VLOG_COMMENT_LIKED_COUNTS, commentId);
            Integer counts = 0;
            if (StringUtils.isNotBlank(countsStr)) {
                counts = Integer.valueOf(countsStr);
            }
            commentVo.setLikeCounts(counts);

            // 判断当前用户是否点赞过评论
            String doILike = redis.hget(REDIS_USER_LIKE_COMMENT, userId + ":" + commentId);
            if (StringUtils.isNotBlank(doILike) && doILike.equalsIgnoreCase("1")) {
                commentVo.setIsLike(YesOrNo.YES.type);
            }
        }
        return setterPagedGrid(list, page);
    }

    @Override
    public void deleteComment(final String commentUserId, final String commentId, final String vlogId) {
        Comment pendingDelete = new Comment();
        pendingDelete.setId(commentId);
        pendingDelete.setCommentUserId(commentUserId);

        commentMapper.delete(pendingDelete);

        redis.decrement(REDIS_VLOG_COMMENT_COUNTS + ":" + vlogId, 1);
    }

    @Override
    public Comment getComment(final String id) {
        return commentMapper.selectByPrimaryKey(id);
    }
}
