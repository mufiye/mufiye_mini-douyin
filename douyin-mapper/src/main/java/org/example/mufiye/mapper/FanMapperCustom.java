package org.example.mufiye.mapper;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import org.example.mufiye.my.mapper.MyMapper;
import org.example.mufiye.pojo.Fan;
import org.example.mufiye.vo.FanVo;
import org.example.mufiye.vo.VlogerVo;
import org.springframework.stereotype.Repository;

@Repository
public interface FanMapperCustom extends MyMapper<Fan> {

    public List<VlogerVo> queryMyFollows(@Param("paramMap") Map<String, Object> map);

    public List<FanVo> queryMyFan(@Param("paramMap") Map<String, Object> map);

}
