package com.github.questionarium.init;

import java.util.List;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.github.questionarium.interfaces.DTOs.AlternativeDTO;
import com.github.questionarium.interfaces.DTOs.QuestionDTO;
import com.github.questionarium.interfaces.DTOs.TagDTO;
import com.github.questionarium.services.QuestionService;
import com.github.questionarium.services.TagService;
import com.github.questionarium.types.QuestionAccessLevel;
import com.github.questionarium.types.QuestionEducationLevel;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class DataLoader implements ApplicationRunner {

        private final QuestionService questionService;
        private final TagService tagService;

        @Override
        public void run(ApplicationArguments args) throws Exception {

                // Tags iniciais
                tagService.createTag(TagDTO.builder().name("Português").description("Português").build());
                tagService.createTag(TagDTO.builder().name("Gramática normativa").description("Gramática normativa")
                                .build());
                tagService.createTag(TagDTO.builder().name("Interpretação de texto")
                                .description("Interpretação de texto").build());
                tagService.createTag(TagDTO.builder().name("Literatura brasileira").description("Literatura brasileira")
                                .build());
                tagService.createTag(TagDTO.builder().name("Literatura portuguesa").description("Literatura portuguesa")
                                .build());
                tagService.createTag(TagDTO.builder().name("Redação").description("Redação").build());
                tagService.createTag(TagDTO.builder().name("Matemática").description("Matemática").build());
                tagService.createTag(TagDTO.builder().name("Aritmética").description("Aritmética").build());
                tagService.createTag(TagDTO.builder().name("Álgebra").description("Álgebra").build());
                tagService.createTag(
                                TagDTO.builder().name("Equação do 2º grau").description("Equação do 2º grau").build());
                tagService.createTag(TagDTO.builder().name("Geometria plana").description("Geometria plana").build());
                tagService.createTag(
                                TagDTO.builder().name("Geometria espacial").description("Geometria espacial").build());
                tagService.createTag(TagDTO.builder().name("Trigonometria").description("Trigonometria").build());
                tagService.createTag(TagDTO.builder().name("Cálculo diferencial").description("Cálculo diferencial")
                                .build());
                tagService.createTag(TagDTO.builder().name("Cálculo integral").description("Cálculo integral").build());
                tagService.createTag(TagDTO.builder().name("Estatística").description("Estatística").build());
                tagService.createTag(TagDTO.builder().name("Probabilidade").description("Probabilidade").build());
                tagService.createTag(TagDTO.builder().name("Física").description("Física").build());
                tagService.createTag(TagDTO.builder().name("Cinemática").description("Cinemática").build());
                tagService.createTag(TagDTO.builder().name("Dinâmica").description("Dinâmica").build());
                tagService.createTag(TagDTO.builder().name("Mecânica dos fluidos").description("Mecânica dos fluidos")
                                .build());
                tagService.createTag(TagDTO.builder().name("Termodinâmica").description("Termodinâmica").build());
                tagService.createTag(TagDTO.builder().name("Óptica").description("Óptica").build());
                tagService.createTag(TagDTO.builder().name("Eletromagnetismo").description("Eletromagnetismo").build());
                tagService.createTag(TagDTO.builder().name("Primeira Lei de Newton")
                                .description("Primeira Lei de Newton").build());
                tagService.createTag(TagDTO.builder().name("Lei de Ampère").description("Lei de Ampère").build());
                tagService.createTag(TagDTO.builder().name("Química").description("Química").build());
                tagService.createTag(TagDTO.builder().name("Química orgânica").description("Química orgânica").build());
                tagService.createTag(
                                TagDTO.builder().name("Química inorgânica").description("Química inorgânica").build());
                tagService.createTag(
                                TagDTO.builder().name("Química analítica").description("Química analítica").build());
                tagService.createTag(TagDTO.builder().name("Biologia").description("Biologia").build());
                tagService.createTag(TagDTO.builder().name("Biologia celular").description("Biologia celular").build());
                tagService.createTag(TagDTO.builder().name("Genética").description("Genética").build());
                tagService.createTag(TagDTO.builder().name("Ecologia").description("Ecologia").build());
                tagService.createTag(TagDTO.builder().name("Evolução").description("Evolução").build());
                tagService.createTag(TagDTO.builder().name("História").description("História").build());
                tagService.createTag(
                                TagDTO.builder().name("História do Brasil").description("História do Brasil").build());
                tagService.createTag(
                                TagDTO.builder().name("Revolução Francesa").description("Revolução Francesa").build());
                tagService.createTag(TagDTO.builder().name("Idade Média").description("Idade Média").build());
                tagService.createTag(TagDTO.builder().name("História Antiga").description("História Antiga").build());
                tagService.createTag(TagDTO.builder().name("História Moderna").description("História Moderna").build());
                tagService.createTag(TagDTO.builder().name("Geografia").description("Geografia").build());
                tagService.createTag(TagDTO.builder().name("Geografia física").description("Geografia física").build());
                tagService.createTag(TagDTO.builder().name("Geografia humana").description("Geografia humana").build());
                tagService.createTag(TagDTO.builder().name("Filosofia").description("Filosofia").build());
                tagService.createTag(TagDTO.builder().name("Sociologia").description("Sociologia").build());
                tagService.createTag(TagDTO.builder().name("Artes").description("Artes").build());
                tagService.createTag(TagDTO.builder().name("Educação Física").description("Educação Física").build());
                tagService.createTag(TagDTO.builder().name("Inglês").description("Inglês").build());
                tagService.createTag(TagDTO.builder().name("Espanhol").description("Espanhol").build());

                questionService.createQuestion(QuestionDTO.builder()
                                .multipleChoice(true)
                                .numberLines(0)
                                .educationLevel(QuestionEducationLevel.ENSINO_MEDIO)
                                .userId(1L) // admin1
                                .header("[UFPR-2025] Uma fonte de ondas produz, em um dado meio, uma onda propagante periódica com velocidade de propagação 𝒗 e frequência 𝒇. Sabe-se que, para ondas em que 𝒇 = 25,0 Hz, a distância entre duas cristas sucessivas da onda produzida vale 𝒅 = 4,00 m. Com base nessas informações, assinale a alternativa que apresenta corretamente o valor da velocidade de propagação 𝒗 da onda produzida para a frequência indicada.")
                                .enable(true)
                                .accessLevel(QuestionAccessLevel.PUBLIC)
                                .tagIds(List.of(18L, 19L)) // Física, Cinemática
                                .alternatives(List.of(
                                                AlternativeDTO.builder()
                                                                .description("𝑣 = 6,25 m/s")
                                                                .imagePath(null)
                                                                .isCorrect(false)
                                                                .explanation("")
                                                                .alternativeOrder(1)
                                                                .build(),
                                                AlternativeDTO.builder()
                                                                .description("𝑣 = 12,5 m/s")
                                                                .imagePath(null)
                                                                .isCorrect(false)
                                                                .explanation("")
                                                                .alternativeOrder(2)
                                                                .build(),
                                                AlternativeDTO.builder()
                                                                .description("𝑣 = 50,0 m/s")
                                                                .imagePath(null)
                                                                .isCorrect(false)
                                                                .explanation("")
                                                                .alternativeOrder(3)
                                                                .build(),
                                                AlternativeDTO.builder()
                                                                .description("𝑣 = 100 m/s")
                                                                .imagePath(null)
                                                                .isCorrect(true)
                                                                .explanation("v = f × d = 25,0 Hz × 4,00 m = 100 m/s")
                                                                .alternativeOrder(4)
                                                                .build(),
                                                AlternativeDTO.builder()
                                                                .description("𝑣 = 200 m/s")
                                                                .imagePath(null)
                                                                .isCorrect(false)
                                                                .explanation("")
                                                                .alternativeOrder(5)
                                                                .build()))
                                .build());

                questionService.createQuestion(QuestionDTO.builder()
                                .multipleChoice(true)
                                .numberLines(0)
                                .educationLevel(QuestionEducationLevel.ENSINO_MEDIO)
                                .userId(1L) // admin1
                                .header("[UFPR - 2025] Uma dada lente delgada convergente tem distância focal 𝒇 = 3,0 cm. Um objeto luminoso puntiforme é colocado no eixo principal da lente a uma distância 𝒑 = 4,0 cm do centro óptico da lente. Assinale a alternativa que apresenta corretamente a distância 𝒑′ em que ocorre a formação da imagem, medida a partir do centro óptico da lente.")
                                .enable(true)
                                .accessLevel(QuestionAccessLevel.PUBLIC)
                                .tagIds(List.of(18L, 23L)) // Física, Óptica
                                .alternatives(List.of(
                                                AlternativeDTO.builder()
                                                                .description("𝑝' = -12,0 cm")
                                                                .imagePath(null)
                                                                .isCorrect(false)
                                                                .explanation("")
                                                                .alternativeOrder(1)
                                                                .build(),
                                                AlternativeDTO.builder()
                                                                .description("𝑝' = -6,0 cm")
                                                                .imagePath(null)
                                                                .isCorrect(false)
                                                                .explanation("")
                                                                .alternativeOrder(2)
                                                                .build(),
                                                AlternativeDTO.builder()
                                                                .description("𝑝' = +4,0 cm")
                                                                .imagePath(null)
                                                                .isCorrect(false)
                                                                .explanation("")
                                                                .alternativeOrder(3)
                                                                .build(),
                                                AlternativeDTO.builder()
                                                                .description("𝑝' = +6,0 cm")
                                                                .imagePath(null)
                                                                .isCorrect(false)
                                                                .explanation("")
                                                                .alternativeOrder(4)
                                                                .build(),
                                                AlternativeDTO.builder()
                                                                .description("𝑝' = +12,0 cm")
                                                                .imagePath(null)
                                                                .isCorrect(true)
                                                                .explanation("1/f = 1/p + 1/p' → 1/3 = 1/4 + 1/p' → 1/p' = 1/3 - 1/4 = (4-3)/12 = 1/12 → p' = 12 cm")
                                                                .alternativeOrder(5)
                                                                .build()))
                                .build());

                questionService.createQuestion(QuestionDTO.builder()
                                .multipleChoice(true)
                                .numberLines(0)
                                .educationLevel(QuestionEducationLevel.ENSINO_MEDIO)
                                .userId(1L) // admin1
                                .header("[UFPR - 2025] Em uma vasta região, há uma população de insetos com três variações de cor em suas carapaças: verde, marrom e amarelo. Essas cores conferem diferentes vantagens em três tipos distintos de ambientes: na floresta densa, os insetos verdes têm melhor camuflagem; nos prados abertos, os insetos marrons se misturam melhor com a vegetação seca; nas áreas de solo arenoso, os insetos amarelos são menos visíveis para os predadores. Com o tempo, um evento geológico separa essas regiões, criando barreiras físicas entre a floresta, os prados e as áreas arenosas.\n\nCom base no texto, assinale a alternativa correta em relação a possíveis consequências da ação da seleção natural.")
                                .enable(true)
                                .accessLevel(QuestionAccessLevel.PUBLIC)
                                .tagIds(List.of(29L, 33L)) // Biologia, Evolução
                                .alternatives(List.of(
                                                AlternativeDTO.builder()
                                                                .description("A seleção natural disruptiva altera o código genético dos insetos verdes isolados em áreas de solo arenoso, fazendo com que se tornem amarelos.")
                                                                .imagePath(null)
                                                                .isCorrect(false)
                                                                .explanation("A seleção natural disruptiva favorece extremos, mas não transforma diretamente insetos verdes em amarelos por alteração do código genético.")
                                                                .alternativeOrder(1)
                                                                .build(),
                                                AlternativeDTO.builder()
                                                                .description("A seleção natural estabilizadora promove irradiação adaptativa ao longo das gerações ao favorecer a sobrevivência de insetos marrons nas áreas de prados abertos.")
                                                                .imagePath(null)
                                                                .isCorrect(false)
                                                                .explanation("A seleção estabilizadora favorece características médias, não está relacionada à irradiação adaptativa nem ao favorecimento de extremos.")
                                                                .alternativeOrder(2)
                                                                .build(),
                                                AlternativeDTO.builder()
                                                                .description("A seleção natural direcional, atuando em cada tipo de ambiente, pode levar à diferenciação entre os insetos ao longo das gerações e à especiação alopátrica.")
                                                                .imagePath(null)
                                                                .isCorrect(true)
                                                                .explanation("O isolamento geográfico aliado à seleção natural direcional pode promover diferenciação genética e resultar em especiação alopátrica.")
                                                                .alternativeOrder(3)
                                                                .build(),
                                                AlternativeDTO.builder()
                                                                .description("A seleção natural estabilizadora induz os insetos a sofrerem mutações nos genes determinantes da cor da carapaça, podendo ocorrer especiação simpátrica.")
                                                                .imagePath(null)
                                                                .isCorrect(false)
                                                                .explanation("A seleção estabilizadora não induz mutações nem está relacionada à especiação simpátrica neste contexto.")
                                                                .alternativeOrder(4)
                                                                .build(),
                                                AlternativeDTO.builder()
                                                                .description("A seleção natural direcional, atuando nos três tipos")
                                                                .imagePath(null)
                                                                .isCorrect(false)
                                                                .explanation("Alternativa incompleta, não apresenta consequência clara da seleção natural sobre os insetos.")
                                                                .alternativeOrder(5)
                                                                .build()))
                                .build());

                questionService.createQuestion(QuestionDTO.builder()
                                .multipleChoice(true)
                                .numberLines(0)
                                .educationLevel(QuestionEducationLevel.ENSINO_MEDIO)
                                .userId(1L) // admin1
                                .header("[UFPR - 2025] Sobre a função e o transporte da seiva nas plantas vasculares, assinale a alternativa correta.")
                                .enable(true)
                                .accessLevel(QuestionAccessLevel.PUBLIC)
                                .tagIds(List.of(29L)) // Biologia
                                .alternatives(List.of(
                                                AlternativeDTO.builder()
                                                                .description("A seiva bruta transporta os nutrientes inorgânicos da fotossíntese, como carboidratos e aminoácidos, das folhas para outras partes da planta.")
                                                                .imagePath(null)
                                                                .isCorrect(false)
                                                                .explanation("A seiva bruta transporta água e sais minerais das raízes para as folhas, não carboidratos nem aminoácidos.")
                                                                .alternativeOrder(1)
                                                                .build(),
                                                AlternativeDTO.builder()
                                                                .description("O xilema é responsável pelo transporte da seiva elaborada, que é composta principalmente de água e carboidratos.")
                                                                .imagePath(null)
                                                                .isCorrect(false)
                                                                .explanation("O xilema transporta seiva bruta (água e sais minerais); a seiva elaborada é transportada pelo floema.")
                                                                .alternativeOrder(2)
                                                                .build(),
                                                AlternativeDTO.builder()
                                                                .description("A seiva bruta contém açúcares e nutrientes orgânicos, enquanto a seiva elaborada contém principalmente água e sais minerais.")
                                                                .imagePath(null)
                                                                .isCorrect(false)
                                                                .explanation("A seiva bruta contém água e sais minerais; a seiva elaborada contém açúcares e nutrientes orgânicos.")
                                                                .alternativeOrder(3)
                                                                .build(),
                                                AlternativeDTO.builder()
                                                                .description("A seiva elaborada é produzida pelas folhas e transporta nutrientes orgânicos, como carboidratos, para diferentes partes da planta por meio do floema.")
                                                                .imagePath(null)
                                                                .isCorrect(true)
                                                                .explanation("A seiva elaborada é produzida nas folhas (fotossíntese) e transportada pelo floema para outras partes da planta.")
                                                                .alternativeOrder(4)
                                                                .build(),
                                                AlternativeDTO.builder()
                                                                .description("A seiva elaborada se movimenta das raízes em direção às folhas, e a seiva bruta pode movimentar-se em várias direções, dependendo da necessidade da planta.")
                                                                .imagePath(null)
                                                                .isCorrect(false)
                                                                .explanation("A seiva elaborada vai das folhas para as outras partes; a seiva bruta vai das raízes para as folhas.")
                                                                .alternativeOrder(5)
                                                                .build()))
                                .build());

                questionService.createQuestion(QuestionDTO.builder()
                                .multipleChoice(true)
                                .numberLines(0)
                                .educationLevel(QuestionEducationLevel.ENSINO_MEDIO)
                                .userId(1L) // admin1
                                .header("[UFPR - 2025] Sobre as enzimas e a sua atuação na síntese de DNA, é correto afirmar:")
                                .enable(true)
                                .accessLevel(QuestionAccessLevel.PUBLIC)
                                .tagIds(List.of(29L, 31L)) // Biologia, Genética
                                .alternatives(List.of(
                                                AlternativeDTO.builder()
                                                                .description("O funcionamento enzimático denominado modelo chave-fechadura permite que diversas enzimas atuem na síntese de DNA, otimizando os recursos celulares.")
                                                                .imagePath(null)
                                                                .isCorrect(false)
                                                                .explanation("O modelo chave-fechadura explica a especificidade da ação enzimática, mas na síntese de DNA, poucas enzimas atuam de forma altamente específica.")
                                                                .alternativeOrder(1)
                                                                .build(),
                                                AlternativeDTO.builder()
                                                                .description("A DNA polimerase degrada o DNA para liberar os nucleotídeos utilizados na síntese de novas moléculas de DNA durante a replicação celular.")
                                                                .imagePath(null)
                                                                .isCorrect(false)
                                                                .explanation("A DNA polimerase não degrada o DNA; ela adiciona nucleotídeos livres à cadeia em formação.")
                                                                .alternativeOrder(2)
                                                                .build(),
                                                AlternativeDTO.builder()
                                                                .description("Os substratos da DNA polimerase são os desoxirribonucleotídeos livres, os quais são incorporados a uma fita em formação.")
                                                                .imagePath(null)
                                                                .isCorrect(true)
                                                                .explanation("A DNA polimerase adiciona desoxirribonucleotídeos livres à cadeia de DNA em formação.")
                                                                .alternativeOrder(3)
                                                                .build(),
                                                AlternativeDTO.builder()
                                                                .description("As enzimas que sintetizam DNA possuem os centros ativos específicos para os diferentes tipos de aminoácidos que formarão a molécula.")
                                                                .imagePath(null)
                                                                .isCorrect(false)
                                                                .explanation("O DNA é composto por nucleotídeos, não aminoácidos. Os centros ativos são específicos para nucleotídeos.")
                                                                .alternativeOrder(4)
                                                                .build(),
                                                AlternativeDTO.builder()
                                                                .description("A síntese de DNA ocorre pela ação de enzimas que, em um processo semiconservativo, convertem o RNA mensageiro em uma molécula de DNA.")
                                                                .imagePath(null)
                                                                .isCorrect(false)
                                                                .explanation("O processo descrito é a replicação do DNA, não a conversão de RNA mensageiro em DNA.")
                                                                .alternativeOrder(5)
                                                                .build()))
                                .build());

                questionService.createQuestion(QuestionDTO.builder()
                                .multipleChoice(true)
                                .numberLines(0)
                                .educationLevel(QuestionEducationLevel.ENSINO_MEDIO)
                                .userId(1L) // admin1
                                .header("[UFPR - 2025] \"O capitalismo é um sistema econômico que, desde sua origem, foi se expandindo econômica e territorialmente: primeiro foi o colonialismo, depois o imperialismo e, nos dias atuais, a globalização. [...] Considerando seu processo de desenvolvimento, costuma-se dividir o capitalismo em quatro etapas: comercial, industrial, financeira e informacional.\"\nMoreira, J. C.; Sene, E. Geografia: ensino médio. São Paulo: Scipione, 2018. p. 278. (Projeto Múltiplo, Parte 2)\nCom base no excerto apresentado e nos conhecimentos sobre o assunto, assinale a alternativa correta a respeito dos processos de expansão e desenvolvimento do capitalismo mencionados.")
                                .enable(true)
                                .accessLevel(QuestionAccessLevel.PUBLIC)
                                .tagIds(List.of(41L)) // Geografia
                                .alternatives(List.of(
                                                AlternativeDTO.builder()
                                                                .description("A fase informacional do capitalismo se caracteriza pela terceira Revolução Industrial, pela integração do comércio mundial e pela industrialização de países em desenvolvimento.")
                                                                .imagePath(null)
                                                                .isCorrect(true)
                                                                .explanation("A fase informacional está ligada à terceira Revolução Industrial, à integração global e ao avanço tecnológico.")
                                                                .alternativeOrder(1)
                                                                .build(),
                                                AlternativeDTO.builder()
                                                                .description("O capitalismo comercial se iniciou no século XIV, quando as potências europeias instituíram a escravidão na África.")
                                                                .imagePath(null)
                                                                .isCorrect(false)
                                                                .explanation("O capitalismo comercial iniciou-se nos séculos XV e XVI, ligado às grandes navegações e à expansão marítima, não à escravidão na África no século XIV.")
                                                                .alternativeOrder(2)
                                                                .build(),
                                                AlternativeDTO.builder()
                                                                .description("A fase do capitalismo industrial começou no final do século XIX, quando surgiu a indústria automobilística.")
                                                                .imagePath(null)
                                                                .isCorrect(false)
                                                                .explanation("O capitalismo industrial começou no século XVIII com a Primeira Revolução Industrial, antes do surgimento da indústria automobilística.")
                                                                .alternativeOrder(3)
                                                                .build(),
                                                AlternativeDTO.builder()
                                                                .description("O capitalismo financeiro se iniciou com a instituição do Banco Mundial e do Fundo Monetário Internacional, responsáveis por regular os fluxos mundiais de capital.")
                                                                .imagePath(null)
                                                                .isCorrect(false)
                                                                .explanation("O capitalismo financeiro surgiu no final do século XIX, muito antes do Banco Mundial e FMI, que só surgiram após a Segunda Guerra Mundial.")
                                                                .alternativeOrder(4)
                                                                .build(),
                                                AlternativeDTO.builder()
                                                                .description("A primeira fase da globalização começou quando as nações europeias ocuparam territórios na África, na Ásia e nas Américas, integrando os continentes em um único mercado.")
                                                                .imagePath(null)
                                                                .isCorrect(false)
                                                                .explanation("A primeira fase da globalização está ligada ao capitalismo comercial, mas não integrou plenamente todos os continentes em um único mercado como ocorre hoje.")
                                                                .alternativeOrder(5)
                                                                .build()))
                                .build());

                questionService.createQuestion(QuestionDTO.builder()
                                .multipleChoice(true)
                                .numberLines(0)
                                .educationLevel(QuestionEducationLevel.ENSINO_MEDIO)
                                .userId(1L) // admin1
                                .header("[UFPR - 2025] Com a adoção do Islã, religião propagada pelo profeta Maomé, os árabes iniciaram uma grande sequência de conquistas territoriais que foram sedimentadas com a formação dos califados omíada (a partir de 661 d.C.) e abássida (a partir de 750 d.C.). Alguns fatores fizeram com que essa expansão e sua subsequente manutenção fossem facilitadas, entre eles:")
                                .enable(true)
                                .accessLevel(QuestionAccessLevel.PUBLIC)
                                .tagIds(List.of(36L)) // História
                                .alternatives(List.of(
                                                AlternativeDTO.builder()
                                                                .description("os laços diplomáticos estabelecidos com as regiões do norte da Europa cristã, especialmente o tratado entre o califado abássida e o reino franco de Carlos Martel.")
                                                                .imagePath(null)
                                                                .isCorrect(false)
                                                                .explanation("Não houve tratados diplomáticos relevantes entre os califados islâmicos e o reino de Carlos Martel; pelo contrário, Carlos Martel deteve o avanço islâmico na Europa.")
                                                                .alternativeOrder(1)
                                                                .build(),
                                                AlternativeDTO.builder()
                                                                .description("a permissão de que povos conquistados mantivessem suas religiões mediante o pagamento da jizia, imposto cobrado de súditos não muçulmanos.")
                                                                .imagePath(null)
                                                                .isCorrect(true)
                                                                .explanation("Os conquistados podiam manter suas crenças, desde que pagassem a jizia, o que facilitou a administração e manutenção do império islâmico.")
                                                                .alternativeOrder(2)
                                                                .build(),
                                                AlternativeDTO.builder()
                                                                .description("o fundamentalismo religioso dos árabes, que eram movidos pelo fervor da jihad, termo que pode ser traduzido como “guerra santa”.")
                                                                .imagePath(null)
                                                                .isCorrect(false)
                                                                .explanation("A expansão não foi motivada apenas por fundamentalismo religioso, e o termo 'jihad' não se restringe à guerra santa.")
                                                                .alternativeOrder(3)
                                                                .build(),
                                                AlternativeDTO.builder()
                                                                .description("a realocação da capital dos califados para Constantinopla, cidade bizantina conquistada pelas forças islâmicas e localizada entre a Ásia e a Europa.")
                                                                .imagePath(null)
                                                                .isCorrect(false)
                                                                .explanation("Constantinopla só foi conquistada pelos otomanos em 1453, séculos após os califados omíada e abássida.")
                                                                .alternativeOrder(4)
                                                                .build(),
                                                AlternativeDTO.builder()
                                                                .description("a conversão voluntária de judeus, especialmente no norte da África e na península Ibérica, que ficaram conhecidos como moçárabes.")
                                                                .imagePath(null)
                                                                .isCorrect(false)
                                                                .explanation("Moçárabes eram cristãos, não judeus, que viviam sob domínio islâmico na Península Ibérica.")
                                                                .alternativeOrder(5)
                                                                .build()))
                                .build());

                questionService.createQuestion(QuestionDTO.builder()
                                .multipleChoice(true)
                                .numberLines(0)
                                .educationLevel(QuestionEducationLevel.ENSINO_MEDIO)
                                .userId(1L) // admin1
                                .header("[UFPR - 2025] Em julho de 2021, manifestantes incendiaram uma estátua do bandeirante Borba Gato, localizada na Zona Sul de São Paulo. A ação foi considerada vandalismo por alguns e um ato político por outros. A dissonância nessas posturas revela um embate público sobre a construção de imagens e memória da História do Brasil. Assinale a alternativa que explica corretamente esse embate entre a construção de imagens dos bandeirantes e a memória da História do Brasil.")
                                .enable(true)
                                .accessLevel(QuestionAccessLevel.PUBLIC)
                                .tagIds(List.of(36L, 37L)) // História, História do Brasil
                                .alternatives(List.of(
                                                AlternativeDTO.builder()
                                                                .description("Por serem portugueses de nascimento, bandeirantes como Borba Gato passaram a ser vistos de forma negativa durante o período da República Velha, uma vez que o novo governo buscava fomentar o nacionalismo por meio de personagens nativos da História do Brasil.")
                                                                .imagePath(null)
                                                                .isCorrect(false)
                                                                .explanation("A maioria dos bandeirantes nasceu no Brasil e foram exaltados, não rejeitados, durante a República Velha como símbolos paulistas e nacionais.")
                                                                .alternativeOrder(1)
                                                                .build(),
                                                AlternativeDTO.builder()
                                                                .description("Tradicionalmente considerados heróis e forjados como símbolos de identidade nacional, os bandeirantes passaram a ser questionados pela historiografia recente por suas ações violentas e excludentes, como o assassinato, a caça e a tentativa de escravização de indígenas nativos.")
                                                                .imagePath(null)
                                                                .isCorrect(true)
                                                                .explanation("A resposta correta, pois reflete o questionamento contemporâneo sobre o legado dos bandeirantes, tradicionalmente exaltados, mas hoje criticados por práticas violentas e excludentes.")
                                                                .alternativeOrder(2)
                                                                .build(),
                                                AlternativeDTO.builder()
                                                                .description("Desprezados desde o início do século XX por movimentos culturais como o dos modernistas, os bandeirantes foram recuperados como fruto de orgulho nacional durante a ditadura militar, motivo pelo qual passaram a ser questionados novamente após a redemocratização.")
                                                                .imagePath(null)
                                                                .isCorrect(false)
                                                                .explanation("Os modernistas exaltaram, e não desprezaram, os bandeirantes, especialmente no movimento paulista de 1922. O resgate do orgulho nacional foi um processo anterior à ditadura.")
                                                                .alternativeOrder(3)
                                                                .build(),
                                                AlternativeDTO.builder()
                                                                .description("Símbolos marcantes da identidade brasileira, os bandeirantes foram idolatrados por seu vasto conhecimento sobre o território, ao passo que, mais recentemente, passaram a ser questionados por não representarem o Brasil como um todo, dada sua vinculação exclusiva com São Paulo.")
                                                                .imagePath(null)
                                                                .isCorrect(false)
                                                                .explanation("A crítica recente não é apenas pela exclusividade paulista, mas principalmente pelas ações violentas dos bandeirantes.")
                                                                .alternativeOrder(4)
                                                                .build(),
                                                AlternativeDTO.builder()
                                                                .description("Embora muitos bandeirantes fossem violentos, motivo que sustenta certos questionamentos contemporâneos, a caça aos indígenas não era a prerrogativa das expedições, que são, hoje, louvadas pelo pioneirismo na catalogação de espécies de fauna e flora nativas no território brasileiro.")
                                                                .imagePath(null)
                                                                .isCorrect(false)
                                                                .explanation("A caça e escravização de indígenas era sim uma das principais finalidades das expedições bandeirantes.")
                                                                .alternativeOrder(5)
                                                                .build()))
                                .build());
                // USER 1
                questionService.createQuestion(QuestionDTO.builder()
                                .multipleChoice(true)
                                .numberLines(2)
                                .educationLevel(QuestionEducationLevel.ENSINO_FUNDAMENTAL)
                                .userId(3L) // user1
                                .header("Leia o trecho: \"O cachorro pulou o muro e correu pelo jardim.\" O que fez o cachorro?")
                                .enable(true)
                                .accessLevel(QuestionAccessLevel.PRIVATE)
                                .tagIds(List.of(1L, 3L)) // Português, Interpretação de texto
                                .alternatives(List.of(
                                                AlternativeDTO.builder()
                                                                .description("Pulou o muro e correu pelo jardim.")
                                                                .isCorrect(true)
                                                                .explanation("O texto diz que ele pulou o muro e correu pelo jardim.")
                                                                .alternativeOrder(1).build(),
                                                AlternativeDTO.builder().description("Dormiu no jardim.")
                                                                .isCorrect(false)
                                                                .explanation("O texto não menciona que o cachorro dormiu.")
                                                                .alternativeOrder(2).build(),
                                                AlternativeDTO.builder().description("Latia para o gato.")
                                                                .isCorrect(false)
                                                                .explanation("Nada no texto indica que havia um gato.")
                                                                .alternativeOrder(3).build(),
                                                AlternativeDTO.builder().description("Ficou parado no muro.")
                                                                .isCorrect(false)
                                                                .explanation("O cachorro não ficou parado.")
                                                                .alternativeOrder(4).build(),
                                                AlternativeDTO.builder().description("Correu atrás de um pássaro.")
                                                                .isCorrect(false)
                                                                .explanation("Não há menção de pássaro.")
                                                                .alternativeOrder(5).build()))
                                .build());

                // USER 1
                questionService.createQuestion(QuestionDTO.builder()
                                .multipleChoice(true)
                                .numberLines(1)
                                .educationLevel(QuestionEducationLevel.ENSINO_FUNDAMENTAL)
                                .userId(3L)
                                .header("Em qual alternativa o verbo está corretamente conjugado no presente do indicativo?")
                                .enable(true)
                                .accessLevel(QuestionAccessLevel.PRIVATE)
                                .tagIds(List.of(1L, 2L)) // Português, Gramática normativa
                                .alternatives(List.of(
                                                AlternativeDTO.builder().description("Nós estudamos todos os dias.")
                                                                .isCorrect(true)
                                                                .explanation("Verbo 'estudar' conjugado corretamente: nós estudamos.")
                                                                .alternativeOrder(1).build(),
                                                AlternativeDTO.builder().description("Eles aprende matemática.")
                                                                .isCorrect(false)
                                                                .explanation("Deveria ser 'aprendem', não 'aprende'.")
                                                                .alternativeOrder(2).build(),
                                                AlternativeDTO.builder().description("Eu jogam bola.").isCorrect(false)
                                                                .explanation("Deveria ser 'jogo', não 'jogam'.")
                                                                .alternativeOrder(3).build(),
                                                AlternativeDTO.builder().description("Você corremos rápido.")
                                                                .isCorrect(false)
                                                                .explanation("Verbo está inadequado ao sujeito.")
                                                                .alternativeOrder(4).build(),
                                                AlternativeDTO.builder().description("Tu leem livros.").isCorrect(false)
                                                                .explanation("Correto seria 'lês', não 'leem'.")
                                                                .alternativeOrder(5).build()))
                                .build());

                // USER 1
                questionService.createQuestion(QuestionDTO.builder()
                                .multipleChoice(true)
                                .numberLines(1)
                                .educationLevel(QuestionEducationLevel.ENSINO_FUNDAMENTAL)
                                .userId(3L)
                                .header("Assinale a alternativa em que todas as palavras estão corretamente escritas:")
                                .enable(true)
                                .accessLevel(QuestionAccessLevel.PRIVATE)
                                .tagIds(List.of(1L, 2L)) // Português, Gramática normativa
                                .alternatives(List.of(
                                                AlternativeDTO.builder().description("cachorro, janela, escola")
                                                                .isCorrect(true)
                                                                .explanation("Todas as palavras estão corretas.")
                                                                .alternativeOrder(1).build(),
                                                AlternativeDTO.builder().description("caxorro, janela, escola")
                                                                .isCorrect(false)
                                                                .explanation("'Caxorro' está errado, o correto é 'cachorro'.")
                                                                .alternativeOrder(2).build(),
                                                AlternativeDTO.builder().description("cachorro, janela, iscola")
                                                                .isCorrect(false)
                                                                .explanation("'Iscola' está errado, o correto é 'escola'.")
                                                                .alternativeOrder(3).build(),
                                                AlternativeDTO.builder().description("cachoro, janela, escola")
                                                                .isCorrect(false)
                                                                .explanation("'Cachoro' está errado, o correto é 'cachorro'.")
                                                                .alternativeOrder(4).build(),
                                                AlternativeDTO.builder().description("cachorro, janella, escola")
                                                                .isCorrect(false)
                                                                .explanation("'Janella' está errado, o correto é 'janela'.")
                                                                .alternativeOrder(5).build()))
                                .build());

                // USER 1
                questionService.createQuestion(QuestionDTO.builder()
                                .multipleChoice(true)
                                .numberLines(2)
                                .educationLevel(QuestionEducationLevel.ENSINO_FUNDAMENTAL)
                                .userId(3L)
                                .header("Assinale a frase em que a pontuação está correta:")
                                .enable(true)
                                .accessLevel(QuestionAccessLevel.PRIVATE)
                                .tagIds(List.of(1L, 2L)) // Português, Gramática normativa
                                .alternatives(List.of(
                                                AlternativeDTO.builder().description("Vamos ao parque, amanhã?")
                                                                .isCorrect(false)
                                                                .explanation("Não se usa vírgula antes do advérbio 'amanhã' nesse caso.")
                                                                .alternativeOrder(1).build(),
                                                AlternativeDTO.builder()
                                                                .description("Ana trouxe lápis borracha, e caderno.")
                                                                .isCorrect(false)
                                                                .explanation("A vírgula está incorreta entre 'lápis' e 'borracha'.")
                                                                .alternativeOrder(2).build(),
                                                AlternativeDTO.builder().description("Hoje, está muito quente.")
                                                                .isCorrect(true)
                                                                .explanation("A vírgula após o advérbio 'Hoje' está correta.")
                                                                .alternativeOrder(3).build(),
                                                AlternativeDTO.builder().description(
                                                                "Pedro foi a escola e, esqueceu o material.")
                                                                .isCorrect(false)
                                                                .explanation("A vírgula após 'e' está errada.")
                                                                .alternativeOrder(4).build(),
                                                AlternativeDTO.builder().description(
                                                                "Ontem fui ao cinema mas, não gostei do filme.")
                                                                .isCorrect(false)
                                                                .explanation("A vírgula antes de 'mas' está errada.")
                                                                .alternativeOrder(5).build()))
                                .build());

                // USER 1
                questionService.createQuestion(QuestionDTO.builder()
                                .multipleChoice(true)
                                .numberLines(1)
                                .educationLevel(QuestionEducationLevel.ENSINO_FUNDAMENTAL)
                                .userId(3L)
                                .header("Qual alternativa apresenta todas as palavras acentuadas corretamente?")
                                .enable(true)
                                .accessLevel(QuestionAccessLevel.PRIVATE)
                                .tagIds(List.of(1L, 2L)) // Português, Gramática normativa
                                .alternatives(List.of(
                                                AlternativeDTO.builder().description("fácil, lápis, café")
                                                                .isCorrect(true)
                                                                .explanation("Todas as palavras estão acentuadas corretamente.")
                                                                .alternativeOrder(1).build(),
                                                AlternativeDTO.builder().description("facil, lápis, café")
                                                                .isCorrect(false)
                                                                .explanation("'Facil' deveria ser 'fácil'.")
                                                                .alternativeOrder(2).build(),
                                                AlternativeDTO.builder().description("fácil, lapis, café")
                                                                .isCorrect(false)
                                                                .explanation("'Lapis' deveria ser 'lápis'.")
                                                                .alternativeOrder(3).build(),
                                                AlternativeDTO.builder().description("fácil, lápis, cafe")
                                                                .isCorrect(false)
                                                                .explanation("'Cafe' deveria ser 'café'.")
                                                                .alternativeOrder(4).build(),
                                                AlternativeDTO.builder().description("facil, lapis, café")
                                                                .isCorrect(false)
                                                                .explanation("'Facil' e 'lapis' deveriam ser acentuados.")
                                                                .alternativeOrder(5).build()))
                                .build());

                // QUSER JOAO
                questionService.createQuestion(QuestionDTO.builder()
                                .multipleChoice(true)
                                .numberLines(1)
                                .educationLevel(QuestionEducationLevel.ENSINO_MEDIO)
                                .userId(5L) // joao
                                .header("Em que ano começou a Revolução Francesa?")
                                .enable(true)
                                .accessLevel(QuestionAccessLevel.PRIVATE)
                                .tagIds(List.of(36L, 39L)) // História, Revolução Francesa
                                .alternatives(List.of(
                                                AlternativeDTO.builder().description("1789").isCorrect(true)
                                                                .explanation("1789 marca o início da Revolução Francesa.")
                                                                .alternativeOrder(1).build(),
                                                AlternativeDTO.builder().description("1815").isCorrect(false)
                                                                .explanation("1815 é o fim da era napoleônica.")
                                                                .alternativeOrder(2).build(),
                                                AlternativeDTO.builder().description("1500").isCorrect(false)
                                                                .explanation("1500 é a data do Descobrimento do Brasil.")
                                                                .alternativeOrder(3).build(),
                                                AlternativeDTO.builder().description("1914").isCorrect(false)
                                                                .explanation("1914 marca o início da Primeira Guerra Mundial.")
                                                                .alternativeOrder(4).build(),
                                                AlternativeDTO.builder().description("1848").isCorrect(false)
                                                                .explanation("1848 é o ano das Revoluções Liberais na Europa.")
                                                                .alternativeOrder(5).build()))
                                .build());

                // USER JOAO
                questionService.createQuestion(QuestionDTO.builder()
                                .multipleChoice(true)
                                .numberLines(1)
                                .educationLevel(QuestionEducationLevel.ENSINO_MEDIO)
                                .userId(5L)
                                .header("Quem foi o líder do processo de Independência do Brasil em 1822?")
                                .enable(true)
                                .accessLevel(QuestionAccessLevel.PRIVATE)
                                .tagIds(List.of(36L, 37L)) // História, História do Brasil
                                .alternatives(List.of(
                                                AlternativeDTO.builder().description("Dom Pedro I").isCorrect(true)
                                                                .explanation("Dom Pedro I proclamou a Independência do Brasil.")
                                                                .alternativeOrder(1).build(),
                                                AlternativeDTO.builder().description("Getúlio Vargas").isCorrect(false)
                                                                .explanation("Getúlio Vargas foi presidente do Brasil no século XX.")
                                                                .alternativeOrder(2).build(),
                                                AlternativeDTO.builder().description("Dom João VI").isCorrect(false)
                                                                .explanation("Dom João VI era rei de Portugal e pai de Dom Pedro I.")
                                                                .alternativeOrder(3).build(),
                                                AlternativeDTO.builder().description("Tiradentes").isCorrect(false)
                                                                .explanation("Tiradentes foi líder da Inconfidência Mineira, antes da Independência.")
                                                                .alternativeOrder(4).build(),
                                                AlternativeDTO.builder().description("Juscelino Kubitschek")
                                                                .isCorrect(false)
                                                                .explanation("Juscelino Kubitschek foi presidente na década de 1950.")
                                                                .alternativeOrder(5).build()))
                                .build());

                // USER JOAO
                questionService.createQuestion(QuestionDTO.builder()
                                .multipleChoice(true)
                                .numberLines(1)
                                .educationLevel(QuestionEducationLevel.ENSINO_MEDIO)
                                .userId(5L)
                                .header("Qual evento foi o estopim da Primeira Guerra Mundial?")
                                .enable(true)
                                .accessLevel(QuestionAccessLevel.PRIVATE)
                                .tagIds(List.of(36L, 40L)) // História, História Moderna
                                .alternatives(List.of(
                                                AlternativeDTO.builder().description(
                                                                "O assassinato do arquiduque Francisco Ferdinando")
                                                                .isCorrect(true)
                                                                .explanation("O assassinato em Sarajevo foi o estopim da guerra.")
                                                                .alternativeOrder(1).build(),
                                                AlternativeDTO.builder()
                                                                .description("A invasão da Polônia pela Alemanha")
                                                                .isCorrect(false)
                                                                .explanation("Este evento marcou o início da Segunda Guerra Mundial.")
                                                                .alternativeOrder(2).build(),
                                                AlternativeDTO.builder().description("A Revolução Russa de 1917")
                                                                .isCorrect(false)
                                                                .explanation("A Revolução Russa ocorreu durante a Primeira Guerra, mas não foi o estopim.")
                                                                .alternativeOrder(3).build(),
                                                AlternativeDTO.builder().description("A queda da Bastilha")
                                                                .isCorrect(false)
                                                                .explanation("A queda da Bastilha foi um marco da Revolução Francesa.")
                                                                .alternativeOrder(4).build(),
                                                AlternativeDTO.builder().description("O fim do Império Otomano")
                                                                .isCorrect(false)
                                                                .explanation("O fim do Império Otomano foi consequência da guerra, não a causa.")
                                                                .alternativeOrder(5).build()))
                                .build());

                // USER JOAO
                questionService.createQuestion(QuestionDTO.builder()
                                .multipleChoice(true)
                                .numberLines(1)
                                .educationLevel(QuestionEducationLevel.ENSINO_MEDIO)
                                .userId(5L)
                                .header("Em qual período Getúlio Vargas governou o Brasil como presidente constitucional pela primeira vez?")
                                .enable(true)
                                .accessLevel(QuestionAccessLevel.PRIVATE)
                                .tagIds(List.of(36L, 37L)) // História, História do Brasil
                                .alternatives(List.of(
                                                AlternativeDTO.builder().description("1934-1937").isCorrect(true)
                                                                .explanation("Vargas foi presidente constitucional de 1934 a 1937.")
                                                                .alternativeOrder(1).build(),
                                                AlternativeDTO.builder().description("1930-1934").isCorrect(false)
                                                                .explanation("Nessa fase, Vargas foi presidente provisório e depois chefe do governo provisório.")
                                                                .alternativeOrder(2).build(),
                                                AlternativeDTO.builder().description("1937-1945").isCorrect(false)
                                                                .explanation("Esse foi o período do Estado Novo, governo ditatorial de Vargas.")
                                                                .alternativeOrder(3).build(),
                                                AlternativeDTO.builder().description("1951-1954").isCorrect(false)
                                                                .explanation("Período do segundo governo de Vargas, eleito pelo voto direto.")
                                                                .alternativeOrder(4).build(),
                                                AlternativeDTO.builder().description("1922-1930").isCorrect(false)
                                                                .explanation("Não corresponde a nenhum governo de Vargas.")
                                                                .alternativeOrder(5).build()))
                                .build());

                // USER JOAO
                questionService.createQuestion(QuestionDTO.builder()
                                .multipleChoice(true)
                                .numberLines(1)
                                .educationLevel(QuestionEducationLevel.ENSINO_MEDIO)
                                .userId(5L)
                                .header("Quais países eram os principais rivais durante a Guerra Fria?")
                                .enable(true)
                                .accessLevel(QuestionAccessLevel.PRIVATE)
                                .tagIds(List.of(36L, 40L)) // História, História Moderna
                                .alternatives(List.of(
                                                AlternativeDTO.builder().description("Estados Unidos e União Soviética")
                                                                .isCorrect(true)
                                                                .explanation("Esses países foram as duas superpotências rivais.")
                                                                .alternativeOrder(1).build(),
                                                AlternativeDTO.builder().description("Estados Unidos e França")
                                                                .isCorrect(false)
                                                                .explanation("")
                                                                .alternativeOrder(2).build(),
                                                AlternativeDTO.builder().description("Reino Unido e Alemanha")
                                                                .isCorrect(false)
                                                                .explanation("")
                                                                .alternativeOrder(3).build(),
                                                AlternativeDTO.builder().description("União Soviética e Japão")
                                                                .isCorrect(false)
                                                                .explanation("")
                                                                .alternativeOrder(4).build(),
                                                AlternativeDTO.builder().description("China e Estados Unidos")
                                                                .isCorrect(false)
                                                                .explanation("")
                                                                .alternativeOrder(5).build()))
                                .build());

        }

}
