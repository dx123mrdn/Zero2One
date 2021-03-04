

# <font color='#b42222'>![](D:\WorkSpace\MyTest\Rosefinch\doc\进阶.png)MySQL进阶实战篇</font>

### <font color='#e6000b'>一、 MySQL视图机制</font>

#### <font color='#ff6537'>1. 什么是视图？</font>

##### <font color='#054fd2'>① 概念</font>

- 	视图是指计算机数据库中的视图，是一个虚拟表，其内容由查询定义。同真实的表一样，视图包含一系列带有名称的列和行数据。但是，视图并不在数据库中以存储的数据值集形式存在。行和列数据来自由定义视图的查询所引用的表，并且在引用视图时动态生成。

##### <font color='#054fd2'>② 名称</font>

VIEW

##### <font color='#054fd2'>③ 简介</font>

- 	对其中所引用的基础表来说，视图的作用类似于筛选。定义视图的筛选可以来自当前或其它数据库的一个或多个表，或者其它视图。分布式查询也可用于定义使用多个异类源数据的视图。如果有几台不同的服务器分别存储组织中不同地区的数据，而您需要将这些服务器上相似结构的数据组合起来，这种方式就很有用。      在mssql，oracle里，视图是不支持输入参数的，因此有些人宁愿用存储过程，也不用视图，而且存储过程和视图，在效率上，基本上没什么区别。     虽然视图不支持输入参数，但在一些数据量不多，但查询比较复杂的操作情况，利用视图来进行开发，是比较方便的。

##### <font color='#054fd2'>④ 含义</font>

- 存储在数据库中的查询的SQL 语句

##### <font color='#054fd2'>⑤ 优点</font>

- ###### <font color='#1cb569'>第一点：   使用视图，可以定制用户数据，聚焦特定的数据。</font>

  	解释：在实际过程中，公司有不同角色的工作人员，我们以销售公司为例的话，采购人员，可以需要一些与其有关的数据，而与他无关的数据，对他没有任何意义，我们可以根据这一实际情况，专门为采购人员创建一个视图，以后他在查询数据时，只需select  *  from  view_caigou  就可以啦。

- ###### <font color='#1cb569'>第二点：使用视图，可以简化数据操作。 </font>

  	解释：我们在使用查询时，在很多时候我们要使用聚合函数，同时还要显示其它字段的信息，可能还会需要关联到其它表，这时写的语句可能会很长，如果这个动作频繁发生的话，我们可以创建视图，这以后，我们只需要select  *  from  view1就可以啦，这样很方便。

- ###### <font color='#1cb569'>第三点：使用视图，基表中的数据就有了一定的安全性 。</font>

  	解释：因为视图是虚拟的，物理上是不存在的，只是存储了数据的集合，我们可以将基表中重要的字段信息，可以不通过视图给用户，视图是动态的数据的集合，数据是随着基表的更新而更新。同时，用户对视图，不可以随意的更改和删除，可以保证数据的安全性。

- ###### <font color='#1cb569'>第四点：可以合并分离的数据，创建分区视图。</font>

  	解释：随着社会的发展，公司的业务量的不断的扩大，一个大公司，下属都设有很多的分公司，为了管理方便，我们需要统一表的结构，定期查看各公司业务情况，而分别看各个公司的数据很不方便，没有很好的可比性，如果将这些数据合并为一个表格里，就方便多啦，这时我们就可以使用union关键字，将各分公司的数据合并为一个视图。  

##### <font color='#054fd2'>⑥ 缺点</font>

- ###### <font color='#1cb569'>性能差</font>　

   	sql server必须把视图查询转化成对基本表的查询，如果这个视图是由一个复杂的多表查询所定义，那么，即使是视图的一个简单查询，sql server也要把它变成一个复杂的结合体，需要花费一定的时间。

- ###### <font color='#1cb569'>修改限制</font>

   当用户试图修改试图的某些信息时，数据库必须把它转化为对基本表的某些信息的修改，对于简单的试图来说，这是很方便的，但是，对于比较复杂的试图，可能是不可修改的。

