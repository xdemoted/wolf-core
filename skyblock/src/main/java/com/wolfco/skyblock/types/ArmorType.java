package com.wolfco.skyblock.types;

public enum ArmorType {
    LIGHT(0, 0, 0.5, 0.0),
    MEDIUM(1, 1, 1.0, 0.5),
    HEAVY(2, 2, 1.5, 1.0);
    
    public int id, modelNumber;
    public double armorValue;
    private ArmorType(int id, int modelNumber, double armorValue, double weight) {
        this.id = id;
        this.modelNumber = modelNumber;
        this.armorValue = armorValue;
    }
}
