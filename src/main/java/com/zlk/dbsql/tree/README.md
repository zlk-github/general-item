## mysql-DB

### 1.数据库常用命令

### 2.表操作

### 3.锁相关与MVCC

### 4.索引与失效

### 5.索引底层结构

### 6.分库分表

### 7.集群与主从

### 8.事务特性与隔离级别

#### 8.1 数据库特性

    原子性 （atomicity）:强调事务的不可分割.
    一致性 （consistency）:事务的执行的前后数据的完整性保持一致.
    隔离性 （isolation）:一个事务执行的过程中,不应该受到其他事务的干扰
    持久性（durability） :事务一旦结束,数据就持久到数据库

#### 8.2 事务隔离级别

| 事务隔离级别  | 脏读 |不可重复读 | 幻读 |
| ------- | ------- | ------- | ------- |
|    读未提交（read-uncommitted）   |  是     |   是   |  是     |
|    不可重复读（read-committed）   |  否     |   是   |  是     |
|    可重复读（repeatable-read）   |  否      |   否    |  是    |
|    串行化（serializable）   |  否      |  否  |  否      |

注：Spring事务隔离级别比数据库事务隔离级别多一个default，具体由数据库决定，一般数据库默认的事务隔离级别为可重复读。
mysql默认为repeatable-read。

1 READ_UNCOMMITTED （读未提交）

    这是事务最低的隔离级别，它允许另外一个事务可以看到这个事务未提交的数据。这种隔离级别会产生脏读，不可重复读和幻像读。

2 READ_COMMITTED （读已提交）

    保证一个事务修改的数据提交后才能被另外一个事务读取，另外一个事务不能读取该事务未提交的数据。这种事务隔离级别可以避免脏读出现，但是可能会出现不可重复读和幻像读。

3 REPEATABLE_READ （可重复读）

     https://blog.csdn.net/u014801432/article/details/81219205
    这种事务隔离级别可以防止脏读、不可重复读，但是可能出现幻像读。它除了保证一个事务不能读取另一个事务未提交的数据外，还保证了不可重复读。
    --可重复读：同一个事务前后读到数据要一样。

4 SERIALIZABLE（串行化）

    这是花费最高代价但是最可靠的事务隔离级别，事务被处理为顺序执行。除了防止脏读、不可重复读外，还避免了幻像读。
    --幻读：指的是一个事务在前后两次查询同一个范围的时候，后一次查询看到了前一次查询没有看到的数据行

### 9.数据库范式

### 10.执行计划

### 11.数据库优化

### 12.常用sql

#### 5.1 树结构

    SELECT * FROM tb_site
    WHERE code IN(
    SELECT code FROM
    (
    SELECT code,parent_code FROM tb_site WHERE parent_code> 0 ORDER BY parent_code, code DESC
    ) t1,
    (SELECT @pv := 10010101) t2
    WHERE (FIND_IN_SET(parent_code,@pv)>0 AND @pv := CONCAT(@pv, ',', code)
                         ) 
                    )
    UNION
    SELECT * FROM tb_site WHERE code = 10010101

****以下5.1存在bug****

    CREATE TABLE `test_user` (
        `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
        `name` varchar(255) NOT NULL DEFAULT '' COMMENT '名称',
        `parent_id` bigint(20) NOT NULL DEFAULT '' COMMENT '父id',
        PRIMARY KEY (`id`)
    ) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8 COMMENT='用户表';

##### 5.1.1 查所有的叶子节点. ( 不含自己 )，下面红色的地方，需要替换自己的属性和表名以及id值

    SELECT u2.id, u2.name
    FROM(
        SELECT
        @ids AS p_ids,
        (SELECT @ids := GROUP_CONCAT(id) FROM test_user WHERE FIND_IN_SET(parentId, @ids)) AS c_ids,
        @l := @l+1 AS LEVEL
        FROM test_user, (SELECT @ids := '101', @l := 0 ) b  #此处为需要传递的父类id.
        WHERE @ids IS NOT NULL
    ) u1
    JOIN test_user u2
    ON FIND_IN_SET(u2.id, u1.p_ids)  AND u2.id != '101'  #需要包含自己, 则删掉 !=

##### 5.1.2 查所有的父节点. ( 含自己 )，下面红色的地方，需要替换自己的属性和表名以及id值

    SELECT u2.id, u2.name
    FROM(
        SELECT
        @id AS c_ids,
        ( SELECT @id := parentId FROM test_user WHERE id = @id ) AS p_ids,
        @l := @l+1 AS LEVEL
        FROM test_user, (SELECT @id := '105', @l := 0 ) b
        WHERE @id > 0
        ) u1
    JOIN test_user u2  ON u1.c_ids = u2.id

##### 5.1.3 查所有的父节点. ( 不含自己 )，下面红色的地方，需要替换自己的属性和表名以及id值

    SELECT
        u2.id,
        u2.NAME 
    FROM
        (
        SELECT
        @id AS c_ids,
        ( SELECT @id := parentId FROM test_user WHERE id = @id ) AS p_ids,
        @l := @l + 1 AS LEVEL
        FROM
        test_user,
        ( SELECT @id := '105', @l := 0 ) b
        WHERE
        @id > 0
        ) u1
    JOIN test_user u2 ON u1.c_ids = u2.id
    AND u2.id != '105'

