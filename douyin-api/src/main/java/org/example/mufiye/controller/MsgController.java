package org.example.mufiye.controller;

import io.swagger.annotations.Api;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.example.mufiye.base.BaseInfoProperties;
import org.example.mufiye.mo.MessageMo;
import org.example.mufiye.result.GraceJSONResult;
import org.example.mufiye.service.MsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Api(tags = "Msg Controller 消息模块")
@RequestMapping("msg")
@RestController
public class MsgController extends BaseInfoProperties {
    @Autowired
    private MsgService msgService;

    @GetMapping("list")
    public GraceJSONResult list(@RequestParam String userId,
                                @RequestParam Integer page,
                                @RequestParam Integer pageSize) {

        // mongodb 从0分页，区别于数据库
        if (page == null) {
            page = COMMON_START_PAGE_ZERO;
        }
        if (pageSize == null) {
            pageSize = COMMON_PAGE_SIZE;
        }

        List<MessageMo> list = msgService.queryList(userId, page, pageSize);

        return GraceJSONResult.ok(list);
    }
}
