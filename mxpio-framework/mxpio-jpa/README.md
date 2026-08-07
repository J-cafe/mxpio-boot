# mxpio-jpa

## 简介

mxpio-jpa 是基于 JPA (Jakarta Persistence API) 与 Spring Data JPA 构建的数据访问增强框架。它在 JPA 之上提供了一套类型安全的链式查询 DSL、智能 CRUD、关联数据收集回填、以及前后端统一的 JSON 查询协议，目标是大幅简化数据密集型应用的数据访问代码。

本模块不替代 Spring Data JPA——它运行在 JPA 之上，你可以随时回退到原生 JPA API 或 Spring Data JPA。

## 核心特性

- **链式查询 DSL**：`Linq`（查）、`Lind`（删）、`Linu`（改）三件套，代码结构与 SQL 相近但更优雅
- **Lambda 类型安全**：`User::getName` 替代字符串属性名，编译期校验
- **跨表查询**：EXISTS/IN 子查询 + collect 批量回填，避免 JOIN 和 N+1
- **智能 CRUD**：递归处理对象树（级联保存/更新/删除），`@Generator` 注解自动生成字段值
- **JSON 查询协议**：`Criteria` 对象支持 Jackson 序列化，前后端使用同一套查询语义
- **多数据源透明路由**：按实体类自动匹配所属数据源，业务代码无感知
- **可扩展策略体系**：CRUD 策略、条件翻译策略、字段生成策略、条件解析器链、结果过滤器

## 快速入门

### Maven 依赖

```xml
<dependency>
    <groupId>com.mxpio</groupId>
    <artifactId>mxpio-jpa</artifactId>
    <version>${mxpio.version}</version>
</dependency>
```

### 第一个查询

```java
// 查询所有年龄大于 18 的用户，按创建时间降序
List<User> users = JpaUtil.linq(User.class)
    .gt("age", 18)
    .desc("createTime")
    .list();

// Lambda 风格（类型安全）
List<User> users = JpaUtil.linq(User.class)
    .gt(User::getAge, 18)
    .desc(User::getCreateTime)
    .list();
```

### 第一个保存

```java
// 智能保存：自动判断 insert/update，级联处理关联对象
JpaUtil.save(user);

// 批量保存
JpaUtil.save(userList);
```

---

## 一、查询 DSL（Linq）

所有查询以 `JpaUtil.linq(实体类.class)` 为入口，返回 `Linq` 对象，支持链式调用。

### 1.1 基本查询

```java
// 查询所有
List<User> users = JpaUtil.linq(User.class).list();

// 按主键查询
User user = JpaUtil.getOne(User.class, "001");

// 查询一条
User user = JpaUtil.linq(User.class).equal("name", "张三").findOne();

// 查询条数
Long count = JpaUtil.linq(User.class).gt("age", 18).count();

// 判断是否存在
boolean exists = JpaUtil.linq(User.class).equal("name", "张三").exists();
```

### 1.2 条件操作符

支持全部常用比较操作，每种都提供字符串属性名和 Lambda 属性引用两个版本：

```java
JpaUtil.linq(User.class)
    .equal("name", "张三")           // =
    .notEqual("status", 0)           // !=
    .gt("age", 18)                   // >
    .ge("age", 18)                   // >=
    .lt("age", 60)                   // <
    .le("age", 60)                   // <=
    .between("age", 18, 60)          // BETWEEN
    .like("name", "%张%")            // LIKE
    .notLike("name", "%测试%")       // NOT LIKE
    .in("deptId", "d1", "d2")       // IN
    .notIn("status", 0, 1)          // NOT IN
    .isNull("remark")                // IS NULL
    .isNotNull("email")              // IS NOT NULL
    .isTrue("married")               // = true
    .isFalse("deleted")              // = false
    .isEmpty("children")             // 集合为空
    .isNotEmpty("children")          // 集合非空
    .list();
```

### 1.3 动态条件（addIf / addIfNot）

