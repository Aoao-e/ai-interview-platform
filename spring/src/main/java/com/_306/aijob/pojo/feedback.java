package com._306.aijob.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class feedback {
    Integer question_id;
    String username;
    String question;
    String feedback;
    Integer is_search=0;
}
