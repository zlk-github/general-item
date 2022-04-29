## mysql执行计划EXPLAIN

### 1.EXPLAIN详解

##### 


##### 索引

    -- DROP INDEX idx_code ON tt_scan;
    ALTER TABLE `tt_scan` ADD INDEX  idx_code(`code`),ALGORITHM=inplace,LOCK=NONE;

