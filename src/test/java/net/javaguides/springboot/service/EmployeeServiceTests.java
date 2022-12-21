package net.javaguides.springboot.service;

import net.javaguides.springboot.exception.ResourceNotFoundException;
import net.javaguides.springboot.model.Employee;
import net.javaguides.springboot.repository.EmployeeRepository;
import net.javaguides.springboot.service.impl.EmployeeServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

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

        Assertions.assertThat(savedEmployee).isNotNull();
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

        Assertions.assertThat(employeeList).isNotNull();
        Assertions.assertThat(employeeList.size()).isEqualTo(2);
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

        Assertions.assertThat(employeeList).isEmpty();
    }
}