- ###### <font color='#1cb569'>举例如下</font>

  基础教程中创建了4张表。

  ```mysql
  #items表创建语句
  CREATE TABLE 'items' (
    'id' int(11) NOT NULL AUTO_INCREMENT,
    'name' varchar(32) NOT NULL COMMENT '商品名称',
    'price' float(10,1) NOT NULL COMMENT '商品定价',
    'detail' text COMMENT '商品描述',
    'pic' varchar(64) DEFAULT NULL COMMENT '商品图片',
    'createtime' datetime NOT NULL COMMENT '生产日期',
    PRIMARY KEY ('id')  -- <---------------------------指明items的唯一主键字段
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
  
  #user表创建语句
  CREATE TABLE 'user' (
    'id' int(11) NOT NULL AUTO_INCREMENT,
    'username' varchar(32) NOT NULL COMMENT '用户名称',
    'birthday' date DEFAULT NULL COMMENT '生日',
    'sex' char(1) DEFAULT NULL COMMENT '性别',
    'address' varchar(256) DEFAULT NULL COMMENT '地址',
    PRIMARY KEY ('id')
  ) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8;
  
  
  #订单表orders创建语句
  CREATE TABLE 'orders' (
    'id' int(11) NOT NULL AUTO_INCREMENT,
    'user_id' int(11) NOT NULL COMMENT '下单用户id',
    'number' varchar(32) NOT NULL COMMENT '订单号',
    'createtime' datetime NOT NULL COMMENT '创建订单时间',
    'note' varchar(100) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY ('id'),
    CONSTRAINT 'FK_orders_id' FOREIGN KEY ('user_id') REFERENCES 'user' ('id') ON DELETE NO ACTION ON UPDATE NO ACTION
  ) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
  
  
  #订单详情表order_info创建语句
  DROP TABLE `t_anvanced`.`order_info`;
  CREATE TABLE `t_anvanced`.`order_info`  (
    `id` int(11) NOT NULL COMMENT 'id',
    `orders_id` int(11) NOT NULL COMMENT '订单id',
    `items_id` int(11) NOT NULL COMMENT '商品id',
    `items_num` int(11) NULL COMMENT '商品购买数量',
    PRIMARY KEY (`id`),
    CONSTRAINT `orders_id` FOREIGN KEY (`orders_id`) REFERENCES `t_anvanced`.`orders`(`id`),
    CONSTRAINT `items_id` FOREIGN KEY (`items_id`) REFERENCES `t_anvanced`.`items`(`id`)
  );
  ```

  - 	因为视图与数据库中存在的表不太一样，前面我们创建的4张表都是包含数据的，如用户信息，订单信息等，而视图则是不包含数据的，下面通过一个例子来演示视图,下面的sql是查询王五的所有订单情况，需要关联到orders表、order_info表、items表、user表。

  ```mysql
  SELECT u.username, o.number, tm.NAME AS itemsName, tm.price, od.items_num FROM
  	(
  		( orders AS o INNER JOIN order_info AS od ON o.id = od.orders_id )
  		INNER JOIN items AS tm ON od.items_id = tm.id 
  	)
  	INNER JOIN USER AS u ON o.user_id = u.id 
  WHERE
  	username = '王五';
  ```

  - 		上面sql显然数据已如期查询出来了，但是我们发现任何需要这个数据的人都必须了解相关联的表结构，并且需要知道如何创建查询和对表进行联结，为了检索其他用户的相同数据必须修改Where条件并带上一大段关联查询的sql语句。是的，每次这样的操作确实挺麻烦的，假如现在可以把这个除了where条件外的sql查询出来的数据包装成一个名为user_order_data的虚拟表，就可以使用以下方式检索出数据了。

  ```sql
  select * from user_order_data where username='王五';
  ```

  - 		按这样的方式每次查询不同的用户只需修改where条件即可也不同再写那段看起有点恶心的长sql了，而事实上user_order_data就是一张视图表，也可称为虚拟表，而这就是视图最显著的作用了。

#### <font color='#ff6537'>2. 视图的创建与使用</font>

- 了解完什么是视图后，我们来看看如何创建视图和使用视图，使用以下语法可创建视图：

```mysql
-- 创建视图虚拟表user_order_data
CREATE VIEW user_order_data ( username, number, itemname, price, items_num ) AS SELECT
u.username, o.number, tm.NAME, tm.price, od.items_num 
FROM
	(
		( orders AS o INNER JOIN orderdetail AS od ON o.id = od.orders_id )
		INNER JOIN items AS tm ON od.items_id = tm.id 
	)
	INNER JOIN USER AS u ON o.user_id = u.id;
```

