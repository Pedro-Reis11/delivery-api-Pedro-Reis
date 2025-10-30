package com.delivery_api.Projeto.Delivery.API.enums;

public enum PedidoStatus {
    PENDENTE,
    CONFIRMADO,
    PREPARANDO,
    SAIU_PARA_ENTREGA,
    ENTREGUE,
    CANCELADO;

    public boolean isFinal() {
        return this == ENTREGUE || this == CANCELADO;
    }
}
