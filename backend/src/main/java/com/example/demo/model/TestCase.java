package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class TestCase {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
  @Column(columnDefinition="text") private String inputData;
  @Column(columnDefinition="text") private String expectedOutput;
  @ManyToOne(optional=false)
  @JsonIgnore
  private Problem problem;
  public TestCase(String in, String out){ this.inputData=in; this.expectedOutput=out; }
}
