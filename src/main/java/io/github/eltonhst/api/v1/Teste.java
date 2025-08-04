package io.github.eltonhst.api.v1;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Teste {

    private static final String PAGE_HOME = "http://comprasnet.gov.br/ConsultaLicitacoes/ConsLicitacaoDia.asp";
    private static final String PAGE_ITEM = "http://comprasnet.gov.br/ConsultaLicitacoes/download/download_editais_detalhe.asp";

    public static void main(String[] args) {
        List<Licitacao> licitacoes = new ArrayList<>();
        try {
            Document document = Jsoup.connect(PAGE_HOME).get();
            Elements tds = document.select("td");

            Pattern patternUasg = Pattern.compile("Código da UASG:\\s*(\\d+)");
            Pattern patternPregao = Pattern.compile("Pregão Eletrônico Nº\\s*(\\d+/\\d+)");
            Pattern patternObjeto = Pattern.compile("Objeto:\\s*Pregão Eletrônico\\s*-\\s*(.+?)\\s*Edital a partir de:", Pattern.DOTALL);

            for (Element td : tds) {
                String texto = td.text();

                Matcher matcherUasg = patternUasg.matcher(texto);
                Matcher matcherPregao = patternPregao.matcher(texto);
                Matcher matcherObjeto = patternObjeto.matcher(texto);

                if (matcherUasg.find() && matcherPregao.find()) {
                    String codigoUasg = matcherUasg.group(1);
                    String numeroPregao = matcherPregao.group(1);

                    System.out.println("Código da UASG: " + codigoUasg);
                    System.out.println("Pregão Eletrônico: " + numeroPregao);
                    if (matcherObjeto.find()) {
                        String objeto = matcherObjeto.group(1).trim();
                        System.out.println("Objeto: " + objeto);

                        Licitacao licitacao = new Licitacao(
                                codigoUasg,
                                numeroPregao,
                                objeto
                        );
                        licitacoes.add(licitacao);
                    }
                    System.out.println("---------------");
                }
            }

            System.out.println("--- Fim da Listagem ---");
            filterByUASG(licitacoes.getFirst());

        } catch (RuntimeException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void filterByUASG(Licitacao licitacao) {
        try {
            String uri = "?coduasg=" + licitacao.getCodigo() + "&modprp=5&numprp=" + licitacao.getPregao().replace("/", "");
            Document document = Jsoup.connect(PAGE_ITEM + uri).get();
            Elements tds = document.select("td");

            Pattern patternTitulo = Pattern.compile("(\\d+\\s*-\\s*.+?)\\s");
            //Pattern patternDescricao = Pattern.compile("</span><br><span class=\"tex3\">(.*?)<br>Tratamento", Pattern.DOTALL);
            Pattern patternQuantidade = Pattern.compile("Quantidade:\\s*(\\d+)");
            Pattern patternUnidade = Pattern.compile("Unidade de fornecimento:\\s*([\\p{L} ]+)");

            for (Element td : tds) {
                String htmlTd = td.html();
                String texto = td.text();

                Matcher matcherTitulo = patternTitulo.matcher(texto);
                //Matcher matcherDescricao = patternDescricao.matcher(htmlTd);
                Matcher matcherQuantidade = patternQuantidade.matcher(texto);
                Matcher matcherUnidade = patternUnidade.matcher(texto);

                if (matcherTitulo.find()) {
                    String titulo = matcherTitulo.group(1).trim();
                    System.out.println("Título: " + titulo);
                }

                //if (matcherDescricao.find()) {
                //    String descricao = matcherDescricao.group(1).replaceAll("<br>", "").trim();
                //    System.out.println("Descrição: " + descricao);
                //}

                if (matcherQuantidade.find()) {
                    String quantidade = matcherQuantidade.group(1).trim();
                    System.out.println("Quantidade: " + quantidade);
                }

                if (matcherUnidade.find()) {
                    String unidade = matcherUnidade.group(1).trim();
                    System.out.println("Unidade de fornecimento: " + unidade);
                }

                System.out.println("---------------");
            }
        } catch (RuntimeException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}

class Licitacao {
    private String codigo;
    private String pregao;
    private String descricao;
    private List<Item> items;

    public Licitacao(String codigo, String pregao, String descricao) {
        this.codigo = codigo;
        this.pregao = pregao;
        this.descricao = descricao;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getPregao() {
        return pregao;
    }

    public String getDescricao() {
        return descricao;
    }

    public List<Item> getItems() {
        return items;
    }
}

class Item {
    private String titulo;
    private String descricao;
    private String quantidade;
    private String unidadeFornecimento;

    public Item(String titulo, String descricao, String quantidade, String unidadeFornecimento) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.quantidade = quantidade;
        this.unidadeFornecimento = unidadeFornecimento;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getQuantidade() {
        return quantidade;
    }

    public String getUnidadeFornecimento() {
        return unidadeFornecimento;
    }
}
