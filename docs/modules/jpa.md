# mxpio-jpa — Linq 增强查询模块

## 概述

`mxpio-jpa` 提供了一套基于 Linq（Language Integrated Query）理念的 JPA 查询增强模块。它封装了 JPA Criteria API 的复杂样板代码，允许开发者以流畅的链式调用方式构建查询条件，大幅提升数据访问层的开发效率。

### 核心特性

- **链式查询构建** — 通过 `Linq` 接口流畅构建查询
- **多种查询终端** — `Lindu`（删除）、`Lind`（删除）、`Linu`（更新）
- **丰富查询条件** — 支持 EQ、NE、LIKE、GT、LT、IN 等常用运算符
- **Lambda 属性提取** — 通过 `SerializableFunction` 类型安全地引用实体属性
- **N+1 优化** — `collect()` 方法自动处理关联集合的批量加载
- **分页查询** — 内置分页支持
- **结果转换** — 支持别名映射到 Bean、Map、Tuple
- **扩展点** — `Filter`、`SubQueryParser`、`CriterionParser` 可自定义扩展

---

## Maven 依赖

```xml
<dependency>
    <groupId>com.mxpio</groupId>
    <artifactId>mxpio-jpa</artifactId>
</dependency>
```

---

## 核心接口

### Linq — 查询入口

`Linq` 接口（继承自 `Lin`）是查询构建的入口，返回查询结果。

### Lind — 删除入口

用于根据条件批量删除。

### Lindu — 更新入口

用于根据条件批量更新。

### Linu — 更新终端

配合 `Lindu` 完成更新操作。

---

## 查询构建器

### Criteria — 条件容器

查询条件通过 `Criteria` 构建，支持多条件的 AND/OR 组合。

### SimpleCriterion — 简单条件

构建单个字段的条件：

```java
SimpleCriterion criterion = new SimpleCriterion("username", Operator.EQ, "admin");
```

**构造参数**：`fieldName`, `operator`, `value`

### Junction — 条件组合

用于 AND/OR 逻辑组合：

```java
Junction junction = new Junction(Operator.AND);
junction.add(new SimpleCriterion("status", Operator.EQ, 1));
junction.add(new SimpleCriterion("type", Operator.EQ, "user"));
```

---

## 运算符一览

| 运算符        | 说明         | 示例                              |
|---------------|--------------|-----------------------------------|
| `EQ`          | 等于         | `Operator.EQ`                     |
| `NE`          | 不等于       | `Operator.NE`                     |
| `LIKE`        | 模糊匹配     | `Operator.LIKE`（%keyword%）      |
| `LIKE_END`    | 左模糊       | `Operator.LIKE_END`（keyword%）   |
| `LIKE_START`  | 右模糊       | `Operator.LIKE_START`（%keyword） |
| `NOT_LIKE`    | 不匹配       | `Operator.NOT_LIKE`               |
| `GT`          | 大于         | `Operator.GT`                     |
| `LT`          | 小于         | `Operator.LT`                     |
| `GE`          | 大于等于     | `Operator.GE`                     |
| `LE`          | 小于等于     | `Operator.LE`                     |
| `AND`         | 逻辑与       | `Operator.AND`                    |
| `OR`          | 逻辑或       | `Operator.OR`                     |
| `IN`          | 包含于       | `Operator.IN`                     |
| `NOT_IN`      | 不包含于     | `Operator.NOT_IN`                 |
| `IS_NULL`     | 为空         | `Operator.IS_NULL`                |
| `IS_NOT_NULL` | 不为空       | `Operator.IS_NOT_NULL`            |

---

## Lambda 属性引用

通过 `SerializableFunction` 和 `LambdaUtils` 实现类型安全的属性名提取，避免硬编码字符串：

```java
import com.mxpio.jpa.lambda.SerializableFunction;
import com.mxpio.jpa.lambda.LambdaUtils;

// 定义 Lambda 函数引用
SerializableFunction<User, String> fn = User::getUsername;

// 提取属性名
String propertyName = LambdaUtils.extractPropertyName(fn);
// 返回: "username"
```

