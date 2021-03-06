package br.ce.wcaquino.servicos;

import br.ce.wcaquino.entidades.Usuario;
import org.junit.Assert;
import org.junit.Test;

public class AssertTest {

  @Test
  public void test(){
    Assert.assertTrue(true);
    Assert.assertFalse(false);

    Assert.assertEquals(1,1);
    //Assert.assertEquals("Erro de comparacao exibido",1,2);
    Assert.assertEquals(0.51234,0.512, 0.001);
    Assert.assertEquals(Math.PI, 3.14, 0.01);

    int i = 5; // Tipo primitivo
    Integer i2 = 5; // Tipo objeto

    Assert.assertEquals(Integer.valueOf(i), i2);
    Assert.assertEquals(i, i2.intValue());

    Assert.assertEquals("tenis", "tenis");
    Assert.assertNotEquals("tenis", "sapato");
    Assert.assertTrue("tenis".equalsIgnoreCase("Tenis"));
    Assert.assertTrue("tenis".startsWith("te"));

    Usuario u1 = new Usuario("User 1");
    Usuario u2 = new Usuario("User 1");
    Usuario u3 = null;

    Assert.assertEquals(u1,u2);

    Assert.assertSame(u2, u2);
    Assert.assertNotSame(u1, u2);

    Assert.assertNull(u3);
    Assert.assertNotNull(u2);



  }

}
