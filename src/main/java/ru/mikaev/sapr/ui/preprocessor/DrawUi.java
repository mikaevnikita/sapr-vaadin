package ru.mikaev.sapr.ui.preprocessor;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import ru.mikaev.sapr.common.PreprocessorDataHolder;


@SpringComponent
@UIScope
@Route("draw")
public class DrawUi extends VerticalLayout implements AfterNavigationObserver {
    private final PreprocessorUi preprocessorUi;

    private final PreprocessorDataHolder holder;

    public DrawUi(PreprocessorUi preprocessorUi, PreprocessorDataHolder holder) {
        this.preprocessorUi = preprocessorUi;
        this.holder = holder;


        add(getMenu());
    }

    HorizontalLayout getMenu() {
        HorizontalLayout menu = new HorizontalLayout();

        menu.add(new Button("Previous page", event -> getUI().ifPresent(ui -> ui.navigate("preprocessor"))));

        return menu;
    }

    private void drawConstruction() {

    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        if (!preprocessorUi.isDrawAction()) {
            getUI().ifPresent(ui -> ui.navigate(""));
        } else {
            drawConstruction();
        }
    }
}