```java
String name = request.getName();     // 可能为空
Integer age = request.getAge();      // 可能为 null
List<String> deptIds = request.getDeptIds();  // 可能为空集合

JpaUtil.linq(User.class)
    .addIf(name)                     // name 非空时条件生效
        .like("name", "%" + name + "%")
    .endIf()
    .addIf(age)                      // age 非 null 时条件生效
        .ge("age", age)
    .endIf()
    .addIf(deptIds)                  // deptIds 非空时条件生效
        .in("deptId", deptIds)
    .endIf()
    .addIfNot(deptIds)               // deptIds 为空时条件生效
        .equal("deptId", "default")
    .endIf()
    .list();
```

判断规则：
- `String`：null 或 `""` 为无效
- `Collection`：null 或空集合为无效
- `Boolean`：false 为无效
- 其他对象：null 为无效

### 1.4 嵌套条件（and / or）

```java
// WHERE age > 18 AND (married = true OR (salary >= 5000 AND salary <= 20000))
JpaUtil.linq(User.class)
    .gt("age", 18)
    .or()
        .isTrue("married")
        .and()
            .ge("salary", 5000)
            .le("salary", 20000)
        .end()
    .end()
    .list();
```

注意：复杂条件后如果是执行方法（`list()`、`count()` 等），末尾的 `end()` 可省略。

### 1.5 投影查询（select）

```java
// 只查部分字段，返回实体对象（未选字段为 null）
List<User> users = JpaUtil.linq(User.class)
    .select("id", "name", "age")
    .list();

// 投影到 Map（需先调用 aliasToMap）
List<Map<String, Object>> maps = JpaUtil.linq(User.class)
    .aliasToMap()
    .select("id", "name", "age")
    .list();

// 投影到自定义 Bean（需先调用 aliasToBean）
List<UserDTO> dtos = JpaUtil.linq(User.class)
    .aliasToBean(UserDTO.class)
    .select("id", "name", "age")
    .list();

// 投影到 Tuple
List<Tuple> tuples = JpaUtil.linq(User.class)
    .aliasToTuple()
    .select("id", "name")
    .list();
```

### 1.6 排序与分页

```java
// 排序
JpaUtil.linq(User.class)
    .asc("age")                      // 升序
    .desc("createTime", "name")      // 多字段降序
    .list();

// 分页查询（返回 Page 对象，含总记录数）
Page<User> page = JpaUtil.linq(User.class)
    .gt("age", 18)
    .paging(PageRequest.of(0, 20));

// 分页不查总数（更轻量）
List<User> list = JpaUtil.linq(User.class)
    .gt("age", 18)
    .list(PageRequest.of(0, 20));
```

### 1.7 去重

```java
JpaUtil.linq(User.class)
    .select("deptId")
    .distinct()
    .list();
```

---

## 二、更新 DSL（Linu）

入口为 `JpaUtil.linu(实体类.class)`，返回 `Linu` 对象。

```java
// 条件批量更新：将年龄小于 18 的用户工资设为 0
int count = JpaUtil.linu(User.class)
    .lt("age", 18)
    .set("salary", 0)
    .update();

// Lambda 风格
int count = JpaUtil.linu(User.class)
    .lt(User::getAge, 18)
    .set(User::getSalary, 0)
    .update();

// 多字段更新
int count = JpaUtil.linu(User.class)
    .equal("deptId", "d1")
    .set("deptId", "d2")
    .set("status", 1)
    .update();
```

---

## 三、删除 DSL（Lind）

入口为 `JpaUtil.lind(实体类.class)`，返回 `Lind` 对象。

```java
// 条件批量删除
int count = JpaUtil.lind(User.class)
    .lt("age", 18)
    .delete();

// 删除所有记录
JpaUtil.removeAllInBatch(User.class);
```

---

## 四、CRUD 操作

### 4.1 标准操作

