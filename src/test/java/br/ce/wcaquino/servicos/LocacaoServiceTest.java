package br.ce.wcaquino.servicos;

import static br.ce.wcaquino.builders.FilmeBuilder.umFilme;
import static br.ce.wcaquino.builders.FilmeBuilder.umFilmeSemEstoque;
import static br.ce.wcaquino.builders.LocacaoBuilder.umLocacao;
import static br.ce.wcaquino.builders.UsuarioBuilder.umUsuario;
import static br.ce.wcaquino.matchers.MatchersProprios.caiEm;
import static br.ce.wcaquino.matchers.MatchersProprios.caiNumaSegunda;
import static br.ce.wcaquino.matchers.MatchersProprios.ehHoje;
import static br.ce.wcaquino.matchers.MatchersProprios.ehHojeComDiferencaDias;
import static br.ce.wcaquino.utils.DataUtils.isMesmaData;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import br.ce.wcaquino.daos.LocacaoDAO;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.matchers.DiaSemanaMatcher;
import br.ce.wcaquino.utils.DataUtils;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

//@RunWith(ParallelRunner.class)
public class LocacaoServiceTest {

  @InjectMocks @Spy
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
    System.out.println("Starting 2...");
    CalculadoraTest.ordem.append("2");
  }

  @After
  public void tearDown(){
    System.out.println("Finishing 2...");
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

    Mockito.doReturn(DataUtils.obterData(28, 4, 2017)).when(service).obterData();

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

  }

  @Test(expected = FilmeSemEstoqueException.class)
  public void naoDeveAlugarFilmeSemEstoque() throws Exception {

    //cenario
    Usuario usuario = umUsuario().agora();
    List<Filme> filmes = Arrays.asList(umFilmeSemEstoque().agora());

    //acao
    Locacao locacao = service.alugarFilme(usuario, filmes);

  }

  @Test
  public void naoDeveAlugarFilmeSemUsuario() throws FilmeSemEstoqueException {
    //cenario
    List<Filme> filmes = Arrays.asList(umFilme().agora());

    //acao
    try {
      service.alugarFilme(null, filmes);
      Assert.fail();
    } catch (LocadoraException e) {
      //Verificacao
      assertThat(e.getMessage(), is("Usuario vazio"));
    }
  }

  @Test
  public void naoDeveAlugarFilmeSemFilme() throws FilmeSemEstoqueException, LocadoraException {
    //cenario
    Usuario usuario = umUsuario().agora();

    exception.expect(LocadoraException.class);
    exception.expectMessage("Filme vazio");

    //acao
    service.alugarFilme(usuario, null);
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

    Mockito.doReturn(DataUtils.obterData(29, 4, 2017)).when(service).obterData();

    //acao
    Locacao retorno = service.alugarFilme(usuario, filmes);

    //verificacao
    assertThat(retorno.getDataRetorno(), new DiaSemanaMatcher(Calendar.MONDAY));
    assertThat(retorno.getDataRetorno(), caiEm(Calendar.MONDAY));
    assertThat(retorno.getDataRetorno(), caiNumaSegunda());
  }

  @Test
  public void naoDeveAlugarFilmeParaNegativadoSPC() throws Exception {
    //cenario
    Usuario usuario = umUsuario().agora();
    List<Filme> filmes = Arrays.asList(umFilme().agora());

    when(spcService.possuiNegativacao(Mockito.any(Usuario.class))).thenReturn(true);

    //acao
    try {
      service.alugarFilme(usuario,filmes);
      Assert.fail();
    } catch (LocadoraException e) {
      assertThat(e.getMessage(), is("Usuário Negativado"));
    }

    //verificacao
    verify(spcService).possuiNegativacao(usuario);
  }

  @Test
  public void deveEnviarEmailParaLocacoesAtrasadas(){
    //cenario
    Usuario usuario = umUsuario().agora();
    Usuario usuario2 = umUsuario().comNome("Usuario 2").agora();
    Usuario usuario3 = umUsuario().comNome("Usuario 3").agora();
    List<Locacao> locacoes = Arrays.asList(
        umLocacao().atrasada().comUsuario(usuario).agora(),
        umLocacao().comUsuario(usuario2).agora(),
        umLocacao().atrasada().comUsuario(usuario3).agora(),
        umLocacao().atrasada().comUsuario(usuario3).agora());

    when(dao.obterLocacoesPendentes()).thenReturn(locacoes);
    //acao
    service.notificarAtrasos();

    //verificacao
    verify(emailService, times(3)).notificarAtraso(Mockito.any(Usuario.class));
    verify(emailService).notificarAtraso(usuario);
    verify(emailService, atLeastOnce()).notificarAtraso(usuario3);
    verify(emailService, Mockito.never()).notificarAtraso(usuario2);
    verifyNoMoreInteractions(emailService);
    verifyZeroInteractions(spcService);
  }

  @Test
  public void deveTratarErronoSPC() throws Exception {
    //cenario
    Usuario usuario = umUsuario().agora();
    List<Filme> filmes = Arrays.asList(umFilme().agora());

    when(spcService.possuiNegativacao(usuario)).thenThrow(new Exception("Falha catatrófica"));

    //verificacao
    exception.expect(LocadoraException.class);
    //exception.expectMessage("Falha catatrófica");
    exception.expectMessage("Problemas com SPC, tente novamente");

    //acao
    service.alugarFilme(usuario,filmes);
  }

  @Test
  public void deveProrrogarUmaLocacao(){
    //cenario
    Locacao locacao = umLocacao().agora();

    //acao
    service.prorrogarLocacao(locacao, 3);

    //verificacao
    ArgumentCaptor<Locacao> argCapt = ArgumentCaptor.forClass(Locacao.class);
    Mockito.verify(dao).salvar(argCapt.capture());
    Locacao locacaoRetornada = argCapt.getValue();

    error.checkThat(locacaoRetornada.getValor(), is(12.0));
    error.checkThat(locacaoRetornada.getDataLocacao(), ehHoje());
    error.checkThat(locacaoRetornada.getDataRetorno(), ehHojeComDiferencaDias(3));
  }

  @Test
  public void deveCalcularValorLocacao() throws Exception {
    //cenario
    List<Filme> filmes = Arrays.asList(umFilme().agora());

    //acao
    Class<LocacaoService> clazz = LocacaoService.class;
    Method metodo = clazz.getDeclaredMethod("calcularValorLocacao", List.class);
    metodo.setAccessible(true);
    Double valor = (Double) metodo.invoke(service, filmes);

    //verificacao
    Assert.assertThat(valor, is(4.0));
  }



}


