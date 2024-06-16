package io.sillysillyman.springnewsfeed.user;

import io.sillysillyman.springnewsfeed.comment.Comment;
import io.sillysillyman.springnewsfeed.common.Timestamped;
import io.sillysillyman.springnewsfeed.follow.Follow;
import io.sillysillyman.springnewsfeed.like.commentlike.CommentLike;
import io.sillysillyman.springnewsfeed.like.postlike.PostLike;
import io.sillysillyman.springnewsfeed.post.Post;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "user")
public class User extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column
    private String email;

    @Column
    private String introduction;

    @Column
    private String pictureUrl;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserStatusEnum status;

    @Column
    private String refreshToken;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private EmailVerification emailVerification;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PostLike> postLikes = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CommentLike> commentLikes = new ArrayList<>();

    @OneToMany(mappedBy = "follower", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Follow> followees = new ArrayList<>();

    @OneToMany(mappedBy = "followee", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Follow> followers = new ArrayList<>();
}
