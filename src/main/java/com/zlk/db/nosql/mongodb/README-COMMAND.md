## MONGODB

### 1.mongodb常用命令

|SQL术语/概念|MongoDB术语/概念|解释/说明|
|---- |---- |----  |
|database|	database|	数据库|
|table|	collection|	数据库表/集合|
|row|	document|	数据记录行/文档|
|column|	field|	数据字段/域|
|index|	index|	索引|
|table joins|	 	|表连接,MongoDB不支持|
|primary key|	primary key|	主键,MongoDB自动将_id字段设置为主键|


常用 mongoDB 角色

    数据库用户角色： read、 readWrite
    数据库管理角色：dbAdmin、dbOwner、userAdmin
    集群管理角色：clusterAdmin、clusterManager、clusterMonitor、hostManager
    备份恢复角色：backup、restore
    所有数据库角色：readAnyDatabase、readWriteAnyDatabase、userAdminAnyDatabase、dbAdminAnyDatabase
    超级用户角色：root
    内部角色： __system

###### 1.1 启动和关闭数据库

    mongod --config /usr/local/MongoDB/mongodbServer/bin/mongod.conf

###### 1.2 登录数据库

    指定数据库：mongo --port 27017 -u 用户名 -p 密码 --authenticationDatabase 库名称
    不指定数据库：mongo --port 27017 -u 用户名 -p 密码
    例子：
        mongo --port 27017 -u root -p 123456 --authenticationDatabase 库名称

###### 1.3 显示数据库列表
     
    show dbs

###### 1.4 增加新用户赋予数据库

    // 进入 admin 数据库
    use admin		
    // 为名为 simple-design 的数据库添加数据库管理员，账号为 name(root)，密码为 password(123456)， 角色权限为 dbOwner
    db.createUser({ user: 'root', pwd: '123456', roles: [{ role: 'dbOwner', db: 'simple-design' }] })

###### 1.5 选择数据库

     use 数据库名;

###### 1.6 显示库中的集合（表）：

    // 进入数据库  
    use 数据库
    // 显示数据库下集合名称
    show tables 或者 show collections 

###### 1.7 建库与删库：

    //建库
    use my_database
    //删库
    切库：use my_database
    删除：db.dropDatabase()

    建库例子
        use my_database
        // 需要有数据才可见
        db.my_database.insert({"name":"测试1001"})
        WriteResult({ "nInserted" : 1 })

###### 1.8 备份数据库

   
###### 1.9 退出：

    ctrl+C


### 2.集合（collection类似表）操作

    前提为：use 数据库

###### 2.1 建集合与删集合：
    
    // 创建集合
        // 方式1（手动创建）
        db.createCollection(集合名称, 可选参数)
        // 方式2（mongodb自动创建）
        db.集合名称.insert({"name" : "菜鸟教程"})
    
    // 删除集合
    db.集合名称.drop()

### 3.文档操作

    use 库名称

###### 3.1 新增

    db.集合名称.insert(document)
    或
    db.集合名称.save(document)

    3.2 版本之后新增了 db.collection.insertOne() 和 db.collection.insertMany()。
        1.
        db.集合名称.insertOne() 用于向集合插入一个新文档，语法格式如下：
        db.集合名称.insertOne(
            <document>,
            {
            writeConcern: <document>
            }
        )

        2.
        db.集合名称.insertMany() 用于向集合插入一个多个文档，语法格式如下：
        db.集合名称.insertMany(
            [ <document 1> , <document 2>, ... ],
            {
            writeConcern: <document>,
            ordered: <boolean>
            }
        )
        注：
        document：要写入的文档。
        writeConcern：写入策略，默认为 1，即要求确认写操作，0 是不要求。
        ordered：指定是否按顺序写入，默认 true，按顺序写入。

例子：

    db.user.insert({"name":"zlk"})

    db.user.save({"name":"zlk1"})

    db.user.insertOne({"code":"1001",name":"zlk2"})

###### 3.2 修改

###### 3.3 查询

    db.getCollection('集合名称').find()

    db.getCollection('集合名称').find({"字段1":"内容","字段2":{$in:["内容2",
        "内容3"
    ]}})

例子：

    db.getCollection('user').find()

###### 3.4 删除

### 4 其它常见用法

### 5 索引

### 6 优化

### 7 集群与分片

### 8 参考