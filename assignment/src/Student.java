import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.Objects;

import javax.imageio.ImageIO;

import com.zimlewis.ByteToImage;

public class Student {
    
    String id , name , email , phoneNumber , address;
    boolean isMale;
    byte[] img;
    
    public byte[] getImg() {
        return img;
    }

    public void setImg(byte[] img) {
        this.img = img;
    }

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

        BufferedImage image1 = convertToBufferedImage(ByteToImage.convertBytesToImage(img));
        BufferedImage image2 = convertToBufferedImage(ByteToImage.convertBytesToImage(otherStudent.img));

        return Objects.equals(id, otherStudent.id) &&
               Objects.equals(name, otherStudent.name) &&
               Objects.equals(email, otherStudent.email) &&
               Objects.equals(phoneNumber, otherStudent.phoneNumber) &&
               Objects.equals(address, otherStudent.address) &&
               areImagesEqual(image1, image2) &&
               isMale == otherStudent.isMale;
    }

    // Additional hashCode method is recommended when overriding equals
    @Override
    public int hashCode() {
        return Objects.hash(id, name, email, phoneNumber, address, isMale);
    }

    private static BufferedImage convertToBufferedImage(Image image) {
        if (image instanceof BufferedImage) {
            return (BufferedImage) image; // If the image is already a BufferedImage, no need to convert
        }

        // Create a BufferedImage with the same dimensions as the Image
        BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        // Draw the Image onto the BufferedImage
        Graphics2D g2d = bufferedImage.createGraphics();
        g2d.drawImage(image, 0, 0, null);
        g2d.dispose();

        return bufferedImage;
    }

    private static boolean areImagesEqual(BufferedImage img1, BufferedImage img2) {
        // Check dimensions
        if (img1.getWidth() != img2.getWidth() || img1.getHeight() != img2.getHeight()) {
            return false;
        }

        // Check pixel values
        for (int y = 0; y < img1.getHeight(); y++) {
            for (int x = 0; x < img1.getWidth(); x++) {
                if (img1.getRGB(x, y) != img2.getRGB(x, y)) {
                    return false;
                }
            }
        }

        return true;
    }
}
