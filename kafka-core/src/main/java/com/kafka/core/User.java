package com.kafka.core;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class User {

   private String name;
   private int age;


   public User(String name, int age) {

      this.name = name;
      this.age = age;
   }
}