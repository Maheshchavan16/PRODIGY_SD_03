import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ContactManagerGUI extends JFrame implements ActionListener {
    
    private JTextField nameField, phoneField, emailField;
    private JTable contactTable;
    private DefaultTableModel tableModel;
    private JButton addButton, viewButton, editButton, deleteButton;
    private ContactList contactList;

    public ContactManagerGUI() {
        super("Contact Manager");

        
        nameField = new JTextField(25);
        phoneField = new JTextField(25);
        emailField = new JTextField(25);

       
        String[] columnNames = {"Name", "Phone", "Email"};
        tableModel = new DefaultTableModel(columnNames, 0);
        contactTable = new JTable(tableModel);
        contactTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        contactTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                fillFieldsFromSelectedRow();
            }
        });

        JScrollPane scrollPane = new JScrollPane(contactTable);

        addButton = new JButton("Add Contact");
        viewButton = new JButton("View Contacts");
        editButton = new JButton("Edit Contact");
        deleteButton = new JButton("Delete Contact");

        
        addButton.addActionListener(this);
        viewButton.addActionListener(this);
        editButton.addActionListener(this);
        deleteButton.addActionListener(this);

        
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        inputPanel.add(new JLabel("Name: "), gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        inputPanel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        inputPanel.add(new JLabel("Phone: "), gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        inputPanel.add(phoneField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        inputPanel.add(new JLabel("Email: "), gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        inputPanel.add(emailField, gbc);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(viewButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(inputPanel, gbc);

        gbc.gridy = 1;
        mainPanel.add(buttonPanel, gbc);

        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        mainPanel.add(scrollPane, gbc);

        
        add(mainPanel);

        
        contactList = new ContactList();

        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setVisible(true);
    }

  
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addButton) {
            if (validateInput()) {
                String name = nameField.getText();
                String phone = phoneField.getText();
                String email = emailField.getText();
                Contact contact = new Contact(name, phone, email);
                contactList.addContact(contact);
                clearFields();
                viewContacts();
                JOptionPane.showMessageDialog(this, "Contact added successfully.");
            }
        } else if (e.getSource() == viewButton) {
            viewContacts();
            JOptionPane.showMessageDialog(this, "Viewing contacts.");
        } else if (e.getSource() == editButton) {
            int selectedRow = contactTable.getSelectedRow();
            if (selectedRow >= 0) {
                String oldName = (String) tableModel.getValueAt(selectedRow, 0);
                if (validateInput()) {
                    String name = nameField.getText();
                    String phone = phoneField.getText();
                    String email = emailField.getText();
                    Contact contact = new Contact(name, phone, email);
                    contactList.updateContact(oldName, contact);
                    clearFields();
                    viewContacts();
                    JOptionPane.showMessageDialog(this, "Contact updated successfully.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a contact to edit.");
            }
        } else if (e.getSource() == deleteButton) {
            int confirmed = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete the selected contact?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
            if (confirmed == JOptionPane.YES_OPTION) {
                int selectedRow = contactTable.getSelectedRow();
                if (selectedRow >= 0) {
                    String name = (String) tableModel.getValueAt(selectedRow, 0);
                    contactList.deleteContact(name);
                    clearFields();
                    viewContacts();
                    JOptionPane.showMessageDialog(this, "Contact deleted successfully.");
                } else {
                    JOptionPane.showMessageDialog(this, "Please select a contact to delete.");
                }
            }
        }
    }
    

    private boolean validateInput() {
        if (nameField.getText().trim().isEmpty() || phoneField.getText().trim().isEmpty() || emailField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all the fields.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private void viewContacts() {
        List<Contact> contacts = contactList.getContacts();
        tableModel.setRowCount(0); // Clear existing data
        for (Contact contact : contacts) {
            tableModel.addRow(new Object[]{contact.getName(), contact.getPhone(), contact.getEmail()});
        }
    }

    private void fillFieldsFromSelectedRow() {
        int selectedRow = contactTable.getSelectedRow();
        if (selectedRow >= 0) {
            nameField.setText((String) tableModel.getValueAt(selectedRow, 0));
            phoneField.setText((String) tableModel.getValueAt(selectedRow, 1));
            emailField.setText((String) tableModel.getValueAt(selectedRow, 2));
        }
    }

    private void clearFields() {
        nameField.setText("");
        phoneField.setText("");
        emailField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ContactManagerGUI::new);
    }
}
