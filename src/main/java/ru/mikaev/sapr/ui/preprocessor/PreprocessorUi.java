package ru.mikaev.sapr.ui.preprocessor;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import ru.mikaev.sapr.common.PreprocessorDataHolder;
import ru.mikaev.sapr.dto.KnotDto;
import ru.mikaev.sapr.dto.PreprocessorDataDto;
import ru.mikaev.sapr.dto.RodDto;
import ru.mikaev.sapr.mapping.PreprocessorDataMapper;
import ru.mikaev.sapr.service.PreprocessorDataService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringComponent
@UIScope
@Route("preprocessor")
public class PreprocessorUi
        extends VerticalLayout
        implements AfterNavigationObserver {
    private final PreprocessorDataService dataService;
    private final PreprocessorDataMapper mapper;

    private final PreprocessorDataHolder holder;

    private final Grid<RodDto> rodGrid;
    private final Grid<KnotDto> knotGrid;

    public PreprocessorUi(PreprocessorDataService dataService,
                          PreprocessorDataMapper mapper,
                          PreprocessorDataHolder holder) {
        this.dataService = dataService;
        this.mapper = mapper;
        this.holder = holder;

        rodGrid = new Grid<>();
        rodGrid.addColumn(RodDto::getL).setHeader("L");
        rodGrid.addColumn(RodDto::getA).setHeader("A");
        rodGrid.addColumn(RodDto::getE).setHeader("E");
        rodGrid.addColumn(RodDto::getSigma).setHeader("σ");
        rodGrid.addColumn(RodDto::getLoad).setHeader("Load");

        knotGrid = new Grid<>();
        knotGrid.addColumn(KnotDto::getLoad).setHeader("Load");

        HorizontalLayout menu = getMenu();

        HorizontalLayout supports = getSupports();

        HorizontalLayout gridLayout = new HorizontalLayout();
        gridLayout.add(rodGrid, knotGrid);
        gridLayout.setSizeFull();


        add(menu);
        add(supports);
        add(gridLayout);

        updateRodGrid();
        updateKnotGrid();
    }

    private void updateRodGrid() {
        holder
                .getPreprocessorData()
                .ifPresent(dto ->
                        rodGrid.setItems(dto.getConstruction().getRods()));
    }

    private void updateKnotGrid() {
        final Optional<PreprocessorDataDto> preprocessorData = holder.getPreprocessorData();
        if (!preprocessorData.isPresent()) {
            return;
        }

        final List<RodDto> rods = preprocessorData
                .get()
                .getConstruction()
                .getRods();

        List<KnotDto> knots = new ArrayList<>();

        for (RodDto rod : rods) {
            final KnotDto leftKnot = rod.getLeftKnot();
            final KnotDto rightKnot = rod.getRightKnot();
            knots.add(leftKnot);
            knots.add(rightKnot);
        }

        knotGrid.setItems(knots);
    }

    private HorizontalLayout getMenu() {
        HorizontalLayout menu = new HorizontalLayout();
        menu.add(new Button("Previous", event -> getUI().ifPresent(ui -> ui.navigate("repository"))));
        menu.add(new Button("Save", event -> Notification.show("Save")));
        menu.add(new Button("Draw", event -> Notification.show("Draw")));

        return menu;
    }

    private HorizontalLayout getSupports() {
        HorizontalLayout supports = new HorizontalLayout();
        Checkbox leftSupportCheckbox = new Checkbox("Left support");
        Checkbox rightSupportCheckbox = new Checkbox("Right support");

        supports.add(leftSupportCheckbox);
        supports.add(rightSupportCheckbox);

        return supports;
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        if (holder.getPreprocessorData().isPresent()) {
            updateRodGrid();
            updateKnotGrid();
        } else {
            getUI().ifPresent(ui -> ui.navigate(""));
        }
    }
}
