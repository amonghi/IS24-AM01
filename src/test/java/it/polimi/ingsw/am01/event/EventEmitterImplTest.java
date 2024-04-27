package it.polimi.ingsw.am01.event;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EventEmitterImplTest {

    class SomeEvent implements Event {
    }

    class SomeSubEvent extends SomeEvent {
    }

    @Test
    void canListenToAny() {
        EventEmitterImpl<SomeEvent> emitter = new EventEmitterImpl<>();
        //noinspection unchecked
        EventListener<SomeEvent> mockListener = (EventListener<SomeEvent>) Mockito.mock(EventListener.class);
        InOrder inOrder = inOrder(mockListener);

        SomeEvent e1 = new SomeEvent();
        SomeSubEvent e2 = new SomeSubEvent();
        SomeEvent e3 = new SomeEvent();

        emitter.onAny(mockListener);
        emitter.emit(e1);
        emitter.emit(e2);
        emitter.emit(e3);

        inOrder.verify(mockListener).onEvent(e1);
        inOrder.verify(mockListener).onEvent(e2);
        inOrder.verify(mockListener).onEvent(e3);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void canListenToSpecificEvent() {
        EventEmitterImpl<SomeEvent> emitter = new EventEmitterImpl<>();
        //noinspection unchecked
        EventListener<SomeSubEvent> mockListener = (EventListener<SomeSubEvent>) Mockito.mock(EventListener.class);
        InOrder inOrder = inOrder(mockListener);

        SomeEvent e1 = new SomeEvent();
        SomeSubEvent e2 = new SomeSubEvent();
        SomeEvent e3 = new SomeEvent();

        emitter.on(SomeSubEvent.class, mockListener);
        emitter.emit(e1);
        emitter.emit(e2);
        emitter.emit(e3);

        inOrder.verify(mockListener).onEvent(e2);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void canUnregister() {
        EventEmitterImpl<SomeEvent> emitter = new EventEmitterImpl<>();
        //noinspection unchecked
        EventListener<SomeEvent> mockListener = (EventListener<SomeEvent>) Mockito.mock(EventListener.class);
        InOrder inOrder = inOrder(mockListener);

        SomeEvent e1 = new SomeEvent();
        SomeEvent e2 = new SomeEvent();

        EventEmitter.Registration registration = emitter.onAny(mockListener);
        emitter.emit(e1);
        emitter.unregister(registration);
        emitter.emit(e2);

        inOrder.verify(mockListener).onEvent(e1);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void listenersAreCalledInOrder() {
        EventEmitterImpl<SomeEvent> emitter = new EventEmitterImpl<>();
        //noinspection unchecked
        EventListener<SomeEvent> mockListener1 = (EventListener<SomeEvent>) Mockito.mock(EventListener.class);
        //noinspection unchecked
        EventListener<SomeEvent> mockListener2 = (EventListener<SomeEvent>) Mockito.mock(EventListener.class);
        InOrder inOrder = inOrder(mockListener1, mockListener2);

        SomeEvent event = new SomeEvent();

        emitter.onAny(mockListener1);
        emitter.onAny(mockListener2);
        emitter.emit(event);

        inOrder.verify(mockListener1).onEvent(event);
        inOrder.verify(mockListener2).onEvent(event);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void canBubble() {
        EventEmitterImpl<SomeEvent> emitter1 = new EventEmitterImpl<>();
        EventEmitterImpl<SomeEvent> emitter2 = new EventEmitterImpl<>();
        //noinspection unchecked
        EventListener<SomeEvent> mockListener = (EventListener<SomeEvent>) Mockito.mock(EventListener.class);

        SomeEvent event = new SomeEvent();
        emitter2.bubble(emitter1);
        emitter2.onAny(mockListener);
        emitter1.emit(event);

        verify(mockListener).onEvent(event);
    }
}
