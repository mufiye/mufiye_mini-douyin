package org.example.mufiye.service;

import org.example.mufiye.bo.CommentBo;
import org.example.mufiye.pojo.Comment;
import org.example.mufiye.utils.PagedGridResult;
import org.example.mufiye.vo.CommentVo;

public interface CommentService {
    // 发表评论
    public CommentVo createComment(CommentBo commentBo);

    // 获取评论列表
    public PagedGridResult queryVlogComments(String vlogId, String userId, Integer page, Integer pageSize);

    // 删除评论
    public void deleteComment(String commentUserId, String commentId, String vlogId);

    // 根据主键查询comment
    public Comment getComment(String id);
}
