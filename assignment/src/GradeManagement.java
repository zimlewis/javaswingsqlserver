import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import com.zimlewis.Signal;

public class GradeManagement extends JPanel {
    JTable gradeTable;
    JTextField idField, englishField, itField, ceField, searchField;
    JButton searchButton , newButton , saveButton , deleteButton , updateButton;
    JButton firstControl , lastControl , previousControl , nextControl;
    JLabel averageValueLabel;
    DefaultTableModel tableModel;
    JTable student3Table;

    String selectedId;


    Signal onTableChangeSelection = new Signal(Integer.class, Integer.class);
    Signal onNextButtonPressed = new Signal(ActionEvent.class);
    Signal onPreviousButtonPressed = new Signal(ActionEvent.class);
    Signal onFirstButtonPressed = new Signal(ActionEvent.class);
    Signal onLastButtonPressed = new Signal(ActionEvent.class);
    Signal onSearchButtonPressed = new Signal(ActionEvent.class);


    Signal onNewButtonPressed = new Signal(ActionEvent.class);
    Signal onDeleteButtonPressed = new Signal(ActionEvent.class);
    Signal onSaveButtonPressed = new Signal(ActionEvent.class);
    Signal onUpdateButtonPressed = new Signal(ActionEvent.class);

    JPanel fieldPanel;




    public GradeManagement() {
        setBorder(new TitledBorder("Grade Management"));

        setLayout(null);

        tableModel = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                // Make all cells uneditable
                return false;
            }
        };
        tableModel.addColumn("id");
        tableModel.addColumn("name");
        tableModel.addColumn("English");
        tableModel.addColumn("CE");
        tableModel.addColumn("IT");
        tableModel.addColumn("Average");






        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(null);
        searchPanel.setBorder(new TitledBorder("Search"));
        searchPanel.setBounds(20 , 30 , 600 , 100);
        add(searchPanel);
        
        JLabel searchLabel = new JLabel("search");
        searchLabel.setBounds(20 , 40 , 50 , 20);
        searchPanel.add(searchLabel);        

        
        searchField = new JTextField();
        searchField.setBounds(90 , 40 , 380 , 20);
        searchPanel.add(searchField);

        searchButton = new JButton("Search");
        searchButton.setBounds(490 , 40 , 100 , 20);
        searchPanel.add(searchButton);


        JPanel actionPanel = new JPanel();
        actionPanel.setBorder(new TitledBorder("Action"));
        actionPanel.setLayout(null);
        actionPanel.setBounds(440 , 130 , 180 , 290);
        add(actionPanel);

        newButton = new JButton("New");
        newButton.setBounds(20, 20, 140, 50);
        actionPanel.add(newButton);

        updateButton = new JButton("Update");
        updateButton.setBounds(20, 90, 140, 50);
        actionPanel.add(updateButton);

        deleteButton = new JButton("Delete");
        deleteButton.setBounds(20, 160, 140, 50);
        actionPanel.add(deleteButton);

        saveButton = new JButton("Save");
        saveButton.setBounds(20, 230, 140, 50);
        actionPanel.add(saveButton);


        
        fieldPanel = new JPanel();
        fieldPanel.setBorder(new TitledBorder(""));
        fieldPanel.setLayout(null);
        fieldPanel.setBounds(20 , 130 , 400 , 200);
        add(fieldPanel);

        JLabel idLabel = new JLabel("ID: ");
        idLabel.setBounds(20, 20, 100, 20);
        fieldPanel.add(idLabel);
        idField = new JTextField();
        idField.setBounds(120, 20, 200, 20);
        fieldPanel.add(idField);

        JLabel englishLabel = new JLabel("English: ");
        englishLabel.setBounds(20, 50, 100, 20);
        fieldPanel.add(englishLabel);
        englishField = new JTextField();
        englishField.setBounds(120, 50, 200, 20);
        fieldPanel.add(englishField);

        JLabel ceLabel = new JLabel("CE: ");
        ceLabel.setBounds(20, 80, 100, 20);
        fieldPanel.add(ceLabel);
        ceField = new JTextField();
        ceField.setBounds(120, 80, 200, 20);
        fieldPanel.add(ceField);

        JLabel itLabel = new JLabel("IT: ");
        itLabel.setBounds(20, 110, 100, 20);
        fieldPanel.add(itLabel);
        itField = new JTextField();
        itField.setBounds(120, 110, 200, 20);
        fieldPanel.add(itField);
        
        JLabel averageLabel = new JLabel("Average: ");
        averageLabel.setBounds(20, 140, 100, 20);
        fieldPanel.add(averageLabel);
        averageValueLabel = new JLabel("0");
        averageValueLabel.setBounds(120, 140, 100, 50);
        averageValueLabel.setFont(new Font("Serif", Font.PLAIN, 50));
        averageValueLabel.setForeground(Color.BLUE);
        fieldPanel.add(averageValueLabel);



        JPanel controlJPanel = new JPanel();
        controlJPanel.setBorder(new TitledBorder("Control"));
        controlJPanel.setLayout(null);
        controlJPanel.setBounds(20 , 330 , 400 , 90);
        add(controlJPanel);

        int gap = 20;
        firstControl = new JButton("<<");
        firstControl.setBounds(30 + gap, 20, 60, 60);
        controlJPanel.add(firstControl);

        previousControl = new JButton("<");
        previousControl.setBounds(110 + gap, 20, 60, 60);
        controlJPanel.add(previousControl);

        nextControl = new JButton(">");
        nextControl.setBounds(190 + gap, 20, 60, 60);
        controlJPanel.add(nextControl);

        lastControl = new JButton(">>");
        lastControl.setBounds(270 + gap, 20, 60, 60);
        controlJPanel.add(lastControl);


        JPanel tablePanel = new JPanel();
        tablePanel.setBorder(new TitledBorder("top 3 sv manh nhat"));
        tablePanel.setBounds(20 , 420 , 600 , 140);
        add(tablePanel);
        
        student3Table = new JTable(tableModel);
        student3Table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        student3Table.getTableHeader().setReorderingAllowed(false);
        JScrollPane scrollPane = new JScrollPane(student3Table);
        scrollPane.setPreferredSize(new Dimension(550, 110));
        tablePanel.add(scrollPane);


        firstControl.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onFirstButtonPressed.emitSignal(e);
            }
        });

        lastControl.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onLastButtonPressed.emitSignal(e);
            }
        });

        nextControl.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onNextButtonPressed.emitSignal(e);
            }
        });

        previousControl.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onPreviousButtonPressed.emitSignal(e);
            }
        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onSearchButtonPressed.emitSignal(e);
            }
        });

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

        student3Table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    // Get the selected row index
                    int selectedRow = student3Table.getSelectedRow();
                    int selectedCollumn = student3Table.getSelectedColumn();

                    onTableChangeSelection.emitSignal(selectedRow , selectedCollumn);
                }
            }
        });

    }

}
