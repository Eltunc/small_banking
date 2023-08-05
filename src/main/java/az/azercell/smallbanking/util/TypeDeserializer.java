package az.azercell.smallbanking.util;

import az.azercell.smallbanking.model.enums.Type;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;

public class TypeDeserializer extends JsonDeserializer<Type> {

    @Override
    public Type deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        String value = jsonParser.getText();

        try {
            return Type.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return Type.DEFAULT;
        }
    }
}

