package code;

import com.github.lgooddatepicker.optionalusertools.DateTimeChangeListener;
import com.github.lgooddatepicker.zinternaltools.DateChangeEvent;
import com.github.lgooddatepicker.zinternaltools.DateTimeChangeEvent;
import com.github.lgooddatepicker.zinternaltools.TimeChangeEvent;

public class SimpleDateTimeChangeListener implements DateTimeChangeListener {

        /**
         * dateTimePickerName, This holds a chosen name for the component that we are listening to,
         * for generating time change messages in the demo.
         */
        public String dateTimePickerName;

        /**
         * Constructor.
         */
        private SimpleDateTimeChangeListener(String dateTimePickerName) {
            this.dateTimePickerName = dateTimePickerName;
        }

        /**
         * dateOrTimeChanged, This function will be called whenever the in date or time in the
         * applicable DateTimePicker has changed.
         */
        @Override
        public void dateOrTimeChanged(DateTimeChangeEvent event) {
            // Report on the overall DateTimeChangeEvent.
            String messageStart = "\n\nThe LocalDateTime in " + dateTimePickerName + " has changed from: (";
            String fullMessage = messageStart + event.getOldDateTimeStrict() + ") to (" + event.getNewDateTimeStrict() + ").";
//            if (!panel.messageTextArea.getText().startsWith(messageStart)) {
//                panel.messageTextArea.setText("");
//            }
//            panel.messageTextArea.append(fullMessage);
            // Report on any DateChangeEvent, if one exists.
            DateChangeEvent dateEvent = event.getDateChangeEvent();
            if (dateEvent != null) {
                String dateChangeMessage = "\nThe DatePicker value has changed from (" + dateEvent.getOldDate()
                    + ") to (" + dateEvent.getNewDate() + ").";
                //panel.messageTextArea.append(dateChangeMessage);
            }
            // Report on any TimeChangeEvent, if one exists.
            TimeChangeEvent timeEvent = event.getTimeChangeEvent();
            if (timeEvent != null) {
                String timeChangeMessage = "\nThe TimePicker value has changed from ("
                    + timeEvent.getOldTime() + ") to (" + timeEvent.getNewTime() + ").";
                //panel.messageTextArea.append(timeChangeMessage);
            }
        }
    }