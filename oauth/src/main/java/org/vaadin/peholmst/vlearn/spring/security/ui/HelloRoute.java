package org.vaadin.peholmst.vlearn.spring.security.ui;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

import java.time.Instant;

@Route("")
public class HelloRoute extends VerticalLayout {

    public HelloRoute() {
        var authentication = (OAuth2AuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        var user = authentication.getPrincipal();
        var name = user.<String>getAttribute("name");
        var avatarUrl = user.<String>getAttribute("avatar_url");

        add(new Image(avatarUrl, name));
        add(new H2(name));
        add(new Button("Do something", event -> {
            add(new Div(new Text("Something done. " + Instant.now())));
        }));
    }
}
