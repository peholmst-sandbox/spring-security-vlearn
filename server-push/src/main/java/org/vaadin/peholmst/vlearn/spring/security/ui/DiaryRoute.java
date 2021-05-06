package org.vaadin.peholmst.vlearn.spring.security.ui;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.lang.NonNull;
import org.vaadin.peholmst.vlearn.spring.security.service.Diary;
import org.vaadin.peholmst.vlearn.spring.security.service.Entry;
import org.vaadin.peholmst.vlearn.spring.security.utils.CurrentUser;

@Route("")
@Push
public class DiaryRoute extends VerticalLayout {

    private final Diary diary;
    private final Grid<Entry> entries;
    private Diary.Registration listenerRegistration;

    public DiaryRoute(@NonNull Diary diary) {
        this.diary = diary;
        entries = new Grid<>();
        entries.addColumn(Entry::getCreated);
        entries.addColumn(Entry::getAuthor);
        entries.addColumn(Entry::getContent);
        entries.setSizeFull();
        add(entries);

        if (CurrentUser.hasRole("WRITER")) {
            var contentField = new TextField();
            var recordBtn = new Button("Record", event -> {
                diary.recordEntry(contentField.getValue());
                contentField.clear();
                contentField.focus();
            });
            recordBtn.addClickShortcut(Key.ENTER);

            var recordLayout = new HorizontalLayout(contentField, recordBtn);
            add(recordLayout);
        }
        setSizeFull();
    }

    private void refreshGrid() {
        entries.setItems(diary.getEntries());
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        listenerRegistration = diary.registerListener(() -> {
            getUI().ifPresent(ui -> ui.access(this::refreshGrid));
        });
        refreshGrid();
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        listenerRegistration.unregister();
    }
}