- 现在我们使用前面关联查询的orders表、order_info表、items表、user表来创建视图user_order_data。

```mysql
-- 使用视图
SELECT *  FROM user_order_data;
```

- 		可以看出除了在select语句前面加上create view user_order_data as外，其他几乎没变化。在使用视图user_order_data时，跟使用数据库表没啥区别，因此以后需要查询指定用户或者所有用户的订单情况时，就不用编写长巴巴的一段sql了，还是蛮简洁的。除了上述的方式，还可以将视图虚拟表的字段别名移动到查询字段后面：

```mysql
 CREATE OR REPLACE VIEW user_order_data
 AS
 SELECT
 	u.username as username, 
 	o.number as number , 
 	tm.name as name , 
 	tm.price as price , 
 	od.items_num as items_num
 FROM
 (
 (orders as o INNER JOIN orderdetail as od ON o.id = od.orders_id ) 
 INNER JOIN items as tm ON od.items_id = tm.id 
 )
 INNER JOIN user as u ON o.user_id = u.id;
```

- 注意这里使用了<font color='red'>`CREATE OR REPLACE VIEW`</font>语句，意思就是不存在就创建，存在就替换。


- 如果想删除视图可以使用以下语法：

```mysql
DROP VIEW 视图名称;
```

在使用视图的过程还有些需要注意的点，如下：

- <font color='#1cb569'>与创建表一样，创建视图的名称必须唯一</font>

  创建视图的个数并没限制，但是如果一张视图嵌套或者关联的表过多，同样会引发性能问题，在实际生产环节中部署时务必进行必要的性能检测。

- <font color='#1cb569'>在过滤条件数据时如果在创建视图的sql语句中存在where的条件语句，而在使用该视图的语句中也存在where条件语句时，这两个where条件语句会自动组合。</font>


- <font color='#1cb569'>order by 可以在视图中使用，但如果从该视图检索数据的select语句中也含有order by ，那么该视图中的order by 将被覆盖。</font>


- <font color='#1cb569'>视图中不能使用索引，也不能使用触发器(索引和触发器后面会分析)</font>


- <font color='#1cb569'>使用可以和普通的表一起使用，编辑一条联结视图和普通表的sql语句是允许的。</font>



	关于使用视图对数据的进行更新(增删改)，因为视图本身并没有数据，所以这些操作都是直接作用到普通表中的，但也并非所有的视图都可以进行更新操作,如视图中存在分组(group by)、联结、子查询、并(unoin)、聚合函数(sum/count等)、计算字段、DISTINCT等都不能对视图进行更新操作，因此我们前面的例子也是不能进行更新操作的，事实上，视图更多的是用于数据检索而更新，因此对于更新也没有必要进行过多阐述。

#### <font color='#ff6537'>3. 视图的本质</font>

- 	到此对于视图的创建和使用都比较清晰了，现在准备进一步认识视图的本质，前面我们反复说过，视图是一张虚拟表，是不带任何数据的，每次查询时只是从普通表中动态地获取数据并组合，只不过外表看起来像一张表罢了。其原理通过下图便一目了然：

![E](D:\WorkSpace\MyTest\Rosefinch\doc\视图本质.png)

- 	事实上有些时候视图还会被用于限制用户对普通表的查询操作，对于这类用户只赋予对应视图的select操作权限，仅让他们只能读取特定的行或列的数据。这样我们也就不用直接使用数据库的权限设置限制行列的读取，同时也避免了权限细化的麻烦。

### <font color='#e6000b'>二、 高效索引</font>

#### <font color='#ff6537'>1. 使用索引的理由是什么？</font>

> 	由于mysql在默认情况下，表中的数据记录是没有顺序可言的，也就是说在数据检索过程中，符合条件的数据存储在哪里，我们是完全不知情的，如果使用select语句进行查询，数据库会从第一条记录开始检索，即使找到第一条符合条件的数据，数据库的搜索也并不会因此而停止，毕竟符合条件的数据可能并不止一条，也就是说此时检索会把表中的数据全部检索一遍才结束，这样的检索方式也称为全表扫描。但假设表中存在上百上千万条数据呢？这样的检索效率就十分低了，为了解决这个问题，索引的概念就诞生了，索引是为检索而存在的。如一些书的末尾一般会提供专门附录索引，指明了某个关键字在正文中的出现的页码位置或章节的位置，这样只要找到对应页面就能找到要搜索的内容了，数据库的索引也是类似这样的原理，通过创建某个字段或者多个字段的索引，在搜索该字段时就可以根据对应的索引进行快速检索出相应内容而无需全表扫描了。

