package ru.mikaev.sapr.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route
public class MainView extends VerticalLayout {
    MainView() {
        H1 header = new H1("SAPR");

        setSizeFull();

        add(header);
        setAlignItems(Alignment.CENTER);

        HorizontalLayout menuLayout = new HorizontalLayout();
        menuLayout.add(new Button("Preprocessor", event -> getUI().ifPresent(ui -> ui.navigate("repository"))));
        menuLayout.add(new Button("Processor", event -> Notification.show("Clicked!")));
        menuLayout.add(new Button("Postprocessor", event -> Notification.show("Clicked!")));

        add(menuLayout);
    }
}