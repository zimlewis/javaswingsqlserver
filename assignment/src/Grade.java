import java.util.Objects;

public class Grade {
    
    String id , name;
    int it , ce , english , average;


    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIt(int it) {
        this.it = it;
    }

    public void setCe(int ce) {
        this.ce = ce;
    }

    public void setEnglish(int english) {
        this.english = english;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getIt() {
        return it;
    }

    public int getCe() {
        return ce;
    }

    public int getEnglish() {
        return english;
    }

    public int getAverage(){
        return (it + ce + english) / 3;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Grade otherGrade = (Grade) obj;
        return Objects.equals(id, otherGrade.id) &&
               Objects.equals(name, otherGrade.name) &&
               it == otherGrade.it &&
               ce == otherGrade.ce &&
               english == otherGrade.english;

    }

    // Additional hashCode method is recommended when overriding equals
    @Override
    public int hashCode() {
        return Objects.hash(id, name, it, ce, english);
    }
}
