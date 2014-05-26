// Copyright © 2010 - May 2014 Rise Vision Incorporated.
// Use of this software is governed by the GPLv3 license
// (reproduced in the LICENSE file).

package com.risevision.ui.client.common.widgets.timeline;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.risevision.common.client.info.TimeLineInfo;
import com.risevision.common.client.info.TimeLineInfo.RecurrenceType;
import com.risevision.common.client.utils.RiseUtils;
import com.risevision.ui.client.common.info.FormValidatorInfo;
import com.risevision.ui.client.common.widgets.ActionsWidget;
import com.risevision.ui.client.common.widgets.FormValidatorWidget;
import com.risevision.ui.client.common.widgets.LabelEnableWidget;
import com.risevision.ui.client.common.widgets.NumericBoxWidget;
import com.risevision.ui.client.common.widgets.RiseListBox;
import com.risevision.ui.client.common.widgets.TimeListBoxWidget;
import com.risevision.ui.client.common.widgets.TimeZoneWidget;

public class TimelineSelectWidget extends PopupPanel {
	
	private static TimelineSelectWidgetUiBinder uiBinder = GWT.create(TimelineSelectWidgetUiBinder.class);
	interface TimelineSelectWidgetUiBinder extends UiBinder<Widget, TimelineSelectWidget> {}
	private static TimelineSelectWidget instance;
	// fields
	private String description;
	private Command onSave;
	//UI pieces
	
	@UiField ActionsWidget wgActions;
	@UiField DeckPanel pnlRecurrence;
	@UiField FormValidatorWidget requiredValidator;
	@UiField FormValidatorWidget valueValidator;
	
	@UiField DateBox tbStartDate;
	@UiField DateBox tbEndDate;
	@UiField CheckBox cbNoEndDate;
	@UiField LabelEnableWidget lbEndDate;
	
	@UiField TimeListBoxWidget tbStartTime;
	@UiField TimeListBoxWidget tbEndTime;
	@UiField CheckBox cbAllDay;
	@UiField LabelEnableWidget lbStartTime;
	@UiField LabelEnableWidget lbEndTime;
	
	@UiField TimeZoneWidget lsbTimeZone;
	@UiField CheckBox cbUseDisplayTime;
	@UiField LabelEnableWidget lbTimeZone;
	
	@UiField RadioButton rbDaily;
	@UiField RadioButton rbWeekly;
	@UiField RadioButton rbMonthly;
	@UiField RadioButton rbYearly;

	@UiField NumericBoxWidget tbDailyRecurrenceFrequency;

	@UiField NumericBoxWidget tbWeeklyRecurrenceFrequency;
	@UiField CheckBox cbSunday;
	@UiField CheckBox cbMonday;
	@UiField CheckBox cbTuesday;
	@UiField CheckBox cbWednesday;
	@UiField CheckBox cbThursday;
	@UiField CheckBox cbFriday;
	@UiField CheckBox cbSaturday;
	
	@UiField RadioButton rbMonthlyRecurrenceAbsolute;
	@UiField RadioButton rbMonthlyRecurrenceRelative;
	@UiField NumericBoxWidget tbMonthlyRecurrenceDayOfMonth;
	@UiField NumericBoxWidget tbMonthlyRecurrenceFrequency;
	@UiField RiseListBox lsbMonthlyRecurrenceWeekOfMonth;
	@UiField RiseListBox lsbMonthlyRecurrenceDayOfWeek;
	@UiField NumericBoxWidget tbMonthlyRecurrenceRelativeFrequency;	
	@UiField LabelEnableWidget lbMonthlyRecurrenceAbsolute1;
	@UiField LabelEnableWidget lbMonthlyRecurrenceAbsolute2;
	@UiField LabelEnableWidget lbMonthlyRecurrenceRelative1;
	@UiField LabelEnableWidget lbMonthlyRecurrenceRelative2;
	
