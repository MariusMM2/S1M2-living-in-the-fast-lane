package game.utils;

import game.ui.Graphics;

//java sucks balls and doesn't feature primitive references (1 long array hack doesn't count)
public class GlobalVar {
    public final String type;//identifier
    private String value;//value stored
    private final String primitiveType;
    private final int maxValue;
    private final String[] literalValues;

    //constructor
    private GlobalVar(String type, Object value, String primitiveType, int maxValue, String... literalValues) {
        this.type = type;
        this.value = String.valueOf(value);
        this.primitiveType = primitiveType;
        this.maxValue = maxValue;
        this.literalValues = literalValues;
    }

    public GlobalVar(String type, boolean value) {
        this(type, value, "boolean", 0, "True", "False");
    }

    public GlobalVar(String type, int value, int maxValue, String... literalValues) {
        this(type, value, "integer", maxValue, literalValues);
    }

    public String getStringValue() {
        return value;
    }

    public int getIntValue() {
        if (primitiveType.equals("integer"))
            return Integer.parseInt(value);
        throw new UnsupportedOperationException();
    }

    public boolean getBooleanValue() {
        if (primitiveType.equals("boolean"))
            return Boolean.parseBoolean(value);
        throw new UnsupportedOperationException();
    }

    public void setValue(int value) {
        if (value <= maxValue)
            this.value = String.valueOf(value);
        else
            throw new IllegalArgumentException();
    }

    public void setValue(boolean value) {
        this.value = String.valueOf(value);
    }

    public String getLiteralValue() {
        switch (primitiveType) {
            case "integer":
                return literalValues[Integer.parseInt(value)];
            case "boolean":
                return value;
        }
        return "INVALID";
    }

    public void incrementValue() {
        switch (primitiveType) {
            case "boolean":
                value = String.valueOf(!Boolean.parseBoolean(value));
                break;
            case "integer":
                try {
                    setValue(Integer.parseInt(this.value) + 1);
                } catch (IllegalArgumentException e) {
                    setValue(0);
                }
        }

        if (type.equals(Graphics.GFX_STYLE.type))
            Graphics.refresh();
    }

    public String[] getLiteralValues() {
        return literalValues;
    }

    public String getPrimitiveType() {
        return primitiveType;
    }
}
