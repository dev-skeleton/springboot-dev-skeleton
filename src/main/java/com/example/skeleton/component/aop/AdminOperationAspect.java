package com.example.skeleton.component.aop;


import com.example.skeleton.exception.CustomRuntimeException;
import com.example.skeleton.model.entity.OperationLog;
import com.example.skeleton.service.OperationLogService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.yaml.snakeyaml.introspector.BeanAccess;
import org.yaml.snakeyaml.introspector.PropertyUtils;

import java.util.*;
import java.util.stream.Collectors;

@Aspect
@Component
@RequestScope
public class AdminOperationAspect {


    static private final Logger logger = LoggerFactory.getLogger(AdminOperationAspect.class);
    private final OperationLogService operationLogService;
    private final OperationLog record = new OperationLog();
    private boolean needProcess = true;

    public AdminOperationAspect(OperationLogService operationLogService) {
        this.operationLogService = operationLogService;
    }

    private Triple<com.example.skeleton.annotation.OperationLog, Object, Object> getAuditLog(JoinPoint joinPoint) {
        var method = ((MethodSignature) joinPoint.getSignature()).getMethod();  //获取对应的方法名
        var an = method.getAnnotation(com.example.skeleton.annotation.OperationLog.class);
        if (an == null) {
            return Triple.of(null, null, null);
        }
        logger.info("audit annotation {}", an);
        if (method.getParameterCount() != joinPoint.getArgs().length) {
            return Triple.of(null, null, null);
        }
        var entityName = sepName(an.entityParam());
        if (an.detailParam().length <= 1) {
            var detailName = sepName(an.detailParam().length == 0 ? "" : an.detailParam()[0]);
            Object entity = null;
            Object detail = null;
            for (int i = 0; i < method.getParameters().length; i++) {
                if (method.getParameters()[i].getName().equals(entityName.getLeft())) {
                    entity = joinPoint.getArgs()[i];
                }
                if (method.getParameters()[i].getName().equals(detailName.getLeft())) {
                    detail = joinPoint.getArgs()[i];
                }
            }
            return Triple.of(an, getValue(entity, entityName.getRight()), getValue(detail, detailName.getRight()));
        } else {
            var detailName = sepNames(an.detailParam());
            Object entity = null;
            Map<String, Pair<Object, String>> detail = new HashMap<>();
            for (int i = 0; i < method.getParameters().length; i++) {
                if (method.getParameters()[i].getName().equals(entityName.getLeft())) {
                    entity = joinPoint.getArgs()[i];
                }
                int finalI = i;
                detailName.forEach(name -> {
                    if (name.getMiddle().equals(method.getParameters()[finalI].getName())) {
                        detail.put(name.getLeft(), Pair.of(joinPoint.getArgs()[finalI], name.getRight()));
                    }
                });
            }
            return Triple.of(an, getValue(entity, entityName.getRight()), detail.entrySet().stream().reduce(new HashMap<String, Object>(), (m, entry) -> {
                m.put(entry.getKey(), getValue(entry.getValue().getLeft(), entry.getValue().getRight()));
                return m;
            }, (a, b) -> null));
        }

    }

    private Pair<String, String> sepName(String name) {
        if (!StringUtils.contains(name, ".")) {
            return Pair.of(name, "");
        }
        return Pair.of(StringUtils.substringBefore(name, "."), StringUtils.substringAfter(name, "."));
    }

    private List<Triple<String, String, String>> sepNames(String[] names) {
        return Arrays.stream(names).map(name -> {
            var p = sepName(name);
            return Triple.of(name, p.getLeft(), p.getRight());
        }).collect(Collectors.toList());
    }

    private String toString(Object obj) {
        if (obj == null) {
            return "null";
        }
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            logger.error("marshal object {} failed, {}", obj, e);
        }
        return "null";
    }

    private Object getValue(Object obj, String field) {
        if (obj == null) {
            return null;
        }
        if (StringUtils.isBlank(field)) {
            return obj;
        }
        var val = new PropertyUtils().getProperty(obj.getClass(), StringUtils.split(field, '.')[0], BeanAccess.FIELD).get(obj);
        if (!field.contains(".")) {
            return val;
        }
        return getValue(val, StringUtils.substringAfter(field, "."));
    }


    @Before("within(com.example.skeleton.controller.*) && @annotation(com.example.skeleton.annotation.OperationLog)")
    public void addBeforeLogger(JoinPoint joinPoint) {
        var auditLog = getAuditLog(joinPoint);
        if (auditLog.getLeft() == null) {
            logger.error("@OperationLog auditLog annotation must exist");
            needProcess = false;
            return;
        }
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        record.setAccount((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        record.setIP(attributes != null ? attributes.getRequest().getRemoteAddr() : null);
        record.setTime(new Date());
        record.setAction(auditLog.getLeft().description());
        record.setEntity(toString(auditLog.getMiddle()));
        record.setDetail(toString(auditLog.getRight()));
    }

    @AfterReturning("within(com.example.skeleton.controller.*) && @annotation(com.example.skeleton.annotation.OperationLog)")
    public void addAfterReturningLogger(JoinPoint joinPoint) {
        if (!needProcess) {
            return;
        }
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert attributes != null;
        assert attributes.getResponse() != null;
        if (attributes.getResponse().getStatus() == 200) {
            record.setStatus("success");
        } else {
            record.setStatus("failed");
        }
        operationLogService.Put(record);
    }

    @AfterThrowing(pointcut = "within(com.example.skeleton.controller.*) && @annotation(com.example.skeleton.annotation.OperationLog)", throwing = "ex")
    public void addAfterThrowingLogger(JoinPoint joinPoint, Exception ex) {
        if (!needProcess) {
            return;
        }
        if (ex instanceof CustomRuntimeException) {
            record.setStatus("failed");
        } else {
            record.setStatus("unexpected error");
        }
        operationLogService.Put(record);
    }
}
