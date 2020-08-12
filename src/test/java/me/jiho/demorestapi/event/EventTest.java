package me.jiho.demorestapi.event;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class EventTest {

    @Test
    public void builder() {
        Event event = Event.builder()
                .name("Spring Rest API")
                .description("REST API development with Spring")
                .build();
        assertThat(event).isNotNull();
    }

    @Test
    public void javaBean() {
        Event event = new Event();
        event.setName("Events");
        String name = "Events";
        assertThat(event.getName()).isEqualTo(name);
    }

    @Test
    @DisplayName("free 값이 설정되는지 테스")
    public void testFree() {
        //Given
        Event event = Event.builder()
                .basePrice(0)
                .maxPrice(0)
                .build();
        //When
        event.update();

        // Then
        assertThat(event.isFree()).isTrue();

        //Given
        event = Event.builder()
                .basePrice(100)
                .maxPrice(0)
                .build();
        //When
        event.update();

        // Then
        assertThat(event.isFree()).isFalse();


        //Given
        event = Event.builder()
                .basePrice(0)
                .maxPrice(100)
                .build();
        //When
        event.update();

        // Then
        assertThat(event.isFree()).isFalse();
    }

    @Test
    @DisplayName("offline 값이 설정되는지 테스트")
    public void testOffline() {
        //Given
        Event event = Event.builder()
                .maxPrice(100)
                .basePrice(100)
                .location("강남")
                .build();
        //When
        event.update();

        // Then
        assertThat(event.isOffline()).isTrue();

        //Given
        event = Event.builder()
                .maxPrice(100)
                .basePrice(100)
                .build();
        //When
        event.update();

        // Then
        assertThat(event.isOffline()).isFalse();
    }


}