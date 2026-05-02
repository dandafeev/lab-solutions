package ru.itmo.labsolutions.domain;

import ru.itmo.labsolutions.domain.enums.FinalQuantityUnit;
import java.time.Instant;


public final class Preparation {

    private long id;


    private long solutionId;


    private double finalQuantity;


    private FinalQuantityUnit finalUnit;


    private String comment;


    private String ownerUsername;


    private Instant preparedAt;


    private Instant createdAt;


    private Instant updatedAt;


    public Preparation(long solutionId, double finalQuantity,
                       FinalQuantityUnit finalUnit, String comment) {
        this.solutionId = solutionId;
        this.finalQuantity = finalQuantity;
        this.finalUnit = finalUnit;
        this.comment = comment;
        this.ownerUsername = "SYSTEM";
        this.preparedAt = Instant.now();
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }



    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getSolutionId() {
        return solutionId;
    }

    public void setSolutionId(long solutionId) {
        this.solutionId = solutionId;
        this.updatedAt = Instant.now();
    }

    public double getFinalQuantity() {
        return finalQuantity;
    }

    public void setFinalQuantity(double finalQuantity) {
        this.finalQuantity = finalQuantity;
        this.updatedAt = Instant.now();
    }

    public FinalQuantityUnit getFinalUnit() {
        return finalUnit;
    }

    public void setFinalUnit(FinalQuantityUnit finalUnit) {
        this.finalUnit = finalUnit;
        this.updatedAt = Instant.now();
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
        this.updatedAt = Instant.now();
    }

    public String getOwnerUsername() {
        return ownerUsername;
    }

    public void setOwnerUsername(String ownerUsername) {
        this.ownerUsername = ownerUsername;
    }

    public Instant getPreparedAt() {
        return preparedAt;
    }

    public void setPreparedAt(Instant preparedAt) {
        this.preparedAt = preparedAt;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }


    @Override
    public String toString() {
        return "Preparation#" + id +
                " solution_id: " + solutionId +
                " finalQty: " + finalQuantity + " " + finalUnit +
                " comment: " + (comment != null ? comment : "-");
    }
}