```java
// 单条
JpaUtil.persist(user);               // 新增
JpaUtil.merge(user);                 // 更新
JpaUtil.remove(user);                // 删除
JpaUtil.persistAndFlush(user);       // 新增并刷入数据库
JpaUtil.mergeAndFlush(user);         // 更新并刷入数据库

// 批量
JpaUtil.persist(userList);
JpaUtil.merge(userList);
JpaUtil.remove(userList);
```

### 4.2 智能操作（推荐）

`save()`、`update()`、`delete()` 是智能方法，内部使用 `DirtyTreeCrudPolicy` 递归处理对象树：

```java
// 智能保存：自动判断 insert/update，递归处理关联对象
JpaUtil.save(user);
JpaUtil.save(userList);

// 智能更新
JpaUtil.update(user);
JpaUtil.update(userList);

// 智能删除
JpaUtil.delete(user);
JpaUtil.delete(userList);

// 带自定义策略
JpaUtil.save(user, myCrudPolicy);
JpaUtil.update(user, myCrudPolicy);
JpaUtil.delete(user, myCrudPolicy);
```

**智能 CRUD 的执行流程**：
1. 检查实体字段上的 `@Generator` 注解，执行字段生成策略（UUID、创建时间、更新时间等）
2. 递归扫描实体类中的关联属性（单对象和 Collection）
3. 逐层下钻，对每个子对象执行相应的 CRUD 操作
4. 实现级联保存/更新/删除，无需手动处理

### 4.3 实体意图标记（MxpioEntity）

实体可以实现 `MxpioEntity` 接口来表达自身的 CRUD 意图：

```java
public class Order implements MxpioEntity {

    @Transient
    private CrudType crudType = CrudType.SAVE;  // 新增意图

    @Override
    public CrudType getCrudType() {
        return crudType;
    }

    @Override
    public boolean isSaveTransient() {
        return true;  // 是否级联持久化关联属性
    }
}
```

`DirtyTreeCrudPolicy` 会根据 `getCrudType()` 判断每个实体的操作类型（SAVE/UPDATE/DELETE），并按 `isSaveTransient()` 决定是否级联处理关联属性。

---

## 五、跨表查询

本模块核心理念：**主查询只查主表，跨表条件通过 EXISTS/IN 子查询在 SQL 层完成，关联数据通过 collect 批量回填在应用层完成**。避免使用 JOIN，规避 N+1 问题。

### 5.1 EXISTS 子查询（手写）

适合需要精细控制子查询条件的场景：

```java
// 查询"属于某菜单下、且角色为 admin 的所有权限"
List<Permission> permissions = JpaUtil.linq(Permission.class)
    .equal("roleId", roleId)
    .equal("resourceType", ResourceType.ELEMENT)
    .exists(Element.class)                         // 开启 EXISTS 子查询
        .equalProperty("id", "resourceId")         // 关联: Element.id = Permission.resourceId
        .equal("parentId", menuId)                 // 子查询内部条件
    .end()                                          // 结束子查询，返回主查询
    .list();
```

生成的 SQL 近似：

```sql
SELECT * FROM permission p
WHERE p.role_id = ? AND p.resource_type = ?
AND EXISTS (
    SELECT e FROM element e
    WHERE e.id = p.resource_id AND e.parent_id = ?
)
```

类似方法：`notExists(Class)` 生成 `NOT EXISTS` 子查询。

### 5.2 IN 子查询

适合"主表某字段值属于另一表结果集"的场景：

```java
// 查询属于某部门的所有用户的订单
List<Order> orders = JpaUtil.linq(Order.class)
    .in("userId", User.class)                      // userId IN (SELECT id FROM User WHERE ...)
        .equal("deptId", "d1")
    .end()
    .list();

// in(Class) 简写：主键 IN 子查询
List<User> users = JpaUtil.linq(User.class)
    .in(Role.class)                                // id IN (SELECT id FROM Role WHERE ...)
        .equal("roleName", "admin")
    .end()
    .list();
```

### 5.3 属性对属性比较（关联子查询专用）

`equalProperty`、`gtProperty`、`leProperty` 等方法在子查询内使用时自动形成关联子查询：

