# Api Documentation


**简介**:Api Documentation


**HOST**:localhost:8099


**联系人**:


**Version**:1.0


**接口路径**:/v2/api-docs


[TOC]






# Comment Controller 评论模块的接口


## 获取某个视频的评论数


**接口地址**:`/comment/counts`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|vlogId|视频id|query|true|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|GraceJSONResult|
|401|Unauthorized||
|403|Forbidden||
|404|Not Found||


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|data||object||
|msg||string||
|status||integer(int32)|integer(int32)|
|success||boolean||


**响应示例**:
```javascript
{
	"data": {},
	"msg": "",
	"status": 0,
	"success": true
}
```


## 创建评论


**接口地址**:`/comment/create`


**请求方式**:`POST`


**请求数据类型**:`application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
  "commentUserId": "",
  "content": "",
  "fatherCommentId": "",
  "vlogId": "",
  "vlogerId": ""
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|commentBo|评论信息|body|true|CommentBo|CommentBo|
|&emsp;&emsp;commentUserId|||false|string||
|&emsp;&emsp;content|||false|string||
|&emsp;&emsp;fatherCommentId|||false|string||
|&emsp;&emsp;vlogId|||false|string||
|&emsp;&emsp;vlogerId|||false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|GraceJSONResult|
|201|Created||
|401|Unauthorized||
|403|Forbidden||
|404|Not Found||


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|data||object||
|msg||string||
|status||integer(int32)|integer(int32)|
|success||boolean||


**响应示例**:
```javascript
{
	"data": {},
	"msg": "",
	"status": 0,
	"success": true
}
```


## 删除某条评论


**接口地址**:`/comment/delete`


**请求方式**:`DELETE`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|commentId|评论id|query|true|string||
|commentUserId|评论用户的id|query|true|string||
|vlogId|视频id|query|true|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|GraceJSONResult|
|204|No Content||
|401|Unauthorized||
|403|Forbidden||


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|data||object||
|msg||string||
|status||integer(int32)|integer(int32)|
|success||boolean||


**响应示例**:
```javascript
{
	"data": {},
	"msg": "",
	"status": 0,
	"success": true
}
```


## 点赞某条评论


**接口地址**:`/comment/like`


**请求方式**:`POST`


**请求数据类型**:`application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|commentId|评论id|query|true|string||
|userId|用户id|query|true|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|GraceJSONResult|
|201|Created||
|401|Unauthorized||
|403|Forbidden||
|404|Not Found||


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|data||object||
|msg||string||
|status||integer(int32)|integer(int32)|
|success||boolean||


**响应示例**:
```javascript
{
	"data": {},
	"msg": "",
	"status": 0,
	"success": true
}
```


## 查看某条视频的评论


**接口地址**:`/comment/list`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|page|页号|query|true|ref||
|pageSize|页面条目数|query|true|ref||
|vlogId|视频id|query|true|string||
|userId|用户id|query|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|GraceJSONResult|
|401|Unauthorized||
|403|Forbidden||
|404|Not Found||


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|data||object||
|msg||string||
|status||integer(int32)|integer(int32)|
|success||boolean||


**响应示例**:
```javascript
{
	"data": {},
	"msg": "",
	"status": 0,
	"success": true
}
```


## 取消点赞某条评论


**接口地址**:`/comment/unlike`


**请求方式**:`POST`


**请求数据类型**:`application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|commentId|评论id|query|true|string||
|userId|用户id|query|true|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|GraceJSONResult|
|201|Created||
|401|Unauthorized||
|403|Forbidden||
|404|Not Found||


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|data||object||
|msg||string||
|status||integer(int32)|integer(int32)|
|success||boolean||


**响应示例**:
```javascript
{
	"data": {},
	"msg": "",
	"status": 0,
	"success": true
}
```


# FanController 粉丝模块的接口


## 取消关注某一个用户


**接口地址**:`/fans/cancel`


**请求方式**:`POST`


**请求数据类型**:`application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|myId|我的用户id|query|true|string||
|vlogerId|要取消关注的用户的id|query|true|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|GraceJSONResult|
|201|Created||
|401|Unauthorized||
|403|Forbidden||
|404|Not Found||


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|data||object||
|msg||string||
|status||integer(int32)|integer(int32)|
|success||boolean||


