package com.paintspray.controller;

import com.paintspray.model.Cliente;
import com.paintspray.model.Veiculo;
import com.paintspray.service.ClienteService;
import com.paintspray.service.VeiculoService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.javafx.FontIcon;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Controller da Central de Clientes e Veículos (SplitPane - Mestre/Detalhe)
 */
public class ClienteController {

    @FXML
    private TextField searchField;

    @FXML
    private TableView<Cliente> clienteTable;

    @FXML
    private TableColumn<Cliente, String> idColumn;

    @FXML
    private TableColumn<Cliente, String> nomeColumn;

    @FXML
    private TableColumn<Cliente, String> telefoneColumn;

    @FXML
    private VBox detalhesPane;

    @FXML
    private Label clienteNomeLabel;

    @FXML
    private Label clienteEnderecoLabel;

    @FXML
    private Label clienteTelefoneLabel;

    @FXML
    private VBox veiculosContainer;

    @FXML
    private Button editarClienteBtn;

    @FXML
    private Button excluirClienteBtn;

    @FXML
    private Button adicionarVeiculoBtn;

    private final ClienteService clienteService = new ClienteService();
    private final VeiculoService veiculoService = new VeiculoService();
    private ObservableList<Cliente> clientes = FXCollections.observableArrayList();
    private Cliente clienteSelecionado;