```java
.exists(Element.class)
    .equalProperty("id", "resourceId")             // 子查询.id = 父查询.resourceId
    .gtProperty("order", "sortOrder")              // 子查询.order > 父查询.sortOrder
```

若不在子查询内，则退化为同表两列比较。

### 5.4 Criteria 条件对象 + SubQueryParser（JSON 驱动跨表）

前端传递带点号路径的 JSON 条件，后端自动转换为 EXISTS 子查询：

```java
// 前端条件: { "fieldName": "roleId.name", "operator": "LIKE", "value": "admin" }
// SubQueryParser 检测到 "roleId.name" 含点号且 roleId 匹配外键
// → 自动生成: EXISTS (SELECT r FROM Role r WHERE r.id = 父.roleId AND r.name LIKE '%admin%')

Linq linq = JpaUtil.linq(User.class)
    .addSubQueryParser(Role.class, "roleId")        // 手动注册：指定外键属性名
    .where(criteria)
    .list();
```

`SubQueryParser` 匹配规则：条件属性名（点号前部分）与外键属性名互相 `startsWith` 匹配即触发。默认外键名 = 实体类名首字母小写 + 主键名大写（如 `roleId`）。

### 5.5 SmartSubQueryParser（全自动，推荐）

当使用 `where(Criteria)` 且执行了 collect 声明时，SmartSubQueryParser 自动为主实体上所有实体类型属性生成 SubQueryParser，**无需手动注册**：

```java
// Dict 实体有 dictItems 属性 (List<DictItem>)，collect 声明了关联
// 前端条件 "dictItems.itemText" LIKE "测试" 自动转换为 EXISTS 子查询

JpaUtil.linq(Dict.class)
    .collect("dictId", DictItem.class, "id")        // collect 声明触发自动子查询
    .where(criteria)                                 // SmartSubQueryParser 自动挂载
    .paging(pageable);
```

**触发条件**：执行查询（list/findOne/paging 等）+ 使用了 `where(Criteria)` + 未调用 `setDisableSmartSubQueryCriterion()`。

**禁用智能子查询**（当条件属性名可能误匹配时）：

```java
JpaUtil.linq(User.class)
    .setDisableSmartSubQueryCriterion()              // 关闭自动子查询解析
    .where(criteria)
    .list();
```

### 5.6 跨表查询方式对比

| 方式 | 适用场景 | SQL 层面 | N+1 风险 |
|---|---|---|---|
| EXISTS 子查询 | 需要"存在性"判断的筛选条件 | EXISTS 子查询 | 无 |
| IN 子查询 | 主表字段值属于另一表结果 | IN 子查询 | 无 |
| SubQueryParser | 前端 JSON 条件驱动的跨表查询 | 自动转 EXISTS | 无 |
| SmartSubQueryParser | 同上，全自动无需手动注册 | 自动转 EXISTS | 无 |
| collect 回填（见第六节） | 需要在结果中附带关联数据 | 2-3 次批量 IN 查询 | 无 |

---

## 六、关联数据收集回填（collect）

collect 是 mxpio-jpa 的特色功能：主查询不 JOIN，拿到结果后用 IN 批量查关联表，再通过反射将关联对象写回实体的对应属性。全程最多 2-3 次 SQL 查询，避免了 N+1。

### 6.1 两表关联

```java
// Dict.dictId → DictItem.id，回填到 Dict 的 dictItems 属性
// collect(主实体外键属性, 被收集实体类, 被收集实体的关联属性)
List<Dict> dicts = JpaUtil.linq(Dict.class)
    .collect("dictId", DictItem.class, "id")
    .list();
// 结果中 dict.getDictItems() 已自动填充
```

### 6.2 多对多中间表关联

