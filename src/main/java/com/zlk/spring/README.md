## spring-framework--v5.2.21.RELEASE编译

### 1 前置准备 

    JDK11
    MAVEN3.6.2
    gradle-5.6.4
    spring-framework(v5.2.21.RELEASE)源码
    IDEA2020.1(不赘述)
    git(不赘述)
    windows10

### 2 下载安装环境

#### 2.1 JDK11下载安装

    JDK11下载地址：https://www.oracle.com/java/technologies/downloads/#java11-windows
    谷歌账号：http://bugmenot.com/view/oracle.com
    JDK11安装教程见：https://blog.csdn.net/weixin_45821811/article/details/116406438
    多个版本JDK安装见：https://blog.csdn.net/ss1997000/article/details/121307386
    
#### 2.2 MAVEN3.6.2

    MAVEN3.6.2下载地址：https://archive.apache.org/dist/maven/maven-3/3.6.2/binaries/
    MAVEN3.6.2安装教程见：https://www.cnblogs.com/luckyzoe/p/13061135.html
    
附：apache-maven-3.6.2\conf\settings.xml

```xml
1.本地仓库路径
<localRepository>D:\dev\maven-repository111\repository</localRepository>

2.配置阿里云镜像
<mirrors> 
    <mirror>
        <id>alimaven</id>
        <mirrorOf>central</mirrorOf>
        <name>aliyun maven</name>
        <url>http://maven.aliyun.com/nexus/content/groups/public/</url>
    </mirror>
</mirrors>
```

#### 2.3 gradle-5.6.4

    gradle-5.6.4下载地址：https://gradle.org/next-steps/?version=5.6.4&format=all
    gradle-5.6.4安装教程见：https://blog.csdn.net/qq_41895810/article/details/120042035

gradle-5.6.4下创建init.gradle配置maven与远程仓库地址

```xml
allprojects {
    repositories {
        maven { url 'file://D:/dev/apache-maven-3.6.2'}
        mavenLocal()
        maven { name "Alibaba" ; url "https://maven.aliyun.com/repository/public" }
        maven { name "Bstek" ; url "http://nexus.bsdn.org/content/groups/public/"}
        mavenCentral()
    }
    buildscript { 
        repositories { 
            maven { name "Alibaba" ; url 'https://maven.aliyun.com/repository/public'}
            maven { name "Bstek" ; url 'http://nexus.bsdn.org/content/groups/public/' }
            maven { name "M2" ; url 'https://plugins.gradle.org/m2/'}
        }
    }
}
```

#### 2.4 下载spring-framework(v5.2.21.RELEASE)源码

    官网地址：https://spring.io/projects/spring-framework#learn
    跳转github进行下载（官网有图标跳转）：https://github.com/spring-projects/spring-framework/tree/v5.2.21.RELEASE
    
    注：
        github中spring源码编译见文档中：Build from Source。
        
### 3 spring-framework编译

    1.项目导入到IDEA。配置JDK(11),MAVEN,gradle。
    
    2. 在terminal窗口执行gradle命令
       gradlew
    
    3.compile及测试
        gradlew -a :spring-oxm:compileTestJava
        gradlew -a :spring-webmvc:test
    
    4.gradlew publishToMavenLocal -x api -x asciidoctor -x asciidoctorPdf

### 4 创建一个测试项目




### 参考

    官网 https://spring.io/projects/spring-framework