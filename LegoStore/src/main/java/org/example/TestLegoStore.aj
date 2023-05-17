package org.example;

public aspect TestLegoStore {
    before(): execution(* Main.*(..)){
        System.out.println("hi from before");
    }
}