**响应示例**:
```javascript
{
	"data": {},
	"msg": "",
	"status": 0,
	"success": true
}
```


## 关注某一个用户


**接口地址**:`/fans/follow`


**请求方式**:`POST`


**请求数据类型**:`application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|myId|我的用户id|query|true|string||
|vlogerId|关注的用户的id|query|true|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|GraceJSONResult|
|201|Created||
|401|Unauthorized||
|403|Forbidden||
|404|Not Found||


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|data||object||
|msg||string||
|status||integer(int32)|integer(int32)|
|success||boolean||


**响应示例**:
```javascript
{
	"data": {},
	"msg": "",
	"status": 0,
	"success": true
}
```


## 查询我是否关注了某一个用户


**接口地址**:`/fans/queryDoIFollowVloger`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|myId|我的用户id|query|true|string||
|vlogerId|关注的用户的id|query|true|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|GraceJSONResult|
|401|Unauthorized||
|403|Forbidden||
|404|Not Found||


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|data||object||
|msg||string||
|status||integer(int32)|integer(int32)|
|success||boolean||


**响应示例**:
```javascript
{
	"data": {},
	"msg": "",
	"status": 0,
	"success": true
}
```


## 查询我的粉丝列表


**接口地址**:`/fans/queryMyFans`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|myId|我的用户id|query|true|string||
|page|现在所在的页号|query|true|ref||
|pageSize|页面大小，也就是一页可以容纳多少item|query|true|ref||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|GraceJSONResult|
|401|Unauthorized||
|403|Forbidden||
|404|Not Found||


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|data||object||
|msg||string||
|status||integer(int32)|integer(int32)|
|success||boolean||


**响应示例**:
```javascript
{
	"data": {},
	"msg": "",
	"status": 0,
	"success": true
}
```


## 查询我的关注列表


**接口地址**:`/fans/queryMyFollows`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|myId|我的用户id|query|true|string||
|page|现在所在的页号|query|true|ref||
|pageSize|页面大小，也就是一页可以容纳多少item|query|true|ref||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|GraceJSONResult|
|401|Unauthorized||
|403|Forbidden||
|404|Not Found||


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|data||object||
|msg||string||
|status||integer(int32)|integer(int32)|
|success||boolean||


**响应示例**:
```javascript
{
	"data": {},
	"msg": "",
	"status": 0,
	"success": true
}
```


# Msg Controller 消息模块的接口


## 查看消息


**接口地址**:`/msg/list`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|page|页号|query|true|ref||
|pageSize|页面条目数|query|true|ref||
|userId|用户id|query|true|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|GraceJSONResult|
|401|Unauthorized||
|403|Forbidden||
|404|Not Found||


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|data||object||
|msg||string||
|status||integer(int32)|integer(int32)|
|success||boolean||


**响应示例**:
```javascript
{
	"data": {},
	"msg": "",
	"status": 0,
	"success": true
}
```


# Passport Controller 用户登录注册模块的接口


## 获取短信验证码


**接口地址**:`/passport/getSMSCode`


**请求方式**:`POST`


**请求数据类型**:`application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|mobile|手机号|query|true|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK||
|201|Created||
|401|Unauthorized||
|403|Forbidden||
|404|Not Found||


**响应参数**:


暂无


**响应示例**:
```javascript

```


## 用户登录


**接口地址**:`/passport/login`


**请求方式**:`POST`


**请求数据类型**:`application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|registerLoginBo|用户登录或者注册的信息|body|true|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|GraceJSONResult|
|201|Created||
|401|Unauthorized||
|403|Forbidden||
|404|Not Found||


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|data||object||
|msg||string||
|status||integer(int32)|integer(int32)|
|success||boolean||


**响应示例**:
```javascript
{
	"data": {},
	"msg": "",
	"status": 0,
	"success": true
}
```


## 用户登出


**接口地址**:`/passport/logout`


**请求方式**:`POST`


**请求数据类型**:`application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|userId|用户id|query|true|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|GraceJSONResult|
|201|Created||
|401|Unauthorized||
|403|Forbidden||
|404|Not Found||


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|data||object||
|msg||string||
|status||integer(int32)|integer(int32)|
|success||boolean||