```java
// User ← UserRole(中间表) → Role
// 简写：自动推断外键命名（userId、roleId）
List<User> users = JpaUtil.linq(User.class)
    .collect(UserRole.class, Role.class)
    .list();
// users.get(0).getRoles() 已自动填充

// 全参写法（自定义外键名）
List<User> users = JpaUtil.linq(User.class)
    .collect(
        UserRole.class,               // 中间表实体类
        "userId",                      // 主实体在中间表中的外键属性
        "roleId",                      // 被收集实体在中间表中的外键属性
        "id",                          // 被收集实体的主键
        Role.class,                    // 被收集实体类
        "id"                           // 主实体的关联属性（默认为主键）
    )
    .list();
```

**执行流程**：
1. 查询主表 → 提取主键集合
2. 查询中间表（只查两列外键）→ 提取被收集方 ID 集合
3. 批量查被收集实体 → 按主实体 ID 分组建立索引 Map
4. `BackfillFilter` 遍历结果，按属性类型和名称匹配，反射写回

### 6.3 链式多 collect

```java
// 同时收集用户的部门、角色和权限
List<User> users = JpaUtil.linq(User.class)
    .collect("deptId", Dept.class, "id")            // 收集部门
    .collect(UserRole.class, Role.class)              // 收集角色
    .collect(Permission.class, "userId")              // 收集权限
    .paging(pageable);
```

### 6.4 Lambda 风格 collect

```java
List<User> users = JpaUtil.linq(User.class)
    .collect(User::getDeptId, Dept.class)            // 按 Lambda 指定外键属性
    .collect(Dept.class, User::getDeptId)            // 按 Lambda 指定主表属性
    .list();
```

### 6.5 collect + 自定义过滤器

```java
// 只收集不自动回填，配合自定义 Filter 处理
JpaUtil.linq(Order.class)
    .collect("userId")                               // 只收集 userId 值集合
    .setDisableBackFillFilter()                      // 禁用自动回填
    .filter(context -> {
        Set<String> userIds = context.getSet("userId");
        // 自定义处理逻辑...
        return true;
    })
    .list();
```

### 6.6 collect 投影控制

```java
// 只查被收集实体的指定字段
JpaUtil.linq(User.class)
    .collect("deptId", Dept.class, "id")
    .collectSelect(Dept.class, "id", "name")         // 只取 id 和 name
    .list();
```

---

## 七、Criteria 查询条件对象（前后端 JSON 协议）

`Criteria` 是前后端统一的查询条件载体，支持 Jackson 双向序列化。

### 7.1 后端构造 Criteria

```java
// 编程式构造
Criteria criteria = Criteria.create()
    .addCriterion("aa", Operator.EQ, "xxx")
    .or()
        .addCriterion("username", Operator.EQ, "admin")
        .addCriterion("username", Operator.EQ, "admin1")
        .and()
            .addCriterion("age", Operator.GT, 18)
            .addCriterion("score", Operator.LT, 60)
        .end()
    .end()
    .addOrder(new Order("createTime", true))
    .addOrder(new Order("updateTime", true));

// 应用条件
JpaUtil.linq(User.class).where(criteria).list();
```

生成 SQL：

```sql
SELECT * FROM m_user
WHERE aa = 'xxx'
  AND (username = 'admin' OR username = 'admin1' OR (age >= 18 AND score <= 60))
ORDER BY createTime, updateTime DESC
```

### 7.2 反序列化 JSON 条件

```java
// 将前端 JSON 字符串还原为 Criteria
Criteria criteria = CriteriaUtils.json2Criteria(jsonString);
JpaUtil.linq(User.class).where(criteria).paging(pageable);
```

### 7.3 使用 Map 快速构造条件

```java
Map<String, Object> params = new HashMap<>();
params.put("username", "admin");
params.put("deptCode", "BM-001");

Criteria criteria = CriteriaUtils.add(params, Operator.LIKE);
// 等价于: username LIKE '%admin%' AND deptCode LIKE '%BM-001%'
```

### 7.4 支持的操作符

