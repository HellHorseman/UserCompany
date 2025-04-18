package com.example.userservice.model;

import com.example.companyservice.model.Company;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.NaturalId;

import java.util.Set;

@Entity
@Table(name = "users",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_users_phone",
                columnNames = "phone_number"
        )
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @NaturalId
    @Column(name = "phone_number", nullable = false, length = 15, unique = true)
    private String phoneNumber;

    @ManyToMany(mappedBy = "employees", fetch = FetchType.LAZY)
    private Set<Company> companies;

    public void joinCompany(Company company) {
        this.companies.add(company);
        company.getEmployees().add(this);
    }

    public void leaveCompany(Company company) {
        this.companies.remove(company);
        company.getEmployees().remove(this);
    }
}

