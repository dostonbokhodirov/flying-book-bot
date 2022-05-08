package uz.doston.flyingbookbot.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String fileId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String genre;

    @Column(nullable = false)
    private String size;

    @Column(nullable = false)
    private Long ownerId;

    @CreationTimestamp
    @Column(nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE default now()")
    private LocalDateTime uploadedAt;

    @Column
    private Integer downloadsCount;

}