```java
public enum Operator {
    EQ,          // 等于 =
    NE,          // 不等于 !=
    LIKE,        // 模糊匹配 %value%
    LIKE_END,    // 后模糊 value%
    LIKE_START,  // 前模糊 %value
    NOT_LIKE,    // 非模糊
    GT,          // 大于 >
    LT,          // 小于 <
    GE,          // 大于等于 >=
    LE,          // 小于等于 <=
    IN,          // 在集合中
    NOT_IN,      // 不在集合中
    IS_NULL,     // 为空
    IS_NOT_NULL  // 非空
}
```

### 7.5 前端使用示例

```javascript
import Criteria from "@/utils/criteria";
import { OPERATOR } from "@/store/mutation-types";

// 简单条件：多个字段统一使用 LIKE
const searchData = {
    username: "admin",
    deptCode: "BM-001",
};
const query = new Criteria();
query.addCriterions(searchData, OPERATOR.LIKE);
query.addOrder({ fieldName: "createTime", desc: true });
// 序列化后发给后端

// 字段级指定操作符（使用 @ 后缀约定）
const mixedData = {
    "username@EQ": "admin",          // 精确匹配
    "deptCode": "BM-001",            // 默认 LIKE
};
query.addCriterions(mixedData, OPERATOR.LIKE);
```

---

## 八、Lambda 类型安全引用

通过 `SerializableFunction` 实现属性引用的类型安全，编译期检查，避免字符串拼写错误：

```java
// 字符串方式（运行时才能发现错误）
JpaUtil.linq(User.class).equal("usernmae", "admin");  // 拼写错误！

// Lambda 方式（编译期直接报错）
JpaUtil.linq(User.class).equal(User::getUsername, "admin");  // 安全

// 支持所有条件方法
JpaUtil.linq(User.class)
    .gt(User::getAge, 18)
    .like(User::getName, "%张%")
    .in(User::getDeptId, "d1", "d2")
    .between(User::getCreateTime, start, end)
    .asc(User::getCreateTime)
    .desc(User::getAge)
    .list();

// 投影
JpaUtil.linq(User.class)
    .select(User::getId, User::getName, User::getAge)
    .list();

// 分组
JpaUtil.linq(Order.class)
    .select(User::getDeptId, User::getDeptId.count())
    .groupBy(User::getDeptId)
    .list();

// 更新
JpaUtil.linu(User.class)
    .lt(User::getAge, 18)
    .set(User::getStatus, 0)
    .update();

// collect
JpaUtil.linq(User.class)
    .collect(User::getDeptId, Dept.class)
    .list();
```

**原理**：Lambda 表达式通过序列化机制反序列化出方法名 → `PropertyNamer.methodToProperty` 转属性名，兼容反射失败和 IDEA 调试代理等场景。

---

## 九、扩展机制

### 9.1 CRUD 策略（CrudPolicy）

自定义 CRUD 行为，推荐继承 `SmartCrudPolicyAdapter` 并覆盖钩子方法：

```java
@Component
public class MyCrudPolicy extends SmartCrudPolicyAdapter {

    @Override
    public boolean beforeInsert(CrudContext context) {
        Object entity = context.getEntity();
        if (entity instanceof BaseEntity) {
            BaseEntity be = (BaseEntity) entity;
            be.setCreateTime(new Date());
            be.setCreateBy(SecurityUtils.getCurrentUser());
        }
        return true;  // 返回 false 则跳过后续操作
    }

    @Override
    public void afterInsert(CrudContext context) {
        // 插入后处理：记录日志、发送消息等
    }

    @Override
    public boolean beforeUpdate(CrudContext context) {
        if (entity instanceof BaseEntity) {
            BaseEntity be = (BaseEntity) entity;
            be.setUpdateTime(new Date());
        }
        return true;
    }

    @Override
    public boolean beforeDelete(CrudContext context) {
        // 软删除：将 deleted 设为 true，返回 false 阻止物理删除
        BaseEntity be = (BaseEntity) context.getEntity();
        be.setDeleted(true);
        context.getEntityManager().merge(be);
        return false;
    }
}

// 使用
JpaUtil.save(entity, new MyCrudPolicy());
```

