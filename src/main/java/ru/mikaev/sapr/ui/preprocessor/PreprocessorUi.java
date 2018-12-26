package ru.mikaev.sapr.ui.preprocessor;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.BeforeLeaveEvent;
import com.vaadin.flow.router.BeforeLeaveObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.apache.commons.lang3.StringUtils;
import ru.mikaev.sapr.common.PreprocessorDataHolder;
import ru.mikaev.sapr.dto.ConstructionDto;
import ru.mikaev.sapr.dto.KnotDto;
import ru.mikaev.sapr.dto.PreprocessorDataDto;
import ru.mikaev.sapr.dto.RodDto;
import ru.mikaev.sapr.factory.DefaultPreprocessorDataFactory;
import ru.mikaev.sapr.mapping.PreprocessorDataMapper;
import ru.mikaev.sapr.mapping.RodMapper;
import ru.mikaev.sapr.service.PreprocessorDataService;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@SpringComponent
@UIScope
@Route("preprocessor")
public class PreprocessorUi
        extends VerticalLayout
        implements AfterNavigationObserver, BeforeLeaveObserver {
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


    //rod grid operations panel
    private final Button editRodButton;

    private final HorizontalLayout editRodLayout;
    private final TextField rodL;
    private final TextField rodA;
    private final TextField rodE;
    private final TextField rodSigma;
    private final TextField rodLoad;

    //knot grid operations panel
    private final TextField knotLoadField;
    private final Button editKnotButton;

    private boolean constructionIsChanged;

    private boolean isDrawAction;

    public PreprocessorUi(PreprocessorDataService dataService,
                          PreprocessorDataMapper mapper,
                          PreprocessorDataHolder holder,
                          RodMapper rodMapper) {
        this.dataService = dataService;
        this.mapper = mapper;
        this.holder = holder;
        this.rodMapper = rodMapper;

        constructionIsChanged = false;
        isDrawAction = false;

        datasetNameH2 = new H2();

        //rod
        editRodButton = new Button("Edit rod");
        editRodButton.addClickListener(e -> onEditRod());

        editRodLayout = new HorizontalLayout();

        rodL = new TextField("L");
        rodL.setPattern("[0-9]*\\.?[0-9]+");
        rodL.setPreventInvalidInput(true);
        rodL.addValueChangeListener(e -> onRodTextFieldChanged(e.getOldValue(), e.getValue(), e.getSource()));

        rodA = new TextField("A");
        rodA.setPattern("[0-9]*\\.?[0-9]+");
        rodA.setPreventInvalidInput(true);
        rodA.addValueChangeListener(e -> onRodTextFieldChanged(e.getOldValue(), e.getValue(), e.getSource()));


        rodE = new TextField("E");
        rodE.setPattern("[0-9]*\\.?[0-9]+");
        rodE.setPreventInvalidInput(true);
        rodE.addValueChangeListener(e -> onRodTextFieldChanged(e.getOldValue(), e.getValue(), e.getSource()));


        rodSigma = new TextField("σ");
        rodSigma.setPattern("[0-9]*\\.?[0-9]+");
        rodSigma.setPreventInvalidInput(true);
        rodSigma.addValueChangeListener(e -> onRodTextFieldChanged(e.getOldValue(), e.getValue(), e.getSource()));


        rodLoad = new TextField("Load");
        rodLoad.setPattern("[-]?[0-9]*\\.?[0-9]+");
        rodLoad.setPreventInvalidInput(true);
        rodLoad.addValueChangeListener(e -> onRodTextFieldChanged(e.getOldValue(), e.getValue(), e.getSource()));


        editRodLayout.add(rodL, rodA, rodE, rodSigma, rodLoad);

        editRodLayout.setSizeFull();
        editRodLayout.setVisible(false);

        //knot
        knotLoadField = new TextField();
        knotLoadField.setPlaceholder("Load");
        knotLoadField.setPattern("[-]?[0-9]*\\.?[0-9]+");
        knotLoadField.setPreventInvalidInput(true);
        editKnotButton = new Button("Edit knot", e -> onEditKnot());

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
        knotGrid.addSelectionListener(e -> onSelectKnot(e.getFirstSelectedItem()));

        HorizontalLayout menu = getMenu();

        HorizontalLayout supports = getSupports();

        VerticalLayout gridLayout = new VerticalLayout();

        gridLayout.add(getRodLayout(), getKnotLayout());
        gridLayout.setSizeFull();


        add(datasetNameH2);
        add(menu);
        add(supports);
        add(gridLayout);

        updateRodGrid();
        updateKnotGrid();
        updateSupports();
    }

    private void onSelectKnot(Optional<KnotDto> firstSelectedItem) {
        if (firstSelectedItem.isPresent()) {
            knotLoadField.setValue(String.valueOf(firstSelectedItem.get().getLoad()));
        }
    }

    private void onRodTextFieldChanged(String oldValue, String newValue, TextField source) {
        if (StringUtils.isBlank(newValue)) {
            source.setValue("0");
        }
    }

    private VerticalLayout getRodLayout() {
        VerticalLayout rodGridLayout = new VerticalLayout();

        HorizontalLayout rodGridOperationsPanel = new HorizontalLayout();
        rodGridOperationsPanel.add(new Button("Add rod", event -> addRod()));
        rodGridOperationsPanel.add(new Button("Delete rod", event -> deleteRod()));
        rodGridOperationsPanel.add(editRodButton);

        rodGridOperationsPanel.setSizeFull();

        rodGridLayout.add(new H2("Rods"), rodGridOperationsPanel, editRodLayout, rodGrid);
        rodGridLayout.setSizeFull();

        return rodGridLayout;
    }

    private VerticalLayout getKnotLayout() {
        VerticalLayout knotGridLayout = new VerticalLayout();

        HorizontalLayout knotGridOperationsPanel = new HorizontalLayout();
        knotGridOperationsPanel.add(knotLoadField, editKnotButton);
        knotGridOperationsPanel.setSizeFull();

        knotGridLayout.add(new H2("Knots"), knotGridOperationsPanel, knotGrid);
        knotGridLayout.setSizeFull();

        return knotGridLayout;
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

    private void updateSupports() {
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
        menu.add(new Button("Save construction", event -> saveData()));
        menu.add(new Button("Draw construction", event -> {
            isDrawAction = true;
            getUI().ifPresent(ui -> ui.navigate("draw"));
        }));

        return menu;
    }

    private void onEditRod() {
        if (editRodButton.getText().equals("Save rod")) {
            final RodDto selectedRod = rodGrid.getSelectionModel().getFirstSelectedItem().get();

            selectedRod.setL(Double.valueOf(rodL.getValue()));
            selectedRod.setA(Double.valueOf(rodA.getValue()));
            selectedRod.setE(Double.valueOf(rodE.getValue()));
            selectedRod.setSigma(Double.valueOf(rodSigma.getValue()));
            selectedRod.setLoad(Double.valueOf(rodLoad.getValue()));

            editRodLayout.setVisible(false);
            editRodButton.setText("Edit rod");

            updateRodGrid();

            constructionIsChanged = true;
        } else {
            if (rodGrid.getSelectionModel().getFirstSelectedItem().isPresent()) {
                editRodLayout.setVisible(true);
                editRodButton.setText("Save rod");

                final RodDto selectedRod = rodGrid.getSelectionModel().getFirstSelectedItem().get();

                rodA.setValue(String.valueOf(selectedRod.getA()));
                rodL.setValue(String.valueOf(selectedRod.getL()));
                rodE.setValue(String.valueOf(selectedRod.getE()));
                rodSigma.setValue(String.valueOf(selectedRod.getSigma()));
                rodLoad.setValue(String.valueOf(selectedRod.getLoad()));
            } else {
                Notification.show("You must select a rod!");
            }
        }
    }

    private void onEditKnot() {
        if (knotGrid.getSelectionModel().getFirstSelectedItem().isPresent()) {
            final KnotDto selectedKnot = knotGrid.getSelectionModel().getFirstSelectedItem().get();
            selectedKnot.setLoad(Double.valueOf(knotLoadField.getValue()));

            updateKnotGrid();

            constructionIsChanged = true;
        }
    }


    private void saveData() {
        dataService.mergePreprocessorData(holder.getPreprocessorData().get());
        holder.updateHolder();
        constructionIsChanged = false;
    }

    private void addRod() {
        final List<RodDto> rods = holder.getPreprocessorData().get().getConstruction().getRods();

        final RodDto rodDto = rodMapper.fromRod(DefaultPreprocessorDataFactory.defaultRod());

        rodDto.setLeftKnot(rods.get(rods.size() - 1).getRightKnot());

        rods.add(rodDto);

        updateRodGrid();
        updateKnotGrid();

        constructionIsChanged = true;
    }

    private void deleteRod() {
        final List<RodDto> rods = holder.getPreprocessorData().get().getConstruction().getRods();

        constructionIsChanged = true;

        if (rods.size() == 1) {
            Notification.show("At least one rod must be!");
        } else {
            rods.remove(rods.size() - 1);
            updateRodGrid();
            updateKnotGrid();
        }
    }

    private int indexOfRod(RodDto rodDto) {
        return ++rodGridSequance;
    }

    private int indexOfKnot(KnotDto knotDto) {
        return ++knotGridSequance;
    }

    private HorizontalLayout getSupports() {
        HorizontalLayout supports = new HorizontalLayout();

        supports.add(leftSupportCheckbox);
        supports.add(rightSupportCheckbox);

        return supports;
    }


    private void onLeftSupportChanged(Boolean oldValue, Boolean newValue) {
        if (!(newValue || rightSupportCheckbox.getValue())) {
            Notification.show("At least one support must be!");
            leftSupportCheckbox.setValue(oldValue);
        }

        if (holder.getPreprocessorData().get().getConstruction().isSupportLeft() != newValue) {
            updateSupportsInDto();
        }
    }


    private void onRightSupportChanged(Boolean oldValue, Boolean newValue) {
        if (!(newValue || leftSupportCheckbox.getValue())) {
            Notification.show("At least one support must be!");
            rightSupportCheckbox.setValue(oldValue);
        }

        if (holder.getPreprocessorData().get().getConstruction().isSupportRight() != newValue) {
            updateSupportsInDto();
        }
    }

    private void updateSupportsInDto() {
        final ConstructionDto construction = holder.getPreprocessorData().get().getConstruction();

        construction.setSupportLeft(leftSupportCheckbox.getValue());
        construction.setSupportRight(rightSupportCheckbox.getValue());

        constructionIsChanged = true;
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        knotGridSequance = 0;
        rodGridSequance = 0;

        if (isDrawAction) {
            isDrawAction = false;
            return;
        }
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

    public boolean hasChanges() {
        return constructionIsChanged;
    }

    @Override
    public void beforeLeave(BeforeLeaveEvent event) {
        final BeforeLeaveEvent.ContinueNavigationAction continueNavigationAction = event.postpone();

        if (hasChanges()) {
            ConfirmDialog leaveConfirmDialog = new ConfirmDialog();
            leaveConfirmDialog.setHeader("Confirm leave");
            leaveConfirmDialog.setText("Are you sure you want to leave the page without save changes?");

            leaveConfirmDialog.setConfirmText("Save");
            leaveConfirmDialog.addConfirmListener(e -> {
                saveData();
                constructionIsChanged = false;
                leaveConfirmDialog.close();
                continueNavigationAction.proceed();
            });

            leaveConfirmDialog.setCancelText("Cancel");
            leaveConfirmDialog.setCancelable(true);
            leaveConfirmDialog.addCancelListener(e -> {
                leaveConfirmDialog.close();
            });

            leaveConfirmDialog.setRejectable(true);
            leaveConfirmDialog.setRejectText("Leave");

            leaveConfirmDialog.addRejectListener(e -> {
                leaveConfirmDialog.close();
                continueNavigationAction.proceed();
            });

            leaveConfirmDialog.open();
            return;
        }

        continueNavigationAction.proceed();
    }

    public boolean isDrawAction() {
        return isDrawAction;
    }
}
