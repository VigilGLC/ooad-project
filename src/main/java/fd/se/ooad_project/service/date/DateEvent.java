package fd.se.ooad_project.service.date;


import org.springframework.context.ApplicationEvent;


public class DateEvent extends ApplicationEvent {
    public DateEvent(Object source) {
        super(source);
    }

    public static DateEvent of(Object source) {
        return new DateEvent(source);
    }

}
