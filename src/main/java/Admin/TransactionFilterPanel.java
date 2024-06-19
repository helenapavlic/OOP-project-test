package Admin;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;

public class TransactionFilterPanel extends JPanel {
    private JDatePickerImpl dateTo;
    private JDatePickerImpl dateFrom;
    private JComboBox<String> transactionStatus;
    private JTextField transactionId;
    private JButton apply;
    private FilterPanelListener filterPanelListener;

    public TransactionFilterPanel() {
        initComponents();
        layoutComponents();
        decorate();
        activatePanel();
    }

    private void activatePanel() {
        apply.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validateInputs()) {
                    String[] filters = new String[4];

                    filters[0] = dateFrom.getModel().getValue() != null ? dateFrom.getJFormattedTextField().getText() : null;
                    filters[1] = dateTo.getModel().getValue() != null ? dateTo.getJFormattedTextField().getText() : null;
                    filters[2] = transactionStatus.getSelectedIndex() != -1 ? transactionStatus.getSelectedItem().toString() : null;
                    filters[3] = !transactionId.getText().trim().isEmpty() ? transactionId.getText().trim() : null;

                    TransactionFilterPanelEvent event = new TransactionFilterPanelEvent(this, filters);

                    if (filterPanelListener != null) {
                        filterPanelListener.transactionFilterPanelEventOccurred(event);
                    }
                }
            }
        });

        transactionId.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                checkInputs();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                checkInputs();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                checkInputs();
            }
        });

        transactionStatus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkInputs();
            }
        });

        dateFrom.getModel().addChangeListener(e -> checkInputs());
        dateTo.getModel().addChangeListener(e -> checkInputs());
    }

    private boolean validateInputs() {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd.MM.yyyy");
        try {
            if (!transactionId.getText().trim().isEmpty()) {
                int id = Integer.parseInt(transactionId.getText().trim());
                if (id < 0) {
                    JOptionPane.showMessageDialog(this, "Transaction ID must be a non-negative integer.", "Invalid Transaction ID", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }

            if (dateTo.getModel().getValue() != null && dateFrom.getModel().getValue() == null) {
                JOptionPane.showMessageDialog(this, "'Date to' cannot be set if 'Date from' is not set.", "Invalid Date Range", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            if (dateFrom.getModel().getValue() != null && dateTo.getModel().getValue() != null) {
                String fromDateStr = dateFrom.getJFormattedTextField().getText();
                String toDateStr = dateTo.getJFormattedTextField().getText();
                Calendar fromDate = Calendar.getInstance();
                Calendar toDate = Calendar.getInstance();
                fromDate.setTime(dateFormatter.parse(fromDateStr));
                toDate.setTime(dateFormatter.parse(toDateStr));
                if (fromDate.after(toDate)) {
                    JOptionPane.showMessageDialog(this, "'Date from' must be earlier than 'Date to'.", "Invalid Date Range", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Transaction ID must be a non-negative integer.", "Invalid Transaction ID", JOptionPane.ERROR_MESSAGE);
            return false;
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, "Invalid date format.", "Invalid Date", JOptionPane.ERROR_MESSAGE);
            return false;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid input.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    private void checkInputs() {
        boolean hasDateFrom = dateFrom.getModel().getValue() != null;
        boolean hasDateTo = dateTo.getModel().getValue() != null;
        boolean hasTransactionStatus = transactionStatus.getSelectedIndex() != -1;
        boolean hasTransactionId = !transactionId.getText().trim().isEmpty();

        apply.setEnabled(hasDateFrom || hasDateTo || hasTransactionStatus || hasTransactionId);
    }

    private void decorate() {
        Border inner = BorderFactory.createTitledBorder("Transaction filter");
        Border outer = BorderFactory.createEmptyBorder(2, 2, 2, 2);
        setBorder(BorderFactory.createCompoundBorder(outer, inner));
    }

    private void layoutComponents() {
        setLayout(new GridBagLayout());
        GridBagConstraints gridBagConstraints = new GridBagConstraints();

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = GridBagConstraints.LINE_START;

        gridBagConstraints.gridy++;
        add(new JLabel("Date from:"), gridBagConstraints);

        gridBagConstraints.gridx++;
        add(dateFrom, gridBagConstraints);

        add(Box.createVerticalStrut(40), gridBagConstraints);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy++;
        add(new JLabel("Date to: "), gridBagConstraints);
        gridBagConstraints.gridx++;
        add(dateTo, gridBagConstraints);

        add(Box.createVerticalStrut(40), gridBagConstraints);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy++;
        add(new JLabel("Transaction status: "), gridBagConstraints);
        gridBagConstraints.anchor = GridBagConstraints.LINE_END;
        gridBagConstraints.gridx++;
        add(transactionStatus, gridBagConstraints);

        add(Box.createVerticalStrut(40), gridBagConstraints);

        gridBagConstraints.anchor = GridBagConstraints.LINE_START;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy++;
        add(new JLabel("Transaction id: "), gridBagConstraints);

        gridBagConstraints.gridx++;
        add(transactionId, gridBagConstraints);

        add(Box.createVerticalStrut(40), gridBagConstraints);

        gridBagConstraints.gridy++;
        gridBagConstraints.gridx++;
        add(apply, gridBagConstraints);
    }

    private void initComponents() {
        setPreferredSize(new Dimension(440, getHeight()));

        UtilDateModel modelFrom = new UtilDateModel();
        UtilDateModel modelTo = new UtilDateModel();
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");

        JDatePanelImpl datePanelFromDate = new JDatePanelImpl(modelFrom, p);
        JDatePanelImpl datePanelToDate = new JDatePanelImpl(modelTo, p);

        dateFrom = new JDatePickerImpl(datePanelFromDate, new DateLabelFormatter());
        dateTo = new JDatePickerImpl(datePanelToDate, new DateLabelFormatter());

        transactionStatus = new JComboBox<>();
        DefaultComboBoxModel<String> transactionModel = new DefaultComboBoxModel<>();
        transactionModel.addElement("Successful transaction");
        transactionModel.addElement("Not enough money error");
        transactionModel.addElement("Item out of stock error");
        transactionModel.addElement("Item not found error");
        transactionModel.addElement("Cancelled transaction");
        transactionStatus.setModel(transactionModel);
        transactionStatus.setSelectedIndex(-1);

        transactionId = new JTextField(5);
        transactionId.setPreferredSize(new Dimension(10, 25));

        apply = new JButton("Apply");
        apply.setActionCommand("APPLY_TRANSACTION");
        apply.setEnabled(false);
    }

    public void reset() {
        dateFrom.getModel().setValue(null);
        dateFrom.getJFormattedTextField().setText("");
        dateTo.getModel().setValue(null);
        dateTo.getJFormattedTextField().setText("");
        transactionStatus.setSelectedIndex(-1);
        transactionId.setText("");
        apply.setEnabled(false);
    }

    public void setFilterPanelListener(FilterPanelListener filterPanelListener) {
        this.filterPanelListener = filterPanelListener;
    }

    public static class DateLabelFormatter extends JFormattedTextField.AbstractFormatter {
        private final String datePattern = "dd.MM.yyyy";
        private final SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern);

        @Override
        public Object stringToValue(String text) throws ParseException {
            return dateFormatter.parseObject(text);
        }

        @Override
        public String valueToString(Object value){
            if (value != null) {
                Calendar cal = (Calendar) value;
                return dateFormatter.format(cal.getTime());
            }
            return "";
        }
    }

}
