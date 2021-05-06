package org.vaadin.peholmst.vlearn.spring.security.ui;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import java.time.Instant;

@Route("")
public class HelloRoute extends VerticalLayout {

    public HelloRoute() {
        add(new Button("Do something", event -> {
            add(new Div(new Text("Something done. " + Instant.now())));
        }));
    }
}
