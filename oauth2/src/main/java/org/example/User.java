package org.example;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    public User(String name, String email, String token){
        this.name = name;
        this.email = email;
        this.token = token;
    }
    private String name;
    private String email;
    private String token;
}
