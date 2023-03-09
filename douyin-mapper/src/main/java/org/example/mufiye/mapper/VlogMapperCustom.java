package org.example.mufiye.mapper;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import org.example.mufiye.vo.IndexVlogVo;
import org.springframework.stereotype.Repository;

@Repository
public interface VlogMapperCustom {
    public List<IndexVlogVo> getIndexVlogList(@Param("paramMap") Map<String, Object> map);

    public List<IndexVlogVo> getVlogDetailById(@Param("paramMap") Map<String, Object> map);

    public List<IndexVlogVo> getMyLikedVlogList(@Param("paramMap") Map<String, Object> map);

    public List<IndexVlogVo> getMyFollowVlogList(@Param("paramMap") Map<String, Object> map);

    public List<IndexVlogVo> getMyFriendVlogList(@Param("paramMap") Map<String, Object> map);
}
