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
******************************

### `@TableName`

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

### `@TableId`

  * 作用：实体类属性与数据库主键字段的映射关系

    ```java
    @TableId(value = "id")
    private Long id;
    ```
  
  * type属性
  
    | 值           | 描述                                                         |
    | ------------ | ------------------------------------------------------------ |
    | AUTO         | 数据库自增(从数据库最后一项ID自增)                           |
    | NONE(默认值) | mybatis plus 雪花算法自己设置主键                            |
    | INPUT        | 需要开发者手动赋值                                           |
    | ASSIGN_ID    | mybatis plus 雪花算法自己设置ID。主键类型Long,Integer,String三种 |
    | ASSIGN_UUID  | mybatis plus 分配ID。主键类型只能用String                    |
  
    
  
    ```java
    //设置主键自增
    @TableId(type = IdType.AUTO)
    private Long id;
    ```

### `@TableField`

  * 作用：实体类属性与数据库非主键字段的映射关系

    ```java
    @TableField(value = "name")
    private String name;
    ```
  
  * exist属性：设置不是数据库字段的属性值
  
    * 这个特性很实用
    
    ```java
    //设置notExistColumn属性不是数据库字段
    @TableField(exist = false)
    ```
    
  * select属性：是否查询这个字段
  
    ```java
    //查询时要查name属性 
    @TableField(select = true)
    ```
  
  * fill属性：是否让mybatis plus自动赋值。比如说订单的时间戳
  
    | 值            | 描述               |
    | ------------- | ------------------ |
    | INSERT        | 只在插入时添加     |
    | UPDATE        | 只在更新时添加     |
    | INSERT_UPDATE | 当插入和更新时添加 |
    | DEFAULT       | 默认不添加         |
    
    ```JAVA
    @TableField(fill = FieldFill.INSERT)//创建的时候填入
    private Date createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)//创建和插入的时候填入
    private Date updateTime;
    ```
    * 完成后还要写一个怎样自动填充的类
    
      * 新版本的方法居然不能使用，要用老版本方法
      * 插入的时候要写更新，不然第一次插入updateTime没值
      
      ```java
      @Component
      public class MyMetaObjectHandler implements MetaObjectHandler {
          @Override
          public void insertFill(MetaObject metaObject) {
              //fieldName名是java的字段名
              this.setFieldValByName("createTime",new Date(),metaObject);
              this.setFieldValByName("updateTime",new Date(),metaObject);
              //this.strictInsertFill(metaObject,"createTime", LocalDateTime.class,LocalDateTime.now());
          }
      
          @Override
          public void updateFill(MetaObject metaObject) {
              this.setFieldValByName("updateTime",new Date(),metaObject);
              //this.strictUpdateFill(metaObject,"updateTime", LocalDateTime.class,LocalDateTime.now());
          }
      }
      ```

### `@Version`

  * 作用：添加乐观锁，防止多线程操作同一数据起冲突，保证数据的安全性

  * 本质：update user set version=2 where version=1。  实际就是一个自增的数。代表更改更新的次数

  * 使用：要给数据库添加一个version字段，且默认值为1。实体类还要用version属性，且配上@Version注解

    ```java
    @Version
    private Integer version;
    ```
    
  * 最后还要添加拦截器配置

    ```java
    @Configuration
    public class MybatisConfig {
        @Bean(name = "optimisticLockerInterceptor")
        public OptimisticLockerInterceptor getOptimisticLockerInterceptor(){
            return new OptimisticLockerInterceptor();
        }
    }
    ```

### `@EnumValue`

  * 作用：将数据库字段映射成实体类枚举类型成员变量
  
  * 使用步骤
  
    * 创建一个enum类，创建格式如下
    
      ```java
      public enum GenderEnum {
          男(1,"男"),
          女(0,"女");
      
          GenderEnum(Integer code, String msg) {
              this.code = code;
              this.msg = msg;
          }
      
          @EnumValue
          private Integer code;
          private String msg;
      }
      ```
      
    * 也可以实现接口。核心是返回integer类型的code与数据库字段对应
      
      ```java
      public enum  GenderEnum implements IEnum<Integer> {
          男(1,"男"),
          女(0,"女");
      
          GenderEnum(Integer code, String msg) {
              this.code = code;
              this.msg = msg;
          }
      
          @EnumValue
          private Integer code;
          private String msg;
      
          @Override
          public Integer getValue() {
              return this.code;
          }
      }
      ```
      
    * 数据库表中添加gender字段，类型为int，因为枚举里面的code是Integer
    
    * 在实体类中添加gender字段。可以添加@TableField映射属性与数据库字段
    
      ```java
      @TableField("gender")
      private GenderEnum gender;
      ```
      
    * 修改yml的内容，配置枚举扫描包
    
      ```yaml
      mybatis-plus:
        type-enums-package: com.demo.enums
      ```