	@UiField RadioButton rbYearlyRecurrenceAbsolute;
	@UiField RadioButton rbYearlyRecurrenceRelative;
	@UiField RiseListBox lsbYearlyRecurrenceMonthOfYear;
	@UiField NumericBoxWidget tbYearlyRecurrenceDayOfMonth;
	@UiField RiseListBox lsbYearlyRecurrenceWeekOfMonth;
	@UiField RiseListBox lsbYearlyRecurrenceDayOfWeek;
	@UiField RiseListBox lsbYearlyRecurrenceRelativeMonthOfYear;
	@UiField LabelEnableWidget lbYearlyRecurrenceRelative1;

	public TimeLineInfo getTimeline() {
		TimeLineInfo t = new TimeLineInfo();
		t.setUseSchedule(true);
		
		t.setStartDate(tbStartDate.getValue());
		t.setEndDate(tbEndDate.getValue());
		t.setNoEndDate(cbNoEndDate.getValue());

		t.setStartTime(tbStartTime.getValue());
		t.setEndTime(tbEndTime.getValue());
		t.setAllDay(cbAllDay.getValue());

		t.setTimeZone(lsbTimeZone.getSelectedValue());
		t.setUseDisplayTime(cbUseDisplayTime.getValue());
		
		if (rbWeekly.getValue()) {
			t.setRecurrenceType(RecurrenceType.Weekly);
			t.setRecurrenceFrequency(RiseUtils.strToInt(tbWeeklyRecurrenceFrequency.getText(), 1));
			t.setRecurrenceSunday(cbSunday.getValue());
			t.setRecurrenceMonday(cbMonday.getValue());
			t.setRecurrenceTuesday(cbTuesday.getValue());
			t.setRecurrenceWednesday(cbWednesday.getValue());
			t.setRecurrenceThursday(cbThursday.getValue());
			t.setRecurrenceFriday(cbFriday.getValue());
			t.setRecurrenceSaturday(cbSaturday.getValue());
		} else if (rbMonthly.getValue()) {
			t.setRecurrenceType(RecurrenceType.Monthly);
            if (rbMonthlyRecurrenceAbsolute.getValue()) {
                t.setRecurrenceIsAbsolute(true);
                t.setRecurrenceDayOfMonth(RiseUtils.strToInt(tbMonthlyRecurrenceDayOfMonth.getText(), 1));
    			t.setRecurrenceFrequency(RiseUtils.strToInt(tbMonthlyRecurrenceFrequency.getText(), 1));
            } else {
                t.setRecurrenceIsAbsolute(false);
                t.setRecurrenceWeekOfMonth(RiseUtils.strToInt(lsbMonthlyRecurrenceWeekOfMonth.getSelectedValue(),1 ));
                t.setRecurrenceDayOfWeek(RiseUtils.strToInt(lsbMonthlyRecurrenceDayOfWeek.getSelectedValue(), 1));
    			t.setRecurrenceFrequency(RiseUtils.strToInt(tbMonthlyRecurrenceRelativeFrequency.getText(), 1));
            }
		} else if (rbYearly.getValue()) {
			t.setRecurrenceType(RecurrenceType.Yearly);
            if(rbYearlyRecurrenceAbsolute.getValue()) {
                t.setRecurrenceIsAbsolute(true);
                t.setRecurrenceMonthOfYear(RiseUtils.strToInt(lsbYearlyRecurrenceMonthOfYear.getSelectedValue(), 1));
                t.setRecurrenceDayOfMonth(RiseUtils.strToInt(tbYearlyRecurrenceDayOfMonth.getText(), 1));
            } else {
                t.setRecurrenceIsAbsolute(false);
                t.setRecurrenceWeekOfMonth(RiseUtils.strToInt(lsbYearlyRecurrenceWeekOfMonth.getSelectedValue(), 1));
                t.setRecurrenceDayOfWeek(RiseUtils.strToInt(lsbYearlyRecurrenceDayOfWeek.getSelectedValue(), 1));
                t.setRecurrenceMonthOfYear(RiseUtils.strToInt(lsbYearlyRecurrenceRelativeMonthOfYear.getSelectedValue(), 1));
            }
		} else {
			t.setRecurrenceType(RecurrenceType.Daily);
			t.setRecurrenceFrequency(RiseUtils.strToInt(tbDailyRecurrenceFrequency.getText(), 1));
		}

		description = t.getDescription();
		return t;
	}

