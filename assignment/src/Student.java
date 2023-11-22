import java.util.Objects;

public class Student {
    
    String id , name , email , phoneNumber , address , image;
    boolean isMale;
    
    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setMale(boolean isMale) {
        this.isMale = isMale;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public String getImage() {
        return image;
    }

    public boolean isMale() {
        return isMale;
    }

    public Student(){

    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Student otherStudent = (Student) obj;
        return Objects.equals(id, otherStudent.id) &&
               Objects.equals(name, otherStudent.name) &&
               Objects.equals(email, otherStudent.email) &&
               Objects.equals(phoneNumber, otherStudent.phoneNumber) &&
               Objects.equals(address, otherStudent.address) &&
               Objects.equals(image, otherStudent.image) &&
               isMale == otherStudent.isMale;
    }

    // Additional hashCode method is recommended when overriding equals
    @Override
    public int hashCode() {
        return Objects.hash(id, name, email, phoneNumber, address, image, isMale);
    }
}