### 9.2 字段生成策略（@Generator）

在实体字段上添加 `@Generator` 注解，智能 CRUD 时自动生成字段值：

```java
public class Order {
    @Id
    @Generator(policy = UUIDPolicy.class)     // 新增时自动生成 UUID
    private String id;

    @Generator(policy = CreatedDatePolicy.class)  // 新增时自动填入创建时间
    private Date createTime;

    @Generator(policy = UpdatedDatePolicy.class)  // 更新时自动填入更新时间
    private Date updateTime;
}

// 保存时 id、createTime 自动填充
JpaUtil.save(order);
```

**内置策略**：
- `UUIDPolicy`：字段为空时生成 UUID
- `CreatedDatePolicy`：SAVE 类型操作时填入当前时间
- `UpdatedDatePolicy`：SAVE 或 UPDATE 时填入当前时间

**自定义策略**：

```java
public class SnowflakeIdPolicy extends AbstractGeneratorPolicy {
    @Override
    public void apply(Object entity, String name) {
        // 通过反射设置字段值
        BeanUtils.setProperty(entity, name, SnowflakeId.next());
    }
}

// 使用
@Generator(policy = SnowflakeIdPolicy.class)
private String id;
```

### 9.3 条件解析器（CriterionParser）

自定义条件解析，将 `SimpleCriterion` 翻译为 DSL 调用：

```java
// 自定义解析器：将 "keyword" 字段自动展开为多字段模糊搜索
public class KeywordParser implements CriterionParser {
    @Override
    public boolean parse(SimpleCriterion criterion, Linq linq, Class<?> entityClass) {
        if ("keyword".equals(criterion.getFieldName())) {
            String value = (String) criterion.getValue();
            linq.or()
                .like("name", "%" + value + "%")
                .like("code", "%" + value + "%")
            .end();
            return true;  // 返回 true 表示已处理此条件
        }
        return false;     // 返回 false 交给下一个解析器
    }
}

// 使用
JpaUtil.linq(User.class)
    .addParser(new KeywordParser())
    .where(criteria)
    .list();
```

### 9.4 结果过滤器（Filter）

对查询结果进行二次处理：

```java
JpaUtil.linq(Order.class)
    .collect("userId")
    .setDisableBackFillFilter()
    .filter(context -> {
        Order order = (Order) context.getEntity();
        Set<String> userIds = context.getSet("userId");
        // 自定义过滤和处理逻辑
        return true;  // true 保留，false 移除
    })
    .list();
```

`LinqContext` 提供的方法：
- `getEntity()` — 当前遍历到的实体
- `get(Class, key)` — 按类型和键取 collect 的单个对象
- `getList(Class, key)` — 按类型和键取 collect 的对象列表
- `getSet(property)` — 取 collect 的值集合

### 9.5 字典翻译（DictAble）

实体实现 `DictAble` 接口后，可自动完成字典文本翻译：

```java
public class User implements DictAble {
    private String status;     // 字典值 "1"

    @Override
    public void putText(Map<String, String> textMap) {
        textMap.put("statusText", "启用");  // status 对应的显示文本
    }

    @Override
    public Map<String, String> getTextMap() {
        return Map.of("status", "statusText");
    }
}
```

---

## 十、多数据源

### 10.1 自动路由

`GetEntityManagerFactoryStrategy` 按实体类在 JPA Metamodel 中查找归属的 EntityManagerFactory，对业务代码完全透明：

```java
// 自动根据实体类选择正确的数据源 EntityManager
JpaUtil.linq(User.class).list();     // 可能走主库
JpaUtil.linq(LogEntry.class).list(); // 可能走日志库
JpaUtil.save(user);                  // 自动判断归属
```

### 10.2 手动指定 EntityManager

```java
EntityManager em = ...;
JpaUtil.linq(User.class, em).list();
JpaUtil.lind(User.class, em).equal("status", 0).delete();
JpaUtil.linu(User.class, em).set("status", 1).update();
```

