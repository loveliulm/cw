# 企万万慧-财务软件源码（开源版）

#### 介绍
企万慧开源财务软件，一款面向中小企业的轻量级财务管理工具。支持凭证管理、账簿报表、应收应付、收支流水等核心功能，界面简洁易上手。基于主流开源技术栈开发，部署灵活、可扩展性强，欢迎社区贡献与二次开发。
### 开源版地址
企万慧开源版<br>
http://cwgl.qwh365.com （开源演示版）<br>
测试账号：18329078628/123456

# 开源项目截图
![输入图片说明](.gitee/003.png)![输入图片说明](.gitee/001.png)![输入图片说明](.gitee/002.png)![输入图片说明](.gitee/004.png)![输入图片说明](.gitee/006.png)![输入图片说明](.gitee/005.png)

#### 安装教程
## 运行项目

### 安装Mysql数据库，并导入初始数据
~~~
创建qifu-dev数据库，把《qifu-dev.sql》导入数据库
~~~

### 启动后端项目(qf-Service)

修改 application.yml 文件，修改qifu.profile文件上传路径和redis配置

~~~~
# 项目相关配置
qifu:
  # 名称
  name: qf
  # 版本
  version: 1.0.0
  # 版权年份
  copyrightYear: 2020
  # 实例演示开关
  demoEnabled: true
  # 文件路径 示例（ Windows配置D:/qifu/uploadPath，Linux配置 /home/qifu/uploadPath）
  profile: D:/qifu/uploadPath

~~~

# redis 配置
  redis:
    # 地址
    host: 127.0.0.1
    # 端口，默认为6379
    port: 6379
    # 密码
    password:

~~~~
修改 application-dev.druid 文件，修改Mysql数据库配置
~~~
# 数据源配置
spring:
    datasource:
        type: com.alibaba.druid.pool.DruidDataSource
        driverClassName: com.mysql.cj.jdbc.Driver
        druid:
            # 主库数据源
            master:
                url: jdbc:mysql://localhost:3306/qifu-dev?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8
                username: root
                password: root
~~~
运行项目启动类 com.qifu.QiFuApplication.java

```
浏览器访问swagger地址：http://localhost:8081/swagger-ui.html（可以打开说明后台服务启动成功）
```



技术交流群
扫码添加客服进群

## 🤝 社区

**遇到问题？想交流技术？数据库下载？来这里：**

<table align="center">
  <tr>
    <td align="center" width="33%">
      <img src="/.gitee/二维码.png" width="180" height="180" alt="微信群二维码"/><br>
      <b>💬 微信群</b>
    </td>
    <td align="center" width="33%">
      <img src="./.gitee/二维码.png" width="180" height="180" alt="公众号二维码"/><br>
      <b>📱 公众号：企万慧</b>
    </td>
  </tr>
</table>

<p align="center" style="margin-top: 30px;">
  <a href="https://gitee.com/qi-wanhui/financial-software"><b>⭐ 点个 Star 支持项目</b></a>（你的鼓励是持续更新的动力 ❤️）
</p>

---
---

## 📄 许可证

使用本项目代码请遵循GPL3协议，欢迎大家一起进行技术交流和学习，
如需有商业应用需求，可联系我们为您提供个性化的财务软件源码和需求定制方案！

---

<p align="center">
  <b>如果 企万慧对你有帮助，欢迎在 Gitee 上点个 Star ⭐</b><br>
  <sub>你的每一次 Star，都是让这个项目变得更好的动力</sub>
</p>
