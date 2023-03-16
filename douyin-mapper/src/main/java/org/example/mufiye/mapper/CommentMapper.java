package org.example.mufiye.mapper;

import org.example.mufiye.my.mapper.MyMapper;
import org.example.mufiye.pojo.Comment;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentMapper extends MyMapper<Comment> {
}