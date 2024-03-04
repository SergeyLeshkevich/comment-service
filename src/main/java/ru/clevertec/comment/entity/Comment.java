package ru.clevertec.comment.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.search.mapper.pojo.automaticindexing.ReindexOnUpdate;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexingDependency;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@Setter
@Builder
@Indexed
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "comment")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime time;

    @FullTextField(analyzer = "english")
    @Column(nullable = false, length = 500)
    private String text;

    @OneToOne
    @IndexedEmbedded
    @JoinColumn(name = "user_id")
    @IndexingDependency(reindexOnUpdate = ReindexOnUpdate.SHALLOW)
    private User user;

    @Column(name = "is_archive", nullable = false)
    private boolean isArchived;

    @Column(name = "news_id", nullable = false)
    private Long newsId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return isArchived == comment.isArchived
                && Objects.equals(id, comment.id)
                && Objects.equals(time, comment.time)
                && Objects.equals(text, comment.text)
                && Objects.equals(user, comment.user)
                && Objects.equals(newsId, comment.newsId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, time, text, user, isArchived, newsId);
    }
}