	public void setTimeline(TimeLineInfo t) {
		//TimeLineInfo t = new TimeLineInfo(timeline);

		if (t.getStartDate() == null)
			t.setStartDate(new Date());
		if (t.getEndDate() == null)
			t.setEndDate(new Date());
		tbStartDate.setValue(t.getStartDate());
		tbEndDate.setValue(t.getEndDate());
		cbNoEndDate.setValue(t.getNoEndDate(), true);
		
		tbStartTime.setValue(t.getStartTime());
		tbEndTime.setValue(t.getEndTime());
		cbAllDay.setValue(t.getAllDay(), true);
		
		lsbTimeZone.setSelectedValue(t.getTimeZone());
		cbUseDisplayTime.setValue(t.getUseDisplayTime(), true);
		
		switch (t.getRecurrenceType()) {
		case Daily:
			rbDaily.setValue(true, true);
			tbDailyRecurrenceFrequency.setText(Integer.toString(t.getRecurrenceFrequency()));
			break;
		case Weekly:
			rbWeekly.setValue(true, true);
			cbSunday.setValue(t.getRecurrenceSunday());
			cbMonday.setValue(t.getRecurrenceMonday());
			cbTuesday.setValue(t.getRecurrenceTuesday());
			cbWednesday.setValue(t.getRecurrenceWednesday());
			cbThursday.setValue(t.getRecurrenceThursday());
			cbFriday.setValue(t.getRecurrenceFriday());
			cbSaturday.setValue(t.getRecurrenceSaturday());
			break;
		case Monthly:
			rbMonthly.setValue(true, true);
            if (t.getRecurrenceIsAbsolute()) {
        		rbMonthlyRecurrenceAbsolute.setValue(true, true);
        		tbMonthlyRecurrenceDayOfMonth.setText(Integer.toString(t.getRecurrenceDayOfMonth()));
        		tbMonthlyRecurrenceFrequency.setText(Integer.toString(t.getRecurrenceFrequency()));
            } else {
        		rbMonthlyRecurrenceRelative.setValue(true, true);
        		lsbMonthlyRecurrenceWeekOfMonth.setSelectedValue(Integer.toString(t.getRecurrenceWeekOfMonth()));
        		lsbMonthlyRecurrenceDayOfWeek.setSelectedValue(Integer.toString(t.getRecurrenceDayOfWeek()));
        		tbMonthlyRecurrenceRelativeFrequency.setText(Integer.toString(t.getRecurrenceFrequency()));
            }
			break;
		case Yearly:
			rbYearly.setValue(true, true);
            if (t.getRecurrenceIsAbsolute()) {
        		rbYearlyRecurrenceAbsolute.setValue(true, true);
        		lsbYearlyRecurrenceMonthOfYear.setSelectedValue(Integer.toString(t.getRecurrenceMonthOfYear()));
        		tbYearlyRecurrenceDayOfMonth.setText(Integer.toString(t.getRecurrenceDayOfMonth()));
            } else {
        		rbYearlyRecurrenceRelative.setValue(true, true);
        		lsbYearlyRecurrenceWeekOfMonth.setSelectedValue(Integer.toString(t.getRecurrenceWeekOfMonth()));
        		lsbYearlyRecurrenceDayOfWeek.setSelectedValue(Integer.toString(t.getRecurrenceDayOfWeek()));
        		lsbYearlyRecurrenceRelativeMonthOfYear.setSelectedValue(Integer.toString(t.getRecurrenceMonthOfYear()));
            }
			break;
		}
	}

	public TimelineSelectWidget() {
		super(true, true); //set auto-hide and modal
		add(uiBinder.createAndBindUi(this));
		//setSize("600px", "400px");
		setWidth("600px");
		initActions();
		styleControls();
		initUI();
		initValidator();
	}

