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

## WHERE 与 HAVING

查询单笔销售超过50的“商品“

使用WHERE:

```sql
SELECT DISTINCT A.goodsname //DISTINCT(itemnumber,goodsname)
FROM (demo.goodsmaster)AS A
join (demo.transactiondetails) AS B
ON (A.itemnumber = B.itemnumber)
WHERE(B.salevalue>50)
//使用DISTINCT去除冗余
//不太确定DISTINCT后面能不能跟多个字段组成的 ”字段“ 试了一下 可以，多个字段应该就是得都相同才算作相同
```

此过程的工作流程是先通过WHERE筛选出salesvalue>50的结果，再将此筛选后的表与表A做join，最后获得联合查询的结果。

因为先where有助于缩小联合表的大小，可以增加效率



groupby + having 最终的结果似乎是 组，而不是这一组的所有数据

**group by的作用是分组，是对筛选结果的 分组、统计，最终关注的结果也是组本身，而不是每个组的数据**

~~groupby 是分组，这很直观，按照columns进行分组，之后全部展示；~~

having 不可单独出现，它是配合group by将最终的分组结果进行筛选，这个筛选的作用对象也是 组，而不是某一条数据；

且如果是关联表的情况，having是发生在连接之后，所以where的效率更高；

**where可以直接使用表中字段作为筛选条件，不可以使用分组函数，因为它发生在group by之前；having可以使用分组函数和 分组字段，其他字段不可以使用，这也说明having”筛选“的是组本身**

思考：如何用where写出group by的效果呢？暂时还想不到怎么写（可能需要业务代码去配合，总之效率会很低，且麻烦）

```sql
//包括交易时间、收银员、商品名称、销售数量、价
格和销售金额等信息，超市的经营者要查询“2020-12-10”和“2020-12-11”这两天收银金
额超过 100 元的销售日期、收银员名称、销售数量和销售金额。
//自己写的版本
SELECT b.date,c.name,count(*),count(b.salesvalue)
FROM
demo.transactionhead AS a
JOIN
demo.transactiondetails AS b ON (a.transno=b.transno)
JOIN
demo.operator AS c ON (b.opid=c.opid)
JOIN
demo.goodsmaster as d ON (b.itemnumber=d.itemnumber)
WHERE b.date='2020-12-10' | b.date='2020-12-11'
GROUP BY c.name,b.date
HAVING count(b.salesvalue)>100
//错误点
//1,求和要用SUM函数，count是计算结果行数的
//2，不需要连接商品表，因为不需要商品详细信息
//3，日期判断要用 in

//疑惑 不知道此时在select中是否可以用count(*) 可以用 就是每组的个数
SELECT b.date,c.name,SUM(b.quantity),SUM(b.salesvalue)
FROM
demo.transactionhead AS a
JOIN
demo.transactiondetails AS b ON (a.transid=b.transid)
JOIN
demo.operator AS c ON (b.opid=c.opid)
WHERE b.date IN ('2020-12-10','2020-12-11')
GROUP BY c.name,b.date
HAVING SUM(b.salesvalue)>100
//另一种条件筛选写法 不用where
HAVING SUM(b.salesvalue)>100 AND b.date IN ('2020-12-10','2020-12-11')
//不需要统计函数的条件语句可以挪到where中
```

思考题：尽管having中也可以放不使用统计函数的判断条件，但是在having中效率会降低，所以尽量将此类判断条件放入where中提高查询效率。

## 聚合函数

