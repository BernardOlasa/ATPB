package org.example.Classes.GUI;

import com.toedter.calendar.JDateChooser;
import org.example.Classes.Bicicleta;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import org.example.Classes.*;
import org.example.Classes.Dados.GerenciadorDeDados;

public class GUI {

    private static final int LARGURA_JANELA = 800;
    private static final int ALTURA_JANELA = 600;
    private static final int MAX_DIAS_ALUGUEL = 30;
    private Util util = new Util();
    private static ArrayList<Bicicleta> bicicletasAlugadas = new ArrayList<>();
    private static ArrayList<Bicicleta> bicicletasDisponiveis = new ArrayList<>();
    private static ArrayList<Bicicleta> bikesSelecionadas = new ArrayList<>();
    private static ArrayList<Bicicleta> bicicletasTodasTemp = new ArrayList<>();
    // Componentes da interface
    // Frame Principal
    private JFrame janelaPrincipal = new JFrame();

    private JPanel cardAtualmenteFocado = null;
    // Navbar
    private JMenuBar barraDeNavegacao;
    private JMenu itemMenu1, itemMenu2, itemMenu3;



    //region Painel Aluguel Componentes
    private JPanel painelAluguel;
    private JPanel painelDadosAluguel;
    private JPanel painelBicicletas;
    private JPanel painelFinal;
    private JPanel painelCliente;
    private JComboBox<String> diasInput;
    private JLabel diasErrorMsg;
    private JComboBox<String> comboBoxClientes = new JComboBox<>();
    private JScrollPane catalogoBicicletas;
    private JPanel innerPanelSelecionadas;
    private JPanel innerPanelDisponiveis;
    private JButton efetuarAluguel;
    private JPanel diasPanel;
    //endregion Painel Aluguel Componentes

    //region Painel CriarBikes

    private JPanel painelCriarBikes;
    private JPanel inputMarcaPanel;
    private JLabel marcaInputLabel;
    private JTextField marcaInput;
    private JPanel inputModeloPanel;
    private JLabel modeloInputLabel;
    private JTextField modeloInput;
    private JPanel inputAroPanel;
    private JLabel aroInputLabel;
    private JComboBox<String> aroInput;
    private JPanel inputAroMaterialPanel;
    private JLabel aroMaterialInputLabel;
    private JComboBox<String> aroMaterialInput;
    private JPanel inputbikeMaterialPanel;
    private JLabel bikeMaterialInputLabel;
    private JComboBox<String> bikeMaterialInput;
    private JPanel inputValorDiariaPanel;
    private JLabel valorDiariaInputLabel;
    private JTextField valorDiariaInput;
    private JPanel inputValorSeguroPanel;
    private JLabel valorSeguroInputLabel;
    private JTextField valorSeguroInput;
    private JPanel inputSpecialistPanel;
    private JLabel valorSpecialistInputLabel;
    private JCheckBox specialistInput;
    private JPanel inputCorPanel;
    private JLabel corInputLabel;
    private JTextField corInput;
    private JPanel inputButtonPanel;
    private JButton criarBike;


    //endregion Painel CriarBikes

    //region Painel Promocoes
    private JPanel painelPromocao;
    private JPanel dataInicioPromocaoPanel;
    private JLabel dataInicioPromocaoLabel;
    private JDateChooser dataInicioPromocao;
    private JPanel dataFimPromocaoPanel;
    private JLabel dataFimPromocaoLabel;
    private JDateChooser dataFimPromocao;
    private JPanel porcentagemPianel;
    private JLabel porcentagemLabel;
    private JTextField porcentagemInput;
    private JPanel estacavelPianel;
    private JLabel estacavelLabel;
    private JCheckBox estacavelCheckbox;
    private JButton criarPromocao;
    private JPanel painelBicicletasPromocao;
    private JPanel innerPanelDisponiveisPromocao;
    private JPanel innerPanelSelecionadasPromocao;

    //endregion Painel Promocoes




