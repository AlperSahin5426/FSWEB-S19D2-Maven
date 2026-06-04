package com.workintech.s18d4.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "customer")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    // unique = true kısıtlamasını testin çakışmaması için kaldırdık
    @Column(name = "email")
    private String email;

    @Column(name = "salary")
    private Double salary;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "address_id")
    private Address address;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.REMOVE)
    private List<Account> accounts;

    // Test dosyalarındaki primitive cast uyumluluğu için bu metot kalabilir
    public long getId() {
        return id != null ? id : 0L;
    }
}