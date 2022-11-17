package com.cursoandroid.testeapp;

import com.cursoandroid.testeapp.util.DSL;
import com.cursoandroid.testeapp.util.DriverFactory;

import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;

public class ExemploTesteApp {

    private AndroidDriver<MobileElement> driver;
    private final DSL dsl = new DSL();

    @Before
    public void setUp() {

        driver = DriverFactory.getDriver();
    }

    @Test
    public void TesteSomaProdutos() {
        // clicar na caixa
        dsl.clicar(By.id("com.cursoandroid.pagamentodecompras:id/cbArroz"));
        // clicar na caixa
        dsl.clicar(By.id("com.cursoandroid.pagamentodecompras:id/cbCarne"));
        // clicar na caixa
        dsl.clicar(By.id("com.cursoandroid.pagamentodecompras:id/cbOvos"));
        // clicar no botão
        dsl.clicar(By.id("com.cursoandroid.pagamentodecompras:id/btTotal"));
        // obter resultado
        dsl.obterResultado(By.id("com.cursoandroid.pagamentodecompras:id/txtValor"));
        // verificar resultado
        Assert.assertEquals("Valor: R$ 23.3",
                dsl.obterResultado(By.id("com.cursoandroid.pagamentodecompras:id/txtValor")));
    }

    @After
    public void tearDown() {
        DriverFactory.finalizarDriver();
    }
}
