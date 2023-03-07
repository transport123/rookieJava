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



## 插入时的字段约束

**在修改列属性时，假设修改后与列中数据存在冲突（比如给列添加not null但是却有为null的数据)则修改会失败**

```sql
ALTER TABLE demo.goodsmaster
MODIFY specification TEXT NOT NULL
```

假如此时有一条数据的specification为null则执行失败

**sql可以插入一行不完整的数据，但是前提是为空的列要知道怎么去处理，比如可以为null，有默认值，自增，否则这种插入也会失败**

```sql
INSERT INTO TABLENAME (COLUMNS...) 
SELECT (COLUMNS...) FROM TABLENAME2
WHERE ... 
//插入同样可以插入查询结果
```

## 删除

```sql
DELETE FROM TABLENAME
WHERE ...
```

注意mysql做了一定的限制，在使用delete时where后的条件必须存在，如果想删除所有数据需要利用主键或者其他列属性来写match所有行的条件语句

## 修改

```sql
UPDATE TABLE
SET COLUMN1=VALUE1,COLUMN2=VALUE2
WHERE...
```

使用修改时尽量不要修改主键，否则容易引起不可预计的错误；

set后可以跟多条属性。

## 查询

```sql
SELECT *|COLUMNS 
FROM TABLENAME
WHERE ...
GROUP BY COLUMN1
HAVING ...
ORDER BY COLUMN2
LIMIT starter,rowsnumber
```

GROUPBY表示查询结果如何分组，经常和聚合函数一起使用；

FROM后不仅能跟表名，还可以跟关联表（暂时不关注），**派生表** 也就是将某个查询语句的结果作为一张临时表，mysql规定必须使用as

将派生表进行临时的命名。

```sql
SELECT TEMP.goodsname
FROM (SELECT * FROM demo.goodsmaster) AS TEMP
```

ORDER BY COLUMN1 DESC,COLUMN2 ASC;

orderby表示按照某种属性升序或者降序排列，可以有多个排列规则同时出现，原则是在满足了前面的规则后，将COLUMN1相同的行按照第二条规则再排列 

LIMIT head,rows;表示只想看[head,head+rows]的查询结果

## on duplicate

可以解决“如果有就更改，没有就插入的问题”

假设要做两张相似表的合并：

```sql
INSERT INTO demo.goodsmaster
(SELECT * FROM demo.goodsmaster1) as A
ON DUPLICATE KEY UPDATE barcode = A.barcode, goodsname=A.goodsname;
```

扩展：可以看到ON DUPLACATE 是作为一个描述“重复”， KEY表示“重复的字段是哪个”，理想情况下即唯一的主键（索引）

当存在多个唯一的索引时可能会有bug，所以引入了gap锁，但是gap锁又可能导致事务的死锁，具体解释在下面的链接，目前还不理解这些锁的原理，不必深入。

on duplicate的缺陷与解决办法：https://cloud.tencent.com/developer/article/1609770

insert ignore 是忽略已存在的数据，插入不存在的数据

## 主键自增不连续

思考题：可以利用count(*)是否等于max(key)-min(key)+1来判断是否有缺失的id 至于怎么找到缺失的id就有些麻烦了

https://blog.csdn.net/bfz_50/article/details/113699457

https://www.jianshu.com/p/1da3f7d73d72

这里关于id不连续自增的坑还是有点多的，以后遇到了回头看看

## 如何正确的设置主键

1，尽量不要用业务字段作为主键，因为在实际场景中字段的变化难以预料，就像例子中的 原卡主不用卡后，新卡主使用了原卡号，相关联的原卡主交易流水就被“分配”给了新卡主，这样就出现了错误；

2，如果是单一数据库可以使用自增的字段作主键；

3，如果是多个服务器对应多个数据库，自增就可能造成重复主键的问题，这个时候就要用业务逻辑来自行设置主键值



思考题：使用 以下方式（两张表进行条件匹配）灵活的进行数据库更改 （这里还没有用到JOIN）

```sql
UPDATE demo.goodsmaster AS a,demo.trans AS b
SET a.price=a.price*0.8// SET price=price*0.8 (因为这里要对ab表的price都进行修改)
WHERE a.itemnumber = b.itemnumber
AND b.unit = '包'
```

## 多表查询

### 外键

主表与从表是一种相对的关系，A与B通过A中的id字段发生关系R，那么A在关系R中就是主表，B就是从表，B中的aid引用了A中的id，aid这个字段就是外键。

外键需要创建，在创建后AB表之间就有了约束，假如要删除A中id被B中aid引用的数据，就会发生错误。

创建外键的方式：

```sql
[CONSTRAINT 外键约束名]FOREIGN KEY 字段名
REFERENCES <主表名> 字段名

//创建主表
CREATE TABLE demo.importhead (
 listnumber INT PRIMARY KEY,
 supplierid INT,
 stocknumber INT,
 importtype INT,
 importquantity DECIMAL(10 , 3 ),
 importvalue DECIMAL(10 , 2 ),
 recorder INT,
 recordingdate DATETIME
);
 
 //创建从表
CREATE TABLE demo.importdetails
(
listnumber INT,
itemnumber INT,
quantity DECIMAL(10,3),
importprice DECIMAL(10,2),
importvalue DECIMAL(10,2),
CONSTRAINT fk_detail_listnumber FOREIGN KEY (listnumber)
REFERENCES demo.importhead (listnumber)
)

ALTER TABLE demo.importdetails
ADD CONSTRAINT fk_detail_listnumber FOREIGN KEY (listnumber)
REFERENCES demo.importhead (listnumber)
//ADD 也可以用来ADD PRIMARY KEY
```

### 连接

### INNER JOIN

返回满足条件的记录（INNER JOIN = JOIN = CROSS JOIN)

```sql
SELECT
    b.cardno,
    b.membername,
    b.memberphone,
    a.transactionnumber
FROM 
demo.trans as a JOIN
demo.membermaster as b
ON (a.cardno=b.cardno)
```

会将ab表中cardno相同的记录选出

### OUTER JOIN

返回某一个表中的所有记录，同时返回满足条件的另一个表中的字段值， 否则为null?default?

```sql
SELECT
a.transactionnumber,
a.transactiontime,
a.price,
b.mermbername
FROM
demo.trans as a LEFT JOIN
demo.membermaster as b
ON(a.cardno=b.cardno)
```

会返回a表中的所有记录，如果在b中有cardno相同的就补上membername，否则为空



虽然不用外键我们也可以使用关联查询，且外键在高并发的情况下确实会有性能的影响，但是这不意味着不推荐使用外键，因为外键是一种简单的保证关联表数据正确的手段，否则我们必须通过外部业务逻辑来确保数据的正确性。

思考题：通过定义”原子操作“，将对 从表 和 主表 的操作绑定在一起，比如要删除时，我们先删除从表中外键符合条件的记录，再删除主表中对应的数据 （疑问，外键引用的必须是主键吗，满足唯一性的索引可以吗？答：不一定，但必须是唯一性索引。）





一个问题：sql的批量查询怎么写，java springboot mybatis是怎么处理的，如何解决批量 存在即更新 不存在就插入的问题

insert update是mysql特有的功能；replace into 坑很多；转换思路：先批量查询，根据结果分成两类，再对两类数据分别进行批量更新与批量插入。

