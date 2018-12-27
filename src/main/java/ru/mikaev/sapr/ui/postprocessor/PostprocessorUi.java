package ru.mikaev.sapr.ui.postprocessor;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.atmosphere.config.service.Post;
import org.springframework.data.util.Pair;
import org.springframework.util.StringUtils;
import ru.mikaev.sapr.common.ProcessorDataHolder;
import ru.mikaev.sapr.dto.ConstructionDto;
import ru.mikaev.sapr.dto.PostprocessorDto;
import ru.mikaev.sapr.dto.ProcessorResult;
import ru.mikaev.sapr.dto.RodDto;
import ru.mikaev.sapr.service.Postprocessor;
import ru.mikaev.sapr.service.PostprocessorUiConfiguration;

import java.util.List;


@SpringComponent
@UIScope
@Route("postprocessor")
public class PostprocessorUi extends VerticalLayout implements AfterNavigationObserver {
    private final ProcessorDataHolder processorDataHolder;

    private final TextField partitionsField;

    private final Grid<PostprocessorDto> postprocessorGrid;

    public PostprocessorUi(ProcessorDataHolder processorDataHolder){
        this.processorDataHolder = processorDataHolder;

        partitionsField = new TextField();
        partitionsField.setPlaceholder("Partitions");
        partitionsField.setPattern("[0-9]+");
        partitionsField.setPreventInvalidInput(true);

        postprocessorGrid = new Grid<>();
        postprocessorGrid.addColumn(PostprocessorDto::getRodNum).setHeader("Rod number");
        postprocessorGrid.addColumn(PostprocessorDto::getX).setHeader("X");
        postprocessorGrid.addColumn(PostprocessorDto::getNx).setHeader("Nx");
        postprocessorGrid.addColumn(PostprocessorDto::getUx).setHeader("Ux");
        postprocessorGrid.addColumn(PostprocessorDto::getGx).setHeader("Gx");
        postprocessorGrid.addColumn(PostprocessorDto::getSigma).setHeader("σ");

        add(getMenu(), postprocessorGrid);

        setSizeFull();
    }

    private HorizontalLayout getMenu() {
        HorizontalLayout menu = new HorizontalLayout();

        Button calculateButton = new Button("Calculate", event -> calculateAndFill());

        menu.add(new Button("Previous page", event -> getUI().ifPresent(ui -> ui.navigate(""))));
        menu.add(partitionsField, calculateButton);
        return menu;
    }

    private void calculateAndFill(){
        if(StringUtils.isEmpty(partitionsField.getValue())){
            Notification.show("You must to set a number of partitions");
            return;
        }

        final Pair<ConstructionDto, ProcessorResult> pair = processorDataHolder.getProcessorResult().get();

        PostprocessorUiConfiguration configuration = new PostprocessorUiConfiguration(
                Integer.valueOf(partitionsField.getValue())
        );

        Postprocessor postprocessor = new Postprocessor(
                pair.getFirst(),
                pair.getSecond(),
                configuration
        );

        postprocessorGrid.setItems(postprocessor.postprocess());
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        if (!processorDataHolder.getProcessorResult().isPresent()) {
            getUI().ifPresent(ui -> ui.navigate(""));
        }
    }
}
