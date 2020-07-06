# MybatisPlusLearninig
学习MybatisPlus

###  简介
* 简单介绍: 不改变mybatis，只做增强不做改变。目的是做mybatis最好的搭档
* 官网：`https://mp.baomidou.com/`
* mybatis-plus是国产开发框架，并没有接入到spring官方孵化器中，因此springboot构造器不可能有
* mapper实现机制：全部动态代理，mapper里面啥都不用写，只用继承

***************

### Lombok

* 作用：自动生成get和set等简化开发

* 举例

  ```java
  @Data
  public class User {
      private Long id;
      private String name;
      private Integer age;
      private String email;
  }
  ```
  
* 查看方式

  * 进入IDEA的structure查看
  
  

### HelloWord

* 实体类全部使用引用类，因为 int, boolean有默认值，有时会自动赋值

* 导入依赖

  * 不用导入mybatis framework和mybatis-spring，可能会有冲突

  ```xml
  <dependency>
      <groupId>com.baomidou</groupId>
      <artifactId>mybatis-plus-boot-starter</artifactId>
      <version>3.3.2</version>
  </dependency>
  ```
  
* 修改yml文件

  *  mybatis-plus. configuration. log-impl加上会打印详细的日志信息
  
  ```yaml
  spring:
    datasource:
      username: root
      password: 542270191MSzyl
      url: jdbc:mysql://localhost:3306/mybatis-plus?useUnicode=true&characterEncoding=utf8
      type: com.alibaba.druid.pool.DruidDataSource
      druid:
        initial-size: 5
        min-idle: 5
        max-active: 20
        max-wait: 40000
        time-between-eviction-runs-millis: 60000
        min-evictable-idle-time-millis: 30000
        validation-query: selcet 1 from dual
        test-while-idle: true
        test-on-borrow: false
        test-on-return: false
        pool-prepared-statements: true
   mybatis-plus:
    configuration:
      log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
  ```
  
* `MainApplication`类加上mapper包扫描注解`@MapperScan("com.demo.mapper")`
  
* 编写实体类

  * 使用了lombok简化编写

  ```java
  @Data
  public class User {
      private Long id;
      private String name;
      private Integer age;
      private String email;
  }
  ```
  
* 编写Mapper

  * 什么都不用写，直接继承传个泛型，继承个父类就行

  ```java 
  public interface UserMapper extends BaseMapper<User> {
  
  }
  ```
  
* Conroller测试

  * 因为是配置的主类扫描，因此usermapper注入会报红，但是没有影响
  * UserMapper 中的 `selectList()` 方法的参数为 MP 内置的条件封装器 `Wrapper`，所以不填写就是无任何条件

  ```java
  @Controller
  public class UserController {
      @Autowired
      private UserMapper userMapper;
  
      @ResponseBody
      @RequestMapping("/")
      private String testMybatis(){
          List<User> users = userMapper.selectList(null);
          return users.toString();
      }
  }
  ```
*******************
### 常用注解

* `@TableName`

  * 作用：实体类名与数据库表名的映射关系。

  * 举例：把User类映射到user表

    ```java
    @Data
    @TableName(value = "user")
    public class User {
        private Long id;
        private String name;
        private Integer age;
        private String email;
    }
    ```
  
* `@TableId`

  * 作用：实体类属性与数据库主键字段的映射关系

  * type属性

    | 值           | 描述                                                         |
    | ------------ | ------------------------------------------------------------ |
    | AUTO         | 数据库自增(从数据库最后一项ID自增)                           |
    | NONE(默认值) | mybatis plus 雪花算法自己设置主键                            |
    | INPUT        | 需要开发者手动赋值                                           |
    | ASSIGN_ID    | mybatis plus 雪花算法自己设置ID。主键类型Long,Integer,String三种 |
    | ASSIGN_UUID  | mybatis plus 分配ID。主键类型只能用String                    |
  
    
  
  * 举例：id属性映射到id主键，且主键自增
  
    ```java
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;
    ```
* `@TableField`

  * 作用：实体类属性与数据库非主键字段的映射关系

  * exist属性：设置不是数据库字段的属性值

    * 这个特性很实用
  * select属性：是否查询这个字段
  
  * 举例：
  
    ```java
    //name属性映射到name列，且查询时要查name属性 
    @TableField(value = "name",select = true)
    private String name;
    //设置notExistColumn属性不是数据库字段1
    @TableField(exist = false)
    private String notExistColumn;
    ```