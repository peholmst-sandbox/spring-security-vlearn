package org.vaadin.peholmst.vlearn.spring.security.ui;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.Route;

import java.time.Instant;

@Route("")
public class ParameterizedRoute extends VerticalLayout implements HasUrlParameter<String> {

    private final Div parameterDiv;

    public ParameterizedRoute() {
        parameterDiv = new Div();
        add(parameterDiv);
        add(new Button("Do something", event -> {
            add(new Div(new Text("Something done. " + Instant.now())));
        }));
        add(new Button("Logout", event -> {
            getUI().ifPresent(ui -> ui.getPage().setLocation("/logout"));
        }));
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
        parameterDiv.removeAll();
        parameterDiv.add("Parameter value: " + parameter);
    }
}
