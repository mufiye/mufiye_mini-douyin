package org.example.mufiye.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.example.mufiye.base.BaseInfoProperties;
import org.example.mufiye.pojo.DyUser;
import org.example.mufiye.result.GraceJSONResult;
import org.example.mufiye.result.ResponseStatusEnum;
import org.example.mufiye.service.FanService;
import org.example.mufiye.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Api(tags = "FanController 粉丝相关业务功能的接口")
@RequestMapping("fans")
@RestController
public class FanController extends BaseInfoProperties {
    @Autowired
    private UserService userService;

    @Autowired
    private FanService fanService;

    @ApiOperation("取消关注")
    @PostMapping("follow")
    public GraceJSONResult follow(@RequestParam String myId,
                                  @RequestParam String vlogerId) {
        // 判断两个id不为空
        if(StringUtils.isBlank(myId) || StringUtils.isBlank(vlogerId)) {
            return GraceJSONResult.errorCustom(ResponseStatusEnum.SYSTEM_ERROR);
        }

        // 判断当前用户，自己不能关注自己
        if(myId.equalsIgnoreCase(vlogerId)) {
            return GraceJSONResult.errorCustom(ResponseStatusEnum.SYSTEM_RESPONSE_NO_INFO);
        }

        // 判断两个id的用户是否存在
        DyUser vloger = userService.getUser(vlogerId);
        DyUser myInfo = userService.getUser(myId);

        if(myInfo == null || vloger == null) {
            return GraceJSONResult.errorCustom(ResponseStatusEnum.SYSTEM_RESPONSE_NO_INFO);
        }

        // 保存粉丝关系到数据库
        fanService.doFollow(myId, vlogerId);

        // redis:
        redis.increment(REDIS_MY_FOLLOWS_COUNTS+":"+myId, 1);  // 关注数
        redis.increment(REDIS_MY_FANS_COUNTS+":"+vlogerId, 1);  // 粉丝数
        redis.set(REDIS_FANS_AND_VLOGGER_RELATIONSHIP+":"+myId+":"+vlogerId, "1");  // 表示关联关系
        return GraceJSONResult.ok();
    }

    @PostMapping("cancel")
    public GraceJSONResult cancel(@RequestParam String myId,
                                  @RequestParam String vlogerId) {
        // 删除业务的执行
        fanService.doCancel(myId, vlogerId);

        // 博主粉丝-1，我的关注-1
        redis.decrement(REDIS_MY_FOLLOWS_COUNTS+":"+myId, 1);
        redis.decrement(REDIS_MY_FANS_COUNTS+":"+vlogerId, 1);

        // 我的博主的关联关系，依赖redis，不要依赖数据库，避免db的性能瓶颈
        redis.del(REDIS_FANS_AND_VLOGGER_RELATIONSHIP+":"+myId+":"+vlogerId);

        return GraceJSONResult.ok();
    }

    @GetMapping("queryDoIFollowVloger")
    public GraceJSONResult queryDoIFollowVloger(@RequestParam String myId,
                                                @RequestParam String vlogerId) {
        // 也可以通过redis进行查询
        return GraceJSONResult.ok(fanService.queryDoIFollowVloger(myId, vlogerId));
    }

    @GetMapping("queryMyFollows")
    public GraceJSONResult queryMyFollows(@RequestParam String myId,
                                          @RequestParam Integer page,
                                          @RequestParam Integer pageSize) {
        return GraceJSONResult.ok(fanService.queryMyFollows(myId, page, pageSize));
    }

    @GetMapping("queryMyFans")
    public GraceJSONResult queryMyFans(@RequestParam String myId,
                                          @RequestParam Integer page,
                                          @RequestParam Integer pageSize) {
        return GraceJSONResult.ok(fanService.queryMyFans(myId, page, pageSize));
    }
}
