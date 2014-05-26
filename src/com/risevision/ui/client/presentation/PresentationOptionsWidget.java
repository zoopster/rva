package com.risevision.ui.client.presentation;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.risevision.common.client.info.PresentationInfo;
import com.risevision.ui.client.common.info.FormValidatorInfo;
import com.risevision.ui.client.common.widgets.ActionsWidget;
import com.risevision.ui.client.common.widgets.DefaultResolutionWidget;
import com.risevision.ui.client.common.widgets.FormValidatorWidget;
import com.risevision.ui.client.common.widgets.background.BackgroundWidget;
import com.risevision.ui.client.common.widgets.grid.FormGridWidget;
import com.risevision.ui.client.presentation.common.PlaceholderSelectListBox;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class PresentationOptionsWidget extends VerticalPanel {	
	private static PresentationOptionsWidget instance;
	private PresentationInfo presentation;
	//UI pieces
	private Label titleLabel = new Label("Presentation Settings");
	private FormValidatorWidget formValidator = new FormValidatorWidget();
	private FormGridWidget mainGrid = new FormGridWidget(7, 2, true);
	private TextBox nameTextBox = new TextBox();
	private DefaultResolutionWidget resolutionWidget = new DefaultResolutionWidget();
	private BackgroundWidget backgroundWidget = new BackgroundWidget();
	private CheckBox isTemplateBox = new CheckBox("");
	private CheckBox hidePointer = new CheckBox("");
	private PlaceholderSelectListBox selectorListBox = new PlaceholderSelectListBox();
	private Label idLabel = new Label();
	private boolean isSaveData = false;
	
	private Command onChange;
	
	private ActionsWidget actionsWidget = new ActionsWidget();
	
	public static PresentationOptionsWidget getInstance() {
		try {
			if (instance == null)
				instance = new PresentationOptionsWidget();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return instance;
	}
	
	public PresentationOptionsWidget() {
		add(titleLabel);
		add(formValidator);
		add(mainGrid);
		
		add(actionsWidget);
		
		styleControls();
		
		// add widgets
		mainGrid.addRow("Name*:", 
				nameTextBox, "rdn-TextBoxMedium");
		mainGrid.addRow("Resolution*:", 
				resolutionWidget, null);
		mainGrid.addRow("Background:", 
				"An image or color background for the Presentation",
				backgroundWidget, null);
		mainGrid.addRow("Template:", 
				"Template Presentations are shared with all Companies below the Parent Company",
				isTemplateBox, "rdn-CheckBox");
		mainGrid.addRow("Hide Mouse Pointer:", 
				"Hide the mouse pointer so it is not seen and cannot be used in the Presentation",
				hidePointer, "rdn-CheckBox");
		mainGrid.addRow("Play Until Done Placeholder:", 
				"The Placeholder that indicates when the Presentation is finished",
				selectorListBox, null);
		mainGrid.addRow("ID:", 
				"The reference used to include this Presentation in another Presentation",
				idLabel, null);
		
		initActions();
		initValidator();
		resolutionWidget.setValidator(formValidator);
	}

	private void styleControls() {
		setSize("400px", "100%");
		getElement().getStyle().setProperty("padding", "10px");
		
		titleLabel.setStyleName("rdn-Head");
		
		actionsWidget.addStyleName("rdn-VerticalSpacer");
	}

	private void initValidator() {
		// Add widgets to validator
		formValidator.addValidationElement(nameTextBox, "Id", FormValidatorInfo.requiredFieldValidator);
	}

	private void initActions() {
		Command cmdSave = new Command() {
			public void execute() {
				doActionSave();
			}
		};			
		
		Command cmdCancel = new Command() {
			public void execute() {
				doActionCancel();
			}
		};	

		actionsWidget.addAction("Okay", cmdSave);
		actionsWidget.addAction("Cancel", cmdCancel);
	}

	public void init(Command onChange){
		// if Adding new presentation
		this.init(null, onChange);
	}

	public void init(PresentationInfo presentation, Command onChange){
		this.presentation = presentation;
		this.onChange = onChange;

		bindData();
	}
	
	public void onLoad() {
		nameTextBox.setFocus(true);
		
		bindData();
	}

	private void bindData() {
		if (presentation == null){
			presentation = new PresentationInfo();
		}

		nameTextBox.setText(presentation.getName());
		
		resolutionWidget.setValue(presentation.getWidth(), presentation.getWidthUnits(), presentation.getHeight(), presentation.getHeightUnits());
		backgroundWidget.init(presentation.getBackgroundStyle(), presentation.isBackgroundScaleToFit());

		isTemplateBox.setValue(presentation.isTemplate());
		hidePointer.setValue(presentation.getHidePointer());
		selectorListBox.bindSelectListBox(presentation, presentation.getDonePlaceholder());
		
		if (presentation.getId() != null && !presentation.getId().isEmpty()) {
//			mainGrid.getRowFormatter().setVisible(4, true);
			idLabel.setText(presentation.getId());
		}
		else {
//			mainGrid.getRowFormatter().setVisible(4, false);
		}

	}

	private boolean saveData() {
		if (presentation == null)
			return true;

		if (!formValidator.validate())
			return false;
			
		presentation.setName(nameTextBox.getText());
		
		presentation.setHeight(resolutionWidget.getHeight());
		presentation.setWidth(resolutionWidget.getWidth());
		presentation.setHeightUnits(resolutionWidget.getHeightUnits());
		presentation.setWidthUnits(resolutionWidget.getWidthUnits());

		presentation.setBackgroundStyle(backgroundWidget.getStyle());
		presentation.setBackgroundScaleToFit(backgroundWidget.isScaleToFit());

		presentation.setTemplate(isTemplateBox.getValue());
		presentation.setHidePointer(hidePointer.getValue());
		presentation.setDonePlaceholder(selectorListBox.getSelectedValue());
		
		return true;
	}

	private void doActionSave() {
		if (saveData()) {
			isSaveData = true;
			if (onChange != null)
				onChange.execute();
		}
	}
	
	private void doActionCancel() {
		bindData();
		isSaveData = false;
		if (onChange != null)
			onChange.execute();
	}
	
	public boolean isSaveData() {
		return isSaveData;
	}
	
	public PresentationInfo getPresentation() {
		return presentation;
	}

}