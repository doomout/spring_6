package com.example.training.service;

public interface AuditLogService {

    //사용자 정보가 필요 없어졌다.
    //void registerLog(String functionName, String userId);
    void registerLog(String functionName);
}
