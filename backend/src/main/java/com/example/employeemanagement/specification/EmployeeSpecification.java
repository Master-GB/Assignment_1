package com.example.employeemanagement.specification;

import com.example.employeemanagement.entity.Employee;
import com.example.employeemanagement.entity.EmployeeStatus;
import org.springframework.data.jpa.domain.Specification;

public class EmployeeSpecification {

    private EmployeeSpecification() {
    }

    public static Specification<Employee> search(String search) {

        return (root, query, criteriaBuilder) -> {

            if (search == null || search.isBlank()) {
                return criteriaBuilder.conjunction();
            }

            String keyword = "%" + search.trim().toLowerCase() + "%";

            return criteriaBuilder.or(
                    criteriaBuilder.like(
                            criteriaBuilder.lower(
                                    root.get("employeeCode")
                            ),
                            keyword
                    ),

                    criteriaBuilder.like(
                            criteriaBuilder.lower(
                                    root.get("firstName")
                            ),
                            keyword
                    ),

                    criteriaBuilder.like(
                            criteriaBuilder.lower(
                                    root.get("lastName")
                            ),
                            keyword
                    ),

                    criteriaBuilder.like(
                            criteriaBuilder.lower(
                                    root.get("nic")
                            ),
                            keyword
                    ),

                    criteriaBuilder.like(
                            criteriaBuilder.lower(
                                    root.get("email")
                            ),
                            keyword
                    )
            );
        };
    }

    public static Specification<Employee> hasStatus(
            EmployeeStatus status) {

        return (root, query, criteriaBuilder) -> {

            if (status == null) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.equal(
                    root.get("status"),
                    status
            );
        };
    }

    // public static Specification<Employee> hasDesignation(
    //         Long designationId) {

    //     return (root, query, criteriaBuilder) -> {

    //         if (designationId == null) {
    //             return criteriaBuilder.conjunction();
    //         }

    //         return criteriaBuilder.equal(
    //                 root.get("designation").get("id"),
    //                 designationId
    //         );
    //     };
    // }
}