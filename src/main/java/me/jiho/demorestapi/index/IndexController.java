package me.jiho.demorestapi.index;

import me.jiho.demorestapi.event.EventController;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.ControllerLinkBuilder.linkTo;

@RestController
public class IndexController {

    @GetMapping("/api")
    public RepresentationModel index() {
        RepresentationModel model = new RepresentationModel();
        model.add(linkTo(EventController.class).withRel("events"));
        return model;
    }
}
