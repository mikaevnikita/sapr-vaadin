package ru.mikaev.sapr.ui;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinRequest;
import elemental.html.Navigator;
import org.springframework.beans.factory.annotation.Autowired;

@Route
public class MainView extends VerticalLayout {
    MainView(){
        H1 header = new H1("SAPR by Mikaev");

        add(header);
        add(new Button("Препроцессор", event -> getUI().ifPresent(ui -> ui.navigate("preprocessor"))));
        add(new Button("Процессор", event -> Notification.show("Clicked!")));
        add(new Button("Постпроцессор", event -> Notification.show("Clicked!")));
    }
}