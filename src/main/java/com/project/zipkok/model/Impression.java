package com.project.zipkok.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Impression")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Impression {

    @Id
    @Column(name ="impression_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long impressionId;

    @Column(name ="impression_title", nullable = false)
    private String impressionTitle;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true)
    private User user;

    @OneToMany(mappedBy = "impression", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<CheckedImpression> checkedImpressions = new ArrayList<>();

    public Impression(String impressionTitle){
        this.impressionTitle = impressionTitle;
    }
}
