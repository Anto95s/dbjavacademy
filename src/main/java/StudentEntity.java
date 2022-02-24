public class StudentEntity {
    public String name;
    public String surname;
    public String department;
    public String drivingLicense;

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", department='" + department + '\'' +
                ", drivingLicense='" + drivingLicense + '\'' +
                '}';
    }

    public StudentEntity(String name, String surname, String department, String drivingLicense) {
        this.name = name;
        this.surname = surname;
        this.department = department;
        this.drivingLicense = drivingLicense;
    }
}
