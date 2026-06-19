package com.codificar.helpdesk.service;

import com.codificar.helpdesk.model.AuditLog;
import com.codificar.helpdesk.repository.AuditLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuditLogService {

    @Autowired
    private AuditLogRepository auditLogRepository;

    @Transactional
    public void logAction(String action, String description) {
        AuditLog log = new AuditLog(action, description);
        auditLogRepository.save(log);
    }
}
