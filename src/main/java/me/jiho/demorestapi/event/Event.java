package me.jiho.demorestapi.event;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Builder @AllArgsConstructor @NoArgsConstructor
@Getter @Setter @EqualsAndHashCode(of = "id")
@Entity
public class Event {
    @Id
    @GeneratedValue
    private Long id;
    @NotEmpty
    private String name;
    @NotEmpty
    private String description;
    @NotEmpty
    private LocalDateTime beginEnrollmentDateTime;
    @NotEmpty
    private LocalDateTime closeEnrollmentDateTime;
    @NotEmpty
    private LocalDateTime beginEventDateTime;
    @NotEmpty
    private LocalDateTime endEventDateTime;
    private String location;
    private Integer basePrice;
    private Integer maxPrice;
    @NotEmpty
    private Integer limitOfEnrollment;
    @NotNull
    private Boolean offline;
    @NotNull
    private Boolean free;
    @Enumerated(value = EnumType.STRING)
    private EventStatus eventStatus;
}
