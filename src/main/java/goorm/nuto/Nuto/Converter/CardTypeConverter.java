package goorm.nuto.Nuto.Converter;

import goorm.nuto.Nuto.Entity.CardType;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.Arrays;

@Converter(autoApply = true)
public class CardTypeConverter implements AttributeConverter<CardType, String> {

    @Override
    public String convertToDatabaseColumn(CardType attribute) {
        return attribute != null ? attribute.getDisplayName() : null;
    }

    @Override
    public CardType convertToEntityAttribute(String dbData) {
        return Arrays.stream(CardType.values())
                .filter(type -> type.getDisplayName().equals(dbData))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown card type: " + dbData));
    }
}