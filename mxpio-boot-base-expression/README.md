# Panda-expression 表达式引擎

## 介绍

Panda-expression构建在著名的aviator引擎之上。保留了aviator全部功能。

[Aviator](https://github.com/killme2008/aviator)是一个轻量级、高性能的Java表达式执行引擎,相关内容可参考官方文档。

## 使用方法

Panda-expression可以使用在Panda环境中，也可以单独使用在其他Spring项目中。使用方法如下

> Panda环境中，您可以在pom中添加Panda-expression依赖即可，无需指定版本号，maven会自动拉取适合的版本

```xml

	<dependency>
		<groupId>org.malagu.panda</groupId>
		<artifactId>panda-expression</artifactId>
	</dependency>

```

> 在非Panda环境中，您除了需要再maven中添加依赖以外（带版本号），同时扫描org.malagu.panda.expression包及其子包的Spring注解。

## 快速开始

```java
int[] a = ...;
Map<String, Object> env = new HashMap<String, Object>();
env.put("a", a);

AviatorEvaluator.execute("1 + 2 + 3");
AviatorEvaluator.execute("a[1] + 100", env);
AviatorEvaluator.execute("'a[1]=' + a[1]", env);

// 求数组长度
AviatorEvaluator.execute("count(a)", env);

// 求数组总和
AviatorEvaluator.execute("reduce(a, +, 0)", env);

// 检测数组每个元素都在 0 <= e < 10 之间
AviatorEvaluator.execute("seq.every(a, seq.and(seq.ge(0), seq.lt(10)))", env);

// Lambda 求和
AviatorEvaluator.execute("reduce(a, lambda(x,y) -> x + y end, 0)", env);

// 导入 String 类实例方法作为自定义函数
AviatorEvaluator.addInstanceFunctions("s", String.class);
AviatorEvaluator.execute("s.indexOf('hello', 'l')");
AviatorEvaluator.execute("s.replaceAll('hello', 'l', 'x')");

// 导入静态方法作为自定义函数
AviatorEvaluator.addStaticFunctions("sutil", StringUtils.class);
AviatorEvaluator.execute("sutil.isBlank('hello')");

// 启用基于反射的 function missing 机制，调用任意 public 实例方法，无需导入
AviatorEvaluator.setFunctionMissing(JavaMethodReflectionFunctionMissing.getInstance());
// 调用 String#indexOf
System.out.println(AviatorEvaluator.execute("indexOf('hello world', 'w')"));
// 调用 Long#floatValue
System.out.println(AviatorEvaluator.execute("floatValue(3)"));
// 调用 BigDecimal#add
System.out.println(AviatorEvaluator.execute("add(3M, 4M)"));

```

更详细的请阅读 [Aviator用户指南](https://github.com/killme2008/aviator/wiki) 。

## 扩展功能

### 自定义函数

> 原来的使用方式需要实现com.googlecode.aviator.runtime.type.AviatorFunction接口, 并注册到AviatorEvaluator中

```java

public class TestAviator {
    public static void main(String[] args) {
        //注册函数
        AviatorEvaluator.addFunction(new AddFunction());
        System.out.println(AviatorEvaluator.execute("add(1, 2)"));           // 3.0
        System.out.println(AviatorEvaluator.execute("add(add(1, 2), 100)")); // 103.0
    }
}
class AddFunction extends AbstractFunction {
    @Override
    public AviatorObject call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2) {
        Number left = FunctionUtils.getNumberValue(arg1, env);
        Number right = FunctionUtils.getNumberValue(arg2, env);
        return new AviatorDouble(left.doubleValue() + right.doubleValue());
    }
    public String getName() {
        return "add";
    }
}

```

> 现在您依然可以使用Aviator原生的方式注册函数，也可以直接实现org.malagu.panda.expression.func.type.AbstractSpringAviatorFunction接口，并注册到Spring IOC中即可，同时可以通过实现disabled()来设定函数是否启用.

```java

@Component
public class AddFunction extends AbstractSpringAviatorFunction {
	@Override
	public AviatorObject call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2) {
		Number left = FunctionUtils.getNumberValue(arg1, env);
        Number right = FunctionUtils.getNumberValue(arg2, env);
        return new AviatorDouble(left.doubleValue() + right.doubleValue());
	}

	@Override
	public String getName() {
		return "add";
	}
	
	@Override
	public boolean disabled() {
		return true;
	}

}

```

## 内置函数表

| 函数名称 | 说明 |
| :------| :------ |
| sysdate() | 返回当前日期对象 java.util.Date |
| rand() | 返回一个介于 0-1 的随机数,double 类型 |
| print([out],obj) | 打印对象,如果指定 out,向 out 打印, 否则输出到控制台 |
| println([out],obj) | 与 print 类似,但是在输出后换行 |
| now() | 返回 System.currentTimeMillis |
| long(v) | 将值的类型转为 long |
| double(v) | 将值的类型转为 double |
| str(v) | 将值的类型转为 string |
| date_to_string(date,format) | 将 Date 对象转化化特定格式的字符串,2.1.1 新增 |
| string_to_date(source,format) | 将特定格式的字符串转化为 Date 对 象,2.1.1 新增 |
| string.contains(s1,s2) | 判断 s1 是否包含 s2,返回 Boolean |
| string.length(s) | 求字符串长度,返回 Long |
| string.startsWith(s1,s2) | s1 是否以 s2 开始,返回 Boolean |
| string.endsWith(s1,s2) | s1 是否以 s2 结尾,返回 Boolean |
| string.substring(s,begin[,end]) | 截取字符串 s,从 begin 到 end,如果忽略 end 的话,将从 begin 到结尾,与 java.util.String.substring 一样。 |
| string.indexOf(s1,s2) | java 中的 s1.indexOf(s2),求 s2 在 s1 中 的起始索引位置,如果不存在为-1 |
| string.split(target,regex,[limit]) | Java 里的 String.split 方法一致,2.1.1 新增函数 |
| string.join(seq,seperator) | 将集合 seq 里的元素以 seperator 为间隔 连接起来形成字符串,2.1.1 新增函数 |
| string.replace_first(s,regex,replacement) | Java 里的 String.replaceFirst 方法, 2.1.1 新增 |
| string.replace_all(s,regex,replacement) | Java 里的 String.replaceAll 方法 , 2.1.1 新增 |
| math.abs(d) | 求 d 的绝对值 |
| math.sqrt(d) | 求 d 的平方根 |
| math.pow(d1,d2) | 求 d1 的 d2 次方 |
| math.log(d) | 求 d 的自然对数 |
| math.log10(d) | 求 d 以 10 为底的对数 |
| math.sin(d) | 正弦函数 |
| math.cos(d) | 余弦函数 |
| math.tan(d) | 正切函数 |
| map(seq,fun) | 将函数 fun 作用到集合 seq 每个元素上, 返回新元素组成的集合 |
| filter(seq,predicate) | 将谓词 predicate 作用在集合的每个元素 上,返回谓词为 true 的元素组成的集合 |
| count(seq) | 返回集合大小 |
| include(seq,element) | 判断 element 是否在集合 seq 中,返回 boolean 值 |
| sort(seq) | 排序集合,仅对数组和 List 有效,返回排 序后的新集合 |
| reduce(seq,fun,init) | fun 接收两个参数,第一个是集合元素, 第二个是累积的函数,本函数用于将 fun 作用在集合每个元素和初始值上面,返回 最终的 init 值 |
| seq.eq(value) | 返回一个谓词,用来判断传入的参数是否跟 value 相等,用于 filter 函数,如filter(seq,seq.eq(3)) 过滤返回等于3 的元素组成的集合 |
| seq.neq(value) | 与 seq.eq 类似,返回判断不等于的谓词 |
| seq.gt(value) | 返回判断大于 value 的谓词 |
| seq.ge(value) | 返回判断大于等于 value 的谓词 |
| seq.lt(value) | 返回判断小于 value 的谓词 |
| seq.le(value) | 返回判断小于等于 value 的谓词 |
| seq.nil() | 返回判断是否为 nil 的谓词 |
| seq.exists() | 返回判断不为 nil 的谓词 |

## Links

* Aviator github : <https://github.com/killme2008/aviator>
* Aviator wiki : <https://github.com/killme2008/aviator/wiki>