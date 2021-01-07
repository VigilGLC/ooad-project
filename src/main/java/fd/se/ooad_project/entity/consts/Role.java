package fd.se.ooad_project.entity.consts;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.HashMap;

import static com.fasterxml.jackson.annotation.JsonCreator.Mode.DELEGATING;
import static fd.se.ooad_project.entity.consts.Constants.*;

public enum Role {

    AUDIT(STRING_AUDIT),
    MARKET(STRING_MARKET),
    EXPERT(STRING_EXPERT),
    ANY(STRING_ANY);


    public final String value;

    Role(String value) {
        this.value = value;
    }

    private final static HashMap<String, Role> valueMap;

    static {
        valueMap = new HashMap<>(Role.values().length);
        for (Role value : Role.values()) {
            valueMap.put(value.value, value);
        }
    }

    @JsonCreator(mode = DELEGATING)
    public static Role of(String value) {
        return valueMap.getOrDefault(value, null);
    }


    @JsonValue
    @Override
    public String toString() {
        return super.toString();
    }
}
