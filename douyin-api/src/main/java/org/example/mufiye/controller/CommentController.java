package org.example.mufiye.controller;

import com.tencentcloudapi.mrs.v20200910.models.BaseInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import java.util.HashMap;
import java.util.Map;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.example.mufiye.base.BaseInfoProperties;
import org.example.mufiye.bo.CommentBo;
import org.example.mufiye.enums.MessageEnum;
import org.example.mufiye.pojo.Comment;
import org.example.mufiye.pojo.Vlog;
import org.example.mufiye.result.GraceJSONResult;
import org.example.mufiye.service.CommentService;
import org.example.mufiye.service.MsgService;
import org.example.mufiye.service.VlogService;
import org.example.mufiye.vo.CommentVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Api(tags = "Comment Controller 评论模块的接口")
@RequestMapping("comment")
@RestController
public class CommentController extends BaseInfoProperties {
    @Autowired
    private CommentService commentService;

    @Autowired
    private MsgService msgService;

    @Autowired
    private VlogService vlogService;

    @ApiOperation("创建评论")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "commentBo", value = "评论信息", required = true, paramType = "body", dataType="CommentBo")
    })
    @PostMapping("create")
    public GraceJSONResult create(@RequestBody @Valid CommentBo commentBo) throws Exception {
        // 返回最新的一条记录
        CommentVo commentVo = commentService.createComment(commentBo);
        return GraceJSONResult.ok(commentVo);
    }

    @ApiOperation("获取某个视频的评论数")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "vlogId", value = "视频id", required = true, paramType = "query", dataType="String")
    })
    @GetMapping("counts")
    public GraceJSONResult commentCounts(@RequestParam String vlogId) {
        String countsStr = redis.get(REDIS_VLOG_COMMENT_COUNTS+":"+vlogId);
        if (StringUtils.isBlank(countsStr)) {
            countsStr = "0";
        }
        return GraceJSONResult.ok(Integer.valueOf(countsStr));
    }

    @ApiOperation("查看某条视频的评论")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "vlogId", value = "视频id", required = true, paramType = "query", dataType="String"),
        @ApiImplicitParam(name = "userId", value = "用户id", required = false, paramType = "query", dataType="String"),
        @ApiImplicitParam(name = "page", value = "页号", required = true, paramType = "query", dataType = "Integer"),
        @ApiImplicitParam(name = "pageSize", value = "页面条目数", required = true, paramType = "query", dataType = "Integer")
    })
    @GetMapping("list")
    public GraceJSONResult getCommentList(@RequestParam String vlogId,
                            @RequestParam(defaultValue = "") String userId,
                            @RequestParam Integer page,
                            @RequestParam Integer pageSize) {

        return GraceJSONResult.ok(commentService.queryVlogComments(vlogId, userId, page, pageSize));
    }

    @ApiOperation("删除某条评论")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "commentUserId", value = "评论用户的id", required = true, paramType = "query", dataType="String"),
        @ApiImplicitParam(name = "commentId", value = "评论id", required = true, paramType = "query", dataType="String"),
        @ApiImplicitParam(name = "vlogId", value = "视频id", required = true, paramType = "query", dataType = "String")
    })
    @DeleteMapping("delete")
    public GraceJSONResult deleteComment(@RequestParam String commentUserId,
                                  @RequestParam String commentId,
                                  @RequestParam String vlogId) {
        commentService.deleteComment(commentUserId, commentId, vlogId);
        return GraceJSONResult.ok();
    }

    @ApiOperation("点赞某条评论")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "commentId", value = "评论id", required = true, paramType = "query", dataType="String"),
        @ApiImplicitParam(name = "userId", value = "用户id", required = true, paramType = "query", dataType="String")
    })
    @PostMapping("like")
    public GraceJSONResult like(@RequestParam String commentId,
                                @RequestParam String userId) {
        // Question: BigKey问题
        redis.incrementHash(REDIS_VLOG_COMMENT_LIKED_COUNTS, commentId, 1);
        redis.setHashValue(REDIS_USER_LIKE_COMMENT, userId + ":" +commentId, "1");

        // 系统消息提示：点赞评论
        Comment comment = commentService.getComment(commentId);
        Vlog vlog = vlogService.getVlog(comment.getVlogId());
        Map msgContent = new HashMap<>();
        msgContent.put("vlogId", vlog.getId());
        msgContent.put("vlogCover", vlog.getCover());
        msgContent.put("commentId", commentId);

        msgService.createMsg(
            userId,
            comment.getCommentUserId(),
            MessageEnum.LIKE_COMMENT.type,
            msgContent
        );

        return GraceJSONResult.ok();
    }

    @ApiOperation("取消点赞某条评论")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "commentId", value = "评论id", required = true, paramType = "query", dataType="String"),
        @ApiImplicitParam(name = "userId", value = "用户id", required = true, paramType = "query", dataType="String")
    })
    @PostMapping("unlike")
    public GraceJSONResult unlike(@RequestParam String commentId,
                                @RequestParam String userId) {

        redis.decrementHash(REDIS_VLOG_COMMENT_LIKED_COUNTS, commentId, 1);
        redis.hdel(REDIS_USER_LIKE_COMMENT, userId + ":" +commentId);

        return GraceJSONResult.ok();
    }
}