**响应示例**:
```javascript
{
	"data": {},
	"msg": "",
	"status": 0,
	"success": true
}
```


# Test Sms Controller 测试短信业务的接口


## 用于测试服务器是否正常运行


**接口地址**:`/hello`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


暂无


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK||
|401|Unauthorized||
|403|Forbidden||
|404|Not Found||


**响应参数**:


暂无


**响应示例**:
```javascript

```


## 测试短信验证码收发


**接口地址**:`/testSMS`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


暂无


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK||
|401|Unauthorized||
|403|Forbidden||
|404|Not Found||


**响应参数**:


暂无


**响应示例**:
```javascript

```


# User Info Controller 用户个人信息模块的接口


## 修改头像或背景图片


**接口地址**:`/userInfo/modifyImage`


**请求方式**:`POST`


**请求数据类型**:`multipart/form-data`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|file|上传的图片文件|formData|true|string||
|type|修改头像还是背景|query|true|ref||
|userId|用户id|query|true|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|GraceJSONResult|
|201|Created||
|401|Unauthorized||
|403|Forbidden||
|404|Not Found||


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|data||object||
|msg||string||
|status||integer(int32)|integer(int32)|
|success||boolean||


**响应示例**:
```javascript
{
	"data": {},
	"msg": "",
	"status": 0,
	"success": true
}
```


## 修改用户信息


**接口地址**:`/userInfo/modifyUserInfo`


**请求方式**:`POST`


**请求数据类型**:`application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|type|指修改的是哪种个人信息|query|true|ref||
|updatedDyUserBo|想要修改为的个人信息|body|true|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|GraceJSONResult|
|201|Created||
|401|Unauthorized||
|403|Forbidden||
|404|Not Found||


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|data||object||
|msg||string||
|status||integer(int32)|integer(int32)|
|success||boolean||


**响应示例**:
```javascript
{
	"data": {},
	"msg": "",
	"status": 0,
	"success": true
}
```


## 查询用户信息


**接口地址**:`/userInfo/query`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|userId|用户id|query|true|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK||
|401|Unauthorized||
|403|Forbidden||
|404|Not Found||


**响应参数**:


暂无


**响应示例**:
```javascript

```


# Vlog Controller 视频模块的接口


## 用户将某个视频改为私密的


**接口地址**:`/vlog/changeToPrivate`


**请求方式**:`POST`


**请求数据类型**:`application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|userId|用户id|query|true|string||
|vlogId|视频id|query|true|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|GraceJSONResult|
|201|Created||
|401|Unauthorized||
|403|Forbidden||
|404|Not Found||


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|data||object||
|msg||string||
|status||integer(int32)|integer(int32)|
|success||boolean||


**响应示例**:
```javascript
{
	"data": {},
	"msg": "",
	"status": 0,
	"success": true
}
```


## 用户将某个视频改为公开的


**接口地址**:`/vlog/changeToPublic`


**请求方式**:`POST`


