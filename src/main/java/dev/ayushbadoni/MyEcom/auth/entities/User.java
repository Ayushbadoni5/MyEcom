package dev.ayushbadoni.MyEcom.auth.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.ayushbadoni.MyEcom.entities.Address;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;


@Entity
@Table(name = "Auth_User_Details")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class User implements UserDetails {

    @Id
    @GeneratedValue
    private UUID id;

    private String firstName;

    private String lastName;

    @Column(nullable = false,unique = true)
    private String email;

    private Date createdOn;

    private Date updatedOn;

    @JsonIgnore
    private String password;

    private String phoneNumber;

    private String verificationCode;

    private boolean enabled = false;
    // Shows if the user account is active or verified (false means not verified yet)


    private String provider;
    // Tells how user signed in: 'local' for email-password, 'google' for Google login

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "Auth_User_Authority", joinColumns = @JoinColumn(referencedColumnName = "id"),inverseJoinColumns = @JoinColumn(referencedColumnName = "id"))
    @JsonIgnore
    private List<Authority> authorities;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @ToString.Exclude
    @JsonIgnore
    private List<Address> addressList;

    @Override

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }
}
