# YiXin-Bms
招募信息业务管理系统-后端Java工程

初始化版本: 1.0



## 项目结构
``` 
yixin-xiaoyi
├─ xiaoyi-server          // 管理模块 [8080]
│  └─ YiXinBmsApplication                 // 启动类
│  └─ YiXinBmsServletInitializer          // 容器部署初始化类
│  └─ resources                        // 资源文件
│      └─ i18n/messages.properties     // 国际化配置文件
│      └─ application.yml              // 框架总配置文件
│      └─ application-dev.yml          // 开发环境配置文件
│      └─ application-prod.yml         // 生产环境配置文件
│      └─ banner.txt                   // 框架启动图标
│      └─ logback.xml                  // 日志配置文件
│      └─ spy.properties               // p6spy配置文件
├─ xiaoyi-job            // 任务调度服务
├─ yixin-bms-extend         // 扩展模块
│  └─ yixin-bms-monitor-admin              // admin监控模块  [8089]
│  └─ yixin-bms-xxl-job-admin              // 任务调度中心模块 [8088]
├─ xiaoyi-system         // 业务模块
│  └─ domain                           // 系统数据对象
│  └─ mapper                           // 系统mapper
│  └─ service                          // 系统业务接口
│      └─ impl                         // 系统业务接口实现类
│  └─ resources
│      └─ mapper/system                // 系统业务XML
├─ xiaoyi-common         // 通用模块
│  └─ annotation                       // 通用注解
│      └─ Anonymous                    // 匿名访问不鉴权注解
│      └─ CellMerge                    // excel列合并
│      └─ DataColumn                   // 数据权限字段绑定
│      └─ DataPermission               // 数据权限组
│      └─ ExcelDictFormat              // 字典格式化
│      └─ Log                          // 自定义操作日志记录注解
│      └─ RateLimiter                  // 自定义限流注解
│      └─ RepeatSubmit                 // 自定义注解防止表单重复提交
│      └─ Sensitive                    // 数据脱敏注解
│  └─ captcha                          // 验证码相关
│  └─ config                           // 通用配置
│  └─ constant                         // 通用常量
│  └─ convert                          // 转换器
│  └─ core                             // 通用核心代码
│      └─ controller                   // web层通用数据处理
│      └─ domain                       // 通用实体
│      └─ page                         // 分页实体
│      └─ mapper                       // 通用mapper
│      └─ validate                     // 通用校验类
│      └─ service                      // 通用接口
│  └─ enums                            // 枚举包    
│  └─ exception                        // 异常包
│  └─ excel                            // 异常包
│  └─ filter                           // 过滤器包
│  └─ helper
│  └─ jackson                          // json序列化工具
│  └─ xss                              // xss过滤拦截
│  └─ utils                            // 工具类包
├─ xiaoyi-framework      // 核心配置模块
│  └─ aspectj                          // aop相关处理
│  └─ config                           // 系统配置相关
│      └─ properties                   // 配置映射包
│      └─ ApplicationConfig            // 框架配置
│      └─ AsyncConfig                  // 异步配置
│      └─ CaptchaConfig                // 验证码配置
│      └─ DruidConfig                  // druid 配置多数据源
│      └─ FilterConfig                 // filter 配置
│      └─ I18nConfig                   // 国际化配置
│      └─ JacksonConfig                // jackson 序列化配置
│      └─ MailConfig                   // JavaMail 配置
│      └─ MybatisPlusConfig            // mybatis-plus 配置
│      └─ RedisConfig                  // redis 配置
│      └─ ResourcesConfig              // 通用资源配置
│      └─ SaTokenConfig                // sa-token 配置
│      └─ SwaggerConfig                // Swagger 文档配置
│      └─ ThreadPoolConfig             // 线程池配置
│      └─ TLogConfig                   // TLog 分布式日志配置
│      └─ UndertowConfig               // Undertow 自定义配置
│      └─ ValidatorConfig              // 校验框架配置
│  └─ jackson                          // jackson相关
│  └─ manager                          // 管理器相关
│  └─ handler                          // 处理器相关
│  └─ Interceptor                      // 拦截器相关
│  └─ satoken                          // 权限鉴权相关
│  └─ web                              // 全局异常处理器
├─ script                // 脚本+文档目录
│  └─ bin                              // 运行脚本包
│  └─ sql                              // sql脚本
├─ .editorconfig        // 编辑器编码格式配置
├─ pom.xml              // 公共依赖
├─ README.md            // 框架说明文件
```
