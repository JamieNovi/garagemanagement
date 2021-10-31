package nl.jamienovi.garagemanagement.eventmanager;

import lombok.Getter;
import org.springframework.core.ResolvableType;
import org.springframework.core.ResolvableTypeProvider;

import java.util.StringJoiner;

@Getter
public class GenericEvent<T> implements ResolvableTypeProvider {
    private T messageEvent;
    private Object source;

    public GenericEvent(Object source,T messageEvent ) {
        this.source = source;
        this.messageEvent = messageEvent;
    }

    @Override
    public ResolvableType getResolvableType() {
        return ResolvableType.forClassWithGenerics(
                getClass(), ResolvableType.forInstance(getSource())
        );
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", GenericEvent.class.getSimpleName() + "[", "]")
                .add("message=" + messageEvent)
                .add("source=" + source)
                .toString();
    }
}