	private void initValidator() {
		requiredValidator.addValidationElement(tbStartDate, "Start Date", FormValidatorInfo.requiredFieldValidator);
		requiredValidator.addValidationElement(tbEndDate, "End Date", FormValidatorInfo.requiredFieldValidator);
		requiredValidator.addValidationElement(tbDailyRecurrenceFrequency, "Daily Recurrence Frequency", FormValidatorInfo.requiredFieldValidator);
		requiredValidator.addValidationElement(tbWeeklyRecurrenceFrequency, "Weekly Recurrence Frequency", FormValidatorInfo.requiredFieldValidator);
		requiredValidator.addValidationElement(tbMonthlyRecurrenceDayOfMonth, "Monthly Recurrence Day Of Month", FormValidatorInfo.requiredFieldValidator);
		requiredValidator.addValidationElement(tbMonthlyRecurrenceFrequency, "Monthly Recurrence Frequency", FormValidatorInfo.requiredFieldValidator);
		requiredValidator.addValidationElement(tbMonthlyRecurrenceRelativeFrequency, "Monthly Recurrence Relative Frequency", FormValidatorInfo.requiredFieldValidator);
		requiredValidator.addValidationElement(tbYearlyRecurrenceDayOfMonth, "Yearly Recurrence Day Of Month", FormValidatorInfo.requiredFieldValidator);
		valueValidator.addValidationElement(tbStartDate, "Start Date", FormValidatorInfo.dateValidator);
		valueValidator.addValidationElement(tbEndDate, "End Date", FormValidatorInfo.dateValidator);
		valueValidator.addValidationElement(tbDailyRecurrenceFrequency, "Daily Recurrence Frequency", FormValidatorInfo.intValidator, false, 1, 999);
		valueValidator.addValidationElement(tbWeeklyRecurrenceFrequency, "Weekly Recurrence Frequency", FormValidatorInfo.intValidator, false, 1, 999);
		valueValidator.addValidationElement(tbMonthlyRecurrenceDayOfMonth, "Monthly Recurrence Day Of Month", FormValidatorInfo.intValidator, false, 1, 31);
		valueValidator.addValidationElement(tbMonthlyRecurrenceFrequency, "Monthly Recurrence Frequency", FormValidatorInfo.intValidator, false, 1, 999);
		valueValidator.addValidationElement(tbMonthlyRecurrenceRelativeFrequency, "Monthly Recurrence Relative Frequency", FormValidatorInfo.intValidator);
		valueValidator.addValidationElement(tbYearlyRecurrenceDayOfMonth, "Yearly Recurrence Day Of Month", FormValidatorInfo.intValidator, false, 1, 31);
	}

