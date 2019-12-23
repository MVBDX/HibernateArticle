package ir.mctab.hibernatearticle.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity

@Table(name = "article")
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "title")
    private String title;
    @Column(name = "brief")
    private String brief;
    @Column(name = "content")
    private String content;
    @Column(name = "create_date")
    private Date createDate;
    @Column(name = "last_update_date")
    private Date lastUpdateDate;
    @Column(name = "publish_date")
    private Date publishDate;
    @Column(name = "is_published")
    private Boolean isPublished;
    @ManyToOne
    @JoinColumn(name = "category")
    private Category category;
    @ManyToOne
    @JoinColumn(name = "user")
    private User user;
}
