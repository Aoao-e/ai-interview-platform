package com._306.aijob.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class requirements {
    private Integer id;
    private String username;
    private String job;
    private String tone;
    private Integer count;
    private String extraInfo;
    private List<answerclass> answers;
}