在查询中使用：

```java
linq.eq(User::getUsername, "admin");
// 等价于: linq.eq("username", "admin");
```

---

## 基本查询示例

### 条件查询

```java
// 获取 Linq 实例
Linq<User> linq = mxpioLinqFactory.create(User.class);

// 简单条件查询
List<User> users = linq
    .eq(User::getStatus, 1)
    .like(User::getUsername, "admin")
    .list();
```

### 分页查询

```java
// 使用 Pageable 分页
Page<User> page = linq
    .eq(User::getDeptId, deptId)
    .paging(PageRequest.of(0, 10));

// 直接指定页码和大小
List<User> userList = linq
    .eq(User::getStatus, 1)
    .list(1, 20); // 第1页，每页20条
```

### 查询单条记录

```java
User user = linq
    .eq(User::getId, 1001L)
    .findOne();
```

### 统计与判断

```java
// 统计数量
long count = linq
    .eq(User::getStatus, 1)
    .count();

// 判断是否存在
boolean exists = linq
    .eq(User::getUsername, "admin")
    .exists();
```

---

## 结果转换

查询结果支持多种转换方式：

```java
// 转换为指定 Bean
List<UserVO> voList = linq
    .eq(User::getStatus, 1)
    .list()
    .aliasToBean(UserVO.class);

// 转换为 Map（key 为别名）
List<Map<String, Object>> mapList = linq
    .eq(User::getStatus, 1)
    .list()
    .aliasToMap();

// 转换为 Tuple
List<Tuple> tuples = linq
    .eq(User::getStatus, 1)
    .list()
    .aliasToTuple();

// 直接投射到 DTO
List<UserDTO> dtos = linq
    .eq(User::getStatus, 1)
    .list()
    .aliasToBean(UserDTO.class);
```

---

## N+1 优化：collect()

在涉及关联查询时，`collect()` 可以批量加载关联集合，避免 N+1 问题：

```java
// 批量加载用户及其角色关联
List<User> users = linq
    .eq(User::getStatus, 1)
    .collect(User::getRoles); // 自动优化角色集合加载

// 支持 ManyToMany 关联，通过 relationClass 指定中间表
List<Student> students = linq
    .eq(Student::getGrade, "三年级")
    .collect(Student::getCourses, Course.class); // 指定关联实体
```

---

## 复杂条件组合

```java
// AND 与 OR 嵌套
List<Order> orders = linq
    .and(
        criterion.eq(Order::getStatus, 1),
        criterion.or(
            criterion.eq(Order::getType, "A"),
            criterion.eq(Order::getType, "B")
        )
    )
    .list();
```

---

## 扩展点

| 扩展点              | 说明                       | 使用场景                   |
|---------------------|----------------------------|----------------------------|
| `Filter`            | 自定义过滤器接口           | 实现自定义查询逻辑         |
| `SubQueryParser`    | 子查询解析器               | 解析子查询条件             |
| `CriterionParser`   | 条件解析器                 | 自定义条件解析行为         |

---

## MxpioEntity — 基础实体

所有 JPA 实体应继承 `MxpioEntity`，提供通用基础字段：

```java
import com.mxpio.jpa.entity.MxpioEntity;
import javax.persistence.*;

@Entity
@Table(name = "my_entity")
public class MyEntity extends MxpioEntity {
    // 自带字段：id, createBy, createTime, updateBy, updateTime, delFlag
    // 只需添加业务字段

    private String name;
    private Integer status;

    // getters / setters ...
}
```

---

## 配置文件

| 配置项                               | 默认值  | 说明                   |
|--------------------------------------|---------|------------------------|
| `spring.jpa.show-sql`                | `false` | 是否显示 SQL           |
| `spring.jpa.properties.*`            | —       | JPA 扩展属性           |
| `mxpio.jpa.linq.*`                   | —       | mxpio Linq 自定义配置   |

---

## 依赖关系

```
mxpio-jpa
├── mxpio-common
├── spring-boot-starter-data-jpa
└── hibernate-core
```