```sql
//咱们的项目需求是这样的：超市经营者提出，他们需要统计某个门店，每天、每个单品的销售
情况，包括销售数量和销售金额等。这里涉及 3 个数据表，具体信息如下所示：


SELECT SUM(A.salesvalue),SUM(A.quantity)
FROM
demo.transactiondetails AS A JOIN
demo.transactionhead AS B ON (A.transactionid=B.transactionid)
JOIN demo.goodsmaster AS C ON (A.itemnumber=C.itemnumber)
GROUP BY B.transdate,C.itemnumber
//缺陷：没有使用LEFT 因为date是包含分秒的，需要用LEFT将其年月日截取出来进行判断;
//没有用order by进行排序 会使得结果很混乱，且由于name和itemnumber是对应的，最后可以用name来group以显示name，否则无法
//在select中使用name


SELECT LEFT(B.transdate),C.goodsname,SUM(A.salesvalue),SUM(A.quantity)
FROM
demo.transactiondetails AS A JOIN
demo.transactionhead AS B ON (A.transactionid=B.transactionid)
JOIN demo.goodsmaster AS C ON (A.itemnumber=C.itemnumber)
GROUP BY LEFT(B.transdate),C.goodsname
ORDER BY LEFT(B.transdate),C.goodsname	

```

SUM AVG MAX MIN

理解了group by的作用机制，上述函数就都好理解了，只需要明白它们作用的是 组 就不会产生困惑。

**COUNT函数也类似，不过要注意如果COUNT(column1)，假如某一行column1为null，就不会计算这一次**

思考题：如果用户想要查询一下，在商品信息表中，到底是哪种商品的商品名称有重复，分别重复了几

次，该如何查询呢

```sql
SELECT goodsname,COUNT(*)
FROM goodsmaster
GROUP BY goodsname
HAVING COUNT(*)>1
```

## 时间函数

需求：

按照小时统计商品销售情况

```sql
SELECT HOUR(b.transdate),SUM(quantity),SUM(salesvalue)
FROM
demo.transactiondetails a
JOIN
demo.transactionhead b ON (a.transactionid = b.transactionid)
GROUP BY HOUR(b.transdate)
ORDER BY HOUR(b.transdate)
```

理解误区：每天的hour虽然是一样的，但是我们统计的是时段，本身就和 天 没有关系，所以hour相等就代表同一时段

提取的两种写法：

```sql
EXTRACT(type FROM date)
HOUR(date)
//举例
EXTRACT(HOUR FROM A.timestamp); EXTRACT(YEAR FROM A.timestamp)
HOUR(A.timestamp);YEAR(A.timestamp)
```

常用的时间计算函数:

```sql
DATE_ADD(DATE,INTERVAL 表达式 type)//将date向前或向后 type类型的 表达式时长的 日期，表达式为负表示向前
LAST_DAY(DATE)//获取DATE所在月份最后一天的日期
```

需求:

假设今天是2020 年 12 月 10 日，超市经营者提出，他们需要计算这个月单品销售金额的统计，以及与去年同期相比的增长率

```sql
//核心是通过上述两个函数获取2019-12-01与2019-12-31这两个日期
退一年-》退一个月-》最后一天-》加一天
SELECT DATE_ADD(LAST_DAY((DATE_ADD(DATE_ADD('2020-12-10',INTERVAL -1 YEAR)),INTERVAL -1 MONTH)),INTERVAL 1 DAY)
```

更多的时间函数：

假设今天是 2021 年 02 月 06 日，通过下面的代码，我们就可以查到今天商品的全部折后价

```sql
//自己的版本：
SELECT A.
FROM discountrule as A JOIN
goodsmaster as B ON A.itemnumber = B.itemnumber
WHERE weekday=DAYOFWEEK(CURDATE());
//无法解决与忽略的问题：
//1，dayofweek是以周日为1开始计算，一直到周六7，所以在日期判断上不能直接用相等，引入条件函数CASE来解决问题
//2，无法处理discount因为join查不到时为null但要将其赋为1
//3，leftjoin我是想到了，但是为什么不将日期的判断放到where中呢？准备实验一下，我觉得是相同的效果


SELECT CASE DAYOFWEEK(CURDATE()) WHEN 1 THEN 7 ELSE DAYOFWEEK(CURDATE())-1 END AS weekday,
B.goodsname,B.price,IFNULL(A.discount,1)*B.price AS discountprice
FROM discountrule as A RIGHT JOIN
goodsmaster as B ON (A.itemnumber = B.itemnumber AND 
                     A.weekday=CASE DAYOFWEEK(CURDATE()) WHEN 1 THEN 7 ELSE DAYOFWEEK(CURDATE())-1 END)

```