### `@TableLogic`

* 作用：设置deleted字段，不真正删除数据库的内容，修改字段来代表删除

* 注意：虽然实际上数据库表存在，但是删除了在差肯定查不出来

* 使用步骤

  * 数据库添加deleted字段，默认值设置为0，类型为Interger

  * 实体类添加属性和注解

    ```java
    @TableLogic
    private Integer deleted;
    ```

  * 修改yml文件改配置

    ```yaml
    mybatis-plus:
      global-config:
        db-config:
          logic-delete-value: 1
          logic-not-delete-value: 0
    ```

### bug

  * 主键属性设置成Long而不是Integer会报属性没有默认值错
***********************

### VO

* 目的：根据前端需求需要啥数据就做一个拼接给它，而不是全部都查询

* 举例：有两个实体类User和Account，前端只需要User类全体属性和Account类的金额属性，其他Acount交易记录啥的都不要，就可以创建一个UserVO类，而不是两个类关联做多表查询

  ```java
  public class UserVo {
      private Long id;
      private String name;
      private Integer age;
      private String email;
      //账户类的余额
      private Double money;
  }
  ```

**********************

### 查询

* eq方法使用 (查询条件为枚举类型)

  ```java
  QueryWrapper wrapper=new QueryWrapper();
  wrapper.eq("gender", GenderEnum.男);
  List list = userMapper.selectList(wrapper);
  ```
  
* allEq方法使用 (要使用map)

  ```java
  QueryWrapper wrapper=new QueryWrapper();
  Map<String,Object> map=new HashMap<>();
  map.put("gender",GenderEnum.女);
  map.put("name","Izumi Sakai");
  wrapper.allEq(map);
  List list = userMapper.selectList(wrapper);
  ```
  
* lt()小于；gt()大于；ne()不等于；ge()大于等于；le()小于等于   使用方法类似

* 模糊查询

  * like()——左右都加%
  * likeLeft()——左边加百分号
  * ikeRight()——右边加百分号

  ```java
  wrapper.like("name","zumi");
  ```
  
* IN查询  (就相当于SQL中的in关键字)

  ```java
  wrapper.inSql("name","select name from user where name like '%zumi%'");
  ```
  
* 排序和having

  ```java
  wrapper.orderByAsc("age");
  wrapper.having("id>8");
  ```
  
* 根据ID查询

  ```java
  wrapper.selcetById(7);
  ```
  
* 查询count

  ```java
int count=wrapper.selectCount(wrapper);
  ```
  
* 查询封装为Map集合。返回的是一个List，List里面的元素是Map
  
  ```java
  List maps = userMapper.selectMaps(wrapper);
  ```
  
* 分页查询
  
  * 添加Page配置类
  
    ```java
    @Configuration
    public class MybatisConfig {
        @Bean(name = "paginationInterceptor")
        public PaginationInterceptor getPaginationInterceptor(){
            return new PaginationInterceptor();
        }
    }
    ```
    
  * 查询
  
    ```java
    QueryWrapper wrapper=new QueryWrapper();
    wrapper.gt("id",0);\
    //每三个分一页，查询第一页
    Page<User> page=new Page<>(1,3);
    //查询时传入page和wrapper
    Page resultPage = userMapper.selectPage(page, wrapper);
    //通过getRecords获得结果
    List records = resultPage.getRecords();
    System.out.println(records);
    ```
**********************
### 多表关联查询（自定义SQL）

*  mybatis plus不能自动生成多表查询，还是要自己写

*  mybatis plus不要在一个类中写另一个对象作为属性，这种写法要写resultMap不方便

