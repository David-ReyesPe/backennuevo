package com.backendfunda.backendfunda.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "usuario")
public class Usuarios {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private  Long idUsuario;
    private String nombre;
    private String apellido;
    private String telefono;
    private String correo;
    private String password;
    private String imagen;

    // Añade el atributo tokenRecuperacion
    @Column(name = "token_recuperacion")
    private String tokenRecuperacion;

    @Column(name = "token_expiracion") // Campo para la fecha de expiración
    private LocalDateTime tokenExpiracion; // Utiliza LocalDateTime para representar la fecha

    /*Usamos FetchType en EADER para que cada vez que se acceda o se extraiga un usuario de la
    base de datos, este se traigo todos sus roles*/
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
      /*Con JoinTable estaremos creando una tabla que unirá la tabla de usuario y role, con lo cual tendremos un total de 3 tablas
    relacionadas en la tabla "usuarios_roles", a través de sus columnas usuario_id que apuntara al ID de la tabla usuario
    y role_id que apuntara al Id de la tabla role */
    @JoinTable(name = "usuarios_roles", joinColumns = @JoinColumn(name = "usuario_id", referencedColumnName = "id_usuario")
    , inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id_role"))
    private List<Roles>roles = new ArrayList<>();

}
