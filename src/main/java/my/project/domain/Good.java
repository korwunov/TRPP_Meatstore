package my.project.domain;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id", "producer", "title", "price"})
public class Good {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;


    @NotBlank(message = "Пожалуйста заполните поле")
    @Length(max = 255)
    private String title;


    @NotBlank(message = "Пожалуйста заполните поле")
    @Length(max = 255)
    private String producer;


    @NotBlank(message = "Пожалуйста заполните поле")
    @Length(max = 255)
    private String country;


    @NotBlank(message = "Пожалуйста заполните поле")
    @Length(max = 255)
    private String stash;


    @NotBlank(message = "Пожалуйста заполните поле")
    @Length(max = 255)
    private String breed;


    private String description;


    private String filename;


    @NotNull(message = "Пожалуйста заполните поле")
    private Integer price;


    @NotBlank(message = "Пожалуйста заполните поле")
    @Length(max = 255)
    private String volume;


    @NotBlank(message = "Пожалуйста заполните поле")
    @Length(max = 255)
    private String type;

    @Override
    public String toString() {
        return String.format("Название: %s, Вес: %s кг.\n",
                this.title,
                this.volume
        );
    }
}