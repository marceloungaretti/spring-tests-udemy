package net.javaguides.springboot.repository;

import net.javaguides.springboot.model.Employee;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
public class EmployeeRepositoryTests {

    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee employee;

    @BeforeEach
    public void setup() {
        employee = Employee.builder()
                .firstName("Marcelo")
                .lastName("Ungaretti")
                .email("marcelo.ungaretti@gmail.com")
                .build();
    }

    @DisplayName("Junit test for save employee operation.")
    @Test
    public void givenEmployeeObject_whenSave_thenReturnSavedEmployee() {

        Employee savedEmployee = employeeRepository.save(employee);

        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getId()).isGreaterThan(0);
    }

    @DisplayName("Junit test for get all employees operation.")
    @Test
    public void givenEmployeesList_whenFindAll_thenEmployeeList() {
        Employee employee2 = Employee.builder()
                .firstName("João")
                .lastName("Silva")
                .email("joao.silva@gmail.com")
                .build();

        employeeRepository.save(employee);
        employeeRepository.save(employee2);

        List<Employee> employeesList = employeeRepository.findAll();

        assertThat(employeesList).isNotNull();
        assertThat(employeesList.size()).isEqualTo(2);
    }

    @DisplayName("Junit test for get employee by id operation.")
    @Test
    public void givenEmployeeObject_whenFindById_thenReturnEmployeeObject() {
        employeeRepository.save(employee);

        Employee employeeDB = employeeRepository.findById(employee.getId()).get();

        assertThat(employeeDB).isNotNull();
    }

    @DisplayName("Junit test for get employee by email operation")
    @Test
    public void givenEmployeeEmail_whenFindByEmail_thenReturnEmployeeObject() {
        employeeRepository.save(employee);

        Employee employeeDB = employeeRepository.findByEmail(employee.getEmail()).get();

        assertThat(employeeDB).isNotNull();
    }

    @DisplayName("Junit test for update employee operation")
    @Test
    public void givenEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmployee() {
        employeeRepository.save(employee);

        Employee savedEmployee = employeeRepository.findById(employee.getId()).get();
        savedEmployee.setEmail("mcainelli@teste.com");
        savedEmployee.setLastName("Cainelli");
        Employee updatedEmployee = employeeRepository.save(employee);

        assertThat(updatedEmployee.getEmail()).isEqualTo("mcainelli@teste.com");
        assertThat(updatedEmployee.getLastName()).isEqualTo("Cainelli");
    }

    @DisplayName("Junit test for delete employee operation")
    @Test
    public void givenEmployeeObject_whenDelete_thenRemoveEmployee() {
        employeeRepository.save(employee);

        employeeRepository.deleteById(employee.getId());

        Optional<Employee> employeeOptional = employeeRepository.findById(employee.getId());
        assertThat(employeeOptional).isEmpty();
    }

    @DisplayName("Junit test for custom JPQL query with index params")
    @Test
    public void givenFirstNameAndLastName_whenFindByJQPLIndexParams_thenReturnEmployeeObject() {
        employeeRepository.save(employee);
        String firstName = "Marcelo";
        String lastName = "Ungaretti";

        Employee savedEmployee = employeeRepository.findByJPQLWithIndexParams(firstName, lastName);

        assertThat(savedEmployee).isNotNull();
    }

    @DisplayName("Junit test for JPQL query with named params")
    @Test
    public void givenFirstNameAndLastName_whenFindByJQPLNamedParams_thenReturnEmployeeObject() {
        employeeRepository.save(employee);
        String firstName = "Marcelo";
        String lastName = "Ungaretti";

        Employee savedEmployee = employeeRepository.findByJPQLWithNamedParams(firstName, lastName);

        assertThat(savedEmployee).isNotNull();
    }

    @DisplayName("Junit test for custom SQL native query with index params")
    @Test
    public void givenFirstNameAndLastName_whenFindByNativeSqlWithIndexParams_thenReturnEmployeeObject() {
        employeeRepository.save(employee);

        Employee savedEmployee = employeeRepository.findByNativeSQLWithIndexParams(employee.getFirstName(), employee.getLastName());

        assertThat(savedEmployee).isNotNull();
    }

    @DisplayName("Junit test for custom SQL native query with named params")
    @Test
    public void givenFirstNameAndLastName_whenFindByNativeSqlWithNamedParams_thenReturnEmployeeObject() {
        employeeRepository.save(employee);

        Employee savedEmployee = employeeRepository.findByNativeSQLWithNamedParams(employee.getFirstName(), employee.getLastName());

        assertThat(savedEmployee).isNotNull();
    }
}
