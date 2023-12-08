import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.zimlewis.*;

public class App extends JFrame {


    String serverName = "localhost";
    String dbName = "ts00500_dinh_pham_le_hoang_asm";
    String url = "jdbc:sqlserver://" + serverName + ":1433;DatabaseName=" + dbName + ";encrypt=true;trustServerCertificate=true";
    String serverUser = "zimlewis";
    String severPassword = "Daylazim123";
    Connection connection;

    Object user_id = "";

    ArrayList<Student> studentsList = new ArrayList<Student>();
    ArrayList<Grade> gradesList = new ArrayList<Grade>();



    JTabbedPane mainTabbedPane = new JTabbedPane();
    GradeManagement gradeTab = new GradeManagement();
    LoginForm loginTab = new LoginForm();
    StudentManagement studentTab = new StudentManagement();

    

    static App app;

    public App(){
        connection = Zql.getConnection(url, serverUser, severPassword);
        System.out.println(connection);
        setSize(640 , 640);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Assignment");



        mainTabbedPane.setBounds(0, 0, getWidth() , getHeight());



        add(mainTabbedPane);
        
        mainTabbedPane.add("Login" , loginTab);
        mainTabbedPane.add("Student Management" , studentTab);
        mainTabbedPane.add("Grade Management" , gradeTab);

        mainTabbedPane.setEnabledAt(1 , false);
        mainTabbedPane.setEnabledAt(2 , false);
        mainTabbedPane.setEnabledAt(0 , true);
        

        setVisible(true);
        buttonSettup();

        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run(){
                try {
                    connection.close();
                }
                catch (Exception e) {

                }
            }
        });

        new Thread(() -> {
            while (true) {

                try {
                    // Update student
                    ArrayList<String> studentColumns = new ArrayList<>();
                    studentColumns.add("student_id");
                    studentColumns.add("student_name");
                    studentColumns.add("email");
                    studentColumns.add("phone_number");
                    studentColumns.add("gender");
                    studentColumns.add("adr");

                    ArrayList<Student> oldList = new ArrayList<>();
                    for (Student student : studentsList){
                        oldList.add(student);
                    }
                    ArrayList<Map<String, Object>> studentUpdateList = Zql.excuteQueryToArrayList(connection , "select * from students" , studentColumns);
                    studentsList = new ArrayList<Student>();

                    for (Map<String , Object> student : studentUpdateList){
                        byte[] imageData = null;
                        String getImageQuery = "select img from students where student_id = ?";

                        try (PreparedStatement statement = connection.prepareStatement(getImageQuery)){
                            statement.setString(1, student.get("student_id").toString());
                            try (ResultSet resultSet = statement.executeQuery()) {
                                if (resultSet.next()) {
                                    imageData = resultSet.getBytes("img");
                                } else {
                                    System.out.println("Image not found for ID: " + student.get("student_id").toString());
                                }
                            }
                        }
                        catch (Exception e){
                            System.out.println(e);
                        }


                        Student temp = new Student();
                        
                        temp.setId(student.get("student_id").toString());
                        temp.setName(student.get("student_name").toString());
                        temp.setEmail(student.get("email").toString());
                        temp.setPhoneNumber(student.get("phone_number").toString());
                        temp.setAddress(student.get("adr").toString());
                        temp.setMale((boolean) student.get("gender"));
                        temp.setImg(imageData);


                        studentsList.add(temp);
                    }

                    if (!areListsEqual(studentsList , oldList)){
                        studentTab.tableModel.setRowCount(0);
                        for (Student student : studentsList){
                            studentTab.tableModel.addRow(new Object[]{
                                student.getId(),
                                student.getName(),
                                student.getEmail(),
                                student.getPhoneNumber(),
                                student.isMale()?"Male":"Female",
                                student.getAddress()
                            });
                        }
                    }
                    // for (int i = 0 ; i < studentsList.size() ; i++){
                    //     System.out.printf("old list %d: %s, new list %d: %s\n" , i , oldList.get(i).getId() , i , studentsList.get(i).getId());
                    // }
                    // Update grade
                    ArrayList<String> gradeColumns = new ArrayList<>();
                    gradeColumns.add("student_id");
                    gradeColumns.add("student_name");
                    gradeColumns.add("english");
                    gradeColumns.add("it");
                    gradeColumns.add("ce");

                    ArrayList<Grade> oldGradeList = new ArrayList<>();
                    for (Grade grade : gradesList){
                        oldGradeList.add(grade);
                    }

                    ArrayList<Map<String, Object>> gradeUpdateList = Zql.excuteQueryToArrayList(connection , "select b.student_id, a.student_name, b.it, b.ce, b.english , (b.it + b.ce + b.english) / 3 as average from students a join grade b on a.student_id = b.student_id order by average desc" , gradeColumns);
                    
                    gradesList = new ArrayList<Grade>();

                    for (Map<String , Object> grade : gradeUpdateList){
                        Grade temp = new Grade();
                        temp.setId(grade.get("student_id").toString());
                        temp.setName(grade.get("student_name").toString());
                        temp.setIt((int) grade.get("it"));
                        temp.setCe((int) grade.get("ce"));
                        temp.setEnglish((int) grade.get("english"));

                        gradesList.add(temp);
                    }

                    if (!areListsEqual(gradesList , oldGradeList)){
                        gradeTab.tableModel.setRowCount(0);
                        for (Grade grade : gradesList){
                            gradeTab.tableModel.addRow(new Object[]{
                                grade.getId(),
                                grade.getName(),
                                grade.getEnglish(),
                                grade.getCe(),
                                grade.getIt(),
                                grade.getAverage()
                            });
                        }
                        gradeTab.tableModel.setRowCount(Math.min(3 , gradesList.size()));

                    }



                    Thread.sleep(100); // Example: Pause for 1 minute
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
    
    private static <T> boolean areListsEqual(List<T> list1, List<T> list2) {
        if (list1.size() != list2.size()) {
            return false;
        }

        for (int i = 0; i < list1.size(); i++) {
            if (!list1.get(i).equals(list2.get(i))) {
                return false;
            }
        }

        return true;
    }

    public void deleteAllSelection(){
        studentTab.idField.setText("");
        studentTab.nameField.setText("");
        studentTab.emailField.setText("");
        studentTab.phoneField.setText("");
        studentTab.genderComboBox.setSelectedIndex(0);;
        studentTab.addressArea.setText("");
        studentTab.setImageData(null);

        studentTab.studentTable.setRowSelectionInterval(-1, -1);
    }

    public void login(){
        try{
            ArrayList<String> columns = new ArrayList<String>();
            columns.add("username");
            columns.add("pass");
            columns.add("rol");
            String username = loginTab.usernameField.getText().trim();
            String password = loginTab.passwordField.getText().trim();
            String query = "select * from users where username = '%s' and pass = '%s'";
            ArrayList<Map<String , Object>> results = Zql.excuteQueryToArrayList(connection , String.format(query, username , password) , columns);


            if (results.size() >= 1) {
                user_id = results.get(0).get("username");
                
                mainTabbedPane.setEnabledAt(1 , true);
                mainTabbedPane.setEnabledAt(2 , true);
                mainTabbedPane.setEnabledAt(0 , false);

                mainTabbedPane.setSelectedIndex(1);
                JOptionPane.showMessageDialog(null , "Login successful as " + username + "!");
            }
            else {
                JOptionPane.showMessageDialog(null , "Login failed!");
            }
        }
        catch (Exception exception){
            System.err.println(exception);
        }
    }

    public void buttonSettup(){
        
        // Loin
        loginTab.onLoginPressed.connectSignal((parameters) -> {
            login();
        });



        // Student
        studentTab.onTableChangeSelection.connectSignal((parameters) -> {
            Integer selectedRow = (Integer) parameters[0];

            if (!(selectedRow >= 0 && selectedRow < studentTab.tableModel.getRowCount())){
                return;
            }

            String selectedID = (String) studentTab.tableModel.getValueAt(selectedRow, 0);
            Student selectedStudent = new Student();
            
            for (Student student : studentsList){
                if (student.getId().equals(selectedID)){
                    selectedStudent = student;
                    continue;
                }
            }


            
            studentTab.idField.setText(selectedStudent.getId());
            studentTab.nameField.setText(selectedStudent.getName());
            studentTab.emailField.setText(selectedStudent.getEmail());
            studentTab.phoneField.setText(selectedStudent.getPhoneNumber());
            studentTab.genderComboBox.setSelectedIndex(selectedStudent.isMale()?0:1);;
            studentTab.addressArea.setText(selectedStudent.getAddress());
            studentTab.setImageData(selectedStudent.getImg());


        });

        studentTab.onSaveButtonPressed.connectSignal((parameters) -> {
            ActionEvent action = (ActionEvent) parameters[0];
           
            String roleQueryCheck = String.format("select rol from users where username = '%s'", user_id);
            ArrayList<String> roleColumn = new ArrayList<>();
            roleColumn.add("rol");
            
            if (!Zql.excuteQueryToArrayList(connection, roleQueryCheck, roleColumn).get(0).get("rol").equals("Cán bộ đào tạo")){
                JOptionPane.showMessageDialog(null, "You don't have this permision!");
                return;
            }

            String _id = studentTab.idField.getText().trim();
            String _name = studentTab.nameField.getText().trim();
            String _email = studentTab.emailField.getText().trim();
            String _phone = studentTab.phoneField.getText().trim();
            Integer _isMale = studentTab.genderComboBox.getSelectedItem() == "Male" ? 1:0;
            String _address = studentTab.addressArea.getText();

            for (Student i : studentsList){
                if (i.getId().equals(_id)){
                    JOptionPane.showMessageDialog(null, "There's alrady a student with the ID: " + _id);
                    return;
                }
            }

            if (!InputValidation.isNameValid(_name)){
                JOptionPane.showMessageDialog(null, "Invalid name!");
                return;
            }

            if (!InputValidation.isEmailValid(_email)){
                JOptionPane.showMessageDialog(null, "Invalid mail!");
                return;
            }

            if (!InputValidation.isPhoneValid(_phone)){
                JOptionPane.showMessageDialog(null, "Invalid phone!");
                return;
            }

            if (studentTab.imageData == null){
                JOptionPane.showMessageDialog(null, "Student must have an image!");
                return;
            }

            String addQuery = String.format(
                "insert into students(student_id , student_name , email , phone_number , gender , adr) values ('%s' , N'%s' , '%s' , '%s' , %d , N'%s')", 
                _id , _name , _email , _phone , _isMale , _address
            );

            Zql.excuteQueryToArrayList(connection, addQuery, new ArrayList<String>());

            String updateQuery = "update students set img = ? where student_id = ?";

            try (PreparedStatement statement = connection.prepareStatement(updateQuery)){
                statement.setString(2 , _id);
                statement.setBytes(1, studentTab.imageData);
                statement.executeUpdate();
            }
            catch (Exception e){
                System.out.println(e);
            }

            deleteAllSelection();
        });

        studentTab.onNewButtonPressed.connectSignal((parameters) -> {
           ActionEvent action = (ActionEvent) parameters[0];
           
           deleteAllSelection();

        });

        studentTab.onUpdateButtonPressed.connectSignal((parameters) -> {
            ActionEvent action = (ActionEvent) parameters[0];
           
            String roleQueryCheck = String.format("select rol from users where username = '%s'", user_id);
            ArrayList<String> roleColumn = new ArrayList<>();
            roleColumn.add("rol");
            
            if (!Zql.excuteQueryToArrayList(connection, roleQueryCheck, roleColumn).get(0).get("rol").equals("Cán bộ đào tạo")){
                JOptionPane.showMessageDialog(null, "You don't have this permision!");
                return;
            }

            String selectedID = (String) studentTab.tableModel.getValueAt(studentTab.studentTable.getSelectedRow(), 0);

            String _name = studentTab.nameField.getText().trim();
            String _email = studentTab.emailField.getText().trim();
            String _phone = studentTab.phoneField.getText().trim();
            Integer _isMale = studentTab.genderComboBox.getSelectedItem() == "Male" ? 1:0;
            String _address = studentTab.addressArea.getText().trim();

            String updateQuery = "update students set";

            if (InputValidation.isNameValid(_name)){
                updateQuery += " student_name = '" + _name + "',";
            }

            if (InputValidation.isEmailValid(_email)){
                updateQuery += " email = '" + _email + "',";
            }

            if (InputValidation.isPhoneValid(_phone)){
                updateQuery += " phone_number = '" + _phone + "',";
            }

            if (!_address.equals("")){
                updateQuery += " adr = '" + _address + "',";
            }
            
            updateQuery += " gender = " + Integer.toString(_isMale);

            updateQuery += " where student_id = '" + selectedID + "'"; 


            Zql.excuteQueryToArrayList(connection, updateQuery, new ArrayList<String>());

            if (studentTab.imageData != null){
                String updateImageQuery = "update students set img = ? where student_id = ?";

                try (PreparedStatement statement = connection.prepareStatement(updateImageQuery)){
                    statement.setString(2 , selectedID);
                    statement.setBytes(1, studentTab.imageData);
                    statement.executeUpdate();
                }
                catch (Exception e){
                    System.out.println(e);
                }
            }

            deleteAllSelection();
        });

        studentTab.onDeleteButtonPressed.connectSignal((parameters) -> {
            ActionEvent action = (ActionEvent) parameters[0];
            
            String roleQueryCheck = String.format("select rol from users where username = '%s'", user_id);
            ArrayList<String> roleColumn = new ArrayList<>();
            roleColumn.add("rol");
            
            if (!Zql.excuteQueryToArrayList(connection, roleQueryCheck, roleColumn).get(0).get("rol").equals("Cán bộ đào tạo")){
                JOptionPane.showMessageDialog(null, "You don't have this permision!");
                return;
            }

            String selectedID = (String) studentTab.tableModel.getValueAt(studentTab.studentTable.getSelectedRow(), 0);

            String deleteGradeQuery = String.format("delete from grade where student_id = '%s'" , selectedID);
            String deleteStudentQuery = String.format("delete from students where student_id = '%s'" , selectedID);

            Zql.excuteQueryToArrayList(connection, deleteGradeQuery, new ArrayList<>());
            Zql.excuteQueryToArrayList(connection, deleteStudentQuery, new ArrayList<>());

            deleteAllSelection();
        });

        studentTab.onImageButtonPressed.connectSignal((p) -> {
            ActionEvent action = (ActionEvent) p[0];

            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();

                try {
                    Path imagePath = selectedFile.toPath();
                    studentTab.setImageData(Files.readAllBytes(imagePath));
                } catch (IOException e) {
                    e.printStackTrace();
                    // Handle the exception
                }
            } else {
                // User canceled the file selection
                return;
            }
        });

        // Grade
        gradeTab.onTableChangeSelection.connectSignal((parameters) -> {
            Integer selectedRow = (Integer) parameters[0];
            if (!(selectedRow < 3 && selectedRow >= 0)) return;
            String selectedID = (String) gradeTab.tableModel.getValueAt(selectedRow, 0);
            Grade selectedGrade = new Grade();
            
            for (Grade grade : gradesList){
                if (grade.getId().equals(selectedID)){
                    selectedGrade = grade;
                    continue;
                }
            }
            
            gradeTab.selectedId = selectedGrade.getId();

            gradeTab.idField.setText(selectedGrade.getId());
            gradeTab.fieldPanel.setBorder(new TitledBorder(selectedGrade.getName() + " - " + gradeTab.selectedId));;
            gradeTab.englishField.setText(Integer.toString(selectedGrade.getEnglish()));
            gradeTab.ceField.setText(Integer.toString(selectedGrade.getCe()));
            gradeTab.itField.setText(Integer.toString(selectedGrade.getIt()));
            gradeTab.averageValueLabel.setText(Integer.toString(selectedGrade.getAverage()));

        });

        gradeTab.onFirstButtonPressed.connectSignal((parameters) -> {
            ActionEvent action = (ActionEvent) parameters[0];
            Integer selectedRow = 0;
            if (selectedRow < gradeTab.tableModel.getRowCount() && selectedRow >= 0){
                gradeTab.student3Table.setRowSelectionInterval(selectedRow, selectedRow);
            }
            else{
                
                Grade selectedGrade = gradesList.get(selectedRow);
                gradeTab.selectedId = selectedGrade.getId();
                gradeTab.idField.setText(selectedGrade.getId());
                gradeTab.fieldPanel.setBorder(new TitledBorder(selectedGrade.getName() + " - " + gradeTab.selectedId));;
                gradeTab.englishField.setText(Integer.toString(selectedGrade.getEnglish()));
                gradeTab.ceField.setText(Integer.toString(selectedGrade.getCe()));
                gradeTab.itField.setText(Integer.toString(selectedGrade.getIt()));
                gradeTab.averageValueLabel.setText(Integer.toString(selectedGrade.getAverage()));
                gradeTab.student3Table.clearSelection();
            }
            
        });

        gradeTab.onLastButtonPressed.connectSignal((parameters) -> {
            ActionEvent action = (ActionEvent) parameters[0];
            Integer selectedRow = gradesList.size() - 1;
            if (selectedRow < gradeTab.tableModel.getRowCount() && selectedRow >= 0){
                gradeTab.student3Table.setRowSelectionInterval(selectedRow, selectedRow);
            }
            else{
                Grade selectedGrade = gradesList.get(selectedRow);
                gradeTab.selectedId = selectedGrade.getId();
                gradeTab.idField.setText(selectedGrade.getId());
                gradeTab.fieldPanel.setBorder(new TitledBorder(selectedGrade.getName() + " - " + gradeTab.selectedId));;
                gradeTab.englishField.setText(Integer.toString(selectedGrade.getEnglish()));
                gradeTab.ceField.setText(Integer.toString(selectedGrade.getCe()));
                gradeTab.itField.setText(Integer.toString(selectedGrade.getIt()));
                gradeTab.averageValueLabel.setText(Integer.toString(selectedGrade.getAverage()));
                gradeTab.student3Table.clearSelection();
            }
        });

        gradeTab.onNextButtonPressed.connectSignal((parameters) -> {
            ActionEvent action = (ActionEvent) parameters[0];
            Integer selectedRow = 0;
            for (Grade grade : gradesList){
                if (grade.getId().equals(gradeTab.selectedId)){
                    selectedRow = gradesList.indexOf(grade);
                }
            }
            selectedRow = Math.max(0 , Math.min(gradesList.size() - 1 , selectedRow + 1));
            if (selectedRow < gradeTab.tableModel.getRowCount() && selectedRow >= 0){
                gradeTab.student3Table.setRowSelectionInterval(selectedRow, selectedRow);
            }
            else{
                Grade selectedGrade = gradesList.get(selectedRow);
                gradeTab.selectedId = selectedGrade.getId();
                gradeTab.idField.setText(selectedGrade.getId());
                gradeTab.fieldPanel.setBorder(new TitledBorder(selectedGrade.getName() + " - " + gradeTab.selectedId));;
                gradeTab.englishField.setText(Integer.toString(selectedGrade.getEnglish()));
                gradeTab.ceField.setText(Integer.toString(selectedGrade.getCe()));
                gradeTab.itField.setText(Integer.toString(selectedGrade.getIt()));
                gradeTab.averageValueLabel.setText(Integer.toString(selectedGrade.getAverage()));
                gradeTab.student3Table.clearSelection();
            }
        });

        gradeTab.onPreviousButtonPressed.connectSignal((parameters) -> {
            ActionEvent action = (ActionEvent) parameters[0];
            Integer selectedRow = 0;
            for (Grade grade : gradesList){
                if (grade.getId().equals(gradeTab.selectedId)){
                    selectedRow = gradesList.indexOf(grade);
                    continue;
                }
            }
            selectedRow = Math.max(0 , Math.min(gradesList.size() - 1 , selectedRow - 1));
            if (selectedRow < gradeTab.tableModel.getRowCount() && selectedRow >= 0){
                gradeTab.student3Table.setRowSelectionInterval(selectedRow, selectedRow);
            }
            else{
                Grade selectedGrade = gradesList.get(selectedRow);
                gradeTab.selectedId = selectedGrade.getId();
                gradeTab.idField.setText(selectedGrade.getId());
                gradeTab.fieldPanel.setBorder(new TitledBorder(selectedGrade.getName() + " - " + gradeTab.selectedId));;
                gradeTab.englishField.setText(Integer.toString(selectedGrade.getEnglish()));
                gradeTab.ceField.setText(Integer.toString(selectedGrade.getCe()));
                gradeTab.itField.setText(Integer.toString(selectedGrade.getIt()));
                gradeTab.averageValueLabel.setText(Integer.toString(selectedGrade.getAverage()));
                gradeTab.student3Table.clearSelection();
            }
        });
    
        gradeTab.onSearchButtonPressed.connectSignal((parameters) -> {
            ActionEvent action = (ActionEvent) parameters[0];
            Integer selectedRow = -1;
            for (Grade grade : gradesList){
                if (grade.getId().equals(gradeTab.searchField.getText())){
                    selectedRow = gradesList.indexOf(grade);
                }
            }
            selectedRow = Math.max(0 , Math.min(gradesList.size() - 1 , selectedRow));
            if (selectedRow < gradeTab.tableModel.getRowCount() && selectedRow >= 0){
                gradeTab.student3Table.setRowSelectionInterval(selectedRow, selectedRow);
            }
            else{
                Grade selectedGrade = gradesList.get(selectedRow);
                gradeTab.selectedId = selectedGrade.getId();
                gradeTab.idField.setText(selectedGrade.getId());
                gradeTab.fieldPanel.setBorder(new TitledBorder(selectedGrade.getName() + " - " + gradeTab.selectedId));;
                gradeTab.englishField.setText(Integer.toString(selectedGrade.getEnglish()));
                gradeTab.ceField.setText(Integer.toString(selectedGrade.getCe()));
                gradeTab.itField.setText(Integer.toString(selectedGrade.getIt()));
                gradeTab.averageValueLabel.setText(Integer.toString(selectedGrade.getAverage()));
                gradeTab.student3Table.clearSelection();
            }
        });
        
        gradeTab.onNewButtonPressed.connectSignal((parameters) -> {
            ActionEvent action = (ActionEvent) parameters[0];
           
            String roleQueryCheck = String.format("select rol from users where username = '%s'", user_id);
            ArrayList<String> roleColumn = new ArrayList<>();
            roleColumn.add("rol");
            
            if (!Zql.excuteQueryToArrayList(connection, roleQueryCheck, roleColumn).get(0).get("rol").equals("Giảng viên")){
                JOptionPane.showMessageDialog(null, "You don't have this permision!");
                return;
            }

            String _id = gradeTab.idField.getText().trim();
            Integer _english = Integer.parseInt(gradeTab.englishField.getText());
            Integer _it = Integer.parseInt(gradeTab.itField.getText());
            Integer _ce = Integer.parseInt(gradeTab.ceField.getText());

            
            for (Grade i : gradesList){
                if (i.getId().equals(_id)){
                    JOptionPane.showMessageDialog(null, "There's alrady a grade for with the ID: " + _id);
                    return;
                }
            }

            Boolean hasThatStudent = false;
            for (Student i : studentsList){
                if (i.getId().equals(_id)){
                    hasThatStudent = true;
                    continue;
                }
            }

            if (!hasThatStudent){
                JOptionPane.showMessageDialog(null, "There's no student with ID: " + _id);
                return;
            }
            //(temp < 0 || temp > 10)
            if (_it < 0 || _it > 10){
                JOptionPane.showMessageDialog(null, "Invalid IT score!");
                return;
            }

            if (_english < 0 || _english > 10){
                JOptionPane.showMessageDialog(null, "Invalid English score!");
                return;
            }

            if (_ce < 0 || _ce > 10){
                JOptionPane.showMessageDialog(null, "Invalid CE!");
                return;
            }

            String addQuery = String.format(
                "insert into grade(student_id , english , it , ce) values ('%s' , %d , %d , %d);", 
                _id , _english , _it , _ce
            );

            Zql.excuteQueryToArrayList(connection, addQuery, new ArrayList<String>());

        });

        gradeTab.onSaveButtonPressed.connectSignal((parameters) -> {
            ActionEvent action = (ActionEvent) parameters[0];
           

        });

        gradeTab.onUpdateButtonPressed.connectSignal((parameters) -> {
            ActionEvent action = (ActionEvent) parameters[0];
           
            String roleQueryCheck = String.format("select rol from users where username = '%s'", user_id);
            ArrayList<String> roleColumn = new ArrayList<>();
            roleColumn.add("rol");
            
            if (!Zql.excuteQueryToArrayList(connection, roleQueryCheck, roleColumn).get(0).get("role").equals("Giảng viên")){
                JOptionPane.showMessageDialog(null, "You don't have this permision!");
                return;
            }

            String _english = gradeTab.englishField.getText();
            String _it = gradeTab.itField.getText();
            String _ce = gradeTab.ceField.getText();

            String selectedID = gradeTab.selectedId;

            System.out.println(1);

            String updateQuery = "update grade set";

            if (InputValidation.isNumeric(_english)){
                Integer temp = Integer.parseInt(_english);
                if (temp < 0 || temp > 10){
                    
                    JOptionPane.showMessageDialog(null, "Invalid English!");
                    return;
                }
                updateQuery += " english = " + Integer.toString(temp) + ",";
            }

            if (InputValidation.isNumeric(_ce)){
                Integer temp = Integer.parseInt(_ce);
                if (temp < 0 || temp > 10){
                    JOptionPane.showMessageDialog(null, "Invalid CE!");
                    return;
                }
                updateQuery += " ce = " + Integer.toString(temp) + ",";
            }

            if (InputValidation.isNumeric(_it)){
                Integer temp = Integer.parseInt(_it);
                if (temp < 0 || temp > 10){
                    JOptionPane.showMessageDialog(null, "Invalid IT!");
                    return;
                }
                updateQuery += " it = " + Integer.toString(temp) + ",";
            }

            Integer index = updateQuery.lastIndexOf(",");
            updateQuery = updateQuery.substring(0, index);
            
            
            updateQuery += " where student_id = '" + selectedID + "'";
            System.out.println(updateQuery);
            // System.out.println(updateQuery);
            Zql.excuteQueryToArrayList(connection, updateQuery, new ArrayList<String>());
        });

        gradeTab.onDeleteButtonPressed.connectSignal((parameters) -> {
            ActionEvent action = (ActionEvent) parameters[0];
           
            String roleQueryCheck = String.format("select rol from users where username = '%s'", user_id);
            ArrayList<String> roleColumn = new ArrayList<>();
            roleColumn.add("rol");
            
            if (!Zql.excuteQueryToArrayList(connection, roleQueryCheck, roleColumn).get(0).get("rol").equals("Giảng viên")){
                JOptionPane.showMessageDialog(null, "You don't have this permision!");
                return;
            }

            String selectedID = gradeTab.selectedId;

            String deleteGradeQuery = String.format("delete from grade where student_id = '%s'" , selectedID);

            Zql.excuteQueryToArrayList(connection, deleteGradeQuery, new ArrayList<>());
        });

    }

    public static void main(String[] args) {
        // JDBC URL, username, and password of SQL Server
        //url = "jdbc:sqlserver://" +serverName + ":1433;DatabaseName=" + dbName + ";encrypt=true;trustServerCertificate=true;
        System.out.print("\033\143");

        app = new App();


    }
}