* 步骤

  * 编写一个UserVO类

    ```java
    @Data
    public class UserVo {
        private Integer id;
        private String name;
        private Integer age;
        private String email;
        //账户类的余额
        //注意换了数据库别名
        @TableField("account_money")
        private Double accountMoney;
    }
    ```
  
* 在UserMapper中些SQL语句
  
  * 参数可以要也可以不要，如果使用参数则用#{}获取参数内容
    
    * `a.money as account_money`这一句很关键，做好实体属性和查询数据库字段之间的映射很重要。不然会出现null
  * 还可以使用左外连接等实现丰富结果
  
    ```java
    public interface UserMapper extends BaseMapper<User> {
        @Select("select u.*,a.money as account_money from user u,account a where u.id=a.user_id and u.id>#{id}")
        List<UserVo> findAllUserVO(Integer id);
    }
    ```
*****************
### 添加

```java
userMapper.insert(user);
```
*********************
### 删除

* 根据查询条件删除

  ```java
  userMapper.delete(wrapper);
  ```

* 根据ID删除

  ```java
  userMapper.deleteById(3);
  ```
  
* 根据很多ID删除

  ```java
  userMapper.deleteBatchIds(Arrays.asList(1,2));
  ```
******************
### 更新

* 根据ID更新 (要求传入的user的id属性值不能为空)

  ```java
  User user = userMapper.selectById(8);
  user.setAge(40);
  userMapper.updateById(user);
  ```
**************
### 自动生成

* 导入依赖

  ```XML
  <dependency>
      <groupId>com.baomidou</groupId>
      <artifactId>mybatis-plus-generator</artifactId>
      <version>3.3.2</version>
  </dependency>
  <dependency>
      <groupId>org.apache.velocity</groupId>
      <artifactId>velocity</artifactId>
      <version>1.7</version>
  </dependency>
  ```
  
* 写代码做一些配置自动生成

  ```Java
  public class Main {
      public static void main(String[] args) {
          // 代码生成器
          AutoGenerator mpg = new AutoGenerator();
  
          // 全局配置
          GlobalConfig gc = new GlobalConfig();
          gc.setOutputDir(System.getProperty("user.dir") + "/src/main/java");
          gc.setAuthor("Izumi Sakai");
          gc.setOpen(false);
          // gc.setSwagger2(true); 实体属性 Swagger2 注解
          mpg.setGlobalConfig(gc);
  
          // 数据源配置
          DataSourceConfig dsc = new DataSourceConfig();
          dsc.setUrl("jdbc:mysql://localhost:3306/shiro-springboot?useUnicode=true&useSSL=false&characterEncoding=utf8");
          // dsc.setSchemaName("public");
          dsc.setDriverName("com.mysql.jdbc.Driver");
          dsc.setUsername("root");
          dsc.setPassword("542270191MSzyl");
          mpg.setDataSource(dsc);
  
          // 包配置
          PackageConfig pc = new PackageConfig();
          pc.setModuleName("generator");
          pc.setParent("com.demo");
          mpg.setPackageInfo(pc);
  
          // 策略配置
          StrategyConfig strategy = new StrategyConfig();
          strategy.setNaming(NamingStrategy.underline_to_camel);
          strategy.setColumnNaming(NamingStrategy.underline_to_camel);
          strategy.setEntityLombokModel(true);
          strategy.setRestControllerStyle(true);
          mpg.setStrategy(strategy);
  
          mpg.execute();
      }
  }
  ```
**************
### 多模块SpringBoot写法

* 示例

  * 最后访问地址为“localhost:8080//generator/user/index”。访问到的HTML为thymeleaf默认根路径下的index。html
  * 这种写法`@RequestMapping("/generator/user")`可以把前面的前缀固定死
  * 返回的不再是String而是ModelAndView

  ```java
  @RestController
  @RequestMapping("/generator/user")
  public class UserController {
      @Autowired
      private UserServiceImpl userService;
  
      @RequestMapping(value = "/index",method = RequestMethod.GET)
      public ModelAndView index(){
          ModelAndView view=new ModelAndView();
          view.addObject("users",userService.list());
          view.setViewName("index");
          return view;
      }
  }
  ```