#### <font color='#ff6537'>2. 什么是索引？</font>

- ##### <font color='#054fd2'>概念</font>

   	在关系数据库中，索引是一种单独的、物理的对数据库表中一列或多列的值进行排序的一种存储结构，它是某个表中一列或若干列值的集合和相应的指向表中物理标识这些值的数据页的逻辑指针清单。索引的作用相当于图书的目录，可以根据目录中的页码快速找到所需的内容。
     索引提供指向存储在表的指定列中的数据值的指针，然后根据您指定的排序顺序对这些指针排序。数据库使用索引以找到特定值，然后顺指针找到包含该值的行。这样可以使对应于表的SQL语句执行得更快，可快速访问数据库表中的特定信息。
   	当表中有大量记录时，若要对表进行查询，第一种搜索信息方式是全表搜索，是将所有记录一一取出，和查询条件进行一一对比，然后返回满足条件的记录，这样做会消耗大量数据库系统时间，并造成大量磁盘I/O操作；第二种就是在表中建立索引，然后在索引中找到符合查询条件的索引值，最后通过保存在索引中的ROWID（相当于页码）快速找到表中对应的记录。

  - <font color='#F5555D'>**索引是为了加速对表中数据行的检索而创建的一种分散的存储结构。索引是针对表而建立的，它是由数据页面以外的索引页面组成的，每个索引页面中的行都会含有逻辑指针，以便加速检索物理数据。**</font>



 	<font color='#1cb569'>在数据库关系图中，可以在选定表的“索引/键”属性页中创建、编辑或删除每个索引类型。当保存索引所附加到的表，或保存该表所在的关系图时，索引将保存在数据库中。</font>

#### <font color='#ff6537'>3. 索引的作用</font>

- ##### <font color='#3a79c3'>快速取数据；</font>

- ##### <font color='#3a79c3'>保证数据记录的唯一性；</font>

- ##### <font color='#3a79c3'>实现表与表之间的参照完整性；</font>

- ##### <font color='#3a79c3'>在使用ORDER BY，GROUP BY子句进行数据检索时，利用索引可以减少排序和分组的时间；</font>

#### <font color='#ff6537'>4. 索引的优缺点</font>

- ##### <font color='#3a79c3'>优点</font>
  - ##### Ⅰ 大大加快了数据检索的速度；

  - ##### Ⅱ 创建唯一性索引，保证数据库中每一行数据的唯一性；

  - ##### Ⅲ 加速表和表之间的连接；

  - ##### Ⅳ 在使用ORDER BY，GROUP BY子句进行数据检索时，可以显著减少查询中分组和排序的时间；

- ##### <font color='#3a79c3'>缺点</font>

  - ##### Ⅰ 索引本身是一种存储结构，需要占用物理存储空间；

  - ##### Ⅱ 但对表中的数据进行增加、删除和修改的时候，索引也要动态地维护，降低了数据的维护速度；

#### <font color='#ff6537'>5. 索引的索引类型</font>

> ##### <font color='#3a79c3'>根据数据库的功能，可以在数据库设计器中创建四种索引：单列索引、复合索引、唯一索引、主键索引和聚集索引。</font>

##### 	<font color='#3a79c3'>① 单列索引</font>

- <font color='#3dcb85'>Ⅰ.    单列索引，也称为普通索引，单列索引是最基本的索引，它没有任何限制，创建一个单列索引。</font>语法如下：

```mysql
CREATE INDEX index_name ON tbl_name(index_colum_name);
```

【注】其中index_name为索引的名称，可以自定义，tbl_name则指明要创建索引的表，而index_colum_name指明表中那一个列要创建索引。

- <font color='#3dcb85'>	Ⅱ.    当然我们也可以通过修改表结构的方式添加索引。</font>语法如下：

```mysql
ALTER TABLE tbl_name ADD INDEX index_name ON (index_colum_name);
```

- <font color='#3dcb85'>Ⅲ.    还可以在创建表时直接指定。</font> 语法如下：

