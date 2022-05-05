#!/bin/bash
find /data/paas/smsa/tomcat-smsa-agent-oem/logs -mtime +90 -name "*.txt.gz" -exec rm -rf {} \;
find /data/paas/smsa/tomcat-smsa-agent-oem/logs -mtime +90 -name "*.log" -exec rm -rf {} \;
find /data/paas/smsa/tomcat-smsa-operation/logs -mtime +90 -name "*.txt.gz" -exec rm -rf {} \;
find /data/paas/smsa/tomcat-smsa-operation/logs -mtime +90 -name "*.log" -exec rm -rf {} \;
find /data/paas/smsa/tomcat-smsa-epay/logs -mtime +90 -name "*.txt.gz" -exec rm -rf {} \;
find /data/paas/smsa/tomcat-smsa-task-finance/logs -mtime +90 -name "*.txt.gz" -exec rm -rf {} \;
find /data/paas/smsa/tomcat-smsa-task-finance/logs -mtime +90 -name "*.log" -exec rm -rf {} \;
find /data/paas/smsa/tomcat-smsp-schedule/logs -mtime +90 -name "*.txt.gz" -exec rm -rf {} \;
find /data/paas/smsa/tomcat-smsp-schedule/logs -mtime +90 -name "*.log" -exec rm -rf {} \;
find /data/paas/smsa/tomcat-smsp-task/logs -mtime +90 -name "*.txt.gz" -exec rm -rf {} \;
find /data/paas/smsa/tomcat-smsp-task/logs -mtime +90 -name "*.log" -exec rm -rf {} \;

find /data/paas/logdata/smsp_access -mtime +90 -name "*.log.gz" -exec rm -rf {} \;
find /data/paas/logdata/smsp_charge -mtime +90 -name "*.log.gz" -exec rm -rf {} \;
find /data/paas/logdata/smsp_send -mtime +90 -name "*.log.gz" -exec rm -rf {} \;
find /data/paas/logdata/tomcat-smsa-task-finance -mtime +90 -name "*.log.gz" -exec rm -rf {} \;
find /data/paas/logdata/smsp_http -mtime +90 -name "*.log.gz" -exec rm -rf {} \;
find /data/paas/logdata/tomcat-smsa-agent-oem -mtime +90 -name "*.log.gz" -exec rm -rf {} \;
find /data/paas/logdata/tomcat-smsp-img -mtime +90 -name "*.log.gz" -exec rm -rf {} \;
find /data/paas/logdata/smsp_audit -mtime +90 -name "*.log.gz" -exec rm -rf {} \;
find /data/paas/logdata/smsp_record_consumer -mtime +90 -name "*.log.gz" -exec rm -rf {} \;
find /data/paas/logdata/tomcat-smsa-epay -mtime +90 -name "*.log.gz" -exec rm -rf {} \;
find /data/paas/logdata/tomcat-smsp-schedule -mtime +90 -name "*.log.gz" -exec rm -rf {} \;
find /data/paas/logdata/smsp_c2s -mtime +90 -name "*.log.gz" -exec rm -rf {} \;
find /data/paas/logdata/smsp_report -mtime +90 -name "*.log.gz" -exec rm -rf {} \;
find /data/paas/logdata/tomcat-smsa-operation -mtime +90 -name "*.log.gz" -exec rm -rf {} \;
find /data/paas/logdata/tomcat-smsp-task -mtime +90 -name "*.log.gz" -exec rm -rf {} \;

find /data/paas/logdata/smsp_report -mtime +90 -name "*.log.gz" -exec rm -rf {} \;
find /data/paas/logdata/tomcat-smsa-operation -mtime +90 -name "*.log.gz" -exec rm -rf {} \;
find /data/paas/logdata/tomcat-smsp-task -mtime +90 -name "*.log.gz" -exec rm -rf {} \;


find /data/paas/smsp/smsp_access/logs -mtime +200 -name "*.log" -exec rm -rf {} \;
find /data/paas/smsp/smsp_access_consumer/logs -mtime +200 -name "*.log" -exec rm -rf {} \;
find /data/paas/smsp/smsp_audit/logs -mtime +200 -name "*.log" -exec rm -rf {} \;
find /data/paas/smsp/smsp_c2s/logs -mtime +200 -name "*.log" -exec rm -rf {} \;
find /data/paas/smsp/smsp_http/logs -mtime +200 -name "*.log" -exec rm -rf {} \;
find /data/paas/smsp/smsp_record_consumer/logs -mtime +200 -name "*.log" -exec rm -rf {} \;
find /data/paas/smsp/smsp_report/logs -mtime +200 -name "*.log" -exec rm -rf {} \;
find /data/paas/smsp/smsp_send/logs -mtime +200 -name "*.log" -exec rm -rf {} \;

