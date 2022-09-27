## MongoDB
    
    mongodb不支持事务，所以，在你的项目中应用时，要注意这点。无论什么设计，都不要要求mongodb保证数据的完整性。
    
    但是mongodb提供了许多原子操作，比如文档的保存，修改，删除等，都是原子操作。
    
    所谓原子操作就是要么这个文档保存到Mongodb，要么没有保存到Mongodb，不会出现查询到的文档没有保存完整的情况。

### 1.MongoDB常用命令

|SQL术语/概念|MongoDB术语/概念|解释/说明|
|---- |---- |----  |
|database|	database|	数据库|
|table|	collection|	数据库表/集合|
|row|	document|	数据记录行/文档|
|column|	field|	数据字段/域|
|index|	index|	索引|
|table joins|	 	|表连接,MongoDB不支持|
|primary key|	primary key|	主键,MongoDB自动将_id字段设置为主键|


常用 MongoDB 角色

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

### 3.文档操作(类似数据库行)

    use 库名称

###### 3.1 新增文档

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

    db.user.insertOne({"code":"1001","name":"zlk2"})

    db.user.insertMany([{"code":"1003","name":"zlk3"},{"code":"1004","name":"zlk4"}])

    查询结果
    db.getCollection('user').find()
        { "_id" : ObjectId("62a7e20ff18ba91aae0ac536"), "name" : "zlk" }
        { "_id" : ObjectId("62a7e286f18ba91aae0ac537"), "name" : "zlk" }
        { "_id" : ObjectId("62a8756ba6618be3cc813c25"), "code" : "1001", "name" : "zlk2" }
        { "_id" : ObjectId("62a87bffa6618be3cc813c26"), "code" : "1003", "name" : "zlk3" }
        { "_id" : ObjectId("62a87bffa6618be3cc813c27"), "code" : "1004", "name" : "zlk4"

###### 3.2 修改文档

    部分替换：update() 方法用于更新已存在的文档。语法格式如下：
        db.集合名称.update(
            <query>,
            <update>,
            {
                upsert: <boolean>,
                multi: <boolean>,
                writeConcern: <document>
            }
        )
        参数说明：
        query : update的查询条件，类似sql update查询内where后面的。
        update : update的对象和一些更新的操作符（如$,$inc...）等，也可以理解为sql update查询内set后面的
        upsert : 可选，这个参数的意思是，如果不存在update的记录，是否插入objNew,true为插入，默认是false，不插入。
        multi : 可选，mongodb 默认是false,只更新找到的第一条记录，如果这个参数为true,就把按条件查出来多条记录全部更新。
        writeConcern :可选，抛出异常的级别。

    全量替换：save() 方法（会通过_id整个替换）
        save() 方法通过传入的文档来替换已有文档，_id 主键存在就更新，不存在就插入。语法格式如下：
        db.集合名称.save(
            <document>,
            {
                writeConcern: <document>
            }
        )
        参数说明：
        
        document : 文档数据。
        writeConcern :可选，抛出异常的级别。

例子：

    //查询
    db.getCollection('user').find({"code":"1005"})
       { "_id" : ObjectId("62a87e7fa6618be3cc813c29"), "code" : "1005", "name" : "zlk5", "age" : "25", "sex" : "男" }
    
    //更新code=5的数据，code改为1005001，name改为1005001
    db.user.update({"code":"1005"},{$set:{"code":"1005001","name":"1005001"}})

    //查询db.getCollection('user').find() 或者 db.getCollection('user').find({"code":"1005001"})
    { "_id" : ObjectId("62a87e7fa6618be3cc813c29"), "code" : "1005001", "name" : "1005001", "age" : "25", "sex" : "男" }

    // 替换"_id" : ObjectId("62a87e7fa6618be3cc813c29")内容如下（全量替换）
    db.user.save({"_id" : ObjectId("62a87e7fa6618be3cc813c29"),"code":"1005001","name":"name1001"})

    // 查询db.getCollection('user').find() 或者 db.getCollection('user').find({"code":"1005001"})
    { "_id" : ObjectId("62a87e7fa6618be3cc813c29"), "code" : "1005001", "name" : "name1001" }


    db.user.update({"createTime":{$gte: "2022-09-22 01:49:59"}},{$set:{"code":"1005001","name":"1005001"}})

    db.getCollection('t_email_send_record').find({"createTime":{$lte: new ISODate("2022-09-22 01:49:59.000Z")},"countryCode":"EG","system":"TDS"})
    
    
    db.t_email_send_record.find({"createTime":{$lte: new ISODate("2022-09-22 01:49:59.000Z")},"countryCode":"EG","system":"TDS"}).forEach(function(item){
    //对查询到的每一条的item中的update_time进行增加2个小时的操
    item.createTime = new Date(item.createTime.getTime() - 2*60*60*1000)
    //将修改过的item重新保存进集合中
    db.t_email_send_record.save(item)
    })

###### 3.3 查询文档

    // 查询集合下全部
    db.getCollection('集合名称').find()

    // 查询集合下全部(格式化展示)
    db.getCollection('集合名称').find().pretty()

     // sort() 方法可以通过参数指定排序的字段，
     //并使用 1 和 -1 来指定排序的方式，其中 1 为升序排列，而 -1 是用于降序排列。
    db.getCollection('集合名称').find().sort({"排序字段":1})

    // 查询集合下“字段1”内容为“内容”
    db.getCollection('集合名称').find({"字段1":"内容"})

    // 查询集合下“字段1”内容为“内容”
       且"字段2"为"内容2"或者"内容3"
    db.getCollection('集合名称').find({"字段1":"内容","字段2":{$in:["内容2",
        "内容3"
    ]}})



|操作|	格式|	范例|	RDBMS中的类似语句|
|  ----  | ----  |----  |----  |
|等于	|{<key>:<value>}	|db.col.find({"by":"菜鸟教程"}).pretty()	|where by = '菜鸟教程'|
|小于	|{<key>:{$lt:<value>}}	|db.col.find({"likes":{$lt:50}}).pretty()	|where likes < 50|
|小于或等于	|{<key>:{$lte:<value>}}	|db.col.find({"likes":{$lte:50}}).pretty()	|where likes <= 50|
|大于	|{<key>:{$gt:<value>}}	|db.col.find({"likes":{$gt:50}}).pretty()	|where likes > 50|
|大于或等于	|{<key>:{$gte:<value>}}	|db.col.find({"likes":{$gte:50}}).pretty()	|where likes >= 50|
|不等于	|{<key>:{$ne:<value>}}	|db.col.find({"likes":{$ne:50}}).pretty()	|where likes != 50|

例子：
  
    // 查询
    db.getCollection('user').find()

    // 类似常规 SQL 语句为： 'where age>50 AND (code = '1001' OR name = 'MongoDB')'
    db.user.find({"age": {$gt:50}, $or: [{"code": "1001"},{"name": "MongoDB"}]}).pretty()

###### 3.4 删除文档
    
    删除文档：
    db.集合名称.remove(
        <query>,
        {
            justOne: <boolean>,
            writeConcern: <document>
        }
    )
    参数说明：
    
    query :（可选）删除的文档的条件。
    justOne : （可选）如果设为 true 或 1，则只删除一个文档，如果不设置该参数，或使用默认值 false，则删除所有匹配条件的文档。
    writeConcern :（可选）抛出异常的级别。

例子：

    db.user.remove({"code":"1005001"})

###### 3.5 分页查询文档
    
    // 指定数量NUMBER读取
    db.COLLECTION_NAME.find().limit(NUMBER)

    // 跳过NUMBER2条后开始读取NUMBER条数据
    db.COLLECTION_NAME.find().limit(NUMBER).skip(NUMBER2)

列子：

    //以下实例只会显示第二条文档数据
    db.user.find({},{"title":1,_id:0}).limit(1).skip(1)
        注:skip()方法默认参数为 0 。

### 4 其它常见用法

### 5 索引

##### 5.1 创建索引

    >db.集合名称.createIndex(keys, options)
    语法中 Key 值为你要创建的索引字段，1 为指定按升序创建索引，如果你想按降序来创建索引指定为 -1 即可。

例子：

    // 非后台创建
    db.user.createIndex({"code":1,"name":-1})

    //在后台创建索引：
    db.user.createIndex({"code":1,"age":-1}, {"background": true})

    // 唯一索引
    db.persons.createIndex({"code":1},{unique:true})
    db.collection.createIndex( { "code":1,"age":1 }, { unique: true }, {"background": true} )
    // 设置过期时间
    db.collection.createIndex( { "code":1,"age":1 }, { unique: true }, { expireAfterSeconds: 3600 }, {"background": true} )


##### 5.2 查看集合索引

    db.集合名称.getIndexes()

##### 5.3 查看集合索引大小

    db.集合名称.totalIndexSize()

##### 5.4 删除集合所有索引

    db.集合名称.dropIndexes()

##### 5.5 删除集合指定索引

    db.集合名称.dropIndex("索引名称")


### 6 优化

### 7 主从与分片（集群）

##### 7.1 主从

    mongodb的复制至少需要两个节点。其中一个是主节点，负责处理客户端请求，其余的都是从节点，负责复制主节点上的数据。
    mongodb各个节点常见的搭配方式为：一主一从、一主多从。

##### 7.2 分片（集群）

    集群分片存储.......

### 8 参考