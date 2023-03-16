package org.example.mufiye.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.example.mufiye.base.BaseInfoProperties;
import org.example.mufiye.bo.RegisterLoginBo;
import org.example.mufiye.pojo.DyUser;
import org.example.mufiye.result.GraceJSONResult;
import org.example.mufiye.result.ResponseStatusEnum;
import org.example.mufiye.service.UserService;
import org.example.mufiye.utils.IPUtil;
import org.example.mufiye.utils.SMSUtils;
import org.example.mufiye.vo.DyUserVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Api(tags = "Passport Controller 用户登录注册模块的接口")
@RequestMapping("passport")
@RestController()
public class PassportController extends BaseInfoProperties {
    @Autowired
    private SMSUtils smsUtils;

    @Autowired
    private UserService userService;

    @ApiOperation("获取短信验证码")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "mobile", value = "手机号", required = true, paramType = "query", dataType="String")
    })
    @PostMapping("getSMSCode")
    public Object getSMSCode(@RequestParam String mobile, HttpServletRequest request) throws Exception {
        if (StringUtils.isBlank(mobile)) {
            return GraceJSONResult.ok();
        }
        log.info("mufiye: before get ip");
        String userIp = IPUtil.getRequestIp(request);
        log.info("mufiye: after get ip");

        redis.setnx60s(MOBILE_SMSCODE + ":" + userIp, userIp);

        log.info("mufiye: before send sms");
        String code = (int) ((Math.random() * 9 + 1) * 100000) + "";
        smsUtils.sendSMS(mobile, code);
        log.info("mufiye: after send sms");

        log.info("mufiye sms code: " + code);

        // TODO：把验证码放入到redis中
        redis.set(MOBILE_SMSCODE + ":" + mobile, code, 30 * 60);  // 超时时间为30分钟

        return GraceJSONResult.ok();
    }

    @ApiOperation("用户登录")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "registerLoginBo", value = "用户登录或者注册的信息", required = true, paramType = "body")
    })
    @PostMapping("login")
    public GraceJSONResult login(@Valid @RequestBody RegisterLoginBo registerLoginBo,
                                 // BindingResult bindingResult,  // 这样会有侵入性，改为异常处理
                                 HttpServletRequest request) {
        //        if(bindingResult.hasErrors()) {
        //            Map<String, String> map = getErrors(bindingResult);
        //            return GraceJSONResult.errorMap(map);
        //        }
        String mobile = registerLoginBo.getMobile();
        String code = registerLoginBo.getSmsCode();
        String redisCode = redis.get(MOBILE_SMSCODE + ":" + mobile);
        if (StringUtils.isBlank(redisCode) || !redisCode.equalsIgnoreCase(code)) {
            return GraceJSONResult.errorCustom(ResponseStatusEnum.SMS_CODE_ERROR);
        }

        // 取出用户，如果没有则创建
        DyUser dyUser = userService.queryMobileIsExist(mobile);
        if (dyUser == null) {
            dyUser = userService.createUser(mobile);
        }

        // 保存用户信息和会话信息（使用redis，前后端分离无法保存session）
        String uToken = UUID.randomUUID().toString();
        redis.set(REDIS_USER_TOKEN + ":" + dyUser.getId(), uToken); // 未设置timeout
        redis.del(MOBILE_SMSCODE + ":" + mobile);

        // 返回用户信息到前端
        DyUserVo dyUserVo = new DyUserVo();
        BeanUtils.copyProperties(dyUser, dyUserVo);  // 前面是src，后面是target
        dyUserVo.setUserToken(uToken);
        return GraceJSONResult.ok(dyUserVo);
    }

    public Map<String, String> getErrors(BindingResult bindingResult) {
        Map<String, String> map = new HashMap<>();
        List<FieldError> errorList = bindingResult.getFieldErrors();
        for (FieldError fe : errorList) {
            String field = fe.getField();
            String msg = fe.getDefaultMessage();
            map.put(field, msg);
        }
        return map;
    }

    @ApiOperation("用户登出")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "userId", value = "用户id", required = true, paramType = "query", dataType="String")
    })
    @PostMapping("logout")
    public GraceJSONResult logout(@RequestParam String userId,
                                  HttpServletRequest request) throws Exception {
        // 清除用户的token的信息
        redis.del(REDIS_USER_TOKEN + ":" + userId);
        return GraceJSONResult.ok();
    }
}
