package ru.itmo.labsolutions.domain;

import ru.itmo.labsolutions.domain.enums.ComponentUnit;
import java.time.Instant;


public final class PreparationComponent {

    private long id;


    private long preparationId;


    private long batchId;


    private double quantity;


    private ComponentUnit unit;


    private Instant createdAt;


    public PreparationComponent(long preparationId, long batchId,
                                double quantity, ComponentUnit unit) {
        this.preparationId = preparationId;
        this.batchId = batchId;
        this.quantity = quantity;
        this.unit = unit;
        this.createdAt = Instant.now();
    }



    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getPreparationId() {
        return preparationId;
    }

    public void setPreparationId(long preparationId) {
        this.preparationId = preparationId;
    }

    public long getBatchId() {
        return batchId;
    }

    public void setBatchId(long batchId) {
        this.batchId = batchId;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public ComponentUnit getUnit() {
        return unit;
    }

    public void setUnit(ComponentUnit unit) {
        this.unit = unit;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }


    @Override
    public String toString() {
        return "Component#" + id +
                " preparation_id: " + preparationId +
                " batch_id: " + batchId +
                " qty: " + quantity + " " + unit;
    }
}
