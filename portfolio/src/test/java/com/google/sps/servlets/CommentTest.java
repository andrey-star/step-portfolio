package com.google.sps.servlets;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CommentTest {

  @Test
  public void sampleTest() {
    Comment comment = new Comment("0", "test", "example@gmail.com", "johndoe", 0);
    assertEquals(comment.getEmail(), "example@gmail.com");
  }

}
