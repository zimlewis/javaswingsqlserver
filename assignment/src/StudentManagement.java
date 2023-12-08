import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import com.zimlewis.*;


public class StudentManagement extends JPanel{
    JTable studentTable;
    JTextField idField, nameField, emailField, phoneField;
    JComboBox<String> genderComboBox;
    JTextArea addressArea;
    Signal onTableChangeSelection = new Signal(Integer.class, Integer.class);
    JLabel imgLabel;
    byte[] imageData;
    

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;

        if (imageData == null){
            imgLabel.setIcon(null);
            return;
        }

        Image image = ByteToImage.convertBytesToImage(imageData);
        ImageIcon ic = new ImageIcon(image.getScaledInstance(150, 200, Image.SCALE_SMOOTH));
        imgLabel.setIcon(ic);
    }

    Signal onNewButtonPressed = new Signal(ActionEvent.class);
    Signal onDeleteButtonPressed = new Signal(ActionEvent.class);
    Signal onSaveButtonPressed = new Signal(ActionEvent.class);
    Signal onUpdateButtonPressed = new Signal(ActionEvent.class);
    Signal onImageButtonPressed = new Signal(ActionEvent.class);

    DefaultTableModel tableModel;

    public StudentManagement(){
        setBorder(new TitledBorder("Student"));



        tableModel = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                // Make all cells uneditable
                return false;
            }
        };;
        tableModel.addColumn("id");
        tableModel.addColumn("name");
        tableModel.addColumn("email");
        tableModel.addColumn("phone");
        tableModel.addColumn("gender");
        tableModel.addColumn("address");




        studentTable = new JTable(tableModel);
        studentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        studentTable.getTableHeader().setReorderingAllowed(false);
        studentTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    // Get the selected row index
                    int selectedRow = studentTable.getSelectedRow();
                    int selectedCollumn = studentTable.getSelectedColumn();

                    onTableChangeSelection.emitSignal(selectedRow , selectedCollumn);
                }
            }
        });
        JScrollPane tableScrollPane = new JScrollPane(studentTable);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(null);

        JLabel idLabel = new JLabel("ID: ");
        idLabel.setBounds(20, 20, 100, 20);
        idField = new JTextField();
        idField.setBounds(120, 20, 200, 20);


        JLabel nameLabel = new JLabel("Name: ");
        nameLabel.setBounds(20, 50, 100, 20);
        nameField = new JTextField();
        nameField.setBounds(120, 50, 200, 20);



        JLabel emailLabel = new JLabel("Email: ");
        emailLabel.setBounds(20, 80, 100, 20);
        emailField = new JTextField();
        emailField.setBounds(120, 80, 200, 20);


        JLabel phoneLabel = new JLabel("Phone:");
        phoneLabel.setBounds(20, 110, 100, 20);
        phoneField = new JTextField();
        phoneField.setBounds(120, 110, 200, 20);


        JLabel genderLabel = new JLabel("Gender:");
        genderLabel.setBounds(20, 140, 100, 20);
        String[] g = {"Male" , "Female"};
        genderComboBox = new JComboBox<String>(g);
        genderComboBox.setBounds(120, 140, 200, 20);

        JLabel addressLabel = new JLabel("Phone:");
        addressLabel.setBounds(20, 170, 100, 20);
        addressArea = new JTextArea();
        JScrollPane addressScroll = new JScrollPane(addressArea);
        addressScroll.setBounds(120, 170, 200, 50);

        JPanel imagePanel = new JPanel();
        imagePanel.setBorder(new TitledBorder("Image"));
        imagePanel.setBounds(320 , 0 , 170 , 230);
        inputPanel.add(imagePanel);

        imgLabel = new JLabel();
        imgLabel.setBounds(0 , 0 , 150 , 200);
        imagePanel.add(imgLabel);

        JButton imageButton = new JButton("Image");
        imageButton.setBounds(340 , 230 , 150 , 50);
        inputPanel.add(imageButton);
        

        inputPanel.add(idLabel);
        inputPanel.add(idField);

        inputPanel.add(nameLabel);
        inputPanel.add(nameField);

        inputPanel.add(emailLabel);
        inputPanel.add(emailField);

        inputPanel.add(phoneLabel);
        inputPanel.add(phoneField);

        inputPanel.add(genderLabel);
        inputPanel.add(genderComboBox);

        inputPanel.add(addressLabel);
        inputPanel.add(addressScroll);

        JButton newButton = new JButton("New");
        JButton saveButton = new JButton("Save");
        JButton deleteButton = new JButton("Delete");
        JButton updateButton = new JButton("Update");


        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(4, 1));
        buttonPanel.add(newButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(updateButton);

        tableScrollPane.setPreferredSize(new Dimension(500, 510));
        
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT , inputPanel , tableScrollPane);
        splitPane.setDividerLocation(300);

        
        add(splitPane, BorderLayout.SOUTH);
        add(buttonPanel, BorderLayout.EAST);
        
        
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                onSaveButtonPressed.emitSignal(e);
            }
        });

        newButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                onNewButtonPressed.emitSignal(e);
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                onUpdateButtonPressed.emitSignal(e);
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                onDeleteButtonPressed.emitSignal(e);

            }
        });

        imageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                onImageButtonPressed.emitSignal(e);
            }
        });
    }
}
