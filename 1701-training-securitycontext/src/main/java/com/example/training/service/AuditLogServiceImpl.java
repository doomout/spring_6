package com.example.training.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.example.training.entity.AuditLog;
import com.example.training.repository.AuditLogRepository;

@Service
public class AuditLogServiceImpl implements AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public AuditLogServiceImpl(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    //public void registerLog(String  functionName, String userId) {
    //사용자 정보를 파라미터로 받지 않도록 수정
    public void registerLog(String  functionName) {    
        //대신 SecurityContext 로 사용자 정보 받음
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();

        AuditLog auditLog = new AuditLog();
        auditLog.setId(UUID.randomUUID().toString());
        auditLog.setFunctionName(functionName);
        auditLog.setEventDateTime(LocalDateTime.now());
        auditLog.setUserId(userId);
        auditLogRepository.insert(auditLog);
    }
}
