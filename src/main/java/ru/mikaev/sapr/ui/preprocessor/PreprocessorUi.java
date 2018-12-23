package ru.mikaev.sapr.ui.preprocessor;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import ru.mikaev.sapr.common.PreprocessorDataHolder;
import ru.mikaev.sapr.dto.ConstructionDto;
import ru.mikaev.sapr.dto.KnotDto;
import ru.mikaev.sapr.dto.PreprocessorDataDto;
import ru.mikaev.sapr.dto.RodDto;
import ru.mikaev.sapr.factory.DefaultPreprocessorDataFactory;
import ru.mikaev.sapr.mapping.PreprocessorDataMapper;
import ru.mikaev.sapr.mapping.RodMapper;
import ru.mikaev.sapr.service.PreprocessorDataService;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@SpringComponent
@UIScope
@Route("preprocessor")
public class PreprocessorUi
        extends VerticalLayout
        implements AfterNavigationObserver {
    private final PreprocessorDataService dataService;
    private final PreprocessorDataMapper mapper;

    private final RodMapper rodMapper;

    private final PreprocessorDataHolder holder;

    private final Grid<RodDto> rodGrid;
    private final Grid<KnotDto> knotGrid;

    private int rodGridSequance = 0;
    private int knotGridSequance = 0;

    private final H2 datasetNameH2;

    private final Checkbox leftSupportCheckbox;

    private final Checkbox rightSupportCheckbox;

    public PreprocessorUi(PreprocessorDataService dataService,
                          PreprocessorDataMapper mapper,
                          PreprocessorDataHolder holder,
                          RodMapper rodMapper) {
        this.dataService = dataService;
        this.mapper = mapper;
        this.holder = holder;
        this.rodMapper = rodMapper;

        datasetNameH2 = new H2();

        leftSupportCheckbox = new Checkbox("Left support");
        leftSupportCheckbox.addValueChangeListener(e -> onLeftSupportChanged(e.getOldValue(), e.getValue()));

        rightSupportCheckbox = new Checkbox("Right support");
        rightSupportCheckbox.addValueChangeListener(e -> onRightSupportChanged(e.getOldValue(), e.getValue()));

        rodGrid = new Grid<>();
        rodGrid.addColumn(this::indexOfRod).setHeader("Number");
        rodGrid.addColumn(RodDto::getL).setHeader("L");
        rodGrid.addColumn(RodDto::getA).setHeader("A");
        rodGrid.addColumn(RodDto::getE).setHeader("E");
        rodGrid.addColumn(RodDto::getSigma).setHeader("σ");
        rodGrid.addColumn(RodDto::getLoad).setHeader("Load");

        knotGrid = new Grid<>();
        knotGrid.addColumn(this::indexOfKnot).setHeader("Number");
        knotGrid.addColumn(KnotDto::getLoad).setHeader("Load");

        HorizontalLayout menu = getMenu();

        HorizontalLayout supports = getSupports();

        HorizontalLayout gridLayout = new HorizontalLayout();

        VerticalLayout rodGridLayout = new VerticalLayout();
        rodGridLayout.add(new H2("Rods"), rodGrid);
        rodGridLayout.setSizeFull();

        VerticalLayout knotGridLayout = new VerticalLayout();
        knotGridLayout.add(new H2("Knots"), knotGrid);
        knotGridLayout.setSizeFull();

        gridLayout.add(rodGridLayout, knotGridLayout);
        gridLayout.setSizeFull();


        add(datasetNameH2);
        add(menu);
        add(supports);
        add(gridLayout);

        updateRodGrid();
        updateKnotGrid();
        updateSupports();
    }

    private void updateRodGrid() {
        holder
                .getPreprocessorData()
                .ifPresent(dto -> {
                    rodGridSequance = 0;
                    rodGrid.setItems(dto.getConstruction().getRods());
                });
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

        Set<KnotDto> knots = new LinkedHashSet<>();

        for (RodDto rod : rods) {
            final KnotDto leftKnot = rod.getLeftKnot();
            final KnotDto rightKnot = rod.getRightKnot();
            knots.add(leftKnot);
            knots.add(rightKnot);
        }

        knotGridSequance = 0;
        knotGrid.setItems(knots);
    }

    private void updateSupports(){
        final Optional<PreprocessorDataDto> preprocessorData = holder.getPreprocessorData();
        if (!preprocessorData.isPresent()) {
            return;
        }

        final ConstructionDto construction = preprocessorData.get().getConstruction();
        leftSupportCheckbox.setValue(construction.isSupportLeft());
        rightSupportCheckbox.setValue(construction.isSupportRight());
    }

    private HorizontalLayout getMenu() {
        HorizontalLayout menu = new HorizontalLayout();
        menu.add(new Button("Previous page", event -> getUI().ifPresent(ui -> ui.navigate("repository"))));
        menu.add(new Button("Add rod", event -> addRod()));
        menu.add(new Button("Delete rod", event -> deleteRod()));
        menu.add(new Button("Save", event -> saveData()));
        menu.add(new Button("Draw", event -> Notification.show("Draw construction")));

        return menu;
    }


    private void saveData(){
        dataService.mergePreprocessorData(holder.getPreprocessorData().get());
        holder.updateHolder();
    }

    private void addRod(){
        final List<RodDto> rods = holder.getPreprocessorData().get().getConstruction().getRods();

        final RodDto rodDto = rodMapper.fromRod(DefaultPreprocessorDataFactory.defaultRod());

        rodDto.setLeftKnot(rods.get(rods.size() - 1).getRightKnot());

        rods.add(rodDto);

        updateRodGrid();
        updateKnotGrid();
    }

    private void deleteRod(){
        final List<RodDto> rods = holder.getPreprocessorData().get().getConstruction().getRods();

        if(rods.size() == 1){
            Notification.show("Хотя бы один стержень должен быть!");
        }
        else{
            rods.remove(rods.size() - 1);
            updateRodGrid();
            updateKnotGrid();
        }
    }

    private int indexOfRod(RodDto rodDto){
        return ++rodGridSequance;
    }

    private int indexOfKnot(KnotDto knotDto){
        return ++knotGridSequance;
    }

    private HorizontalLayout getSupports() {
        HorizontalLayout supports = new HorizontalLayout();

        supports.add(leftSupportCheckbox);
        supports.add(rightSupportCheckbox);

        return supports;
    }


    private void onLeftSupportChanged(Boolean oldValue, Boolean newValue){
        if(!(newValue || rightSupportCheckbox.getValue())){
            Notification.show("At least one support must be!");
            leftSupportCheckbox.setValue(oldValue);
        }

        updateSupportsInDto();
    }


    private void onRightSupportChanged(Boolean oldValue, Boolean newValue){
        if(!(newValue || leftSupportCheckbox.getValue())){
            Notification.show("At least one support must be!");
            rightSupportCheckbox.setValue(oldValue);
        }

        updateSupportsInDto();
    }

    private void updateSupportsInDto(){
        final ConstructionDto construction = holder.getPreprocessorData().get().getConstruction();

        construction.setSupportLeft(leftSupportCheckbox.getValue());
        construction.setSupportRight(rightSupportCheckbox.getValue());
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        if (holder.getPreprocessorData().isPresent()) {
            holder.updateHolder();
            datasetNameH2.setText("Name: " + holder.getPreprocessorData().get().getDataName());
            updateRodGrid();
            updateKnotGrid();
            updateSupports();
        } else {
            getUI().ifPresent(ui -> ui.navigate(""));
        }
    }
}
