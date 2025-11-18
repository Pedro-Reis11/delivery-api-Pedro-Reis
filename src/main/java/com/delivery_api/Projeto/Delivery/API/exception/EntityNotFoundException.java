package com.delivery_api.Projeto.Delivery.API.exception;

public class EntityNotFoundException extends BusinessException  {
    private String entityName;
    private Object entityId;

    public EntityNotFoundException(String entityName, Object entityId) {
        super(String.format("%s com ID %s não foi encontrado(a)", entityName, entityId));
        this.entityName = entityName;
        this.entityId = entityId;
        this.setErrorCode("ENTITY_NOT_FOUND");
    }

    public EntityNotFoundException(String message) {
        super(message);
        this.setErrorCode("ENTITY_NOT_FOUND");
    }

    public String getEntityName() {
        return entityName;
    }

    public Object getEntityId() {
        return entityId;
    }
}
