package uz.doston.flyingbookbot.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import uz.doston.flyingbookbot.enums.AuthRole;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class AuthUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String chatId;

    @Column
    private String fullName;

    @Column
    private Integer age;

    @Column
    private String gender;

    @Column
    private String phoneNumber;

    @Column
    private String language;

    @Column
    @Enumerated(value = EnumType.STRING)
    private AuthRole role;

    @Column
    private String userName;

    @CreationTimestamp
    @Column(nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE default now()")
    private LocalDateTime createdAt;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "auth_user_books",
            joinColumns = @JoinColumn(name = "auth_user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "book_id", referencedColumnName = "id")
    )
    private List<Book> Books;

}