    @FXML
    private void initialize() {
        // Configura colunas da tabela
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomeColumn.setCellValueFactory(new PropertyValueFactory<>("nome"));
        telefoneColumn.setCellValueFactory(new PropertyValueFactory<>("numeroTelefone"));

        // Listener para seleção de cliente
        clienteTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                clienteSelecionado = newSelection;
                carregarDetalhesCliente(newSelection);
            }
        });

        // Busca ao digitar
        searchField.textProperty().addListener((obs, oldValue, newValue) -> filtrarClientes(newValue));

        // Carrega clientes
        carregarClientes();

        // Desabilita detalhes inicialmente
        detalhesPane.setDisable(true);
    }

    private void carregarClientes() {
        try {
            List<Cliente> listaClientes = clienteService.listarTodos();
            clientes.clear();
            clientes.addAll(listaClientes);
            clienteTable.setItems(clientes);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao carregar clientes: " + e.getMessage());
        }
    }

    private void filtrarClientes(String filtro) {
        if (filtro == null || filtro.isEmpty()) {
            carregarClientes();
            return;
        }

        try {
            List<Cliente> todos = clienteService.listarTodos();
            List<Cliente> filtrados = todos.stream()
                    .filter(c -> c.getNome().toLowerCase().contains(filtro.toLowerCase()) ||
                            c.getNumeroTelefone().contains(filtro))
                    .toList();

            clientes.clear();
            clientes.addAll(filtrados);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao filtrar clientes: " + e.getMessage());
        }
    }

    private void carregarDetalhesCliente(Cliente cliente) {
        detalhesPane.setDisable(false);

        // Atualiza informações do cliente
        clienteNomeLabel.setText(cliente.getNome());
        clienteEnderecoLabel.setText(cliente.getEndereco());
        clienteTelefoneLabel.setText(cliente.getNumeroTelefone());

        // Carrega veículos do cliente
        carregarVeiculosCliente(cliente.getId());
    }

    private void carregarVeiculosCliente(String clienteId) {
        veiculosContainer.getChildren().clear();

        try {
            List<Veiculo> veiculos = veiculoService.listarVeiculosPorCliente(clienteId);

            if (veiculos.isEmpty()) {
                Label emptyLabel = new Label("Nenhum veículo cadastrado");
                emptyLabel.setStyle("-fx-text-fill: #95A5A6; -fx-font-style: italic;");
                veiculosContainer.getChildren().add(emptyLabel);
            } else {
                for (Veiculo veiculo : veiculos) {
                    veiculosContainer.getChildren().add(criarVeiculoCard(veiculo));
                }
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao carregar veículos: " + e.getMessage());
        }
    }

    private HBox criarVeiculoCard(Veiculo veiculo) {
        HBox card = new HBox(15);
        card.getStyleClass().add("card-pane");
        card.setPadding(new Insets(12));
        card.setAlignment(Pos.CENTER_LEFT);

        // Ícone
        FontIcon icon = new FontIcon("mdi2m-motorbike");
        icon.setIconSize(32);
        icon.setIconColor(javafx.scene.paint.Color.web("#2980B9"));

        // Informações
        VBox info = new VBox(5);
        HBox.setHgrow(info, Priority.ALWAYS);

        Label modeloLabel = new Label(veiculo.getModelo());
        modeloLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        Label detalhesLabel = new Label(
                String.format("%s • %d", veiculo.getCor(), veiculo.getAnoFabricacao()));
        detalhesLabel.setStyle("-fx-text-fill: #7F8C8D; -fx-font-size: 12px;");

        info.getChildren().addAll(modeloLabel, detalhesLabel);

        // Botões de ação
        Button editBtn = new Button();
        editBtn.setGraphic(new FontIcon("mdi2p-pencil"));
        editBtn.getStyleClass().add("button-secondary");
        editBtn.setOnAction(e -> editarVeiculo(veiculo));

        Button deleteBtn = new Button();
        deleteBtn.setGraphic(new FontIcon("mdi2d-delete"));
        deleteBtn.getStyleClass().add("button-danger");
        deleteBtn.setOnAction(e -> excluirVeiculo(veiculo));

        HBox actions = new HBox(5, editBtn, deleteBtn);

        card.getChildren().addAll(icon, info, actions);
        return card;
    }

    @FXML
    private void handleNovoCliente() {
        Dialog<Cliente> dialog = criarDialogCliente(null);
        Optional<Cliente> result = dialog.showAndWait();

        result.ifPresent(cliente -> {
            try {
                clienteService.cadastrarCliente(cliente);
                carregarClientes();
                showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Cliente cadastrado com sucesso!");
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao cadastrar cliente: " + e.getMessage());
            }
        });
    }

    @FXML
    private void handleEditarCliente() {
        if (clienteSelecionado == null)
            return;

        Dialog<Cliente> dialog = criarDialogCliente(clienteSelecionado);
        Optional<Cliente> result = dialog.showAndWait();

        result.ifPresent(cliente -> {
            try {
                clienteService.atualizarCliente(cliente);
                carregarClientes();
                carregarDetalhesCliente(cliente);
                showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Cliente atualizado com sucesso!");
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao atualizar cliente: " + e.getMessage());
            }
        });
    }

    @FXML
    private void handleExcluirCliente() {
        if (clienteSelecionado == null)
            return;

        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacao.setTitle("Confirmar Exclusão");
        confirmacao.setHeaderText("Deseja realmente excluir este cliente?");
        confirmacao.setContentText("Esta ação não pode ser desfeita.\n" +
                "Todos os veículos e serviços associados também serão excluídos.");

        Optional<ButtonType> resultado = confirmacao.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            try {
                clienteService.removerCliente(clienteSelecionado.getId());
                carregarClientes();
                detalhesPane.setDisable(true);
                showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Cliente excluído com sucesso!");
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao excluir cliente: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleAdicionarVeiculo() {
        if (clienteSelecionado == null)
            return;

        Dialog<Veiculo> dialog = criarDialogVeiculo(null, clienteSelecionado.getId());
        Optional<Veiculo> result = dialog.showAndWait();

        result.ifPresent(veiculo -> {
            try {
                veiculoService.cadastrarVeiculo(veiculo);
                carregarVeiculosCliente(clienteSelecionado.getId());
                showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Veículo cadastrado com sucesso!");
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao cadastrar veículo: " + e.getMessage());
            }
        });
    }

    private void editarVeiculo(Veiculo veiculo) {
        Dialog<Veiculo> dialog = criarDialogVeiculo(veiculo, clienteSelecionado.getId());
        Optional<Veiculo> result = dialog.showAndWait();

        result.ifPresent(v -> {
            try {
                veiculoService.atualizarVeiculo(v);
                carregarVeiculosCliente(clienteSelecionado.getId());
                showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Veículo atualizado com sucesso!");
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao atualizar veículo: " + e.getMessage());
            }
        });
    }

    private void excluirVeiculo(Veiculo veiculo) {
        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacao.setTitle("Confirmar Exclusão");
        confirmacao.setHeaderText("Deseja realmente excluir este veículo?");
        confirmacao.setContentText("Os serviços associados também serão excluídos.");

        Optional<ButtonType> resultado = confirmacao.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            try {
                veiculoService.removerVeiculo(veiculo.getId());
                carregarVeiculosCliente(clienteSelecionado.getId());
                showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Veículo excluído com sucesso!");
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao excluir veículo: " + e.getMessage());
            }
        }
    }

    private Dialog<Cliente> criarDialogCliente(Cliente cliente) {
        Dialog<Cliente> dialog = new Dialog<>();
        dialog.setTitle(cliente == null ? "Novo Cliente" : "Editar Cliente");
        dialog.setHeaderText(null);

        ButtonType salvarButton = new ButtonType("Salvar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(salvarButton, ButtonType.CANCEL);

        // Formulário
        VBox form = new VBox(10);
        form.setPadding(new Insets(20));

        TextField idField = new TextField(cliente != null ? cliente.getId() : "");
        idField.setPromptText("CPF ou ID");
        idField.setDisable(cliente != null); // ID não editável

        TextField nomeField = new TextField(cliente != null ? cliente.getNome() : "");
        nomeField.setPromptText("Nome completo");

        TextField enderecoField = new TextField(cliente != null ? cliente.getEndereco() : "");
        enderecoField.setPromptText("Endereço");

        TextField telefoneField = new TextField(cliente != null ? cliente.getNumeroTelefone() : "");
        telefoneField.setPromptText("Telefone");

        form.getChildren().addAll(
                new Label("ID/CPF:"), idField,
                new Label("Nome:"), nomeField,
                new Label("Endereço:"), enderecoField,
                new Label("Telefone:"), telefoneField);

        dialog.getDialogPane().setContent(form);

        // Converter resultado
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == salvarButton) {
                return new Cliente(
                        idField.getText(),
                        nomeField.getText(),
                        enderecoField.getText(),
                        telefoneField.getText());
            }
            return null;
        });

        return dialog;
    }

    private Dialog<Veiculo> criarDialogVeiculo(Veiculo veiculo, String clienteId) {
        Dialog<Veiculo> dialog = new Dialog<>();
        dialog.setTitle(veiculo == null ? "Novo Veículo" : "Editar Veículo");
        dialog.setHeaderText(null);

        ButtonType salvarButton = new ButtonType("Salvar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(salvarButton, ButtonType.CANCEL);

        // Formulário
        VBox form = new VBox(10);
        form.setPadding(new Insets(20));

        TextField modeloField = new TextField(veiculo != null ? veiculo.getModelo() : "");
        modeloField.setPromptText("Modelo da moto");

        TextField corField = new TextField(veiculo != null ? veiculo.getCor() : "");
        corField.setPromptText("Cor");

        TextField anoField = new TextField(veiculo != null ? String.valueOf(veiculo.getAnoFabricacao()) : "");
        anoField.setPromptText("Ano de fabricação");

        form.getChildren().addAll(
                new Label("Modelo:"), modeloField,
                new Label("Cor:"), corField,
                new Label("Ano:"), anoField);

        dialog.getDialogPane().setContent(form);

        // Converter resultado
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == salvarButton) {
                return new Veiculo(
                        veiculo != null ? veiculo.getId() : 0,
                        modeloField.getText(),
                        corField.getText(),
                        Integer.parseInt(anoField.getText()),
                        clienteId);
            }
            return null;
        });

        return dialog;
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
