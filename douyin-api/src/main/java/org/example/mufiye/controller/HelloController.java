package org.example.mufiye.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.example.mufiye.result.GraceJSONResult;
import org.example.mufiye.utils.SMSUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Api(tags = "hello Controller")
@RestController
public class HelloController {
    @ApiOperation(value = "用于测试服务器是否正常运行")
    @GetMapping("hello")
    public Object hello() {
        return GraceJSONResult.ok();
    }

    @Autowired
    private SMSUtils smsUtils;

    @ApiOperation(value = "测试短信验证码收发")
    @GetMapping("/testSMS")
    public Object testSMS() throws Exception {
        String code = "123456";
        smsUtils.sendSMS("15306600085", code);
        return GraceJSONResult.ok();
    }
}
