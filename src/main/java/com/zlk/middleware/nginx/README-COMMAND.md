### nginx常用命令

默认端口80，配置文件nginx.conf，成功日志access.log，失败日志error.log。

#### 1 启动nginx

	sudo nginx 
	
	或者
	sudo nginx -c 配置文件路径（nginx.conf）

#### 2 重启nginx

	sudo nginx -s reload

#### 3 停止nginx

	sudo nginx -s stop

#### 4 侦听nginx端口

	ps aux | grep nginx
	
	或者
	netstat -ntpl | grep 80

	sudo netstat -antup|grep PID号

#### 5 查看nginx版本

	sudo nginx -v

#### 6 测试nginx的配置文件是否有格式错误

	sudo nginx -t