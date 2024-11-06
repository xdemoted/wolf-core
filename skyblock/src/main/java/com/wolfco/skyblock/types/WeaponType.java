package com.wolfco.skyblock.types;

public enum WeaponType {
    // MELEE
    SWORD(0, 0, 1, 1.0, 0.1, 2.0),
    CLAYMORE(1, 1, 2.0, 0.5, 0.2, 2.0),
    DAGGER(2, 2, 0.7, 1.5, 0.1, 1.5),
    SPEAR(3, 3, 1, 1.0, 0.1, 2.0),
    // RANGED
    BOW(4, 0, 1, 1.0, 0.1, 2.0),
    CROSSBOW(5, 0, 1, 1.0, 0.1, 2.0),
    STAFF(6, 0, 1, 1.0, 0.1, 2.0);

    public int id, modelNumber;
    public double damageMultiplier, atkSpeedMultiplier, critChance, critDamageMultiplier;
    
    private WeaponType(int id, int modelNumber, double damageMultiplier, double atkSpeedMultiplier, double critChance, double critDamageMultiplier) {
        this.id = id;
        this.modelNumber = modelNumber;
        this.damageMultiplier = damageMultiplier;
        this.atkSpeedMultiplier = atkSpeedMultiplier;
        this.critChance = critChance;
        this.critDamageMultiplier = critDamageMultiplier;
    }
}