	private void initUI() {
		fillMonths();
		fillWeeks();
		fillWeeksOfMonth();
		
		tbStartDate.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getFormat(RiseUtils.SHORT_DATE_FORMAT)));
		tbStartDate.setValue(new Date());
		tbEndDate.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getFormat(RiseUtils.SHORT_DATE_FORMAT)));
		tbEndDate.setValue(new Date());
		
		cbNoEndDate.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				tbEndDate.setEnabled(!cbNoEndDate.getValue());
				lbEndDate.setEnabled(!cbNoEndDate.getValue());
				if (cbNoEndDate.getValue())
					tbEndDate.setValue(null);
				else if (tbEndDate.getValue() == null)
					tbEndDate.setValue(tbStartDate.getValue() != null ? tbStartDate.getValue() : new Date());
			}
		});
		cbNoEndDate.setValue(true, true);
		
		cbAllDay.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				tbStartTime.setEnabled(!cbAllDay.getValue());
				tbEndTime.setEnabled(!cbAllDay.getValue());
				lbStartTime.setEnabled(!cbAllDay.getValue());
				lbEndTime.setEnabled(!cbAllDay.getValue());
			}
		});
		cbAllDay.setValue(true, true);
		
		cbUseDisplayTime.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				lsbTimeZone.setEnabled(!cbUseDisplayTime.getValue());
				lbTimeZone.setEnabled(!cbUseDisplayTime.getValue());
			}
		});
		cbUseDisplayTime.setValue(true, true);
			
		ValueChangeHandler<Boolean> recurrenceTypeChanged = new ValueChangeHandler<Boolean>() {
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				updateRecurrencePannels();
			}
		};

		rbDaily.addValueChangeHandler(recurrenceTypeChanged);
		rbWeekly.addValueChangeHandler(recurrenceTypeChanged);
		rbMonthly.addValueChangeHandler(recurrenceTypeChanged);
		rbYearly.addValueChangeHandler(recurrenceTypeChanged);
		rbDaily.setValue(true, true);

		ValueChangeHandler<Boolean> recurrenceMonthlyChanged = new ValueChangeHandler<Boolean>() {
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				tbMonthlyRecurrenceDayOfMonth.setEnabled(rbMonthlyRecurrenceAbsolute.getValue());
				tbMonthlyRecurrenceFrequency.setEnabled(rbMonthlyRecurrenceAbsolute.getValue());
				lbMonthlyRecurrenceAbsolute1.setEnabled(rbMonthlyRecurrenceAbsolute.getValue());
				lbMonthlyRecurrenceAbsolute2.setEnabled(rbMonthlyRecurrenceAbsolute.getValue());
				
				lsbMonthlyRecurrenceWeekOfMonth.setEnabled(!rbMonthlyRecurrenceAbsolute.getValue());
				lsbMonthlyRecurrenceDayOfWeek.setEnabled(!rbMonthlyRecurrenceAbsolute.getValue());
				tbMonthlyRecurrenceRelativeFrequency.setEnabled(!rbMonthlyRecurrenceAbsolute.getValue());
				lbMonthlyRecurrenceRelative1.setEnabled(!rbMonthlyRecurrenceAbsolute.getValue());
				lbMonthlyRecurrenceRelative2.setEnabled(!rbMonthlyRecurrenceAbsolute.getValue());
			}
		};
		
		rbMonthlyRecurrenceAbsolute.addValueChangeHandler(recurrenceMonthlyChanged);
		rbMonthlyRecurrenceRelative.addValueChangeHandler(recurrenceMonthlyChanged);
		rbMonthlyRecurrenceAbsolute.setValue(true, true);

		ValueChangeHandler<Boolean> recurrenceYearlyChanged = new ValueChangeHandler<Boolean>() {
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				lsbYearlyRecurrenceMonthOfYear.setEnabled(rbYearlyRecurrenceAbsolute.getValue());
				tbYearlyRecurrenceDayOfMonth.setEnabled(rbYearlyRecurrenceAbsolute.getValue());
				lsbYearlyRecurrenceWeekOfMonth.setEnabled(!rbYearlyRecurrenceAbsolute.getValue());
				lsbYearlyRecurrenceDayOfWeek.setEnabled(!rbYearlyRecurrenceAbsolute.getValue());
				lsbYearlyRecurrenceRelativeMonthOfYear.setEnabled(!rbYearlyRecurrenceAbsolute.getValue());
				lbYearlyRecurrenceRelative1.setEnabled(!rbYearlyRecurrenceAbsolute.getValue());
			}
		};

		rbYearlyRecurrenceAbsolute.addValueChangeHandler(recurrenceYearlyChanged);
		rbYearlyRecurrenceRelative.addValueChangeHandler(recurrenceYearlyChanged);
		rbYearlyRecurrenceAbsolute.setValue(true, true);
		
		//show DAILY recurrence panel
		pnlRecurrence.showWidget(0);

	}
	
	private void updateRecurrencePannels() {
		if (rbDaily.getValue())
			pnlRecurrence.showWidget(0);
		else if (rbWeekly.getValue())
			pnlRecurrence.showWidget(1);
		else if (rbMonthly.getValue())
			pnlRecurrence.showWidget(2);
		else
			pnlRecurrence.showWidget(3);
	}

	public static TimelineSelectWidget getInstance() {
		try {
			if (instance == null)
				instance = new TimelineSelectWidget();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return instance;
	}	

	@Override
	public boolean onKeyDownPreview(char key, int modifiers) {
		// Use the popup's key preview hooks to close the dialog when either
		// enter or escape is pressed.
		switch (key) {
			case KeyCodes.KEY_ESCAPE:
				hide();
				break;
			}

		return true;
	}
	
	public void show(TimeLineInfo timeline, Command onSaveCallBack){
		super.show( );
		center();
		this.onSave = onSaveCallBack;
		description = timeline.getDescription();
		resetUI();
		setTimeline(timeline);
	}
	
	private void resetUI() {
		requiredValidator.clear();
		valueValidator.clear();
		tbStartTime.setSelectedIndex(8*4); //8:00 AM
		tbEndTime.setSelectedIndex(17*4+2); //5:30 PM
	}

	private void styleControls() {
		this.getElement().getStyle().setProperty("padding", "10px");
		wgActions.addStyleName("rdn-VerticalSpacer");
	}

	private void initActions(){
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

		wgActions.addAction("Save", cmdSave);
		wgActions.addAction("Cancel", cmdCancel);
	}	

	private void doActionSave() {
		if (formIsValid()) {
			hide();
			if (onSave != null)
				onSave.execute();
		}
	}
	
	private void doActionCancel() {
		hide();
	}
	
	private void fillMonths() {
		for(int i=0; i<12; i++) {
			lsbYearlyRecurrenceMonthOfYear.addItem(TimeLineInfo.MONTHNAMES[i], Integer.toString(i));
			lsbYearlyRecurrenceRelativeMonthOfYear.addItem(TimeLineInfo.MONTHNAMES[i], Integer.toString(i));
		}
	}

	private void fillWeeks() {
		for(int i=0; i<7; i++) {
			lsbMonthlyRecurrenceDayOfWeek.addItem(TimeLineInfo.WEEKDAYNAMES[i], Integer.toString(i));
			lsbYearlyRecurrenceDayOfWeek.addItem(TimeLineInfo.WEEKDAYNAMES[i], Integer.toString(i));
		}
	}

	private void fillWeeksOfMonth() {
		for(int i=0; i<5; i++) {
			lsbYearlyRecurrenceWeekOfMonth.addItem(TimeLineInfo.WEEKOFMONTHNAMES[i], Integer.toString(i));
			lsbMonthlyRecurrenceWeekOfMonth.addItem(TimeLineInfo.WEEKOFMONTHNAMES[i], Integer.toString(i));
		}
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	private boolean fieldIsValid(Widget w) {
		return (requiredValidator.validate(w) && valueValidator.validate(w));
	}

	private boolean formIsValid() {
		boolean res = true;
				
		requiredValidator.clear();
		valueValidator.clear();
	
		res &= fieldIsValid(tbStartDate);
		if (!cbNoEndDate.getValue())
			res &= fieldIsValid(tbEndDate);
						
		if (rbWeekly.getValue()) {
			res &= fieldIsValid(tbWeeklyRecurrenceFrequency);
		} else if (rbMonthly.getValue()) {
            if (rbMonthlyRecurrenceAbsolute.getValue()) {
    			res &= fieldIsValid(tbMonthlyRecurrenceDayOfMonth);
    			res &= fieldIsValid(tbMonthlyRecurrenceFrequency);
            } else {
    			res &= fieldIsValid(tbMonthlyRecurrenceRelativeFrequency);
            }
		} else if (rbYearly.getValue()) {
            if(rbYearlyRecurrenceAbsolute.getValue())
    			res &= fieldIsValid(tbYearlyRecurrenceDayOfMonth);
		} else {
			res &= fieldIsValid(tbDailyRecurrenceFrequency);
		}
			
		//make sure start date <= end date
		if (res && !cbNoEndDate.getValue()) {
			res = (tbStartDate.getValue().compareTo(tbEndDate.getValue()) <= 0);
			if (!res)
				requiredValidator.addErrorMessage("End Date must occur after Start Date.");
		}
		return res;
	}
	
}