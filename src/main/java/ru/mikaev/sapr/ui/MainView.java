package ru.mikaev.sapr.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route
public class MainView extends VerticalLayout {
    public MainView() {
        add(new Button("Препроцессор", event -> Notification.show("Clicked!")));
        add(new Button("Процессор", event -> Notification.show("Clicked!")));
        add(new Button("Постпроцессор", event -> Notification.show("Clicked!")));
    }
}