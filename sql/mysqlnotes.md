## 数据类型注意事项

Int：TINYINT 1字节；SMALLINT 2字节；MEDIUMINT 3字节；INT 4字节；BIGINT 8字节

浮点数：float 4字节；double 8字节  由于浮点数是通过二进制来表示，所以精度会有问题，在进行等号判断时需要格外注意；

定点数：将小数点 前和后 的部分拆开来分别转换成16进制存储 使用DECIMAL(M,D)的方式去定义M<=65 D表示小数点后的位数

~~浮点数表示范围大但是不精准，平时使用中decimal用的更多一些~~

DECIMAL是根据M的值动态调整它的占用空间，可以看到它的理论值要比8个字节的double 还要大，但是同时占用的空间也更多，所以当在数据范围不大的情况下，我们可以设置较小的M值保证精准计算的同时占据空间也比较少。

TEXT: 系统按照实际长度进行存储，不需要定义长度。

TINYTEXT 255字符；TEXT 65535字符；MEDIUMTEXT 2的24次方个字符;LONGTEXT 4G；

CHAR(M):定长字符串，预先定义M的值表示长度为多少 

VARCHAR(M):变长字符串，也需要定义M为最大长度

EUNM ：枚举类型，取值为预先定义的可能的结果中的一个，必须知道所有可能的结果；

 SET: 与ENUM类似，但是取值是0或多个可能的结果（相当于集合）

由于text实际存储长度不固定，无法作为主键；只能使用CHAR(M)或VARCHAR(M)

日期：

DATETIME最精准，推荐使用。 YYYY-MM-DD HH:MM:SS 1000-01-01 00:00:00 ~ 9999-12-31 23:59:59 8字节

TIMESTAMP范围相对较小

## 创建表时的约束类型

非空约束

唯一性约束 不同于主键约束，它的值可以为空；

自增约束 只有int类型可以定义为自增约束，并且手动插入值后自增基数会改变

常用的一些语句格式：

```sql
 CREATE TABLE demo.template{
 name type NOT NULL PRIMARY KEY UNIQUE AUTO_INCREMENT DEFAULT value
 }
 
 ALTER TABLE demo.template
 ADD COLUMN name type FIRST|AFTER anycolumn,
 CHANGE oldname newname type,
 MODIFY name type FIRST|AFTER anycolumn
```

注意一条alter table后如果想写多条表修改的语句，需要使用逗号隔开，不可以用分号，也不可以省略。



## context

看到第四章中段 select部分













一个问题：sql的批量查询怎么写，java springboot mybatis是怎么处理的，如何解决批量 存在即更新 不存在就插入的问题

insert update是mysql特有的功能；replace into 坑很多；转换思路：先批量查询，根据结果分成两类，再对两类数据分别进行批量更新与批量插入。

