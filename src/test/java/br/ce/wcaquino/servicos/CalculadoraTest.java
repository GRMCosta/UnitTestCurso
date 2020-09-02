package br.ce.wcaquino.servicos;

import br.ce.wcaquino.exceptions.NaoPodeDividirPorZeroException;
import br.ce.wcaquino.runners.ParallelRunner;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

//@RunWith(ParallelRunner.class)
public class CalculadoraTest {

  public static StringBuffer ordem = new StringBuffer();

  private Calculadora calc;

  @Before
  public void setUp() {
    calc = new Calculadora();
    System.out.println("Starting...");
    ordem.append("1");
  }

  @After
  public void tearDown(){
    System.out.println("Finishing...");
  }

  @AfterClass
  public static void tearDownClass(){
    System.out.println(ordem);
  }

  @Test
  public void deveSomarDoisValores() {
    //cenario
    int a = 5;
    int b = 3;

    //acao
    int resultado = calc.somar(a, b);

    //verificacao
    Assert.assertEquals(8, resultado);

  }

  @Test
  public void deveSubtrairDoisValores() {
    //cenario
    int a = 8;
    int b = 5;

    //acao
    int resultado = calc.subtrair(a, b);

    //verificacao
    Assert.assertEquals(3, resultado);
  }

  @Test
  public void deveDividirDoisValores() throws NaoPodeDividirPorZeroException {
    //cenario
    int a = 6;
    int b = 3;

    //acao
    int resultado = calc.dividir(a, b);

    //verificacao
    Assert.assertEquals(2, resultado);
  }

  @Test(expected = NaoPodeDividirPorZeroException.class)
  public void deveLancarExcecaoAoDividirPorZero() throws NaoPodeDividirPorZeroException {
    //cenario
    int a = 10;
    int b = 0;

    //acao
    int resultado = calc.dividir(a, b);

    //verificacao
    Assert.assertEquals(2, resultado);
  }

}