    public void executar(int nivel) {
        janelaPrincipal.removeAll();
        configurarJanelaPrincipal();
        configurarBarraNavegacao(nivel);
        configurarPainelAluguel();

        if(nivel == 1) {

            configurarPainelCriarBicicletas();
            configurarAreaPromocao();
            configurarPainelBicicletasPromocao();


        }

        janelaPrincipal.setLocationRelativeTo(null);
        janelaPrincipal.setVisible(true);

        abrirJanela(painelAluguel);
    }


    private void configurarJanelaPrincipal() {
        janelaPrincipal = new JFrame("Sistema de Aluguel de Bicicletas");
        janelaPrincipal.setResizable(false);
        janelaPrincipal.setSize(LARGURA_JANELA, ALTURA_JANELA);
        janelaPrincipal.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void configurarBarraNavegacao(int nivel) {
        barraDeNavegacao = new JMenuBar();
        janelaPrincipal.setJMenuBar(barraDeNavegacao);

        itemMenu1 = new JMenu("Aluguel");
        itemMenu1.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                abrirJanela(painelAluguel);
                atualizarPainelBicicletas(innerPanelDisponiveis, innerPanelSelecionadas,bicicletasDisponiveis,bikesSelecionadas,"Adicionar Bicicleta","Remover Bicicleta");
                atualizarPainelBicicletas(innerPanelSelecionadas, innerPanelDisponiveis,bikesSelecionadas, bicicletasDisponiveis,"Remover Bicicleta","Adicionar Bicicleta");

            }
        });
        barraDeNavegacao.add(itemMenu1);
        if(nivel == 1) {
            itemMenu2 = new JMenu("Cadastro Bicicletas");
            itemMenu2.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent evt) {
                    abrirJanela(painelCriarBikes);
                }
            });

            itemMenu3 = new JMenu("Promoções");
            itemMenu3.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent evt) {
                    abrirJanela(painelPromocao);
                }
            });



            barraDeNavegacao.add(itemMenu2);
            barraDeNavegacao.add(itemMenu3);
        }

    }

    //region Iniciar Painel de Aluguel

    private void configurarPainelAluguel() {
        painelAluguel = new JPanel();
        painelAluguel.setSize(LARGURA_JANELA, ALTURA_JANELA);
        painelAluguel.setLayout(new GridLayout(3, 1, 25, 25));

        configurarPainelDadosAluguel();
        configurarPainelFinalizacao();
        configurarPainelBicicletas();

        janelaPrincipal.add(painelAluguel);
    }

    private void configurarPainelDadosAluguel() {
        painelDadosAluguel = new JPanel(new GridLayout(2, 1));
        painelDadosAluguel.setBorder(BorderFactory.createEmptyBorder(0, 20, 5, 20));

        JLabel titulo = new JLabel("Dados de Aluguel", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 16));
        painelDadosAluguel.add(titulo);

        JPanel inputPanel = new JPanel(new GridLayout(1, 3));

        // Configuração do campo de dias
        diasPanel = new JPanel(new GridLayout(2, 2));
        diasPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 5, 40));
        diasPanel.add(new JLabel("Dias:"));

        String[] dias = new String[MAX_DIAS_ALUGUEL + 1];
        dias[0] = "";
        for (int i = 1; i <= MAX_DIAS_ALUGUEL; i++) {
            dias[i] = String.valueOf(i);
        }

        diasInput = new JComboBox<>(dias);
        diasPanel.add(diasInput);

        diasErrorMsg = new JLabel("Selecione uma quantidade de dias");
        diasErrorMsg.setForeground(Color.RED);
        diasErrorMsg.setVisible(false);
        diasPanel.add(diasErrorMsg);

        inputPanel.add(diasPanel);

        // Configuração do comboBox de clientes
        JPanel painelSelecionarClientes = new JPanel(new GridLayout(2, 1));
        painelSelecionarClientes.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel labelClientes = new JLabel("Selecione um cliente:");
        painelSelecionarClientes.add(labelClientes);

        comboBoxClientes.removeAllItems();
        for (Cliente c : GerenciadorDeDados.clientes) {
            comboBoxClientes.addItem(c.getNome() + " : " + c.getCpf());
        }

        painelSelecionarClientes.add(comboBoxClientes);
        inputPanel.add(painelSelecionarClientes);

        // Botão para adicionar cliente
        JButton adicionarCliente = new JButton("Adicionar Cliente");
        adicionarCliente.addActionListener(e -> {
                    PainelCadastro painel = new PainelCadastro();

        });

        JPanel painelBotaoAddCliente = new JPanel();
        painelBotaoAddCliente.setBorder(new EmptyBorder(10, 10, 10, 10));
        painelBotaoAddCliente.add(adicionarCliente);
        inputPanel.add(painelBotaoAddCliente);

        painelDadosAluguel.add(inputPanel);
        painelAluguel.add(painelDadosAluguel);
    }

    private void configurarPainelBicicletas() {
        painelBicicletas = new JPanel();
        painelBicicletas.setBorder(BorderFactory.createLineBorder(Color.black));
        painelBicicletas.setLayout(new GridLayout(1, 2));

        innerPanelDisponiveis = new JPanel();
        innerPanelDisponiveis.setLayout(new BoxLayout(innerPanelDisponiveis, BoxLayout.Y_AXIS));
        JScrollPane catalogoBicicletasDisponiveis = new JScrollPane(innerPanelDisponiveis);
        painelBicicletas.add(catalogoBicicletasDisponiveis, BorderLayout.CENTER);

        innerPanelSelecionadas = new JPanel();
        innerPanelSelecionadas.setLayout(new BoxLayout(innerPanelSelecionadas, BoxLayout.Y_AXIS));
        JScrollPane catalogoBicicletasSelecionadas = new JScrollPane(innerPanelSelecionadas);
        painelBicicletas.add(catalogoBicicletasSelecionadas, BorderLayout.CENTER);

        AtualizarBikesDisponiveis();
        atualizarPainelBicicletas(innerPanelDisponiveis,innerPanelSelecionadas,bicicletasDisponiveis,bikesSelecionadas,"Adicionar Bicicleta","Remover Bicicleta");
        atualizarPainelBicicletas(innerPanelSelecionadas,innerPanelDisponiveis,bikesSelecionadas, bicicletasDisponiveis,"Remover Bicicleta","Adicionar Bicicleta");



        painelAluguel.add(painelBicicletas);
    }

    private void configurarPainelFinalizacao() {
        painelFinal = new JPanel();
        efetuarAluguel = new JButton("Efetuar Aluguel");
        efetuarAluguel.addActionListener(e -> efetuarAluguel());
        painelFinal.add(efetuarAluguel);
        painelAluguel.add(painelFinal);
    }
    //endregion Iniciar Painel de Aluguel

    //region Iniciar Painel de Criar Bikes

    private void configurarPainelCriarBicicletas() {
        painelCriarBikes = new JPanel();
        painelCriarBikes.setSize(LARGURA_JANELA, ALTURA_JANELA);
        painelCriarBikes.setLayout(new GridLayout(6, 2, 50, 25));
        configurarAreaInputsCriarBikes();

        janelaPrincipal.add(painelCriarBikes);
    }

    private void configurarAreaInputsCriarBikes(){
        inputMarcaPanel = new JPanel();
        inputMarcaPanel.setLayout(new GridLayout(1, 2, 25, 5));
        painelCriarBikes.add(inputMarcaPanel);
        marcaInputLabel = new JLabel("Marca:");
        marcaInput = new JTextField();
        inputMarcaPanel.add(marcaInputLabel);
        inputMarcaPanel.add(marcaInput);

        inputModeloPanel = new JPanel();
        inputModeloPanel.setLayout(new GridLayout(1, 2, 25, 5));
        painelCriarBikes.add(inputModeloPanel);
        modeloInputLabel = new JLabel("Modelo:");
        modeloInput = new JTextField();
        inputModeloPanel.add(modeloInputLabel);
        inputModeloPanel.add(modeloInput);

        inputAroPanel = new JPanel();
        inputAroPanel.setLayout(new GridLayout(1, 2, 25, 5));
        painelCriarBikes.add(inputAroPanel);
        aroInputLabel = new JLabel("Aro:");
        aroInput = new JComboBox<>();
        aroInput.setModel(new DefaultComboBoxModel(new String[]{"", "12", "16", "20", "24", "26",
                "29", "650", "700"}));
        inputAroPanel.add(aroInputLabel);
        inputAroPanel.add(aroInput);

        inputAroMaterialPanel = new JPanel();
        inputAroMaterialPanel.setLayout(new GridLayout(1, 2, 25, 5));
        painelCriarBikes.add(inputAroMaterialPanel);
        aroMaterialInputLabel = new JLabel("Material do Aro:");
        aroMaterialInput = new JComboBox<>();
        aroMaterialInput.setModel(new DefaultComboBoxModel(new String[]{"", "Alumínio Carbono", "Aço Carbono", "Alumínio", "Fibra de Carbono", "Titânio",
                "Magnésio"}));
        inputAroMaterialPanel.add(aroMaterialInputLabel);
        inputAroMaterialPanel.add(aroMaterialInput);

        inputbikeMaterialPanel = new JPanel();
        inputbikeMaterialPanel.setLayout(new GridLayout(1, 2, 25, 5));
        painelCriarBikes.add(inputbikeMaterialPanel);
        bikeMaterialInputLabel = new JLabel("Material do Aro:");
        bikeMaterialInput = new JComboBox<>();
        bikeMaterialInput.setModel(new DefaultComboBoxModel(new String[]{"", "Aço", "Alumínio", "Fibra de Carbono", "Titânio"}));
        inputbikeMaterialPanel.add(bikeMaterialInputLabel);
        inputbikeMaterialPanel.add(bikeMaterialInput);


        inputValorDiariaPanel = new JPanel();
        inputValorDiariaPanel.setLayout(new GridLayout(1, 2, 25, 5));
        painelCriarBikes.add(inputValorDiariaPanel);
        valorDiariaInputLabel = new JLabel("Valor Diaria:");
        valorDiariaInput = new JTextField();
        inputValorDiariaPanel.add(valorDiariaInputLabel);
        inputValorDiariaPanel.add(valorDiariaInput);


        inputValorSeguroPanel = new JPanel();
        inputValorSeguroPanel.setLayout(new GridLayout(1, 2, 25, 5));
        painelCriarBikes.add(inputValorSeguroPanel);
        valorSeguroInputLabel = new JLabel("Seguro:");
        valorSeguroInput = new JTextField();
        inputValorSeguroPanel.add(valorSeguroInputLabel);
        inputValorSeguroPanel.add(valorSeguroInput);




        inputCorPanel = new JPanel();
        inputCorPanel.setLayout(new GridLayout(1, 2, 25, 5));
        painelCriarBikes.add(inputCorPanel);
        corInputLabel = new JLabel("Cor:");
        corInput = new JTextField();
        inputCorPanel.add(corInputLabel);
        inputCorPanel.add(corInput);

        inputSpecialistPanel = new JPanel();
        inputSpecialistPanel.setLayout(new GridLayout(1, 3, 25, 5));
        painelCriarBikes.add(inputSpecialistPanel);
        valorSpecialistInputLabel = new JLabel("Especialista:");
        specialistInput =  new JCheckBox();
        inputSpecialistPanel.add(valorSpecialistInputLabel);
        inputSpecialistPanel.add(specialistInput);

        inputButtonPanel = new JPanel();
        inputButtonPanel.setLayout(new GridLayout(1, 2, 25, 5));
        painelCriarBikes.add(inputButtonPanel);
        criarBike = new  JButton("Criar Bicicleta");
        criarBike.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!(marcaInput.getText().isEmpty() ||
                        modeloInput.getText().isEmpty()  ||
                        corInput.getText().isEmpty()  ||
                        aroInput.getSelectedItem().toString().isEmpty()  ||
                        aroMaterialInput.getSelectedItem().toString().isEmpty()  ||
                        bikeMaterialInput.getSelectedItem().toString().isEmpty()  ||
                        valorDiariaInput.getText().isEmpty()  ))
                {
                    try {

                    JOptionPane.showMessageDialog(janelaPrincipal, "Bicicleta criada!");
                    GerenciadorDeDados.bicicletas.add(new Bicicleta(
                            GerenciadorDeDados.bicicletas.toArray().length,
                            Integer.parseInt(aroInput.getSelectedItem().toString()),
                            Double.parseDouble(valorDiariaInput.getText()),
                            Double.parseDouble(valorSeguroInput.getText()),
                            specialistInput.isSelected(),
                            false,false,
                            new ArrayList<Promocao>(),
                            marcaInput.getText(),
                            modeloInput.getText(),
                            corInput.getText(),
                            aroMaterialInput.getSelectedItem().toString(),
                            bikeMaterialInput.getSelectedItem().toString()

                    ));




                    }catch (NumberFormatException ex) {
                        System.out.println(ex.getMessage());
                    }
                    GerenciadorDeDados.salvarDados("Bicicletas",GerenciadorDeDados.bicicletas);
                    AtualizarBikesDisponiveis();

                }else{
                    JOptionPane.showMessageDialog(janelaPrincipal, "Confira se todos os campos estão preenchidos!");
                }
            }
        });
        inputButtonPanel.add(criarBike);

    }

    //endregion Iniciar Painel de Criar Bikes


    //region Iniciar Painel de Criar Promoções

    private void configurarAreaPromocao(){

        painelPromocao = new JPanel();
        painelPromocao.setLayout(new GridLayout(2,1));
        JPanel painelPromocoesInput = new JPanel();
        painelPromocoesInput.setLayout(new GridLayout(2,2));
        painelPromocao.add(painelPromocoesInput);
        janelaPrincipal.add(painelPromocao);
        dataInicioPromocaoPanel = new JPanel();
        dataInicioPromocaoPanel.setLayout(new GridLayout(1,2));
        dataInicioPromocaoLabel = new JLabel("Data de Inicio:");
        dataInicioPromocao = new JDateChooser();
        painelPromocoesInput.add(dataInicioPromocaoPanel);
        dataInicioPromocaoPanel.add(dataInicioPromocaoLabel);
        dataInicioPromocaoPanel.add(dataInicioPromocao);

        dataFimPromocaoPanel = new JPanel();
        dataFimPromocaoPanel.setLayout(new GridLayout(1,2));
        dataFimPromocaoLabel = new JLabel("Data de Fim:");
        dataFimPromocao = new JDateChooser();
        painelPromocoesInput.add(dataFimPromocaoPanel);
        dataFimPromocaoPanel.add(dataFimPromocaoLabel);
        dataFimPromocaoPanel.add(dataFimPromocao);

        porcentagemPianel = new JPanel();
        porcentagemPianel.setLayout(new GridLayout(1,2));
        painelPromocoesInput.add(porcentagemPianel);
        porcentagemLabel = new JLabel("Porcentagem:");
        porcentagemPianel.add(porcentagemLabel);
        porcentagemInput = new JTextField(40);
        porcentagemPianel.add(porcentagemInput);

        estacavelPianel = new JPanel();
        estacavelPianel.setLayout(new GridLayout(1,3));
        painelPromocoesInput.add(estacavelPianel);
        estacavelLabel = new JLabel("Estacavel:");
        estacavelPianel.add(estacavelLabel);
        estacavelCheckbox = new JCheckBox();
        estacavelPianel.add(estacavelCheckbox);

        // O botão agora é "Criar Promoção"
        criarPromocao = new JButton("Criar Promoção");
        estacavelPianel.add(criarPromocao);

        // >>> INÍCIO DA CORREÇÃO <<<
        criarPromocao.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Validação simples dos campos
                if(dataInicioPromocao.getDate() == null || dataFimPromocao.getDate() == null || porcentagemInput.getText().trim().isEmpty()){
                    JOptionPane.showMessageDialog(janelaPrincipal, "Preencha todos os campos da promoção.", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (bikesSelecionadas.isEmpty()){
                    JOptionPane.showMessageDialog(janelaPrincipal, "Selecione ao menos uma bicicleta para a promoção.", "Aviso", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // 1. Aplica a promoção em cada bicicleta selecionada
                for(Bicicleta b: bikesSelecionadas){
                    try {
                        Promocao novaPromocao = new Promocao(
                                dataInicioPromocao.getDate(),
                                dataFimPromocao.getDate(),
                                Double.parseDouble(porcentagemInput.getText()),
                                estacavelCheckbox.isSelected()
                        );
                        b.getPromocoes().add(novaPromocao);
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(janelaPrincipal, "O valor da porcentagem é inválido.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
                        return; // Interrompe a operação se a porcentagem for inválida
                    }
                }

                // 2. Move os dados das bicicletas de volta para a lista de origem
                bicicletasTodasTemp.addAll(bikesSelecionadas);

                // 3. Limpa a lista de seleção
                bikesSelecionadas.clear();

                // 4. MANDA A INTERFACE SE ATUALIZAR com base nas listas de dados modificadas
                atualizarPainelBicicletas(innerPanelDisponiveisPromocao, innerPanelSelecionadasPromocao, bicicletasTodasTemp, bikesSelecionadas, "Adicionar Bicicleta", "Remover Bicicleta");
                atualizarPainelBicicletas(innerPanelSelecionadasPromocao, innerPanelDisponiveisPromocao, bikesSelecionadas, bicicletasTodasTemp, "Remover Bicicleta", "Adicionar Bicicleta");

                JOptionPane.showMessageDialog(janelaPrincipal, "Promoção aplicada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        // >>> FIM DA CORREÇÃO <<<
    }




    private void configurarPainelBicicletasPromocao() {
        painelBicicletasPromocao = new JPanel();
        painelBicicletasPromocao.setBorder(BorderFactory.createLineBorder(Color.black));
        painelBicicletasPromocao.setLayout(new GridLayout(1, 2));

        innerPanelDisponiveisPromocao = new JPanel();
        innerPanelDisponiveisPromocao.setLayout(new BoxLayout(innerPanelDisponiveisPromocao, BoxLayout.Y_AXIS));
        JScrollPane catalogoBicicletasDisponiveis = new JScrollPane(innerPanelDisponiveisPromocao);
        painelBicicletasPromocao.add(catalogoBicicletasDisponiveis, BorderLayout.CENTER);

        innerPanelSelecionadasPromocao = new JPanel();
        innerPanelSelecionadasPromocao.setLayout(new BoxLayout(innerPanelSelecionadasPromocao, BoxLayout.Y_AXIS));
        JScrollPane catalogoBicicletasSelecionadas = new JScrollPane(innerPanelSelecionadasPromocao);
        painelBicicletasPromocao.add(catalogoBicicletasSelecionadas, BorderLayout.CENTER);

        bicicletasTodasTemp.addAll(GerenciadorDeDados.bicicletas);
        atualizarPainelBicicletas(innerPanelDisponiveisPromocao, innerPanelSelecionadasPromocao,bicicletasTodasTemp,bikesSelecionadas,"Adicionar Bicicleta","Remover Bicicleta");
        atualizarPainelBicicletas(innerPanelSelecionadasPromocao, innerPanelDisponiveisPromocao,bikesSelecionadas, bicicletasTodasTemp,"Remover Bicicleta","Adicionar Bicicleta");


        painelPromocao.add(painelBicicletasPromocao);
    }


    //endregion Iniciar Painel de Criar Promoções




    private void abrirJanela(JPanel janelaAtiva) {
        painelAluguel.setVisible(false);
        painelCriarBikes.setVisible(false);
        painelPromocao.setVisible(false);
        janelaAtiva.setVisible(true);
    }




    // Métodos para criação dos cards de bicicletas

    private void aplicarCoresAoCard(Component componente, Color corFundo, Color corLetra) {
        componente.setBackground(corFundo);

        // Se o componente for um JLabel, mudamos a cor da letra
        if (componente instanceof JLabel) {
            componente.setForeground(corLetra);
        }

        // Se o componente for um container (como um JPanel), fazemos o mesmo para seus filhos
        if (componente instanceof Container) {
            for (Component filho : ((Container) componente).getComponents()) {
                aplicarCoresAoCard(filho, corFundo, corLetra);
            }
        }
    }

    public JPanel criarCardBicicleta(Bicicleta bicicleta, List<Bicicleta> origem, List<Bicicleta> destino,
                                     String textoBotao, String textoBotaoOposto,
                                     JPanel painelOrigem, JPanel painelDestino) {

        // --- NOSSO NOVO ESQUEMA DE CORES ---
        final Color COR_FUNDO_PADRAO = Color.BLACK;
        final Color COR_LETRA_PADRAO = new Color(255, 215, 0); // Amarelo Ouro (Gold)

        final Color COR_FUNDO_FOCO = new Color(255, 215, 0); // Amarelo Ouro (Gold)
        final Color COR_LETRA_FOCO = Color.BLACK;

        // Criação do card
        JPanel card = new JPanel();
        card.setPreferredSize(new Dimension(300, 90));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
        card.setLayout(new BorderLayout(5, 5));
        // A borda agora pode ser da cor do texto para um bom contraste
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COR_LETRA_PADRAO, 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        // Painel de informações interno
        JPanel painelInformacoes = new JPanel(new GridLayout(0, 2, 5, 2));
        painelInformacoes.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        // Adicionando os textos (a cor será definida pelo método "pintor")
        class LabelHelper {
            void adicionar(JPanel painel, String rotulo, Object valor) {
                Font fontePequena = new Font("Tahoma", Font.PLAIN, 11);
                JLabel labelRotulo = new JLabel(rotulo);
                labelRotulo.setFont(fontePequena);
                JLabel labelValor = new JLabel(valor.toString());
                labelValor.setFont(fontePequena);
                JPanel container = new JPanel(new BorderLayout(5, 0));
                container.add(labelRotulo, BorderLayout.WEST);
                container.add(labelValor, BorderLayout.CENTER);
                painel.add(container);
            }
        }
        new LabelHelper().adicionar(painelInformacoes, "ID:", bicicleta.getIdBicicleta());
        new LabelHelper().adicionar(painelInformacoes, "Preço:", String.format("R$ %.2f", bicicleta.getValorAluguel()));
        new LabelHelper().adicionar(painelInformacoes, "Marca:", bicicleta.getMarca());
        new LabelHelper().adicionar(painelInformacoes, "Modelo:", bicicleta.getModelo());

        card.add(painelInformacoes, BorderLayout.CENTER);

        // --- ESTADO INICIAL ---
        // Pinta o card com as cores padrão (fundo preto, letras douradas)
        aplicarCoresAoCard(card, COR_FUNDO_PADRAO, COR_LETRA_PADRAO);

        // --- GERENCIADOR DE CLIQUES ATUALIZADO ---
        MouseAdapter clickManager = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    cardAtualmenteFocado = null;
                    moverBicicleta(bicicleta.getIdBicicleta(), origem, destino, painelOrigem, painelDestino, textoBotao, textoBotaoOposto);
                    return;
                }

                if (e.getClickCount() == 1) {
                    // Se já existe um card focado e não é o atual, restaura sua cor padrão.
                    if (cardAtualmenteFocado != null && cardAtualmenteFocado != card) {
                        aplicarCoresAoCard(cardAtualmenteFocado, COR_FUNDO_PADRAO, COR_LETRA_PADRAO);
                        // Atualiza a borda também
                        cardAtualmenteFocado.setBorder(BorderFactory.createCompoundBorder(
                                BorderFactory.createLineBorder(COR_LETRA_PADRAO, 1),
                                BorderFactory.createEmptyBorder(5, 5, 5, 5)
                        ));
                    }

                    // Aplica as cores de foco no card que acabou de ser clicado.
                    aplicarCoresAoCard(card, COR_FUNDO_FOCO, COR_LETRA_FOCO);
                    // Atualiza a borda para a cor de foco
                    card.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(COR_LETRA_FOCO, 1),
                            BorderFactory.createEmptyBorder(5, 5, 5, 5)
                    ));

                    // Atualiza a referência do card focado.
                    cardAtualmenteFocado = card;
                }
            }
        };

        card.addMouseListener(clickManager);

        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return card;
    }




    private void adicionarLabelFormatada(JPanel painel, String rotulo, Object valor) {
        JPanel container = new JPanel(new BorderLayout(5, 0));
        container.add(new JLabel(rotulo), BorderLayout.WEST);
        container.add(new JLabel(valor.toString()), BorderLayout.CENTER);
        painel.add(container);
    }
    private void moverBicicleta(int idBicicleta, List<Bicicleta> origem, List<Bicicleta> destino,
                                JPanel painelOrigem, JPanel painelDestino,
                                String textoBotao, String textoBotaoOposto) {
        origem.stream()
                .filter(b -> b.getIdBicicleta() == idBicicleta)
                .findFirst()
                .ifPresent(bicicleta -> {
                    destino.add(bicicleta);
                    origem.remove(bicicleta);
                    atualizarPainelBicicletas(painelOrigem, painelDestino, origem, destino, textoBotao, textoBotaoOposto);
                    atualizarPainelBicicletas(painelDestino,painelOrigem, destino, origem, textoBotaoOposto, textoBotao);
                });
    }
    public void atualizarPainelBicicletas(JPanel painel, JPanel painel2, List<Bicicleta> bicicletas,
                                          List<Bicicleta> bicicletasOpostas,
                                          String textoBotao, String textoBotaoOposto) {
        painel.removeAll();

        // Ordenar bicicletas por ID
        bicicletas.sort(Comparator.comparingInt(Bicicleta::getIdBicicleta));

        // Adicionar cards atualizados
        for (Bicicleta bicicleta : bicicletas) {
            painel.add(criarCardBicicleta(
                    bicicleta,
                    bicicletas,
                    bicicletasOpostas,
                    textoBotao,
                    textoBotaoOposto,
                    painel,painel2
                    // Encontrar o painel oposto dinamicamente
            ));
        }

        painel.revalidate();
        painel.repaint();
    }

    public static void AtualizarBikesDisponiveis() {
        bicicletasDisponiveis.clear();
        bicicletasDisponiveis.addAll(
                GerenciadorDeDados.bicicletas.stream()
                        .filter(b -> !b.isAlugada() && !b.isEmManutencao())
                        .sorted(Comparator.comparingInt(Bicicleta::getIdBicicleta))
                        .collect(Collectors.toList())
        );
    }

    public void efetuarAluguel() {
        // Validação de dias
        if (diasInput.getSelectedIndex() <= 0) {
            diasErrorMsg.setVisible(true);
            return;
        }
        diasErrorMsg.setVisible(false);

        // Validação de cliente
        if (comboBoxClientes.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(janelaPrincipal, "Selecione um cliente!");
            return;
        }

        // Validação de bicicletas selecionadas
        if (bicicletasAlugadas.isEmpty()) {
            JOptionPane.showMessageDialog(janelaPrincipal, "Selecione pelo menos uma bicicleta!");
            return;
        }

        try {
            String clienteCombobox = comboBoxClientes.getSelectedItem().toString();
            String CPF = clienteCombobox.split(" : ")[1];

            Cliente clienteAlugando = GerenciadorDeDados.clientes.stream()
                    .filter(c -> c.getCpf().equals(CPF))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

            int diasAlugados = Integer.parseInt(diasInput.getSelectedItem().toString());

            int novoId = GerenciadorDeDados.alugueis.isEmpty() ? 1 :
                    Collections.max(GerenciadorDeDados.alugueis, Comparator.comparing(Aluguel::getIdAluguel)).getIdAluguel() + 1;

            Aluguel novoAluguel = new Aluguel(novoId, diasAlugados, clienteAlugando,
                    new ArrayList<>(bicicletasAlugadas), new Date());

            GerenciadorDeDados.alugueis.add(novoAluguel);
            GerenciadorDeDados.salvarDados("Alugueis", GerenciadorDeDados.alugueis);

            // Atualizando status das bicicletas
            bicicletasAlugadas.forEach(b -> {
                int index = GerenciadorDeDados.bicicletas.indexOf(b);
                if (index >= 0) {
                    GerenciadorDeDados.bicicletas.get(index).setAlugada(true);
                }
            });

            // Limpando seleção
            bicicletasAlugadas.clear();
            AtualizarBikesDisponiveis();
            GerenciadorDeDados.salvarDados("Bicicletas", GerenciadorDeDados.bicicletas);

            JOptionPane.showMessageDialog(janelaPrincipal, "Aluguel realizado com sucesso!");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(janelaPrincipal,
                    "Erro ao efetuar aluguel: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}