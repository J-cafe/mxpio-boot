# mxpio-jpa

## 简介

mxpio-jpa 是基于 JPA (Jakarta Persistence API) 与 Spring Data JPA 构建的数据访问增强框架。它在 JPA 之上提供了一套类型安全的链式查询 DSL、智能 CRUD、关联数据收集回填、以及前后端统一的 JSON 查询协议。

本模块运行在 JPA 之上，与 Spring Data JPA 共存——你可以随时回退到原生 JPA API 或 `JpaRepository`。

**核心价值**：类型安全的链式 DSL（Linq/Lind/Linu） + 对象树一键持久化 + JSON 条件协议 + 关联数据收集回填。

---

## 目录

- [一、JpaUtil 静态门面——API 速查表](#一jpautil-静态门面api-速查表)
- [二、查询 DSL——Linq](#二查询-dsllinq)
- [三、更新 DSL——Linu](#三更新-dsllinu)
- [四、删除 DSL——Lind](#四删除-dsllind)
- [五、CRUD 操作](#五crud-操作)
- [六、跨表查询](#六跨表查询)
- [七、关联数据收集回填——collect](#七关联数据收集回填collect)
- [八、Lambda 类型安全引用](#八lambda-类型安全引用)
- [九、Criteria 查询条件对象（前后端 JSON 协议）](#九criteria-查询条件对象前后端-json-协议)
- [十、扩展机制](#十扩展机制)
- [十一、多数据源与 EntityManager](#十一多数据源与-entitymanager)
- [十二、原生 SQL 与命名查询](#十二原生-sql-与命名查询)
- [十三、辅助类与接口](#十三辅助类与接口)
- [十四、前端使用指南](#十四前端使用指南)
- [十五、最佳实践与注意事项](#十五最佳实践与注意事项)

---

## 一、JpaUtil 静态门面——API 速查表

所有操作的唯一入口是 `JpaUtil` 静态类。

### 1.1 创建查询/更新/删除

| 方法 | 返回类型 | 说明 |
|---|---|---|
| `JpaUtil.linq(Class<T>)` | `Linq` | 创建查询 DSL |
| `JpaUtil.linq(Class<T>, EntityManager)` | `Linq` | 指定 EntityManager 创建查询 |
| `JpaUtil.linq(Class<T>, Class<?> resultClass)` | `Linq` | 指定结果类型（投影到其他 Bean） |
| `JpaUtil.linq(Class<T>, Class<?> resultClass, EntityManager)` | `Linq` | 指定结果类型 + EntityManager |
| `JpaUtil.lind(Class<?>)` | `Lind` | 创建删除 DSL |
| `JpaUtil.lind(Class<?>, EntityManager)` | `Lind` | 指定 EntityManager 创建删除 |
| `JpaUtil.linu(Class<?>)` | `Linu` | 创建更新 DSL |
| `JpaUtil.linu(Class<?>, EntityManager)` | `Linu` | 指定 EntityManager 创建更新 |

### 1.2 查找与统计

| 方法 | 返回类型 | 说明 |
|---|---|---|
| `JpaUtil.getOne(Class<T>, ID)` | `T` | 按主键查询单条（EntityManager.find） |
| `JpaUtil.findOne(Class<T>)` | `T` | 查询该实体类第一条记录 |
| `JpaUtil.findAll(Class<T>)` | `List<T>` | 查询该实体类所有记录 |
| `JpaUtil.findAll(CriteriaQuery<T>)` | `List<T>` | 按 CriteriaQuery 条件查询 |
| `JpaUtil.findAll(Class<T>, Pageable)` | `Page<T>` | 分页查询所有记录 |
| `JpaUtil.findAll(CriteriaQuery<T>, Pageable)` | `Page<T>` | 按 CriteriaQuery 条件分页 |
| `JpaUtil.count(Class<T>)` | `Long` | 统计记录总数 |
| `JpaUtil.count(CriteriaQuery<T>)` | `Long` | 按条件统计记录数 |
| `JpaUtil.exists(Class<T>)` | `boolean` | 判断记录是否存在 |
| `JpaUtil.exists(CriteriaQuery<T>)` | `boolean` | 按条件判断记录是否存在 |

### 1.3 CRUD 操作

| 方法 | 返回类型 | 说明 |
|---|---|---|
| `JpaUtil.persist(T)` | `T` | 持久化单条 |
| `JpaUtil.persist(Iterable<T>)` | `List<T>` | 批量持久化 |
| `JpaUtil.persistAndFlush(T)` | `T` | 持久化并刷入数据库 |
| `JpaUtil.merge(T)` | `T` | 更新单条 |
| `JpaUtil.merge(Iterable<T>)` | `List<T>` | 批量更新 |
| `JpaUtil.mergeAndFlush(T)` | `T` | 更新并刷入数据库 |
| `JpaUtil.remove(T)` | `void` | 删除单条 |
| `JpaUtil.remove(Iterable<T>)` | `void` | 批量删除 |
| `JpaUtil.removeAll(Class<T>)` | `void` | 删除该实体类所有记录（逐条） |
| `JpaUtil.removeAllInBatch(Class<T>)` | `void` | 批量删除所有记录（一条 SQL） |
| `JpaUtil.save(T)` | `void` | 智能保存（自动判断 insert/update，递归处理关联对象） |
| `JpaUtil.save(T, CrudPolicy)` | `void` | 智能保存 + 自定义策略 |
| `JpaUtil.save(Collection<T>)` | `void` | 批量智能保存 |
| `JpaUtil.save(Collection<T>, CrudPolicy)` | `void` | 批量智能保存 + 自定义策略 |
| `JpaUtil.update(T)` | `void` | 智能更新 |
| `JpaUtil.update(T, CrudPolicy)` | `void` | 智能更新 + 自定义策略 |
| `JpaUtil.update(Collection<T>)` | `void` | 批量智能更新 |
| `JpaUtil.update(Collection<T>, CrudPolicy)` | `void` | 批量智能更新 + 自定义策略 |
| `JpaUtil.delete(T)` | `void` | 智能删除 |
| `JpaUtil.delete(T, CrudPolicy)` | `void` | 智能删除 + 自定义策略 |
| `JpaUtil.delete(Collection<T>)` | `void` | 批量智能删除 |
| `JpaUtil.delete(Collection<T>, CrudPolicy)` | `void` | 批量智能删除 + 自定义策略 |
| `JpaUtil.flush(T)` | `void` | 刷新实体对应的 EntityManager |
| `JpaUtil.flush(Class<T>)` | `void` | 刷新实体类对应的 EntityManager |

### 1.4 EntityManager 获取

| 方法 | 返回类型 | 说明 |
|---|---|---|
| `JpaUtil.getEntityManager()` | `EntityManager` | 获取默认 EntityManager（事务感知） |
| `JpaUtil.getEntityManager(Class<T>)` | `EntityManager` | 按实体类获取 EntityManager（多数据源自动路由） |
| `JpaUtil.getEntityManager(T entity)` | `EntityManager` | 按实体对象获取 EntityManager |
| `JpaUtil.getEntityManager(String name)` | `EntityManager` | 按 Bean 名称获取 EntityManager |
| `JpaUtil.createEntityManager()` | `EntityManager` | 创建默认 EntityManager（非事务感知） |
| `JpaUtil.createEntityManager(Class<T>)` | `EntityManager` | 按实体类创建 EntityManager |
| `JpaUtil.createEntityManager(T entity)` | `EntityManager` | 按实体对象创建 EntityManager |
| `JpaUtil.createEntityManager(String name)` | `EntityManager` | 按 Bean 名称创建 EntityManager |
| `JpaUtil.getEntityManagerFactory()` | `EntityManagerFactory` | 获取默认 EMF |
| `JpaUtil.getEntityManagerFactory(Class<T>)` | `EntityManagerFactory` | 按实体类获取 EMF |
| `JpaUtil.getEntityManagerFactory(String name)` | `EntityManagerFactory` | 按 Bean 名称获取 EMF |

### 1.5 元数据与判断

| 方法 | 返回类型 | 说明 |
|---|---|---|
| `JpaUtil.getIdName(Class<T>)` | `String` | 获取实体类主键属性名（通过 JPA Metamodel） |
| `JpaUtil.getId(Class<T>)` | `SingularAttribute` | 获取实体类主键 SingularAttribute |
| `JpaUtil.isEntityClass(Class<T>)` | `boolean` | 判断类是否为实体类 |

### 1.6 集合工具方法

| 方法 | 返回类型 | 说明 |
|---|---|---|
| `JpaUtil.collect(Collection<?>, String)` | `Set<T>` | 提取集合中每个对象的指定属性值，组成去重 Set |
| `JpaUtil.collect(Collection<?>, SerializableFunction)` | `Set<T>` | Lambda 版本 |
| `JpaUtil.collectId(Collection<?>)` | `Set<T>` | 提取集合中每个对象的主键值 |
| `JpaUtil.classify(Collection<V>, String)` | `Map<K, List<V>>` | 按指定属性值分组 |
| `JpaUtil.classify(Collection<V>, SerializableFunction)` | `Map<K, List<V>>` | Lambda 版本 |
| `JpaUtil.index(Collection<V>)` | `Map<K, V>` | 按主键建索引 Map |
| `JpaUtil.index(Collection<V>, String)` | `Map<K, V>` | 按指定属性值建索引 Map |
| `JpaUtil.index(Collection<V>, SerializableFunction)` | `Map<K, V>` | Lambda 版本 |

### 1.7 配置

| 方法 | 说明 |
|---|---|
| `JpaUtil.getDefaultQBCCriteriaPolicy()` | 获取默认条件翻译策略 |
| `JpaUtil.setDefaultQBCCriteriaPolicy(CriteriaPolicy)` | 设置默认条件翻译策略 |
| `JpaUtil.getApplicationContext()` | 获取 Spring ApplicationContext |

---

## 二、查询 DSL——Linq

入口：`JpaUtil.linq(实体类.class)`，返回 `Linq` 接口。

### 2.1 执行方法

```java
// 查询列表（永不为 null）
List<User> list = JpaUtil.linq(User.class).gt("age", 18).list();

// 查询单条（必须有且只有一条，否则抛异常）
User user = JpaUtil.linq(User.class).equal("id", "001").findOne();

// 分页查询（返回 Page 对象，含总记录数和当前页数据）
Page<User> page = JpaUtil.linq(User.class).paging(PageRequest.of(0, 20));

// 分页不查总数（直接返回 List，更轻量）
List<User> list = JpaUtil.linq(User.class).list(PageRequest.of(0, 20));
List<User> list = JpaUtil.linq(User.class).list(0, 20);  // page, size

// 统计条数
Long count = JpaUtil.linq(User.class).gt("age", 18).count();

// 判断是否存在
boolean exists = JpaUtil.linq(User.class).equal("name", "张三").exists();
```

### 2.2 等值条件

```java
// equal — 等于 =
linq.equal("name", "张三");
linq.equal(User::getName, "张三");

// idEqual — 主键等于
linq.idEqual("001");

// notEqual — 不等于 !=
linq.notEqual("status", 0);
linq.notEqual(User::getStatus, 0);
```

### 2.3 数值比较条件

两类风格，功能等价：`gt/ge/lt/le` 接受 `Number` 类型，`greaterThan/greaterThanOrEqualTo/lessThan/lessThanOrEqualTo` 接受 `Comparable` 类型。

```java
// gt / greaterThan — 大于 >
linq.gt("age", 18);
linq.gt(User::getAge, 18);
linq.greaterThan("createTime", someDate);
linq.greaterThan(User::getCreateTime, someDate);

// ge / greaterThanOrEqualTo — 大于等于 >=
linq.ge("age", 18);
linq.ge(User::getAge, 18);
linq.greaterThanOrEqualTo("age", 18);

// lt / lessThan — 小于 <
linq.lt("age", 60);
linq.lt(User::getAge, 60);
linq.lessThan("age", 60);

// le / lessThanOrEqualTo — 小于等于 <=
linq.le("age", 60);
linq.le(User::getAge, 60);
linq.lessThanOrEqualTo("age", 60);

// between — BETWEEN x AND y
linq.between("age", 18, 60);
linq.between(User::getAge, 18, 60);
```

### 2.4 字符串条件

```java
// like — LIKE
linq.like("name", "%张%");
linq.like(User::getName, "%张%");

// like 带转义字符
linq.like("name", "%100%", '\\');  // 转义 %

// notLike — NOT LIKE
linq.notLike("name", "%test%");
linq.notLike(User::getName, "%test%");
```

### 2.5 集合条件

```java
// in — IN (...)
linq.in("deptId", "d1", "d2", "d3");
linq.in("deptId", Arrays.asList("d1", "d2"));
linq.in(User::getDeptId, "d1", "d2");
linq.in(User::getDeptId, deptIdList);

// in — IN 子查询（见跨表查询章节）
linq.in("userId", User.class);
linq.in(User.class);

// notIn — NOT IN (...)
linq.notIn("status", 0, 1);
linq.notIn(User::getStatus, Arrays.asList(0, 1));
```

### 2.6 Null / Boolean / 集合判空条件

```java
// isNull — IS NULL
linq.isNull("remark");
linq.isNull(User::getRemark);

// isNotNull — IS NOT NULL
linq.isNotNull("email");
linq.isNotNull(User::getEmail);

// isTrue — = true
linq.isTrue("married");
linq.isTrue(User::getMarried);

// isFalse — = false
linq.isFalse("deleted");
linq.isFalse(User::getDeleted);

// isEmpty — 集合为空
linq.isEmpty("children");
linq.isEmpty(User::getChildren);

// isNotEmpty — 集合非空
linq.isNotEmpty("children");
linq.isNotEmpty(User::getChildren);

// isMember — 元素是否在集合中
linq.isMember("userId", "memberIds");

// isNotMember — 元素不在集合中
linq.isNotMember("userId", "memberIds");
```

### 2.7 NOT 取反

```java
linq.not(cb.isTrue(root.get("deleted")));
```

### 2.8 属性对属性比较（同表或关联子查询）

```java
// equalProperty — 属性 = 属性
linq.equalProperty("columnA", "columnB");
linq.equalProperty(User::getColumnA, User::getColumnB);

// greaterThanProperty
linq.greaterThanProperty("score", "passScore");

// greaterThanOrEqualToProperty
linq.greaterThanOrEqualToProperty("score", "passScore");

// lessThanProperty
linq.lessThanProperty("score", "passScore");

// lessThanOrEqualToProperty
linq.lessThanOrEqualToProperty("score", "passScore");

// gt / ge / lt / le 属性版本
linq.gt("score", "passScore");
linq.ge("score", "passScore");
linq.lt("score", "passScore");
linq.le("score", "passScore");

// notEqualProperty
linq.notEqualProperty("columnA", "columnB");
```

> 在子查询内部使用时，右操作数自动引用父查询的 Root，形成**关联子查询**（correlated subquery）。

### 2.9 动态条件——addIf / addIfNot / endIf

```java
String name = request.getName();          // 可能为空字符串
Integer age = request.getAge();           // 可能为 null
List<String> deptIds = request.getDeptIds(); // 可能为空集合

JpaUtil.linq(User.class)
    .addIf(name)                          // name 非空时生效
        .like("name", "%" + name + "%")
    .endIf()
    .addIf(age)                           // age 非 null 时生效
        .ge("age", age)
    .endIf()
    .addIf(deptIds)                       // 集合非空时生效
        .in("deptId", deptIds)
    .endIf()
    .addIfNot(deptIds)                    // 集合为空时生效（取反逻辑）
        .equal("deptId", "default")
    .endIf()
    .list();
```

**条件生效判断规则**：

| 参数类型 | 生效条件（addIf） | 生效条件（addIfNot） |
|---|---|---|
| `String` | 非 null 且非 `""` | null 或 `""` |
| `Collection` | 非 null 且非空 | null 或空 |
| `Boolean` | true | false |
| 其他对象 | 非 null | null |

### 2.10 嵌套条件——and / or / end

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

> 复杂条件后紧跟执行方法（`list()`、`update()`、`delete()`、`count()` 等）时，末尾的 `end()` 可省略。

### 2.11 投影查询——select / selectId

```java
// 投影指定字段（返回实体对象，未选字段为 null）
JpaUtil.linq(User.class).select("id", "name", "age").list();

// 只选主键
JpaUtil.linq(User.class).selectId().list();

// Lambda 风格
JpaUtil.linq(User.class).select(User::getId, User::getName, User::getAge).list();

// 带别名投影
JpaUtil.linq(User.class).select("id as userId", "name as userName").list();

// 混合 String 和 JPA Selection
linq.select("id", cb.count(root.get("id")));
```

### 2.12 结果类型转换——aliasToBean / aliasToMap / aliasToTuple

```java
// 投影到 Map<String, Object>
List<Map<String, Object>> maps = JpaUtil.linq(User.class)
    .aliasToMap()
    .select("id", "name", "age")
    .list();

// 投影到自定义 Bean
List<UserDTO> dtos = JpaUtil.linq(User.class)
    .aliasToBean(UserDTO.class)
    .select("id", "name", "age")
    .list();

// 投影到当前实体类
List<User> users = JpaUtil.linq(User.class)
    .aliasToBean()                        // 等同于 aliasToBean(User.class)
    .select("id", "name", "age")
    .list();

// 投影到 Tuple（JPA 标准元组）
List<Tuple> tuples = JpaUtil.linq(User.class)
    .aliasToTuple()
    .select("id", "name")
    .list();
```

> 注意：`aliasToBean/aliasToMap/aliasToTuple` 必须在 `select` 之前调用。

### 2.13 排序

```java
// asc — 升序
linq.asc("age", "name");
linq.asc(User::getAge, User::getName);

// desc — 降序
linq.desc("createTime", "name");
linq.desc(User::getCreateTime, User::getName);

// 同时使用
linq.asc("age").desc("createTime");
```

### 2.14 去重

```java
JpaUtil.linq(User.class)
    .select("deptId")
    .distinct()
    .list();
```

### 2.15 分组与 Having

```java
// 分组查询
JpaUtil.linq(Order.class)
    .select("deptId", "deptId.count()")
    .groupBy("deptId")
    .list();

// Lambda 风格
JpaUtil.linq(Order.class)
    .select(Order::getDeptId, Order::getDeptId.count())
    .groupBy(Order::getDeptId)
    .list();

// 分组 + having
// 注意：groupBy + having 目前主要用于子查询场景，having() 开启 having 条件块
```

### 2.16 where——应用 Criteria 条件对象

```java
Criteria criteria = Criteria.create()
    .addCriterion("name", Operator.LIKE, "admin")
    .addOrder(new Order("createTime", true));

JpaUtil.linq(User.class).where(criteria).paging(pageable);
```

### 2.17 解析器与过滤器配置

```java
// 添加条件解析器（自定义 CriterionParser）
linq.addParser(new MyCriterionParser());

// 添加子查询解析器（手动注册跨表条件转换）
linq.addSubQueryParser(Role.class, "roleId");
linq.addSubQueryParser(Role.class);  // 自动推断外键名

// 禁用智能子查询（避免条件属性名误匹配）
linq.setDisableSmartSubQueryCriterion();

// 禁用 collect 自动回填
linq.setDisableBackFillFilter();

// 添加结果过滤器
linq.filter(new MyFilter());
```

---

## 三、更新 DSL——Linu

入口：`JpaUtil.linu(实体类.class)`，返回 `Linu` 接口。

### 3.1 执行方法

```java
// 条件批量更新，返回受影响行数
int count = JpaUtil.linu(User.class)
    .lt("age", 18)
    .set("status", 0)
    .update();
```

### 3.2 set 方法（6 个重载）

```java
// 按属性名字符串
linu.set("status", 0);
linu.set("salary", 5000);

// 按 Lambda 属性
linu.set(User::getStatus, 0);
linu.set(User::getSalary, 5000);

// 按 JPA Path + 值
linu.set(root.get("status"), 0);

// 按 JPA Path + Expression
linu.set(root.get("status"), root.get("defaultStatus"));

// 按 SingularAttribute + 值
linu.set(User_.status, 0);

// 按 SingularAttribute + Expression
linu.set(User_.status, root.get("defaultStatus"));
```

### 3.3 完整示例

```java
// 多字段更新 + 条件
int count = JpaUtil.linu(User.class)
    .equal("deptId", "d1")
    .lt("age", 18)
    .set("deptId", "d2")
    .set("status", 0)
    .update();

// Lambda 风格
int count = JpaUtil.linu(User.class)
    .equal(User::getDeptId, "d1")
    .lt(User::getAge, 18)
    .set(User::getDeptId, "d2")
    .set(User::getStatus, 0)
    .update();

// 动态条件更新
int count = JpaUtil.linu(User.class)
    .addIf(newDeptId)
        .set("deptId", newDeptId)
    .endIf()
    .addIf(newStatus)
        .set("status", newStatus)
    .endIf()
    .equal("deptId", oldDeptId)
    .update();
```

---

## 四、删除 DSL——Lind

入口：`JpaUtil.lind(实体类.class)`，返回 `Lind` 接口。

### 4.1 执行方法

```java
// 条件批量删除，返回受影响行数
int count = JpaUtil.lind(User.class).lt("age", 18).delete();
```

### 4.2 完整示例

```java
// 简单条件删除
int count = JpaUtil.lind(User.class)
    .equal("status", 0)
    .delete();

// 复杂条件删除
int count = JpaUtil.lind(User.class)
    .equal("deptId", "d1")
    .or()
        .lt("age", 18)
        .gt("age", 60)
    .end()
    .delete();

// 动态条件删除
int count = JpaUtil.lind(User.class)
    .addIf(deptId)
        .equal("deptId", deptId)
    .endIf()
    .lt("age", 18)
    .delete();
```

---

## 五、CRUD 操作

### 5.1 标准 JPA 操作

```java
// 新增
User user = JpaUtil.persist(newUser);           // 返回托管实体
List<User> users = JpaUtil.persist(userList);

// 更新
User user = JpaUtil.merge(existingUser);        // 返回托管实体
List<User> users = JpaUtil.merge(userList);

// 删除
JpaUtil.remove(user);
JpaUtil.remove(userList);

// 刷新
User user = JpaUtil.persistAndFlush(newUser);
User user = JpaUtil.mergeAndFlush(existingUser);

// 批量删除所有（一条 SQL）
JpaUtil.removeAllInBatch(User.class);
// 逐条删除所有
JpaUtil.removeAll(User.class);
```

### 5.2 智能操作（推荐）

`safe()`、`update()`、`delete()` 内部使用 `DirtyTreeCrudPolicy` 递归处理对象树，实现级联 CRUD：

```java
// 智能保存：自动判断 insert/update，递归处理关联属性
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

**智能 CRUD 执行流程**：
1. 检查实体字段上的 `@Generator` 注解，执行字段生成策略
2. 递归扫描实体类中的关联属性（单对象 + Collection）
3. 逐层下钻，对每个子对象执行相应 CRUD 操作
4. 实现级联保存/更新/删除

### 5.3 MxpioEntity 接口

实体实现此接口可表达自身 CRUD 意图：

```java
public interface MxpioEntity {
    CrudType getCrudType();       // SAVE / UPDATE / DELETE / SAVE_OR_UPDATE
    boolean isSaveTransient();    // 是否级联持久化关联属性
}
```

```java
// 使用示例
public class Order implements MxpioEntity {
    @Transient
    private CrudType crudType = CrudType.SAVE;

    @Override
    public CrudType getCrudType() {
        return crudType;
    }

    @Override
    public boolean isSaveTransient() {
        return true;
    }
}
```

`DirtyTreeCrudPolicy` 根据 `getCrudType()` 决定每个实体级的操作，根据 `isSaveTransient()` 决定是否级联。

---

## 六、跨表查询

**核心思想**：主查询只查主表，跨表条件通过 EXISTS/IN 子查询（SQL 层）完成，关联数据通过 collect 批量回填（应用层）。避免 JOIN，规避 N+1。

### 6.1 EXISTS 子查询

```java
// 查询属于某菜单下且角色为 admin 的所有权限
List<Permission> permissions = JpaUtil.linq(Permission.class)
    .equal("roleId", roleId)
    .equal("resourceType", ResourceType.ELEMENT)
    .exists(Element.class)                       // EXISTS (SELECT ... FROM Element)
        .equalProperty("id", "resourceId")       // Element.id = Permission.resourceId（关联条件）
        .equal("parentId", menuId)               // Element.parentId = menuId（子查询内部条件）
    .end()                                        // 结束子查询
    .list();
```

生成 SQL：

```sql
SELECT * FROM permission p
WHERE p.role_id = ? AND p.resource_type = ?
AND EXISTS (
    SELECT e FROM element e WHERE e.id = p.resource_id AND e.parent_id = ?
)
```

**notExists**：

```java
linq.notExists(Order.class)                      // NOT EXISTS
    .equalProperty("userId", "id")
    .gt("amount", 10000)
.end();
```

### 6.2 IN 子查询

```java
// 主键 IN 子查询
List<User> users = JpaUtil.linq(User.class)
    .in(Role.class)                              // id IN (SELECT id FROM Role WHERE ...)
        .equal("roleName", "admin")
    .end()
    .list();

// 指定属性 IN 子查询
List<Order> orders = JpaUtil.linq(Order.class)
    .in("userId", User.class)                    // userId IN (SELECT id FROM User WHERE ...)
        .equal("deptId", "d1")
    .end()
    .list();
```

### 6.3 属性对属性比较

在 EXISTS/IN 子查询内部使用以下方法时，右侧操作数自动引用父查询的 Root，形成关联子查询：

```java
.exists(Element.class)
    .equalProperty("id", "resourceId")           // 子查询.id = 父查询.resourceId
    .gtProperty("order", "sortOrder")            // 子查询.order > 父查询.sortOrder
    .geProperty("order", "sortOrder")
    .ltProperty("order", "sortOrder")
    .leProperty("order", "sortOrder")
    .greaterThanProperty("order", "sortOrder")
    .greaterThanOrEqualToProperty("order", "sortOrder")
    .lessThanProperty("order", "sortOrder")
    .lessThanOrEqualToProperty("order", "sortOrder")
    .notEqualProperty("status", "otherStatus")
.end();
```

> 不在子查询内使用时，这些方法退化为同表两列比较。

### 6.4 SubQueryParser——Criteria 驱动的 EXISTS 转换

当使用 `where(Criteria)` 时，点号路径条件（如 `"roleId.name"`）可通过 `SubQueryParser` 自动转换为 EXISTS 子查询：

```java
// 手动注册
JpaUtil.linq(User.class)
    .addSubQueryParser(Role.class, "roleId")     // 注册：Role 实体，外键属性为 roleId
    .addSubQueryParser(Dept.class, "deptId")     // 可注册多个
    .where(criteria)                              // 前端条件 "roleId.name" LIKE ... 自动转换
    .list();
```

**匹配规则**：`SimpleCriterion.fieldName` 中点号前的别名与外键属性名互相 `startsWith` 匹配即触发。

**默认外键名规则**：实体类名首字母小写 + 主键名首字母大写（如 `Role` + `id` = `roleId`）。

以下两个简写重载使用默认规则：

```java
linq.addSubQueryParser(Role.class);              // 等价于 addSubQueryParser(Role.class, "roleId")
linq.addSubQueryParser(Role.class, Dept.class);   // 批量注册多个
```

### 6.5 SmartSubQueryParser——全自动

当同时满足以下条件时，**自动**为主实体上所有实体类型的属性生成 SubQueryParser：

1. 执行了 `where(Criteria)` 设置 JSON 条件
2. 未调用 `setDisableSmartSubQueryCriterion()`
3. 正在执行查询（list/findOne/paging/count 等）

无需手动注册任何 SubQueryParser：

```java
// Dict 有 List<DictItem> 属性，collect 声明了关联
// 前端条件 "dictItems.itemText" LIKE "测试" 自动转换为 EXISTS 子查询
JpaUtil.linq(Dict.class)
    .collect("dictId", DictItem.class, "id")
    .where(criteria)
    .paging(pageable);
```

**禁用**（当条件属性名可能被误匹配时）：

```java
linq.setDisableSmartSubQueryCriterion();
```

### 6.6 跨表方式对比

| 方式 | 适用场景 | SQL | N+1 |
|---|---|---|---|
| EXISTS 子查询 | 手工编写存在性判断 | EXISTS | 无 |
| IN 子查询 | 主表字段值在另一表结果集中 | IN | 无 |
| *Property 系列 | 子查询内向父查询关联 | 关联子查询 | 无 |
| SubQueryParser | 前端 JSON 条件 + 手动注册 | 自动转 EXISTS | 无 |
| SmartSubQueryParser | 前端 JSON 条件 + 全自动 | 自动转 EXISTS | 无 |
| collect 回填（第七节） | 需要关联数据 | 2-3 次批量 IN | 无 |

---

## 七、关联数据收集回填——collect

### 7.1 collect 方法全览

`collect` 有 12+ 个重载（字符串属性名 + Lambda 属性引用），覆盖两表关联和多对多中间表关联。

#### 两表直接关联

```java
// 只收集主键值集合（不查关联表，仅收集值供 Filter 使用）
linq.collect("deptId");
linq.collect(User::getDeptId);

// 按主键收集关联实体
linq.collect(Dept.class);                         // 默认：主表主键 = Dept 主键
linq.collect(User::getDeptId, Dept.class);         // Lambda 指定关联属性

// 按指定属性收集关联实体
// collect(被收集表的关联属性, 被收集实体类, 主表的关联属性...)
linq.collect("id", Dept.class, "deptId");         // Dept.id = 主表.deptId
linq.collect(User::getDeptId, Dept.class, User::getId);  // Lambda 版本

// collectSelect — 控制被收集实体的投影字段
linq.collect("deptId", Dept.class, "id")
    .collectSelect(Dept.class, "id", "name");     // 只取 id 和 name
linq.collectSelect(Dept.class, Dept::getId, Dept::getName);  // Lambda 版本
```

#### 多对多中间表关联

```java
// 全参（字符串）
linq.collect(
    UserRole.class,        // relationClass: 中间表实体类
    "userId",              // relationProperty: 主表在中间表中的外键
    "roleId",              // relationOtherProperty: 被收集表在中间表中的外键
    "id",                  // otherProperty: 被收集实体关联属性（通常为主键）
    Role.class,            // entityClass: 被收集实体类
    "id"                   // properties: 主表用于关联的属性（通常为主键）
);

// Lambda 版本
linq.collect(
    UserRole.class,
    UserRole::getUserId,
    UserRole::getRoleId,
    Role::getId,
    Role.class,
    User::getId
);

// 简写（自动推断外键命名：userId、roleId）
linq.collect(UserRole.class, Role.class);

// 带投影的中间表关联
linq.collect(UserRole.class, UserRole::getUserId, UserRole::getRoleId, Role.class)
    .collectSelect(Role.class, Role::getId, Role::getName);
```

### 7.2 collect 参数说明（全参版本）

```
collect(relationClass, relationProperty, relationOtherProperty, otherProperty, entityClass, properties...)
```

| 参数 | 含义 | 示例 |
|---|---|---|
| `relationClass` | 中间表实体类，无中间表时传 null | `UserRole.class` |
| `relationProperty` | 主表在中间表中对应的外键属性名 | `"userId"` |
| `relationOtherProperty` | 被收集表在中间表中对应的外键属性名 | `"roleId"` |
| `otherProperty` | 被收集实体的关联属性（通常为其主键） | `"id"` |
| `entityClass` | 被收集实体类 | `Role.class` |
| `properties` | 主表用于关联的属性名（通常为主键） | `"id"` |

### 7.3 执行流程

1. 查询主表 → 从结果中提取外键值集合
2. （如有中间表）查中间表两列投影 → 提取被收集方 ID 集合
3. 批量 IN 查询被收集实体 → 按主表标识分组建立索引 `Map<主表ID, List<关联对象>>`
4. `BackfillFilter` 遍历结果，按属性类型和名称匹配，反射写回实体属性

全程最多 2-3 次 SQL，无 N+1。

### 7.4 完整示例

```java
// 示例 1：两表关联（一对多）
// 查询 Dict，同时填充每个 Dict 的 dictItems 属性
List<Dict> dicts = JpaUtil.linq(Dict.class)
    .collect("dictId", DictItem.class, "id")
    .list();
// dicts.get(0).getDictItems() 已自动填充

// 示例 2：两表关联（多对一）
// 查询 User，同时填充每个 User 的 dept 属性
List<User> users = JpaUtil.linq(User.class)
    .collect("deptId", Dept.class, "id")
    .list();
// users.get(0).getDept() 已自动填充

// 示例 3：多对多关联
// 查询 User，同时填充每个 User 的 roles 集合
List<User> users = JpaUtil.linq(User.class)
    .collect(UserRole.class, Role.class)
    .list();
// users.get(0).getRoles() 已自动填充

// 示例 4：链式多个 collect
// 同时收集部门、角色、权限
List<User> users = JpaUtil.linq(User.class)
    .collect("deptId", Dept.class, "id")
    .collect(UserRole.class, Role.class)
    .collect(Permission.class, "userId")
    .where(criteria)
    .paging(pageable);

// 示例 5：只收集值集合 + 自定义 Filter
JpaUtil.linq(Order.class)
    .collect("userId")                           // 只收集 userId 值集合
    .setDisableBackFillFilter()                  // 禁用自动回填
    .filter(ctx -> {
        Order order = ctx.getEntity();
        Set<String> userIds = ctx.getSet("userId");
        // 自定义处理
        return true;
    })
    .list();
```

### 7.5 BackfillFilter 回填规则

当 entityClass 不为 null 时，`BackfillFilter` 按以下优先顺序回填：

1. 反射实体所有 `PropertyDescriptor`，找到**类型可赋值**于被收集实体类的属性
2. 仅一个匹配：直接填充
3. 多个匹配：按属性名与 collect property 的包含关系逐一匹配填充
4. 零个匹配：按约定名 `Introspector.decapitalize(实体类简单名)` 填充单个对象
5. Collection 类型属性填充 `List<关联实体>`，单对象类型属性填充单个关联实体

---

## 八、Lambda 类型安全引用

通过 `SerializableFunction`（`SFunction`）实现属性引用的编译期类型检查。

### 8.1 支持 Lambda 的方法列表

Lin（Lin/Linq/Lind/Linu 基接口）中所有条件方法均支持 Lambda 重载：

| 类别 | 方法 |
|---|---|
| 等值 | `equal`, `notEqual`, `equalProperty`, `notEqualProperty` |
| 数值 | `gt`, `ge`, `lt`, `le`, `greaterThan`, `greaterThanOrEqualTo`, `lessThan`, `lessThanOrEqualTo`, `between` |
| 字符串 | `like`, `notLike` |
| 集合 | `in`, `notIn` |
| 判空 | `isNull`, `isNotNull`, `isEmpty`, `isNotEmpty`, `isTrue`, `isFalse` |
| 投影 | `select` |
| 分组 | `groupBy` |
| 排序 | `asc`, `desc`（Linq） |
| 更新 | `set`（Linu） |
| 收集 | `collect`, `collectSelect`（Linq） |
| 比较 | `greaterThanProperty`, `greaterThanOrEqualToProperty`, `lessThanProperty`, `lessThanOrEqualToProperty` |

### 8.2 使用对比

```java
// ❌ 字符串：拼写错误运行时才发现
JpaUtil.linq(User.class).equal("usernmae", "admin");

// ✅ Lambda：编译期直接报错
JpaUtil.linq(User.class).equal(User::getUsername, "admin");

// 混合使用完全没问题
JpaUtil.linq(User.class)
    .equal(User::getStatus, 1)                  // Lambda 条件
    .like("name", "%张%")                        // 字符串条件
    .desc(User::getCreateTime)                   // Lambda 排序
    .select(User::getId, User::getName)           // Lambda 投影
    .list();
```

---

## 九、Criteria 查询条件对象（前后端 JSON 协议）

`Criteria` 支持 Jackson 双向序列化，是前后端统一的查询协议。

### 9.1 后端编程式构造

```java
Criteria c = Criteria.create()
    .addCriterion("resourceType", Operator.EQ, "ELEMENT")
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

// 也可使用 Lambda 构造 SimpleCriterion 和 Order
c.addCriterion(User::getName, Operator.LIKE, "admin");
c.addOrder(new Order(User::getCreateTime, true));
```

生成 SQL：

```sql
WHERE resourceType = 'ELEMENT'
  AND (username = 'admin' OR username = 'admin1' OR (age >= 18 AND score <= 60))
ORDER BY createTime, updateTime DESC
```

### 9.2 等价 JSON 格式

```json
{
    "criterions": [
        {"fieldName": "resourceType", "value": "ELEMENT", "operator": "EQ"},
        {"type": "OR", "criterions": [
            {"fieldName": "username", "value": "admin", "operator": "EQ"},
            {"fieldName": "username", "value": "admin1", "operator": "EQ"},
            {"type": "AND", "criterions": [
                {"fieldName": "age", "value": 18, "operator": "GT"},
                {"fieldName": "score", "value": 60, "operator": "LT"}
            ]}
        ]}
    ],
    "orders": [
        {"fieldName": "createTime", "desc": true},
        {"fieldName": "updateTime", "desc": true}
    ]
}
```

### 9.3 后端反序列化

```java
// JSON 字符串 → Criteria
Criteria c = CriteriaUtils.json2Criteria(jsonString);

// 直接应用
JpaUtil.linq(User.class).where(c).paging(pageable);
```

### 9.4 Criteria API 全览

```java
// 工厂
Criteria c = Criteria.create();
Criteria c = Criteria.create(existingCriteria);

// 条件
c.addCriterion(SimpleCriterion);                // 添加 SimpleCriterion 对象
c.addCriterion("name", Operator.EQ, "value");   // 属性名 + 操作符 + 值
c.addCriterion(User::getName, Operator.EQ, "v"); // Lambda 属性
c.addJunction(Junction);                        // 添加 Junction 对象
c.addCriterion("name", Operator.EQ, "value");   // 简单条件

// 嵌套
c.and();    // 开始 AND 块
c.or();     // 开始 OR 块
c.end();    // 结束当前块

// 排序
c.addOrder(new Order("createTime", true));       // true = desc
c.addOrder(new Order(User::getCreateTime, true));

// 内部状态
c.getCriterions();    // List<Object>——SimpleCriterion 或 Junction
c.setCriterions(list);
c.getOrders();        // List<Order>
c.setOrders(list);
```

### 9.5 SimpleCriterion

```java
// 构造
new SimpleCriterion("name", Operator.EQ, "value");
new SimpleCriterion(User::getName, Operator.EQ, "value");  // Lambda

// getter/setter
criterion.getFieldName();    // 属性名（支持点号路径如 "roleId.name"）
criterion.getValue();        // 条件值
criterion.getOperator();     // 操作符
```

### 9.6 Junction（联合条件）

```java
Junction j = new Junction(JunctionType.OR);      // OR 或 AND
j.add(new SimpleCriterion(...));
j.addCriterion("name", Operator.EQ, "value");
j.addJunction(anotherJunction);
j.getType();        // JunctionType.OR / JunctionType.AND
j.getCriterions();  // List<Object>
```

### 9.7 Order

```java
new Order("createTime", true);                   // true = desc, false = asc
new Order(User::getCreateTime, true);            // Lambda

order.getFieldName();
order.isDesc();
order.setFieldName("name");
order.setFieldName(User::getName);               // Lambda
order.setDesc(true);
```

### 9.8 Operator 枚举

| 枚举值 | SQL 含义 | 说明 |
|---|---|---|
| `EQ` | `=` | 等于 |
| `NE` | `!=` / `<>` | 不等于 |
| `LIKE` | `LIKE '%value%'` | 全模糊匹配 |
| `LIKE_END` | `LIKE '%value'` | 后模糊 |
| `LIKE_START` | `LIKE 'value%'` | 前模糊 |
| `NOT_LIKE` | `NOT LIKE '%value%'` | 非模糊 |
| `GT` | `>` | 大于（Number） |
| `LT` | `<` | 小于（Number） |
| `GE` | `>=` | 大于等于（Number） |
| `LE` | `<=` | 小于等于（Number） |
| `IN` | `IN (...)` | 在集合中 |
| `NOT_IN` | `NOT IN (...)` | 不在集合中 |
| `IS_NULL` | `IS NULL` | 为空 |
| `IS_NOT_NULL` | `IS NOT NULL` | 非空 |

### 9.9 CriteriaUtils 工具方法

```java
// JSON → Criteria
Criteria c = CriteriaUtils.json2Criteria(jsonString);

// Map 快速构建 AND 条件
Map<String, Object> params = new HashMap<>();
params.put("username", "admin");
params.put("deptCode", "BM-001");
Criteria c = CriteriaUtils.add(null, params, Operator.LIKE);

// 已有 Criteria 追加
CriteriaUtils.add(criteria, params);

// 单条件追加
CriteriaUtils.add(criteria, "name", Operator.EQ, "value");

// Junction 级别追加
CriteriaUtils.add(junction, params);
CriteriaUtils.add(junction, "name", Operator.EQ, "value");

// 条件翻译（将 SimpleCriterion 翻译为 Linq 链式调用）
CriteriaUtils.parse(linq, simpleCriterion);
```

---

## 十、扩展机制

### 10.1 CRUD 策略——CrudPolicy

```java
public interface CrudPolicy {
    void apply(CrudContext context);
}
```

`CrudContext` 提供：
- `getEntity()` — 当前实体
- `getEntityManager()` — 当前 EntityManager
- `getCrudType()` — SAVE / UPDATE / DELETE
- `getParent()` — 父实体
- `isSaveTransient()` — 是否级联

#### SmartCrudPolicyAdapter（推荐继承）

提供 6 个钩子方法：

```java
public class MyCrudPolicy extends SmartCrudPolicyAdapter {

    @Override
    public boolean beforeInsert(CrudContext context) {
        // 返回 false 则跳过后续 persist 操作
        Object entity = context.getEntity();
        if (entity instanceof BaseEntity) {
            BaseEntity be = (BaseEntity) entity;
            be.setCreateTime(new Date());
            be.setCreateBy(currentUser());
        }
        return true;
    }

    @Override
    public void afterInsert(CrudContext context) {
        // 插入后处理：发消息、记录日志等
    }

    @Override
    public boolean beforeUpdate(CrudContext context) {
        if (entity instanceof BaseEntity) {
            ((BaseEntity) entity).setUpdateTime(new Date());
        }
        return true;
    }

    @Override
    public void afterUpdate(CrudContext context) { }

    @Override
    public boolean beforeDelete(CrudContext context) {
        // 软删除示例
        BaseEntity be = (BaseEntity) context.getEntity();
        be.setDeleted(true);
        context.getEntityManager().merge(be);
        return false;  // 阻止物理删除
    }

    @Override
    public void afterDelete(CrudContext context) { }
}

// 使用
JpaUtil.save(entity, new MyCrudPolicy());
```

### 10.2 字段生成策略——@Generator + GeneratorPolicy

```java
public interface GeneratorPolicy {
    void apply(Object entity, String fieldName);
    CrudType getType();  // 匹配的 CRUD 类型（SAVE / UPDATE）
}
```

#### 内置策略

| 策略类 | 触发类型 | 行为 |
|---|---|---|
| `UUIDPolicy` | SAVE | 字段为空时生成 `UUID.randomUUID().toString()` |
| `CreatedDatePolicy` | SAVE | 字段为空时填入 `new Date()` |
| `UpdatedDatePolicy` | UPDATE | 始终填入 `new Date()` |

#### 使用方式

```java
public class Order {
    @Id
    @Generator(policy = UUIDPolicy.class)
    private String id;

    @Generator(policy = CreatedDatePolicy.class)
    private Date createTime;

    @Generator(policy = UpdatedDatePolicy.class)
    private Date updateTime;
}
```

#### 自定义策略

```java
// 继承 AbstractGeneratorPolicy，只需实现 getValue 和 getType
public class SnowflakeIdPolicy extends AbstractGeneratorPolicy {
    @Override
    protected Object getValue(Object entity, String fieldName) {
        Object value = BeanReflectionUtils.getPropertyValue(entity, fieldName);
        if (value == null || "".equals(value)) {
            return SnowflakeId.next();
        }
        return value;
    }

    @Override
    public CrudType getType() {
        return CrudType.SAVE;
    }
}

// 使用
@Generator(policy = SnowflakeIdPolicy.class)
private String id;
```

### 10.3 条件解析器——CriterionParser

将 `where(Criteria)` 中的 `SimpleCriterion` 自定义转换为 DSL 调用：

```java
public interface CriterionParser {
    boolean parse(SimpleCriterion criterion);
    // 返回 true = 已处理此条件，后续 parser 不再处理
    // 返回 false = 未处理，交给下一个 parser
}
```

```java
// 示例：将 keyword 字段展开为多字段 OR 模糊搜索
public class KeywordParser implements CriterionParser {

    private Linq linq;
    private Class<?> entityClass;

    public KeywordParser(Linq linq, Class<?> entityClass) {
        this.linq = linq;
        this.entityClass = entityClass;
    }

    @Override
    public boolean parse(SimpleCriterion criterion) {
        if ("keyword".equals(criterion.getFieldName())) {
            String keyword = (String) criterion.getValue();
            linq.or()
                .like("name", "%" + keyword + "%")
                .like("code", "%" + keyword + "%")
            .end();
            return true;
        }
        return false;
    }
}

// 注册
JpaUtil.linq(User.class)
    .addParser(new KeywordParser(...))
    .where(criteria)
    .list();
```

### 10.4 结果过滤器——Filter

```java
public interface Filter {
    boolean invoke(LinqContext linqContext);
    // 返回 true = 保留该条记录
    // 返回 false = 从结果中移除该条记录
}
```

```java
JpaUtil.linq(Order.class)
    .collect("userId")
    .setDisableBackFillFilter()
    .filter(ctx -> {
        Order order = ctx.getEntity();
        Set<String> userIds = ctx.getSet("userId");
        // 自定义逻辑
        return order.getAmount() != null && order.getAmount() > 0;
    })
    .list();
```

#### LinqContext API

```java
public class LinqContext {
    <T> T getEntity();                          // 当前正在处理的实体
    void setEntity(Object entity);

    void put(Object key, Object value);         // 存入元数据
    <T> T get(Object key);                      // 按 Key 取元数据

    // 按类型 + 键取 collect 结果
    <T> T get(Class<T> entityClass, Object id);       // 取单个关联对象
    <T> List<T> getList(Class<T> entityClass, Object value);  // 取关联对象列表

    // 按属性名 + 键取 collect 结果
    <T> T get(String property, Object id);
    <T> List<T> getList(String property, Object value);

    <T> List<T> get(String property);           // 按属性名取列表
    <T> Set<T> getSet(String property);         // 按属性名取 Set（collect 值集合）

    Map<Object, Object> getMetadata();          // 获取全部元数据
}
```

### 10.5 条件翻译策略——CriteriaPolicy

```java
public interface CriteriaPolicy {
    void apply(CriteriaContext context);
}
```

一般不直接使用，由模块内部调用。可以通过 `JpaUtil.setDefaultQBCCriteriaPolicy()` 设置全局默认策略。

### 10.6 字典翻译——DictAble

```java
public interface DictAble {
    String putText(String key, String value);
    Map<String, String> getTextMap();  // key = 字典值字段名, value = 字典文本字段名
}
```

```java
public class User implements DictAble {
    private String status;

    @Override
    public Map<String, String> getTextMap() {
        Map<String, String> map = new HashMap<>();
        map.put("status", "statusText");
        return map;
    }

    @Override
    public String putText(String key, String value) {
        // 框架通过反射设置文本值
        return null;
    }
}
```

---

## 十一、多数据源与 EntityManager

### 11.1 自动路由

`GetEntityManagerFactoryStrategy` 按实体类在 JPA Metamodel 中查找归属的 EntityManagerFactory，对业务代码完全透明：

```java
// 自动选择正确的数据源——业务代码无需关心实体属于哪个库
JpaUtil.linq(User.class).list();       // 可能走主库
JpaUtil.linq(LogEntry.class).list();   // 可能走日志库
JpaUtil.save(order);                   // 自动判断归属
```

### 11.2 手动指定

```java
EntityManager em = someEntityManager;

// 查询
JpaUtil.linq(User.class, em).list();

// 删除
JpaUtil.lind(User.class, em).equal("status", 0).delete();

// 更新
JpaUtil.linu(User.class, em).set("status", 1).update();

// 获取/创建 EntityManager
EntityManager em1 = JpaUtil.getEntityManager(User.class);        // 事务感知
EntityManager em2 = JpaUtil.createEntityManager(User.class);     // 新建，非事务感知
EntityManager em3 = JpaUtil.getEntityManager("myEmfBeanName");   // 按 Bean 名称
EntityManagerFactory emf = JpaUtil.getEntityManagerFactory(User.class);
```

### 11.3 元数据方法

```java
String idName = JpaUtil.getIdName(User.class);    // 获取主键属性名 → "id"
SingularAttribute<?, ?> idAttr = JpaUtil.getId(User.class);  // 获取主键属性
boolean isEntity = JpaUtil.isEntityClass(MyClass.class);      // 判断是否为实体类
```

---

## 十二、原生 SQL 与命名查询

```java
// 原生 SQL
Query q1 = JpaUtil.nativeQuery("SELECT * FROM m_user WHERE age > ?1");
q1.setParameter(1, 18);
List result = q1.getResultList();

// 原生 SQL + 结果类映射
Query q2 = JpaUtil.nativeQuery("SELECT id, name FROM m_user", User.class);
List<User> users = q2.getResultList();

// 原生 SQL + 结果集映射名称
Query q3 = JpaUtil.nativeQuery("SELECT ...", "myResultSetMapping");

// 指定 EntityManager
Query q4 = JpaUtil.nativeQuery("SELECT ...", User.class, myEntityManager);

// 命名查询
Query q5 = JpaUtil.namedQuery("User.findByDept");
q5.setParameter("deptId", "d1");
List<User> users = q5.getResultList();

// 命名查询 + 指定 EntityManager
Query q6 = JpaUtil.namedQuery("User.findByDept", myEntityManager);
```

---

## 十三、辅助类与接口

### 13.1 AdditionalSupport

带 `@Transient` 的 `additional` 字段基类，供实体继承以附加非持久化数据：

```java
public class User extends AdditionalSupport {
    // 继承的 additional 字段不会被持久化
}

user.setAdditional(someTemporaryData);
Object data = user.getAdditional();
```

### 13.2 EntityUtils

判断类型是否为简单类型（不需要级联 CRUD 的类型）：

```java
boolean simple = EntityUtils.isSimpleType(Integer.class);  // true
boolean simple = EntityUtils.isSimpleType(User.class);     // false
```

简单类型包括：`Integer`, `String`, `Boolean`, `Short`, `Byte`, `Long`, `BigDecimal`, `Double`, `Float`, `Date`。

### 13.3 MxpioEntity

```java
public interface MxpioEntity {
    CrudType getCrudType();       // SAVE / UPDATE / DELETE / SAVE_OR_UPDATE
    boolean isSaveTransient();    // 控制是否级联持久化关联属性
}
```

### 13.4 DictAble

```java
public interface DictAble {
    String putText(String key, String value);
    Map<String, String> getTextMap();
}
```

### 13.5 SerializableFunction

Lambda 属性引用的基接口，用法：

```java
SerializableFunction<User, String> func = User::getName;
String propertyName = LambdaUtils.extractPropertyName(func);  // → "name"
```

### 13.6 JpaUtilAble / JpaUtilInitiator

启动期初始化钩子：

```java
// JpaUtilAble: 初始化完成后回调
public class MyBean implements JpaUtilAble {
    @Override
    public void afterPropertiesSet() {
        // 此时 JpaUtil 已完全初始化，可以安全使用
        JpaUtil.linq(User.class).count();
    }
}

// JpaUtilInitiator: 在 LinqConfiguration 初始化时执行
public class MyInitiator implements JpaUtilInitiator {
    @Override
    public void initialize() {
        // 向 JpaUtil 注入扩展
    }
}
```

---

## 十四、前端使用指南

### 14.1 简单查询

```javascript
import Criteria from "@/utils/criteria";
import { OPERATOR } from "@/store/mutation-types";

// 所有搜索字段使用同一种操作符
const searchData = {
    username: "admin",
    deptCode: "BM-001",
};
const query = new Criteria();
query.addCriterions(searchData, OPERATOR.LIKE);
query.addOrder({ fieldName: "createTime", desc: true });

// 序列化后作为请求参数发给后端
// POST /api/user/list  body: JSON.stringify(query)
```

对应的 JSON：

```json
{
    "criterions": [
        {"fieldName": "username", "value": "admin", "operator": "LIKE"},
        {"fieldName": "deptCode", "value": "BM-001", "operator": "LIKE"}
    ],
    "orders": [
        {"fieldName": "createTime", "desc": true}
    ]
}
```

### 14.2 字段级操作符（@ 后缀约定）

```javascript
// 使用 @操作符 后缀指定不同字段使用不同的操作符
const searchData = {
    "username@EQ": "admin",       // username 精确匹配
    "deptCode": "BM-001",          // deptCode 使用默认 LIKE
};
const query = new Criteria();
query.addCriterions(searchData, OPERATOR.LIKE);
// 结果: username EQ "admin" AND deptCode LIKE "%BM-001%"
```

### 14.3 复杂嵌套条件

```javascript
const query = new Criteria();
query
    .addCriterion("resourceType", OPERATOR.EQ, "ELEMENT")
    .or()
        .addCriterion("username", OPERATOR.EQ, "admin")
        .addCriterion("username", OPERATOR.EQ, "admin1")
        .and()
            .addCriterion("age", OPERATOR.GT, 18)
            .addCriterion("score", OPERATOR.LT, 60)
        .end()
    .end()
    .addOrder("createTime", true)
    .addOrder("updateTime", true);
```

### 14.4 后端接收

```java
@PostMapping("/list")
public Page<User> list(@RequestBody Pageable pageable, @RequestBody String criteriaJson) {
    Criteria c = CriteriaUtils.json2Criteria(criteriaJson);
    return JpaUtil.linq(User.class).where(c).paging(pageable);
}
```

---

## 十五、最佳实践与注意事项

### 15.1 推荐写法

```java
// ✅ 推荐：Lambda + collect + where(Criteria) + 分页
Page<User> page = JpaUtil.linq(User.class)
    .collect(User::getDeptId, Dept.class)
    .addIf(name)
        .like(User::getName, "%" + name + "%")
    .endIf()
    .where(criteria)                              // 前端 JSON 条件
    .desc(User::getCreateTime)
    .paging(pageable);

// ✅ 推荐：继承 SmartCrudPolicyAdapter 实现通用 CRUD 拦截
JpaUtil.save(entity, new MyCrudPolicy());
```

### 15.2 注意事项

1. **collect IN 上限**：大批量主键集合使用 IN 子句时可能超过数据库参数上限（如 Oracle 1000 限制），超大数据量需分批次处理。
2. **Linu/Lind 不触发策略**：`Linu.update()` / `Lind.delete()` 直接走 `executeUpdate()`，不触发 `CrudPolicy` 和 `@Generator`。这些扩展仅对 `save()/update()/delete()` 生效。
3. **事务**：`Linu.update()` / `Lind.delete()` 需要事务上下文。
4. **end() 的可选性**：and/or 嵌套后紧跟执行方法时末尾 `end()` 可省略；子查询中 `end()` 必须调用以返回父查询。
5. **多数据源**：实体类需唯一归属一个 EMF，否则 `getEntityManager()` 返回默认 EMF。
6. **select 投影**：`aliasToBean/aliasToMap/aliasToTuple` 必须在 `select` 之前调用。
7. **子查询 select 限制**：子查询内部 `select` 只能有一个 Selection。
8. **groupBy**：目前主要用于子查询场景，主查询中使用可能受限。
9. **CriteriaUtils.parse 属性降级**：未被 CriterionParser 拦截的点号属性会被截断取最后一段，可能产生语义偏差。使用 `setDisableSmartSubQueryCriterion()` 可规避。

### 15.3 SmartCrudPolicyAdapter vs CrudPolicy 直接实现

| 方式 | 适用场景 |
|---|---|
| 直接实现 `CrudPolicy` | 完全自定义 CRUD 行为 |
| 继承 `SmartCrudPolicyAdapter` | 只需在 CRUD 前后加逻辑（推荐） |

### 15.4 常见问题

**Q: 与 Spring Data JPA 的关系？**
A: 共存关系。可以同时使用 `JpaUtil.linq()` 和 `JpaRepository`，共享同一个 EntityManager。

**Q: collect 和 JPA @OneToMany 的区别？**
A: collect 是手动控制的批量关联加载，不依赖 JPA 关联映射，适合跨库关联或不想配置 JPA 关联的场景。`@OneToMany` 更简单但灵活度低。

**Q: SmartSubQueryParser 什么时候不会自动挂载？**
A: 不使用 `where(Criteria)`（纯 DSL 条件）、调用了 `setDisableSmartSubQueryCriterion()`、或当前在子查询内部时。

**Q: 如何实现软删除？**
A: 继承 `SmartCrudPolicyAdapter`，覆盖 `beforeDelete` 方法设置 `deleted = true` 并返回 `false`。

**Q: persist 和 save 有什么区别？**
A: `persist` 是标准 JPA 操作，仅处理当前实体。`save` 是智能操作，会递归处理对象树并执行 `@Generator` 策略和 `CrudPolicy`。
