package me.jiho.demorestapi.event;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import me.jiho.demorestapi.accounts.Account;
import me.jiho.demorestapi.accounts.AccountSerializer;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder @AllArgsConstructor @NoArgsConstructor
@Getter @Setter @EqualsAndHashCode(of = "id")
@Entity
public class Event {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String description;
    private LocalDateTime beginEnrollmentDateTime;
    private LocalDateTime closeEnrollmentDateTime;
    private LocalDateTime beginEventDateTime;
    private LocalDateTime endEventDateTime;
    private String location;
    private Integer basePrice;
    private Integer maxPrice;
    private Integer limitOfEnrollment;
    private boolean offline;
    private boolean free;
    @Enumerated(value = EnumType.STRING)
    private EventStatus eventStatus;

    @ManyToOne
    @JsonSerialize(using = AccountSerializer.class)
    private Account manager;

    public void update() {
        // Update Free
        if (this.basePrice == 0 && this.maxPrice == 0) {
            this.free = true;
        } else {
            this.free = false;
        }

        if (this.location == null || this.location.isBlank()) {
            this.offline = false;
        } else {
            this.offline = true;
        }

        this.eventStatus = EventStatus.DRAFT;
    }
}
