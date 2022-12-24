package net.javaguides.springboot.service;

import net.javaguides.springboot.exception.ResourceNotFoundException;
import net.javaguides.springboot.model.Employee;
import net.javaguides.springboot.repository.EmployeeRepository;
import net.javaguides.springboot.service.impl.EmployeeServiceImpl;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTests {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee employee;

    @BeforeEach
    public void setup(){
        employee = Employee.builder()
                .id(1L)
                .firstName("Marcelo")
                .lastName("Cainelli")
                .email("marcelo_ungaretti@hotmail.com")
                .build();
    }

    @DisplayName("Junit test for save employee method.")
    @Test
    public void givenEmployeeObject_whenSaveEmployee_thenReturnEmployeeObject(){
        given(employeeRepository.findByEmail(employee.getEmail())).willReturn(Optional.empty());
        given(employeeRepository.save(employee)).willReturn(employee);
        Employee savedEmployee = employeeService.saveEmployee(employee);

        assertThat(savedEmployee).isNotNull();
    }

    @DisplayName("Junit test for save employee method which throws exception.")
    @Test
    public void givenExistingEmail_whenSaveEmployee_thenThrowsException(){
        given(employeeRepository.findByEmail(employee.getEmail())).willReturn(Optional.of(employee));

        org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, () -> {
           employeeService.saveEmployee(employee);
        });

        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @DisplayName("Junit test for getAllEmployees method")
    @Test
    public void givenEmployees_whenGetAllEmployees_thenReturnEmployeesList(){
        Employee employee2 = Employee.builder()
                .id(2L)
                .firstName("Fulano")
                .lastName("Da Silva")
                .email("fulano_silva@hotmail.com")
                .build();

        given(employeeRepository.findAll()).willReturn(List.of(employee, employee2));

        List<Employee> employeeList = employeeService.getAllEmployees();

        assertThat(employeeList).isNotNull();
        assertThat(employeeList.size()).isEqualTo(2);
    }

    @DisplayName("Junit test for getAllEmployees method (negative scenario)")
    @Test
    public void givenEmptyEmployeesList_whenGetAllEmployees_thenReturnEmptyEmployeesList(){
        Employee employee2 = Employee.builder()
                .id(2L)
                .firstName("Fulano")
                .lastName("Da Silva")
                .email("fulano_silva@hotmail.com")
                .build();

        given(employeeRepository.findAll()).willReturn(Collections.emptyList());

        List<Employee> employeeList = employeeService.getAllEmployees();

        assertThat(employeeList).isEmpty();
    }

    @DisplayName("Junit test for getEmployeeById method")
    @Test
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject(){
        given(employeeRepository.findById(1L)).willReturn(Optional.of(employee));

        Employee savedEmployee = employeeService.getEmployeeById(employee.getId()).get();

        assertThat(savedEmployee).isNotNull();
    }

    @DisplayName("Junit test for updateEmployee method")
    @Test
    public void givenEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmployee(){
        given(employeeRepository.save(employee)).willReturn(employee);
        employee.setEmail("mcainelli@emailatualizado.com");
        employee.setFirstName("Novo Marcelo");

        Employee updatedEmployee = employeeService.updateEmployee(employee);

        assertThat(updatedEmployee.getEmail()).isEqualTo("mcainelli@emailatualizado.com");
        assertThat(updatedEmployee.getFirstName()).isEqualTo("Novo Marcelo");
    }

    @DisplayName("Junit test for deleteEmployee method")
    @Test
    public void givenEmployeeId_whenDeleteEmployee_thenVoid(){
        long employeeId = 1L;
        BDDMockito.willDoNothing().given(employeeRepository).deleteById(employeeId);

        employeeService.deleteEmployee(employeeId);

        verify(employeeRepository, times(1)).deleteById(employeeId);
    }
}
