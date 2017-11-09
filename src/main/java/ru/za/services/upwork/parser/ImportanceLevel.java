package ru.za.services.upwork.parser;

public enum ImportanceLevel {
    HIGH{ public int valueAnInt(){return 4;} },
    MEDIUM{ public int valueAnInt(){return 3;} },
    LOW{ public int valueAnInt(){return 2;} },
    TRASH{ public int valueAnInt(){return 1;} };

    @Override
    public String toString() {
        return super.toString();
    }

    public abstract int valueAnInt();

    public static ImportanceLevel fromInt(int value){
        switch (value){
            case 1: return TRASH;
            case 2: return LOW;
            case 3: return MEDIUM;
            case 4: return HIGH;
            default:{
                if (value <= 1) {
                    return TRASH;
                }else if(value >= 4){
                    return HIGH;
                }
            };
        }
        return TRASH;
    }

    public static ImportanceLevel fromString(String value){
        switch (value){
            case "TRASH": return TRASH;
            case "LOW": return LOW;
            case "MEDIUM": return MEDIUM;
            case "HIGH": return HIGH;
            default:{
                return MEDIUM;
            }
        }
    }

}
