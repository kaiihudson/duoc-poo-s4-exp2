package org.salmonttapp.common.utils;

import org.salmonttapp.common.exceptions.InvalidTypeException;
import org.salmonttapp.model.SalmonType;

public class Utils {

    /**
     * Validation to translate type to enum.
     * @param rawType string indicating the type
     * @return type parsed by enum
     * @throws InvalidTypeException if type is not added to this validation
     */
    public static SalmonType validateType(String rawType) throws InvalidTypeException {
        return switch (rawType) {
            case "atlantico" -> SalmonType.ATLANTICO;
            case "coho" -> SalmonType.COHO;
            case "arcoiris" -> SalmonType.ARCOIRIS;
            default -> throw new InvalidTypeException("Tipo no válido");
        };
    }

    /**
     * Validation to assure a number is a number and acts like a number
     * @param rawNumber the string containing a suspected number
     * @return the number in int format
     * @throws NumberFormatException if the number does not parse
     */
    public static int validateInteger(String rawNumber) throws NumberFormatException {
        return Integer.parseInt(rawNumber);
    }
}
