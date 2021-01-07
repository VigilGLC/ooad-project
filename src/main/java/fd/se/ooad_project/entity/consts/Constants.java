package fd.se.ooad_project.entity.consts;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Constants {

    public static final String STRING_AUDIT = "audit";
    public static final String STRING_MARKET = "market";
    public static final String STRING_EXPERT = "expert";
    public static final String STRING_ANY = "any";

    public static final int PUNCTUAL_GRADE = 10;
    public static final int TIMEOUT_GRADE = -10;
    public static final int OVERLATE_GRADE = -20;

    public static final int OVERLATE_DELTA = 20;


}
