package com.agribridge.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
     private Long id;

     private String firstName;

     private String lastName;

     private String phoneNumber;

     private String gender;

     private LocalDateTime dateOfBirth;

     private String address;
     @Column(unique = true, nullable = false)

     private String username;
     @Column(unique = true, nullable = false)

     private String email;
     @Column(nullable = false)

     private String password;

     private Boolean enabled=false;

     @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
     @JoinTable(name = "user_roles",
             joinColumns = @JoinColumn(name = "user_id",referencedColumnName = "id"),
             inverseJoinColumns = @JoinColumn(name = "role_id",referencedColumnName = "id")
                )
     private Set<Role> roles;
     @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
     private List<Farm> farms = new ArrayList<>();

     @CreationTimestamp
     private LocalDateTime createdAt;
     @UpdateTimestamp
     private LocalDateTime updatedAt;
}