在写这个sql语句中遇到了很诡异的事，如果将and后的逻辑移出ON 并写入where，最后查询出的结果只有一条，顺序变成了

：on->join->where where筛选放在了联合的后面，而不是where筛选完再进行联合，至于为什么稍后专门开一小节解释。

剩下的时间函数：DATE_FORMAT(DATE,'%T') 按照一定格式显示时间；DATEDIFF(DATE1,DATE2)计算两个日期之间相差多少天，都比较好理解。

思考题：

```sql
SELECT DATE_FORMAT(CURDATE(),"%W")

```

## ON JOIN WHERE的执行顺序

在WHERE与HAVING那一节，作者说过 WHERE是在联合之前进行筛选，随后再对表进行联合；

可是当我写了LEFT JOIN的语句后发现并非如此，下面是一些思考和结论：

首先从逻辑上来说，执行顺序是完全按照SELECT的规范模板来的

```sql
SELECT FROM 
a
JOIN b ON ()
WHERE
GROUP BY
HAVING
ORDER BY
LIMIT head,length
```

如上述模板所示。首先理解ON这个条件，它其实也是一种”筛选“，只不过是将符合其中条件的 两表中的数据都留了下来，比如有一个条件a.id=b.id,就表示在表b中能找到对应的a.id的话，那么a.id对应在表a中的那一行就要被保留下来；同理对b表也做一个相同的筛选，最后再将两个筛选后的临时表进行连接。

连接这个过程比我想象中要复杂一些，如果是1对1的连接还好理解，连接后的表和两张临时表的长度是一样的，那如果出现1对多的情况要怎么连接呢？（尽管1对多在实际业务中可能并不合理，但你必须能理解这种情况是怎样的一种行为）其实就是使用”排列组合“的思想，将临时表中符合某个具体值的行全部拿出来（分为a，b两侧），将两侧的行排列组合链接就得到了该值情况下的连接结果

举个抽象的例子：a表id=1的行有2，3行；b表id=1的行有5，6行，那么我们最终对id=1的连接结果就是 2-5,2-6,3-5,3-6，这样理解就很简单了。（这里如果能画图就很直观了）

https://blog.csdn.net/muxiaoshan/article/details/7617533文中的sql1能很好的解释连接这个过程是怎么做的。

理解了join的具体原理，再解释left join就很简单，上面我们说过join是在on的筛选后进行的联合，那么left join同样如此，只不过它的左表会忽视on中的筛选条件，也就是左表的筛选后结果为全表，接下来的join就和普通的join一样，只是可能碰到一个问题，因为筛选条件的不对等，会导致某个具体的条件值在左表中有，而在右表中没有，这种时候可能就变成了1-0这种联合，解决的方案也很简单，0这一侧的行会使用NULL或者default来代替一行。

理解了上面的所有内容后，where的筛选时机就很好理解了，当普通join时，由于on中的条件是左右对等的，所以where的筛选发生在join之前或之后，结果都是一样的！（这里不会用数学或者公式证明，但是确实如此）

而left join中由于左右条件不对称，where发生在join之前就变成了对一侧筛选后再进行不对称的双侧筛选；where发生在join之后就变成了不对称的双侧筛选后再对联合结果做全部的筛选（这里确实很绕，我也没法用形象的语言解释，不过从例子中我们是可以看出差别的）

（我自己做了一个总结就是，where条件在对称时是可以从一侧传向另一侧的，也就是它同时能够约束两侧，筛选条件就变成了一致的；

而非对称时where等于被on给屏蔽了，因为on总是能输出true给强侧，那么最终的结果就显示出差异）

**最终结论：普通join时where的发生时机不影响结果，mysql为了优化效率此时会将它先于join执行；而left join与right join时，由于where的发生时机会影响结果，所以就是默认的在join后发生，mysql不会也不能对此做出优化了**

所以当使用outer join时 要特别注意筛选条件是放在on中还是where中，因为最终结果会有很大差别（时间函数最后一个例子就是如此）
