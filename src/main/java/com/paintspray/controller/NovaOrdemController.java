package com.paintspray.controller;

import com.paintspray.enums.FormaPagamento;
import com.paintspray.enums.TipoServico;
import com.paintspray.model.Cliente;
import com.paintspray.model.Usuario;
import com.paintspray.model.Veiculo;
import com.paintspray.service.ClienteService;
import com.paintspray.service.ServicoService;
import com.paintspray.service.VeiculoService;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.kordamp.ikonli.javafx.FontIcon;

import java.sql.SQLException;
import java.util.List;

/**
 * Controller do formulário de Nova Ordem de Serviço (Wizard em 3 etapas)
 */
public class NovaOrdemController {

    @FXML
    private VBox wizardContainer;

    @FXML
    private Label stepLabel;

    @FXML
    private GridPane step1Grid;

    @FXML
    private GridPane step2Grid;

    @FXML
    private GridPane step3Grid;

    // Step 1: Cliente e Veículo
    @FXML
    private ComboBox<Cliente> clienteCombo;

    @FXML
    private ComboBox<Veiculo> veiculoCombo;

    @FXML
    private Button novoClienteBtn;

    // Step 2: Serviço
    @FXML
    private ComboBox<TipoServico> tipoServicoCombo;

    @FXML
    private TextArea descricaoArea;

    // Step 3: Financeiro
    @FXML
    private TextField precoField;

    @FXML
    private ComboBox<FormaPagamento> formaPagamentoCombo;

    // Navegação
    @FXML
    private Button voltarBtn;

    @FXML
    private Button proximoBtn;

    @FXML
    private Button finalizarBtn;

    private int etapaAtual = 1;
    private final ClienteService clienteService = new ClienteService();
    private final VeiculoService veiculoService = new VeiculoService();
    private final ServicoService servicoService = new ServicoService();

