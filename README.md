本系统是为学习开发而模拟的银行信贷管理系统，不可当真，如有雷同，纯属巧合。



tiger bank是一家银行，主要负责给企业发放贷款，本系统旨在帮助业务人员更好地完成信贷业务。

系统目前主要分为客户管理，贷款管理，还款管理，系统管理四大功能模块。（暂时只提供核心业务，未来可以增加更多业务功能）核心业务流程为银行员工登录系统，先通过客户管理模块维护融资企业客户信息。然后再使用贷款管理模块，通过调用客户管理模块，查询客户信息，给融资企业添加贷款信息，最后由管理员用户进行审批。最后使用还款模块，通过调用贷款模块，查询相应的贷款信息，减去还款金额，维护剩余的贷款信息。最终完成整个信贷业务。

客户管理主要是维护贷款客户信息，包括企业名称，统一社会信用代码，注册地址等。贷款管理主要是维护贷款相关信息，包括产品名称，企业id，利率，期限等，且需要计算剩余金额。还款管理主要是维护还款信息，包括企业id，贷款项目id，还款金额等，也需要通过贷款管理模块维护贷款相关信息，包括未偿还金额等。系统管理主要是维护系统用户信息，进行登录校验，权限控制等。

数据库有五张表，客户信息表（主要维护企业id，名称，统一社会信用代码，注册地址，注册资本，营业范围等），贷款信息表（主要维护贷款项目id，产品名称，企业id，利率，期限，剩余未还金额等），放款记录表（主要维护放款记录id，贷款项目id，企业id，放款金额等），还款记录表（主要维护还款记录id，企业id，贷款项目id，还款金额）。

项目代码结构按照开发指导手册要求，系统项目名为tiger-bank-management，客户管理模块工程名为customer-management，贷款管理模块工程名为lending-management，还款管理模块工程名为repayment-management，每个模块都使用Springboot开发框架，使用mybatis进行数据库交互，并通过maven进行项目构建。数据库用mysql。前端使用Vue框架。接口文档使用swagger生成。

命名规范参照开发指导手册，service类放在facade包，实体类和Dao类放在dal包，controller类放在ws包，service类dto和controller类vo的参数放在model包下。方法命名上增删改前缀为add，delete，update，如果方法要用到属性就在后面加上By属性名。查询单体记录用get，所有记录用list，分页记录用page（分页参数名start起始页，limit每页记录数）。返回给前端的响应体统一用Result类（包括code，data，message，success，timestamp，traceId这几个属性)。业务异常统一用ServiceException类处理（包括code，cause，params这几个属性）

包结构如下
tiger-bank-management/
├── customer-management/
│   └── src/main/java/org/zoo/
│       ├── ws/             # Controller类
│       ├── facade/         # Service接口及实现
│       ├── dal/            # 实体类和Dao接口
│       └── model/          # DTO和VO
├── lending-management/
│   └── src/main/java/org/zoo/
│       ├── ws/
│       ├── facade/
│       ├── dal/
│       └──model/
└── repayment-management/
    └── src/main/java/org/zoo/
        ├── ws/
        ├── facade/
        ├── dal/
        └── model/

项目预期
[//]: # (V1.0.0)

[//]: #
[//]: # (单体项目，功能模块分为三个module，共用一个父工程启动类。)

[//]: # (V2.0.0)

[//]: #
[//]: # (微服务项目，每个功能模块为一个单独的服务并可以部署到不同的物理机上，通过微服务组件进行交互。另外项目是微服务架构，因此采用了Spring Cloud框架和Eureka服务注册发现组件，openFeign服务调用组件，nacos服务配置中心等组件。)

