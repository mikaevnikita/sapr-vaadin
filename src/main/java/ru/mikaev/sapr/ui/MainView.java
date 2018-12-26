package ru.mikaev.sapr.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import ru.mikaev.sapr.common.PreprocessorDataHolder;
import ru.mikaev.sapr.common.ProcessorDataHolder;
import ru.mikaev.sapr.dto.ConstructionDto;
import ru.mikaev.sapr.dto.PreprocessorDataDto;
import ru.mikaev.sapr.service.Processor;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Route
public class MainView extends VerticalLayout {
    private final PreprocessorDataHolder holder;

    private final ProcessorDataHolder processorDataHolder;

    public MainView(PreprocessorDataHolder holder, ProcessorDataHolder processorDataHolder) {
        this.holder = holder;
        this.processorDataHolder = processorDataHolder;

        H1 header = new H1("SAPR");

        setSizeFull();

        add(header);
        setAlignItems(Alignment.CENTER);

        HorizontalLayout menuLayout = new HorizontalLayout();
        menuLayout.add(new Button("Preprocessor", event -> getUI().ifPresent(ui -> ui.navigate("repository"))));
        menuLayout.add(new Button("Processor", event -> calculate()));
        menuLayout.add(new Button("Postprocessor", event -> Notification.show("Clicked!")));

        add(menuLayout);
    }

    private void calculate(){
        if(!holder.getPreprocessorData().isPresent()){
            Notification.show("First you need to choose a data set!");
        }
        else{
            ConstructionDto construction = holder.getPreprocessorData().get().getConstruction();
            Processor processor = new Processor(construction);
            processorDataHolder.setProcessorResult(Optional.of(processor.process()));
            Notification.show("Processed!");
        }
    }

}