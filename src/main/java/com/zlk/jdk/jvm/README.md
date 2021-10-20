## JVM

### JVM 工具使用

1  看看哪个对象太大 如果是cpu过高就 找到对应线程先jstack 出来文件

2  ./jmap  -histo 9274 | sort -n -r -k 2 | head -20