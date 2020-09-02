package br.ce.wcaquino.servicos;

import static br.ce.wcaquino.utils.DataUtils.isMesmaData;
import static br.ce.wcaquino.builders.FilmeBuilder.umFilme;
import static br.ce.wcaquino.builders.UsuarioBuilder.umUsuario;
import static br.ce.wcaquino.matchers.MatchersProprios.caiEm;
import static br.ce.wcaquino.matchers.MatchersProprios.caiNumaSegunda;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import br.ce.wcaquino.daos.LocacaoDAO;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.utils.DataUtils;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import br.ce.wcaquino.matchers.DiaSemanaMatcher;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

@RunWith(PowerMockRunner.class)
@PrepareForTest({LocacaoService.class})
public class LocacaoServiceTest_PM {

  @InjectMocks
  private LocacaoService service;

  @Mock
  private SPCService spcService;

  @Mock
  private LocacaoDAO dao;

  @Mock
  private EmailService emailService;

  @Rule
  public ErrorCollector error = new ErrorCollector();

  @Rule
  public ExpectedException exception = ExpectedException.none();

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    service = PowerMockito.spy(service);
    System.out.println("Starting 4...");
    CalculadoraTest.ordem.append("4");
  }

  @After
  public void tearDown(){
    System.out.println("Finishing 4...");
  }

  @AfterClass
  public static void tearDownClass(){
    System.out.println(CalculadoraTest.ordem.toString());
  }

  @Test
  public void testeLocacao() throws Exception {
    //cenario
    Usuario usuario = umUsuario().agora();
    List<Filme> filmes = Arrays.asList(umFilme().comValor(5.0).agora());

    PowerMockito.whenNew(Date.class).withNoArguments().thenReturn(DataUtils.obterData(28,4,2017));


    //acao
    Locacao locacao = service.alugarFilme(usuario, filmes);

    //verificacao
/*    Assert.assertEquals(5.0, locacao.getValor(), 0.001);
    Assert.assertTrue(isMesmaData(locacao.getDataLocacao(), new Date()));
    Assert.assertTrue(isMesmaData(locacao.getDataRetorno(), obterDataComDiferencaDias(1)));

    assertThat(locacao.getValor(), is(5.0));
    assertThat(locacao.getValor(), is(not(6.0)));
    assertThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
    assertThat(isMesmaData(locacao.getDataRetorno(), obterDataComDiferencaDias(1)), is(true));
    assertThat("1bc", CoreMatchers.startsWith("1bc"));*/

    error.checkThat(locacao.getValor(), is(5.0));
    //error.checkThat(locacao.getDataLocacao(), ehHoje());
	  //error.checkThat(locacao.getDataRetorno(), ehHojeComDiferencaDias(1));
    error.checkThat(isMesmaData(locacao.getDataLocacao(), DataUtils.obterData(28, 4, 2017)), is(true));
    error.checkThat(isMesmaData(locacao.getDataRetorno(), DataUtils.obterData(29, 4, 2017)), is(true));
    PowerMockito.verifyStatic(times(2));
    Calendar.getInstance();

  }
/*  @Test
  public void devePagar75NoFilme3() throws FilmeSemEstoqueException, LocadoraException {
    //cenario
    Usuario usuario = new Usuario("Usuario 1");
    List<Filme> filmes = Arrays.asList(
        new Filme("Filme 1", 2, 4.0),
        new Filme("Filme 2", 2, 4.0),
        new Filme("Filme 3", 2, 4.0));
    //acao

    Locacao resultado = service.alugarFilme(usuario, filmes);

    //verificacao
    Assert.assertThat(resultado.getValor(), is(11.0));
  }

  @Test
  public void devePagar50NoFilme4() throws FilmeSemEstoqueException, LocadoraException {
    //cenario
    Usuario usuario = new Usuario("Usuario 1");
    List<Filme> filmes = Arrays.asList(
        new Filme("Filme 1", 2, 4.0),
        new Filme("Filme 2", 2, 4.0),
        new Filme("Filme 3", 2, 4.0),
        new Filme("Filme 4", 2, 4.0));
    //acao

    Locacao resultado = service.alugarFilme(usuario, filmes);

    //verificacao
    Assert.assertThat(resultado.getValor(), is(13.0));
  }

  @Test
  public void devePagar25NoFilme5() throws FilmeSemEstoqueException, LocadoraException {
    //cenario
    Usuario usuario = new Usuario("Usuario 1");
    List<Filme> filmes = Arrays.asList(
        new Filme("Filme 1", 2, 4.0),
        new Filme("Filme 2", 2, 4.0),
        new Filme("Filme 3", 2, 4.0),
        new Filme("Filme 4", 2, 4.0),
        new Filme("Filme 5", 2, 4.0));
    //acao

    Locacao resultado = service.alugarFilme(usuario, filmes);

    //verificacao
    Assert.assertThat(resultado.getValor(), is(14.0));
  }

  @Test
  public void devePagar0NoFilme6() throws FilmeSemEstoqueException, LocadoraException {
    //cenario
    Usuario usuario = new Usuario("Usuario 1");
    List<Filme> filmes = Arrays.asList(
        new Filme("Filme 1", 2, 4.0),
        new Filme("Filme 2", 2, 4.0),
        new Filme("Filme 3", 2, 4.0),
        new Filme("Filme 4", 2, 4.0),
        new Filme("Filme 5", 2, 4.0),
        new Filme("Filme 6", 2, 4.0));
    //acao

    Locacao resultado = service.alugarFilme(usuario, filmes);

    //verificacao
    Assert.assertThat(resultado.getValor(), is(14.0));
  }*/

  @Test
  public void naoDeveDevolverFilmeNoDomingo() throws Exception {
    //cenario
    Usuario usuario = umUsuario().agora();
    List<Filme> filmes = Arrays.asList(umFilme().agora());

    PowerMockito.whenNew(Date.class).withNoArguments().thenReturn(DataUtils.obterData(29,4,2017));

    //acao
    Locacao retorno = service.alugarFilme(usuario, filmes);

    //verificacao
    assertThat(retorno.getDataRetorno(), new DiaSemanaMatcher(Calendar.MONDAY));
    assertThat(retorno.getDataRetorno(), caiEm(Calendar.MONDAY));
    assertThat(retorno.getDataRetorno(), caiNumaSegunda());

  }

  @Test
  public void deveAlugarFilme_SemCalcularValor() throws Exception {
    //cenario
    Usuario usuario = umUsuario().agora();
    List<Filme> filmes = Arrays.asList(umFilme().agora());

    PowerMockito.doReturn(1.0).when(service, "calcularValorLocacao", filmes);

    //acao
    Locacao locacao = service.alugarFilme(usuario, filmes);

    //verificacao
    Assert.assertThat(locacao.getValor(), is(1.0));
    PowerMockito.verifyPrivate(service).invoke("calcularValorLocacao", filmes);
  }

  @Test
  public void deveCalcularValorLocacao() throws Exception {
    //cenario
    List<Filme> filmes = Arrays.asList(umFilme().agora());

    //acao
    Double valor = (Double) Whitebox.invokeMethod(service, "calcularValorLocacao", filmes);

    //verificacao
    Assert.assertThat(valor, is(4.0));
  }



}