    @FXML
    private void initialize() {
        // Configura ComboBoxes
        tipoServicoCombo.getItems().addAll(TipoServico.values());
        formaPagamentoCombo.getItems().addAll(FormaPagamento.values());

        // Configura display dos ComboBoxes
        clienteCombo.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Cliente cliente, boolean empty) {
                super.updateItem(cliente, empty);
                setText(empty || cliente == null ? null : cliente.getNome());
            }
        });
        clienteCombo.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Cliente cliente, boolean empty) {
                super.updateItem(cliente, empty);
                setText(empty || cliente == null ? "Selecione um cliente" : cliente.getNome());
            }
        });

        veiculoCombo.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Veiculo veiculo, boolean empty) {
                super.updateItem(veiculo, empty);
                setText(empty || veiculo == null ? null
                        : String.format("%s - %s (%d)", veiculo.getModelo(), veiculo.getCor(),
                                veiculo.getAnoFabricacao()));
            }
        });
        veiculoCombo.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Veiculo veiculo, boolean empty) {
                super.updateItem(veiculo, empty);
                setText(empty || veiculo == null ? "Selecione um veículo"
                        : String.format("%s - %s", veiculo.getModelo(), veiculo.getCor()));
            }
        });

        tipoServicoCombo.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(TipoServico tipo, boolean empty) {
                super.updateItem(tipo, empty);
                setText(empty || tipo == null ? null : tipo.getDescricao());
            }
        });
        tipoServicoCombo.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(TipoServico tipo, boolean empty) {
                super.updateItem(tipo, empty);
                setText(empty || tipo == null ? "Selecione o tipo" : tipo.getDescricao());
            }
        });

        formaPagamentoCombo.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(FormaPagamento forma, boolean empty) {
                super.updateItem(forma, empty);
                setText(empty || forma == null ? null : forma.getDescricao());
            }
        });
        formaPagamentoCombo.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(FormaPagamento forma, boolean empty) {
                super.updateItem(forma, empty);
                setText(empty || forma == null ? "Selecione a forma" : forma.getDescricao());
            }
        });

        // Listener para carregar veículos quando cliente for selecionado
        clienteCombo.setOnAction(e -> carregarVeiculosDoCliente());

        // Carrega clientes
        carregarClientes();

        // Inicia no step 1
        mostrarEtapa(1);
    }

    private void carregarClientes() {
        try {
            List<Cliente> clientes = clienteService.listarTodos();
            clienteCombo.getItems().clear();
            clienteCombo.getItems().addAll(clientes);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao carregar clientes: " + e.getMessage());
        }
    }

    private void carregarVeiculosDoCliente() {
        Cliente clienteSelecionado = clienteCombo.getValue();
        if (clienteSelecionado == null) {
            veiculoCombo.getItems().clear();
            veiculoCombo.setDisable(true);
            return;
        }

        try {
            List<Veiculo> veiculos = veiculoService.listarVeiculosPorCliente(clienteSelecionado.getId());
            veiculoCombo.getItems().clear();
            veiculoCombo.getItems().addAll(veiculos);
            veiculoCombo.setDisable(veiculos.isEmpty());

            if (veiculos.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Aviso",
                        "Este cliente não possui veículos cadastrados.\nCadastre um veículo primeiro.");
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao carregar veículos: " + e.getMessage());
        }
    }

    @FXML
    private void handleNovoCliente() {
        // Abre dialog de novo cliente (similar ao ClienteController)
        Alert info = new Alert(Alert.AlertType.INFORMATION);
        info.setTitle("Em Desenvolvimento");
        info.setHeaderText(null);
        info.setContentText("Funcionalidade de cadastro rápido em desenvolvimento.\n" +
                "Use a Central de Clientes para cadastrar.");
        info.showAndWait();
    }

    @FXML
    private void handleVoltar() {
        if (etapaAtual > 1) {
            mostrarEtapa(etapaAtual - 1);
        }
    }

    @FXML
    private void handleProximo() {
        if (validarEtapaAtual()) {
            if (etapaAtual < 3) {
                mostrarEtapa(etapaAtual + 1);
            }
        }
    }

    @FXML
    private void handleFinalizar() {
        if (!validarEtapaAtual()) {
            return;
        }

        try {
            // Coleta dados
            Veiculo veiculo = veiculoCombo.getValue();
            TipoServico tipo = tipoServicoCombo.getValue();
            String descricao = descricaoArea.getText();
            double preco = Double.parseDouble(precoField.getText());
            FormaPagamento formaPagamento = formaPagamentoCombo.getValue();
            Usuario usuario = SessionManager.getUsuarioLogado();

            // Cadastra serviço
            servicoService.cadastrarServico(tipo, descricao, preco, formaPagamento, veiculo, usuario);

            // Sucesso
            Alert sucesso = new Alert(Alert.AlertType.INFORMATION);
            sucesso.setTitle("Sucesso");
            sucesso.setHeaderText("Ordem de Serviço Criada!");
            sucesso.setContentText("A ordem foi cadastrada com status PENDENTE.\n" +
                    "Você pode acompanhá-la no Dashboard.");
            sucesso.showAndWait();

            // Fecha o dialog
            fecharDialog();

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Preço inválido. Use apenas números.");
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao criar ordem: " + e.getMessage());
        }
    }

    @FXML
    private void handleCancelar() {
        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacao.setTitle("Cancelar");
        confirmacao.setHeaderText("Deseja realmente cancelar?");
        confirmacao.setContentText("Os dados preenchidos serão perdidos.");

        if (confirmacao.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            fecharDialog();
        }
    }

    private void mostrarEtapa(int etapa) {
        etapaAtual = etapa;

        // Esconde todas as etapas
        step1Grid.setVisible(false);
        step1Grid.setManaged(false);
        step2Grid.setVisible(false);
        step2Grid.setManaged(false);
        step3Grid.setVisible(false);
        step3Grid.setManaged(false);

        // Mostra etapa atual
        switch (etapa) {
            case 1:
                stepLabel.setText("Etapa 1 de 3: Cliente e Veículo");
                step1Grid.setVisible(true);
                step1Grid.setManaged(true);
                voltarBtn.setDisable(true);
                proximoBtn.setVisible(true);
                finalizarBtn.setVisible(false);
                break;
            case 2:
                stepLabel.setText("Etapa 2 de 3: Tipo de Serviço");
                step2Grid.setVisible(true);
                step2Grid.setManaged(true);
                voltarBtn.setDisable(false);
                proximoBtn.setVisible(true);
                finalizarBtn.setVisible(false);
                break;
            case 3:
                stepLabel.setText("Etapa 3 de 3: Informações Financeiras");
                step3Grid.setVisible(true);
                step3Grid.setManaged(true);
                voltarBtn.setDisable(false);
                proximoBtn.setVisible(false);
                finalizarBtn.setVisible(true);
                break;
        }
    }

    private boolean validarEtapaAtual() {
        switch (etapaAtual) {
            case 1:
                if (clienteCombo.getValue() == null) {
                    showAlert(Alert.AlertType.WARNING, "Atenção", "Selecione um cliente.");
                    return false;
                }
                if (veiculoCombo.getValue() == null) {
                    showAlert(Alert.AlertType.WARNING, "Atenção", "Selecione um veículo.");
                    return false;
                }
                return true;

            case 2:
                if (tipoServicoCombo.getValue() == null) {
                    showAlert(Alert.AlertType.WARNING, "Atenção", "Selecione o tipo de serviço.");
                    return false;
                }
                if (descricaoArea.getText().trim().isEmpty()) {
                    showAlert(Alert.AlertType.WARNING, "Atenção", "Informe a descrição do serviço.");
                    return false;
                }
                return true;

            case 3:
                if (precoField.getText().trim().isEmpty()) {
                    showAlert(Alert.AlertType.WARNING, "Atenção", "Informe o preço.");
                    return false;
                }
                try {
                    double preco = Double.parseDouble(precoField.getText());
                    if (preco < 0) {
                        showAlert(Alert.AlertType.WARNING, "Atenção", "Preço não pode ser negativo.");
                        return false;
                    }
                } catch (NumberFormatException e) {
                    showAlert(Alert.AlertType.WARNING, "Atenção", "Preço inválido.");
                    return false;
                }
                if (formaPagamentoCombo.getValue() == null) {
                    showAlert(Alert.AlertType.WARNING, "Atenção", "Selecione a forma de pagamento.");
                    return false;
                }
                return true;

            default:
                return false;
        }
    }

    private void fecharDialog() {
        Stage stage = (Stage) wizardContainer.getScene().getWindow();
        stage.close();
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
