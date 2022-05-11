## Linux-ubuntu

### 1 Linux-ubuntu常用命令

#### 1.1 切换root

    1. 设置密码
      sudo passwd root
    2. 需要输入两次密码
    3. 切换root 用户
       su root

#### 1.2 防火墙和端口开放

    查看防火墙状态、开启防火墙、开启端口、防火墙重启、查看端口监听情况
    $ sudo ufw status
    $ sudo ufw enable
    $ sudo ufw allow 22
    $ sudo ufw reload
    $ sudo netstat -tunlp | grep 22  
    查看开放的端口：netstat -aptn 或者 iptables -nL

### 参考
