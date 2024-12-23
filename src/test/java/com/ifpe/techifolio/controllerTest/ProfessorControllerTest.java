package com.ifpe.techifolio.controllerTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ifpe.techifolio.controller.ProfessorController;
import com.ifpe.techifolio.entities.Professor;
import com.ifpe.techifolio.repository.ProfessorRepository;

import org.springframework.http.MediaType;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(ProfessorController.class)
public class ProfessorControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Mock
    private ProfessorRepository professorRepository;

    @InjectMocks
    private ProfessorController professorController;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateProfessorSucessTC013() throws Exception {
        ObjectId id = new ObjectId();
        Professor professor = new Professor(id, "João", "joao@mail.com","123456", "ABC");
        //Define o comportamento do mock: quando o método save for chamado, retorne o objeto professor
        when(professorRepository.save(any(Professor.class))).thenReturn(professor);

        mockMvc.perform(post("/professores")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(professor)))
                .andExpect(status().isCreated()) // Verifica se o status da resposta é 201 Created
                .andExpect(jsonPath("$.nome").value(professor.getNome())) // Verifica o conteúdo da resposta
                .andExpect(jsonPath("$.email").value(professor.getEmail()))
                .andExpect(jsonPath("$.senha").value(professor.getSenha()))
                .andExpect(jsonPath("$.faculdade").value(professor.getFaculdade())); 
    }

    @Test
    public void testCreateProfessorErrorDuplicateEmailTC014() throws Exception {
        ObjectId id = new ObjectId();
        Professor professor = new Professor(id, "João", "joao@mail.com","123456", "ABC");

        //Define o comportamento do mock: quando o método findByEmail for chamado, retorne o objeto professor
        when(professorRepository.findByEmail("joao@mail.com")).thenReturn(professor);

        mockMvc.perform(post("/professores")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(professor)))
                .andExpect(status().isConflict()) // Verifica se o status da resposta é 409 Conflict
                .andExpect(jsonPath("$.message").value("Erro: Já existe um professor cadastrado com o email informado.")) // Verifica o conteúdo da resposta
                .andExpect(jsonPath("$.receivedObject.nome").value(professor.getNome())) // Verifica o conteúdo da resposta
                .andExpect(jsonPath("$.receivedObject.email").value(professor.getEmail()))
                .andExpect(jsonPath("$.receivedObject.senha").value(professor.getSenha()))
                .andExpect(jsonPath("$.receivedObject.faculdade").value(professor.getFaculdade())); 
    }

    @Test
    public void testCreateProfessorErrorNullFieldNomeTC015() throws Exception {
        ObjectId id = new ObjectId();
        Professor professor = new Professor(id, null, "joao@mail.com","123456", "ABC");

        //Define o comportamento do mock: quando o método getNullFieldMessageProfessor for chamado, retorne a string "Nome não pode ser nulo. "
        when(professor.getNullFieldMessageProfessor()).thenReturn("Nome não pode ser nulo. ");

        mockMvc.perform(post("/professores")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(professor)))
                .andExpect(status().isBadRequest()) // Verifica se o status da resposta é 400 Bad Request
                .andExpect(jsonPath("$.message").value("Erro: Nome não pode ser nulo. ")) // Verifica o conteúdo da resposta
                .andExpect(jsonPath("$.receivedObject.nome").value(professor.getNome())) // Verifica o conteúdo da resposta
                .andExpect(jsonPath("$.receivedObject.email").value(professor.getEmail()))
                .andExpect(jsonPath("$.receivedObject.senha").value(professor.getSenha()))
                .andExpect(jsonPath("$.receivedObject.faculdade").value(professor.getFaculdade()));
    }
    @Test
    public void testGetProfessorById() throws Exception {
        // Define o comportamento do mock: quando o método findById for chamado, retorne um Optional contendo o professor
        when(professorRepository.findById(id)).thenReturn(Optional.of(professor));

        // Simula uma requisição GET para obter um professor pelo ID
        mockMvc.perform(get("/professores/{id}", id.toHexString()))
                .andExpect(status().isOk()) // Verifica se o status da resposta é 200 OK
                .andExpect(jsonPath("$.nome").value(professor.getNome())); // Verifica o conteúdo da resposta
    }

    @Test
    public void testUpdateProfessor() throws Exception {
        // Define o comportamento do mock: quando o método findById for chamado, retorne um Optional contendo o professor
        when(professorRepository.findById(id)).thenReturn(Optional.of(professor));
        // Define o comportamento do mock: quando o método save for chamado, retorne o objeto professor
        when(professorRepository.save(any(Professor.class))).thenReturn(professor);

        // Simula uma requisição PUT para atualizar um professor
        mockMvc.perform(put("/professores/{id}", id.toHexString())
                .contentType(MediaType.APPLICATION_JSON) // Define o tipo de conteúdo da requisição
                .content(new ObjectMapper().writeValueAsString(professor))) // Define o corpo da requisição
                .andExpect(status().isOk()) // Verifica se o status da resposta é 200 OK
                .andExpect(content().string("Professor atualizado com sucesso")); // Verifica o conteúdo da resposta
    }

    @Test
    public void testDeleteProfessor() throws Exception {
        // Define o comportamento do mock: quando o método findById for chamado, retorne um Optional contendo o professor
        when(professorRepository.findById(id)).thenReturn(Optional.of(professor));

        // Simula uma requisição DELETE para deletar um professor
        mockMvc.perform(delete("/professores/{id}", id.toHexString()))
                .andExpect(status().isNoContent()); // Verifica se o status da resposta é 204 No Content
    }

    @Test
    public void testLogin() throws Exception {
        // Define o comportamento do mock: quando o método findByEmailAndSenha for chamado, retorne o objeto professor
        when(professorRepository.findByEmailAndSenha(professor.getEmail(), professor.getSenha())).thenReturn(professor);

        // Simula uma requisição POST para login
        mockMvc.perform(post("/professores/login")
                .param("email", professor.getEmail()) // Define o parâmetro de email
                .param("senha", professor.getSenha())) // Define o parâmetro de senha
                .andExpect(status().isOk()) // Verifica se o status da resposta é 200 OK
                .andExpect(jsonPath("$.nome").value(professor.getNome())); // Verifica o conteúdo da resposta
    }
}
