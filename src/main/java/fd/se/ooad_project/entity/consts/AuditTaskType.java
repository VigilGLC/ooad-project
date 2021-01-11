package fd.se.ooad_project.entity.consts;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.HashMap;

import static com.fasterxml.jackson.annotation.JsonCreator.Mode.DELEGATING;
import static fd.se.ooad_project.entity.consts.Constants.STRING_EXPERT;
import static fd.se.ooad_project.entity.consts.Constants.STRING_MARKET;

public enum AuditTaskType {

    MARKET(STRING_MARKET),
    EXPERT(STRING_EXPERT);

    public final String value;

    AuditTaskType(String value) {
        this.value = value;
    }

    private final static HashMap<String, AuditTaskType> valueMap;

    static {
        valueMap = new HashMap<>(AuditTaskType.values().length);
        for (AuditTaskType value : AuditTaskType.values()) {
            valueMap.put(value.value, value);
        }
    }

    @JsonCreator(mode = DELEGATING)
    public static AuditTaskType of(String value) {
        return valueMap.getOrDefault(value, null);
    }


    @JsonValue
    @Override
    public String toString() {
        return this.value;
    }
}