**请求数据类型**:`application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|userId|用户id|query|true|string||
|vlogId|视频id|query|true|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|GraceJSONResult|
|201|Created||
|401|Unauthorized||
|403|Forbidden||
|404|Not Found||


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|data||object||
|msg||string||
|status||integer(int32)|integer(int32)|
|success||boolean||


**响应示例**:
```javascript
{
	"data": {},
	"msg": "",
	"status": 0,
	"success": true
}
```


## 获取具体的某个视频


**接口地址**:`/vlog/detail`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|userId|用户id,用于获取某用户的视频|query|true|string||
|vlogId|视频id|query|true|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|GraceJSONResult|
|401|Unauthorized||
|403|Forbidden||
|404|Not Found||


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|data||object||
|msg||string||
|status||integer(int32)|integer(int32)|
|success||boolean||


**响应示例**:
```javascript
{
	"data": {},
	"msg": "",
	"status": 0,
	"success": true
}
```


## 用户查看自己的关注用户的视频列表


**接口地址**:`/vlog/followList`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|myId|用户自己的id|query|true|string||
|page|页号|query|true|ref||
|pageSize|页面条目数|query|true|ref||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|GraceJSONResult|
|401|Unauthorized||
|403|Forbidden||
|404|Not Found||


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|data||object||
|msg||string||
|status||integer(int32)|integer(int32)|
|success||boolean||


**响应示例**:
```javascript
{
	"data": {},
	"msg": "",
	"status": 0,
	"success": true
}
```


## 用户查看自己的朋友的视频列表


**接口地址**:`/vlog/friendList`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|myId|用户自己的id|query|true|string||
|page|页号|query|true|ref||
|pageSize|页面条目数|query|true|ref||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|GraceJSONResult|
|401|Unauthorized||
|403|Forbidden||
|404|Not Found||


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|data||object||
|msg||string||
|status||integer(int32)|integer(int32)|
|success||boolean||


**响应示例**:
```javascript
{
	"data": {},
	"msg": "",
	"status": 0,
	"success": true
}
```


## 获取视频列表


**接口地址**:`/vlog/indexList`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|page|页号|query|true|ref||
|pageSize|页面条目数|query|true|ref||
|search|搜索的关键词|query|false|string||
|userId|用户id,用于获取某用户的视频|query|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|GraceJSONResult|
|401|Unauthorized||
|403|Forbidden||
|404|Not Found||


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|data||object||
|msg||string||
|status||integer(int32)|integer(int32)|
|success||boolean||


**响应示例**:
```javascript
{
	"data": {},
	"msg": "",
	"status": 0,
	"success": true
}
```


## 用户点赞某个视频


**接口地址**:`/vlog/like`


**请求方式**:`POST`


**请求数据类型**:`application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|userId|用户id|query|true|string||
|vlogId|视频id|query|true|string||
|vlogerId|发布视频者的id|query|true|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|GraceJSONResult|
|201|Created||
|401|Unauthorized||
|403|Forbidden||
|404|Not Found||


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|data||object||
|msg||string||
|status||integer(int32)|integer(int32)|
|success||boolean||


**响应示例**:
```javascript
{
	"data": {},
	"msg": "",
	"status": 0,
	"success": true
}
```


## 用户查看自己点赞过的视频


**接口地址**:`/vlog/myLikedList`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|page|页号|query|true|ref||
|pageSize|页面条目数|query|true|ref||
|userId|用户id|query|true|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|GraceJSONResult|
|401|Unauthorized||
|403|Forbidden||
|404|Not Found||


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|data||object||
|msg||string||
|status||integer(int32)|integer(int32)|
|success||boolean||


**响应示例**:
```javascript
{
	"data": {},
	"msg": "",
	"status": 0,
	"success": true
}
```


## 用户查看自己的私密视频列表


**接口地址**:`/vlog/myPrivateList`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|page|页号|query|true|ref||
|pageSize|页面条目数|query|true|ref||
|userId|用户id|query|true|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|GraceJSONResult|
|401|Unauthorized||
|403|Forbidden||
|404|Not Found||


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|data||object||
|msg||string||
|status||integer(int32)|integer(int32)|
|success||boolean||


**响应示例**:
```javascript
{
	"data": {},
	"msg": "",
	"status": 0,
	"success": true
}
```


## 用户查看自己的公开视频列表


**接口地址**:`/vlog/myPublicList`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|page|页号|query|true|ref||
|pageSize|页面条目数|query|true|ref||
|userId|用户id|query|true|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|GraceJSONResult|
|401|Unauthorized||
|403|Forbidden||
|404|Not Found||


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|data||object||
|msg||string||
|status||integer(int32)|integer(int32)|
|success||boolean||


**响应示例**:
```javascript
{
	"data": {},
	"msg": "",
	"status": 0,
	"success": true
}
```


## 上传视频信息


**接口地址**:`/vlog/publish`


**请求方式**:`POST`


