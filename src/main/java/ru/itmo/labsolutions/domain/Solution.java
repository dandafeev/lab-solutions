package ru.itmo.labsolutions.domain;

import ru.itmo.labsolutions.domain.enums.SolutionConcentrationUnit;
import java.time.Instant;


public final class Solution {

    private long id;


    private String name;


    private double concentration;


    private SolutionConcentrationUnit concentrationUnit;


    private String solvent;


    private String ownerUsername;


    private Instant createdAt;


    private Instant updatedAt;


    public Solution(String name, double concentration,
                    SolutionConcentrationUnit concentrationUnit, String solvent) {
        this.name = name;
        this.concentration = concentration;
        this.concentrationUnit = concentrationUnit;
        this.solvent = solvent;
        this.ownerUsername = "SYSTEM"; // временно
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }



    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        this.updatedAt = Instant.now();
    }

    public double getConcentration() {
        return concentration;
    }

    public void setConcentration(double concentration) {
        this.concentration = concentration;
        this.updatedAt = Instant.now();
    }

    public SolutionConcentrationUnit getConcentrationUnit() {
        return concentrationUnit;
    }

    public void setConcentrationUnit(SolutionConcentrationUnit concentrationUnit) {
        this.concentrationUnit = concentrationUnit;
        this.updatedAt = Instant.now();
    }

    public String getSolvent() {
        return solvent;
    }

    public void setSolvent(String solvent) {
        this.solvent = solvent;
        this.updatedAt = Instant.now();
    }

    public String getOwnerUsername() {
        return ownerUsername;
    }

    public void setOwnerUsername(String ownerUsername) {
        this.ownerUsername = ownerUsername;
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
        return "Solution#" + id +
                " name: " + name +
                " concentration: " + concentration + " " + concentrationUnit +
                " solvent: " + (solvent != null ? solvent : "-");
    }
}
