package org.example.mufiye.mapper;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import org.example.mufiye.vo.CommentVo;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentMapperCustom {
    public List<CommentVo> getCommentList(@Param("paramMap") Map<String, Object> map);
}