**请求数据类型**:`application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
  "commentsCounts": 0,
  "cover": "",
  "height": 0,
  "id": "",
  "likeCounts": 0,
  "title": "",
  "url": "",
  "vlogerId": "",
  "width": 0
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|vlogBo|视频信息|body|true|VlogBo|VlogBo|
|&emsp;&emsp;commentsCounts|||false|integer(int32)||
|&emsp;&emsp;cover|||false|string||
|&emsp;&emsp;height|||false|integer(int32)||
|&emsp;&emsp;id|||false|string||
|&emsp;&emsp;likeCounts|||false|integer(int32)||
|&emsp;&emsp;title|||false|string||
|&emsp;&emsp;url|||false|string||
|&emsp;&emsp;vlogerId|||false|string||
|&emsp;&emsp;width|||false|integer(int32)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|GraceJSONResult|
|201|Created||
|401|Unauthorized||
|403|Forbidden||
|404|Not Found||


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|data||object||
|msg||string||
|status||integer(int32)|integer(int32)|
|success||boolean||


**响应示例**:
```javascript
{
	"data": {},
	"msg": "",
	"status": 0,
	"success": true
}
```


## 获取某个视频的点赞总数


**接口地址**:`/vlog/totalLikedCounts`


**请求方式**:`POST`


**请求数据类型**:`application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|vlogId|视频id|query|true|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|GraceJSONResult|
|201|Created||
|401|Unauthorized||
|403|Forbidden||
|404|Not Found||


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|data||object||
|msg||string||
|status||integer(int32)|integer(int32)|
|success||boolean||


**响应示例**:
```javascript
{
	"data": {},
	"msg": "",
	"status": 0,
	"success": true
}
```


## 用户取消点赞某个视频


**接口地址**:`/vlog/unlike`


**请求方式**:`POST`


**请求数据类型**:`application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|userId|用户id|query|true|string||
|vlogId|视频id|query|true|string||
|vlogerId|发布视频者的id|query|true|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|GraceJSONResult|
|201|Created||
|401|Unauthorized||
|403|Forbidden||
|404|Not Found||


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|data||object||
|msg||string||
|status||integer(int32)|integer(int32)|
|success||boolean||


**响应示例**:
```javascript
{
	"data": {},
	"msg": "",
	"status": 0,
	"success": true
}
```


# basic-error-controller


## error


**接口地址**:`/error`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


暂无


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK||
|401|Unauthorized||
|403|Forbidden||
|404|Not Found||


**响应参数**:


暂无


**响应示例**:
```javascript

```


## error


**接口地址**:`/error`


**请求方式**:`POST`


**请求数据类型**:`application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


暂无


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK||
|201|Created||
|401|Unauthorized||
|403|Forbidden||
|404|Not Found||


**响应参数**:


暂无


**响应示例**:
```javascript

```


## error


**接口地址**:`/error`


**请求方式**:`PUT`


**请求数据类型**:`application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


暂无


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK||
|201|Created||
|401|Unauthorized||
|403|Forbidden||
|404|Not Found||


**响应参数**:


暂无


**响应示例**:
```javascript

```


## error


**接口地址**:`/error`


**请求方式**:`DELETE`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


暂无


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK||
|204|No Content||
|401|Unauthorized||
|403|Forbidden||


**响应参数**:


暂无


**响应示例**:
```javascript

```


## error


**接口地址**:`/error`


**请求方式**:`PATCH`


**请求数据类型**:`application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


暂无


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK||
|204|No Content||
|401|Unauthorized||
|403|Forbidden||


**响应参数**:


暂无


**响应示例**:
```javascript

```


## error


**接口地址**:`/error`


**请求方式**:`OPTIONS`


**请求数据类型**:`application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


暂无


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK||
|204|No Content||
|401|Unauthorized||
|403|Forbidden||


**响应参数**:


暂无


**响应示例**:
```javascript

```


## error


**接口地址**:`/error`


**请求方式**:`HEAD`


**请求数据类型**:`application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


暂无


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK||
|204|No Content||
|401|Unauthorized||
|403|Forbidden||


**响应参数**:


暂无


**响应示例**:
```javascript

```