```mysql
-- 创建表时直接指定
CREATE TABLE `table_name` (
`id` int(11) NOT NULL AUTO_INCREMENT ,
`name` varchar(32)  NOT NULL ,
......  -- 其他字段
PRIMARY KEY (`id`),
indexName (name(32))  -- 创建name字段索引
);
```

举例：下面为user表中username字段创建单列索引：

```mysql
-- 创建username字段的索引名称为 index_name，这就是基础的索引创建
CREATE index index_name ON user(username);
-- 查看user表中存在的索引 \G 代表优化显示方式
SHOW index FROM user \G;
```

- 	执行上面的语句得查询结果，可见user表中的username字段的索引已被创建，在使用<font color='red'>`SHOW index FROM user`</font>查看索引时，我们<font color='#1cb569'>发现 id 字段也被创建的索引，事实上，当user表被创建时，主键定义的字段 id 就会被自动按创建索引，这是一种特殊的索引，也称为***丛生索引***</font>，而刚才创建的index_name索引属于单列索引。

##### <font color='#3a79c3'>② 复合索引</font>

- 	<font color='#1cb569'>Ⅰ. 复合索引是在多个字段上创建的索引。复合索引遵守“最左前缀”原则，即在查询条件中使用了复合索引的第一个字段，索引才会被使用。因此，在复合索引中索引列的顺序至关重要。   </font>创建一个复合索引的语法如下：

```mysql
-- index_name 代表索引名称，而index_colum_named1, index_colum_named2, ... 为列名，可以是多个。 
CREATE INDEX index_name ON tbl_name(index_colum_name1, index_colum_name2, ...);
```

- <span style='color:#1cb569'>Ⅱ.   同样的道理，也可通过添加表结构的方式添加索引。</span>语法如下：

```mysql
ALTER TABLE tbl_name ADD INDEX index_name ON (index_colum_name1,index_colum_name2,...);
```

- <span style='color:#1cb569'>Ⅲ.     创建表时直接指定。</span>语法如下：

```mysql
CREATE TABLE `table` (
	`id` int(11) NOT NULL AUTO_INCREMENT ,
	`name` varchar(32)  NOT NULL ,
	'pinyin' varchar(32) ,
	......  -- 其他字段
PRIMARY KEY (`id`),
indexName (name(32),pinyin(32)) 
);
```

- 为了方便演示，为user表中添加名称为拼音字段的字段（pinyin）：

```mysql
-- 添加新字段pinyin
ALTER TABLE user ADD pinyin varchar(32) AFTER username;
```

- 	现在利用username和pinyin 两个字段为user表创建复合索引，先删除之前为username创建的索引，删	 除索引语法如下：

```mysql
DROP INDEX 索引名称 ON 表名
```

```mysql
-- 删除username的索引：
DROP index index_name ON user;

-- 查看user的索引
SHOW index FROM user \G;
```

- OK，index_name 索引已被删除，现在联合username和pinyin创建索引如下：

```mysql
-- 创建新索引多列组成，index_pinyin为复合索引名称
CREATE index index_pinyin ON user(username,pinyin);

-- 这里省略主键索引
SHOW index FROM user \G;
```

- 	像这样<font color='#1cb569'>由两个以上组成的索引，称为***`复合索引`*** </font>，由于是复合索引，因此索引的名称都相同，注意`Seq_in_index`代表索引字段的顺序，前面我们说过在查询条件中使用了复合索引的第一个字段（这里指username），索引才会被使用。因此，<font color='#1cb569'>***在复合索引中索引列的顺序至关重要。***</font>



##### 	<font color='#3a79c3'>③ 唯一索引</font>



##### 	<font color='#3a79c3'>④ 主键索引</font>



##### 	<font color='#3a79c3'>⑤ 聚集索引</font>



#### <font color='#ff6537'>6. 索引的创建，修改及删除</font>



#### <font color='#ff6537'>7. 索引的设计</font>



### <font color='#e6000b'>三、 存储过程</font>



### <font color='#e6000b'>四、 存储函数</font>



### <font color='#e6000b'>五、 触发器</font>



### <font color='#e6000b'>六、 游标</font>



### <font color='#e6000b'>七、 事务处理</font>





https://blog.csdn.net/javazejian/article/details/69857949

https://baike.baidu.com/item/%E7%B4%A2%E5%BC%95/5716853

