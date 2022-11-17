package com.cursoandroid.testeapp.util;

import static com.cursoandroid.testeapp.util.DriverFactory.getDriver;

import org.openqa.selenium.By;

public class DSL {

    public void clicar(By by) {
        DriverFactory.getDriver().findElement(by).click();
    }

    public String obterResultado(By by) {
        return DriverFactory.getDriver().findElement(by).getText();
    }
}
