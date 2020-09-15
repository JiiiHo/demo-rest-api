package me.jiho.demorestapi.event;

import me.jiho.demorestapi.accounts.Account;
import me.jiho.demorestapi.accounts.CurrentUser;
import me.jiho.demorestapi.common.ErrorsResource;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Controller
@RequestMapping(value = "/api/events", produces = MediaTypes.HAL_JSON_VALUE)
public class EventController {

    private final EventRepository eventRepository;

    private final ModelMapper modelMapper;

    private final EventValidator eventValidator;

    public EventController(EventRepository eventRepository, ModelMapper modelMapper, EventValidator eventValidator) {
        this.eventRepository = eventRepository;
        this.modelMapper = modelMapper;
        this.eventValidator = eventValidator;
    }

    @PostMapping
    public ResponseEntity createEvent(@RequestBody @Valid EventDto eventDto, Errors errors) {
        if (errors.hasErrors()) {
            return badRequest(errors);
        }

        eventValidator.validate(eventDto, errors);

        if (errors.hasErrors()) {
            return badRequest(errors);
        }

        // eventDto 객체를 event 객체로 매핑 해주는 ModelMapper 아니면 빌더로 하기
        Event event = modelMapper.map(eventDto, Event.class);
        event.update();
        Event event1 = this.eventRepository.save(event);
        WebMvcLinkBuilder selfLinkBuilder = linkTo(EventController.class).slash(event1.getId());
        URI createdUri = selfLinkBuilder.toUri();
        event.setId(10l);
        EventResource eventResource = new EventResource(event);
        eventResource.add(linkTo(EventController.class).withRel("query-events"));
        eventResource.add(selfLinkBuilder.withRel("update-event"));
        eventResource.add(new Link("/docs/index.html#resources-events-create").withRel("profile"));
        return ResponseEntity.created(createdUri).body(eventResource);
    }

    @GetMapping
    public ResponseEntity queryEvents(Pageable pageable,
                                      PagedResourcesAssembler<Event> assembler,
                                      @CurrentUser Account account) {
        Page<Event> page = this.eventRepository.findAll(pageable);
        PagedModel<EntityModel<Event>> pagedModel = assembler.toModel(page, e -> new EventResource(e));
        pagedModel.add(Link.of("/docs/index.html#resources-events-list").withRel("profile"));
        if (account != null) {
            pagedModel.add(linkTo(EventController.class).withRel("create-event"));
        }
        return ResponseEntity.ok(pagedModel);
    }



    @GetMapping("/{id}")
    public ResponseEntity getEvent(@PathVariable Long id,
                                   @CurrentUser Account account
    ) {
        Optional<Event> optionalEvent = this.eventRepository.findById(id);
        if (optionalEvent.isEmpty())
            return ResponseEntity.notFound().build();
        Event event = optionalEvent.get();
        EventResource eventResource = new EventResource(event);
        eventResource.add(Link.of("/docs/index.html#resources-events-get").withRel("profile"));
        if (event.getManager().equals(account))
            eventResource.add(linkTo(EventController.class).slash(event.getId()).withRel("update-event"));
        return ResponseEntity.ok(eventResource);
    }

    @PutMapping("/{id}")
    public ResponseEntity updateEvent(@PathVariable Long id,
                                      @RequestBody @Valid EventDto eventDto,
                                      @CurrentUser Account currnetUser,
                                      Errors errors) {
        Optional<Event> optionalEvent = this.eventRepository.findById(id);
        if (optionalEvent.isEmpty())
            return ResponseEntity.notFound().build();

        if (errors.hasErrors()) {
            return badRequest(errors);
        }

        //비지니스 로직에 맞는지 확인
        this.eventValidator.validate(eventDto, errors);

        if (errors.hasErrors()) {
            return badRequest(errors);
        }

        Event existEvent = optionalEvent.get();
        if (!existEvent.getManager().equals(currnetUser))
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);

        this.modelMapper.map(eventDto, existEvent);
        Event savedEvent = this.eventRepository.save(existEvent);

        EventResource eventResource = new EventResource(savedEvent);
        eventResource.add(Link.of("/docs/index.html#resources-events-update").withRel("profile"));

        return ResponseEntity.ok(eventResource);

    }

    private ResponseEntity<ErrorsResource> badRequest(Errors errors) {
        return ResponseEntity.badRequest()
                .body(new ErrorsResource(errors));
    }
}
