package org.vaadin.peholmst.vlearn.spring.security.ui;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.vaadin.peholmst.vlearn.spring.security.service.NaiveChatService;
import org.vaadin.peholmst.vlearn.spring.security.utils.CurrentUser;

@Route("")
@Push
public class ChatRoute extends VerticalLayout {

    private final NaiveChatService chatService;
    private final VerticalLayout messagesLayout;
    private NaiveChatService.Registration listenerRegistration;

    public ChatRoute(@NonNull NaiveChatService chatService) {
        this.chatService = chatService;

        var messageField = new TextField();
        var sendBtn = new Button("Send", event -> {
            chatService.sendMessage(messageField.getValue());
            messageField.clear();
            messageField.focus();
        });
        sendBtn.addClickShortcut(Key.ENTER);

        var sendLayout = new HorizontalLayout(messageField, sendBtn);
        add(sendLayout);

        messagesLayout = new VerticalLayout();
        messagesLayout.setSizeFull();
        add(messagesLayout);

        setSizeFull();
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        listenerRegistration = chatService.registerListener(this::onMessage);
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        listenerRegistration.unregister();
    }

    private void onMessage(String sender, String message) {
        var currentUser = CurrentUser.get().map(Authentication::getName).orElse("N/A");
        // The current user here is not actually the user who received the message, but the user who *sent* it.
        getUI().ifPresent(ui -> ui.access(() -> {
            // The current user here depends on what thread ends up invoking this operation. It could be the same
            // thread that called `onMessage()`, but also some other thread.
            var currentUserAgain = CurrentUser.get().map(Authentication::getName).orElse("N/A");
            messagesLayout.add(new Div(new Text(String.format("%s: %s to %s/%s: %s", Thread.currentThread().getName(), sender, currentUser, currentUserAgain,
                    message))));
        }));
    }
}