---

## 十一、原生 SQL 与命名查询

```java
// 原生 SQL
Query query = JpaUtil.nativeQuery("SELECT * FROM m_user WHERE age > ?1");
query.setParameter(1, 18);
List result = query.getResultList();

// 原生 SQL + 结果映射
Query query = JpaUtil.nativeQuery("SELECT id, name FROM m_user", User.class);
List<User> users = query.getResultList();

// 命名查询
Query query = JpaUtil.namedQuery("User.findByDept");
query.setParameter("deptId", "d1");
List<User> users = query.getResultList();
```

---

## 十二、集合工具方法

JpaUtil 提供了一些便利的集合操作方法：

```java
// 按属性提取集合
Set<String> ids = JpaUtil.collect(userList, "id");
Set<String> ids = JpaUtil.collect(userList, User::getId);

// 提取主键集合
Set<String> ids = JpaUtil.collectId(userList);

// 按属性分组
Map<String, List<User>> byDept = JpaUtil.classify(userList, "deptId");

// 按属性建索引（属性 → 对象）
Map<String, User> byId = JpaUtil.index(userList, "id");

// 按主键建索引
Map<String, User> byId = JpaUtil.index(userList);
```

---

## 十三、最佳实践

### 13.1 推荐用法

```java
// ✅ 推荐：Lambda 类型安全 + collect 关联回填 + 分页
Page<User> page = JpaUtil.linq(User.class)
    .collect(User::getDeptId, Dept.class)        // 收集部门信息
    .addIf(name)
        .like(User::getName, "%" + name + "%")
    .endIf()
    .addIf(status)
        .equal(User::getStatus, status)
    .endIf()
    .where(criteria)                              // 前端 JSON 条件
    .desc(User::getCreateTime)
    .paging(pageable);

// ✅ 推荐：智能保存 + 自定义策略
JpaUtil.save(entity, new MyCrudPolicy());
```

### 13.2 注意事项

1. **collect 的 IN 上限**：大批量主键集合使用 IN 子句时可能超过数据库参数上限（如 Oracle 的 1000 限制），需注意数据量，必要时分批次查询。
2. **SmartCrudPolicyAdapter 的 executeUpdate 限制**：`Linu` / `Lind` 的条件批量操作直接走 `executeUpdate()`，不触发 CRUD 策略和 `@Generator`——这些扩展仅对 `save/update/delete` 方法生效。
3. **`end()` 的可选性**：and/or 嵌套后若紧跟执行方法（`list()`、`update()`、`delete()`、`count()` 等），末尾的 `end()` 可省略。子查询中则必须调用 `end()` 返回父查询。
4. **多数据源环境**：实体类需唯一归属一个 EntityManagerFactory；若一个实体对应多个 EMF，`getEntityManager()` 会返回默认 EMF。
5. **事务管理**：`Linu.update()` 和 `Lind.delete()` 应放置在事务上下文中，否则可能抛出 `TransactionRequiredException`。

---

## 十四、常见问题

**Q: 与 Spring Data JPA 的关系？**
A: mxpio-jpa 构建在 JPA 之上，与 Spring Data JPA 共存。你可以同时使用 `JpaUtil.linq()` 和 `JpaRepository`，它们共享同一个 EntityManager。

**Q: collect 和 JPA @OneToMany 的区别？**
A: collect 是手动控制的批量关联加载，不依赖 JPA 关联映射，适合跨库关联或复杂关联场景。`@OneToMany` 是 JPA 标准关联映射，更简单但灵活度较低。

**Q: SmartSubQueryParser 什么时候不会自动挂载？**
A: 不使用 `where(Criteria)`（纯 DSL 条件）、调用了 `setDisableSmartSubQueryCriterion()`、或当前是在子查询内部时。

**Q: 如何实现软删除？**
A: 参考 9.1 节，继承 `SmartCrudPolicyAdapter`，覆盖 `beforeDelete` 方法将 `deleted` 设为 true 并返回 false。
